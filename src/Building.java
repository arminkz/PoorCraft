import java.awt.*;
import java.io.Serializable;

/**
 * Created by Armin on 5/20/2016.
 */
public abstract class Building extends GameObject implements Cloneable , Serializable {


    private int width;
    private int height;

    public Building(Sprite sprite, int w, int h){
        super(sprite);
        this.width = w;
        this.height = h;
    }

    //Overridable (by Default valid terain is grass / soil)
    public boolean isValidTerrain(int base){
        return (base != 2);
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
}
