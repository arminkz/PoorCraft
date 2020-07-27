
public class ASPoint {

    public int i, j;

    public ASPoint(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public boolean equals(ASPoint p) {
        return i == p.i && j == p.j;
    }

    public ASPoint reverse () {
        return new ASPoint(j,i);
    }
}