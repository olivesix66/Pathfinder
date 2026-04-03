package olivesix.pathfinder.astar.heuristic;

public class OctileHeuristic implements  Heuristic {

    private static final int STEP_COST = 10;
    private static final int DIAGONAL_COST = (int) Math.round(Math.sqrt(2) * STEP_COST); // ~14 (V2 * 10)
    private static final int DIAGONAL_BIAS = DIAGONAL_COST - 2 * STEP_COST; // -6

    @Override
    public int getCost(int x1, int y1, int x2, int y2) {
        int distX = Math.abs(x1 - x2);
        int distY = Math.abs(y1 - y2);
        return STEP_COST * (distX + distY) + DIAGONAL_BIAS * Math.min(distX, distY);
    }

}
