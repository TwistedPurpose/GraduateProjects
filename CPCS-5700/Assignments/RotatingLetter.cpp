// RotatingLetter.cpp: use vertex buffer to draw 'B'
// Soren Ludwig

#include <stdio.h>
#include <time.h>
#include <glew.h>
#include <freeglut.h>
#include "GLSL.h"

// Application Data

GLuint vBufferID = -1;    // GPU vertex buffer ID, valid if >= 0
GLuint shaderProgram = 0; // GLSL program ID, valid if > 0

// 10 2D vertex locations for 'B'
float points[][2] = {{-.15f, .125f}, {-.5f,  -.75f}, {-.5f,  .75f}, {.17f,  .75f}, { .38f, .575f},
	                 { .38f,  .35f}, { .23f, .125f}, {.5f, -.125f}, { .5f, -.5f},  {.25f, -.75f}};

// 10 colors
float colors[][3] = {{ 1, 1, 1}, { 1, 0, 0}, {.5, 0, 0}, {1, 1, 0},  {.5, 1, 0},
	                 { 0, 1, 0}, { 0, 1, 1}, {0, 0, 1},  { 1, 0, 1}, {.5, 0, .5}};

// 9 triangles
int triangles[][3] = {{0, 1, 2}, {0, 2, 3}, {0, 3, 4}, {0, 4, 5},
                      {0, 5, 6}, {0, 6, 7}, {0, 7, 8}, {0, 8, 9}, {0, 9, 1}};

time_t startTime = clock();
static float degPerSec = 30;

float degreeOfRotation = 0;

// Simple Shaders

// the vertex shader accepts as inputs a position and a color pulled from GPU memory
// the inputs are passed unchanged to the shader output (using a specified output for
// color and a pre-built output for position); the resulting output vertices are
// assembled into triangles that are clipped and sent to the rasterizer

char *vertexShader = "\
	#version 130							\n\
	in vec3 point;							\n\
	in vec3 color;							\n\
	out vec4 vColor;						\n\
	uniform float radAng = 0; // amount of rotation, in radians \n\
	vec2 Rotate2D(vec2 v) { \n\
		float c = cos(radAng), s = sin(radAng); \n\
		return vec2(c*v.x - s*v.y, s*v.x + c*v.y); \n\
	}\n\
	void main() {							\n\
		vec2 r = Rotate2D(point.xy); \n\
		r.x = r.x*sin(radAng); \n\
		r.y = r.y*sin(radAng); \n\
		gl_Position = vec4(r, 0, 1);		\n\
	    vColor = vec4(color, 1);			\n\
	}										\n";

// the rasterizer converts triangles into pixels, interpolating the color and
// sending it to the fragment shader, which passes it unchanged as pixel value

char *fragmentShader = "\
	#version 130							\n\
	in vec4 vColor;							\n\
	out vec4 fColor;						\n\
	void main() {							\n\
        fColor = vColor;					\n\
	}										\n";

// Initialization

// https://cs.mtsu.edu/~jhankins/files/4250/notes/GLUTMouseTutorial.pdf This is where I learned how to do this
void MouseMove(int x, int y)
{
	// Respond to mouse button presses.
	// If button1 pressed, mark this state so we know in motion function.
	degreeOfRotation = x;
}

void InitVertexBuffer() {
    // create GPU buffer to hold positions and colors, and make it the active buffer
    glGenBuffers(1, &vBufferID);
    glBindBuffer(GL_ARRAY_BUFFER, vBufferID);

    // allocate memory for vertex positions and colors
    glBufferData(GL_ARRAY_BUFFER, sizeof(points)+sizeof(colors), NULL, GL_STATIC_DRAW);

    // load data to sub-buffers
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(points), points);
        // start at beginning of buffer, for length of positions array
    glBufferSubData(GL_ARRAY_BUFFER, sizeof(points), sizeof(colors), colors);
        // start at end of positions array, for length of colors array
}

void SetupVertexFeeder() {
    // those arrays within the vertex buffer that provide input to the vertex
    // shader (ie, position, color) must be connected to the shader - with
    // GLSLv4.0, a layout parameter can be given directly to a shader; pre-v4.0,
    // each named shader input must be associated with an array in the vertex buffer
    // this is done via the following attribute calls, after linking the shader program

    // associate position input to shader with position array in vertex buffer 
    GLuint pointId = glGetAttribLocation(shaderProgram, "point");
    glEnableVertexAttribArray(pointId);
    glVertexAttribPointer(pointId, 2, GL_FLOAT, GL_FALSE, 0, (void *) 0);
        // last argument is offset of data within vertex buffer
		// 2nd arg says 2D, but 3D vertex is sent to vertex shader (with z=0, w=1)

    // associate color input to shader with color array in vertex buffer
    GLuint colorId = glGetAttribLocation(shaderProgram, "color");
    glEnableVertexAttribArray(colorId);
    glVertexAttribPointer(colorId, 3, GL_FLOAT, GL_FALSE, 0, (void *) sizeof(points));
}

// Application

void Idle() {
	glutPostRedisplay();
}

void Display() {
    glClearColor(.5, .5, .5, 1);
    glClear(GL_COLOR_BUFFER_BIT);
	float dt = (float)(clock() - startTime) / CLOCKS_PER_SEC; // elapsed time in secs
	GLint id = glGetUniformLocation(shaderProgram, "radAng");
	if (id >= 0)
		glUniform1f(id, (3.1415f / 180.f)*degreeOfRotation);
		//glUniform1f(id, (3.1415f / 180.f)*dt*degPerSec);	// update radAng

	SetupVertexFeeder();
    glDrawElements(GL_TRIANGLES, sizeof(triangles)/sizeof(int), GL_UNSIGNED_INT, triangles);
    glFlush();
}

void Close() {
	// unbind vertex buffer and free GPU memory
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &vBufferID);
}

void main(int argc, char **argv) {
	// init window
    glutInit(&argc, argv);
    glutInitWindowSize(400, 400);
    glutCreateWindow("Rotating Letter");
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

	// GLUT callbacks and event loop
	glutIdleFunc(Idle);
    glutDisplayFunc(Display);
    glutCloseFunc(Close);
	glutPassiveMotionFunc(MouseMove);
    glutMainLoop();
}
