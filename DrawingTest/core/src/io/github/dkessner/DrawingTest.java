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

        colorMode(RGB, 255);
        fill(255, 0, 0);
        stroke(255);
        ellipse(100, 100, 100, 50);

        colorMode(RGB, 1.0f);
        noFill();
        stroke(0, 1.0f, 0);
        ellipse(200, 200, 100, 50);

        colorMode(HSB, 360, 100, 100);
        fill(240, 100, 100); // blue HSV 
        noStroke();
        ellipse(300, 300, 100, 50);

        // draw the contents of the offscreen buffer

        image(pg, 0, 300, 100, 100);

        drawPolygon();
        drawRotatedEllipses();
    }

    public void drawRotatedEllipses()
    {
        translate(200, 50); 

        pushMatrix();
        rotate(PI/4);
        fill(255, 255, 0); // yellow
        ellipse(0, 0, 100, 50);
        popMatrix();

        fill(0, 0, 255);
        ellipse(0, 0, 50, 25);
    }

    public void drawPolygon()
    {
        colorMode(RGB, 255);
        beginShape(POLYGON);
        fill(255, 0, 0);
        vertex(300, 200);
        fill(0, 255, 0);
        vertex(400, 200);
        fill(0, 0, 255);
        vertex(350, 250);
        endShape();
    }

    public void drawOffscreen()
    {
        pg.beginDraw();

        pg.background(50);

        pg.image(img, 300, 0, 100, 100);

        pg.noStroke();
        pg.fill(255, 0, 0);
        pg.ellipse(100, 100, 100, 50);

        pg.fill(0, 255, 0);
        pg.ellipse(200, 200, 100, 50);

        pg.fill(0, 0, 255);
        pg.ellipse(300, 300, 100, 50);

        pg.endDraw();
    }

    @Override
    public void keyPressed()
    {
        println("processing keyPressed key: " + (int)key + " keyCode: " + keyCode); 
    }

    @Override
    public void keyTyped()
    {
        println("processing keyTyped key: " + (int)key + " keyCode: " + keyCode); 
    }

    @Override
    public void keyReleased()
    {
        println("processing keyReleased key: " + (int)key + " keyCode: " + keyCode); 
    }

    private PImage img;
    private PGraphics pg;
}


