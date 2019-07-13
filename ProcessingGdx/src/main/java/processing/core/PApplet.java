//
// PApplet.java
//
// Darren Kessner
//


package processing.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class PApplet extends PGraphics
{
    public PApplet()
    {
    }

    // communication with gdx.ApplicationAdapter

    public void create()
    {
        initialize(); // PGraphics
    }

    public void render()
    {
        draw(); // PApplet subclass
    }

    public void resize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void dispose() 
    {
        finalize(); // PGraphics
    }
    
    // PApplet API

    public void size(int width, int height)
    {
        Gdx.graphics.setWindowedMode(width, height);

        camera = new OrthographicCamera(width, height);
        final boolean yDown = true;
        camera.setToOrtho(yDown, width, height);

        this.width = width;
        this.height = height;
    }
}


