package renderers;

import game.SimpleGame;
import game.World;

public interface Renderer {
    void render(World world, SimpleGame canvas);
}
