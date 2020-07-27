import javax.swing.*;
import java.awt.*;

/**
 * Created by Armin on 5/30/2016.
 */
public class ImagePanel extends JPanel {

    Image mustDraw;
    Image front;

    public ImagePanel(Image i){
        mustDraw = i;

        setOpaque(false);
    }

    public void setImage(Image i){
        mustDraw = i;
        repaint();
    }

    public void setFrontImage(Image i){
        front = i;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(mustDraw,0,0,null);

        if(front != null){
            g.drawImage(front,0,0,null);
        }
    }
}
