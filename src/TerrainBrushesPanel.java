import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Armin on 5/20/2016.
 */
public class TerrainBrushesPanel extends JPanel {

    //Integer activeTerrainBrush;

    public TerrainBrushesPanel(GameWindow gw){

        //activeTerrainBrush = gw.activeTerrainBrush;

        setLayout(new GridLayout(0,9));

        JToggleButton grassBrushButton = new JToggleButton(new ImageIcon(gw.bgSprites[0].getImage()));
        grassBrushButton.addActionListener((ActionEvent e) -> {
            unselectAllButMe(grassBrushButton);
            gw.activeTerrainBrush = 0;
        });
        JToggleButton rockBrushButton = new JToggleButton(new ImageIcon(gw.bgSprites[1].getImage()));
        rockBrushButton.addActionListener((ActionEvent e) -> {
            unselectAllButMe(rockBrushButton);
            gw.activeTerrainBrush = 1;
        });
        JToggleButton waterBrushButton = new JToggleButton(new ImageIcon(gw.bgSprites[2].getImage()));
        waterBrushButton.addActionListener((ActionEvent e) -> {
            unselectAllButMe(waterBrushButton);
            gw.activeTerrainBrush = 2;
        });
        grassBrushButton.setSelected(true);
        add(grassBrushButton);
        add(rockBrushButton);
        add(waterBrushButton);
    }

    public void unselectAllButMe(JToggleButton me){
        for(Component c : this.getComponents()){
            if(c instanceof JToggleButton && !(me == c)){
                ((JToggleButton)c).setSelected(false);
            }
        }
    }

}
