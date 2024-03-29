//
// PApplet.java
//
// Darren Kessner
//


package processing.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;


public class PApplet
{
    public class PImage extends Texture 
    {
        public PImage(String filename) 
        {
            super(filename); 
            width = getWidth();
            height = getHeight();
        }

        @Override
        public void finalize() {super.dispose();}

        public int width;
        public int height;
    }

    public void size(int width, int height)
    {
        Gdx.graphics.setWindowedMode(width, height);
        camera = new OrthographicCamera(width, height);
        this.width = width;
        this.height = height;
    }

    public void create()
    {
        setup(); // note: this must be first?

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
    }

    public void setup() {}

    public PImage loadImage(String filename)
    {
        return new PImage(filename);
    }

    public void image(PImage img, int x, int y)
    {
        batch.begin();
        batch.draw(img, x, y);
        batch.end();
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
        shapeRenderer.ellipse(x-width/2, y-height/2, width, height); // ellipse draw by corner?
        //shapeRenderer.circle(x, y, width/2);
        shapeRenderer.end();
    }

    public void render()
    {
        draw();
    }

    public void draw() {}

    public void resize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void dispose() 
    {
        batch.dispose();
    }

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    protected int width = 400;
    protected int height = 400;
}


