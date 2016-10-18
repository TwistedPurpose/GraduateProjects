/* ======================================
   Mesh.cpp - basic mesh representation and IO
   Copyright (c) Jules Bloomenthal, Seattle, 2012
   All rights reserved
   ====================================== */

#include "Mesh.h"
#include <assert.h>
#include <iostream>
#include <fstream>
#include <string>
#include <direct.h>

using std::string;
using std::vector;
using std::ios;
using std::ifstream;

// miscellaneous operations

void MinMax(vector<vec3> &vertices, vec3 &min, vec3 &max) {
	min[0] = min[1] = min[2] = FLT_MAX;
	max[0] = max[1] = max[2] = -FLT_MAX;
	for (int i = 0; i < (int) vertices.size(); i++) {
		vec3 &v = vertices[i];
		for (int k = 0; k < 3; k++) {
			if (v[k] < min[k])
				min[k] = v[k];
			if (v[k] > max[k])
				max[k] = v[k];
		}
	}
}

void Normalize(vector<vec3> &vertices, float scale) {
	vec3 min, max;
	MinMax(vertices, min, max);
	vec3 center(.5f*(min[0]+max[0]), .5f*(min[1]+max[1]), .5f*(min[2]+max[2]));
	float maxrange = 0;
	for (int k = 0; k < 3; k++)
		if ((max[k]-min[k]) > maxrange)
			maxrange = max[k]-min[k];
	float s = scale*2.f/maxrange;
	for (int i = 0; i < (int) vertices.size(); i++) {
		vec3 &v = vertices[i];
		for (int k = 0; k < 3; k++)
			v[k] = s*(v[k]-center[k]);
	}
}

vec3 Normal(vector<vec3> *verts, int3 &tri) {
	vec3 &v1 = (*verts)[tri.i1];
	vec3 &v2 = (*verts)[tri.i2];
	vec3 &v3 = (*verts)[tri.i3];
	vec3 a(v2-v1), b(v3-v2);
	return(normalize(cross(a, b)));
}

void SetVertexNormals(vector<vec3> &vertices, vector<int3> &triangles, vector<vec3> &normals) {
	int nverts = (int) vertices.size();
	// size normals array and initialize to zero
	normals.resize(nverts, vec3(0));
	// accumulate each triangle normal into its three vertex normals
	for (int i = 0; i < (int) triangles.size(); i++) {
		int3 &t = triangles[i];
		vec3 n = Normal(&vertices, t);
		normals[t.i1] += n;
		normals[t.i2] += n;
		normals[t.i3] += n;
	}
	// set to unit length
	for (int i = 0; i < nverts; i++)
		normals[i] = normalize(normals[i]);
}

// ASCII support

bool ReadWord(char* &ptr, char *word, int charLimit) {
	ptr += strspn(ptr, " \t");					// skip white space
	int nChars = strcspn(ptr, " \t");	        // get # non-white-space characters
	if (!nChars)
		return false;					        // no non-space characters
	int nRead = charLimit-1 < nChars? charLimit-1 : nChars;
	strncpy(word, ptr, nRead);
	word[nRead] = 0;							// strncpy does not null terminate
	ptr += nChars;
		return true;
}

void ReadString(FILE *in, char *buffer, int maxSize) {
	buffer[maxSize-1] = 0;
    for (int i = 0; i < maxSize-1; i++)
        if (!(buffer[i] = (char) fgetc(in)))
            break;
}

// STL

