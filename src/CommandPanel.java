import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class CommandPanel extends JPanel {

    GameWindow gw;

    Image patternImg;

    public CommandPanel(GameWindow parent){

        this.gw = parent;
        patternImg = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_2.png"))).getImage();

        setLayout(null);


        //shieldImg = (new ImageIcon(this.getClass().getResource("resources/images/misc/shield.png"))).getImage();
/*
        setLayout(new BorderLayout());

        JPanel sidePanel = new JPanel(new GridBagLayout());
        sidePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.VERTICAL;

        gbc.gridwidth = 1;
        //gbc.weightx = 0.25;

        changeBrushButton = new JButton("Brush Mode");
        changeBrushButton.setHorizontalAlignment(JButton.CENTER);
        changeBrushButton.setVerticalAlignment(JButton.CENTER);
        //changeBrushButton.setMargin(new Insets(20,20,20,20));
        gbc.gridx=0;
        gbc.gridy=0;
        sidePanel.add(changeBrushButton,gbc);

        switchRawButton = new JButton("Raw / Unraw");
        switchRawButton.setHorizontalAlignment(JButton.CENTER);
        switchRawButton.setVerticalAlignment(JButton.CENTER);
        //switchRawButton.setMargin(new Insets(20,20,20,20));
        gbc.gridx=0;
        gbc.gridy=1;
        sidePanel.add(switchRawButton,gbc);

        saveMapButton = new JButton("Save Map");
        saveMapButton.setHorizontalAlignment(JButton.CENTER);
        saveMapButton.setVerticalAlignment(JButton.CENTER);
        saveMapButton.addActionListener((ActionEvent e) -> {
            JFileChooser jfc = new JFileChooser();
            int returnVal = jfc.showOpenDialog(gw);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                GameMap.saveToFile(file,gw.rawMap);
            }
            else{
                //statusLabel.setText("Open command cancelled by user." );
            }
        });
        //saveMapButton.setMargin(new Insets(20,20,20,20));
        gbc.gridx=1;
        gbc.gridy=0;
        sidePanel.add(saveMapButton,gbc);

        loadMapButton = new JButton("Load Map");
        loadMapButton.setHorizontalAlignment(JButton.CENTER);
        loadMapButton.setVerticalAlignment(JButton.CENTER);
        loadMapButton.addActionListener((ActionEvent e) -> {
            JFileChooser jfc = new JFileChooser();
            int returnVal = jfc.showOpenDialog(gw);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                gw.rawMap = GameMap.loadFromFile(file);
                gw.map = MapHelper.compileMap(gw.rawMap);
            }
            else{
                //statusLabel.setText("Open command cancelled by user." );
            }
        });
        //loadMapButton.setMargin(new Insets(20,20,20,20));
        gbc.gridx=1;
        gbc.gridy=1;
        sidePanel.add(loadMapButton,gbc);

        add(sidePanel,BorderLayout.WEST);


        JPanel sidePanel2 = new JPanel();
        sidePanel2.setOpaque(false);

        undoButton = new JButton("Undo");
        undoButton.addActionListener((ActionEvent e) -> gw.gp.undo());
        sidePanel2.add(undoButton);

        redoButton = new JButton("Redo");
        redoButton.addActionListener((ActionEvent e) -> gw.gp.redo());
        sidePanel2.add(redoButton);

        add(sidePanel2,BorderLayout.EAST);


        //selectBrushButtons = new JToggleButton[8];
        TerrainBrushesPanel tbp = new TerrainBrushesPanel(gw);
        tbp.setOpaque(false);
        add(tbp);
        */
    }
    //ArrayList<CustomToggleButton> allButtons = new ArrayList<>();
    public void initButtons(ArrayList<CustomToggleButton> buttons){

        removeAll();

        int buttonCount = buttons.size();
        int margin = 15;
        int x0 = 250;
        int startLocX = (((getWidth()-x0) - ((130 + margin) * buttonCount)) / 2) + x0;
        int LocY = (getHeight() - 130) / 2;

        for(int i=0;i<buttonCount;i++){
            //CustomToggleButton test1 = new CustomToggleButton(gw.but1Images);
            buttons.get(i).setLocation(startLocX + (130 + margin) * i , LocY );
            //allButtons.add(test1);
            buttons.get(i).setGroup(buttons);
            add(buttons.get(i));
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int n = (this.getWidth() / 50) + 1;
        for (int i = 0; i < n ; i++) {
            g.drawImage(patternImg,50*i,0,null);
        }
    }
}
