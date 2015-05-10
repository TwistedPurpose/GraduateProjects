// CPSC 5500 - Atomic Transactions: log

#ifndef LOG_H
#define LOG_H

#include <cstdio>
#include <pthread.h>
#include <iostream>
#include <fstream>

using namespace std;

class Log
{
private:
	pthread_mutex_t logLock;
	

public:
	Log();
	void clearLog();
	void lockLog();
	void unlockLog();
	void commit(int threadId, int transactionId);
};

#endif // LOG_H
