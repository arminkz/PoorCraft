import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * Created by Armin on 7/4/2016.
 */
public class HoverAnimationButton extends HoverButton {

    Timer animTimer;
    int animState;
    int frames;

    Image[] animImgs;

    public HoverAnimationButton(Image[] imgs,Image[] animImgs,int frames){
        super(imgs);

        this.animImgs = animImgs;
        this.frames = frames;

        animState = 0;

        animTimer = new Timer(50,(ActionEvent e) -> {
            animState = (animState + 1) ;
            repaint();
            if(animState == frames + 3){
                animTimer.stop();
                al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(animState > 0 && animState < frames + 1){
            g.drawImage(animImgs[animState - 1],0,0,null);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isEnabled && al != null) animTimer.start();
    }

}
