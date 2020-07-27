import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar implements Runnable {

    private Thread thread;
    private Node[][] grid;
    private GameMap<Integer> map;

    private ActionListener al;
    private PriorityQueue<Node> open;
    private Node current;
    private ASPoint start, end;
    private int max;
    private boolean closed[][];
    private final int vhCost=10, dCost=14;

    private ArrayList<Integer> result;
    private boolean isRunning;

    public void updateCost(Node current, Node t, int cost) {
        if (t == null || closed[t.i][t.j]) return;
        int finalCost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || finalCost < t.finalCost) {
            t.finalCost = finalCost;
            t.parent = current;
            if (!inOpen) open.add(t);
        }
    }

    public void solve(GameMap<Integer> map, ASPoint start, ASPoint end, int max) {

        this.map = map;
        this.start = start.reverse();
        this.end = end.reverse();
        this.max = max;

        grid = new Node[map.rowsCount()][map.columnsCount()];
        closed = new boolean[map.rowsCount()][map.columnsCount()];
        result = new ArrayList<>();

        open = new PriorityQueue<>((Object o1, Object o2) -> {
            Node c1 = (Node) o1;
            Node c2 = (Node) o2;

            return c1.finalCost < c2.finalCost ? -1 :
                    c1.finalCost > c2.finalCost ? 1 : 0;
        });

        for (int i = 0; i < map.rowsCount(); i++) {
            for (int j = 0; j < map.columnsCount(); j++) {
                if (map.getAt(j, i) < max) {
                    grid[i][j] = new Node(i, j, map.getAt(j, i));
                    grid[i][j].heuristicCost = Math.abs(i - end.i) + Math.abs(j - end.j);
                } else {
                    grid[i][j] = null;
                }
            }
        }

        grid[start.i][start.j].finalCost = 0;
        open.add(grid[start.i][start.j]);
        isRunning = true;

        thread = new Thread(this);
        thread.start();
    }

    public void run() {

        while (isRunning) {

            current = open.poll();

            if (current == null) {
                result = null;
                if (al != null) al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
                isRunning = false;
                break;
            }

            closed[current.i][current.j] = true;

            if (current.equals(grid[end.i][end.j])) {
                if (closed[end.i][end.j]) {

                    Node current = grid[end.i][end.j];

                    while (current.parent != null) {
                        if (current.parent.direction(current) != -1) result.add(0, current.parent.direction(current));
                        System.out.print(current.toString() + " -> ");
                        current = current.parent;
                    }

                    if (al != null) al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
                    isRunning = false;
                    break;
                } else {
                    result = null;
                    if (al != null) al.actionPerformed(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 1, ""));
                    isRunning = false;
                    break;
                }
            }

            Node t;

            if (current.i % 2 == 0) {

                if (current.i - 2 >= 0) {
                    t = grid[current.i - 2][current.j]; // u
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.i - 1 >= 0) {

                    if (current.j - 1 >= 0) {
                        t = grid[current.i - 1][current.j - 1]; // ul
                        updateCost(current, t, current.finalCost + dCost);
                    }

                    t = grid[current.i - 1][current.j]; // ur
                    updateCost(current, t, current.finalCost + dCost);
                }

                if (current.j - 1 >= 0) { // left
                    t = grid[current.i][current.j - 1];
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.j + 1 < grid[0].length) { // r
                    t = grid[current.i][current.j + 1];
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.i + 2 < grid.length) {

                    t = grid[current.i + 2][current.j]; // d
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.i + 1 < grid.length) {
                    if (current.j - 1 >= 0) {
                        t = grid[current.i + 1][current.j - 1]; // dl
                        updateCost(current, t, current.finalCost + dCost);
                    }

                    t = grid[current.i + 1][current.j]; // dr
                    updateCost(current, t, current.finalCost + dCost);
                }
            } else {

                if (current.i - 2 >= 0) {
                    t = grid[current.i - 2][current.j]; // u
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.i - 1 >= 0) {

                    t = grid[current.i - 1][current.j]; // ul
                    updateCost(current, t, current.finalCost + dCost);
                }

                if (current.i - 1 > 0) {
                    if (current.j + 1 < grid[0].length) {
                        t = grid[current.i - 1][current.j + 1]; // ur
                        updateCost(current, t, current.finalCost + dCost);
                    }
                }

                if (current.j - 1 >= 0) { // left
                    t = grid[current.i][current.j - 1];
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.j + 1 < grid[0].length) { // r
                    t = grid[current.i][current.j + 1];
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.i + 2 < grid.length) {
                    t = grid[current.i + 2][current.j]; // d
                    updateCost(current, t, current.finalCost + vhCost);
                }

                if (current.i + 1 < grid.length) {
                    if (current.j + 1 < grid[0].length) {
                        t = grid[current.i + 1][current.j + 1]; // dr
                        updateCost(current, t, current.finalCost + dCost);
                    }

                    t = grid[current.i + 1][current.j]; // dl
                    updateCost(current, t, current.finalCost + dCost);
                }
            }
        }
    }

    public void setActionListener(ActionListener al) {
        this.al = al;
    }

    public ArrayList<Integer> getResult() {
        return result;
    }

    public void stop() {
        isRunning = false;
    }
}