bool ReadSTL(char *filename, vector<vec3> &vertices, vector<int3> &triangles, vector<vec3> *vertexNormals) {
	// the facet normal should point outwards from the solid object; if this is zero,
	// most software will calculate a normal from the ordered triangle vertices using the right-hand rule
    class Helper {
    public:
        bool status;
		vector<vec3> *verts, *vnrms;
		vector<int3> *tris;
        vector<string> vSpecs;                                  // ASCII only
        Helper(char *filename, vector<vec3> *verts, vector<int3> *tris, vector<vec3> *vnrms)
			: verts(verts), tris(tris), vnrms(vnrms) {
				char line[1000], word[1000], *ptr = line;
				ifstream inText(filename, ios::in);				// text default mode
				inText.getline(line, 10);
				bool ascii = ReadWord(ptr, word, 10) && !_stricmp(word, "solid");
				if (ascii)
					status = ReadASCII(inText);
				inText.close();
				if (!ascii) {
					FILE *inBinary = fopen(filename, "rb");		// inText.setmode(ios::binary) fails
					if (inBinary) {
						status = ReadBinary(inBinary);
						fclose(inBinary);
					}
					else
						status = false;
				}
        }
        int VertexASCII(char *s) {
            vec3 v;
            string vSpec(s);
            int nVertices = verts->size();
            for (int i = 0; i < nVertices; i++)
                if (vSpecs[i] == vSpec)
                    return i;
            if (sscanf(s, "%g%g%g", &v.x, &v.y, &v.z) != 3)
                return -1;
            verts->push_back(vec3(v.x, v.y, v.z));
		//	printf("adding %s\n", s);
            vSpecs.push_back(vSpec);
            return nVertices;
        }
        bool ReadASCII(ifstream &in) {
            int nVIDs = 0, vids[3];
            static const int bufLim = 1000;
            char line[bufLim], word[bufLim];
            for (int lineNum = 0; !in.eof(); lineNum++) {
                in.getline(line, bufLim);
                if (strlen(line) >= bufLim-1) {
                    printf("line %i too long\n", lineNum);
					return false;
				}
                char *ptr = line;
                if (!ReadWord(ptr, word, bufLim))
                    continue;
                else if (!_stricmp(word, "solid")) {
                    if (ReadWord(ptr, word, bufLim))
                        string dummyName = string(word);
                }
                else if (!_stricmp(word, "vertex")) {
                    if ((vids[nVIDs] = VertexASCII(ptr)) < 0) {
						printf("bad line %d\n", lineNum);
						return false;
					}
                    if (!(nVIDs = ++nVIDs%3))
						tris->push_back(int3(vids[0], vids[1], vids[2]));
                }
            }
            return true;
        }
        int VertexBinary(FILE *in) {
            vec3 v;
            if (fread(&v.x, sizeof(float), 3, in) != 3)
                return -1;
            int nverts = verts->size();
            for (int i = 0; i < nverts; i++) // **** inefficient
                if (!memcmp(&v.x, &(*verts)[i].x, sizeof(vec3)))
                    return i;
			verts->push_back(v);
            return nverts;
        }
        bool ReadBinary(FILE *in) {
                  // # bytes      use                  significance
                  // -------      ---                  ------------
                  //      80      header               none
                  //       4      unsigned long int    number of triangles
                  //      12      3 floats             triangle normal
                  //      12      3 floats             x,y,z for vertex 1
                  //      12      3 floats             vertex 2
                  //      12      3 floats             vertex 3
                  //       2      unsigned short int   attribute (0)
                  // endianness is assumed to be little endian
            // in.setmode(ios::binary); doc says setmode good, but compiler says not so
			// sizeof(bool)=1, sizeof(char)=1, sizeof(short)=2, sizeof(int)=4, sizeof(float)=4
            vec3 n;
            char buf[81];
            unsigned int nTriangles = 0, nTriangle = 0, vid1, vid2, vid3;
            if (fread(buf, 1, 80, in) != 80) // header
                return false;
            if (fread(&nTriangles, sizeof(int), 1, in) != 1)
                return false;
            while (!feof(in)) {
                if (nTriangle == nTriangles)
                    break;
                if (nTriangles > 5000 && nTriangle && nTriangle%1000 == 0)
					printf("\rread %i/%i triangles", nTriangle, nTriangles);
                if (fread(&n.x, sizeof(float), 3, in) != 3) {
                    printf("\ncan't read triangle %d normal\n", nTriangle);
                    return false;
                }
                if ((vid1 = VertexBinary(in)) < 0 ||
                    (vid2 = VertexBinary(in)) < 0 ||
                    (vid3 = VertexBinary(in)) < 0) {
                        printf("\ncan't read vids: %d, %d, %d\n", vid1, vid2, vid3);
                        return false;
                }
				unsigned short attribute;
                if (fread(&attribute, sizeof(short), 1, in) != 1) {
                    printf("\ncan't read attribute\n");
                    return false;
                }
                if (vid1 != vid2 && vid2 != vid3 && vid3 != vid1) {
                    int3 t(vid1, vid2, vid3);
					vec3 nrm = Normal(verts, t);
                    if (dot(nrm, n) < 0) {
						int tmp = t.i1;
						t.i1 = t.i2;
						t.i2 = tmp;
					}
				//	t.g = attribute;
					tris->push_back(t);
                }
				else
					printf("\nSTL ReadBinary: bad triangle\n");
                nTriangle++;
            }
		//	printf("\r");
            return true;
        }
    };
    Helper h(filename, &vertices, &triangles, vertexNormals);
	if (vertexNormals)
		SetVertexNormals(vertices, triangles, *vertexNormals);
    return h.status;
} // end ReadSTL

