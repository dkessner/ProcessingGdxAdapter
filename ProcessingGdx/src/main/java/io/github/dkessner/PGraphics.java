//
// PGraphics.java
//
// Darren Kessner
//


package io.github.dkessner;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;

import static java.lang.Math.*;

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
    public PGraphics(int width, int height, String rendererType)
    {
        this(); 
        initialize(width, height, rendererType);

        final boolean hasDepth = false;
        fb = new FrameBuffer(Format.RGBA8888, width, height, hasDepth);
    }

    public PGraphics(int width, int height) {this(width, height, P2D);}

    /**
     * Initialization, which can be called during or after construction.
     *
     * - internal libgdx objects
     * - the coordinate system
     *      - y-axis down
     *      - (0,0) top right
     *      - (width, height) bottom left
     */
    void initialize(int width, int height, String rendererType) // package-private
    {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        final boolean flip = true; 
        font = new BitmapFont(flip);

        initializeShapeTypeMap();

        // initialization for the coordinate system

        this.width = width;
        this.height = height;

        if (rendererType == P3D) // string comparison by reference ok
            perspective();
        else
            ortho();

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

        if (camera instanceof PerspectiveCamera)
        {
            // set default Processing coordinate system:
            // - y-axis down
            // - origin upper left
            // - (width, height, 0) lower right

            float fov = radians(((PerspectiveCamera)camera).fieldOfView);
            float cameraZ = calculateProcessingCameraZ(fov, height);
            scale(1, -1, 1);
            translate(-width/2, -height/2, -cameraZ);
        }
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

    public void fill(float v1, float v2, float v3) {fill(v1, v2, v3, aMax);}
    public void fill(float v) {fill(v, v, v, aMax);} 

    public void fill(int c) 
    {
        if (isARGB(c)) 
            Color.argb8888ToColor(fillColor, c);
        else
            fill(c, c, c, aMax);
    }

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
    public void stroke(float v) {stroke(v, v, v, aMax);}

    public void stroke(int c)
    {
        if (isARGB(c))
            Color.argb8888ToColor(strokeColor, c);
        else
            stroke(c, c, c, aMax);
    }

    public void noStroke() {currentStrokeColor = null;}

    public int color(float v1, float v2, float v3, float a)
    {
        setColor(tempColor, v1, v2, v3, a);
        return Color.argb8888(tempColor);
    }

    public int color(float v1, float v2, float v3) {return color(v1, v2, v3, aMax);}
    public int color(float v) {return color(v, v, v, aMax);}

    public int color(int c)
    {
        if (isARGB(c))
            return c;
        else
            return color(c, c, c, aMax);
    }

    private boolean isARGB(int n)
    {
        // This function encapsulates the logic in Processing to determine
        // whether a single int value should be interpreted as ARGB or as
        // greyscale.

        return ((n & 0xff000000) != 0) || (n > v1Max);
    }

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

    public void text(String s, float x, float y)
    {
        batch.begin();
        font.draw(batch, s, x, y);
        batch.end();
    }

    // TODO: implement text() overloads

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
        translate(-w/2, -h/2, d/2); // libgdx box(): (x,y,z) == bottom left front

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

        // TODO: refactor with ellipse()
    }

    public void box(float size) {box(size, size, size);}

    private void initializeShapeTypeMap()
    {
        shapeTypeMap = new HashMap<Integer, Integer>();
        shapeTypeMap.put(POLYGON, GL20.GL_TRIANGLE_FAN);
        shapeTypeMap.put(LINE_STRIP, GL20.GL_LINE_STRIP);
        shapeTypeMap.put(LINE_LOOP, GL20.GL_LINE_LOOP);
        shapeTypeMap.put(POINTS, GL20.GL_POINTS);
        shapeTypeMap.put(TRIANGLE_STRIP, GL20.GL_TRIANGLE_STRIP);
        
        // TODO: add shape types
    }

    class Vertex
    {
        public float x;
        public float y;
        public float z;

        public float u;
        public float v;

        public Color fillColor;
        public Color strokeColor;

        public float nx;
        public float ny;
        public float nz;

        public Vertex(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void setFillColor(Color c)
        {
            fillColor = (c == null) ? c : c.cpy();
        }
        
        public void setStrokeColor(Color c)
        {
            strokeColor = (c == null) ? c : c.cpy();
        }
        
        public void setTexCoord(float u, float v)
        {
            this.u = u;
            this.v = v;
        }

        public void setNormal(float nx, float ny, float nz)
        {
            this.nx = nx;
            this.ny = ny;
            this.nz = nz;
        }
    }

    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    private int shapeType = GL20.GL_TRIANGLE_FAN;

    public void beginShape(int kind)
    {
        vertices.clear();

        if (!shapeTypeMap.containsKey(kind))
            throw new RuntimeException("[PGraphics.beginShape()] Invalid shape type.");

        shapeType = shapeTypeMap.get(kind);


    }

    public void beginShape() {beginShape(POLYGON);}

    public void endShape(int close)
    {
        drawShape(close == CLOSE);                
    }

    public void endShape() {endShape(0);}

    private void drawShape(boolean close)
    {
        ImmediateModeRenderer r = shapeRenderer.getRenderer();
        if (r == null) return;

        Matrix4 transformation = camera.combined.cpy();
        transformation.mul(shapeRenderer.getTransformMatrix());

        // TODO: Processing default behavior

        if (currentFillColor != null)
        {
            r.begin(transformation, shapeType);
            for (Vertex v : vertices)
            {
                r.color(v.fillColor);
                r.vertex(v.x, v.y, v.z);
                r.texCoord(v.u, v.v);
                r.normal(v.nx, v.ny, v.nz);
            }

            r.end();
        }

        if (shapeType == GL20.GL_TRIANGLE_FAN && currentStrokeColor != null)
        {
            r.begin(transformation, close ? GL20.GL_LINE_LOOP : GL20.GL_LINE_STRIP);
            for (Vertex v : vertices)
            {
                r.color(v.strokeColor);
                r.vertex(v.x, v.y, v.z);
                r.texCoord(v.u, v.v);
                r.normal(v.nx, v.ny, v.nz);
            }

            r.end();
        }
    }

    public void vertex(float x, float y, float z, float u, float v)
    {
        Vertex vertex = new Vertex(x, y, z);
        vertex.setFillColor(currentFillColor);
        vertex.setStrokeColor(currentStrokeColor);
        vertex.setTexCoord(u, v);
        vertices.add(vertex);
    }

    public void vertex(float x, float y, float z) {vertex(x, y, z, 0, 0);}
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

    public void resetMatrix()
    {
        shapeRenderer.identity();

        if (camera instanceof PerspectiveCamera)
        {
            scale(1, -1, 1);
        }
    }

    public void translate(float x, float y, float z)
    {
        shapeRenderer.translate(x, y, z);     
    }

    public void translate(float x, float y) {translate(x, y, 0);}

    public void rotateX(float angle)
    {
        shapeRenderer.rotate(1, 0, 0, angle*180/PI);
    }

    public void rotateY(float angle)
    {
        shapeRenderer.rotate(0, 1, 0, angle*180/PI);
    }

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

    //
    // camera
    //

    public void ortho() 
    {
        OrthographicCamera camera = new OrthographicCamera(width, height);
        this.camera = camera;

        final boolean yDown = true;
        camera.setToOrtho(yDown, width, height);

        camera.update();
        updateProjectionMatrices();
    }

    // TODO: ortho(left, right, bottom, top, near, far)
        
    public static float calculateProcessingCameraZ(float fov, float height)
    {
       return (float)(height / 2.0f / tan(fov/2));
    }

    public void perspective(float fov, float aspect, float near, float far)
    {
        camera = new PerspectiveCamera(degrees(fov), aspect, 1);

        // TODO: check Processing behavior -- likely these should be set in beforeDraw();
        // resetMatrix() called from draw() should un-set


        /*
        camera.position.x = width/2;
        camera.position.y = -height/2;
        camera.position.z = cameraZ(fov, height);
        */

        camera.near = near;
        camera.far = far;

        //camera.lookAt(camera.position.x, camera.position.y, 0);

        camera.update();
        updateProjectionMatrices();
    }

    public void perspective()
    {
        // defaults from Processing

        final float fov = PI/3;
        final float aspect = (float)width / height;

        final float cameraZ = calculateProcessingCameraZ(fov, height);
        final float near = cameraZ/10.0f;
        final float far = cameraZ*10.0f;

        perspective(fov, aspect, near, far);
    }

    protected void updateProjectionMatrices()
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
    }

    //
    // misc
    //

    public static float degrees(float angle) {return angle/PI*180;}
    public static float radians(float angleDegrees) {return angleDegrees*PI/180;}

    public static float sin(float value) {return (float)Math.sin(value);}
    public static float cos(float value) {return (float)Math.cos(value);}
    public static float tan(float value) {return (float)Math.tan(value);}
    public static float sqrt(float value) {return (float)Math.sqrt(value);}

    public static float lerp(float start, float stop, float amount)
    {
        return start + amount*(stop-start);
    }

    public static int alpha(int c) {return c>>24 & 0xff;}
    public static int red(int c) {return c>>16 & 0xff;}
    public static int green(int c) {return c>>8 & 0xff;}
    public static int blue(int c) {return c & 0xff;}

    private static int argb(int r, int g, int b, int a) {return a<<24 | r<<16 | g<<8 | b;}

    public static int lerpColor(int c1, int c2, float amount)
    {
        return argb((int)lerp(red(c1), red(c2), amount),
                    (int)lerp(green(c1), green(c2), amount),
                    (int)lerp(blue(c1), blue(c2), amount),
                    (int)lerp(alpha(c1), alpha(c2), amount));
    }

    public static float random(float low, float high)
    {
        return (float)(low + Math.random() * (high-low));
    }

    public static float random(float high) {return random(0, high);}

    public static float max(float a, float b) {return Math.max(a, b);}
    public static float max(float a, float b, float c) {return max(max(a, b),c);}
    
    // libgdx implementation 

    private FrameBuffer fb;
    protected Camera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private BitmapFont font;

    // color handling

    final private Color fillColor = new Color();
    private Color currentFillColor = fillColor;

    final private Color strokeColor = new Color();
    private Color currentStrokeColor = strokeColor;

    final private static Color tempColor = new Color(); // temporary storage to avoid memory allocations

    private int colorMode = RGB;
    private float v1Max = 255.f;
    private float v2Max = 255.f;
    private float v3Max = 255.f;
    private float aMax = 255.f;

    // immediate mode rendering

    HashMap<Integer, Integer> shapeTypeMap;
}


