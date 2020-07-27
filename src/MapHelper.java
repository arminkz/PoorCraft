import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Armin on 5/21/2016.
 */
public class MapHelper {

    //String Format : WEST NORTH EAST SOUTH
    public static GameMap<String> compileMap(GameMap<Integer> rawMap){
        GameMap<String> result = new GameMap(rawMap.rowsCount(),rawMap.columnsCount(),"");
        for(int i=0;i<rawMap.rowsCount();i++) {
            for (int j = 0; j < rawMap.columnsCount(); j++) {
                String res = "";

                Integer l,u,r,d;
                if(i%2 == 0) {
                    l = rawMap.getAt(j - 1, i + 1);
                    u = rawMap.getAt(j, i);
                    r = rawMap.getAt(j, i + 1);
                    d = rawMap.getAt(j, i + 2);
                }else{
                    l = rawMap.getAt(j, i + 1);
                    u = rawMap.getAt(j, i);
                    r = rawMap.getAt(j + 1, i + 1);
                    d = rawMap.getAt(j, i + 2);
                }


                if(l != null)
                    res += l.toString();
                else
                    res += "2";

                if(u != null)
                    res += u.toString();
                else
                    res += "2";

                if(r != null)
                    res += r.toString();
                else
                    res += "2";

                if(d != null)
                    res += d.toString();
                else
                    res += "2";

                result.setAt(j,i,res);
            }
        }
        return result;
    }

    public static String compileCell(GameMap<Integer> rawMap , int x , int y){
        String res = "";

        Integer l,u,r,d;

        l = rawMap.getAt(x-1, y+1);
        u = rawMap.getAt(x, y);
        r = rawMap.getAt(x, y+1);
        d = rawMap.getAt(x, y+2);


        if(l != null)
            res += l.toString();
        else
            res += "2";

        if(u != null)
            res += u.toString();
        else
            res += "2";

        if(r != null)
            res += r.toString();
        else
            res += "2";

        if(d != null)
            res += d.toString();
        else
            res += "2";

        return res;
    }

    public static ArrayList<Point> getDiamondRowCoords(Point mostFront, int rx){
        ArrayList<Point> result = new ArrayList<>();
        Point currentPoint = mostFront;
        for(int i=0;i<rx;i++) {
            result.add(currentPoint);
            int oldX = currentPoint.x;
            int oldY = currentPoint.y;
            if (currentPoint.y % 2 == 0) {
                int newX = oldX - 1;
                int newY = oldY - 1;
                currentPoint = new Point(newX, newY);
            }else{
                int newX = oldX ;
                int newY = oldY - 1;
                currentPoint = new Point(newX, newY);
            }
        }
        return result;
    }

    public static ArrayList<Point> getDiamondColCoords(Point mostFront,int ry){
        ArrayList<Point> result = new ArrayList<>();
        Point currentPoint = mostFront;
        for(int i=0;i<ry;i++) {
            result.add(currentPoint);
            int oldX = currentPoint.x;
            int oldY = currentPoint.y;
            if (currentPoint.y % 2 == 0) {
                int newX = oldX ;
                int newY = oldY - 1;
                currentPoint = new Point(newX, newY);
            }else{
                int newX = oldX + 1;
                int newY = oldY - 1;
                currentPoint = new Point(newX, newY);
            }
        }
        return result;
    }

    public static ArrayList<Point> getRectCoords(Point mostFront,int rx,int ry){
        ArrayList<Point> result = new ArrayList<>();
        ArrayList<Point> heads = getDiamondColCoords(mostFront,ry);

        for(Point h: heads){
            for(Point p : getDiamondRowCoords(h,rx)){
                result.add(p);
            }
        }
        return result;
    }

    public static ArrayList<Point> getRectCoords(Point p1,Point p2){

        Point mostFront;
        Point mostBehind;

        if(p1.y > p2.y){
            mostFront = p1;
            mostBehind = p2;
        }else{
            mostFront = p2;
            mostBehind = p1;
        }

        int resX = 0;
        int resY = 0;

        int xp1 = mostBehind.x * 2 + mostBehind.y % 2;
        int xp2 = mostFront.x * 2 + mostFront.y % 2;
        int xpd = xp2 - xp1;

        resX += xpd;

        int tempY = mostFront.y - xpd;
        int incBoth = (tempY - mostBehind.y ) / 2 + 1;
        resX += incBoth;
        resY += incBoth;
        //System.out.println("xpd = " + xpd + " x = " + resX + " y = " + resY);
        return getRectCoords(mostFront,resX,resY);
    }

}