// binary OBJ

static char versionKey[] = "MESH_BINARY_VERSION_";
enum BoolEx {False, True, BE_Unknown};

bool ReadBinaryObj(char         *filename,
				   vector<vec3>	&vertices,
				   vector<int3>	&triangles,
				   vector<int2> *edges,
				   vector<vec3>	*vertexNormals,
				   vector<vec2>	*vertexTextures,
				   vector<int>  *vertexTypes,
				   vector<int>	*triangleGroups) {
	// read in order:
	//     version (ignored)
	//     misc parameters (ignored)
	//	   key/value pairs (ignored)
	//	   array sizes
	//	   vertices
	//	   normals
	//	   textures
	//	   vertex types
	//	   segments (ignored)
	//	   triangles and triangleGroups
	//     polygons (ignored)
	//     edges
	//     *** remainder of file ignored - see MeshIO.cpp to read errors, name,
	//         bounds, visibility, transparency, color, and render flags
	// attempt to open
	FILE *in = fopen(filename, "rb");
    if (!in)
        return false;
    // version
    char buf[1000];
    ReadString(in, buf, 1000); // read null-terminated string
    char *v = strstr(buf, versionKey);
	if (!v || atoi(v+strlen(versionKey)) != 5)
        assert("bad read version" == NULL);
    // misc parameters
	BoolEx tessellated, triangleEIDsSet, noUnusedVertices, noDegenerateTriangles,
		   noDuplicateTriangles, noDanglingTriangles;
    if (fread(&tessellated,           sizeof(BoolEx), 1, in) != 1 ||
        fread(&triangleEIDsSet,       sizeof(BoolEx), 1, in) != 1 ||
        fread(&noUnusedVertices,      sizeof(BoolEx), 1, in) != 1 ||
        fread(&noDegenerateTriangles, sizeof(BoolEx), 1, in) != 1 ||
        fread(&noDuplicateTriangles,  sizeof(BoolEx), 1, in) != 1 ||
        fread(&noDanglingTriangles,   sizeof(BoolEx), 1, in) != 1)
            return false;
    
    // keyword pairs
	struct KeyValue {
		string key, value;
		KeyValue() {key = ""; value = "";}
		KeyValue(string k, string v) {key = k; value = v;}
	};
    int nKeywords;
    fread(&nKeywords, sizeof(int), 1, in);
	vector<KeyValue> keyValues(nKeywords);
    for (int i = 0; i < nKeywords; i++) {
        ReadString(in, buf, 1000);
        char *ptr = buf, word[100];
        if (ReadWord(ptr, word, 100) && !_stricmp(word, "pair")) {
            char key[1000], value[1000];
            ReadWord(ptr, key, 1000);
            ptr += strspn(ptr, " \t");
            strcpy(value, ptr);
            keyValues[i] = KeyValue(string(key), string(value));
        }
    }
    // sizes
	struct MeshSizes {
		int nVertices, nNormals, nTextures, nEdges, nVertexTypes,
		nSegments, nTriangles, nPolygons, nFaces, nPhantoms;
	} sizes;
    fread(&sizes, sizeof(MeshSizes), 1, in);
	// vertices, normals, and textures all written as doubles but floats expected
	double temp[3];
    // vertices
    vertices.resize(sizes.nVertices);
    for (int i = 0; i < sizes.nVertices; i++) {
		vec3 &v = vertices[i];
		if (fread(temp, sizeof(double), 3, in) != 3) {
            printf("ReadBinary: bad vertex\n");
            return false;
        }
		for (int i = 0; i < 3; i++)
			v[i] = (float) temp[i];
	}
	// normals
	if (vertexNormals)
        vertexNormals->resize(sizes.nNormals);
	for (int i = 0; i < sizes.nNormals; i++) {
		if (fread(temp, sizeof(double), 3, in) != 3) {
            printf("ReadBinary: bad normal\n");
            return false;
        }
		if (vertexNormals) {
			vec3 &v = (*vertexNormals)[i];
			for (int i = 0; i < 3; i++)
				v[i] = (float) temp[i];
		}
	}
	// textures
	if (vertexTextures)
        vertexTextures->resize(sizes.nTextures);
    for (int i = 0; i < sizes.nTextures; i++) {
		if (fread(temp, sizeof(double), 2, in) != 2) {
            printf("ReadBinary: bad texture\n");
            return false;
        }
		if (vertexTextures) {
			vec2 &v = (*vertexTextures)[i];
			for (int i = 0; i < 2; i++)
				v[i] = (float) temp[i];
		}
	}
	// types
	int itemp;
	if (vertexTypes)
		vertexTypes->resize(sizes.nVertexTypes);
    for (int i = 0; i < sizes.nVertexTypes; i++) {
		if (fread(&itemp, sizeof(int), 1, in) != 1) {
            printf("ReadBinary: bad vertex type\n");
            return false;
        }
		if (vertexTypes)
			(*vertexTypes)[i] = itemp;
	}
	// segments
	int2 s;
    for (int i = 0; i < sizes.nSegments; i++)
		if (fread(&s, sizeof(int), 2, in) != 2) {
            printf("ReadBinary: bad segment\n");
            return false;
        }
	// triangles and triangleGroups
    triangles.resize(sizes.nTriangles);
	if (triangleGroups)
		triangleGroups->resize(sizes.nTriangles);
    for (int i = 0; i < sizes.nTriangles; i++) {
		if (fread(&triangles[i], sizeof(int), 3, in) != 3) {
            printf("ReadBinary: bad triangle\n");
            return false;
        }
		int tmp;
		if (fread(triangleGroups? &((*triangleGroups)[i]) : &tmp, sizeof(int), 1, in) != 1) {
            printf("ReadBinary: bad triangle.g\n");
            return false;
        }
    }
	// polygons
	vector<int> p;
	for (int i = 0; i < sizes.nPolygons; i++) {
		int nvids, vids[100];
		if (fread(&nvids, sizeof(int), 1, in) != 1) {
			printf("ReadBinary: bad polygon\n");
			return false;
		}
		if (fread(vids, sizeof(int), nvids, in) != nvids) {
			printf("ReadBinary: bad polygon\n");
			return false;
		}
	}
	// edges
	int2 e;
	if (edges)
		edges->resize(sizes.nEdges);
    for (int i = 0; i < sizes.nEdges; i++) {
		if (fread(&e, sizeof(int), 2, in) != 2) {
            printf("ReadBinary: bad segment\n");
            return false;
        }
		if (edges)
			(*edges)[i] = e;
	}
    // finish
    return true;
} // end ReadBinaryObj

