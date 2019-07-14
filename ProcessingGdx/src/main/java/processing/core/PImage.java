//
// PImage.java
//
// Darren Kessner
//


package processing.core;


import com.badlogic.gdx.graphics.Texture;


public class PImage implements PConstants
{
    public PImage() {}

    public PImage(String filename) 
    {
        texture = new Texture(filename);
        width = texture.getWidth();
        height = texture.getHeight();
        flipY = true;
    }

    @Override
    public void finalize() 
    {
        texture.dispose();
    }

    protected int width;
    protected int height;
    protected boolean flipY = false;

    // libgdx implementation

    Texture getTexture() {return texture;}
    boolean flipY() {return flipY;}

    private Texture texture;
}

