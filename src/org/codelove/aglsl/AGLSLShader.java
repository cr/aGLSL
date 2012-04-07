package org.codelove.aglsl;

import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class AGLSLShader {

	private int mProgram;
	private int vertexShader;
	private int fragmentShader;
	// private int muMVPMatrixHandle;
	private int maPositionHandle;
	private int maColorHandle;
	private int muTimeHandle;
	
	public AGLSLShader() {
		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		vertexShader = compileShader(GLES20.GL_VERTEX_SHADER,
				vertexShader());
		GLES20.glAttachShader(mProgram, vertexShader);
		fragmentShader = -1;
	}

	public String vertexShader() {
		return vertexShaderCode;
	}

	public String fragmentShader() {
		return fragmentShaderCode;
	}
	
	public void createProgram() {

		// TODO: unclean check, replace
		if (fragmentShader != -1) {
			GLES20.glDetachShader(mProgram, fragmentShader);
			GLES20.glDeleteShader(fragmentShader);
		}
		
		// always recompile fragment shader
		fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShader());

		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glBindAttribLocation(mProgram, 0, "vPosition");
		GLES20.glBindAttribLocation(mProgram, 1, "vColor");
		GLES20.glLinkProgram(mProgram); // creates OpenGL program executables

		// get handles to vertex shader's attribute members
		// muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
		// "uMVPMatrix");
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		maColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
		muTimeHandle = GLES20.glGetUniformLocation(mProgram, "fTime");

	}

	private int compileShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		int[] compiled = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			Log.e(AGLSLShader.class.getName(), "Could not compile shader " + type + ":");
			Log.e(AGLSLShader.class.getName(), GLES20.glGetShaderInfoLog(shader));
			GLES20.glDeleteShader(shader);
			shader = 0;
		}
		return shader;
	}

	public void use() {
		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);
	}
	
	public void loadData(FloatBuffer quadVB) {

		// Prepare position vertex data
		quadVB.position(0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 7 * 4, quadVB);
		GLES20.glEnableVertexAttribArray(maPositionHandle);

		// Prepare color vertex data
		quadVB.position(3);
		GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,
				7 * 4, quadVB);
		GLES20.glEnableVertexAttribArray(maColorHandle);

		// Send time
		GLES20.glUniform1f(muTimeHandle, (float) (System.currentTimeMillis() % 100000));
		
	}
	
	private final String vertexShaderCode =
			  "//uniform mat4 u_MVPMatrix;\n"
			+ "uniform float fTime;\n"
			+ "attribute vec4 vPosition;\n"
			+ "attribute vec4 vColor;\n"
			+ "varying vec4 color;\n"
			+ "void main(){\n"
			+ "  //gl_Position = vPosition + vec4(0.1*cos(fTime/1000.0*2.0*3.1415927),0.1*sin(fTime/1000.0*2.0*3.1415927),0.0,0.0);\n"
			+ "  gl_Position = vPosition;\n"
			+ "  color = vColor;\n"
			+ "}\n";

	private final String fragmentShaderCode =
			"precision mediump float;\n"
			+ "varying vec4 color;\n"
			+ "\n"
			+ "void main() {\n"
			+ "  gl_FragColor = color;\n"
			+ "}\n";

	protected void finalize() throws Throwable {
		GLES20.glDetachShader(mProgram, vertexShader);
		GLES20.glDetachShader(mProgram, fragmentShader);
		GLES20.glDeleteShader(vertexShader);
		GLES20.glDeleteShader(fragmentShader);
		GLES20.glDeleteProgram(mProgram);
	}
	
}
