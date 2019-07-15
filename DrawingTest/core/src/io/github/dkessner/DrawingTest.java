//
// DrawingTest.java
//
// Darren Kessner
//


package io.github.dkessner;


import processing.core.*;


public class DrawingTest extends PApplet
{
    @Override
    public void settings()
    {
        size(400, 400);
    }

    @Override
    public void setup()
    {
        img = loadImage("badlogic.jpg");

        pg = createGraphics(400, 400);
        drawOffscreen();
    }

    @Override
    public void draw()
    {
        background(0);

        // draw the badlogic logo

        image(img, 300, 0, 100, 100);

        // draw RGB ellipses

        fill(255, 0, 0);
        stroke(255);
        ellipse(100, 100, 100, 50);

        noFill();
        stroke(0, 255, 0);
        ellipse(200, 200, 100, 50);

        fill(0, 0, 255);
        noStroke();
        ellipse(300, 300, 100, 50);

        // draw the contents of the offscreen buffer

        image(pg, 0, 300, 100, 100);
    }

    public void drawOffscreen()
    {
        pg.beginDraw();

        pg.image(img, 300, 0, 100, 100);

        pg.fill(255, 0, 0);
        pg.ellipse(100, 100, 100, 50);

        pg.fill(0, 255, 0);
        pg.ellipse(200, 200, 100, 50);

        pg.fill(0, 0, 255);
        pg.ellipse(300, 300, 100, 50);

        pg.endDraw();
    }

    private PImage img;
    private PGraphics pg;
}


