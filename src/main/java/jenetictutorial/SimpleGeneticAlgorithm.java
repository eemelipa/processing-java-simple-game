package jenetictutorial;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;

public class SimpleGeneticAlgorithm {

    public static Integer eval(Genotype<BitGene> gt) {
        return gt.getChromosome().as(BitChromosome.class).bitCount();
    }

}
