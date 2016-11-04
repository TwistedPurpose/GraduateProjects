// TextureCandleMesh.cpp: Phong shade .obj mesh with .tga texture
// Soren Ludwig

#include <stdio.h>
#include "glew.h"
#include "freeglut.h"
#include "Draw.h"
#include "GLSL.h"
#include "Mesh.h"
#include "Widget.h"

// Application Data

char        *objFilename = "Candlestick.obj";
char		*texFilename = "florent-lebrun-fallen.tga";

vector<Vertex> vertices;

vec3         lightSource(1, 1, 0);
GLuint		 shaderId = 0, vBufferId = -1, textureId = -1;

float scale = 3.0;

// Shaders

char *vertexShader = "\
	#version 130															\n\
	in vec3 point;															\n\
	in vec3 normal;															\n\
	in vec2 uv;																\n\
	out vec3 vPoint;														\n\
	out vec3 vNormal;														\n\
	out vec2 vUv;															\n\
    uniform mat4 modelview;													\n\
	uniform mat4 persp;														\n\
	uniform float scale;													\n\
	void main() {															\n\
		vec4 hPosition = modelview*vec4(point, 1);							\n\
		vPoint = hPosition.xyz;												\n\
		gl_Position = persp*hPosition;										\n\
		vNormal = (modelview*vec4(normal, 0)).xyz;							\n\
		vUv = scale*uv;															\n\
	}";

char *fragmentShader = "\
    #version 130															\n\
	in vec3 vPoint;															\n\
	in vec3 vNormal;														\n\
	in vec2 vUv;															\n\
	uniform vec3 light;														\n\
	uniform sampler2D textureImage;											\n\
	float PhongIntensity(vec3 pos, vec3 nrm) {								\n\
		vec3 N = normalize(nrm);           // surface normal				\n\
        vec3 L = normalize(light-pos);     // light vector					\n\
        vec3 E = normalize(pos);           // eye vector					\n\
        vec3 R = reflect(L, N);            // highlight vector				\n\
        float d = abs(dot(N, L));          // two-sided diffuse				\n\
        float s = abs(dot(R, E));          // two-sided specular			\n\
		return clamp(d+pow(s, 50), 0, 1);									\n\
	}																		\n\
    void main() {															\n\
		float intensity = PhongIntensity(vPoint, vNormal);					\n\
		gl_FragColor = vec4(intensity*texture(textureImage, vUv). rgb, 1);	\n\
	}";

// Vertex Buffering

void InitVertexBuffer() {
    // create GPU buffer, make it the active buffer
    glGenBuffers(1, &vBufferId);
    glBindBuffer(GL_ARRAY_BUFFER, vBufferId);
	// allocate and fill vertex buffer
	glBufferData(GL_ARRAY_BUFFER, vertices.size()*sizeof(Vertex), &vertices[0], GL_STATIC_DRAW);
}

// Input

void ReadObject(const char *filename) {
	vertices.resize(0);
	// read Alias/Wavefront "obj" formatted mesh file
	if (!ReadAsciiObj(filename, vertices)) {
		printf("Failed to read %s\n", filename);
		return;
	}
	// scale/move model to uniform +/-1
	int nvrts = vertices.size();
	vector<vec3> points(nvrts);
	printf("%i triangles\n", nvrts/3);
	for (int i = 0; i < nvrts; i++)
		points[i] = vertices[i].point;
	Normalize(points, .8f);
	for (int i = 0; i < nvrts; i++)
		vertices[i].point = points[i];
	// allocate vertex memory in the GPU
    InitVertexBuffer();
}

// Interactive Rotation

vec2 mouseDown;				// for each mouse down, need start point
vec2 rotOld, rotNew;	    // previous, current rotations
mat4 rotM;				    // MouseDrag sets, Display uses
bool buttonPicked = false;

void MouseButton(int butn, int state, int x, int y) {
    y = glutGet(GLUT_WINDOW_HEIGHT)-y;
	if (state == GLUT_DOWN)
		mouseDown = vec2((float) x, (float) y);
	if (state == GLUT_UP)
		rotOld = rotNew;
	glutPostRedisplay();
}

void MouseDrag(int x, int y) {
	if (!buttonPicked) {
	    y = glutGet(GLUT_WINDOW_HEIGHT)-y;
		vec2 mouse((float) x, (float) y);
		rotNew = rotOld+.3f*(mouse-mouseDown);
			// new rotations depend on old plus mouse distance from mouseDown
		rotM = RotateY(rotNew.x)*RotateX(rotNew.y);
			// rot.x is about Y-axis, rot.y is about X-axis
	}
	glutPostRedisplay();
}

