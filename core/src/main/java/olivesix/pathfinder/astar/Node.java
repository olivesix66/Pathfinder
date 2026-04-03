package olivesix.pathfinder.astar;

import com.badlogic.gdx.math.Vector2;

public class Node {

    private Node parent;
    private int gCost;
    private int hCost;
    private int fCost;
    private boolean solid;

    private final int col, row;
    private final Vector2 position;

    public Node(int col, int row, boolean solid){
        this.position = new Vector2(col * World.TILE_SIZE, row * World.TILE_SIZE);
        this.col = col;
        this.row = row;
        this.solid = solid;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Vector2 getPosition() {
        return position;
    }
}
