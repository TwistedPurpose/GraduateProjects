// CPSC 5500 - Atomic Transactions: transaction manager

#include "TransManager.h"

// Constructor
TransManager::TransManager(int numThreads)
{
	numberOfThreads = numThreads;

	diskLocks = new pthread_mutex_t[50];
	for (int i = 0; i < 50; i++)
	{
		pthread_mutex_init(&diskLocks[i], NULL);
	}
}

// Destructor
TransManager::~TransManager()
{
	delete diskLocks;
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
	throw "Recover not yet implemented";
	return new int[numberOfThreads];
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
	//TODO: get globaltrandactioncounter lock
	pthread_mutex_lock(&globalTransactionCounterLock);
	int globalTransactionId = globalTransactionCounter + 1;
	pthread_mutex_unlock(&globalTransactionCounterLock);

	log.begin(threadId, transId, globalTransactionId);
	heldLocks = locationList;
	aquireDiskLocks();

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
	log.update(id, location, newValue);
	disk.writeToDisk(location, newValue);
}

void TransManager::putValue(
    int id,
    int location,
    int newValue,
    int currValue)
{
}

// commitTransaction: Commits the transaction:
// - Log the transaction as committed (COMMITTED).
// - Release held locks.
//
// --Inputs--
// id:		transaction id returned from beginTransaction
void TransManager::commitTransaction(int id)
{
	//Finish This
	log.commit(id);
	releaseDiskLocks();
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
}

void TransManager::aquireDiskLocks()
{
	for (auto it = heldLocks.begin(); it != heldLocks.end(); it++)
	{
		pthread_mutex_lock(&diskLocks[*it]);
	}
}

void TransManager::releaseDiskLocks()
{
	for (auto it = heldLocks.begin(); it != heldLocks.end(); it++)
	{
		pthread_mutex_unlock(&diskLocks[*it]);
	}
}