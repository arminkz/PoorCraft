import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomButton extends JComponent implements MouseListener {

    boolean isHover = false;
    boolean isPressed = false;
    Dimension size;

    private Image icon;
    private Image glowImage;
    private Image buttonNormal;
    private Image buttonPressed;

    private ActionListener al;

    public CustomButton(Image[] imgs){
        super();
        enableInputMethods(true);
        addMouseListener(this);
        buttonNormal = imgs[0];
        buttonPressed = imgs[1];
        glowImage = imgs[2];
        size = new Dimension(imgs[0].getWidth(null),imgs[0].getHeight(null));
        setSize(size);
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public ActionListener getAction() {
        return al;
    }

    public void setAction(ActionListener al) {
        this.al = al;
    }

    @Override
    public Dimension getMaximumSize() {
        return size;
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isPressed){
            g.drawImage(buttonNormal,(size.width - buttonNormal.getWidth(null)) / 2,(size.height - buttonNormal.getHeight(null)) / 2,null);
        }else{
            g.drawImage(buttonPressed,(size.width - buttonPressed.getWidth(null)) / 2,(size.height - buttonPressed.getHeight(null)) / 2,null);
        }

        if(icon != null){
            g.drawImage(icon,(size.width - icon.getWidth(null)) / 2,(size.height - icon.getHeight(null)) / 2,null);
        }

        if (isHover){
            g.drawImage(glowImage,(size.width - glowImage.getWidth(null)) / 2,(size.height - glowImage.getHeight(null)) / 2,null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        isPressed = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
        repaint();
        //do action Here
        if (al != null) al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isHover = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isHover = false;
        repaint();
    }
}
