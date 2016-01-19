TARGET_NAME = atomic
CPP = g++
FLAGS = -lpthread -pthread

all: driver

driver:
	$(CPP) -g $(FLAGS) -o $(TARGET_NAME) Disk.cpp Log.cpp TransManager.cpp driver.cpp

clean:
	rm -f *.o $(TARGET_NAME)
	rm -f LOG DISK
