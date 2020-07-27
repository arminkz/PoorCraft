import javax.swing.*;
import java.awt.*;

/**
 * Created by Armin on 6/1/2016.
 */
public class TimePanel extends JPanel {


    private GameWindow gw;
    private Image frame;
    private Image inner;

    public TimePanel(GameWindow parent){
        this.gw = parent;
        setOpaque(false);
        setSize(150,75);
        frame = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_4.png"))).getImage();
        inner = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_4_1.png"))).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(inner,9,-66,null);
        g.drawImage(frame,0,0,null);
    }
}
