import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Armin on 6/28/2016.
 */
public class CharacterMoveHandler{

    private Timer handler;
    public ArrayList<Character> characters;

    AStar as;

    GameWindow gw;

    public CharacterMoveHandler(GameWindow gw){
        this.gw = gw;
        characters = new ArrayList<>();
        as = new AStar();
        //as.solve(gw.rawMap,new ASPoint(sw.cy,sw.cx),new ASPoint(mousePos.x,mousePos.y),1);
        handler = new Timer(30,(ActionEvent e) -> {
            Iterator<Character> i = characters.iterator();
            while (i.hasNext()){
                Character c = i.next();
                if(c.path != null) {
                    if (!c.isMoving) {
                        if(c.todoMove != null){
                            c.path = null;
                            as.setActionListener(a -> {
                                if (as.getResult() != null) {
                                    c.path = as.getResult();
                                } else {
                                    System.out.println("No possible path");
                                }
                            });
                            as.solve(gw.rawMap,new ASPoint(c.cy,c.cx),new ASPoint(c.todoMove.x,c.todoMove.y),1);
                            System.out.println("A* Solving !");
                            c.todoMove = null;
                        }else {
                            c.prepareToMove();
                            c.moveCounter = 50;
                            c.isMoving = true;
                        }
                    } else {
                        c.advance();
                    }
                }else{
                    if(c.todoMove != null){
                        c.path = null;
                        as.setActionListener(a -> {
                            if (as.getResult() != null) {
                                c.path = as.getResult();
                            } else {
                                System.out.println("No possible path");
                            }
                        });
                        as.solve(gw.rawMap,new ASPoint(c.cy,c.cx),new ASPoint(c.todoMove.x,c.todoMove.y),1);
                        System.out.println("A* Solving !");
                        c.todoMove = null;
                    }
                }
            }
        });
        handler.start();
    }

}
