package olivesix.pathfinder.astar.heuristic;

public class EuclidianHeuristic implements Heuristic {
    @Override
    public int getCost(int x1, int y1, int x2, int y2) {
        float distX = Math.abs(x1 - x2);
        float distY = Math.abs(y1 - y2);
        return (int) Math.round(10 * Math.sqrt(distX * distX + distY * distY));
    }
}
