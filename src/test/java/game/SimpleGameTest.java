package game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleGameTest {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());

    @Test
    public void shouldRunWorldSilently() {
        // GIVEN
        Player player = new Player(new EmptyBrain());

        World world = new World(player, 1L);

        // WHEN
        Assert.assertEquals(player.getAge(), 0);
        world.progressATick();

        // THEN
        Assert.assertEquals(player.getAge(), 1);
    }

    @Test
    public void shouldDie() {
        // GIVEN
        Player player = new Player(new EmptyBrain());

        World world = new World(player, 1L);

        // WHEN
        Assert.assertTrue(player.isAlive());
        long count = 0;
        while (player.isAlive()) {
            world.progressATick();
            count++;

            if (count >= 4000) {
                Assert.fail("Player didn't die");
            }
        }

        // THEN
        LOG.info("Player age: " + player.getAge());
        Assert.assertTrue(player.getAge() > 10);
    }

    @Test
    public void shouldTrain() {
        // GIVEN


        // WHEN

        // THEN

    }

}
