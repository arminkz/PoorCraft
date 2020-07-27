import java.util.ArrayList;

public class BuildingsHelper {

    //High Performance Impact ! (must fix this with better Algorithm) (it's a good idea to use Insertion Sort when user adds a new Building)
    /*public static ArrayList<Building> sort(ArrayList<Building> unsorted, int mapSizeRow, int mapSizeCol){
        ArrayList<Building> sorted = new ArrayList<>();
        for(int i=0;i<mapSizeRow;i++) {
            for (int j = 0; j < mapSizeCol; j++) {
                for(Building b : unsorted){
                    if(b.getX()==j && b.getY()==i){
                        sorted.add(b);
                    }
                }
            }
        }
        return sorted;
    }*/

}
