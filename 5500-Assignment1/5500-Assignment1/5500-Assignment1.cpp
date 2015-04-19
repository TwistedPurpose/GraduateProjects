/*
Author: Soren Ludwig
Class: CPCS 5500
Assignment: 1
*/

#define TEST

#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>

using namespace std;

class Metrics
{
public:
	int readHits = 0;
	int reads = 0;
	int writeHits = 0;
	int writes = 0;
	int usefulUpdates = 0;
	int updates = 0;

	void printStats(string protocol)
	{
		cout << "Number of read hits: " << readHits << endl;
		cout << "Number of reads: " << reads << endl;
		cout << "Read hit rate: " << ((double)readHits / (double)reads) * 100 << "%" << endl;
		cout << "Number of write hits: " << writeHits << endl;
		cout << "Number of writes: " << writes << endl;
		cout << "Write hit rate: " << ((double)writeHits / (double)writes) * 100 << "%" << endl;
		if (protocol == "u")
		{
			cout << "Number of useless updates: " << updates - usefulUpdates << endl;
			cout << "Number of updates: " << updates << endl;
			cout << "Percentage of useless updates: " << ((double)(updates - usefulUpdates) / (double)updates) * 100 << "%" << endl;
		}
	}
};

void printTrace(int processorNumber, string address, string operation, bool wasHit,
	string policy, string processorsAffected)
{
	string fullOpName = "";
	string hitOrMissText = "";
	string policyName = "";

	if (operation == "R")
	{
		fullOpName = "Read";
	}
	else
	{
		fullOpName = "Write";
	}

	if (wasHit)
	{
		hitOrMissText = "Hit!";
	}
	else
	{
		hitOrMissText = "Miss!";
	}

	if (policy == "u")
	{
		policyName = " Update: ";
	}
	else
	{
		policyName = " Invalidate: ";
	}

	cout << "Proc: " << processorNumber << " Address: " << address << " Op: " <<
		fullOpName << " Cache: " << hitOrMissText << policyName << processorsAffected << endl;
}

class CacheLine
{
public:
	string address = "";
	bool IsUseful = false;
	bool WasUpdated = false;

	CacheLine() {}
	CacheLine(string _address)
	{
		address = _address;
	}
};

Metrics metrics;

class Processor
{
	void evictOrAddAddress(string address)
	{
		CacheLine lineToMove;

		bool foundEmptyAddress = false;

		for (auto it = cache.begin(); it != cache.end(); ++it)
		{
			if ((*it).address == "") // Only used for Invalidation
			{
				(*it).address = address;
				lineToMove = (*it);
				cache.erase(it);

				lineToMove.WasUpdated = false;  // Don't care, we're using invalidate, just resetting this
				lineToMove.IsUseful = false;

				cache.push_back(lineToMove);
				foundEmptyAddress = true;
				break;
			}
		}

		if (!foundEmptyAddress)  //
		{
			CacheLine newLine(address);

			if (cache.size() >= cacheMaxSize) // Delete least recently used if cache is full
			{
				cache.erase(cache.begin());
			}

			cache.push_back(address);
		}
	}
public:
	int processorNumber;
	vector<CacheLine> cache;
	int cacheMaxSize;

	Processor(){}

	Processor(int _processorNumber, int _cacheSize)
	{
		processorNumber = _processorNumber;
		cacheMaxSize = _cacheSize;
	}

	bool write(string address)
	{
		bool wasHit = false;
		CacheLine lineToMove;

		for (auto it = cache.begin(); it != cache.end(); ++it)
		{
			if ((*it).address == address)  // If there was a hit
			{
				wasHit = true;

				(*it).WasUpdated = false; // Reset update/usefulness
				(*it).IsUseful = false;

				lineToMove = (*it); // Push to be most recently used
				cache.erase(it);

				cache.push_back(lineToMove);

				break;
			}
		}

		if (!wasHit)
		{
			evictOrAddAddress(address);
		}

		return wasHit;
	}

	bool read(string address)
	{
		bool wasHit = false;
		CacheLine lineToMove;

		for (auto it = cache.begin(); it != cache.end(); ++it)
		{
			if ((*it).address == address)  // If there was a hit
			{
				wasHit = true;

				if ((*it).WasUpdated && !(*it).IsUseful)
				{
					(*it).IsUseful = true;
					metrics.usefulUpdates += 1;
				}

				lineToMove = (*it);
				cache.erase(it);

				cache.push_back(lineToMove);

				break;
			}
		}

		if (!wasHit)
		{
			evictOrAddAddress(address);
		}

		return wasHit;
	}

