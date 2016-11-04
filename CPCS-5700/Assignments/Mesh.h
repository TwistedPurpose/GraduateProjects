/*	==============================
    Mesh.h - 3D mesh of triangles
    Copyright (c) Jules Bloomenthal, 2012-2016
    All rights reserved
	=============================== */

#ifndef MESH_HDR
#define MESH_HDR

#include <vector>
#include "mat.h"

using std::vector;

class Vertex {
public:
	vec3 point;
	vec3 normal;
	vec2 uv;
	Vertex() { }
	Vertex(vec3 &p, vec3 &n, vec2 &t) : point(p), normal(n), uv(t) { }
};

bool ReadSTL(	   char		    *filename,
				   vector<vec3>	&vertices,
				   vector<int3>	&triangles,
				   vector<vec3>	*vertexNormals = NULL);

// Alias/Wavefront "obj" format

bool ReadBinaryObj(char         *filename,
				  vector<vec3>	&vertices,
				  vector<int3>	&triangles,
				  vector<int2>  *edges,
				  vector<vec3>	*vertexNormals  = NULL,
				  vector<vec2>	*vertexTextures = NULL,
				  vector<int>   *vertexTypes    = NULL,
				  vector<int>	*triangleGroups = NULL);


bool ReadAsciiObj(char          *filename,
				  vector<vec3>	&vertices,
				  vector<int3>	&triangles,
				  vector<vec3>	*vertexNormals  = NULL,
				  vector<vec2>	*vertexTextures = NULL,
				  vector<int>	*triangleGroups = NULL);

bool ReadAsciiObj(const char *filename, vector<Vertex> &vertices);
	// for use with glDrawArrays (not glDrawElements)
	// permits differnt uv/normal for a particular vertex location

void Normalize(vector<vec3> &vertices, float scale = 1);
	// translate and apply uniform scale so that vertices fit in -1,1 in X,Y and 0,1 in Z

void SetVertexNormals(vector<vec3> &vertices,
					  vector<int3> &triangles,
					  vector<vec3> &normals);
	// compute/recompute vertex normals as the average of surrounding triangle normals

#endif
