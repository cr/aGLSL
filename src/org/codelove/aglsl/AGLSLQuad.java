package org.codelove.aglsl;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class AGLSLQuad {

	private float quadData[] = {
			// X, Y, Z, R, G, B, A
			1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
			-1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
			1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f
		};
	private FloatBuffer quadVB;

	private AGLSLShader shader;

	public AGLSLQuad(float scale, float xoff, float yoff) {

		for (int i = 0; i < 4; i++) {
			quadData[i * 7 + 0] *= scale;
			quadData[i * 7 + 1] *= scale;
			quadData[i * 7 + 0] += xoff;
			quadData[i * 7 + 1] += yoff;
		}

		// initialize vertex Buffer for quad
		quadVB = ByteBuffer.allocateDirect(quadData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		quadVB.put(quadData).position(0);

		shader = new AGLSLShader();
		shader.createProgram();
		
	}

	public void draw() {

		shader.use();
		shader.loadData(quadVB);

		// Draw the quad as triangle fan
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
	}

}