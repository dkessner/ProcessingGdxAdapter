//
// PApplet.java
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


public class PApplet
{
    public void create()
    {
        Gdx.graphics.setWindowedMode(width, height);
        camera = new OrthographicCamera(width, height);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    public void setup()
    {

    }

    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(img, imgX, imgY);
        batch.end();

        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.circle(x, y, r);
        shapeRenderer.end();

        x += vx;
        y += vy;

        if (x<r || x>width-r) vx *= -1;
        if (y<r || y>height-r) vy *= -1;

        imgX += imgVx;
        imgY += imgVy;

        if (imgX<0 || imgX>width-img.getWidth()) imgVx *= -1;
        if (imgY<0 || imgY>height-img.getHeight()) imgVy *= -1;
    }

    public void draw()
    {

    }

    public void resize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void dispose() 
    {
        batch.dispose();
        img.dispose();
    }

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Texture img;

    private int width = 800;
    private int height = 600;

    private int r = 25;
    private int x = width/2;
    private int y = height/2;
    private int vx = 2;
    private int vy = 3;

    private int imgX = 0;
    private int imgY = 0;
    private int imgVx = 3;
    private int imgVy = 2;
}


