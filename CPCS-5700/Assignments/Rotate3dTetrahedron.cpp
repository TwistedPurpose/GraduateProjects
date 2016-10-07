// Rotate3dTetrahedron.cpp: rotate a tetrahedron with the mouse

#include <stdio.h>
#include <glew.h>
#include <freeglut.h>
#include "GLSL.h"
#include <time.h>

// Application Data

GLuint vBufferID = -1;    // GPU vertex buffer ID 
GLuint shaderProgram = 0; // GLSL program ID

float speed = .3f;

vec2  mouseDown;		// location of last mouse down  
vec2  rotOld, rotNew;	// .x is rotation about Y-axis, .y about X-axis 
mat4  rotM;		// set during mouse motion, used by Display 


//float points[][2] = {{-.15f, .125f}, {-.5f,  -.75f}, {-.5f,  .75f}, {.17f,  .75f}, { .38f, .575f},
//	                 { .38f,  .35f}, { .23f, .125f}, {.5f, -.125f}, { .5f, -.5f},  {.25f, -.75f}};

float points[][3] = { { -.9f,-.9f,0 },{ .9f,-.9f,0 },{ 0,.9f,0 },{ 0,0,.9f } };


//float colors[][3] = {{ 1, 1, 1}, { 1, 0, 0}, {.5, 0, 0}, {1, 1, 0},  {.5, 1, 0},
//	                 { 0, 1, 0}, { 0, 1, 1}, {0, 0, 1},  { 1, 0, 1}, {.5, 0, .5}};

float colors[][3] = { { 1, 1, 1 },{ 1, 0, 0 },{ .5, 0, 0 },{ 1, 1, 0 } };


//int triangles[][3] = {{0, 1, 2}, {0, 2, 3}, {0, 3, 4}, {0, 4, 5},
//                      {0, 5, 6}, {0, 6, 7}, {0, 7, 8}, {0, 8, 9}, {0, 9, 1}};

int triangles[][3] = { { 0, 1, 2 },{ 0, 2, 3 },{ 3, 2, 1 },{ 0, 3, 1 } };

// Vertex and Fragment (Pixel) Shaders

char *vertexShader = "\
	#version 130										\n\
	in vec3 point;										\n\
	in vec3 color;										\n\
	out vec4 vColor;									\n\
	uniform float radAng = 0;							\n\
	uniform float scale = 1;							\n\
	uniform mat4 view;									\n\
	void main() {								\n\
					\n\
		gl_Position = view*vec4(point, 1);			\n\
	    vColor = vec4(color, 1);				\n\
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
    glGenBuffers(1, &vBufferID);
    glBindBuffer(GL_ARRAY_BUFFER, vBufferID);
    // allocate memory for vertex positions and colors
    glBufferData(GL_ARRAY_BUFFER, sizeof(points)+sizeof(colors), NULL, GL_STATIC_DRAW);
    // load data to sub-buffers
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(points), points);
    glBufferSubData(GL_ARRAY_BUFFER, sizeof(points), sizeof(colors), colors);
}

void SetVertexFeeder() {
    // associate position input to shader with position array in vertex buffer 
    GLuint pointId = glGetAttribLocation(shaderProgram, "point");
    glEnableVertexAttribArray(pointId);
	glVertexAttribPointer(pointId, 3, GL_FLOAT, GL_FALSE, 0, (void *)0);
    // associate color input to shader with color array in vertex buffer
    GLuint colorId = glGetAttribLocation(shaderProgram, "color");
    glEnableVertexAttribArray(colorId);
    glVertexAttribPointer(colorId, 3, GL_FLOAT, GL_FALSE, 0, (void *) sizeof(points));
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
    glDrawElements(GL_TRIANGLES, sizeof(triangles)/sizeof(int), GL_UNSIGNED_INT, triangles);

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
    glutCreateWindow("Rotate Teatrahedron");
    glewInit();

	// build and use shader program
	shaderProgram = GLSL::LinkProgramViaCode(vertexShader, fragmentShader);
	if (!shaderProgram) {
		printf("Can't link shader program\n");
		getchar();
		return;
	}
    glUseProgram(shaderProgram);

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
