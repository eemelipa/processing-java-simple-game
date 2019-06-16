package jenetictutorial;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

public class BitProblem {

    public static void main(String[] args) {
        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(20, 0.5));

        System.out.println(gtf);

        Engine<BitGene, Integer> engine = Engine.builder(SimpleGeneticAlgorithm::eval, gtf).build();

        Genotype<BitGene> result = engine.stream()
                .limit(500)
                .collect(EvolutionResult.toBestGenotype());

        System.out.println(result);
    }

}
