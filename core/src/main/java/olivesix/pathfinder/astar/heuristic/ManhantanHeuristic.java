package olivesix.pathfinder.astar.heuristic;

public class ManhantanHeuristic implements Heuristic {

    public static final int STEP_SCALE = 10;

    @Override
    public int getCost(int x1, int y1, int x2, int y2) {
        int distX = Math.abs(x1 - x2);
        int distY = Math.abs(y1 - y2);
        return STEP_SCALE *  (distX + distY);
    }

}
