import java.awt.*;

/**
 * Created by Armin on 5/28/2016.
 * used for Undo / Redo Operations
 */
public class BrushCommand implements Command  {

    private GameMap<Integer> map;
    public Point coords;
    public int initValue;
    public int finalValue;

    public BrushCommand(GameMap<Integer> map,Point coords,int changeTo){
        this.map = map;
        this.coords = coords;
        initValue = map.getAt(coords.x,coords.y);
        finalValue = changeTo;
    }

    public void execute(){
        map.setAt(coords.x,coords.y,finalValue);
    }

    public void unexecute(){
        map.setAt(coords.x,coords.y,initValue);
    }

}
