// CandlestickMesh.cpp
// By Soren Ludwig

#include <stdio.h>
#include <glew.h>
#include <freeglut.h>
#include "GLSL.h"
#include <time.h>
#include <vector>
#include "Mesh.h"

using namespace std;

// Application Data

GLuint vBufferID = -1;    // GPU vertex buffer ID 
GLuint shaderProgram = 0; // GLSL program ID

float speed = .3f;

vec2  mouseDown;		// location of last mouse down  
vec2  rotOld, rotNew;	// .x is rotation about Y-axis, .y about X-axis 
mat4  rotM;		// set during mouse motion, used by Display 

vector<vec3> points;  	// 3D mesh vertices
vector<int3> triangles; // vertex indices

float colors[][3] = { { 1, 1, 1 },{ 1, 0, 0 },{ .5, 0, 0 },{ 1, 1, 0 } };


// Vertex and Fragment (Pixel) Shaders

char *vertexShader = "\
	#version 130										\n\
	in vec3 point;										\n\
	in vec3 color;										\n\
	out vec4 vColor;									\n\
	uniform float radAng = 0;							\n\
	uniform float scale = 1;							\n\
	uniform mat4 view;									\n\
		vec3 hsv2rgb(vec3 c) {                                      \n\
		vec4 K = vec4(1., 2./3., 1./3., 3.);                  \n\
		vec3 p = abs(fract(c.xxx+K.xyz)*6.-K.www);            \n\
		return c.z * mix(K.xxx, clamp(p-K.xxx, 0., 1.), c.y); \n\
	}\n\
	void main() {								\n\
					\n\
		gl_Position = view*vec4(point, 1);			\n\
	    vColor = vec4(hsv2rgb(vec3(point.x, 1, 1)), 1);				\n\
	}\n";

char *fragmentShader = "\
	#version 130								\n\
	in vec4 vColor;								\n\
	out vec4 fColor;							\n\
	void main() {								\n\
        fColor = vColor;						\n\
	}\n";


void MouseButton(int butn, int state, int x, int y) {
	if (state == GLUT_DOWN)
		mouseDown = vec2((float)x, (float)y); // save for continuity
	if (state == GLUT_UP)
		rotOld = rotNew;					// save for continuity
}

void MouseDrag(int x, int y) {
	// called only if mouse button down
	vec2 mouse((float)x, (float)y);
	rotNew = rotOld + speed*(mouse - mouseDown);
	rotM = RotateY(rotNew.x)*RotateX(rotNew.y);
	glutPostRedisplay();
}


// Initialization

void InitVertexBuffer() {
    // create GPU buffer to hold positions and colors, and make it the active buffer
	int nPointBytes = points.size() * sizeof(vec3);
	void *pointsPtr = &points[0]; // pointer is to contiguous vector data
    glGenBuffers(1, &vBufferID);
    glBindBuffer(GL_ARRAY_BUFFER, vBufferID);
    // allocate memory for vertex positions and colors
    glBufferData(GL_ARRAY_BUFFER, nPointBytes, NULL, GL_STATIC_DRAW);
    // load data to sub-buffers
    glBufferSubData(GL_ARRAY_BUFFER, 0, nPointBytes, pointsPtr);
    //glBufferSubData(GL_ARRAY_BUFFER, sizeof(points), sizeof(colors), colors);
}

void SetVertexFeeder() {
    // associate position input to shader with position array in vertex buffer 
    GLuint pointId = glGetAttribLocation(shaderProgram, "point");
    glEnableVertexAttribArray(pointId);
	glVertexAttribPointer(pointId, 3, GL_FLOAT, GL_FALSE, 0, (void *)0);
    // associate color input to shader with color array in vertex buffer
    //GLuint colorId = glGetAttribLocation(shaderProgram, "color");
    //glEnableVertexAttribArray(colorId);
    //glVertexAttribPointer(colorId, 3, GL_FLOAT, GL_FALSE, 0, (void *) sizeof(points));
}

void Idle() {
	glutPostRedisplay();
}

void Display() {
	glEnable(GL_DEPTH_BUFFER);
	glClear(GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	GLint id = glGetUniformLocation(shaderProgram, "view");
	if (id >= 0)
		glUniformMatrix4fv(id, 1, true, (float *)&rotM);
    glClearColor(.5, .5, .5, 1);
    glClear(GL_COLOR_BUFFER_BIT);
	glDrawElements(GL_TRIANGLES, 3 * triangles.size(), GL_UNSIGNED_INT, &triangles[0]);

    glFlush();
}

// Application

void Close() {
	// unbind vertex buffer and free GPU memory
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &vBufferID);
}

void main(int argc, char **argv) {
	// init window
    glutInit(&argc, argv);
    glutInitWindowSize(400, 400);
    glutCreateWindow("Soren's Homework");
    glewInit();

	// build and use shader program
	shaderProgram = GLSL::LinkProgramViaCode(vertexShader, fragmentShader);
	if (!shaderProgram) {
		printf("Can't link shader program\n");
		getchar();
		return;
	}
    glUseProgram(shaderProgram);

	if (!ReadAsciiObj("Candlestick.obj", points, triangles)) {
		printf("Failed to read obj file\n");
		getchar();		// pause til user keystroke
		return;
	}
	printf("%i vrts, %i tris\n", points.size(), triangles.size()); // apprise user

	Normalize(points, .8f);	// in Mesh.h

	// allocate vertex memory in the GPU and link it to the vertex shader
    InitVertexBuffer();
    SetVertexFeeder();

	// GLUT callbacks and event loop
	glutMotionFunc(MouseDrag);
	glutMouseFunc(MouseButton);
    glutDisplayFunc(Display);
    glutCloseFunc(Close);
	glutIdleFunc(Idle);
    glutMainLoop();
}
