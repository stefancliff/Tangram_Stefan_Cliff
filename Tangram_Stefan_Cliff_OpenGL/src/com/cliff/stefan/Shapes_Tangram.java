package com.cliff.stefan;

import com.jogamp.opengl.GL2;

/*
	Stefan Cliff 2019/230449 June 22

		 - RGB COLOURS -
	glColor3f (0.0, 0.0, 0.0) Black
	glColor3f (1.0, 0.0, 0.0) Red
	glColor3f (1.0, 1.0, 0.0) Yellow
	glColor3f (0.0, 1.0, 0.0) Green
	glColor3f (0.0, 1.0, 1.0) Cyan
	glColor3f (0.0, 0.0, 1.0) Blue
	glColor3f (1.0, 0.0, 1.0) Magenta
	glColor3f (1.0, 1.0, 1.0) White
	glColor3f (0.5, 0.5, 0.5) Gray

*/

public class Shapes_Tangram {

	public static void drawSquare(GL2 gl, double side) {

		double radius = side / 2;

		gl.glBegin(GL2.GL_POLYGON);

		gl.glNormal3f(0,0,1);

		gl.glVertex2d(	-radius, 	 radius);
		gl.glVertex2d(	-radius,	-radius);
		gl.glVertex2d(	radius , 	-radius);
		gl.glVertex2d(	radius , 	 radius);

		gl.glEnd();

	}

	public static void drawParallelogram(GL2 gl, double side) {

		float[] m = { 1.0f, 0.0f, 0.0f, 0.0f,
					 -1.0f, 1.0f, 0.0f, 0.0f,
					 0.0f, 0.0f, 1.0f, 0.0f,
					 0.0f, 0.0f, 0.0f, 1.0f	};

		gl.glPushMatrix();
		gl.glMultMatrixf(m, 0);
		drawSquare(gl, side);
		gl.glPopMatrix();

	}

	public static void drawTriangle(GL2 gl, double side) {

		double radius = side / 2;

		double height 	= (Math.sqrt(2) * side) / 6;  // the height inside a 90 degree triangle ( JKK triangle )
		height 			= Math.round(height * 100.0) / 100.0;


		gl.glBegin(GL2.GL_TRIANGLES);

		gl.glNormal3f(0,0,1);

		gl.glVertex2d(-radius - height , -radius 	+ height);
		gl.glVertex2d(radius  - height , -radius 	+ height);
		gl.glVertex2d(radius  - height ,  radius 	+ height);

		gl.glEnd();

	}

}
