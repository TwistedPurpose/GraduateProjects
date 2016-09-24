// ClearGlslApp.cpp: minimal GLSL application to clear window
// (c) 2016 J. Bloomenthal

#include <gl/glew.h>
#include <gl/freeglut.h>

const char *vertexShader = "\
	// output one of four window corners										\n\
	#version 150																\n\
	void main()																	\n\
	{																			\n\
		vec2 pts[] = vec2[4](vec2(-1,-1), vec2(-1,1), vec2(1,1), vec2(1,-1));	\n\
		// gl_Position, gl_VertexID are 'built-in' variables					\n\
		gl_Position = vec4(pts[gl_VertexID], 0, 1);								\n\
	}																			\n";

const char *pixelShader = "\
	// output opaque red														\n\
	#version 150																\n\
	out vec4 fColor;															\n\
	void main()																	\n\
	{																			\n\
        fColor = vec4(1, 0, 0, 1);												\n\
	}																			\n";

void Display() {
	// called whenever window manager displays the application
	glDrawArrays(GL_QUADS, 0, 4);							// display quadrilateral (entire window)
    glFlush();												// ensure GL ops complete, screen refresh
}

void AddShader(GLuint program, const char **source, int type) {
	GLuint shader = glCreateShader(type);					// create shader of type vertex or fragment/pixel
	glShaderSource(shader, 1, source, NULL);				// download source code
	glCompileShader(shader);								// compile source
	glAttachShader(program, shader);						// include shader in future link
}

void main(int argc, char **argv) {							// application entry
    glutInit(&argc, argv);									// prepare GLUT for window manager
    glutInitWindowSize(400, 400);							// specify window size
    glutCreateWindow("Clear Demo (GLSL)");					// create named window
    glewInit();												// wrangle GL extensions
	GLuint program = glCreateProgram();						// create GLSL shader program
	AddShader(program, &vertexShader, GL_VERTEX_SHADER);	// add vertex shader to GLSL program
	AddShader(program, &pixelShader, GL_FRAGMENT_SHADER);	// add pixel shader
	glLinkProgram(program);									// link shader program
	glUseProgram(program);									// enable shader program
	glutDisplayFunc(Display);								// set GLUT display callback
    glutMainLoop();											// enter GLUT event loop
}
