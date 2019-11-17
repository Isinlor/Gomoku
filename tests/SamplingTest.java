public class SamplingTest extends SimpleUnitTest {

    public static void main(String[] args) {

        it("allows sampling from discrete distribution over finite set", () -> {

            // TODO: sample from distribution where x has 0.33 chances, y has 0.67 chances

            double proportionOfx = 0;

            assertEqual(proportionOfx, 0.33, 0.1, "Wrong proportion of X.");

        });

    }

}
