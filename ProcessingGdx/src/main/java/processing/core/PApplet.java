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
        // window initialization 

        settings();

        // sketch initializaton

        setup();
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
        super.initialize(width, height);
    }
}


