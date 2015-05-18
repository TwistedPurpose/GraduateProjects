// CPSC 5500 - Atomic Transactions: driver

#include "TransManager.h"
#include <string.h>

void threadZero(TransManager transManager)
{
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

		int transactionId = transManager.beginTransaction(threadNumber, i, diskLocationsUsed);
		for (vector<int>::iterator it = diskLocationsUsed.begin(); it != diskLocationsUsed.end();
			it++)
		{
			int value = transManager.getValue(*it);
			value += numberToAdd;
			transManager.putValue(transactionId,*it,value);
		}
		transManager.commitTransaction(transactionId);

		rangeBegin = rangeEnd + 1;
		rangeEnd = rangeEnd + 10;
		diskLocationsUsed.clear();
	}
}

void threadOne(TransManager transManager)
{
	int numberToAdd = 10;
	int threadNumber = 1;
	int mod = 5;
	vector<int> diskLocationsUsed;

	for (int i = 0; i < 5; i++, numberToAdd += 10)
	{
		// Simulate abort, remove this later
		if (i == 3)
			continue;

		for (int k = 0; k < 50; k++)
		{
			if (k % mod == i)
			{
				diskLocationsUsed.push_back(k);
			}
		}

		int transactionId = transManager.beginTransaction(threadNumber, i, diskLocationsUsed);
		for (vector<int>::iterator it = diskLocationsUsed.begin(); it != diskLocationsUsed.end();
			it++)
		{
			if (*it % mod == i)
			{
				int value = transManager.getValue(*it);
				value += numberToAdd;
				transManager.putValue(transactionId, *it, value);
			}
		}

		if (i == 3)
		{
			//Add aborts later
			//transManager.abortTransaction(transactionId);
		}
		{
			transManager.commitTransaction(transactionId);
		}

		diskLocationsUsed.clear();
	}
}

void threadTwo(TransManager transManager)
{
	int threadNumber = 2;
}

void threadThree(TransManager transManager)
{
	int numberToAdd = 1;
	int threadNumber = 3;
	vector<int> diskLocationsUsed;

	for (int i = 0; i < 50; i++, numberToAdd++)
	{
		diskLocationsUsed.push_back(i);

		int transactionId = transManager.beginTransaction(threadNumber, i, diskLocationsUsed);
		for (vector<int>::iterator it = diskLocationsUsed.begin(); it != diskLocationsUsed.end();
			it++)
		{
			int value = transManager.getValue(*it);
			value += numberToAdd;
			transManager.putValue(transactionId, *it, value);
		}
		transManager.commitTransaction(transactionId);

		diskLocationsUsed.clear();
	}
}

int main(int argc, char* argv[])
{
	int numberOfThreads = 4;
	TransManager transManager(numberOfThreads);
	int * threadStates;

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
	threadZero(transManager);
	threadOne(transManager);
	//threadTwo(transManager);
	threadThree(transManager);

	//Wait for threads to come back

	//Delete this later
	transManager.debugPrintDiskContents();

	delete threadStates;
}
