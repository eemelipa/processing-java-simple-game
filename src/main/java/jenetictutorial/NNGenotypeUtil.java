package jenetictutorial;

import java.util.Random;
import java.util.function.Function;

import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.util.DoubleRange;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;
import io.jenetics.util.RandomRegistry;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;

public class NNGenotypeUtil implements Factory<Genotype<DoubleGene>> {

    public static Factory<Genotype<DoubleGene>> factory(int maxInputSize, int maxHiddenLayers, int numberOfOuputs) {
        Random random = RandomRegistry.getRandom();

        /*
        Generate neural network topology
        Column specifies layer. Cell value specifies node count in layer.
         */

        // Topology
        int hiddenLayers = random.nextInt(maxHiddenLayers);
        DoubleGene[] topologyGenes = new DoubleGene[hiddenLayers + 2];

        // Input size
        topologyGenes[0] = DoubleGene.of(maxInputSize, maxInputSize, maxInputSize + 0.001); // TODO: 2019-06-14 make dynamic
        // Output size
        topologyGenes[topologyGenes.length - 1] = DoubleGene.of(numberOfOuputs, numberOfOuputs, numberOfOuputs + 0.001);
        // Hidden layers
        int weightSize = 0;
        int previous = 0;
        for (int i = 1; i < topologyGenes.length - 1; i++) {
            topologyGenes[i] = DoubleGene.of(1, random.nextInt(10) + 0.001); // Limit hidden layer nodes to 10 for now
            int current = topologyGenes[i].intValue();

            weightSize += previous * current;

            previous = topologyGenes[i].intValue();
        }
        weightSize += previous * topologyGenes[topologyGenes.length - 1].intValue(); // Also count the connections between last two layers

        DoubleChromosome topologyChromosome = DoubleChromosome.of(topologyGenes);

        /*
        Generate neural network weights
        Each cell represents a weight between NN nodes. Row holds all weights in the network. For example

        Network: 2+4+1+2 neural network nodes => total cell count in weight chromosome 2*4+4*1+1*2 = 14
         */

        DoubleChromosome weightChromosome = DoubleChromosome.of(DoubleRange.of(-1, 1), weightSize);


        DoubleChromosome inputChromosome = DoubleChromosome.of(0, 10000, IntRange.of(1)); // Does not matter at this point


        return Genotype.of(topologyChromosome, weightChromosome);
    }

    public static void test() {
        MultilayerPerceptronClassifier params = new MultilayerPerceptronClassifier();
//        params.setInitialWeights();
//        params.setLayers();
    }

    public static Function<Genotype<DoubleGene>, Double> fitnessFunction() {
        // TODO: 2019-06-11 something clever here. we basically need to run the simulation
        return null;
    }

    public static boolean isValid(Genotype<DoubleGene> genotype) {




        return false;
    }

    @Override
    public Genotype<DoubleGene> newInstance() {
        return null;
    }
}
