//
// PApplet.java
//
// Darren Kessner
//


package io.github.dkessner;


import java.io.*;
import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Graphics;
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
        initializeKeyCodeMap();
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
        beforeDraw();
        draw();         // PApplet subclass
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

    private void initializeKeyCodeMap()
    {
        keyCodeMap = new HashMap<Integer, Integer>();

        keyCodeMap.put(Input.Keys.SHIFT_RIGHT, SHIFT);
        keyCodeMap.put(Input.Keys.SHIFT_LEFT, SHIFT);

        keyCodeMap.put(Input.Keys.LEFT, LEFT);
        keyCodeMap.put(Input.Keys.RIGHT, RIGHT);
        keyCodeMap.put(Input.Keys.UP, UP);
        keyCodeMap.put(Input.Keys.DOWN, DOWN);

        // TODO: missing CODED keys?
    }

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
        else if (gdxKeycode == Input.Keys.ESCAPE)
        {
            this.key = ESC;
            this.keyCode = 0;
        }
        // TODO: missing ASCII keys
        else
        {
            key = CODED;
            keyCode = keyCodeMap.getOrDefault(gdxKeycode, 0);
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        println("libgdx keyDown keycode: " + keycode);
        translateGdxKeycodeToProcessing(keycode);
        if (keyCode == SHIFT) keyShiftDown = true;
        keyPressed();
       
        if (key == ESC)
            Gdx.app.exit();

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

    public void size(int width, int height, String rendererType)
    {
        Gdx.graphics.setWindowedMode(width, height);
        super.initialize(width, height, rendererType);
    }

    public void size(int width, int height) {size(width, height, P2D);}

    public void fullScreen(String rendererType)
    {
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);
        super.initialize(displayMode.width, displayMode.height, rendererType);
    }

    public void fullScreen() {fullScreen(P2D);}

    public void saveStrings(String filename, String[] strings)
    {
        FileHandle handle = Gdx.files.local(filename);
        
        handle.writeString("", false); // overwrite file: write empty string with append==false

        for (String s : strings)
            handle.writeString(s + '\n', true); // append==true
    }

    public String[] loadStrings(String filename)
    {
        try
        {
            FileHandle handle = Gdx.files.internal(filename);
            BufferedReader reader = handle.reader(1024);

            ArrayList<String> strings = new ArrayList<String>();

            while (true)
            {
                String s = reader.readLine();
                if (s == null) break;
                strings.add(s);
            }

            return strings.toArray(new String[strings.size()]);
        }
        catch (IOException e)
        {
            println(e);
            return null;
        }
    }

    // event handling

    public void keyPressed() {}
    public void keyTyped() {}
    public void keyReleased() {}

    public char key;
    public int keyCode;
    private boolean keyShiftDown;

    private HashMap<Integer, Integer> keyCodeMap;
}


