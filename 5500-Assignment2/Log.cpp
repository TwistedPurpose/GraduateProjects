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

void Log::begin(int threadId, int transactionId, int globalTransactionId)
{
	lockLog();
	writeToLog("BEGIN " + to_string(threadId) + " " + to_string(transactionId));
	unlockLog();
}

void Log::update(int transactionId, int location, int value)
{
	lockLog();
	writeToLog("UPDATE " + to_string(transactionId) + " " + to_string(location) + " " + to_string(value));
	unlockLog();
}

void Log::commit(int transactionId)
{
	lockLog();
	writeToLog("COMMIT " + to_string(transactionId));
	unlockLog();
}

void Log::abort(int transactionId)
{
	lockLog();
	writeToLog("ABORT " + to_string(transactionId));
	unlockLog();
}

void Log::neverFinish(int transactionId)
{
	lockLog();
	writeToLog("NEVER_FINISHED " + to_string(transactionId));
	unlockLog();
}

void Log::lockLog()
{
	pthread_mutex_lock(&logLock);
}

void Log::unlockLog()
{
	pthread_mutex_unlock(&logLock);
}

void Log::writeToLog(string message)
{
	ofstream logFile;
	logFile.open("LOG", ios::app);
	logFile << message + "\n";
	logFile.close();
}