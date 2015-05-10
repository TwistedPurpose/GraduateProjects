// CPSC 5500 - Atomic Transactions: log 

#include "Log.h"

void Log::clearLog()
{
	remove("LOG");
}

void Log::setupLog()
{

}