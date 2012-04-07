package org.codelove.aglsl;

import android.content.Context;
import android.opengl.GLSurfaceView;

class AGLSLSurfaceView extends GLSurfaceView {

	//private AGLSLTouch touchHandler;
	
	public AGLSLSurfaceView(Context context){
	        super(context);
	        
	        // Create an OpenGL ES 2.0 context.
	        setEGLContextClientVersion(2);
	        // Set the Renderer for drawing on the GLSurfaceView
	        setRenderer(new AGLSLRenderer());
	        
	        //touchHandler = new AGLSLTouch();
	    }

/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchHandler.handle(event);
		return true; // indicate event was handled
	}
*/
}
