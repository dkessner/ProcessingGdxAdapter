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
        size(800, 400);
    }

    @Override
    public void setup()
    {
        img = loadImage("badlogic.jpg");
    }

    @Override
    public void draw()
    {
        background(0);

        image(img, 300, 300);

        fill(255, 0, 0);
        ellipse(100, 100, 100, 50);

        fill(0, 255, 0);
        ellipse(200, 200, 100, 50);

        fill(0, 0, 255);
        ellipse(300, 300, 100, 50);
    }

    private PImage img;
}


