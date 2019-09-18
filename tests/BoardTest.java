import Board.*;

public class BoardTest extends SimpleUnitTest {
    public static void main(String[] args) {
        it("Creates simple board", () -> {
            new Board(15);
        });
    }
}
