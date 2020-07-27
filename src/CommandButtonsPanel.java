import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by Armin on 5/18/2016.
 */
public class CommandButtonsPanel extends JPanel {

    //ArrayList<JButton> cmdButtons;
    GameWindow gw;
    ArrayList<CustomButton> buttons;

    public CommandButtonsPanel(GameWindow parent){

        gw = parent;
        setBackground(new Color(37,50,55));

        setLayout(null);

        for (int i = 0; i < 3; i++) {
            int locY = 2+ i*72;
            for (int j = 0; j < 3; j++) {
                CustomButton cb = new CustomButton(gw.but2Images);
                cb.setLocation(3+j*72,locY);
                add(cb);
            }
        }
    }

    public void initButtons(ArrayList<CustomButton> buttons){
        if(buttons.size() > 9){
            return;
        }

        removeAll();

        for (int i = 0; i < 3; i++) {
            int locY = 2+ i*72;
            for (int j = 0; j < 3; j++) {
                CustomButton cb;
                if(i*3+j < buttons.size()) {
                    cb = buttons.get(i * 3 + j);
                }else{
                    cb = new CustomButton(gw.but2Images);
                }
                cb.setLocation(3+j*72,locY);
                add(cb);
            }
        }

        revalidate();
        repaint();
    }

}
