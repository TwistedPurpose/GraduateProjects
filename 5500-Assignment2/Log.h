// CPSC 5500 - Atomic Transactions: log

#ifndef LOG_H
#define LOG_H

#include <cstdio>
#include <pthread.h>
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>
#include <string.h>

using namespace std;

class Log
{
private:
	pthread_mutex_t logLock;
	void lockLog();
	void unlockLog();
	void writeToLog(string message);

public:
	Log();
	void clearLog();
	bool doesLogExist();

	void begin(int threadId, int transactionId, int globalTransactionId);
	void update(int transactionId, int location, int currentValue, int value);
	void commit(int transactionId);
	void abort(int transactionId);
	void neverFinish(int transactionId);
	int getGlobalTransactionNumber();
};

#endif // LOG_H
