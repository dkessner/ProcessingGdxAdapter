//
// PImage.java
//
// Darren Kessner
//


package processing.core;


import com.badlogic.gdx.graphics.Texture;


public class PImage
{
    public PImage() {}

    public PImage(String filename) 
    {
        texture = new Texture(filename);
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    public void finalize() 
    {
        texture.dispose();
    }

    protected int width;
    protected int height;

    // libgdx implementation

    Texture getTexture() {return texture;}

    private Texture texture;
}

