//
// Test3D.java
//
// Darren Kessner
//


package io.github.dkessner;

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

        final float cameraZ = height/2.0f / tan(PI/6);
        cameraPosition.z = cameraZ;

        final float fov = PI/3;
        final float aspect = (float)width / height;
        final float near = cameraZ/100.0f;
        final float far = cameraZ*10.0f;

        perspective(fov, aspect, near, far);
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

        fill(255);
        int x = 25, y = 25, step = 25;
        text("arrow keys: yaw/pitch", x, y);
        text("shift + arrow keys: translate x/y", x, y+=step);
        text("asdw: translate y/z", x, y+=step);
        text("f: full screen (bug returning to window in web app)", x, y+=step);

        updateCamera();

        resetMatrix();
        rotateX(cameraPitch);
        rotateY(cameraYaw);
        translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

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

        colorMode(RGB, 255);
        fill(255);
        text("Hello, world!", 150, 150);

        pushMatrix();
        drawAxes();
        drawPoints();
        drawPoints2();
        drawPoints3();
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
            beginShape();
            for (int j=0; j<=n; j++)
            {
                float t = 2*PI*j/n;
                vertex(p1.x + r*cos(t), p1.y + r*sin(t), p1.z);
                vertex(p2.x + r*cos(t), p2.y + r*sin(t), p2.z);
            }
            endShape();
        }
    }

    final float cameraSpeed = 5.0f;
    final float cameraAngularSpeed = radians(1.2f);

    PVector cameraPosition = new PVector();
    PVector cameraVelocity = new PVector();
    float cameraYaw;
    float cameraYawVelocity;
    float cameraPitch;
    float cameraPitchVelocity;

    void cameraMoveX(float speed) {cameraVelocity.x = speed;}
    void cameraMoveY(float speed) {cameraVelocity.y = speed;}
    void cameraMoveZ(float speed) {cameraVelocity.z = speed;}
    void cameraMoveYaw(float speed) {cameraYawVelocity = speed;}
    void cameraMovePitch(float speed) {cameraPitchVelocity = speed;}

    PMatrix3D getCameraMatrix() 
    {
        PMatrix3D transformation = new PMatrix3D();
        transformation.rotateY(-cameraYaw);
        transformation.rotateX(-cameraPitch);
        return transformation;
    }

    void updateCamera()
    {
        PMatrix3D transformation = getCameraMatrix();
        PVector step = transformation.mult(cameraVelocity, null);
        cameraPosition.add(step); 

        cameraYaw += cameraYawVelocity;
        cameraPitch += cameraPitchVelocity;
    }


    private boolean shift = false;

    @Override
    public void keyPressed()
    {
        if (keyCode == SHIFT)
            shift = true;
        else if (keyCode == RIGHT)
        {
            if (shift) 
                cameraMoveX(cameraSpeed);
            else 
                cameraMoveYaw(cameraAngularSpeed);
        }
        else if (keyCode == LEFT)
        {
            if (shift)
                cameraMoveX(-cameraSpeed);
            else
                cameraMoveYaw(-cameraAngularSpeed);
        }
        else if (keyCode == UP)
        {
            if (shift)
                cameraMoveY(-cameraSpeed);
            else
                cameraMovePitch(cameraAngularSpeed);
        }
        else if (keyCode == DOWN)
        {
            if (shift)
                cameraMoveY(cameraSpeed);
            else
                cameraMovePitch(-cameraAngularSpeed);
        }
        else if (key == 'w')
            cameraMoveZ(-cameraSpeed);
        else if (key == 's')
            cameraMoveZ(cameraSpeed);
        else if (key == 'a')
            cameraMoveX(-cameraSpeed);
        else if (key == 'd')
            cameraMoveX(cameraSpeed);
        else if (key == 'o')
            ortho();
        else if (key == 'p')
            perspective();
        else if (key == 'f')
            fullScreen(P3D);
        else if (key == 'F')
            size(800, 600, P3D);
    }

    @Override
    public void keyReleased()
    {
        if (keyCode == SHIFT)
            shift = false;
        else if (keyCode == RIGHT || keyCode == LEFT)
        {
            if (shift)
                cameraMoveX(0);
            else
                cameraMoveYaw(0);
        }
        else if (keyCode == UP || keyCode == DOWN)
        {
            if (shift)
                cameraMoveY(0);
            else
                cameraMovePitch(0);
        }
        else if (key == 'w' || key == 's')
            cameraMoveZ(0);
        else if (key == 'a' || key == 'd')
            cameraMoveX(0);
    }

    ArrayList<PVector> points;
    ArrayList<PVector> points2;
    ArrayList<PVector> points3;
}


