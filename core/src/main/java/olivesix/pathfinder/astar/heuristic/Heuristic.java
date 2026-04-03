package olivesix.pathfinder.astar.heuristic;

public interface Heuristic {
    public int getCost(int x1, int y1, int x2, int y2);
}
