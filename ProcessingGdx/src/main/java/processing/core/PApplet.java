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

    private void translateGdxKeycodeToProcessing(int gdxKeycode)
    {
        // set variables key and keyCode based on gdxKeycode

        if (Input.Keys.A <= gdxKeycode && gdxKeycode <= Input.Keys.Z)
        {
            this.key = (char)(gdxKeycode - Input.Keys.A + (int)'a');
            this.keyCode = key + 'A' - 'a';

            if (keyShiftDown) 
                key += 'A' - 'a';
        }
        else if (gdxKeycode == Input.Keys.ENTER) // TODO: check against ASCII values for efficiency?
        {
            this.key = ENTER;
            this.keyCode = 0;
        }
        // CODED keys 
        // TODO: put this in a map
        else if (gdxKeycode == Input.Keys.SHIFT_RIGHT || 
                 gdxKeycode == Input.Keys.SHIFT_LEFT)
        {
            this.key = CODED;
            this.keyCode = SHIFT;
        }
        else if (gdxKeycode == Input.Keys.UP)
        {
            this.key = CODED;
            this.keyCode = UP;
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        println("libgdx keyDown keycode: " + keycode);
        translateGdxKeycodeToProcessing(keycode);
        if (keyCode == SHIFT) keyShiftDown = true;
        keyPressed();
        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        println("libgdx keyTyped character: " + (int)character);

        if (character == '\0' || key == CODED)
            return true;    // suppress call to PApplet.keyTyped()

        this.key = character;
        this.keyCode = 0;
        keyTyped();
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        println("libgdx keyUp keycode: " + keycode);
        translateGdxKeycodeToProcessing(keycode);
        if (keyCode == SHIFT) keyShiftDown = false;
        keyReleased();                     
        return true;
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
    public void keyTyped() {}
    public void keyReleased() {}

    public char key;
    public int keyCode;
    public boolean keyShiftDown;
}


