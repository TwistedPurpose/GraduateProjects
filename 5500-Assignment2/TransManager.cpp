// CPSC 5500 - Atomic Transactions: transaction manager

#include "TransManager.h"

// Constructor
TransManager::TransManager(int numThreads)
{

	globalTransactionCounter = 0;
	numberOfThreads = numThreads;

	diskLocks = new pthread_mutex_t[50];
	for (int i = 0; i < 50; i++)
	{
		pthread_mutex_init(&diskLocks[i], NULL);
	}
	pthread_mutex_init(&globalTransactionCounterLock, NULL);
}

// Destructor
TransManager::~TransManager()
{
}

// reset: Resets the manager:
// - Formats the disk.
// - Clears out the log.
void TransManager::reset()
{
	disk.formatDisk();
	log.clearLog();
}

// recover: Recover from a crash:
// - Resets the disk to a sane state such that unfinished transactions
// are rolled back.
// - Returns a dynamically allocated array the id of the next transaction
// to run for each thread.
// - If the log file does not exist, then call reset and return an array
// such that each threads starts with transaction 0.
int* TransManager::recover()
{
	int * lastThreadTransactionsCompleted = new int[numberOfThreads];

	for (int i = 0; i < numberOfThreads; i++)
	{
		lastThreadTransactionsCompleted[i] = 0;
	}

	if (log.doesLogExist())
	{
		globalTransactionCounter = log.getGlobalTransactionNumber();

		for (int i = 0; i < numberOfThreads; i++)
		{
			ifstream logFile("LOG");
			string line;
			int currentTransactionId = -1;
			bool currentTransactionCompleted = true;

			while (getline(logFile, line))
			{
				string buffer;
				stringstream ss(line);
				vector<string> logItems;

				while (ss >> buffer)
				{
					logItems.push_back(buffer);
				}

				string commandType = logItems[0];

				if (commandType.compare("BEGIN") == 0 && logItems[2].compare(log.to_string(i)) == 0)
				{
					
					currentTransactionId = atoi(logItems[1].c_str());
					currentTransactionCompleted = false;

				} else if ((commandType.compare("COMMITTED") == 0 ||
					commandType.compare("ABORTED") == 0 || commandType.compare("NEVER_FINISHED") == 0)
					&& atoi(logItems[1].c_str()) == currentTransactionId)
				{
					currentTransactionCompleted = true;

					if (commandType.compare("COMMITTED") == 0 || commandType.compare("ABORTED") == 0)
					{
						lastThreadTransactionsCompleted[i] = lastThreadTransactionsCompleted[i] + 1;
					}
				}
			}

			logFile.close();

			// Perform Rollback
			if (!currentTransactionCompleted)
			{
				vector<pair<int,int> > updates;

				ifstream rolebackLogFile("LOG");
				string rollbackLine;

				while (getline(rolebackLogFile, rollbackLine))
				{
					string buffer;
					stringstream ss(rollbackLine);
					vector<string> logItems;

					while (ss >> buffer)
						logItems.push_back(buffer);

					string commandType = logItems[0];

					if (logItems[1].compare(log.to_string(currentTransactionId)) == 0 && commandType.compare("UPDATE") == 0)
					{
						updates.push_back(pair<int, int>(atoi(logItems[2].c_str()), atoi(logItems[3].c_str())));
					}
				}

				for (vector<pair<int, int> >::reverse_iterator it = updates.rbegin(); it != updates.rend(); it++)
				{
					putValue(currentTransactionId, (*it).first, (*it).second);
				}

				log.neverFinish(currentTransactionId);
			}
		}
	}
	else
	{
		cout << "Log does not exist, performing reset" << endl;
		reset();
	}
	return lastThreadTransactionsCompleted;
}

// beginTransaction: Marks the beginning of the transaction.  It does the
// following:
// - Logs the beginning of the transaction.
// - Acquires all of the locks needed for this transaction.
// - Assigns a unique id number (which is returned).
//
// --Inputs--
// threadId:	thread number
// transId:		transaction number for the thread
// locationList:	list of accessed disk locations
int TransManager::beginTransaction(
    int threadId,
    int transId,
    const vector<int> &locationList)
{
	pthread_mutex_lock(&globalTransactionCounterLock);
	globalTransactionCounter = globalTransactionCounter + 1;
	int globalTransactionId = globalTransactionCounter;
	pthread_mutex_unlock(&globalTransactionCounterLock);

	log.begin(threadId, transId, globalTransactionId);
	heldLocks[globalTransactionId] = locationList;
	aquireDiskLocks(globalTransactionId);
	createBackup(globalTransactionId, locationList);

	return globalTransactionId;
}

// getValue: Returns the current value of the specifed location.
//
// --Inputs--
// location:	location on disk to retrieve data from
int TransManager::getValue(
    int location)
{
	return disk.readFromDisk(location);
}

// putValue: Places the specified value in the specified location on the disk.
// This function must first create an UPDATE log entry before writing the
// value to the disk.
//
// --Inputs--
// id:		transaction id returned from beginTransaction
// location:	location on disk to write data to
// newValue:	value to write onto disk
// currValue (optional):	current value in location (function will read
// 				current value from disk if not provided)
void TransManager::putValue(
    int id,
    int location,
    int newValue)
{
	int oldValue = getValue(location);
	log.update(id, location, oldValue, newValue);
	disk.writeToDisk(location, newValue);
}

void TransManager::putValue(
    int id,
    int location,
    int newValue,
    int currValue)
{
	log.update(id, location, currValue, newValue);
	disk.writeToDisk(location, newValue);
}

// commitTransaction: Commits the transaction:
// - Log the transaction as committed (COMMITTED).
// - Release held locks.
//
// --Inputs--
// id:		transaction id returned from beginTransaction
void TransManager::commitTransaction(int id)
{
	backups[id].clear();
	log.commit(id);
	releaseDiskLocks(id);
}

// abortTransaction: Aborts the transaction:
// - Log the transaction as aborted (ABORTED).
// - Roll back state such that it appears the transaction never happened.
// - Release held locks.
//
// --Inputs--
// id:		transaction id returned from beginTransaction
void TransManager::abortTransaction(int id)
{
	restoreBackup(id);
	log.abort(id);
	releaseDiskLocks(id);
}

void TransManager::aquireDiskLocks(int id)
{
	vector<int> locks = heldLocks[id];
	for (vector<int>::iterator it = locks.begin(); it != locks.end(); it++)
	{
		pthread_mutex_lock(&diskLocks[*it]);
	}
}

void TransManager::releaseDiskLocks(int id)
{
	vector<int> locks = heldLocks[id];
	for (vector<int>::iterator it = locks.begin(); it != locks.end(); it++)
	{
		pthread_mutex_unlock(&diskLocks[*it]);
	}
}

void TransManager::createBackup(int transactionId, vector<int> locations)
{
	vector<pair<int, int> > backupValues;
	for (vector<int>::iterator it = locations.begin(); it != locations.end(); it++)
	{
		pair<int, int> locationValuePair(*it, getValue(*it));
		backupValues.push_back(locationValuePair);
	}
	backups[transactionId] = backupValues;
}

void TransManager::restoreBackup(int transactionId)
{
	vector<pair<int, int> > backupValues = backups[transactionId];
	for (vector<pair<int, int> >::iterator it = backupValues.begin(); it != backupValues.end(); it++)
	{
		pair<int, int> val = *it;
		disk.writeToDisk(val.first, val.second);
	}
}
