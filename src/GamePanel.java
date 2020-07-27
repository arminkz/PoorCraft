import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Stack;

public class GamePanel extends JPanel implements KeyListener , MouseListener ,MouseMotionListener {

    GameWindow gw;
    boolean isMapEditorMode = false;

    Timer redrawTimer;

    Point mouseHoverTile;

    Point mouseDragStart;
    Point mouseDragEnd;
    boolean isDragging;
    String mouseDebug = "";

    //Camera Config
    static int TILE_WIDTH = 100;
    static int TILE_HEIGHT = 50;
    static int TOP = -50; //-25q
    static int LEFT = -50; //-50
    static int MINI_TILE_WIDTH = 15;
    static int MINI_TILE_HEIGHT = 15;

    //Undo/Redo Command Management
    Stack<Command> doneCommands;
    Stack<Command> undoneCommands;

    FrameAnimator fa = new FrameAnimator();
    CharacterMoveHandler cmh;

    ArrayList<Collider> colliders;

    //FPS Monitor
    FPSCounter fpsCounter;

    GameObject selectedUnit;

    //ImageFilter
    CustomImageFilter cif = new CustomImageFilter(CustomImageFilter.FilterType.Green);

    public GamePanel(GameWindow parent,boolean isMapEditorMode){
        setBackground(Color.white);

        isDragging = false;
        gw = parent;
        this.isMapEditorMode = isMapEditorMode;

        gw.BrushMode = 1;
        gw.isRaw = false;

        colliders = new ArrayList<>();

        gw.rawMap = new GameMap(gw.mapSizeX,gw.mapSizeY,0);
        gw.rawMap.suppressWarnings();
        gw.map = MapHelper.compileMap(gw.rawMap);
        gw.buildings = new GameObjectMap(gw.mapSizeX,gw.mapSizeY);
        gw.buildings.suppressWarnings();

        cmh = new CharacterMoveHandler(gw);

        //gw.rawMap.setAt(10,20,0);
        //gw.map = MapHelper.compileMap(gw.rawMap);

        /*Swordsman s = new Swordsman(gw.knightSprites,fa,gw.buildings,15,15);
        gw.buildings.addCharacter(s,10,10);
        cmh.characters.add(s);
        colliders.add(s.collider);

        Swordsman s2 = new Swordsman(gw.knightSprites,fa,gw.buildings,20,20);
        gw.buildings.addCharacter(s2,20,20);
        cmh.characters.add(s2);
        colliders.add(s2.collider);*/



        //as.solve();


        /*gw.buildings = new ArrayList<>();

        gw.buildings.add(new House(new Point(4,18)));
        gw.buildings.add(new Fence1(new Point(7,18)));
        gw.buildings.add(new Fence1(new Point(6,19)));
        gw.buildings.add(new Fence3(new Point(7,17)));
        gw.buildings.add(new Fence2(new Point(8,18)));
        gw.buildings.add(new Fence5(new Point(6,20)));
        gw.buildings.add(new Fence2(new Point(8,19)));
        gw.buildings.add(new Fence2(new Point(6,21)));
        gw.buildings.add(new Fence2(new Point(7,22)));

        gw.buildings.add(new Fence6(new Point(9,20)));

        gw.buildings.add(new Fence1(new Point(8,21)));
        gw.buildings.add(new Fence1(new Point(8,22)));
        gw.buildings.add(new Fence4(new Point(7,23)));

        gw.buildings.add(new BigTree(new Point(7,21)));

        gw.buildings.add(new Bush1(new Point(8,9)));
        gw.buildings.add(new Bush2(new Point(9,9)));
        gw.buildings.add(new Bush3(new Point(8,8)));
        gw.buildings.add(new Bush4(new Point(9,8)));

        gw.buildings.add(new MiniBush1(new Point(7,24)));

        gw.buildings.add(new MiniBush2(new Point(5,25)));

        gw.buildings = BuildingsHelper.sort(gw.buildings,gw.rawMap.rowsCount(),gw.rawMap.columnsCount());*/

        //chTest = new CharacterTest();
        //map.setAt(2,18,84);
        //map.setAt(2,19,79);
        //buildingsMap.setAt(4,18,1);
        //buildingsMap.setAt(7,14,2);
        //buildingsMap.setAt(7,15,2);
        //buildingsMap.setAt(5,18,3);
        //buildingsMap.setAt(6,17,4);
        //buildingsMap.fillRect(new Point(5,25),1,10,5);

        //buildingsMap.fillRect(new Point(10,27),new Point(11,22),5);

        //buildingsMap.setAt(6,27,6);

        doneCommands = new Stack<>();
        undoneCommands = new Stack<>();

        //redrawTimer must have at least 5ms delay (don't starve Garbage Collector ! :D)
        redrawTimer = new Timer(5, (ActionEvent e) -> {
            renderLayers();
            repaint();
        });
        redrawTimer.start();

        fpsCounter = new FPSCounter();
        fpsCounter.start();
    }

