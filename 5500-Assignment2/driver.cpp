// CPSC 5500 - Atomic Transactions: driver

#include "TransManager.h"

void threadOne(TransManager transManager)
{

}

void threadOne(TransManager transManager)
{

}

void threadOne(TransManager transManager)
{

}

void threadOne(TransManager transManager)
{

}

int main(int argc, char* argv[])
{
	int numberOfThreads = 4;
	TransManager * transManager = new TransManager(numberOfThreads);
	int * threadStates;

	//Delete this later
	transManager->debugTurnCrashesOff();

	if (argc == 2 && argv[1] == "-c")
	{
		//Clear disk and log
		transManager->reset();

		threadStates = new int[numberOfThreads];

		for (int i = 0; i < numberOfThreads; i++)
		{
			threadStates[i] = 0;
		}
	}
	else if (argc == 1)
	{
		//Restore
		threadStates = transManager->recover();
	}
	else
	{
		cerr << "Invalid command line arguments.";
		exit(-1);
	}

	//Spin off threads

	//Wait for threads to come back

	//Delete this later
	transManager->debugPrintDiskContents();

	delete threadStates;
	delete transManager;
}
