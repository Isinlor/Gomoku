import Distribution.DistributionTableMethod;
import java.util.HashMap;
import java.util.Map;

public class SamplingTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nDistribution Test\n");

        distributionTableMethodTest();

    }

    private static void distributionTableMethodTest() {
        it("allows sampling from discrete distribution over finite set of size 2", () -> {

            // sample from distribution where "true" has 0.33 chances, "false" has 0.67 chances

            Map<Boolean, Double> map = new HashMap<>();
            map.put(true, 0.33);
            map.put(false, 0.67);

            DistributionTableMethod<Boolean> distribution = new DistributionTableMethod<>(map);

            int trueSelected = 0;
            int samples = 100000;
            for (int i = 0; i < samples; i++) {
                if(distribution.sample()) {
                    trueSelected++;
                }
            }

            assertEqual((double) trueSelected / samples, 0.33, 0.1, "The proportion of X must be around 33%.");

        });

        it("allows sampling from discrete distribution over finite set of size 3", () -> {

            // sample from distribution where "true" has 0.33 chances, "false" has 0.67 chances

            Map<Character, Double> map = new HashMap<>();
            map.put('a', 1.);
            map.put('b', 2.);
            map.put('c', 3.);

            DistributionTableMethod<Character> distribution = new DistributionTableMethod<>(map);

            int aSelected = 0;
            int bSelected = 0;
            int cSelected = 0;
            int samples = 100000;
            for (int i = 0; i < samples; i++) {
                switch (distribution.sample()) {
                    case 'a':
                        aSelected++;
                        break;
                    case 'b':
                        bSelected++;
                        break;
                    case 'c':
                        cSelected++;
                        break;
                }
            }

            assertEqual((double) aSelected / samples, 1./6., 0.1, "The proportion of a must be around 1/6.");
            assertEqual((double) bSelected / samples, 2./6., 0.1, "The proportion of b must be around 2/6.");
            assertEqual((double) cSelected / samples, 3./6., 0.1, "The proportion of c must be around 3/6.");

        });
    }

}
