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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;
import static com.badlogic.gdx.graphics.Pixmap.*;
import static com.badlogic.gdx.graphics.Camera.*;


/** Provides the main Processing API.
 *
 * As in Processing, the PGraphics class provides the API for drawing to either
 * the main window or to an offscreen frame buffer.  Here we implement this
 * using a libgdx FrameBuffer object, which wraps an OpenGL frame buffer.
 *
 * To draw to the frame buffer, drawing calls must be made between beginDraw()
 * and endDraw().  Otherwise drawing calls go to the main screen.  This
 * behavior is the same as in Processing.
 *
 * Implementation note: initialization of the coordinate system (including the
 * y-down transformation) happens after we know the width and height (of the
 * window or the offscreen buffer).  In the window case, initialize() is called
 * when the user calls size() or fullScreen() in settings().  In the offscreen
 * buffer case, initialize() is called on construction.
 */
public class PGraphics extends PImage 
{
    // PApplet methods to be overridden by Processing sketches

    public void settings() {}
    public void setup() {}
    public void draw() {}

    /**
     * Construct a new PGraphics object.
     *
     * This default constructor does not call {@link #initialize(int, int)}, which handles
     * the coordinate system initialization.
     */
    public PGraphics()
    {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
    }

    /**
     * Construct a new PGraphics object as an offscreen buffer.  
     *
     * This constructor initializes the coordinate system, and creates a
     * backing libgdx FrameBuffer object.
     *
     */
    public PGraphics(int width, int height)
    {
        this(); 
        initialize(width, height);

        final boolean hasDepth = false;
        fb = new FrameBuffer(Format.RGBA8888, width, height, hasDepth);
    }

    /**
     * Initializes the coordinate system:  
     *
     * - y-axis down
     * - (0,0) top right
     * - (width, height) bottom left
     */
    void initialize(int width, int height) // package-private
    {
        // initialization for the coordinate system

        this.width = width;
        this.height = height;

        camera = new OrthographicCamera(width, height);
        final boolean yDown = true;
        camera.setToOrtho(yDown, width, height);

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
    }

    public void finalize()
    {
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (batch != null) batch.dispose();
        if (fb != null) fb.dispose();
    }

    // PGraphics API

    public void beginDraw()
    {
        if (fb != null) fb.begin();
    }

    public void endDraw()
    {
        if (fb != null) fb.end();
    }

    public PGraphics createGraphics(int width, int height)
    {
        return new PGraphics(width, height);
    }

    public PImage loadImage(String filename)
    {
        return new PImage(filename);
    }

    @Override
    Texture getTexture() 
    {
        if (fb != null)
            return fb.getColorBufferTexture();
        return super.getTexture();
    }

    public void image(PImage img, float x, float y, float w, float h)
    {
        batch.begin();

        final boolean flipX = false;
        final boolean flipY = img.flipY();

        Texture t = img.getTexture();

        if (t != null)
            batch.draw(t, x, y, w, h, 
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

    private FrameBuffer fb;

    protected OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
}


