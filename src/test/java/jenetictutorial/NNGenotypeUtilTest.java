package jenetictutorial;

import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.util.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class NNGenotypeUtilTest {

    @Test
    public void shouldCreateValid() {
        // GIVEN
        Factory<Genotype<DoubleGene>> factory = NNGenotypeUtil.factory(5, 10, 3);

        // WHEN
        int count = 0;
        while (count < 100) {
            Genotype<DoubleGene> genotype = factory.newInstance();

            NNGenotypeUtil.isValid(genotype);

            count++;
        }

        // THEN

    }

}