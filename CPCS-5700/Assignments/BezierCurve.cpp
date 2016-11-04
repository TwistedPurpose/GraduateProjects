// BezierCurveStub.cpp - interactive curve design
// Soren Ludwig

#include "glew.h"
#include "freeglut.h"
#include <time.h>
#include "Draw.h"
#include "Widget.h"
#include "mat.h"

// four Bezier control points
vec3	ctrlPts[] = { vec3(.5f,-.25f,0), vec3(.5f,.25f,0), vec3(-.5f,-.25f,0), vec3(-.5f,.25f,0) };

vec3 animatedPoint = vec3(.5f, -.25f, 0);
bool forward = true;
float animatedPointStep = 0.0;

// curve
int		res = 50;	// number of points to compute along the curve

// Bezier operations

vec3 BezierPoint(float t, vec3 &b1, vec3 &b2, vec3 &b3, vec3 &b4) {
	vec3 ret = (-pow(t, 3) + (3 * pow(t, 2)) - (3 * t) + 1)*b1 + ((3 * pow(t, 3)) - (6 * pow(t, 2)) + (3 * t))*b2 + ((-3 * pow(t, 3)) + (3 * pow(t, 2)))*b3 + (pow(t, 3))*b4;
	return ret;
}

// display

mat4 rotM, modelview, persp, fullview;

void Display() {
	// background, blending, zbuffer
	glClearColor(.6f, .6f, .6f, 1);
	glClear(GL_COLOR_BUFFER_BIT);
	glEnable(GL_BLEND);
	glEnable(GL_POINT_SMOOTH);
	glEnable(GL_LINE_SMOOTH);
	glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	glDisable(GL_DEPTH_TEST);
	// update transformations
	modelview = Translate(0, 0, -5)*rotM; // translate +/-1 in z ok, but beyond and clipped by NDC
	float fov = 15, nearPlane = -.001f, farPlane = -500;
	float aspect = (float)glutGet(GLUT_WINDOW_WIDTH) / (float)glutGet(GLUT_WINDOW_HEIGHT);
	persp = Perspective(fov, aspect, nearPlane, farPlane);
	// the persp matrix compresses the z from near to far to 0/1 in NDC
	// without it, translate in Z confined to +/-1.
	fullview = persp*modelview;
	// enable draw shader
	UseDrawShader(fullview);
	// in ShadeMesh.cpp, the persp and modelview matrices were separately loaded to the
	// vertex shader in order to obtain points in eye space (needed for shading) and in
	// perspective space (needed for clipping and projection to the screen)
	// Draw.h does no shading, so a single persp*modelview matrix suffices
// draw curve
	for (int i = 0; i < res; i++) {
		vec3 point1 = BezierPoint((float)i / (float)res, ctrlPts[0], ctrlPts[1], ctrlPts[2], ctrlPts[3]);
		vec3 point2 = BezierPoint((float)(i + 1) / (float)res, ctrlPts[0], ctrlPts[1], ctrlPts[2], ctrlPts[3]);
		Line(point1, point2, vec3(1, 0, 0));
	}
	// control mesh
	DashOn();
	Line(ctrlPts[0], ctrlPts[1], vec3(0, 1, 0));
	Line(ctrlPts[1], ctrlPts[2], vec3(0, 1, 0));
	Line(ctrlPts[2], ctrlPts[3], vec3(0, 1, 0));

	DashOff();
	// control points
	Disk(ctrlPts[0], 5, vec3(0, 0, 1));
	Disk(ctrlPts[1], 5, vec3(0, 0, 1));
	Disk(ctrlPts[2], 5, vec3(0, 0, 1));
	Disk(ctrlPts[3], 5, vec3(0, 0, 1));


	Disk(animatedPoint, 10, vec3(0, 1, 1));
	animatedPoint = BezierPoint((float)animatedPointStep / ((float)res*100), ctrlPts[0], ctrlPts[1], ctrlPts[2], ctrlPts[3]);
	if (forward) {
		animatedPointStep += 1;
	}
	else {
		animatedPointStep -= 1;
	}

	if (animatedPointStep <= 0 || animatedPointStep >= (res*100))
		forward = !forward;

	glFlush();
}

void Idle() {
	glutPostRedisplay();
}

// mouse

vec2	mouseDown;
vec2	rotOld, rotNew;			// previous, current rotations
Mover	ptMover;
bool 	cameraDown = false;

vec3 *PickPoint(int x, int y, bool rightButton) {
	vec3 * clickedPoint = NULL;
	if (rightButton)
	{
		for (int i = 0; i < 4; i++)
		{
			vec4 a = vec4(x, y, 0, 1);
			vec4 b = modelview*a;
			float dist = sqrt(ScreenDistSq(x, y, ctrlPts[i], fullview));
			if (dist < 10) {
				clickedPoint = &ctrlPts[i];
				break;
			}
		}
	}
	return clickedPoint;
}

void MouseButton(int butn, int state, int x, int y) {
	y = glutGet(GLUT_WINDOW_HEIGHT) - y; // invert y for upward-increasing screen space
	if (state == GLUT_UP) {
		if (cameraDown)
			rotOld = rotNew;
		else if (ptMover.point)
			ptMover.Set(NULL);
	}
	cameraDown = false;
	if (state == GLUT_DOWN) {
		vec3 *pp = PickPoint(x, y, butn == GLUT_RIGHT_BUTTON);
		if (pp) {
			ptMover.Set(pp);
			ptMover.Down(x, y, modelview, persp);
		}
		else {
			cameraDown = true;
			mouseDown = vec2((float)x, (float)y);
		}
	}
	glutPostRedisplay();
}

void MouseDrag(int x, int y) {
	y = glutGet(GLUT_WINDOW_HEIGHT) - y;
	if (ptMover.point)
		ptMover.Drag(x, y, modelview, persp);
	else if (cameraDown) {
		rotNew = rotOld + .3f*(vec2((float)(x - mouseDown.x), (float)(y - mouseDown.y)));
		// new rotations depend on old plus mouse distance from mouseDown
		rotM = RotateY(rotNew.x)*RotateX(rotNew.y);
		// rot.x is about Y-axis, rot.y is about X-axis
	}
	glutPostRedisplay();
}

// application

int main(int ac, char **av) {
	// init app window
	glutInit(&ac, av);
	glutInitWindowSize(930, 800);
	glutInitWindowPosition(100, 100);
	glutCreateWindow("Bezier Patch");
	GLenum err = glewInit();
	if (err != GLEW_OK)
		printf("Error initializaing GLEW: %s\n", glewGetErrorString(err));
	// callbacks
	glutDisplayFunc(Display);
	glutIdleFunc(Idle);
	glutMouseFunc(MouseButton);
	glutMotionFunc(MouseDrag);
	glutMainLoop();
}
