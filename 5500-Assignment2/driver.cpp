// CPSC 5500 - Atomic Transactions: driver

#include "TransManager.h"
#include <string.h>

void threadOne(TransManager transManager)
{

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
	threadOne(transManager);
	threadOne(transManager);
	threadOne(transManager);

	//Wait for threads to come back

	//Delete this later
	transManager.debugPrintDiskContents();

	delete threadStates;
}
