package Contract;

public enum BoardCell {

    Black, White, Empty;

    public Color getColor() {
        switch (this) {
            case Black:
                return Color.Black;
            case White:
                return Color.White;
        }
        return null;
    }

}
