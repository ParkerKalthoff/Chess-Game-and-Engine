package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board;

public class Move {
    
    private int moveData;

    // Bit shifts for move
    private static final int TO_SQUARE_SHIFT = 6;
    private static final int TEAM_SHIFT = 12;
    private static final int PIECE_TYPE_SHIFT = 13;
    private static final int CAPTURE_FLAG_SHIFT = 16;
    private static final int CAPTURE_PIECE_TYPE_SHIFT = 17;
    private static final int PROMOTION_FLAG_SHIFT = 20;
    private static final int PROMOTION_PIECE_SHIFT = 21;
    private static final int PAWN_DOUBLE_MOVE_SHIFT = 24;
    private static final int EN_PASSANT_SHIFT = 25;
    private static final int CASTLING_SHIFT = 26;
    

    // Bit masks
    private static final int FROM_SQUARE_MASK = 0x3F; // 6 bits
    private static final int TO_SQUARE_MASK = 0x3F;   // 6 bits
    private static final int TEAM_MASK = 0x1;        // 1 bit
    private static final int PIECE_TYPE_MASK = 0x7;  // 3 bits
    private static final int CAPTURE_FLAG_MASK = 0x1; // 1 bit
    private static final int CAPTURE_PIECE_TYPE_MASK = 0x7; // 3 bits
    private static final int PROMOTION_FLAG_MASK = 0x1; // 1 bit
    private static final int PROMOTION_PIECE_MASK = 0x7; // 3 bits
    private static final int PAWN_DOUBLE_MOVE_MASK = 0x1; // 1 bit
    private static final int EN_PASSANT_MASK = 0x1;   // 1 bit
    private static final int CASTLE_MASK = 0x1;   // 1 bit

    public Move(int fromSquare, int toSquare, int team, int pieceType, boolean captureFlag, int capturePieceType, boolean promotionFlag, int promotionPiece, boolean pawnDoubleMove, boolean enPassant, boolean castling) {
        moveData = 0;
        moveData |= (fromSquare & FROM_SQUARE_MASK);
        moveData |= (toSquare & TO_SQUARE_MASK) << TO_SQUARE_SHIFT;
        moveData |= (team & TEAM_MASK) << TEAM_SHIFT;
        moveData |= (pieceType & PIECE_TYPE_MASK) << PIECE_TYPE_SHIFT;
        moveData |= ((captureFlag ? 1 : 0) & CAPTURE_FLAG_MASK) << CAPTURE_FLAG_SHIFT;
        moveData |= (capturePieceType & CAPTURE_PIECE_TYPE_MASK) << CAPTURE_PIECE_TYPE_SHIFT;
        moveData |= ((promotionFlag ? 1 : 0) & PROMOTION_FLAG_MASK) << PROMOTION_FLAG_SHIFT;
        moveData |= (promotionPiece & PROMOTION_PIECE_MASK) << PROMOTION_PIECE_SHIFT;
        moveData |= ((pawnDoubleMove ? 1 : 0) & PAWN_DOUBLE_MOVE_MASK) << PAWN_DOUBLE_MOVE_SHIFT;
        moveData |= ((enPassant ? 1 : 0) & EN_PASSANT_MASK) << EN_PASSANT_SHIFT;
        moveData |= ((castling ? 1 : 0) & CASTLE_MASK) << CASTLING_SHIFT;
    }

    // Getters for each field
    public int getFromSquare() {
        return moveData & FROM_SQUARE_MASK;
    }

    public int getToSquare() {
        return (moveData >> TO_SQUARE_SHIFT) & TO_SQUARE_MASK;
    }

    public int getTeam() {
        return (moveData >> TEAM_SHIFT) & TEAM_MASK;
    }

    public int getPieceType() {
        return (moveData >> PIECE_TYPE_SHIFT) & PIECE_TYPE_MASK;
    }

    public boolean isCapture() {
        return ((moveData >> CAPTURE_FLAG_SHIFT) & CAPTURE_FLAG_MASK) == 1;
    }

    public int getCapturePieceTeam() {
        return ((moveData >> TEAM_SHIFT) & TEAM_MASK) ^ 1;
    }

    public int getCapturePieceType() {
        return (moveData >> CAPTURE_PIECE_TYPE_SHIFT) & CAPTURE_PIECE_TYPE_MASK;
    }

    public boolean isPromotion() {
        return ((moveData >> PROMOTION_FLAG_SHIFT) & PROMOTION_FLAG_MASK) == 1;
    }

    public int getPromotionPiece() {
        return (moveData >> PROMOTION_PIECE_SHIFT) & PROMOTION_PIECE_MASK;
    }

    public boolean isPawnDoubleMove() {
        return ((moveData >> PAWN_DOUBLE_MOVE_SHIFT) & PAWN_DOUBLE_MOVE_MASK) == 1;
    }

    public boolean isEnPassant() {
        return ((moveData >> EN_PASSANT_SHIFT) & EN_PASSANT_MASK) == 1;
    }

    public boolean isCastling() {
        return ((moveData >> CASTLING_SHIFT) & CASTLE_MASK) == 1;
    }

    @Override
    public String toString() {
        return String.format("Move(from: %d, to: %d, team: %d, pieceType: %d, capture: %b, capturePiece: %d, promotion: %b, promotionPiece: %d, pawnDoubleMove: %b, enPassant: %b)",
            getFromSquare(), getToSquare(), getTeam(), getPieceType(), isCapture(), getCapturePieceType(),
            isPromotion(), getPromotionPiece(), isPawnDoubleMove(), isEnPassant());
    }
}