    //Game Layers
    BufferedImage layer1;
    BufferedImage layer2;
    BufferedImage layer3;
    BufferedImage layer4;
    BufferedImage miniMapLayer;

    boolean[] needsRepaint = {true,true,true};

    public static Point getTilePosition(int x,int y){
        int offsetX = (y % 2 == 1) ? TILE_WIDTH / 2 : 0;
        int sx = (x * TILE_WIDTH) + offsetX;
        int sy = y * TILE_HEIGHT / 2;
        return new Point(sx,sy);
    }

    private void renderLayers(){
        layer1 = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB); //for Terrain
        layer2 = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB); //for Terrain Selection
        layer3 = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB); //for Buildings / Characters

        layer4 = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB); //for Debug Mostly

        miniMapLayer = new BufferedImage(260,250,BufferedImage.TYPE_INT_ARGB); //for MiniMap


        Graphics2D g1 = layer1.createGraphics();
        Graphics2D g2 = layer2.createGraphics();
        Graphics2D g3 = layer3.createGraphics();
        Graphics2D g4 = layer4.createGraphics();

        Graphics2D gmm = miniMapLayer.createGraphics();

        //gmm.setColor(Color.white);
        //gmm.fillRect(0,0,260,250);

        int czi = 0;

        //Main  Drawing Loop
        for (int i = 0; i < gw.rawMap.rowsCount(); i++) {
            int offsetX = (i % 2 == 1) ? TILE_WIDTH / 2 : 0;
            int offsetXM = (i % 2 == 1) ? MINI_TILE_WIDTH / 2 : 0;
            for (int j = 0; j < gw.rawMap.columnsCount(); j++) {


                int sx = (j * TILE_WIDTH) + offsetX + LEFT ;
                int sy = i * TILE_HEIGHT / 2 + TOP;

                int sxm = (j * MINI_TILE_WIDTH) + offsetXM - 8; // 8 = 15/2
                int sym = i * MINI_TILE_HEIGHT / 2 - 8;

                //draw Buildings & Characters
                if(gw.buildings.getAt(j,i) != null && gw.buildings.getBoolAt(j,i)) {
                    GameObject go = gw.buildings.getAt(j,i);
                    if( go instanceof Building){
                        g3.drawImage(go.getObjectSprite().getImage(), sx - go.getObjectSprite().getAnchor().x, sy - go.getObjectSprite().getAnchor().y, null);
                        go.collider.rectangle = new Rectangle(sx - go.getObjectSprite().getAnchor().x,sy - go.getObjectSprite().getAnchor().y,
                                go.getObjectSprite().getImage().getWidth(null),go.getObjectSprite().getImage().getHeight(null));
                        go.collider.zindex = czi;
                        g4.drawRect(go.collider.rectangle.x,go.collider.rectangle.y,go.collider.rectangle.width,go.collider.rectangle.height);
                    }else if(go instanceof Character){
                        Character c = (Character)go;
                        g3.drawImage(c.getObjectSprite().getImage(), c.pixelPosition.x - c.getObjectSprite().getAnchor().x + LEFT , c.pixelPosition.y - c.getObjectSprite().getAnchor().y + TOP, null);
                        c.collider.rectangle = new Rectangle(c.pixelPosition.x - c.getObjectSprite().getAnchor().x + LEFT ,c.pixelPosition.y - c.getObjectSprite().getAnchor().y + TOP,
                                c.getObjectSprite().getImage().getWidth(null),c.getObjectSprite().getImage().getHeight(null));
                        c.collider.zindex = czi;
                        g4.drawRect(c.collider.rectangle.x,c.collider.rectangle.y,c.collider.rectangle.width,c.collider.rectangle.height);

                        if(c.isSelected()) {
                            //Draw Health
                            g3.setColor(new Color(100, 100, 100, 200));
                            g3.fillRect(c.pixelPosition.x + LEFT + 25, c.pixelPosition.y + TOP - 50, 50, 5);

                            double h = (c.health / c.maxHealth);
                            if (h < 0.25) {
                                g3.setColor(new Color(218, 17, 0, 240));
                            } else if (h < 0.5) {
                                g3.setColor(new Color(218, 164, 0, 240));
                            } else {
                                g3.setColor(new Color(65, 218, 0, 240));
                            }
                            g3.fillRect(c.pixelPosition.x + LEFT + 25, c.pixelPosition.y + TOP - 50, (int) (h * 50), 5);
                            g3.setColor(new Color(35, 35, 35, 240));
                            g3.drawRect(c.pixelPosition.x + LEFT + 25, c.pixelPosition.y + TOP - 50, 50, 5);


                        }
                    }
                }

                //draw Terrain
                Sprite spriteToDraw;
                if (gw.isRaw) {
                    spriteToDraw = gw.rawSprites[gw.rawMap.getAt(j, i)];
                } else {
                    sy += TILE_HEIGHT / 2;
                    spriteToDraw = gw.unrawSprites.get(gw.map.getAt(j, i));
                }
                g1.drawImage(spriteToDraw.getImage(), sx - spriteToDraw.getAnchor().x, sy - spriteToDraw.getAnchor().y, null);
                gmm.drawImage(gw.unrawMiniSprites.get(gw.map.getAt(j, i)).getImage(),sxm,sym,null);
                //g1.drawString(j + "," + i, sx + 35, sy + 30);

                //draw Selection Building Part
                if(mouseHoverTile != null && j==mouseHoverTile.x && i==mouseHoverTile.y && gw.BrushType==1){
                    int bof = (mouseHoverTile.y % 2 == 1) ? TILE_WIDTH / 2 : 0;
                    int bsx = (mouseHoverTile.x * TILE_WIDTH) + bof+ LEFT;
                    int bsy = mouseHoverTile.y * TILE_HEIGHT / 2 + TOP;
                    Building abb = gw.activeBuildingBrush;
                    if(gw.isBuildingBrushValid){
                        g3.drawImage(gw.activeBuildingBrushGreenTint,bsx - abb.getObjectSprite().getAnchor().x,bsy - abb.getObjectSprite().getAnchor().y ,null);
                    }else{
                        g3.drawImage(gw.activeBuildingBrushRedTint,bsx - abb.getObjectSprite().getAnchor().x,bsy - abb.getObjectSprite().getAnchor().y ,null);
                    }
                }

                czi++;
            }
        }

        if(isMapEditorMode) {
            //Draw Terrain Selection
            if (!isDragging) {
                //Highlight the Mouse Hover Tile
                if (mouseHoverTile != null) {
                    if (gw.BrushType == 0) { // Terrain Brush

                        int offsetX = (mouseHoverTile.y % 2 == 1) ? TILE_WIDTH / 2 : 0;
                        int sx = (mouseHoverTile.x * TILE_WIDTH) + offsetX + LEFT;
                        int sy = mouseHoverTile.y * TILE_HEIGHT / 2 + TOP;
                        g2.drawImage(gw.highlightSprite.getImage(), sx, sy, null);

                    } else if (gw.BrushType == 1) { //Building Brush

                        Building abb = gw.activeBuildingBrush;
                        ArrayList<Point> ps = MapHelper.getRectCoords(mouseHoverTile, abb.getWidth(), abb.getHeight());
                        boolean ibbv = true;
                        for (Point p : ps) {
                            int offsetX = (p.y % 2 == 1) ? TILE_WIDTH / 2 : 0;
                            int sx = (p.x * TILE_WIDTH) + offsetX + LEFT;
                            int sy = p.y * TILE_HEIGHT / 2 + TOP;
                            if (gw.rawMap.getAt(p.x, p.y) != null && abb.isValidTerrain(gw.rawMap.getAt(p.x, p.y)) && gw.buildings.getAt(p.x, p.y) == null) {
                                g2.drawImage(gw.highlightGreenSprite.getImage(), sx, sy, null);
                            } else {
                                g2.drawImage(gw.highlightRedSprite.getImage(), sx, sy, null);
                                ibbv = false;
                            }
                        }
                        gw.isBuildingBrushValid = ibbv;

                    }
                }
            } else {
                //Highlight Mouse Dragging Area
                if (mouseDragStart != null && mouseDragEnd != null) {
                    ArrayList<Point> ps = MapHelper.getRectCoords(mouseDragEnd, mouseDragStart);
                    for (Point p : ps) {
                        int offsetX = (p.y % 2 == 1) ? TILE_WIDTH / 2 : 0;
                        int sx = (p.x * TILE_WIDTH) + offsetX + LEFT;
                        int sy = p.y * TILE_HEIGHT / 2 + TOP;
                        g2.drawImage(gw.highlightSprite.getImage(), sx, sy, null);
                    }
                }
            }
        }else{
            if (mouseDragStart != null && mouseDragEnd != null) {
                g4.setColor(Color.green);
                g4.drawRect(Math.min(mouseDragStart.x,mouseDragEnd.x),Math.min(mouseDragStart.y,mouseDragEnd.y),Math.abs(mouseDragEnd.x - mouseDragStart.x),Math.abs(mouseDragEnd.y - mouseDragStart.y));
            }
        }

        g1.dispose();
        g2.dispose();
        g3.dispose();
        g4.dispose();
        gmm.dispose();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draw Layers
        g.drawImage(layer1,0,0,null);
        g.drawImage(layer2,0,0,null);
        g.drawImage(layer3,0,0,null);
        g.drawImage(layer4,0,0,null);

        if(miniMapLayer != null) {
            gw.mm.update(miniMapLayer);
        }

        //Draw Buildings Map Using ZigZag Method
        /*for(Building b : gw.buildings){
            Point p = b.getCoords();
            int offsetX = (p.y % 2 == 1) ? TILE_WIDTH / 2 : 0;
            int sx = (p.x * TILE_WIDTH) + offsetX + LEFT - b.getBuildingSprite().getAnchor().x;
            int sy = p.y * TILE_HEIGHT / 2 + TOP - b.getBuildingSprite().getAnchor().y;
            g.drawImage(b.getBuildingSprite().getImage(), sx, sy, null);
        }*/



        /*for(int i=0;i<buildingsMap.rowsCount();i++) {
            int offsetX = (i % 2 == 1) ? TILE_WIDTH / 2 : 0;
            for (int j = 0; j < buildingsMap.columnsCount(); j++) {
                int mapSegment = buildingsMap.getAt(j, i);
                if(mapSegment != 0) {
                    Sprite spriteToDraw = fgSprites[mapSegment];
                    int sx = (j * TILE_WIDTH) + offsetX + LEFT;
                    int sy = i * TILE_HEIGHT / 2 + TOP;
                    g.drawImage(spriteToDraw.getImage(), sx - spriteToDraw.getAnchor().x, sy - spriteToDraw.getAnchor().y, null);
                }
            }
        }*/

        //Draw Map Using Diamond Method
        /*for (int y = 0; y < 5; y++) {
            for (int x = 4; x >=0; x--) {
                int sx = (x*TILE_WIDTH/2)+(y*TILE_WIDTH/2) + LEFT;
                int sy = (y*TILE_HEIGHT/2)-(x*TILE_HEIGHT/2) + TOP;

                g.drawImage(tileImage,sx,sy,null);
                g.drawString(x+","+y,sx+35,sy+30);

            }
        }*/

        //g.drawImage(chTest.getImage(), chTest.getX(), chTest.getY(), null);



        g.drawString(mouseDebug,20,60);
        if(mouseDragEnd != null) {
            g.drawString("MOUSE DRAG END : " + mouseDragEnd.toString(), 20, 80);
        }

        //Finished Drawing -> Display FPS
        g.drawString("FPS : " + fpsCounter.getFPS(),20,40);
        fpsCounter.interrupt();

        g.setColor(Color.RED);
        g.drawString("PoorCraft 3 Game : Age of Kharkari v0.1",20,20);
    }

    public void tryScroll(int dir){
        switch(dir){
            case 0 :
                if(LEFT>=-50){
                    LEFT= -50;
                }else {
                    LEFT += 15;
                }
                break;
            case 1:
                if(TOP>=-50){
                    TOP= -50;
                }else {
                    TOP += 15;
                }
                break;
            case 2 :
                if(LEFT<=getWidth()-gw.map.columnsCount()*100){
                    LEFT=(getWidth()-gw.map.columnsCount()*100);
                }else {
                    LEFT -= 15;
                }
                break;
            case 3:
                if(TOP<=getHeight()-gw.map.rowsCount()*25){
                    TOP= (getHeight()-gw.map.rowsCount()*25);
                }else {
                    TOP -= 15;
                }
                break;
        }
    }

    public void undo(){
        if(!doneCommands.isEmpty()) {
            Command c = doneCommands.pop();
            if(c instanceof BrushCommand) {
                BrushCommand bc = ((BrushCommand)c);
                System.out.println("must undo " + bc.coords.toString() + " from " + bc.finalValue  + " to " + bc.initValue);
            }
            c.unexecute();
            undoneCommands.push(c);
            gw.map = MapHelper.compileMap(gw.rawMap);
        }
    }

    public void redo(){
        if(!undoneCommands.isEmpty()) {
            Command c = undoneCommands.pop();
            c.execute();
            doneCommands.push(c);
            gw.map = MapHelper.compileMap(gw.rawMap);
        }
    }

    public void brushAt(Point pt){
        BrushCommand bc = new BrushCommand(gw.rawMap, pt, gw.activeTerrainBrush);
        bc.execute();
        doneCommands.push(bc);
        undoneCommands = new Stack<>();
        //gw.rawMap.setAt(mouseHoverTile.x,mouseHoverTile.y,gw.activeTerrainBrush);
        gw.map = MapHelper.compileMap(gw.rawMap);
    }

    public void buildingAt(Point pt){
        if(gw.isBuildingBrushValid) {
            BuildingCommand bc = new BuildingCommand(gw.buildings, pt, gw.activeBuildingBrush);
            bc.execute();
            doneCommands.push(bc);
            undoneCommands = new Stack<>();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key Press : " + e.getKeyCode());
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP: //UP
                tryScroll(1);
                break;
            case KeyEvent.VK_DOWN: //DOWN
                tryScroll(3);
                break;
            case KeyEvent.VK_RIGHT: //RIGHT
                tryScroll(2);
                break;
            case KeyEvent.VK_LEFT: //LEFT
                tryScroll(0);
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isMapEditorMode) {
            if (mouseHoverTile != null) {
                mouseDragStart = new Point(mouseHoverTile.x, mouseHoverTile.y);
                isDragging = true;
            }
        }else{
            mouseDragStart = new Point(e.getX(), e.getY());
            isDragging = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isMapEditorMode) {
            if (mouseDragEnd != null && mouseDragStart != null) {
                if (mouseDragStart.x == mouseDragEnd.x && mouseDragStart.y == mouseDragEnd.y) {
                    if (gw.BrushType == 0) {
                        brushAt(mouseHoverTile);
                    } else if (gw.BrushType == 1) {
                        //JOptionPane.showMessageDialog(this,"M!");
                        buildingAt(mouseHoverTile);
                    }
                } else {
                    if (gw.BrushType == 0) {
                        BrushSquareCommand bsc = new BrushSquareCommand(gw.rawMap, MapHelper.getRectCoords(mouseDragEnd, mouseDragStart), gw.activeTerrainBrush);
                        bsc.execute();
                        doneCommands.push(bsc);
                        undoneCommands = new Stack<>();
                        //gw.rawMap.fillRect(mouseDragEnd,mouseDragStart,gw.activeTerrainBrush);
                        gw.map = MapHelper.compileMap(gw.rawMap);
                    }
                }
                mouseHoverTile = mouseDragEnd;
            } else if (mouseDragStart != null) {
                if (mouseDragStart.x == mouseHoverTile.x && mouseDragStart.y == mouseHoverTile.y) {
                    if (gw.BrushType == 0) {
                        brushAt(mouseHoverTile);
                    } else if (gw.BrushType == 1) {
                        //JOptionPane.showMessageDialog(this,"M!");
                        buildingAt(mouseHoverTile);
                    }
                }
            }
            isDragging = false;
            mouseDragStart = null;
            mouseDragEnd = null;
        }else{
            if(e.getButton() == MouseEvent.BUTTON1) {
                Collider selectedCollider = null;
                for (Collider c : colliders) {
                    if (c.rectangle.contains(e.getX(), e.getY())) {
                        if ((selectedCollider == null) || (c.zindex > selectedCollider.zindex)) {
                            selectedCollider = c;
                        }
                    }
                    c.gameObject.deselect();
                }
                if (selectedCollider != null) {
                    selectedCollider.gameObject.select();
                    selectedUnit = selectedCollider.gameObject;
                }
            }else if(e.getButton() == MouseEvent.BUTTON3){
                Point mousePos = getTileAt(new Point(e.getX(),e.getY()));
                if(selectedUnit instanceof Swordsman && mousePos != null){
                    Swordsman sw = ((Swordsman)selectedUnit);
                    sw.moveTo(mousePos.x,mousePos.y);
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(isMapEditorMode) {
            if (gw.BrushType == 0) {
                switch (gw.BrushMode) {
                    case 0:
                        for (int i = 0; i < gw.map.rowsCount(); i++) {
                            int offsetX = (i % 2 == 1) ? TILE_WIDTH / 2 : 0;
                            for (int j = 0; j < gw.map.columnsCount(); j++) {
                                //Center of Diamond Tile
                                int ox = (j * TILE_WIDTH) + offsetX + TILE_WIDTH / 2 + LEFT;
                                int oy = i * TILE_HEIGHT / 2 + TILE_HEIGHT / 2 + TOP;

                                //ONLY WORKS WITH 2:1 Ratio Tiles ! (stupid i'll fix it later)
                                if ((Math.abs(e.getX() - ox) + 2 * Math.abs(e.getY() - oy)) < (TILE_WIDTH / 2)) {
                                    //mouseDebug = "MOUSE IS ON : " + i + " , " + j;
                                    mouseDragEnd = new Point(j, i);
                                    return;
                                }
                            }
                        }
                        mouseDragEnd = null;
                        break;
                    case 1:
                        for (int i = 0; i < gw.map.rowsCount(); i++) {
                            int offsetX = (i % 2 == 1) ? TILE_WIDTH / 2 : 0;
                            for (int j = 0; j < gw.map.columnsCount(); j++) {
                                //Center of Diamond Tile
                                int ox = (j * TILE_WIDTH) + offsetX + TILE_WIDTH / 2 + LEFT;
                                int oy = i * TILE_HEIGHT / 2 + TILE_HEIGHT / 2 + TOP;

                                //ONLY WORKS WITH 2:1 Ratio Tiles ! (stupid i'll fix it later)
                                if ((Math.abs(e.getX() - ox) + 2 * Math.abs(e.getY() - oy)) < (TILE_WIDTH / 2)) {
                                    BrushCommand bc = new BrushCommand(gw.rawMap, new Point(j, i), gw.activeTerrainBrush);
                                    bc.execute();
                                    doneCommands.push(bc);
                                    undoneCommands = new Stack<>();
                                    //gw.rawMap.setAt(j,i,gw.activeTerrainBrush);
                                    gw.map = MapHelper.compileMap(gw.rawMap);
                                    return;
                                }
                            }
                        }
                        break;
                }
            }
        }else{
            mouseDragEnd = new Point(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(isMapEditorMode) {
            //Handle Mouse Hover
            mouseHoverTile = getTileAt(new Point(e.getX(),e.getY()));
        }
    }

    public Point getTileAt(Point mouse){
        Point tile;
        for (int i = 0; i < gw.map.rowsCount(); i++) {
            int offsetX = (i % 2 == 1) ? TILE_WIDTH / 2 : 0;
            for (int j = 0; j < gw.map.columnsCount(); j++) {
                //Center of Diamond Tile
                int ox = (j * TILE_WIDTH) + offsetX + TILE_WIDTH / 2 + LEFT;
                int oy = i * TILE_HEIGHT / 2 + TILE_HEIGHT / 2 + TOP;

                //ONLY WORKS WITH 2:1 Ratio Tiles ! (stupid i'll fix it later)
                if ((Math.abs(mouse.getX() - ox) + 2 * Math.abs(mouse.getY() - oy)) < (TILE_WIDTH / 2)) {
                    return new Point(j, i);
                }
            }
        }
        return null;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ///
    }

    @Override
    public void keyTyped(KeyEvent e) {
        ///
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ///
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ///
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
