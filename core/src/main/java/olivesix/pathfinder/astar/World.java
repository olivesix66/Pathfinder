package olivesix.pathfinder.astar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class World {

    public static final int TILE_SIZE = 64;

    private final int width, height;
    private final Node[][] grid;

    private final int widthInPixels;
    private final int heightInPixels;

    public World(int width, int height) {
        this.width  = width;
        this.height = height;
        this.grid = new Node[width][height];

        this.widthInPixels  = width  * TILE_SIZE;
        this.heightInPixels = height * TILE_SIZE;
    }

    public void build(){
        for (int col = 0; col < width; col++){
            for (int row = 0; row < height; row++){
                this.grid[col][row] = new Node(col, row, false);
            }
        }
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidthInPixels(){
        return this.widthInPixels;
    }

    public int getHeightInPixels(){
        return this.heightInPixels;
    }

    public boolean isNodeOutBounds(int col, int row){
        return col < 0 || col >= width || row < 0 || row >= height;
    }

    public Node getNode(int col, int row){
        if(isNodeOutBounds(col, row)){
            throw new GdxRuntimeException("Node not in bounds");
        }
        return this.grid[col][row];
    }

    public void markNodeSolid(int col, int row){
        if(isNodeOutBounds(col, row)) return;
        Node node = getNode(col, row);
        node.setSolid(!node.isSolid());
    }

    public void generateMaze() {
        // reset all nodes as solid
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                grid[col][row].setSolid(true);
            }
        }

        Random random = new Random();
        boolean[][] visited = new boolean[width][height];
        Stack<int[]> stack = new Stack<>();

        // maze cells sit on odd positions
        int startCol = 1;
        int startRow = 1;

        grid[startCol][startRow].setSolid(false);
        visited[startCol][startRow] = true;
        stack.push(new int[]{startCol, startRow});

        // directions in steps of 2 (skips the wall between cells)
        int[][] dirs = {{0, 2}, {0, -2}, {2, 0}, {-2, 0}};

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int col = current[0];
            int row = current[1];

            List<int[]> neighbors = new ArrayList<>();
            for (int[] dir : dirs) {
                int nextCol = col + dir[0];
                int nextRow = row + dir[1];
                if (!isNodeOutBounds(nextCol, nextRow) && !visited[nextCol][nextRow]) {
                    neighbors.add(new int[]{nextCol, nextRow, dir[0] / 2, dir[1] / 2});
                }
            }

            if (!neighbors.isEmpty()) {
                int[] chosen = neighbors.get(random.nextInt(neighbors.size()));
                int nextCol = chosen[0];
                int nextRow = chosen[1];
                int wallCol = col + chosen[2];
                int wallRow = row + chosen[3];

                // open the cell and the wall between them
                grid[nextCol][nextRow].setSolid(false);
                grid[wallCol][wallRow].setSolid(false);
                visited[nextCol][nextRow] = true;
                stack.push(new int[]{nextCol, nextRow});
            } else {
                stack.pop();
            }
        }
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, widthInPixels, heightInPixels);

        for(int col = 0; col < width; col++){
            for(int row = 0; row < height; row++){
                Node node = getNode(col, row);

                float drawX = node.getPosition().x + 1;
                float drawY = node.getPosition().y + 1;

                shapeRenderer.setColor(node.isSolid() ? Color.BLACK : Color.GRAY);
                shapeRenderer.rect(drawX, drawY, TILE_SIZE - 2, TILE_SIZE - 2);
            }
        }

        shapeRenderer.flush();
    }

    public void drawNodesData(SpriteBatch batch, BitmapFont font){
        for(int col = 0; col < width; col++){
            for(int row = 0; row < height; row++){
                Node node = getNode(col, row);
                if(!node.isSolid()){
                    float drawX = node.getPosition().x + 4;
                    float drawY = node.getPosition().y + 16;

                    font.draw(batch, "F:" + node.getFCost(), drawX, drawY);
                    font.draw(batch, "H:" + node.getHCost(), drawX, drawY + 22);
                    font.draw(batch, "G:" + node.getGCost(), drawX, drawY + 44);
                }
            }
        }
    }

}
