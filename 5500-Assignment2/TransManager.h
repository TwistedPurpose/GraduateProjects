// CPSC 5500 - Atomic Transactions: transaction manager

#ifndef TRANS_MANAGER_H
#define TRANS_MANAGER_H

#include "Disk.h"
#include "Log.h"

#include <vector>
#include <deque>
#include <pthread.h>
using namespace std;

class TransManager
{
  // --- DO NOT MODIFY THE PUBLIC INTERFACE ---
  public:

    // Constructor
    TransManager(int numThreads);

    // Destructor
    ~TransManager();

    // reset: Resets the manager:
    // - Formats the disk.
    // - Clears out the log.
    void reset();

    // recover: Recover from a crash:
    // - Resets the disk to a sane state such that unfinished transactions
    // are rolled back. 
    // - Returns a dynamically allocated array the id of the next transaction
    // to run for each thread.
    // - If the log file does not exist, then call reset and return an array
    // such that each threads starts with transaction 0.
    int *recover();

    // beginTransaction: Marks the beginning of the transaction.  It does the
    // following:
    // - Logs the beginning of the transaction.
    // - Acquires all of the locks needed for this transaction.
    // - Assigns a global transaction id number (which is returned).
    //
    // --Inputs--
    // threadId:	thread number
    // transId:		transaction number for the thread
    // locationList:	list of accessed disk locations
    int beginTransaction(int threadId, int transId, const vector<int> &locationList);

    // getValue: Returns the current value of the specified location.
    //
    // --Inputs--
    // location:	location on disk to retrieve data from
    int getValue(int location);

    // putValue: Places the specified value in the specified location on the disk.
    // This function must first create an UPDATE log entry before writing the
    // value to the disk.
    //
    // --Inputs--
    // id:		transaction id returned from beginTransaction
    // location:	location on disk to write data to
    // newValue:	value to write onto disk
    // currValue (optional):	current value in location (function will read
    // 				current value from disk if not provided)
    void putValue(int id, int location, int newValue);
    void putValue(int id, int location, int newValue, int currValue);

    // commitTransaction: Commits the transaction:
    // - Log the transaction as committed (COMMITTED).
    // - Release held locks.
    //
    // --Inputs--
    // id:		transaction id returned from beginTransaction
    void commitTransaction(int id);

    // abortTransaction: Aborts the transaction:
    // - Log the transaction as aborted (ABORTED).
    // - Roll back state such that it appears the transaction never happened.
    // - Release held locks.
    //
    // --Inputs--
    // id:		transaction id returned from beginTransaction
    void abortTransaction(int id);

    // DEBUG FUNCTIONS

    // DEBUG: Prints the contents of the disk.
    void debugPrintDiskContents() { disk.debugPrintDiskContents(); };

    // DEBUG: Turns random crashes on
    void debugTurnCrashesOn() { disk.debugTurnCrashesOn(); }

    // DEBUG: Turns random crashes off
    void debugTurnCrashesOff() { disk.debugTurnCrashesOff(); }

  private:
    
    Disk disk;		// Disk to access
    Log log;		// Log file
	int numberOfThreads;

    // TODO: Add your own data members and private functions here.
};

#endif
