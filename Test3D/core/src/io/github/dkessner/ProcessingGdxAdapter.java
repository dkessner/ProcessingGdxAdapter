//
// ProcessingGdxAdapter.java
//
// Darren Kessner
//


package io.github.dkessner;


import processing.core.*;
import com.badlogic.gdx.ApplicationAdapter;


public class ProcessingGdxAdapter extends ApplicationAdapter 
{
    @Override
    public void create() 
    {
        papplet = new Test3D();
        papplet.create();
    }

    @Override
    public void render() 
    {
        papplet.render();
    }

    @Override
    public void resize(int width, int height)
    {
        papplet.resize(width, height);
    }

    @Override
    public void dispose() 
    {
        papplet.dispose();
    }

    private PApplet papplet;
}

