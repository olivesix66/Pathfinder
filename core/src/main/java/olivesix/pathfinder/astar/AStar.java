package olivesix.pathfinder.astar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import olivesix.pathfinder.astar.heuristic.Heuristic;
import olivesix.pathfinder.astar.heuristic.OctileHeuristic;

import java.util.*;

public class AStar {

    private final static int MAX_COST = 99999;

    private final World  world;
    private final Heuristic heuristic;

    private final Queue<Node> openedList;
    private final Set<Node> closedList;
    private final Stack<Node> path;

    private Node initNode;
    private Node goalNode;
    private Node currNode;

    public AStar(World world) {
        this.world = world;
        this.heuristic = new OctileHeuristic();

        this.openedList = new PriorityQueue<>(this::sortNodes);
        this.closedList = new HashSet<>();
        this.path = new Stack<>();
    }

    private int sortNodes(Node n1, Node n2) {
        if(n1.getFCost() != n2.getFCost()) return Integer.compare(n1.getFCost(), n2.getFCost());
        return Integer.compare(n1.getHCost(), n2.getHCost());
    }

    private void reset() {
        this.openedList.clear();
        this.closedList.clear();
        this.path.clear();
        this.initNode = null;
        this.goalNode = null;
        this.currNode = null;
    }

    public void setInit(int initCol, int initRow) {
        this.reset();
        this.initNode = world.getNode(initCol, initRow);
    }

    private void setTarget(int goalCol, int goalRow) {
        this.goalNode = world.getNode(goalCol, goalRow);
        if(goalNode.isSolid()) return;

        for(int col = 0; col < world.getWidth(); col++) {
            for(int row = 0; row < world.getHeight(); row++) {
                Node node = world.getNode(col, row);
                node.setGCost(MAX_COST);
                node.setFCost(MAX_COST);
                node.setParent(null);
            }
        }

        this.initNode.setGCost(0);
        this.initNode.setHCost(heuristic.getCost(initNode.getCol(), initNode.getRow(), goalNode.getCol(), goalNode.getRow()));
        this.initNode.setFCost(initNode.getHCost());
        this.openedList.offer(initNode);
    }

    private void buildPath(){
        Node current = goalNode;
        while(current != initNode){
            this.path.push(current);
            current = current.getParent();
        }
    }

    private void openNeighbour(Node current, Node neighbour) {
        if(!neighbour.isSolid() && !closedList.contains(neighbour)) {
            int neighbourStepCost = heuristic.getCost(current.getCol(), current.getRow(), neighbour.getCol(), neighbour.getRow());
            int tentativeGCost = current.getGCost() + neighbourStepCost;
            if(tentativeGCost < neighbour.getGCost()) {
                if(neighbour.getGCost() == MAX_COST) {
                    neighbour.setHCost(heuristic.getCost(neighbour.getCol(), neighbour.getRow(), goalNode.getCol(), goalNode.getRow()));
                }

                neighbour.setParent(current);
                neighbour.setGCost(tentativeGCost);
                neighbour.setFCost(neighbour.getGCost() + neighbour.getHCost());

                if(openedList.contains(neighbour)) {
                    this.openedList.remove(neighbour);
                }

                this.openedList.offer(neighbour);
            }
        }
    }

    private void expandNode(Node node, boolean allowDiagonalMove) {
        int col = node.getCol();
        int row = node.getRow();

        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                if(x == 0 && y == 0 || world.isNodeOutBounds(col + x, row + y)) {
                    continue;
                }

                if((x != 0 && y != 0)) {
                    if(!allowDiagonalMove ||
                        (world.getNode(col + x, row).isSolid() ||
                         world.getNode(col, row + y).isSolid())) continue;
                }

                Node neighbour = world.getNode(col + x, row + y);
                this.openNeighbour(node, neighbour);
            }
        }
    }

    public Stack<Node> findPath(int goalCol, int goalRow, boolean allowDiagonalMove) {
        this.setTarget(goalCol, goalRow);

        while(currNode != goalNode && !openedList.isEmpty()) {
            this.currNode = openedList.poll();

            if(currNode == goalNode) {
                this.buildPath();
                break;
            }

            this.closedList.add(currNode);
            this.expandNode(currNode, allowDiagonalMove);
        }

        return this.path;
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.YELLOW);
        this.openedList.forEach(node -> {
            float drawX = node.getPosition().x + 2;
            float drawY = node.getPosition().y + 2;
            shapeRenderer.rect(drawX, drawY, World.TILE_SIZE - 4, World.TILE_SIZE - 4);
        });

        shapeRenderer.setColor(Color.BLUE);
        this.closedList.forEach(node -> {
            float drawX = node.getPosition().x + 2;
            float drawY = node.getPosition().y + 2;
            shapeRenderer.rect(drawX, drawY, World.TILE_SIZE - 4, World.TILE_SIZE - 4);
        });

        shapeRenderer.setColor(Color.GREEN);
        this.path.forEach(node -> {
            float drawX = node.getPosition().x + 2;
            float drawY = node.getPosition().y + 2;
            shapeRenderer.rect(drawX, drawY, World.TILE_SIZE - 4, World.TILE_SIZE - 4);
        });

        if(initNode != null){
            shapeRenderer.setColor(Color.CYAN);
            float initX = initNode.getPosition().x + 2;
            float initY = initNode.getPosition().y + 2;
            shapeRenderer.rect(initX, initY, World.TILE_SIZE - 4, World.TILE_SIZE - 4);

        }

        if(goalNode != null){
            shapeRenderer.setColor(Color.RED);
            float goalX = goalNode.getPosition().x + 2;
            float goalY = goalNode.getPosition().y + 2;
            shapeRenderer.rect(goalX, goalY, World.TILE_SIZE - 4, World.TILE_SIZE - 4);
        }
    }

}
