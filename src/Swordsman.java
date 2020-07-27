import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Armin on 6/28/2016.
 */
public class Swordsman extends Character {

    Sprite[][][] sprites;
    int moveDir = 0;
    int dcx ,dcy;              //diff in logical pos
    double dpx = -2 , dpy = 0; //diff in pixel pos
    double pixelX,pixelY;
    private static final double rd = 0.5;

    boolean isAttacking;

    FrameAnimator fa;
    GameObjectMap gomap;


    public Swordsman(Sprite[][][] sprites,FrameAnimator fa,GameObjectMap gomap,int cx,int cy){
        this.sprites = sprites;
        this.fa = fa;
        this.gomap = gomap;
        this.cx = cx;
        this.cy = cy;
        pixelPosition = GamePanel.getTilePosition(cx,cy);
        pixelX = pixelPosition.x;
        pixelY = pixelPosition.y;

        maxHealth=1000;
        health=200;
        /*path = new ArrayList<>();
        path.add(1);
        path.add(1);
        path.add(1);*/
    }

    @Override
    public Sprite getObjectSprite() {
        if(path != null) {
            return sprites[0][moveDir][fa.getFrameNo(15, 3)];
        }else{
            return sprites[1][moveDir][0];
        }
    }

    @Override
    public void advance(){
        pixelX += dpx;
        pixelY += dpy;
        updatePixelPosition();
        moveCounter--;
        if(moveCounter==0){
            if(path.size() == 0) {
                path = null;
            }
            isMoving = false;
            updateMapPosition();
            pixelPosition = GamePanel.getTilePosition(cx,cy);
            pixelX = pixelPosition.x;
            pixelY = pixelPosition.y;
        }
    }

    public void updatePixelPosition(){
        pixelPosition.x = (int)pixelX;
        pixelPosition.y = (int)pixelY;
    }

    public void updateMapPosition(){
        gomap.removeCharacter(this,cx,cy);
        cx += dcx;
        cy += dcy;
        gomap.addCharacter(this,cx,cy);
    }

    public void prepareToMove(){
        Integer thisMove = path.get(0);
        path.remove(0);
        moveDir = thisMove;
        switch (thisMove) {
            case 0:
                dpx = -2;
                dpy = 0;
                break;
            case 1:
                dpx = -2 * rd;
                dpy = -1 * rd;
                break;
            case 2:
                dpx = 0;
                dpy = -1;
                break;
            case 3:
                dpx = 2 * rd;
                dpy = -1 * rd;
                break;
            case 4:
                dpx = 2;
                dpy = 0;
                break;
            case 5:
                dpx = 2 * rd;
                dpy = 1 * rd;
                break;
            case 6:
                dpx = 0;
                dpy = 1;
                break;
            case 7:
                dpx = -2 * rd;
                dpy = 1 * rd;
                break;
        }

        if (cy % 2 == 0) {
            switch (thisMove) {
                case 0:
                    dcx = -1;
                    dcy = 0;
                    break;
                case 1:
                    dcx = -1;
                    dcy = -1;
                    break;
                case 2:
                    dcx = 0;
                    dcy = -2;
                    break;
                case 3:
                    dcx = 0;
                    dcy = -1;
                    break;
                case 4:
                    dcx = 1;
                    dcy = 0;
                    break;
                case 5:
                    dcx = 0;
                    dcy = 1;
                    break;
                case 6:
                    dcx = 0;
                    dcy = 2;
                    break;
                case 7:
                    dcx = -1;
                    dcy = 1;
                    break;
            }
        } else {
            switch (thisMove) {
                case 0:
                    dcx = -1;
                    dcy = 0;
                    break;
                case 1:
                    dcx = 0;
                    dcy = -1;
                    break;
                case 2:
                    dcx = 0;
                    dcy = -2;
                    break;
                case 3:
                    dcx = 1;
                    dcy = -1;
                    break;
                case 4:
                    dcx = 1;
                    dcy = 0;
                    break;
                case 5:
                    dcx = 1;
                    dcy = 1;
                    break;
                case 6:
                    dcx = 0;
                    dcy = 2;
                    break;
                case 7:
                    dcx = 0;
                    dcy = 1;
                    break;
            }
        }
    }

}
