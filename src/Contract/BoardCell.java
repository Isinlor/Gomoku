package Contract;

public enum BoardCell {
    Black, White, Empty;

    public Color getColor() {
        if(name().equals("Empty")) {
            return null;
        }
        return Color.valueOf(name());
    }

}
