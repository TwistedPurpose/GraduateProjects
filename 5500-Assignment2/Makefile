TARGET_NAME = atomic
CPP = g++
FLAGS = -lpthread

all: driver

driver:
	$(CPP) $(FLAGS) -o $(TARGET_NAME) Disk.cpp Log.cpp TransManager.cpp driver.cpp

clean:
	rm -f *.o $(TARGET_NAME)
	rm -f LOG DISK
