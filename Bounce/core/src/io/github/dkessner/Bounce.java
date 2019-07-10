//
// Bounce.java
//
// Darren Kessner
//


package io.github.dkessner;


import processing.core.*;


public class Bounce extends PApplet
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

        x = width/2;
        y = height/2;
    }

    @Override
    public void draw()
    {
        background(0);

        image(img, imgX, imgY);

        fill(0, 0, 255);

        ellipse(x, y, 2*r, 2*r);

        x += vx;
        y += vy;

        if (x<r || x>width-r) vx *= -1;
        if (y<r || y>height-r) vy *= -1;

        imgX += imgVx;
        imgY += imgVy;

        if (imgX<0 || imgX>width-img.width) imgVx *= -1;
        if (imgY<0 || imgY>height-img.height) imgVy *= -1;
    }

    private PImage img;

    private int r = 25;
    private int x;
    private int y;
    private int vx = 2;
    private int vy = 3;

    private int imgX = 0;
    private int imgY = 0;
    private int imgVx = 3;
    private int imgVy = 2;
}


