import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by Armin on 5/18/2016.
 */
public class MiniMap extends JLabel {

    /*private GameMap<Integer> gm;
    private Timer timer;
    private Image[] ii;
    private Color[] colors;
    private Image frameImg;
    private int mouseX, mouseY, camX, camY, dx, dy;
    private final int camSize, scale;
    private final int blocksize;*/

    //public Image renderedMiniMap;

    public MiniMap(GameWindow gw){
        //frameImg = (new ImageIcon(this.getClass().getResource("resources/images/misc/minimap_frame.png"))).getImage();
        //setOpaque(false);
        setBackground(new Color(37,50,55));

        /*this.gm = gw.rawMap;
        this.camSize = 320;

        camX = 0;
        camY = 0;
        blocksize = 80;

        scale = camSize / blocksize;

        //loadImages();

        colors = new Color[5];
        colors[2] = new Color(58, 118, 156);
        colors[0] = new Color(39, 156, 67);
        colors[1] = new Color(39, 156, 67, 1);*/

        setBackground(Color.DARK_GRAY);

        //timer = new Timer(50, this);
        //timer.start();
    }

    public void update(Image i){
        setIcon(new ImageIcon(i));
    }

    /*private void loadImages() {
        ii = new Image[3];
        ii[0] = (new ImageIcon(this.getClass().getResource("resources/images/miniMap/grass.png"))).getImage();
        ii[2] = (new ImageIcon(this.getClass().getResource("resources/images/miniMap/water.png"))).getImage();
        ii[1] = (new ImageIcon(this.getClass().getResource("resources/images/miniMap/grass.png"))).getImage();
    }*/

    @Override
    protected void paintComponent(Graphics pg) {
        super.paintComponent(pg);

        //g.drawImage(renderedMiniMap,0,0,null);

        /*BufferedImage bi = new BufferedImage((int)getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();

        for (int i = 0; i < gm.rowsCount(); i++) {
            int offsetX = (i % 2 == 1) ? scale / 2 : 0;
            for (int j = 0; j < gm.columnsCount(); j++) {

                int sx = (j * scale) + offsetX + camX;
                int sy = i * scale / 2 + camY;

                g.drawImage(ii[gm.getAt(j,i)],sx,sy,this);
            }
        }*/


        //AffineTransform tx = new AffineTransform();
        //tx.rotate(0.25*Math.PI, bi.getWidth() / 2, bi.getHeight() / 2);
        //AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
        //bi = op.filter(bi, null);

        //pg.drawImage(bi,0,0,null);
    }

    /*@Override
    public void mouseMoved(MouseEvent e) {

        mouseX = e.getX();
        mouseY = e.getY();

        dx = 0;
        dy = 0;

        if (mouseX < 20) {
            dx = -20;
        } else if (mouseX > camSize - 20) {
            dx = 20;
        }

        if (mouseY < 20) {
            dy = -20;
        } else if (mouseY > camSize - 20) {
            dy = 20;
        }

        mouseX -= blocksize;
        mouseY -= blocksize;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    void scroll() {
        camX += dx;
        camY += dy;
    }*/

}

