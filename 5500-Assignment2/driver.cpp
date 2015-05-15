// CPSC 5500 - Atomic Transactions: driver

#include "TransManager.h"

void threadOne(TransManager transManager)
{
	int numberToAdd = 1;
	int rangeBegin = 0;
	int rangeEnd = 9;
	vector<int> diskLocationsUsed;

	for (int i = 0; i <= 4; i++, numberToAdd++)
	{
		for (int k = rangeBegin; k <= rangeEnd; k++)
		{
			diskLocationsUsed.push_back(k);
		}

		int transactionId = transManager.beginTransaction(1, i, diskLocationsUsed);
		for (int j = rangeBegin; j <= rangeEnd; j++)
		{
			int value = transManager.getValue(j);
			value += numberToAdd;
			transManager.putValue(transactionId,j,value);
		}
		transManager.commitTransaction(transactionId);

		rangeBegin = rangeEnd + 1;
		rangeEnd = rangeEnd + 10;
		diskLocationsUsed.clear();
	}
}

void threadTwo(TransManager transManager)
{

}

void threadThree(TransManager transManager)
{

}

void threadFour(TransManager transManager)
{

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
	threadOne(transManager);
	threadTwo(transManager);
	threadThree(transManager);
	threadFour(transManager);

	//Wait for threads to come back

	//Delete this later
	transManager.debugPrintDiskContents();

	delete threadStates;
}
