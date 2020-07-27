import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class CustomToggleButton extends JComponent implements MouseListener {

    private boolean isHover = false;
    private boolean selected = false;
    Dimension size;

    private Image icon;
    private Image glowImage;
    private Image buttonNormal;
    private Image buttonPressed;

    ArrayList<CustomToggleButton> tbs;

    private ActionListener al;

    public CustomToggleButton(Image[] imgs){
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
        repaint();
    }

    public ActionListener getAction() {
        return al;
    }

    public void setAction(ActionListener al) {
        this.al = al;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setGroup(ArrayList<CustomToggleButton> tbs) {
        this.tbs = tbs;
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

        if (!selected){
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
        if(tbs != null) { //if Group set then acts like radioButton
            if (!selected) {
                for (CustomToggleButton tb : tbs) {
                    tb.setSelected(false);
                }
                if (al != null) al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
            }
            selected = true;
        }else{ // if Group not set then acts like checkBox
            selected = !selected;
            if (al != null) al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

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