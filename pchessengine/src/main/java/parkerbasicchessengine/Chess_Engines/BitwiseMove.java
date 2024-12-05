package parkerbasicchessengine.Chess_Engines;

public class BitwiseMove {
    public short move;

    public static final byte NORMAL_MOVE = 0;        // Regular move
    public static final byte PROMOTE_TO_QUEEN = 1;   // Pawn promotion to Queen
    public static final byte PROMOTE_TO_BISHOP = 2;  // Pawn promotion to Bishop
    public static final byte PROMOTE_TO_KNIGHT = 3;  // Pawn promotion to Knight
    public static final byte PROMOTE_TO_ROOK = 4;    // Pawn promotion to Rook
    public static final byte PAWN_MOVE_DOUBLE = 5;   // Pawn moves double
    public static final byte EN_PASSANT_CAPTURE = 6; // En passant capture
    public static final byte CASTLE_KINGSIDE = 7;    // Kingside castle
    public static final byte CASTLE_QUEENSIDE = 8;   // Queenside castle

    public BitwiseMove(byte fromSquare, byte toSquare, short flag) {
        this.move = (short) ((flag << 12) | (toSquare << 6) | fromSquare);
    }

    public BitwiseMove(int fromSquare, int toSquare, int flag) {
        this.move = (short) ((flag << 12) | (toSquare << 6) | fromSquare);
    }

    public BitwiseMove(int fromSquare, int toSquare, byte flag) {
        this.move = (short) ((flag << 12) | (toSquare << 6) | fromSquare);
    }

    public BitwiseMove(int fromSquare, int toSquare, int flag) {
        this.move = (short) ((flag << 12) | (toSquare << 6) | fromSquare);
    }

    public BitwiseMove(short fromSquare, short toSquare, short flag) {
        this.move = (short) ((flag << 12) | (toSquare << 6) | fromSquare);
    }

    public BitwiseMove(short move){
        this.move = move;
    }

    public boolean isPromoting() {
        int flag = this.getFlag();
        return flag > 0 && flag < 5;
    }

    public boolean isCastling(){
        return this.getFlag() >= 7;
    }

    public boolean isEnpassantMove(){
        return this.getFlag() == BitwiseMove.EN_PASSANT_CAPTURE;
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
