package jenetictutorial;

import java.util.function.Function;

import io.jenetics.Chromosome;
import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.SinglePointCrossover;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.IntRange;

public class NeuralOptimizer {

    public static void main(String[] args) {
        // 1. Factory
        /*
          Now we want to optimize neural net, more specifically Multilayer Perceptor. NN will be modeled by genotype having three chromosome rows:

          Chromosomes
          1. NN weights
           - Weights in MLP NN. NN is fully connected. This vector can be split to parts according to NN topology
           decomposing the correct weights between nodes.
           - Constraints: -1..1 ? values. Column count = topology multiplied.
          2. NN topology
           - Gene contains number of nodes on specific NN layer
           - Each column represents a layer. Column 0 is input layer node count, last column is output layer node count.
           - Constraints: 1..n (int), Min 2 columns.
          3. Id for fetching inputs to NN
           - Column count needs to match topology input layer node count.
           - These ids are some sort of references to data inputs. Idea is that evolution can freely pick what inputs
           it would like to have.
           - Constraints: int?

          Gene type will be double. For weights it is true double, for topology and inputs it is double rounded to int.

         */


        /*
         Now the factory randomizes chromosome length on each creation. We might want to alter that so we don't end up
         generating lots of invalid chromosomes. This could be done by
         1. override isValid method (?) => not good because of retries
            . we might need this because of e.g. mutations, or crossovers. some stuff could happen there.
            => genotype isValid needs to be overridden. eh, needs to be added tto engine. genotype is final.
         2. Implement own newInstance generator => done by extending genotype and override "of" method. add to engine.
         3. Create custom genotype factory. => Sounds simple and straightforward. Pretty good.
         */


        DoubleChromosome weightChromosome = DoubleChromosome.of(-1, 1, IntRange.of(1, 5));
        DoubleChromosome topologyChromosome = DoubleChromosome.of(1, 3, IntRange.of(1, 10)); // Target: input (1), hidden (3), output (1).
        DoubleChromosome inputChromosome = DoubleChromosome.of(0, 10000, IntRange.of(1)); // Does not matter at this point

        Genotype<DoubleGene> genotypeFactory = Genotype.of(weightChromosome, topologyChromosome);

        System.out.println(genotypeFactory);

        // 2. Engine
        Function<Genotype<DoubleGene>, Double> fitnessFunction = genotype -> {
            /*
            We can implement constraints here.

            Should this be reward or cost function?
            => Reward, the further player gets the better.

            Testing:
            Now we implement something simple. Let's try to make this create specific type of NN
             */

            double fitness = 0;

            Chromosome<DoubleGene> weight = genotype.getChromosome(0);
            fitness += weight.stream().mapToDouble(DoubleGene::doubleValue).sum();

            Chromosome<DoubleGene> topology = genotype.getChromosome(1);
            if (topology.length() == 3) {
                // 3 points minus the distance from wanted node count. capped to 0
                fitness += Math.max(3 - Math.abs(1 - topology.getGene(0).doubleValue()), 0);
                fitness += Math.max(3 - Math.abs(3 - topology.getGene(1).doubleValue()), 0);
                fitness += Math.max(3 - Math.abs(1 - topology.getGene(2).doubleValue()), 0);
            }


            Chromosome<DoubleGene> input = genotype.getChromosome(0);

            return fitness;
        };

        Engine<DoubleGene, Double> engine = Engine.builder(fitnessFunction, genotypeFactory)
                .populationSize(200)
                // TODO: 2019-06-14 we need custom mutator and maybe crossover
//                .alterers()
                .build();

        // 3. Result
        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        EvolutionResult<DoubleGene, Double> best = engine.stream()
                .limit(500)
                .peek(statistics)
                .collect(EvolutionResult.toBestEvolutionResult());

        System.out.println(best.getBestPhenotype());
        System.out.println(statistics);

    }

}
