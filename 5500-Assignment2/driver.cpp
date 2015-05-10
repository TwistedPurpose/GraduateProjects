// CPSC 5500 - Atomic Transactions: driver

#include "TransManager.h"

int main(int argc, char* argv[])
{
	TransManager * transManager = new TransManager(4);
	if (argc == 2 && argv[1] == "-c")
	{
		//Clear disk and log
	}
	else
	{
		//Restore
	}

	//Spin off threads

	//Wait for threads to come back

}