	bool update(string address)
	{
		bool wasUpdated = false;

		for (auto it = cache.begin(); it != cache.end(); it++)
		{
			if ((*it).address == address)
			{
				wasUpdated = true;
				(*it).WasUpdated = true;
				(*it).IsUseful = false;
				metrics.updates += 1;
			}
		}
		return wasUpdated;
	}

	bool invalidate(string address)
	{
		bool wasInvalidated = false;

		for (int i = 0; i < cache.size(); i++)
		{
			if (cache[i].address == address)
			{
				wasInvalidated = true;
				cache[i].address = "";
			}
		}

		return wasInvalidated;
	}
};

string invalidateProcessors(vector<Processor> &processors, string addressToInvalidate, int excludedProcessor)
{
	string invalidatedProcs = "";

	for (auto it = processors.begin(); it != processors.end(); it++)
	{
		if ((*it).processorNumber == excludedProcessor)
			continue;
		if ((*it).invalidate(addressToInvalidate))
		{
			invalidatedProcs += to_string((*it).processorNumber) + " ";
		}
	}
	return invalidatedProcs;
}

string updateProcessors(vector<Processor> &processors, string addressToUpdate, int excludedProcessor)
{
	string updatedProcs = "";

	for (auto it = processors.begin(); it != processors.end(); it++)
	{
		if ((*it).processorNumber == excludedProcessor)
			continue;
		if ((*it).update(addressToUpdate))
		{
			updatedProcs += to_string((*it).processorNumber) + " ";
		}
	}
	return updatedProcs;
}

vector<string> stringSplit(string stringToBeSplit)
{
	string buffer;
	stringstream splitter(stringToBeSplit);

	vector<string> elements;

	while (splitter >> buffer)
	{
		elements.push_back(buffer);
	}

	return elements;
}

void cacheSimulator(vector<string> commandFileVector, int cacheSize, string protocol,
	string output, int numberOfProcessors)
{
	vector<Processor> processors;

	for (int i = 0; i < numberOfProcessors; i++)
	{
		Processor p(i, cacheSize);
		processors.push_back(p);
	}

	for each (string command in commandFileVector)
	{
		vector<string> commandLine = stringSplit(command);

		bool wasHit = false;
		int processorForCommand = atoi(commandLine.at(0).c_str());
		string stringCommandAction = commandLine.at(1).c_str();
		string address = commandLine.at(2).c_str();

		string affectedProcessors = "";

		if (stringCommandAction == "R")  // if it is a read
		{
			metrics.reads += 1;

			wasHit = processors.at(processorForCommand).read(address);

			if (wasHit)
			{
				metrics.readHits += 1;
			}

			if (output == "t")
			{
				printTrace(processorForCommand, address, stringCommandAction, wasHit, protocol, affectedProcessors);
			}
		}
		else if (stringCommandAction == "W") // If it is a write
		{
			metrics.writes += 1;

			wasHit = processors.at(processorForCommand).write(address);

			if (wasHit)
			{
				metrics.writeHits += 1;
			}

			if (protocol == "i")
			{
				affectedProcessors = invalidateProcessors(processors, address, processorForCommand);
			}
			else if (protocol == "u")
			{
				affectedProcessors = updateProcessors(processors, address, processorForCommand);
			}

			if (output == "t")
			{
				printTrace(processorForCommand, address, stringCommandAction, wasHit, protocol, affectedProcessors);
			}
		}
	}

	if (output == "s")
	{
		metrics.printStats(protocol);
	}
}

vector<string> parseFile(string filePath)
{
	vector<string> wordVector;

	ifstream myFile(filePath);
	if (myFile.is_open())
	{
		string word;

		while (std::getline(myFile, word))
		{
			wordVector.push_back(word);
		}

		myFile.close();
	}
	else
	{
		wordVector.clear();
	}

	return wordVector;
}

int main(int argc, char* argv[])
{
	if (argc != 5)
	{
		cout << argc;
		cerr << "Improper number of arguments!" << endl;
		exit(-1);
	}

	vector<string> commandFileVector = parseFile(argv[1]);

	if (commandFileVector.empty())
	{
		cerr << "Unable to open file!" << endl;
		exit(-1);
	}

	int cacheSize = atoi(argv[2]);
	string protocol = argv[3];
	string output = argv[4];
	int numberOfProcessors = atoi(commandFileVector.front().c_str());

	commandFileVector.erase(commandFileVector.begin());

	cacheSimulator(commandFileVector, cacheSize, protocol, output, numberOfProcessors);

#ifdef TEST
	int i = 0;
	cin >> i;
#endif
}