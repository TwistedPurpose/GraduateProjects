// Soren Ludwig

#include "glew.h"
#include "freeglut.h"
#include "GLSL.h"


// Shaders

float points[][2] = { { -.5, -.5 },{ -.5, .75 },{ -.8, .75 },{ -.8, -.5 } ,{ -.8, -.9 } ,{ .2, -.9 } ,{ .2, -.5 } };

float colors[][3] = { { 1, 0, 0 },{ 0, 1, 0 },{ 0, 0, 1 },{ 1, 0, 0 },{ 0, 1, 0 },{ 0, 0, 1 },{ 1, 0, 0 } };

int pointSize = sizeof(points);
int colorSize = sizeof(colors);

GLuint vBufferID = -1;

GLuint shaderProgramID = 0;

char *vertexShader = "\
	in vec3 vPosition; \n\
	in vec3 vColor; \n\
	varying out vec4 color;																			\n\
	void main() {																					\n\
		gl_Position = vec4(vPosition, 1);											\n\
		color = vec4(vColor,1);														\n\
	}\n";

char *fragmentShader = "\
	in vec4 color;																					\n\
	varying out vec4 fColor;																		\n\
	void main()	{																					\n\
        fColor = color;																				\n\
	}\n";


// GLUT callback and application

void InitVertexBuffer()
{
	// create a vertex buffer for the vertex data, and make it the active buffer
	glGenBuffers(1, &vBufferID);
	glBindBuffer(GL_ARRAY_BUFFER, vBufferID);

	// allocate memory for vertex locations and colors
	glBufferData(GL_ARRAY_BUFFER, pointSize + colorSize, NULL, GL_STATIC_DRAW);

	// load data to sub-buffers
	glBufferSubData(GL_ARRAY_BUFFER, 0, pointSize, points);
	// start at beginning of buffer, for length of points array
	glBufferSubData(GL_ARRAY_BUFFER, pointSize, colorSize, colors);
	// start at end of points array, for length of colors array
}

void SetupVertexFeeder()
{
	// connect named vertex shader inputs (vPosition, vColor) to GPU sub-buffers
	

	// connect position input
	GLuint vPosition = glGetAttribLocation(shaderProgramID, "vPosition");
	glEnableVertexAttribArray(vPosition);
	glVertexAttribPointer(vPosition, 2, GL_FLOAT, GL_FALSE, 0, (void *)0);
	// 2 means 2D, but 3D vertex (z=0) is sent to vertex shader
	// the last argument is offset of data within vertex buffer

	// connect color input
	GLuint vColor = glGetAttribLocation(shaderProgramID, "vColor");
	glEnableVertexAttribArray(vColor);
	glVertexAttribPointer(vColor, 3, GL_FLOAT, GL_FALSE, 0, (void *)pointSize);

}

void Close()
{
	// unbind vertex buffer and free GPU memory
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &vBufferID);
}

void Display()
{
	// the letter B: 9 triangles, each with 3 indices into the vertex array

	static int triangles[][3] = { { 0, 1, 3 },{ 1, 2, 3 },{ 3, 4, 6 },{ 4, 6,5 } };
    glClear(GL_COLOR_BUFFER_BIT);
    // draw triangles, sending vertices to shader according to triangle indices
	SetupVertexFeeder();
    glDrawElements(GL_TRIANGLES, sizeof(triangles)/sizeof(int), GL_UNSIGNED_INT, triangles);
    glFlush();
}

void main(int ac, char **av)
{
    // init window
    glutInit(&ac, av);
	
    glutInitWindowSize(400, 400);
    glutCreateWindow("Letter from Triangles");
	glClearColor(0, 0, 0, 1);
	// obtain OpenGL extension bindings, init shaders
	glewInit();
	shaderProgramID = GLSL::LinkProgramViaCode(vertexShader, fragmentShader);
	InitVertexBuffer();
	if (!shaderProgramID) {
		printf("Error linking shader program\n");
		getchar();
	}
	
	glutCloseFunc(Close);
	glUseProgram(shaderProgramID);
    // GLUT callbacks
    glutDisplayFunc(Display);
    // begin event handler
    glutMainLoop();
}
