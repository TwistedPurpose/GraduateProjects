// CPSC 5500 - Atomic Transactions: disk

// --- DO NOT MODIFY THIS FILE ---

#include <iostream>
#include <iomanip>
using namespace std;

#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <cstdlib>
#include <signal.h>

#include "Disk.h"

// Constructor
Disk::Disk(int _numBlocks)
{
  // HACK: Seed random number generator for crashes
  srand(time(NULL));

  // Initialize variables.
  numBlocks = _numBlocks;
  doRandomCrashes = true;
  pthread_mutex_init(&diskLock, NULL);

  // Check to see if disk exists.
  int diskDescriptor = open("DISK", O_RDWR);
  if (diskDescriptor != -1)  {
    close(diskDescriptor);
    return;
  }

  // Disk does not exist - create a new disk
  diskDescriptor = open("DISK", O_RDWR | O_CREAT | O_EXCL, S_IRUSR | S_IWUSR);
  if (diskDescriptor == -1) {
    cerr << "Could not create disk" << endl;
    exit(-1);
  }
  formatDisk();
  close(diskDescriptor);

  return;
}

// Destructor
Disk::~Disk()
{
}

void Disk::formatDisk()
{
  // HACK: Turn off random crashes during formatting
  bool oldRandomCrashes = doRandomCrashes;
  doRandomCrashes = false;

  // Format disk
  for (int i = 0; i < numBlocks; i++) {
    writeToDisk(i, 0);
  }

  // HACK: Restore random crashes value
  doRandomCrashes = oldRandomCrashes;

  return;
}

int Disk::readFromDisk(int location)
{
  // HACK: Check for random crash
  checkRandomCrash();

  pthread_mutex_lock(&diskLock);
  int diskDescriptor = open("DISK", O_RDWR);

  // Check for invalid location
  if (location < 0 || location >= numBlocks) {
    cerr << "readFromDisk: invalid location" << endl;
    exit(-1);
  }

  // Seek to the appropriate location
  off_t offset = location * sizeof(int);
  off_t newOffset = lseek(diskDescriptor, offset, SEEK_SET);
  if (offset != newOffset) {
    cerr << "readFromDisk: internal failure" << endl;
    exit(-1);
  }

  // Read the value
  int value;
  ssize_t size = read(diskDescriptor, (void *) &value, sizeof(int));
  if (size != sizeof(int)) {
    cerr << "readFromDisk: internal failure" << endl;
    exit(-1);
  }

  close(diskDescriptor);
  pthread_mutex_unlock(&diskLock);
  return value;
}

void Disk::writeToDisk(int location, int value)
{
  // HACK: Check for random crash
  checkRandomCrash();

  pthread_mutex_lock(&diskLock);
  int diskDescriptor = open("DISK", O_RDWR);

  // Check for invalid location
  if (location < 0 || location >= numBlocks) {
    cerr << "writeToDisk: invalid location" << endl;
    exit(-1);
  }
  // Seek to the appropriate location
  off_t offset = location * sizeof(int);
  off_t newOffset = lseek(diskDescriptor, offset, SEEK_SET);
  if (offset != newOffset) {
    cerr << "writeToDisk: internal failure" << endl;
    exit(-1);
  }

  // Write the value
  ssize_t size = write(diskDescriptor, (void *) &value, sizeof(int));
  if (size != sizeof(int)) {
    cerr << "writeToDisk: internal failure" << endl;
    exit(-1);
  }
  close(diskDescriptor);
  pthread_mutex_unlock(&diskLock);
}

void Disk::debugPrintDiskContents()
{
  bool savedCrashFlag = doRandomCrashes;
  doRandomCrashes = false;

  for (int i = 0; i < numBlocks; i++) {
    if (i % 5 == 0) {
      cout << "Locations " << setw(2) << i << "-"
	<< setw(2) << (((numBlocks-1) < i + 4) ? numBlocks - 1 : i + 4)
	<< ":\t";
    }
    cout << readFromDisk(i);
    if (i % 5 == 4)
      cout << endl;
    else
      cout << "\t";
  }
  if (numBlocks % 5 != 0) cout << endl;

  doRandomCrashes = savedCrashFlag;
}

void Disk::checkRandomCrash()
{
  if (doRandomCrashes && rand() % 50 == 0) {
    cerr << "Random hardware failure -- program shutting down NOW!!!" << endl;
    raise(SIGKILL);
  }
}