// ASCII OBJ

bool ReadAsciiObj(char          *filename,
				  vector<vec3>	&vertices,
				  vector<int3>	&triangles,
				  vector<vec3>	*vertexNormals,
				  vector<vec2>	*vertexTextures,
				  vector<int>	*triangleGroups) {
	// read 'object' file (Alias/Wavefront .obj format); return true if successful;
	// polygons are assumed simple (ie, no holes and not self-intersecting);
	// some file attributes are not supported by this implementation;
	// obj format indexes vertices from 1
	FILE *in = fopen(filename, "r");
	if (!in)
		return false;
	vec2 t;
	vec3 v;
	int group = 0;
	static const int LineLim = 1000, WordLim = 100;
	char line[LineLim], word[WordLim];
	for (int lineNum = 0;; lineNum++) {
		if (feof(in))                            // hit end of file
			break;
		fgets(line, LineLim, in);               // \ line continuation not supported
		if (strlen(line) >= LineLim-1) {         // getline reads LineLim-1 max
			printf("line %d too long", lineNum);
			return false;
		}
		char *ptr = line;
		if (!ReadWord(ptr, word, WordLim))
			continue;
		else if (*word == '#')
			continue;
		else if (!_stricmp(word, "g"))
			// this implementation: group field significant only if integer
			// .obj format, however, supported arbitrary string identifier
			sscanf(ptr, "%d", &group);
		else if (!_stricmp(word, "v")) {         // read vertex coordinates
			if (sscanf(ptr, "%g%g%g", &v.x, &v.y, &v.z) != 3) {
				printf("bad line %d in object file", lineNum);
				return false;
			}
			vertices.push_back(vec3(v.x, v.y, v.z));
		}
		else if (!_stricmp(word, "vn")) {        // read vertex normal
			if (sscanf(ptr, "%g%g%g", &v.x, &v.y, &v.z) != 3) {
				printf("bad line %d in object file", lineNum);
				return false;
			}
			if (vertexNormals)
				vertexNormals->push_back(vec3(v.x, v.y, v.z));
		}
		else if (!_stricmp(word, "vt")) {        // read vertex texture
			if (sscanf(ptr, "%g%g", &t.x, &t.y) != 2) {
				printf("bad line in object file");
				return false;
			}
			if (vertexTextures)
				vertexTextures->push_back(vec2(t.x, t.y));
		}
		else if (!_stricmp(word, "f")) {         // read triangle or polygon
			static vector<int> vids;
			vids.resize(0);
			while (ReadWord(ptr, word, WordLim)) { // read arbitrary # face vid/tid/nid        
				// set texture and normal pointers to preceding /
				char *tPtr = strchr(word+1, '/');    // pointer to /, or null if not found
				char *nPtr = tPtr? strchr(tPtr+1, '/') : NULL;
				// use of / is optional (ie, '3' is same as '3/3/3')
				// convert to vid, tid, nid indices (vertex, texture, normal)
				int vid = atoi(word);
				int tid = tPtr && *++tPtr != '/'? atoi(tPtr) : vid;
				int nid = nPtr && *++nPtr != 0? atoi(nPtr) : vid;
				// standard .obj is indexed from 1, mesh indexes from 0
				vid--;
				tid--;
				nid--;
				if (vid < 0 || tid < 0 || nid < 0) { // atoi = 0 is conversion failure
					printf("bad format on line %d\n", lineNum);
					return false;
				}
				vids.push_back(vid);
				// ignore tid, set nid for corresponding vertex
			}
			if (vids.size() == 3) {
				// create triangle
				triangles.push_back(int3(vids[0], vids[1], vids[2]));
				if (triangleGroups)
					triangleGroups->push_back(group);
			}
			else {
				// create polygon as nvids-2 triangles
				int nids = vids.size();
				for (int i = 1; i < nids-1; i++) {
					triangles.push_back(int3(vids[0], vids[i], vids[(i+1)%nids]));
					if (triangleGroups)
						triangleGroups->push_back(group);
				}
			}
		}
		else if (*word == 0 || *word== '\n')                     // skip blank line
			continue;
		else {                                     // unrecognized attribute
			// printf("unsupported attribute in object file: %s", word);
			continue; // return false;
		}
	} // end read til end of file
	return true;
} // end ReadAsciiObj
