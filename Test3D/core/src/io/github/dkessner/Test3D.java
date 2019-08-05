//
// Test3D.java
//
// Darren Kessner
//


package io.github.dkessner;


import processing.core.*;
import java.util.*;


public class Test3D extends PApplet
{
    @Override
    public void settings()
    {
        size(800, 600, P3D);
    }

    @Override
    public void setup()
    {
        initializePoints();
    }

    private void initializePoints()
    {
        points = new ArrayList<PVector>();
        points2 = new ArrayList<PVector>();
        points3 = new ArrayList<PVector>();

        PVector current = new PVector(-100, -100, -100);
        while (current.mag() <= 200)
        {
            points.add(current.copy());
            current.add(random(5), random(5), random(5));
        }

        current.set(-100, 100, 100);
        while (current.mag() <= 200)
        {
            points2.add(current.copy());
            current.add(random(20), random(-20, 0), random(-20, 0));
        }

        current.set(100, -100, 100);
        while (current.mag() <= 200)
        {
            points3.add(current.copy());
            current.add(random(-5, 0), random(5), random(-3, -2));
        }
    }

    @Override
    public void draw()
    {
        background(0);

        translate(-cameraX, -cameraY, -cameraZ);

        cameraX += cameraVX;
        cameraY += cameraVY;
        cameraZ += cameraVZ;

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
        drawAxes();
        drawPoints();
        drawPoints2();
        //drawPoints3();
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

    void drawPoints()
    {
        final int c1 = color(0xff226de5);
        final int c2 = color(0xff50e550);

        for (PVector p : points)
        {
            pushMatrix();
            translate(p.x, p.y, p.z);
            fill(lerpColor(c1, c2, (p.x+100)/200));
            box(3);
            popMatrix();
        }
    }

    void drawPoints2()
    {
        final int c1 = color(0xfff77111);
        final int c2 = color(0xffcfea20);

        noFill();
        beginShape();
        for (PVector p : points2)
        {
            stroke(lerpColor(c1, c2, (p.x+100)/200));
            vertex(p.x, p.y, p.z);
        }
        endShape();
    }


    void drawPoints3()
    {
        final int c1 = color(0xff000000);
        final int c2 = color(0xff888888);
        final int n = 20;

        for (int i=0; i<points3.size()-1; i++)
        {
            PVector p1 = points3.get(i);
            PVector p2 = points3.get(i+1);
            final float r = 10;

            fill(lerpColor(c1, c2, (p1.x+100)/200));
            stroke(200);
            beginShape(LINE_LOOP);
            for (int j=0; j<=n; j++)
            {
                float t = 2*PI*j/n;
                vertex(p1.x + r*cos(t), p1.y + r*sin(t), p1.z);
                vertex(p2.x + r*cos(t), p2.y + r*sin(t), p2.z);
            }
            endShape();
        }
    }



    float cameraX;
    float cameraY;
    float cameraZ;

    float cameraVX;
    float cameraVY;
    float cameraVZ;

    // TODO: implement PVector, PMatrix

    @Override
    public void keyPressed()
    {
        println("processing keyPressed key: " + (int)key + " keyCode: " + keyCode); 

        final float speed = 5.0f;

        if (keyCode == RIGHT)
            cameraVX = speed;
        else if (keyCode == LEFT)
            cameraVX = -speed;
        else if (keyCode == UP)
            cameraVY = -speed;
        else if (keyCode == DOWN)
            cameraVY = speed;
        else if (key == 'a')
            cameraVZ = -speed;
        else if (key == 'z')
            cameraVZ = speed;
        else if (key == 'o')
            ortho();
        else if (key == 'p')
            perspective();
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

        if (keyCode == RIGHT)
            cameraVX = 0;
        else if (keyCode == LEFT)
            cameraVX = 0;
        else if (keyCode == UP)
            cameraVY = 0;
        else if (keyCode == DOWN)
            cameraVY = 0;
        else if (key == 'a')
            cameraVZ = 0;
        else if (key == 'z')
            cameraVZ = 0;
        else if (key == 'f')
            fullScreen(P3D);
        else if (key == 'w')
            size(800, 600, P3D);

        // TODO: weirdness with ortho/perspective switching after fullScreen/size switch on html 
    }

    ArrayList<PVector> points;
    ArrayList<PVector> points2;
    ArrayList<PVector> points3;
}


