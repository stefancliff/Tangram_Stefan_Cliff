
// I READ SOMEWHERE THAT THE WHOLE FUNCTIONALITY OF THE CAMERA
// CAN BE DONE MUCH FASTER AND SIMPLER WITHIN OPENGL ITSELF BUT
// I'LL KEEP THIS HERE JUST IN CASE IT DOESN'T WORK

package com.cliff.stefan;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

/**
 * Camera encapsulates the information needed to define a viewing transform
 * and a projection for an OpenGL context
 *
 */

public class Camera_Tangram {

	// look from camera XYZ
	private double eyex = 30;
	private double eyey = 30;
	private double eyez = 30;

	// look at the origin
	private double refx = 0;
	private double refy = 0;
	private double refz = 0;

	// positive Y up vector (roll of the camera)
	private double upx = 0;
	private double upy = 1;
	private double upz = 0;


	private double xminRequested = -5;
	private double xmaxRequested = 5;
	private double yminRequested = -5;
	private double ymaxRequested = 5;
	private double zmin = -10;
	private double zmax  = 10;

	private boolean orthographic = true;
	private boolean preserveAspect = false;

	private double xminActual = 0;
	private double xmaxActual = 0;
	private double yminActual = 0;
	private double ymaxActual = 0;

	// give us access to the opengl utility library
	private GLU glu = new GLU();

	// set the limit for the viewpoint
	public void setLimits(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {

		// x-min & x-max gives the horizontal limits on the screen
		this.xminRequested = xmin;
		this.xminActual = xmin;
		this.xmaxRequested = xmax;
		this.xmaxActual = xmax;

		// y-min & y-max gives the vertical limits on the screen
		this.yminRequested = ymin;
		this.yminActual = ymin;
		this.ymaxRequested = ymax;
		this.ymaxActual = ymax;

		// z-min & z-max gives the limit of the viewing volume along the z-axis
		this.zmin = zmin;
		this.zmax = zmax;
	}

	// limits the shape of the blueprint in the matching process
	public void setScale(double limit) {
		setLimits(-limit, limit, -limit, limit, -2*limit, 2*limit);
	}

	// Set the information for the viewing transformation.
	public void lookAt(double eyeX, double eyeY, double eyeZ,
					   double viewCenterX, double viewCenterY, double viewCenterZ,
					   double viewUpX, double viewUpY, double viewUpZ) {

		/* look from camera XYZ*/
		this.eyex = eyeX;
		this.eyey = eyeY;
		this.eyez = eyeZ;

		/* look from origin*/
		this.refx = viewCenterX;
		this.refy = viewCenterY;
		this.refz = viewCenterZ;

		/* positive Y up vector (roll of the camera) */
		this.upx = viewUpX;
		this.upy = viewUpY;
		this.upz = viewUpZ;
	}

	// set the view of what we see when the game starts
	public void apply(GL2 gl) {

		int [] viewport = new int[4];
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);

		this.xminActual = this.xminRequested;
		this.xmaxActual = this.xmaxRequested;
		this.yminActual = this.yminRequested;
		this.ymaxActual = this.ymaxRequested;

		if(preserveAspect) { // use perspective projection

			double viewWidth = viewport[2]; // grab the width
			double viewHeight = viewport[3]; // grab the height

			// compute the window width and height
			double windowWidth = xmaxActual - xminActual;
			double windowHeight = ymaxActual - yminActual;

			// compute the aspect ratio of the view and desired view
			double aspect = viewHeight / viewWidth;
			double desired = windowHeight / windowWidth;

			if(desired > aspect) { // expand the width
				double extra = (desired / aspect - 1.0) * (xmaxActual - xminActual) / 2.0;
				xminActual = xminActual - extra;
				xmaxActual = xmaxActual + extra;
			} else if(aspect > desired) { // expand the height
				double extra = (aspect / desired - 1.0) * (ymaxActual - yminActual) / 2.0;
				yminActual = yminActual - extra;
				ymaxActual = ymaxActual + extra;
			}
		}

		// convert camera space into screen space
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		double viewDistance = norm(new double [] {refx - eyex, refy -eyey, refz-eyez});

		// switch between views
		if(orthographic) {
			gl.glOrtho(xminActual, xmaxActual, yminActual, ymaxActual, viewDistance - zmax, viewDistance - zmin);
		} else { // perspective view
			double near = viewDistance - zmax;
			if(near < 0.1) {
				near = 0.1;
			}

			double centerX = (xminActual + xmaxActual) / 2;
			double centerY = (yminActual + ymaxActual) / 2;

			double newWidth = (near / viewDistance) * (xmaxActual - xminActual);
			double newHeight = (near / viewDistance) * (ymaxActual - yminActual);

			double x1 = centerX - newWidth / 2;
			double x2 = centerX + newWidth / 2;
			double y1 = centerY - newHeight / 2;
			double y2 = centerY + newHeight / 2;

			gl.glFrustum(x1, x2, y1, y2, near, viewDistance - zmin);
		}

		// define the position orientation of the camera
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(eyex, eyey, eyez, /* look from the camera*/
				refx, refy, refz, /* look at the origin */
				upx, upy, upz); /* positive Y up vector*/
	}

	private double norm(double [] v) {
		double norm2 = v[0]*v[0] + v[1]*v[1] + v[2]*v[2];

		if (Double.isNaN(norm2) || Double.isInfinite(norm2) || norm2 == 0) {
			throw new NumberFormatException("Vector length zero, undefined, or infinite.");
		}

		return Math.sqrt(norm2);
	}

}
