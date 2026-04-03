package olivesix.pathfinder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import olivesix.pathfinder.astar.Node;
import olivesix.pathfinder.astar.World;

import java.util.Stack;

public class Agent {

    private static final float AGENT_RADIUS = World.TILE_SIZE * 0.375f;
    private static final float SPEED = 180f;
    private static final float REACH_THRESHOLD = 2f;

    private final Vector2 pos;
    private final Vector2 dir;

    private final Vector2 toTarget;

    private Stack<Node> path;
    private Vector2 target;

    public Agent(int col, int row) {
        this.pos = new Vector2(col * World.TILE_SIZE, row * World.TILE_SIZE);
        this.dir = new Vector2();

        this.toTarget = new Vector2();
    }

    public void setPath(Stack<Node> path) {
        this.path = path;
    }

    public void setPos(int col, int row) {
        this.pos.set(col * World.TILE_SIZE, row * World.TILE_SIZE);
        this.reset();
    }

    private void reset(){
        this.dir.setZero();
        this.target = null;
        this.path = null;
    }

    public void update(float delta){
        if ((path == null || path.isEmpty()) && target == null) return;

        if (target == null) {
            target = nextTarget();
        }

        this.toTarget.set(target).sub(pos);

        while (toTarget.len() <= REACH_THRESHOLD) {
            this.pos.set(target);
            if (path.isEmpty()) {
                target = null;
                dir.setZero();
                return;
            }
            target = nextTarget();
            toTarget.set(target).sub(pos);
        }

        dir.set(toTarget).nor();
        pos.mulAdd(dir, SPEED * delta);
    }

    private Vector2 nextTarget() {
       return path.pop().getPosition().cpy();
    }

    public void render(ShapeRenderer shapeRenderer){
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.circle(pos.x + World.TILE_SIZE / 2f, pos.y + World.TILE_SIZE / 2f, AGENT_RADIUS);
    }

}
