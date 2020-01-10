package Contract;

public enum Color {
    Black, White;
    public Color getOpposite() {
        switch (this) {
            case Black:
                return White;
            case White:
                return Black;
        }
        return null;
    }
}
