// LetterApp.cpp: simplest program to draw colored triangle with GLSL
// Assignment for Soren Ludwig

#include <glew.h>
#include <freeglut.h>
#include "GLSL.h"

// simple vertex shader: output one of three RGBA colors and 4D points
char *vertexShader = "\
	#version 130															\n\
	out vec4 color;															\n\
	void main()																\n\
	{																		\n\
													\n\
		vec2 points[] = vec2[7](vec2(-.5,-.5), vec2(-.5,.75), vec2(-.8,.75),vec2(-.8,-.5),vec2(-.8,-.9), vec2(.2,-.9), vec2(.2,-.5));	\n\
																				\n\
		// gl_Position, gl_VertexID are built-in variables					\n\
		gl_Position = vec4(points[gl_VertexID],0, 1);						\n\
		vec3 pointColor = vec3(points[gl_VertexID][0],points[gl_VertexID][1], -(points[gl_VertexID][0]+points[gl_VertexID][1])/2.0);						\n\
		color = vec4(pointColor, 1);								\n\
	}\n";

// trivial fragment shader: pass input color out as pixel value
char *fragmentShader = "\
	#version 130															\n\
	in vec4 color;															\n\
	out vec4 fColor;														\n\
	void main()																\n\
	{																		\n\
        fColor = color;														\n\
	}\n";

// GLUT display callback
void Display()
{
	int triangles[4][3] = { { 0, 1, 3 },{ 1, 2, 3 }, { 3, 4, 6 },{ 4, 6,5} };

    glClearColor(0, 0, 0, 1);						// set clear color
    glClear(GL_COLOR_BUFFER_BIT);					// clear frame buffer
	glDrawElements(GL_TRIANGLES, 12, GL_UNSIGNED_INT, triangles);
    glFlush();										// ensure all gl commands executed
}

void main(int argc, char **argv)
{
	// init window
    glutInit(&argc, argv);							// init GL window
    glutInitWindowSize(400, 400);					// width, height
    glutCreateWindow("Simplest GLSL Triangle");		// title
    glewInit();										// establish GL extensions
    // build shader program from inline GLSL code
	GLuint program = InitShader(vertexShader, fragmentShader);
	if (!program) {
        printf("Error linking shader program\n");	// report error
		getchar();									// keep app open
	}
	glUseProgram(program);							// enable shader program
	glutDisplayFunc(Display);						// set GLUT display callback
	glutMainLoop();									// no return from this event loop
}
