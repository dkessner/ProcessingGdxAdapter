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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;


/**
 * Base class for all Processing sketches.
 */
public class PApplet extends PGraphics implements InputProcessor
{
    public PApplet()
    {
    }

    // communication with gdx.ApplicationAdapter

    public void create()
    {
        Gdx.input.setInputProcessor(this); 
        settings(); // window initialization 
        setup(); // sketch initializaton
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
        super.finalize();
    }

    // InputProcessor interface

    @Override
    public boolean keyDown(int keycode)
    {
        if (Input.Keys.A <= keycode && keycode <= Input.Keys.Z)
        {
            key = (char)(keycode - Input.Keys.A + (int)'a');
        }
        keyPressed();                     
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    
    // PApplet API

    public void println(Object o)
    {
        System.out.println(o);
    }

    public void size(int width, int height)
    {
        Gdx.graphics.setWindowedMode(width, height);
        super.initialize(width, height);
    }

    // event handling

    public void keyPressed() {}

    public char key;
    public int keyCode;
}


