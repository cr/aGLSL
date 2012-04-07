package org.codelove.aglsl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class AGLSLRenderer implements GLSurfaceView.Renderer {

	private AGLSLQuad quad;
	
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		quad = new AGLSLQuad(1f, 0f, 0f);
		timePrev = System.currentTimeMillis();
	}

	long timeNow;
	long timePrev = -1;
	long times[] = new long[100];
	int timePtr = 0;
	boolean timesFilled = false;

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		quad.draw();
		
		timeNow = System.currentTimeMillis();
		times[timePtr++] = timeNow - timePrev;
		timePrev = timeNow;

		timePtr %= times.length;
		if (timePtr == 0) {
			long avg = 0;
			for (int i = 0; i < times.length; i++)
				avg += times[i];
			avg /= times.length;
			Log.i(AGLSLRenderer.class.getName(), "Running at "
					+ (1.0 / (avg / 1000.0)) + " fps");
		}

	}

	public int surfaceWidth, surfaceHeight;

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		surfaceWidth = width;
		surfaceHeight = height;
	}

	public double absoluteToRelativeX(int x) {
		return ((double) x) / surfaceWidth;
	}

	public double absoluteToRelativeY(int y) {
		return ((double) y) / surfaceHeight;
	}

	public int relativeToAbsoluteX(double x) {
		return (int) (x / (double) surfaceWidth);
	}

	public int relativeToAbsoluteY(double y) {
		return (int) (y / (double) surfaceHeight);
	}
	
}
