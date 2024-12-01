package parkerbasicchessengine.Chess_Engines;

public class BitwiseMove {
    public short move;

    public static final byte NORMAL_MOVE = 0;        // Regular move
    public static final byte PROMOTE_TO_QUEEN = 1;   // Pawn promotion to Queen
    public static final byte PROMOTE_TO_ROOK = 2;    // Pawn promotion to Rook
    public static final byte PROMOTE_TO_BISHOP = 3;  // Pawn promotion to Bishop
    public static final byte PROMOTE_TO_KNIGHT = 4;  // Pawn promotion to Knight
    public static final byte EN_PASSANT = 5;         // En passant capture
    public static final byte CASTLE_KINGSIDE = 6;    // Kingside castle
    public static final byte CASTLE_QUEENSIDE = 7;   // Queenside castle

    public BitwiseMove(byte fromSquare, byte toSquare, byte flag) {
        this.move = (short) ((flag << 12) | (toSquare << 6) | fromSquare);
    }

    public int getFromSquare() {
        return move & 0x3F;
    }
    
    public int getToSquare() {
        return (move >> 6) & 0x3F;
    }

    public int getFlag() {
        return (move >> 12) & 0xF;
    }
}
