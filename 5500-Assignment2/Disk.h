// CPSC 5500 - Atomic Transactions: disk

// --- DO NOT MODIFY THIS FILE ---

#ifndef DISK_H
#define DISK_H

#include <pthread.h>

class Disk {
  
  public:

    // Constructor
    Disk(int _numBlocks = 50);

    // Destructor
    ~Disk();

    // Format the disk by writing zeroes into all locations.
    void formatDisk();

    // Reads disk from the specified location and returns its value.  Any
    // error aborts the program.
    int readFromDisk(int location);

    // Writes the specified value to the specified location on the disk.  Any
    // error aborts the program.
    void writeToDisk(int location, int value);

    // Returns the number of blocks
    int getNumBlocks() { return numBlocks; }

    // DEBUG FUNCTIONS

    // DEBUG: Prints the contents of the disk.
    void debugPrintDiskContents();

    // DEBUG: Turns random crashes on
    void debugTurnCrashesOn() { doRandomCrashes = true; }

    // DEBUG: Turns random crashes off
    void debugTurnCrashesOff() { doRandomCrashes = false; }

  private:

    int numBlocks;		// number of blocks
    bool doRandomCrashes;	// enable random crashes
    pthread_mutex_t diskLock;	// lock to make disk operations atomic

    // Possible cause random crash
    void checkRandomCrash();

};

#endif
