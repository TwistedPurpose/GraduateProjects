// CPSC 5500 - Atomic Transactions: log 

#include "Log.h"

Log::Log()
{
	pthread_mutex_init(&logLock, NULL);
}

void Log::clearLog()
{
	remove("LOG");
}

void Log::setupLog()
{
	ofstream logFile;
	logFile.open("LOG");
	logFile.close();
}