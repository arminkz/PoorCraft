import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GameWindow extends JFrame implements MouseMotionListener {

    GameMenu gm;
    GamePanel gp;
    MiniMap mm;
    CommandPanel cp;
    CommandButtonsPanel cbp;
    TimePanel tp;
    HoverButton switchRawButton;
    HoverButton changeBrushButton;
    ImagePanel shieldHole;

    ArrayList<CustomToggleButton> terrainButtons;
    ArrayList<CustomToggleButton> buildingButtons;
    ArrayList<CustomButton> mapEditorButtons;

    Image rawImg,unrawImg,brushImg,fillImg;

    //Maps
    GameMap<Integer> rawMap;
    GameMap<String> map;
    GameObjectMap buildings;

    //Map Size
    int mapSizeX = 100;
    int mapSizeY = 50;

    //HUD Images
    Image[] hudImages;

    //Button Images
    Image[] but1Images;
    Image[] but2Images;
    Image[] butHBigImages;
    Image[] butHSmallImages;

    //Sprites
    //TODO:Create Static Class for Sprites
    Sprite[] bgSprites;
    Sprite[] rawSprites;
    HashMap<String,Sprite> unrawSprites;
    HashMap<String,Sprite> unrawMiniSprites;

    Sprite[][][] knightSprites;

    //Icons
    Image undoIcon;
    Image redoIcon;
    Image terrainIcon;
    Image buildingIcon;
    Image treeIcon;
    Image saveIcon;
    Image loadIcon;

    //Scroll
    boolean[] scrollState;
    Timer scrollHandler;

    Sprite voidSprite;
    Sprite highlightSprite;
    Sprite highlightGreenSprite;
    Sprite highlightRedSprite;


    public boolean isRaw;

    public Integer BrushMode; //0 Square // 1 Point
    public Integer BrushType; //0 Terrain //1 Building
    public Integer activeTerrainBrush;
    public Building activeBuildingBrush;
    public boolean isBuildingBrushValid;
    public Image activeBuildingBrushRedTint;
    public Image activeBuildingBrushGreenTint;


    //Load Images From Resource
    private void loadImages(){
        hudImages = new Image[5];
        hudImages[0] = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_1.png"))).getImage();
        hudImages[1] = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_1_1.png"))).getImage();
        hudImages[2] = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_2.png"))).getImage();
        hudImages[3] = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_3.png"))).getImage();
        hudImages[4] = (new ImageIcon(this.getClass().getResource("resources/images/hud/HUD_1_2.png"))).getImage();

        but1Images = new Image[3];
        but1Images[0] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/but1_released.png"))).getImage();
        but1Images[1] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/but1_pressed.png"))).getImage();
        but1Images[2] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/but1_glow.png"))).getImage();

        but2Images = new Image[3];
        but2Images[0] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/but2_released.png"))).getImage();
        but2Images[1] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/but2_pressed.png"))).getImage();
        but2Images[2] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/but2_glow.png"))).getImage();

        butHBigImages = new Image[2];
        butHBigImages[0] =  (new ImageIcon(this.getClass().getResource("resources/images/custom_button/butHB_normal.png"))).getImage();
        butHBigImages[1] =  (new ImageIcon(this.getClass().getResource("resources/images/custom_button/butHB_hover.png"))).getImage();

        butHSmallImages = new Image[2];
        butHSmallImages[0] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/butHS_normal.png"))).getImage();
        butHSmallImages[1] = (new ImageIcon(this.getClass().getResource("resources/images/custom_button/butHS_hover.png"))).getImage();

        rawImg = (new ImageIcon(this.getClass().getResource("resources/images/icon/raw.png"))).getImage();
        unrawImg = (new ImageIcon(this.getClass().getResource("resources/images/icon/unraw.png"))).getImage();
        brushImg = (new ImageIcon(this.getClass().getResource("resources/images/icon/brush.png"))).getImage();
        fillImg = (new ImageIcon(this.getClass().getResource("resources/images/icon/fill.png"))).getImage();

        undoIcon = (new ImageIcon(this.getClass().getResource("resources/images/icon/undo.png"))).getImage();
        redoIcon = (new ImageIcon(this.getClass().getResource("resources/images/icon/redo.png"))).getImage();
        terrainIcon = (new ImageIcon(this.getClass().getResource("resources/images/icon/terrain.png"))).getImage();
        buildingIcon = (new ImageIcon(this.getClass().getResource("resources/images/icon/building2.png"))).getImage();
        treeIcon = (new ImageIcon(this.getClass().getResource("resources/images/icon/tree.png"))).getImage();
        saveIcon = (new ImageIcon(this.getClass().getResource("resources/images/icon/save.png"))).getImage();
        loadIcon = (new ImageIcon(this.getClass().getResource("resources/images/icon/load.png"))).getImage();
    }

    private Image getImageFromResource(String name){
        return (new ImageIcon(this.getClass().getResource("resources/images/"+name))).getImage();
    }

    private void createMapEditorCommands(){
        terrainButtons = new ArrayList<>();
        CustomToggleButton grassTerrainButton = new CustomToggleButton(but1Images);
        grassTerrainButton.setIcon(getImageFromResource("icon/t_grass.png"));
        grassTerrainButton.setAction((ActionEvent e) -> activeTerrainBrush = 0);
        CustomToggleButton rockTerrainButton = new CustomToggleButton(but1Images);
        rockTerrainButton.setIcon(getImageFromResource("icon/t_rock.png"));
        rockTerrainButton.setAction((ActionEvent e) -> activeTerrainBrush = 1);
        CustomToggleButton waterTerrainButton = new CustomToggleButton(but1Images);
        waterTerrainButton.setIcon(getImageFromResource("icon/t_water.png"));
        waterTerrainButton.setAction((ActionEvent e) -> activeTerrainBrush = 2);
        terrainButtons.add(grassTerrainButton);
        terrainButtons.add(rockTerrainButton);
        terrainButtons.add(waterTerrainButton);

        buildingButtons = new ArrayList<>();
        CustomToggleButton barracksBuildingButton = new CustomToggleButton(but1Images);
        barracksBuildingButton.setIcon(getImageFromResource("icon/b_barracks.png"));
        barracksBuildingButton.setAction((ActionEvent e) -> {
            activeBuildingBrush = new Barracks();
            activeBuildingBrushRedTint = ImageHelper.tint(activeBuildingBrush.getObjectSprite().getImage(),Color.red);
            activeBuildingBrushGreenTint = ImageHelper.setOpacity(activeBuildingBrush.getObjectSprite().getImage(),0.7f);
        });
        buildingButtons.add(barracksBuildingButton);

        CustomToggleButton castleBuildingButton = new CustomToggleButton(but1Images);
        castleBuildingButton.setIcon(getImageFromResource("icon/b_castle.png"));
        castleBuildingButton.setAction((ActionEvent e) -> {
            activeBuildingBrush = new Castle();
            activeBuildingBrushRedTint = ImageHelper.tint(activeBuildingBrush.getObjectSprite().getImage(),Color.red);
            activeBuildingBrushGreenTint = ImageHelper.setOpacity(activeBuildingBrush.getObjectSprite().getImage(),0.7f);
        });
        buildingButtons.add(castleBuildingButton);
    }

    private void createMapEditorCommandButtons(){
        mapEditorButtons = new ArrayList<>();

        CustomButton t = new CustomButton(but2Images);
        t.setIcon(terrainIcon);
        t.setAction((ActionEvent e) -> {
            changeBrushButton.setEnabled(true);
            BrushType = 0;
            cp.initButtons(terrainButtons);
        });
        mapEditorButtons.add(t);

        CustomButton b = new CustomButton(but2Images);
        b.setIcon(buildingIcon);
        b.setAction((ActionEvent e) -> {
            changeBrushButton.setEnabled(false);
            BrushType = 1;
            cp.initButtons(buildingButtons);
        });
        mapEditorButtons.add(b);

        CustomButton tr = new CustomButton(but2Images);
        tr.setIcon(treeIcon);
        mapEditorButtons.add(tr);

        CustomButton undo = new CustomButton(but2Images);
        undo.setIcon(undoIcon);
        undo.setAction((ActionEvent e) -> gp.undo());
        mapEditorButtons.add(undo);

        CustomButton redo = new CustomButton(but2Images);
        redo.setIcon(redoIcon);
        redo.setAction((ActionEvent e) -> gp.redo());
        mapEditorButtons.add(redo);

        CustomButton save = new CustomButton(but2Images);
        save.setIcon(saveIcon);
        save.setAction((ActionEvent e) -> {
            try {
                new ObjectOutputStream(new FileOutputStream("objects.obj")).writeObject(buildings);
                new ObjectOutputStream(new FileOutputStream("rawMap.obj")).writeObject(rawMap);
            } catch (Exception ex) {
                System.out.println("can't save -> " + ex.getMessage());
            }
        });
        mapEditorButtons.add(save);

        CustomButton load = new CustomButton(but2Images);
        load.setIcon(loadIcon);
        load.setAction((ActionEvent e) ->{
            try {
                rawMap = (GameMap)(new ObjectInputStream(new FileInputStream("rawMap.obj")).readObject());
                map = MapHelper.compileMap(rawMap);
            } catch (Exception ex) {
                System.out.println("can't load -> " + ex.getMessage());
            }

            try {
                buildings = (GameObjectMap)(new ObjectInputStream(new FileInputStream("objects.obj")).readObject());
            } catch (Exception ex) {
                System.out.println("can't load -> " + ex.getMessage());
            }

        });
        mapEditorButtons.add(load);

    }


    public GameWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Cursor[] cur = new Cursor[1];
        cur[0] = Toolkit.getDefaultToolkit().createCustomCursor(getImageFromResource("cursor/normal.png"),new Point(0,0),"normal_cur");

        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBounds(getGraphicsConfiguration().getBounds());
        getGraphicsConfiguration().getDevice().setFullScreenWindow(this);

        setCursor(cur[0]);
        setLayout(null);

        GameMenu gm = new GameMenu(this);
        gm.setSize((int)(screenSize.getWidth()),(int)(screenSize.getHeight()));
        getLayeredPane().add(gm,new Integer(99));

        setVisible(true);
    }

    public void loadStuff(){
        BrushType = 0;
        BrushMode = 1;
        activeTerrainBrush = 0;

        activeBuildingBrush = new Barracks(); //use 0,0 for bush
        isBuildingBrushValid = false;
        activeBuildingBrushRedTint = ImageHelper.tint(activeBuildingBrush.getObjectSprite().getImage(),Color.red);
        activeBuildingBrushGreenTint = ImageHelper.setOpacity(activeBuildingBrush.getObjectSprite().getImage(),0.7f);

        //load 81 terrain tiles (v2)
        unrawSprites = new HashMap<>();
        unrawMiniSprites = new HashMap<>();
        int n = 3;
        int k; // current working digit
        int[] ps = new int[n+1];
        for (int i = 0; i < n+1; i++) {
            ps[i] = 0;
        }
        String name;
        while (ps[n] != 3) {
            name = "";
            for (int i = n; i >= 0; i--) {
                name += ps[i];
            }
            //System.out.println(name);
            unrawSprites.put(name,new Sprite("terrain2/" + name + ".png"));
            unrawMiniSprites.put(name,new Sprite("minimap/" + name + ".png"));
            k = 0;
            ps[k]++;
            while (ps[k] == 3 && k+1 < 4) {
                ps[k] = 0;
                ps[k + 1]++;
                k++;
            }
        }

        //load Knight Character
        knightSprites = new Sprite[3][8][18]; // mode - dir - frame
        Point pw = new Point(-17,40);
        Point pi = new Point(14,35);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 15; j++) {
                knightSprites[0][i][j] = new Sprite("character/knight/walk/"+i+"/"+j+".png",pw);
            }
            for (int j = 0; j < 18; j++) {
                knightSprites[2][i][j] = new Sprite("character/knight/attack1/"+i+"/"+j+".png",pw);
            }
            knightSprites[1][i][0] = new Sprite("character/knight/idle/"+i+".png",pi);
        }

        loadImages();

        //Load Sprites
        voidSprite = new Sprite("void1.png");
        highlightSprite =  new Sprite("highlight.png");
        highlightGreenSprite = new Sprite("highlight_green.png");
        highlightRedSprite = new Sprite("highlight_red.png");

        rawSprites = new Sprite[3];
        rawSprites[0] = new Sprite("raw/Green.png");
        rawSprites[1] = new Sprite("raw/Brown.png");
        rawSprites[2] = new Sprite("raw/Blue.png");



    }

    public void initMapEditor(){

        loadStuff();
        createMapEditorCommands();
        createMapEditorCommandButtons();

        getLayeredPane().removeAll();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        gp = new GamePanel(this,true);
        addKeyListener(gp);
        addMouseListener(gp);
        addMouseMotionListener(this);
        addMouseMotionListener(gp);
        gp.setSize((int)(screenSize.getWidth()),(int)(screenSize.getHeight())-200);
        getLayeredPane().add(gp,new Integer(0));


        cp = new CommandPanel(this);
        cp.setSize((int)(screenSize.getWidth())-600,200);
        cp.setLocation(350,(int)(screenSize.getHeight())-200);
        getLayeredPane().add(cp,new Integer(2));
        cp.initButtons(terrainButtons);

        //cp.changeBrushButton.addActionListener((ActionEvent e) -> switchBrushMode());
        //cp.switchRawButton.addActionListener((ActionEvent e) -> switchRawMode());

        ImagePanel vis1 = new ImagePanel(hudImages[0]);
        vis1.setSize(707,304);
        vis1.setLocation(0,(int)(screenSize.getHeight())-304);
        getLayeredPane().add(vis1,new Integer(3));


        ImagePanel vis2 = new ImagePanel(hudImages[3]);
        vis2.setSize(362,246);
        vis2.setLocation((int)(screenSize.getWidth())-362,(int)(screenSize.getHeight())-246);
        getLayeredPane().add(vis2,new Integer(4));


        //Disabled Minimap for Performance issues
        mm = new MiniMap(this);
        mm.setSize(260,250);
        mm.setLocation(19,(int)(screenSize.getHeight())-265);
        getLayeredPane().add(mm,new Integer(5));


        JPanel mapActionPanel = new JPanel();
        mapActionPanel.setLayout(null);
        //mapActionPanel.setBackground(Color.yellow);
        mapActionPanel.setOpaque(false);
        mapActionPanel.setSize(300,210);
        mapActionPanel.setLocation(288,(int)(screenSize.getHeight())-210);

        changeBrushButton = new HoverButton(butHBigImages);
        changeBrushButton.setLocation(167,27);
        changeBrushButton.setIcon(brushImg);
        //changeBrushButton.setSize(82,82);
        changeBrushButton.setAction((ActionEvent e) -> switchBrushMode());
        mapActionPanel.add(changeBrushButton);

        switchRawButton = new HoverButton(butHSmallImages);
        switchRawButton.setLocation(0,0);
        switchRawButton.setIcon(unrawImg);
        switchRawButton.setAction((ActionEvent e) -> switchRawMode());
        mapActionPanel.add(switchRawButton);

        getLayeredPane().add(mapActionPanel,new Integer(6));

        ImagePanel vis11 = new ImagePanel(hudImages[1]);
        vis11.setSize(75,82);
        vis11.setLocation(0,(int)(screenSize.getHeight())-335);
        getLayeredPane().add(vis11,new Integer(7));

        cbp = new CommandButtonsPanel(this);
        cbp.setSize(219,215);
        cbp.setLocation((int)(screenSize.getWidth())-234,(int)(screenSize.getHeight())-229);
        getLayeredPane().add(cbp,new Integer(8));
        cbp.initButtons(mapEditorButtons);

        tp = new TimePanel(this);
        tp.setLocation((int)(screenSize.getWidth() / 2)-75,0);
        getLayeredPane().add(tp,new Integer(9));

        //Scroll Timer
        scrollState = new boolean[4];
        for (int i = 0; i < 4; i++) {
            scrollState[i] = false;
        }
        // 0 Left 1 Top 2 Right 3 Down
        scrollHandler = new Timer(20, (ActionEvent e) -> {
            for (int i = 0; i < 4; i++) {
                if(scrollState[i]) gp.tryScroll(i);
            }
        });
        scrollHandler.start();

        getLayeredPane().revalidate();
        getLayeredPane().repaint();
    }


    public void initGame(){

        loadStuff();

        getLayeredPane().removeAll();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        gp = new GamePanel(this,false);
        addKeyListener(gp);
        addMouseListener(gp);
        addMouseMotionListener(this);
        addMouseMotionListener(gp);
        gp.setSize((int)(screenSize.getWidth()),(int)(screenSize.getHeight())-200);
        getLayeredPane().add(gp,new Integer(0));


        cp = new CommandPanel(this);
        cp.setSize((int)(screenSize.getWidth())-600,200);
        cp.setLocation(350,(int)(screenSize.getHeight())-200);
        getLayeredPane().add(cp,new Integer(2));
        //cp.initButtons(terrainButtons);

        //cp.changeBrushButton.addActionListener((ActionEvent e) -> switchBrushMode());
        //cp.switchRawButton.addActionListener((ActionEvent e) -> switchRawMode());

        ImagePanel vis1 = new ImagePanel(hudImages[0]);
        vis1.setSize(707,304);
        vis1.setLocation(0,(int)(screenSize.getHeight())-304);
        getLayeredPane().add(vis1,new Integer(3));


        ImagePanel vis2 = new ImagePanel(hudImages[3]);
        vis2.setSize(362,246);
        vis2.setLocation((int)(screenSize.getWidth())-362,(int)(screenSize.getHeight())-246);
        getLayeredPane().add(vis2,new Integer(4));


        //Disabled Minimap for Performance issues
        mm = new MiniMap(this);
        mm.setSize(260,250);
        mm.setLocation(19,(int)(screenSize.getHeight())-265);
        getLayeredPane().add(mm,new Integer(5));



        getLayeredPane().add(vis2,new Integer(4));


        JPanel mapActionPanel = new JPanel();
        mapActionPanel.setLayout(null);
        //mapActionPanel.setBackground(Color.yellow);
        mapActionPanel.setOpaque(false);
        mapActionPanel.setSize(300,210);
        mapActionPanel.setLocation(288,(int)(screenSize.getHeight())-220);

        shieldHole = new ImagePanel(hudImages[4]);
        shieldHole.setSize(244,258);
        shieldHole.setLocation(135,0);
        mapActionPanel.add(shieldHole);

        /*changeBrushButton = new HoverButton(butHBigImages);
        changeBrushButton.setLocation(167,27);
        changeBrushButton.setIcon(brushImg);
        //changeBrushButton.setSize(82,82);
        changeBrushButton.setAction((ActionEvent e) -> switchBrushMode());
        mapActionPanel.add(changeBrushButton);

        switchRawButton = new HoverButton(butHSmallImages);
        switchRawButton.setLocation(0,0);
        switchRawButton.setIcon(unrawImg);
        switchRawButton.setAction((ActionEvent e) -> switchRawMode());
        mapActionPanel.add(switchRawButton);*/

        getLayeredPane().add(mapActionPanel,new Integer(6));

        ImagePanel vis11 = new ImagePanel(hudImages[1]);
        vis11.setSize(75,82);
        vis11.setLocation(0,(int)(screenSize.getHeight())-335);
        getLayeredPane().add(vis11,new Integer(7));

        cbp = new CommandButtonsPanel(this);
        cbp.setSize(219,215);
        cbp.setLocation((int)(screenSize.getWidth())-234,(int)(screenSize.getHeight())-229);
        getLayeredPane().add(cbp,new Integer(8));

        tp = new TimePanel(this);
        tp.setLocation((int)(screenSize.getWidth() / 2)-75,0);
        getLayeredPane().add(tp,new Integer(9));

        //Scroll Timer
        scrollState = new boolean[4];
        for (int i = 0; i < 4; i++) {
            scrollState[i] = false;
        }
        // 0 Left 1 Top 2 Right 3 Down
        scrollHandler = new Timer(20, (ActionEvent e) -> {
            for (int i = 0; i < 4; i++) {
                if(scrollState[i]) gp.tryScroll(i);
            }
        });
        scrollHandler.start();

        getLayeredPane().revalidate();
        getLayeredPane().repaint();
    }


    private void switchBrushMode(){
        if(BrushMode == 0){
            changeBrushButton.setIcon(brushImg);
        }else{
            changeBrushButton.setIcon(fillImg);
        }
        BrushMode = (BrushMode + 1) % 2;
    }

    private void switchRawMode(){
        if(isRaw) {
            switchRawButton.setIcon(unrawImg);
        }else{
            switchRawButton.setIcon(rawImg);
        }
        isRaw = !isRaw;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        //Handle Scroll
        scrollState[0] = (e.getX()<20);
        scrollState[1] = (e.getY()<20);
        scrollState[2] = (e.getX()> getWidth()-20);
        scrollState[3] = (e.getY()> getHeight()-20);
    }
}
