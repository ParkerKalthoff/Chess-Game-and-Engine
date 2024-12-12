package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.LSBLoopGenerator;

public class MoveGenerator2 extends Constants {

    private BitwiseBoard bwB;
    public long teamMask[] = new long[2];
    public long allPieces;

    public MoveGenerator2(BitwiseBoard bwB) {
        this.bwB = bwB;
    }

    public BitwiseMove[] generateMoves() {
        this.teamMask[White] = generateTeamMask(White);
        this.teamMask[Black] = generateTeamMask(Black);
        this.allPieces = this.teamMask[White] | this.teamMask[Black];

        MoveList moveList = new MoveList();

        generatePawnMoves(moveList);
        generateSlidingPieceMoves(moveList);
        generateKnightMoves(moveList);
        generateKingMoves(moveList);

        return moveList.toArray();
    }

    private long generateTeamMask(int team) {
        long mask = 0L;
        for (long pieceMask : bwB.piece_bitboards[team]) {
            mask |= pieceMask;
        }
        return mask;
    }

    // These methods are used to generator move objects for the game

    private void generatePawnMoves(MoveList moveList) {
        long bitboard = generatePawnBitboard(bwB, teamMask, allPieces);
        MoveUtils.generateMovesFromBitboard(bitboard, moveList, BitwiseMove.NORMAL_MOVE);
    }

    private void generateSlidingPieceMoves(MoveList moveList) {
        long bitboard = generateRookBitboard(bwB, teamMask, allPieces);
        MoveUtils.generateMovesFromBitboard(bitboard, moveList, BitwiseMove.NORMAL_MOVE);
    }

    private void generateBishopPieceMoves(MoveList moveList) {
        long bitboard = generateBishopBitboard(bwB, teamMask, allPieces);
        MoveUtils.generateMovesFromBitboard(bitboard, moveList, BitwiseMove.NORMAL_MOVE);
    }

    private void generateQueenPieceMoves(MoveList moveList) {
        long bitboard = generateQueenBitboard(bwB, teamMask, allPieces);
        MoveUtils.generateMovesFromBitboard(bitboard, moveList, BitwiseMove.NORMAL_MOVE);
    }

    private void generateKnightMoves(MoveList moveList) {
        long bitboard = KnightMoveGenerator.generateBitboard(bwB, teamMask);
        MoveUtils.generateMovesFromBitboard(bitboard, moveList, BitwiseMove.NORMAL_MOVE);
    }

    private void generateKingMoves(MoveList moveList) {
        long bitboard = KingMoveGenerator.generateBitboard(bwB, teamMask);
        MoveUtils.generateMovesFromBitboard(bitboard, moveList, BitwiseMove.NORMAL_MOVE);
    }



    public static long generateBitboard(BitwiseBoard bwB, long[] teamMask, long allPieces) {
        
        return 0L;
    }


    public static long generateRookBitboard(BitwiseBoard bwB, long[] teamMask, long allPieces) {
        return 0L;
    }



    public static long generateKnightBitboard(BitwiseBoard bwB, long[] teamMask) {
        return 0L;
    }



    public static long generateKingBitboard(BitwiseBoard bwB, long[] teamMask) {
        return 0L;
    }



}

class MoveUtils {
    public static void generateMovesFromBitboard(long bitboard, MoveList moveList, int moveFlag) {
        LSBLoopGenerator loop = new LSBLoopGenerator(bitboard);
        while (loop.hasNext) {
            int toIndex = Long.numberOfTrailingZeros(loop.getNext());
            int fromIndex = 0; 
            moveList.append(new BitwiseMove(fromIndex, toIndex, moveFlag));
        }
    }
}