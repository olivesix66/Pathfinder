package olivesix.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import olivesix.pathfinder.astar.AStar;
import olivesix.pathfinder.astar.World;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Pathfinder extends ApplicationAdapter {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private World world;
    private AStar astar;

    private OrthographicCamera camera;
    private FitViewport viewport;

    private Vector3 worldPos;
    private GridPoint2 worldCoords;

    private enum Phase {SET_INIT, SET_GOAL}
    private Phase phase = Phase.SET_INIT;

    @Override
    public void create() {

        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();

        int worldWidth  = 25; // 25 horizontal tiles - 25 * 64 = 1600 pixels
        int worldHeight = 15; // 15 vertical tiles - 15 * 64 = 960 pixels

        this.world = new World(worldWidth, worldHeight);
        this.world.build();

        this.astar = new AStar(world);

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(world.getWidthInPixels(), world.getHeightInPixels(), camera);

        this.worldPos = new Vector3();
        this.worldCoords = new GridPoint2();
    }

    public GridPoint2 getWorldCoordsFromMouseClick(float mouseX, float mouseY){
        this.worldPos.set(mouseX, mouseY, 0);
        this.camera.unproject(worldPos,
            viewport.getScreenX(),
            viewport.getScreenY(),
            viewport.getScreenWidth(),
            viewport.getScreenHeight());

        this.worldCoords.x = (int) (worldPos.x / World.TILE_SIZE);
        this.worldCoords.y = (int) (worldPos.y / World.TILE_SIZE);

        return this.worldCoords;
    }

    private void update(float dt) {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)){
            GridPoint2 mouseCords = getWorldCoordsFromMouseClick(Gdx.input.getX(), Gdx.input.getY());
            if(world.isNodeOutBounds(mouseCords.x, mouseCords.y)) return;
            this.world.markNodeSolid(mouseCords.x, mouseCords.y);
        }

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            GridPoint2 mouseCords = getWorldCoordsFromMouseClick(Gdx.input.getX(), Gdx.input.getY());
            if(world.isNodeOutBounds(mouseCords.x, mouseCords.y)) return;
            if(phase == Phase.SET_INIT){
                this.astar.setInit(mouseCords.x, mouseCords.y);
                this.phase = Phase.SET_GOAL;
            }else{
                this.astar.findPath(mouseCords.x, mouseCords.y, true);
                this.phase = Phase.SET_INIT;
            }
        }
    }

    @Override
    public void resize(int width, int height){
        this.viewport.update(width, height, true);
    }

    @Override
    public void render() {
        this.update(Gdx.graphics.getDeltaTime());

        this.viewport.apply(true);

        ScreenUtils.clear(Color.BLACK);

        this.shapeRenderer.setProjectionMatrix(camera.combined);
        this.shapeRenderer.setAutoShapeType(true);
        this.shapeRenderer.begin();
        this.world.draw(shapeRenderer);
        this.astar.drawPath(shapeRenderer);
        this.shapeRenderer.end();

        this.batch.setProjectionMatrix(camera.combined);
        this.batch.begin();
        this.world.drawNodesData(batch, font);
        this.batch.end();
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();
    }
}
