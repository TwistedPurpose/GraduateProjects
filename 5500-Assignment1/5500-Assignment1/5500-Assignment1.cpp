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
	int uselessUpdates = 0;
	int updates = 0;

	void printStats()
	{
		cout << "Number of read hits: " << readHits << endl;
		cout << "Number of reads: " << reads << endl;
		cout << "Read hit rate: " << (double)readHits / (double)reads << endl;
		cout << "Number of write hits: " << writeHits << endl;
		cout << "Number of writes: " << writes << endl;
		cout << "Write hit rate: " << (double)writeHits / (double)writes << endl;
		cout << "Number of useless updates: " << uselessUpdates << endl;
		cout << "Number of updates: " << updates << endl;
		cout << "Write hit rate: " << (double)uselessUpdates / (double)updates << endl;
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

public:
	vector<CacheLine> cache;
	int cacheMaxSize;

	bool write(string address)
	{

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
				// Set Usefulness here?

				//makeMostRecentlyUsed(it);
				lineToMove = (*it);
				cache.erase(it);

				cache.push_back(lineToMove);

				break;
			}
		}

		if (!wasHit)
		{
			bool foundEmptyAddress = false;

			for (auto it = cache.begin(); it != cache.end(); ++it)
			{
				if ((*it).address == "") // Only used for Invalidation
				{
					(*it).address = address;
					lineToMove = (*it);
					cache.erase(it);

					cache.push_back(lineToMove);
					foundEmptyAddress = true;
					break;
				}
			}

			if (!foundEmptyAddress)
			{
				CacheLine newLine(address);

				if (cache.size() >= cacheMaxSize) // Delete least recently used if cache is full
				{
					cache.erase(cache.begin());
				}

				cache.push_back(address);
			}
		}

		return wasHit;
	}

	bool update(string address)
	{

	}

	bool invalidate(string address)
	{

	}
};



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
		Processor p;
		p.cacheMaxSize = cacheSize;
		processors.push_back(p);
	}

	for each (string command in commandFileVector)
	{
		vector<string> commandLine = stringSplit(command);

		bool wasHit = false;
		int processorForCommand = atoi(commandLine.at(0).c_str());
		string stringCommandAction = commandLine.at(1).c_str();
		string address = commandLine.at(2).c_str();

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
				printTrace(processorForCommand, address, stringCommandAction, wasHit, protocol, "");
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

			if (output == "t")
			{
				printTrace(processorForCommand, address, stringCommandAction, wasHit, protocol, "");
			}
		}
	}

	if (output == "s")
	{
		metrics.printStats();
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