// Display

void Display() {
    glUseProgram(shaderId);
	// update and send modelview matrix to vertex shaderId
	mat4 modelview = Translate(0, 0, -5)*rotM;
	glUniformMatrix4fv(glGetUniformLocation(shaderId, "modelview"), 1, true, (float *) &modelview[0][0]);
	// update and send perspective matrix to vertex shaderId
	float fov = 15, nearPlane = -.001f, farPlane = -500;
	float aspect = (float) glutGet(GLUT_WINDOW_WIDTH) / (float) glutGet(GLUT_WINDOW_HEIGHT);
	mat4 persp = Perspective(fov, aspect, nearPlane, farPlane);
	glUniformMatrix4fv(glGetUniformLocation(shaderId, "persp"), 1, true, (float *) &persp[0][0]);
	// transform homogeneous light and send to fragment shaderId
	vec4 hLight = modelview*vec4(lightSource, 1);
	vec3 xlight(hLight.x, hLight.y, hLight.z);
	glUniform3fv(glGetUniformLocation(shaderId, "light"), 1, (float *) &xlight);

	GLint id = glGetUniformLocation(shaderId, "scale");
	if (id >= 0)
		glUniform1f(id, scale);
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
    // link shaderId input with points array at start of vertex buffer
	int pointId = GLSL::EnableVertexAttribute(shaderId, "point");
	int normalId = GLSL::EnableVertexAttribute(shaderId, "normal");
	int uvId = GLSL::EnableVertexAttribute(shaderId, "uv");
    glBindBuffer(GL_ARRAY_BUFFER, vBufferId);
	glVertexAttribPointer(pointId,  3,  GL_FLOAT, GL_FALSE, sizeof(Vertex), (void *) 0);
	glVertexAttribPointer(normalId, 3,  GL_FLOAT, GL_FALSE, sizeof(Vertex), (void *) sizeof(vec3));
	glVertexAttribPointer(uvId,     2,  GL_FLOAT, GL_FALSE, sizeof(Vertex), (void *) (2*sizeof(vec3)));
    //                    attrib    num type      normalize stride          offset
	// draw triangles and finish
	glDrawArrays(GL_TRIANGLES, 0, vertices.size());
    glFlush();
}

// Texture

void SetTexture(char *filename) {
	// open targa file, read header
	FILE *in = fopen(filename, "rb");
	short tgaHeader[9];
	fread(tgaHeader, sizeof(tgaHeader), 1, in);
	// allocate, read pixels
	int width = tgaHeader[6], height = tgaHeader[7], bitsPerPixel = tgaHeader[8];
	int bytesPerPixel = bitsPerPixel/8, bytesPerImage = width*height*bytesPerPixel;
	if (bytesPerPixel != 3)
		printf("bytes per pixel not 3!\n");
	char *pixels = new char[bytesPerImage];
	fread(pixels, bytesPerImage, 1, in);
	// allocate GPU texture buffer; copy, free pixels
	glGenTextures(1, &textureId);
	glBindTexture(GL_TEXTURE_2D,  textureId);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_BGR, GL_UNSIGNED_BYTE, pixels);
	delete [] pixels;
	glGenerateMipmap(GL_TEXTURE_2D);
	// refer sampler uniform to texture 0
	GLSL::SetUniform(shaderId, "textureImage", 0);
}

// Application

void Close() {
	// unbind vertex buffer, free GPU memory
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &vBufferId);
}

void main(int argc, char **argv) {
	// init window
    glutInit(&argc, argv);
    glutInitWindowSize(400, 400);
    glutCreateWindow("Shader Example");
    glewInit();
	// build, use shaderId program
	shaderId = GLSL::LinkProgramViaCode(vertexShader, fragmentShader);
	if (!shaderId) {
		printf("Can't link shaderId program\n");
		getchar();
		return;
	}
	ReadObject(objFilename);
	// read texture image, create mipmap, link it to pixel shader
	SetTexture(texFilename);
	// GLUT callbacks, event loop
    glutDisplayFunc(Display);
	glutMouseFunc(MouseButton);
	glutMotionFunc(MouseDrag);
    glutCloseFunc(Close);
    glutMainLoop();
}
