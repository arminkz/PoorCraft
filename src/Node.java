public class Node {

    Node parent;
    int heuristicCost = 0; // Heuristic cost
    int finalCost = 0; // F = G + H
    int i, j;

    Node(int i, int j, int addCost) {
        this.i = i;
        this.j = j;
        heuristicCost += addCost;
    }

    @Override
    public String toString() {
        return "[" + this.i + ", " + this.j + "]";
    }

    public int direction(Node next) {

        if (next == null) return -1;

        if (next.i == i) {
            if (next.j < j) {
                return 0;
            } else if (next.j > j) {
                return 4;
            }
        }

        if (next.j == j) {

            if (next.i + 2 == i) {
                return 2;
            } else if (i + 2 == next.i) {
                return 6;
            }

            if (i % 2 == 0) {
                if (next.i < i) {
                    return 3;
                } else if (next.i > i) {
                    return 5;
                }
            } else {
                if (next.i < i) {
                    return 1;
                } else if (next.i > i) {
                    return 7;
                }
            }
        }

        if (next.j < j) {
            if (next.i < i) {
                return 1;
            } else if (next.i > i) {
                return 7;
            }
        } else if (next.j > j) {
            if (next.i < i) {
                return 3;
            } else if (next.i > i) {
                return 5;
            }
        }

        return -1;
    }
}