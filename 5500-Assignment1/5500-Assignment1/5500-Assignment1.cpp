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
};

class CacheLine
{
public:
	string address = "";
	bool IsUseful = false;
};

class Processor
{
public:
	vector<CacheLine> cache;
	int cacheSize;

	Metrics write(string address, Metrics metrics)
	{

	}

	bool read(string address)
	{
		bool wasHit = false;
		CacheLine lineToMove;

		for (auto it = cache.begin(); it != cache.end(); ++it)
		{
			if ((*it).address == address)
			{
				wasHit = true;
				(*it).IsUseful = true;

				lineToMove = (*it);
				cache.erase(it);

				cache.push_back(lineToMove);

				break;
			}
		}

		return wasHit;
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
	Metrics metrics;

	for (int i = 0; i < numberOfProcessors; i++)
	{
		Processor p;
		p.cacheSize = cacheSize;
		processors.push_back(p);
	}

	for each (string command in commandFileVector)
	{
		vector<string> commandLine = stringSplit(command);

		int processorForCommand = atoi(commandLine.at(0).c_str);
		string stringCommandAction = commandLine.at(1).c_str;
		string address = commandLine.at(1).c_str;

		if (stringCommandAction == "R")  // if it is a read
		{

		}
		else if (stringCommandAction == "W") // If it is a write
		{
			metrics.writes += 1;
		}
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