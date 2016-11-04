// ShadeMesh.cpp: Phong shade .obj mesh
// Copyright (c) Jules Bloomenthal, 2014
// All rights reserved

#include <stdio.h>
#include <glew.h>
#include <freeglut.h>
#include "GLSL.h"
#include "Mesh.h"

// Application Data

char        *objFilename = "C:/Users/jules/SeattleUniversity/Web/Models/MonkeyMan.obj";
vector<vec3> points;				// 3D mesh vertices
vector<vec3> normals;				// vertex normals
vector<int3> triangles;				// triplets of vertex indices
vec3         lightSource(1, 1, 0);	// for Phong shading
GLuint		 vBufferID = -1;	    // GPU vertex buffer ID
GLuint		 shaderProgram = 0;		// GLSL program ID

// Shaders

char *vertexShader = "\
	#version 130														\n\
	in vec3 point;														\n\
	in vec3 normal;														\n\
	out vec3 vPoint;													\n\
	out vec3 vNormal;													\n\
    uniform mat4 modelview;												\n\
	uniform mat4 persp;													\n\
	void main() {														\n\
		vec4 hPosition = modelview*vec4(point, 1);						\n\
		vPoint = hPosition.xyz;											\n\
		gl_Position = persp*hPosition;									\n\
		vNormal = (modelview*vec4(normal, 0)).xyz;						\n\
	}";

char *fragmentShader = "\
    #version 130														\n\
	in vec3 vPoint;														\n\
	in vec3 vNormal;													\n\
	out vec4 fColor;													\n\
	uniform vec3 light;													\n\
	uniform vec4 color = vec4(.7, .7, 0, 1);							\n\
	float PhongIntensity(vec3 pos, vec3 nrm) {							\n\
		vec3 N = normalize(nrm);           // surface normal			\n\
        vec3 L = normalize(light-pos);     // light vector				\n\
        vec3 E = normalize(pos);           // eye vertex				\n\
        vec3 R = reflect(L, N);            // highlight vector			\n\
        float d = abs(dot(N, L));          // two-sided diffuse			\n\
        float s = abs(dot(R, E));          // two-sided specular		\n\
		return clamp(d+pow(s, 50), 0, 1);								\n\
	}																	\n\
    void main() {														\n\
		float intensity = PhongIntensity(vPoint, vNormal);				\n\
		fColor = vec4(intensity*color.rgb, color.a);					\n\
	}";

// Initialization

void SetVertexFeeder() {
    // link shader input with points array at start of vertex buffer
	int pointId = GLSL::EnableVertexAttribute(shaderProgram, "point");
	glVertexAttribPointer(pointId, 3, GL_FLOAT, GL_FALSE, 0, (void *) 0);
    // link shader input with normal array following points array in vertex buffer
	int normalId = GLSL::EnableVertexAttribute(shaderProgram, "normal");
	int sizePts = points.size()*sizeof(vec3);
	glVertexAttribPointer(normalId, 3, GL_FLOAT, GL_FALSE, 0, (void *) (sizePts));
}

void InitVertexBuffer() {
    // create GPU buffer, make it the active buffer
    glGenBuffers(1, &vBufferID);
    glBindBuffer(GL_ARRAY_BUFFER, vBufferID);
    // allocate memory for vertex positions and normals
	int sizePts = points.size()*sizeof(vec3);
	int sizeNrms = normals.size()*sizeof(vec3);
    glBufferData(GL_ARRAY_BUFFER, sizePts+sizeNrms, NULL, GL_STATIC_DRAW);
    // copy data
	glBufferSubData(GL_ARRAY_BUFFER, 0, sizePts, &points[0]);
	glBufferSubData(GL_ARRAY_BUFFER, sizePts, sizeNrms, &normals[0]);
}

// Interactive Rotation

vec2 mouseDown;				// for each mouse down, need start point
vec2 rotOld, rotNew;	    // previous, current rotations
mat4 rotM;				    // MouseDrag sets, Display uses

void MouseButton(int butn, int state, int x, int y) {
	if (state == GLUT_DOWN)
		mouseDown = vec2((float) x, (float) y);
	if (state == GLUT_UP)
		rotOld = rotNew;
}

void MouseDrag(int x, int y) {
	vec2 mouse((float) x, (float) y);
	rotNew = rotOld+.3f*(mouse-mouseDown);
		// new rotations depend on old plus mouse distance from mouseDown
	rotM = RotateY(rotNew.x)*RotateX(rotNew.y);
		// rot.x is about Y-axis, rot.y is about X-axis
	glutPostRedisplay();
}

// Application

void Display() {
	// update and send modelview matrix to vertex shader
	mat4 modelview = Translate(0, 0, -5)*rotM;
	glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "modelview"), 1, true, (float *) &modelview[0][0]);
	// update and send perspective matrix to vertex shader
	float fov = 15, nearPlane = -.001f, farPlane = -500;
	float aspect = (float) glutGet(GLUT_WINDOW_WIDTH) / (float) glutGet(GLUT_WINDOW_HEIGHT);
	mat4 persp = Perspective(fov, aspect, nearPlane, farPlane);
	glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "persp"), 1, true, (float *) &persp[0][0]);
	// transform homogeneous light and send to fragment shader
	vec4 hLight = modelview*vec4(lightSource, 1);
	vec3 xlight(hLight.x, hLight.y, hLight.z);
	glUniform3fv(glGetUniformLocation(shaderProgram, "light"), 1, (float *) &xlight);
	// clear screen to grey
    glClearColor(.5f, .5f, .5f, 1);
    glClear(GL_COLOR_BUFFER_BIT);
	// enable transparency
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	// use z-buffer
	glEnable(GL_DEPTH_BUFFER);
	glClear(GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	// draw triangles and finish
    glDrawElements(GL_TRIANGLES, 3*triangles.size(), GL_UNSIGNED_INT, &triangles[0]);
    glFlush();
}

void Close() {
	// unbind vertex buffer, free GPU memory
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &vBufferID);
}

void main(int argc, char **argv) {
	// init window
    glutInit(&argc, argv);
    glutInitWindowSize(400, 400);
    glutCreateWindow("Shader Example");
    glewInit();
	// build, use shader program
	shaderProgram = GLSL::LinkProgramViaCode(vertexShader, fragmentShader);
	if (!shaderProgram) {
		printf("Can't link shader program\n");
		getchar();
		return;
	}
    glUseProgram(shaderProgram);
	// read Alias/Wavefront "obj" formatted mesh file
	if (!ReadAsciiObj(objFilename, points, triangles, &normals)) {
		printf("Failed to read obj file\n");
		getchar();
		return;
	}
	printf("%i vertices, %i triangles", points.size(), triangles.size());
	if (normals.size())
		printf(", %i normals", normals.size());
	printf("\n");
	// scale/move model to uniform +/-1, approximate normals if none from file
	Normalize(points, .8f);
//	if (normals.size() != points.size())
		// some objects, like MonkeyMan.obj, have normals, but they're bad
	SetVertexNormals(points, triangles, normals);
	// allocate vertex memory in the GPU, link it to the vertex shader
    InitVertexBuffer();
    SetVertexFeeder();
	// GLUT callbacks, event loop
    glutDisplayFunc(Display);
	glutMouseFunc(MouseButton);
	glutMotionFunc(MouseDrag);
    glutCloseFunc(Close);
    glutMainLoop();
}
