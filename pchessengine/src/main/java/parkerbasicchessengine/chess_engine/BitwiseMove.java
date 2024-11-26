package parkerbasicchessengine.chess_engine;

public class BitwiseMove {

    private short move;

    // Constructor to initialize a move
    public BitwiseMove(int start, int end, int promotion) {
        if (start < 0 || start >= 64 || end < 0 || end >= 64) {
            throw new IllegalArgumentException("Square indices must be in the range 0-63");
        }
        if (promotion < 0 || promotion > 7) {
            throw new IllegalArgumentException("Promotion must be between 0 and 7");
        }
        this.move = (short) ((start & 0x3F) | ((end & 0x3F) << 6) | ((promotion & 0x7) << 12));
    }

    public int getStartingSquareIndex() {
        return move & 0x3F;
    }

    public int getEndSquareIndex() {
        return (move >> 6) & 0x3F;
    }

    public String getStartingSquareCoord() {
        int startingSquare = this.getStartingSquareIndex();
        return "" + ((char) ('A' + (startingSquare % 8))) + (8 - (startingSquare / 8));
    }

    public String getEndSquareCoord() {
        int endSquare = this.getEndSquareIndex();
        return "" + ((char) ('A' + (endSquare % 8))) + (8 - (endSquare / 8));
    }

    public int getPromotion() {
        return (move >> 12) & 0x7;
    }

    public String getPromotionString() {
        int promotion = getPromotion();
        if (promotion == 0) {
            return "No promotion";
        }

        boolean isWhite = (promotion & 0x4) != 0;
        String team = isWhite ? "White" : "Black";

        String pieceType = "None";
        
        switch (promotion & 0x3) {
            case 0 -> pieceType = "Queen";
            case 1 -> pieceType = "Bishop";
            case 2 -> pieceType = "Knight";
            case 3 -> pieceType = "Rook";
        }

        return team + " " + pieceType;
    }

    @Override
    public String toString() {
        return "{Start : " + getStartingSquareCoord() + ", End : " + getEndSquareCoord() + (getPromotion() == 0 ? "" : ", Promotion : " + getPromotionString()) + "}";
    }
}
