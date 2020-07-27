import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Armin on 5/14/2016.
 */
public class Sprite implements Serializable{

    private ImageIcon spriteImg;
    private Point spriteAnchor;

    public Sprite(String imageName,Point anchor){
        //load Image using ImageIcon
        try {
            this.spriteImg = (new ImageIcon(this.getClass().getResource("resources/images/" + imageName)));
        }catch (Exception e){
            System.err.println("Sprite not Found : " + imageName);
        }
        this.spriteAnchor = anchor;
    }

    public Sprite(String imageName){
        this(imageName,new Point(0,0));
    }

    public Image getImage() {
        return spriteImg.getImage();
    }

    public Point getAnchor() {
        return spriteAnchor;
    }
}

