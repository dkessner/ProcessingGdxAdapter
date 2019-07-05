//
// ProcessingGdxAdapter.java
//
// Darren Kessner
//


package io.github.dkessner;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;


public class ProcessingGdxAdapter extends ApplicationAdapter 
{
    @Override
    public void create() 
    {
        papplet = new PApplet();
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

