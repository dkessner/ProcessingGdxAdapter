//
// Test3D.java
//
// Darren Kessner
//


package io.github.dkessner;


import processing.core.*;


public class Test3D extends PApplet
{
    @Override
    public void settings()
    {
        size(400, 400);
    }

    @Override
    public void setup()
    {
    }

    @Override
    public void draw()
    {
        background(0);

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

        pushMatrix();
        translate(200, 200);
        drawAxes();
        popMatrix();
    }

    void drawAxes()
    {
        final float length = 200;

        colorMode(RGB, 255);

        // red positive x-axis
        stroke(255, 0, 0);
        line(0, 0, 0, length, 0, 0);
        stroke(255);
        line(-length, 0, 0, 0, 0, 0);

        // green positive y-axis
        stroke(0, 255, 0);
        line(0, 0, 0, 0, length, 0);
        stroke(255);
        line(0, -length, 0, 0, 0, 0);

        // blue positive z-axis
        stroke(0, 0, 255);
        line(0, 0, 0, 0, 0, length);
        stroke(255);
        line(0, 0, -length, 0, 0, 0);

        drawTicks();

        fill(0, 255, 0);
        box(20);
    }


    void drawTicks()
    {
        final int red = color(255, 0, 0);
        final int green = color(0, 255, 0);
        final int blue = color(0, 0, 255);
        final int white = color(255);

        // red boxes on x-axis

        stroke(0);

        for (int i=-4; i<=4; i++)
        {
            if (i==0) continue;
            fill(i>0 ? red : white);
            pushMatrix();
            translate(25*i, 0, 0);
            box(i%4==0 ? 10 : 5);
            popMatrix();
        }

        // green boxes on y-axis

        for (int i=-4; i<=4; i++)
        {
            if (i==0) continue;
            fill(i>0 ? green : white);
            pushMatrix();
            translate(0, 25*i, 0);
            box(i%4==0 ? 10 : 5);
            popMatrix();
        }

        // blue boxes on z-axis

        for (int i=-4; i<=4; i++)
        {
            if (i==0) continue;
            fill(i>0 ? blue : white);
            pushMatrix();
            translate(0, 0, i*25);
            box(i%4==0 ? 10 : 5);
            popMatrix();
        }
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
}


