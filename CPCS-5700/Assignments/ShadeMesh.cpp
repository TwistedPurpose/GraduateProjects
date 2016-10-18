// ShadeMesh.cpp
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
vec3 lightSource(1, 1, 0); // or any 3D location for the light you wish

vector<vec3> normals;
vector<vec3> points;  	// 3D mesh vertices
vector<int3> triangles; // vertex indices

// Vertex and Fragment (Pixel) Shaders

char *vertexShader = "\
	in vec3 position;							\n\
	in vec3 normal;							\n\
	out vec3 vPosition;							\n\
	out vec3 vNormal;							\n\
   	uniform mat4 modelview;						\n\
	uniform mat4 persp;							\n\
void main() {								\n\
		vec4 hPosition = modelview*vec4(position, 1);		\n\
		vPosition = hPosition.xyz;					\n\
		gl_Position = persp*hPosition;				\n\
		vNormal = (modelview*vec4(normal, 0)).xyz;		\n\
	}\n";

char *fragmentShader = "\
	in vec3 vPosition;							\n\
	in vec3 vNormal;							\n\
	out vec4 fColor;							\n\
	uniform vec3 light;							\n\
	uniform vec4 color = vec4(.7, .7, 0, 1);				\n\
	float PhongIntensity(vec3 position, vec3 normal){ \n\
		vec3 N = normalize(normal);							\n\
		vec3 L = normalize(light - vPosition);\n\
		vec3 E = normalize(vPosition);	\n\
		vec3 R = reflect(L,N); \n\
		float diffuse = abs(dot(N,L)); \n\
		float specular = abs(dot(R,E));\n\
		float result = diffuse+pow(specular,10); \n\
		if (result > 1) result = 1.0;\n\
		if (result < 0) result = 0.0; \n\
		return result;\n\
	} \n\
    void main() {								\n\
		float intensity = PhongIntensity(vPosition, vNormal);	\n\
		fColor = vec4(intensity*color.xyz, color.w);		\n\
		//fColor = color;\n\
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
	int sizePoints = points.size() * sizeof(vec3);
	int sizeNrms = normals.size() * sizeof(vec3);
	void *normalsPtr = &normals[0];
	void *pointsPtr = &points[0]; // pointer is to contiguous vector data
	glGenBuffers(1, &vBufferID);
	glBindBuffer(GL_ARRAY_BUFFER, vBufferID);
	
	glBufferData(GL_ARRAY_BUFFER, sizePoints + sizeNrms, NULL, GL_STATIC_DRAW);

	// load data
	glBufferSubData(GL_ARRAY_BUFFER, 0, sizePoints, pointsPtr);
	glBufferSubData(GL_ARRAY_BUFFER, sizePoints, sizeNrms, normalsPtr);

}

void SetVertexFeeder() {
	// associate position input to shader with position array in vertex buffer 
	GLuint pointId = glGetAttribLocation(shaderProgram, "position");
	glEnableVertexAttribArray(pointId);
	glVertexAttribPointer(pointId, 3, GL_FLOAT, GL_FALSE, 0, (void *)0);

	GLuint normalId = glGetAttribLocation(shaderProgram, "normal");
	if (normalId >= 0) {
		int sizeVerts = points.size() * sizeof(vec3);
		glEnableVertexAttribArray(normalId);
		glVertexAttribPointer(normalId, 3, GL_FLOAT, GL_FALSE, 0, (void *)(sizeVerts));
	}

}

void Idle() {
	glutPostRedisplay();
}

void Display() {
	glEnable(GL_DEPTH_BUFFER);
	glClear(GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	glClearColor(.5, .5, .5, 1);
	glClear(GL_COLOR_BUFFER_BIT);
	// update and send modelview matrix to vertex shader
	mat4 modelview = Translate(0, 0, -5)*rotM;
	GLint id = glGetUniformLocation(shaderProgram, "modelview");
	if (id >= 0)
		glUniformMatrix4fv(id, 1, true, (float *)&modelview[0][0]);

	// update and send perspective matrix to vertex shader
	float fov = 15, nearPlane = -.001f, farPlane = -500;
	float aspect = (float)glutGet(GLUT_WINDOW_WIDTH) / (float)glutGet(GLUT_WINDOW_HEIGHT);
	mat4 persp = Perspective(fov, aspect, nearPlane, farPlane);
	id = glGetUniformLocation(shaderProgram, "persp");
	if (id >= 0)
		glUniformMatrix4fv(id, 1, true, (float *)&persp[0][0]);

	// transform homogeneous light and update uniform
	vec4 hLight = modelview*vec4(lightSource, 1);
	vec3 xlight(hLight.x, hLight.y, hLight.z);
	id = glGetUniformLocation(shaderProgram, "light");
	if (id >= 0)
		glUniform3fv(id, 1, (float *)&xlight);

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

	if (!ReadAsciiObj("Cow.obj", points, triangles, &normals)) {
		printf("Failed to read obj file\n");
		getchar();		// pause til user keystroke
		return;
	}
	printf("%i vrts, %i tris, %i norms\n", points.size(), triangles.size(), normals.size()); // apprise user

	if (normals.size() != points.size())
		SetVertexNormals(points, triangles, normals);


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
