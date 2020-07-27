import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class GameObjectMap implements Serializable {

    private ArrayList<ArrayList<GameObject>> map;
    private ArrayList<ArrayList<Boolean>> isDraw;
    private int rows;
    private int cols;
    private boolean isSuppressed = false;

    public GameObjectMap(int rows,int cols){
        this.rows = rows;
        this.cols = cols;
        map = new ArrayList<>();
        isDraw = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<GameObject> row = new ArrayList<>();
            ArrayList<Boolean> idrow = new ArrayList<>();
            for (int j = 0; j < cols ; j++) {
                row.add(null);
                idrow.add(false);
            }
            map.add(row);
            isDraw.add(idrow);
        }
    }

    /*public GameObjectMap(ArrayList<ArrayList<Building>> map){
        this.rows = map.size();
        this.cols = map.get(0).size();
        this.map = map;
    }*/

    public GameObject getAt(int x, int y){
        if(x>=0 && x<cols){
            if(y>=0 && y<rows){
                return map.get(y).get(x);
            }else{
                if(!isSuppressed)
                    System.err.println("warning at BuildingMap.getAt(): y index out of bounds "+y);
            }
        }else{
            if(!isSuppressed)
                System.err.println("warning at BuildingMap.getAt(): x index out of bounds "+x);
        }
        return null;
    }

    public Boolean getBoolAt(int x,int y){
        if(x>=0 && x<cols){
            if(y>=0 && y<rows){
                return isDraw.get(y).get(x);
            }else{
                if(!isSuppressed)
                    System.err.println("warning at BuildingMap.getBoolAt(): y index out of bounds "+y);
            }
        }else{
            if(!isSuppressed)
                System.err.println("warning at BuildingMap.getBoolAt(): x index out of bounds "+x);
        }
        return null;
    }

    private void setAt(int x,int y,GameObject value){
        if(x>=0 && x<cols){
            if(y>=0 && y<rows){
                map.get(y).set(x,value);
            }else{
                if(!isSuppressed)
                    System.err.println("warning at BuildingMap.setAt(): y index out of bounds "+y);
            }
        }else{
            if(!isSuppressed)
                System.err.println("warning at BuildingMap.setAt(): x index out of bounds "+x);
        }
    }

    private void setBoolAt(int x,int y,Boolean b){
        if(x>=0 && x<cols){
            if(y>=0 && y<rows){
                isDraw.get(y).set(x,b);
            }else{
                if(!isSuppressed)
                    System.err.println("warning at BuildingMap.setAt(): y index out of bounds "+y);
            }
        }else{
            if(!isSuppressed)
                System.err.println("warning at BuildingMap.setAt(): x index out of bounds "+x);
        }
    }

    public void addBuilding(Building b,int x,int y){
        ArrayList<Point> pts = MapHelper.getRectCoords(new Point(x,y),b.getWidth(),b.getHeight());
        for(Point pt : pts){
            setAt(pt.x,pt.y,b);
        }
        setBoolAt(x,y,true);
    }

    public void removeBuilding(Building b,int x,int y){
        ArrayList<Point> pts = MapHelper.getRectCoords(new Point(x,y),b.getWidth(),b.getHeight());
        for(Point pt : pts){
            setAt(pt.x,pt.y,null);
        }
        setBoolAt(x,y,false);
    }

    public void addCharacter(Character c,int x,int y){
        setAt(x,y,c);
        setBoolAt(x,y,true);
    }

    public void removeCharacter(Character c,int x,int y){
        setAt(x,y,null);
        setBoolAt(x,y,false);
    }

    public void addRow(){
        rows++;
        ArrayList<GameObject> row = new ArrayList<>();
        ArrayList<Boolean> idrow = new ArrayList<>();
        for (int j = 0; j < cols ; j++) {
            row.add(null);
            idrow.add(false);
        }
        map.add(row);
        isDraw.add(idrow);
    }

    public void addColumn(){
        cols++;
        for(ArrayList<GameObject> row : map){
            row.add(null);
        }
        for(ArrayList<Boolean> idrow : isDraw){
            idrow.add(false);
        }
    }

    public int rowsCount(){
        return rows;
    }

    public int columnsCount(){
        return cols;
    }

    public void suppressWarnings(){
        isSuppressed = true;
    }



    /*public void fillRow(Point mostFront,int rx,T value){
        ArrayList<Point> pts = GameMap.getDiamondRowCoords(mostFront,rx);
        for(Point pt : pts){
            setAt(pt.x,pt.y,value);
        }
    }

    public void fillCol(Point mostFront,int ry,T value){
        ArrayList<Point> pts = GameMap.getDiamondColCoords(mostFront,ry);
        for(Point pt : pts){
            setAt(pt.x,pt.y,value);
        }
    }

    public void fillRect(Point mostFront,int rx,int ry,T value){
        ArrayList<Point> pts = GameMap.getRectCoords(mostFront,rx,ry);
        for(Point pt : pts){
            setAt(pt.x,pt.y,value);
        }
    }

    public void fillRect(Point mostFront,Point mostBehind,T value){
        ArrayList<Point> pts = GameMap.getRectCoords(mostFront,mostBehind);
        for(Point pt : pts){
            setAt(pt.x,pt.y,value);
        }
    }

    public static void saveToFile(File f,GameMap<Integer> map){

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(f));
            for (int i = 0; i < map.rowsCount() ; i++) {
                for (int j = 0; j < map.columnsCount() ; j++) {
                    fw.write(map.getAt(j,i).toString());
                    if(j != map.columnsCount() - 1)
                        fw.write(" ");
                }
                if(i != map.rowsCount() - 1)
                    fw.write("\n");
            }
            fw.flush();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static GameMap loadFromFile(File f){

        Scanner scn = null;
        try {
            scn = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<ArrayList<Integer>> map = new ArrayList<>();
        while(scn.hasNextLine()){
            String ln = scn.nextLine();
            ArrayList<Integer> row = new ArrayList<>();
            for(String m : ln.split(" ")){
                //System.out.println(m);
                row.add(Integer.parseInt(m));
            }
            map.add(row);
        }
        GameMap<Integer> result = new GameMap<Integer>(map);
        return result;
    }*/

}
