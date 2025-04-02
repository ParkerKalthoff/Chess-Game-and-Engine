package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.NULL_SQUARE;
import java.util.Random;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen.HelperFunctions; 

public class ZobristHasher {

    // Setting a constant value for random seed to make debugging easier
    private static final int RANDOM_SEED = "The Stray Dog Saved A Stray Cat, And When It Grew Up, It Found Out That It Was A Gouger".hashCode();

    private Board board;
    private Random random = new Random(RANDOM_SEED);

    private long hash = 0L;

    private long[][][] pieceHash = new long[2][6][64];
    private long whiteToMoveHash;
    private long[] enpassantSquareHash = new long[64];
    private long[] castlingRightsHash = new long[16];

    public long getHash() {
        // generally you should use the return hash of the other hashing functions, leaving this here for future use
        return hash;
    }

    public ZobristHasher(Board board) {

        this.board = board;
    
        for (int team = 0; team < pieceHash.length; team++) {
            for (int piece = 0; piece < pieceHash[team].length; piece++) {
                for (int square = 0; square < pieceHash[team][piece].length; square++) {
                    pieceHash[team][piece][square] = random.nextLong();
                }
            }
        }
    
        whiteToMoveHash = random.nextLong();
    
        for (int i = 0; i < enpassantSquareHash.length; i++) {
            enpassantSquareHash[i] = random.nextLong();
        }
    
        for (int i = 0; i < castlingRightsHash.length; i++) {
            castlingRightsHash[i] = random.nextLong();
        }
    }
    
    public long generateNewBoardHash() {

        hash = 0L;
        
        for (int team = 0; team < pieceHash.length; team++) {
            for (int piece = 0; piece < pieceHash[team].length; piece++) {
                long pieceBitboard = board.bitboards[team][piece];
                int[] indicies = HelperFunctions.bitboardToArray(pieceBitboard);
                
                for(int index : indicies) {
                    hash ^= pieceHash[team][piece][index];
                }
            }
        }

        if(board.isWhitesTurn) {
            hash ^= whiteToMoveHash;
        }

        hash ^= castlingRightsHash[board.castlingRights];

        if(board.enPassantIndex != NULL_SQUARE) {
            hash ^= enpassantSquareHash[board.enPassantIndex];
        }

        return hash;
    }

    public void preMoveHashAdjustment() {
        
        if(board.enPassantIndex != NULL_SQUARE) {
            hash ^= enpassantSquareHash[board.enPassantIndex];
        }

        hash ^= castlingRightsHash[board.castlingRights];

    }

    
    public long postMoveHashAdjustment(Move move) {
        
        if(board.enPassantIndex != NULL_SQUARE) {
            hash ^= enpassantSquareHash[board.enPassantIndex];
        }

        hash ^= pieceHash[move.getTeam()][move.getPieceType()][move.getFromSquare()];

        if(move.isCapture()) {
            hash ^= pieceHash[move.getCapturePieceTeam()][move.getCapturePieceType()][move.getToSquare()];
        }

        int pieceType = move.isPromotion() ? move.getPromotionPiece() : move.getPieceType();

        hash ^= pieceHash[move.getTeam()][pieceType][move.getToSquare()];

        hash ^= castlingRightsHash[board.castlingRights];

        return hash;
    }
}
