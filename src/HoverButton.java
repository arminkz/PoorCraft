import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HoverButton extends JComponent implements MouseListener {

    boolean isHover = false;
    boolean isPressed = false;
    protected boolean isEnabled = true;
    Dimension size;

    private Image icon;
    private Image buttonNormal;
    private Image buttonHover;

    protected ActionListener al;

    public HoverButton(Image[] imgs){
        super();
        enableInputMethods(true);
        addMouseListener(this);
        buttonNormal = imgs[0];
        buttonHover = imgs[1];
        size = new Dimension(imgs[0].getWidth(null),imgs[0].getHeight(null));
        setSize(size);
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
        repaint();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        repaint();
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

        if (isHover && isEnabled){
            g.drawImage(buttonHover,(size.width - buttonHover.getWidth(null)) / 2,(size.height - buttonHover.getHeight(null)) / 2,null);
        }else{
            g.drawImage(buttonNormal,(size.width - buttonNormal.getWidth(null)) / 2,(size.height - buttonNormal.getHeight(null)) / 2,null);
        }

        if(icon != null){
            if(isEnabled) {
                g.drawImage(icon, (size.width - icon.getWidth(null)) / 2, (size.height - icon.getHeight(null)) / 2, null);
            }else{
                g.drawImage(ImageHelper.toGrayScale(icon), (size.width - icon.getWidth(null)) / 2, (size.height - icon.getHeight(null)) / 2, null);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (isEnabled && al != null) al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
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
