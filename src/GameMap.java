import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Created by Armin on 5/13/2016.
 * converted to Generic Type
 * added addRow / addColumn
 * added Save / Load
 */
public class GameMap<T> implements Serializable{

    private ArrayList<ArrayList<T>> map;
    private int rows;
    private int cols;
    private boolean isSuppressed = false;

    public GameMap(int rows,int cols,T defaultValue){
        this.rows = rows;
        this.cols = cols;
        map = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<T> row = new ArrayList<>();
            for (int j = 0; j < cols ; j++) {
                row.add(defaultValue);
            }
            map.add(row);
        }
    }

    public GameMap(ArrayList<ArrayList<T>> map){
        this.rows = map.size();
        this.cols = map.get(0).size();
        this.map = map;
    }

    public T getAt(int x,int y){
        if(x>=0 && x<cols){
            if(y>=0 && y<rows){
                return map.get(y).get(x);
            }else{
                if(!isSuppressed)
                    System.err.println("warning at GameMap.getAt(): y index out of bounds "+y);
            }
        }else{
            if(!isSuppressed)
                System.err.println("warning at GameMap.getAt(): x index out of bounds "+x);
        }
        return null;
    }

    public void setAt(int x,int y,T value){
        if(x>=0 && x<cols){
            if(y>=0 && y<rows){
                map.get(y).set(x,value);
            }else{
                if(!isSuppressed)
                    System.err.println("warning at GameMap.setAt(): y index out of bounds "+y);
            }
        }else{
            if(!isSuppressed)
                System.err.println("warning at GameMap.setAt(): x index out of bounds "+x);
        }
    }

    public void addRow(T defaultValue){
        rows++;
        ArrayList<T> row = new ArrayList<>();
        for (int j = 0; j < cols ; j++) {
            row.add(defaultValue);
        }
        map.add(row);
    }

    public void addColumn(T defaultValue){
        cols++;
        for(ArrayList<T> row : map){
            row.add(defaultValue);
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
    }

}
