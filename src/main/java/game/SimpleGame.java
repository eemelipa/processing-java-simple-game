package game;

import java.io.File;

import org.encog.ml.MLMethod;
import org.encog.ml.MLRegression;
import org.encog.neural.neat.NEATPopulation;
import org.encog.persist.EncogDirectoryPersistence;
import processing.core.PApplet;
import processing.event.KeyEvent;
import renderers.MasterRenderer;

public class SimpleGame extends PApplet {

    private World world;
    private int framerate = 80;
    private int speed = 0;
    private int skipToFrame = 0;
    private boolean pause = false;

    @Override
    public void settings() {
        size(800, 500);
    }

    @Override
    public void setup() {
        frameRate(framerate);

        boolean useAI = true;
        Player player = new HumanPlayer();

        if (useAI) {
            Object object = EncogDirectoryPersistence.loadObject(new File(System.getProperty("user.dir") + "/best-network.eg"));
            if (object instanceof NEATPopulation) {
                NEATPopulation population = (NEATPopulation) object;
                MLMethod mlMethod = population.getCODEC().decode(population.getBestGenome());

                AIBrainBlockVision brain = new AIBrainBlockVision((MLRegression) mlMethod);
                player = new Player(brain);
            }
        }

        world = new World(player, 1L);
    }

    @Override
    public void draw() {
        while (world.getTick() < skipToFrame) {
            world.progressATick();
        }

        background(80);
        if (world.getPlayer().isAlive() && !pause) {
            int count = 0;
            do {
                world.progressATick();
                count++;
            } while (count <= speed);
        }
        MasterRenderer.draw(world, this);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case 38:
                // UP arrow
                world.humanPlayerUp();
                break;
            case 40:
                // DOWN arrow
                world.humanPlayerDown();
                break;
            case 82: // r
                setup();
                break;
            case 32: // space
                pause = !pause;
                break;
            case 45:
                // +
                speed++;
                break;
            case 47:
                // -
                if (speed > 0) {
                    speed--;
                }
                break;
            default:
                System.out.println(event.getKeyCode());
        }
    }

    public static void main(String... args) {
        PApplet.main("game.SimpleGame");
    }
}
