// CPSC 5500 - Atomic Transactions: driver

#include "TransManager.h"
#include <string.h>
#include <stdio.h>

void * threadZeroWork(void * transManager)
{
	TransManager * trans = static_cast<TransManager *>(transManager);
	int numberToAdd = 1;
	int threadNumber = 0;
	int rangeBegin = 0;
	int rangeEnd = 9;
	vector<int> diskLocationsUsed;

	for (int i = 0; i < 5; i++, numberToAdd++)
	{
		for (int k = rangeBegin; k <= rangeEnd; k++)
		{
			diskLocationsUsed.push_back(k);
		}

		int transactionId = trans->beginTransaction(threadNumber, i, diskLocationsUsed);
		for (vector<int>::iterator it = diskLocationsUsed.begin(); it != diskLocationsUsed.end();
			it++)
		{
			int value = trans->getValue(*it);
			value += numberToAdd;
			trans->putValue(transactionId, *it, value);
		}
		trans->commitTransaction(transactionId);

		rangeBegin = rangeEnd + 1;
		rangeEnd = rangeEnd + 10;
		diskLocationsUsed.clear();
	}
	return NULL;
}

void * threadOneWork(void * transManager)
{
	TransManager * trans = static_cast<TransManager *>(transManager);
	int numberToAdd = 10;
	int threadNumber = 1;
	int mod = 5;
	vector<int> diskLocationsUsed;

	for (int i = 0; i < 5; i++, numberToAdd += 10)
	{
		// Simulate abort, remove this later
		//if (i == 3)
		//	continue;

		for (int k = 0; k < 50; k++)
		{
			if (k % mod == i)
			{
				diskLocationsUsed.push_back(k);
			}
		}

		int transactionId = trans->beginTransaction(threadNumber, i, diskLocationsUsed);
		for (vector<int>::iterator it = diskLocationsUsed.begin(); it != diskLocationsUsed.end();
			it++)
		{
			if (*it % mod == i)
			{
				int value = trans->getValue(*it);
				value += numberToAdd;
				trans->putValue(transactionId, *it, value);
			}
		}

		if (i == 3)
		{
			//Add aborts later
			//transManager.abortTransaction(transactionId);
		}
		{
			trans->commitTransaction(transactionId);
		}

		diskLocationsUsed.clear();
	}
	return NULL;
}

void * threadTwoWork(void * transManager)
{
	TransManager * trans = static_cast<TransManager *>(transManager);
	int numberToAddOrSubtract = 2;
	int threadNumber = 2;
	vector<int> diskLocationsUsed;

	for (int i = 0; i < 25; i++)
	{
		int subLocation = 2 * i;
		int addLocation = (2 * i) + 1;
		////Remove later, add abort
		//if (i == 20)
		//	continue;

		diskLocationsUsed.push_back(subLocation);
		diskLocationsUsed.push_back(addLocation);
		int transactionId = trans->beginTransaction(threadNumber, i, diskLocationsUsed);

		int subValue = trans->getValue(subLocation);
		subValue -= 2;
		trans->putValue(transactionId, subLocation, subValue);

		int addValue = trans->getValue(addLocation);
		addValue += 2;
		trans->putValue(transactionId, addLocation, addValue);

		if (i == 20)
		{
			trans->abortTransaction(transactionId);
		}
		else 
		{
			trans->commitTransaction(transactionId);
		}

	}
	return NULL;
}

void * threadThreeWork(void * transManager)
{
	TransManager * trans = static_cast<TransManager *>(transManager);
	int threadNumber = 3;
	vector<int> diskLocationsUsed;

	for (int i = 0; i < 50; i++)
	{
		diskLocationsUsed.push_back(i);

		int transactionId = trans->beginTransaction(threadNumber, i, diskLocationsUsed);

		int value = trans->getValue(i);
		value += i + 1;
		trans->putValue(transactionId, i, value);
		
		trans->commitTransaction(transactionId);

		diskLocationsUsed.clear();
	}
	return NULL;
}

int main(int argc, char* argv[])
{
	int numberOfThreads = 4;
	TransManager transManager(numberOfThreads);
	int * threadStates;

	pthread_t threadZero;
	pthread_t threadOne;
	pthread_t threadTwo;
	pthread_t threadThree;

	//Delete this later
	transManager.debugTurnCrashesOff();

	if (argc == 2 && strcmp(argv[1], "-c") == 0)
	{
		//Clear disk and log
		transManager.reset();

		threadStates = new int[numberOfThreads];

		for (int i = 0; i < numberOfThreads; i++)
		{
			threadStates[i] = 0;
		}
	}
	else if (argc == 1)
	{
		//Restore
		threadStates = transManager.recover();
	}
	else
	{
		cerr << "Invalid command line arguments." << endl;
		return -1;
	}

	//Spin off threads
	pthread_create(&threadZero, NULL, threadZeroWork, &transManager);
	pthread_create(&threadOne, NULL, threadOneWork, &transManager);
	pthread_create(&threadTwo, NULL, threadTwoWork, &transManager);
	pthread_create(&threadThree, NULL, threadThreeWork, &transManager);

	//Wait for threads to come back
	pthread_join(threadZero, NULL);
	pthread_join(threadOne, NULL);
	pthread_join(threadTwo, NULL);
	pthread_join(threadThree, NULL);

	//Delete this later
	transManager.debugPrintDiskContents();

	delete threadStates;
}
