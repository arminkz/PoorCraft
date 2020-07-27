import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Armin on 5/16/2016.
 */

public abstract class Character extends GameObject {

    Point pixelPosition;
    int cx,cy; //logical position :(

    double maxHealth;
    double health;

    int moveCounter;
    boolean isMoving = false;

    ArrayList<Integer> path;
    Point todoMove;

    public Character(){
        super();
        selectable = true;
    }

    public void moveTo(int x,int y){
        todoMove = new Point(x,y);
        System.out.println("must move to" + x + "," + y);
    }

    public abstract void advance();
    public abstract void prepareToMove();

}

/*public abstract class Character extends GameObject {

    Timer moveTimer;
    ActionListener moveAL;
    
    Point pixelPosition;
    Point logicalPosition;
    int movementX = 0, movementY = 0;

    private GameWindow gw;



    public Character(){
        img = new Image[9];
        dir = MoveDirection.E;

        gw = parent;

        moveAL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (dir){
                    case N:
                        pixelPosition.y--;
                        movementY--;
                        break;
                    case S:
                        pixelPosition.y++;
                        movementY++;
                        break;
                    case E:
                        pixelPosition.x+=2;
                        movementX+=2;
                        break;
                    case W:
                        pixelPosition.x-=2;
                        movementX-=2;
                        break;
                    case NE:
                        pixelPosition.y--;
                        pixelPosition.x+=2;
                        break;
                }

                if(movementX >= gw.gp.TILE_WIDTH){
                    logicalPosition.x++;
                    movementX = 0;
                }

                if(movementX <= -gw.gp.TILE_WIDTH){
                    logicalPosition.x--;
                    movementX = 0;
                }

                if(movementY >= gw.gp.TILE_HEIGHT){
                    logicalPosition.y++;
                    movementY = 0;
                }

                if(movementY <= -gw.gp.TILE_HEIGHT){
                    logicalPosition.y--;
                    movementY = 0;
                }

            }
        };
        moveTimer = new Timer(50,moveAL);
        moveTimer.start();
    }

    protected Image[] img;
    
    protected MoveDirection dir;

    public Image getImage(){
        switch (dir){
            case N:
                return img[1];
            case E:
                return img[2];
            case W:
                return img[3];
            case S:
                return img[4];
            default:
                return img[1];
        }
    }

}*/
