//
// PGraphics.java
//
// Darren Kessner
//


package processing.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;
import static com.badlogic.gdx.graphics.Camera.*;


public class PGraphics extends PImage
{
    public void initialize()
    {
        // window initialization

        settings();

        // implementation initialization

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        // sketch initializaton

        setup();
    }

    public void finalize()
    {
        batch.dispose();
    }

    // PApplet methods to be overridden by Processing sketches

    public void settings() {}
    public void setup() {}
    public void draw() {}

    // PGraphics API

    public PImage loadImage(String filename)
    {
        return new PImage(filename);
    }

    public void image(PImage img, float x, float y, float w, float h)
    {
        batch.begin();

        final boolean flipX = false;
        final boolean flipY = true;

        batch.draw(img.getTexture(), x, y, w, h, 
                   0, 0, img.width, img.height,
                   flipX, flipY);

        batch.end();
    }

    public void image(PImage img, float x, float y)
    {
        image(img, x, y, img.width, img.height);
    }

    public void background(int r, int g, int b, int a)
    {
        Gdx.gl.glClearColor(r/255f, g/255f, b/255f, a/255f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void background(int r, int g, int b) {background(r, g, b, 255);}
    public void background(int n) {background(n, n, n, 255);}

    public void fill(int r, int g, int b, int a)
    {
        shapeRenderer.setColor(r/255f, g/255f, b/255f, 1);
    }

    public void fill(int r, int g, int b) {fill(r, g, b, 255);}
    public void fill(int n) {fill(n, n, n, 255);}

    public void ellipse(float x, float y, float width, float height)
    {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.ellipse(x-width/2, y-height/2, width, height);
        shapeRenderer.end();
    }
    
    // implementation variables

    protected OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
}


