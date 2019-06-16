package game.encog;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

import game.AIBrainBlockVision;
import game.Player;
import game.World;
import org.encog.Encog;
import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.ea.species.Species;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.simple.EncogUtility;

public class TrainNeat {

    public static double[][] XOR_INPUT = {{0.0, 0.0}, {1.0, 0.0},
            {0.0, 1.0}, {1.0, 1.0}};

    public static double[][] XOR_IDEAL = {{0.0}, {1.0}, {1.0}, {0.0}};

    public static void main(final String[] args) {

        NEATPopulation population = (NEATPopulation) EncogDirectoryPersistence.loadObject(new File(System.getProperty("user.dir") + "/best-network.eg"));
//        NEATPopulation population = new NEATPopulation(61, 2, 1000);
//        population.reset();
        population.setPopulationSize(2000);
        population.setSurvivalRate(0.6);

        final int targetScore = 700_000; // Use lower for faster training rounds
        Random random = new Random();

        CalculateScore calculateScore = new CalculateScore() {
            @Override
            public double calculateScore(MLMethod method) {
                if (method instanceof NEATNetwork) {
                    NEATNetwork network = (NEATNetwork) method;

                    Player player = new Player(new AIBrainBlockVision(network));
                    List<World> worlds = new ArrayList<>();
                    worlds.add(new World(player, 1L));

                    int totalScore = 0;
                    for (World world : worlds) {
                        int count = 0;
                        while (player.isAlive() && count <= targetScore) {
                            world.progressATick();
                            count++;
                        }

                        totalScore += player.getAge();
                    }

                    return totalScore / worlds.size();
                }
                throw new IllegalArgumentException("Cannot process MLMethod: " + method);
            }

            @Override
            public boolean shouldMinimize() {
                return false;
            }

            @Override
            public boolean requireSingleThreaded() {
                return false;
            }

        };

        final EvolutionaryAlgorithm train = NEATUtil.constructNEATTrainer(population, calculateScore);

        System.out.println("Timestamp: " + LocalDateTime.now() + " Epoch #" + train.getIteration() + " Best Score:" + train.getBestGenome().getScore() + ", Species:" + population.getSpecies().size());

        int iteration = 0;
        OptionalDouble previousAverage = OptionalDouble.empty();
        do {
            iteration++;
            train.iteration();
            System.out.println("Timestamp: " + LocalDateTime.now() + " Epoch #" + train.getIteration() + " Best Score:" + train.getBestGenome().getScore() + ", Species:" + population.getSpecies().size());
            if (iteration % 1 == 0) {
                EncogDirectoryPersistence.saveObject(new File(System.getProperty("user.dir") + "/best-network.eg"), train.getPopulation());

                OptionalDouble currentAverage = train.getPopulation().getSpecies().stream().mapToDouble(Species::getBestScore).average();
                System.out.println("Average species score: " + currentAverage.orElse(-1) + ". Previous: " + previousAverage.orElse(-1));
                previousAverage = currentAverage;
            }
        } while (train.getBestGenome().getScore() < targetScore);

        NEATNetwork network = (NEATNetwork) train.getCODEC().decode(train.getBestGenome());

        EncogDirectoryPersistence.saveObject(new File(System.getProperty("user.dir") + "/best-network.eg"), train.getPopulation());

        Encog.getInstance().shutdown();
    }


    private void runXOR() {
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
        NEATPopulation pop = new NEATPopulation(2, 1, 1000);
        pop.setInitialConnectionDensity(1.0);// not required, but speeds training
        pop.reset();

        CalculateScore score = new TrainingSetScore(trainingSet);
        // train the neural network

        final EvolutionaryAlgorithm train = NEATUtil.constructNEATTrainer(pop, score);

        do {
            train.iteration();
            System.out.println("Epoch #" + train.getIteration() + " Error:" + train.getError() + ", Species:" + pop.getSpecies().size());
        } while (train.getError() > 0.01);

        NEATNetwork network = (NEATNetwork) train.getCODEC().decode(train.getBestGenome());

        // test the neural network
        System.out.println("Neural Network Results:");
        EncogUtility.evaluate(network, trainingSet);

        Encog.getInstance().shutdown();
    }
}
