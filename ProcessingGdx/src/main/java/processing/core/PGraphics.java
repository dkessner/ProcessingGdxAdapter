//
// PGraphics.java
//
// Darren Kessner
//


package processing.core;

import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;

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
     * Note: This default constructor does not initialize anything.
     * Initialization happens when the client subclass calls size() or
     * fullScreen(), which calls initialize().
     */
    public PGraphics() {}

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
     * Initialization, which can be called during or after construction.
     *
     * - internal libgdx objects
     * - the coordinate system
     *      - y-axis down
     *      - (0,0) top right
     *      - (width, height) bottom left
     */
    void initialize(int width, int height) // package-private
    {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        initializeShapeTypeMap();

        // initialization for the coordinate system

        this.width = width;
        this.height = height;

        /*        camera = new OrthographicCamera(width, height);
        final boolean yDown = true;
        camera.setToOrtho(yDown, width, height);
        //println("[PGraphics initialize()] camera near: " + camera.near + " far: " + camera.far);

        */

        float aspectRatio = (float)width / height;
        camera = new PerspectiveCamera(45, width, height);

        camera.position.x = 200f;
        camera.position.y = 200f;
        camera.position.z = 500f;
        camera.far = 1000f;
        camera.lookAt(200, 200, 0);

        camera.update(true);


        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
    }

    public void finalize()
    {
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (batch != null) batch.dispose();
        if (fb != null) fb.dispose();
    }

    /*package-private*/ void beforeDraw()
    {
        shapeRenderer.identity();
    }

    // PGraphics API

    public void println(Object o)
    {
        System.out.println(o);
    }

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

    public void fill(float v1, float v2, float v3, float a)
    {
        setColor(fillColor, v1, v2, v3, a);
        currentFillColor = fillColor;
    }

    public void fill(Color c)
    {
        fillColor.set(c);
        currentFillColor = fillColor;
    }

    public void fill(float v1, float v2, float v3) {fill(v1, v2, v3, aMax);}
    public void fill(float v) {fill(v, v, v, aMax);}
    public void noFill() {currentFillColor = null;}

    public void stroke(float v1, float v2, float v3, float a)
    {
        setColor(strokeColor, v1, v2, v3, a);
        currentStrokeColor = strokeColor;
    }

    public void stroke(Color c)
    {
        strokeColor.set(c);
        currentStrokeColor = strokeColor;
    }

    public void stroke(float v1, float v2, float v3) {stroke(v1, v2, v3, aMax);}
    public void stroke(int v) {stroke(v, v, v, aMax);}
    public void noStroke() {currentStrokeColor = null;}

    public class color extends Color {} // implement Processing color type as a libgdx Color

    public color color(float v1, float v2, float v3, float a)
    {
        color tempColor = new color();
        setColor(tempColor, v1, v2, v3, a);
        return tempColor;
    }

    public color color(float v1, float v2, float v3) {return color(v1, v2, v3, aMax);}
    public color color(float v) {return color(v, v, v, aMax);}

    private void setColor(Color c, float v1, float v2, float v3, float a)
    {
        if (colorMode == HSB)
        {
            c.fromHsv(v1/v1Max*360, v2/v2Max, v3/v3Max);
            c.a = a/aMax;
        }
        else // colorMode == RGB
        {
            c.set(v1/v1Max, v2/v2Max, v3/v3Max, a/aMax);
        }
    }

    public void colorMode(int colorMode, float v1Max, float v2Max, float v3Max, float aMax)
    {
        this.colorMode = colorMode;
        this.v1Max = v1Max;
        this.v2Max = v2Max;
        this.v3Max = v3Max;
        this.aMax = aMax;
    }

    public void colorMode(int colorMode, float v1Max, float v2Max, float v3Max)
    {
        this.colorMode = colorMode;
        this.v1Max = v1Max;
        this.v2Max = v2Max;
        this.v3Max = v3Max;
    }

    public void colorMode(int colorMode, float max)
    {   
        this.colorMode = colorMode;
        this.v1Max = max;
        this.v2Max = max;
        this.v3Max = max;
    }

    public void colorMode(int colorMode)
    {
        this.colorMode = colorMode;
    }

    public void ellipse(float x, float y, float width, float height)
    {
        if (currentFillColor != null)
        {
            shapeRenderer.setColor(currentFillColor);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.ellipse(x-width/2, y-height/2, width, height);
            shapeRenderer.end();
        }

        if (currentStrokeColor != null)
        {
            shapeRenderer.setColor(currentStrokeColor);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.ellipse(x-width/2, y-height/2, width, height);
            shapeRenderer.end();
        }
    }

    public void line(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        shapeRenderer.setColor(currentStrokeColor);
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.line(x1, y1, z1, x2, y2, z2);
        shapeRenderer.end();
    }

    public void line(float x1, float y1, float x2, float y2) {line(x1, y1, 0, x2, y2, 0);}

    // TODO: implement strokeWidth
    // - for line(), use libgdx rectLine() in the xy plane?
    // - needs to work for ellipse() and rect()

    public void box(float w, float h, float d)
    {
        pushMatrix();
        translate(-w/2, -h/2, -d/2);

        if (currentFillColor != null)
        {
            shapeRenderer.setColor(currentFillColor);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.box(0, 0, 0, w, h, d);
            shapeRenderer.end();
        }

        if (currentStrokeColor != null)
        {
            shapeRenderer.setColor(currentStrokeColor);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.box(0, 0, 0, w, h, d);
            shapeRenderer.end();
        }

        popMatrix();

        // TODO: check this with 3D / perspective camera
        // TODO: refactor with ellipse()
    }

    public void box(float size) {box(size, size, size);}

    private void initializeShapeTypeMap()
    {
        shapeTypeMap = new HashMap<Integer, Integer>();
        shapeTypeMap.put(POLYGON, GL20.GL_TRIANGLE_FAN);
        // TODO: add shape types
    }

    public void beginShape(int kind)
    {
        ImmediateModeRenderer r = shapeRenderer.getRenderer();
        if (r == null) return;

        if (!shapeTypeMap.containsKey(kind))
            throw new RuntimeException("[PGraphics.beginShape()] Invalid shape type.");

        r.begin(camera.combined, shapeTypeMap.get(kind));
    }

    public void endShape()
    {
        ImmediateModeRenderer r = shapeRenderer.getRenderer();
        if (r == null) return;

        r.end();
    }

    public void vertex(float x, float y, float z)
    {
        ImmediateModeRenderer r = shapeRenderer.getRenderer();
        if (r == null) return;

        if (currentFillColor != null)
            r.color(currentFillColor);

        r.vertex(x, y, z);
    }

    public void vertex(float x, float y) {vertex(x, y, 0);}

    // TODO: texture(u, v)

    // Model-view matrix transformations: we're using the convenient libgdx
    // ShapeRenderer transformations, but with our own Matrix4 stack.

    public void pushMatrix()
    {
        matrixStack.push(shapeRenderer.getTransformMatrix().cpy());
    }

    public void popMatrix()
    {
        shapeRenderer.setTransformMatrix(matrixStack.pop());
    }

    private Stack<Matrix4> matrixStack = new Stack<Matrix4>();

    public void translate(float x, float y, float z)
    {
        shapeRenderer.translate(x, y, z);     
    }

    public void translate(float x, float y) {translate(x, y, 0);}

    // TODO: rotateX, rotateY

    public void rotateZ(float angle)
    {
        shapeRenderer.rotate(0, 0, 1, angle*180/PI);
    }

    public void rotate(float angle) {rotateZ(angle);}

    public void scale(float x, float y, float z)
    {
        shapeRenderer.scale(x, y, z);     
    }

    public void scale(float x, float y) {scale(x, y, 1);}

    
    // libgdx implementation 

    private FrameBuffer fb;

    //private OrthographicCamera camera;
    private PerspectiveCamera camera;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    // color handling

    final private Color fillColor = new Color();
    private Color currentFillColor = fillColor;

    final private Color strokeColor = new Color();
    private Color currentStrokeColor = strokeColor;

    private int colorMode = RGB;
    private float v1Max = 255.f;
    private float v2Max = 255.f;
    private float v3Max = 255.f;
    private float aMax = 255.f;

    // immediate mode rendering

    HashMap<Integer, Integer> shapeTypeMap;

}


