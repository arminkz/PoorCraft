import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Armin on 5/28/2016.
 * used for Undo / Redo Operations
 */
public class BrushSquareCommand implements Command {

    private GameMap<Integer> map;
    private ArrayList<Point> rectCoords;
    private ArrayList<Integer> initValues;
    private int finalValue;

    public BrushSquareCommand(GameMap<Integer> map,ArrayList<Point> coords,int changeTo){
        this.map = map;
        this.rectCoords = coords;
        initValues = new ArrayList<>();
        for(Point p : coords){
            initValues.add(map.getAt(p.x,p.y));
        }
        finalValue = changeTo;
    }

    public void execute(){
        for(Point p : rectCoords){
            map.setAt(p.x,p.y,finalValue);
        }
    }

    public void unexecute(){
        for (int i = 0; i < rectCoords.size() ; i++) {
            Point p = rectCoords.get(i);
            map.setAt(p.x,p.y,initValues.get(i));
        }
    }

}