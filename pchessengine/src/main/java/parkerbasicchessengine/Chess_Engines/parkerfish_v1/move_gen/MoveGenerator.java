package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.LSBLoopGenerator;

public class MoveGenerator extends Constants {

    private BitwiseBoard bwB;
    public long teamMask[] = new long[2];

    private long generateTeamMask(int team) {

        long teamMask = 0L;

        long[] team_bitboards = bwB.piece_bitboards[team];

        for (long pieceMask : team_bitboards) {

            teamMask |= pieceMask;

        }

        return teamMask;
    }

    public MoveGenerator(BitwiseBoard bwB) {
        this.bwB = bwB;

        this.teamMask[White] = generateTeamMask(White);
        this.teamMask[Black] = generateTeamMask(Black);
    }

    public BitwiseMove[] generateMoves() {

        MoveList moveList = new MoveList();

        pawnMoveGen(moveList);

        return moveList.moves;
    }

    private void pawnMoveGen(MoveList moveList) {
        
        int team = bwB.isWhiteToMove ? White : Black;

        long emptySquares = ~(this.teamMask[White] | this.teamMask[Black]);
        long activePawns = bwB.piece_bitboards[team][P];
        long enPassantSquare = (bwB.enpassantIndex >= 0 && bwB.enpassantIndex < 64) ? (1L << bwB.enpassantIndex) : 0L;

        long singlePushes, doublePushes, leftCaptures, rightCaptures;
        long enPassantLeft, enPassantRight;
        long promotionsSinglePushes, promotionsLeftCaptures, promotionsRightCaptures;

        if (bwB.isWhiteToMove) {
            singlePushes = (activePawns << 8) & emptySquares;
            doublePushes = ((singlePushes & RANK_3) << 8) & emptySquares;

            leftCaptures = (activePawns << 7) & this.teamMask[Black] & ~FILE_H;
            rightCaptures = (activePawns << 9) & this.teamMask[Black] & ~FILE_A;

            enPassantLeft = (activePawns << 7) & enPassantSquare & ~FILE_H;
            enPassantRight = (activePawns << 9) & enPassantSquare & ~FILE_A;

            promotionsSinglePushes = singlePushes & RANK_7;
            promotionsLeftCaptures = leftCaptures & RANK_7;
            promotionsRightCaptures = rightCaptures & RANK_7;
        } else {
            singlePushes = (activePawns >>> 8) & emptySquares;
            doublePushes = ((singlePushes & RANK_6) >>> 8) & emptySquares;

            leftCaptures = (activePawns >>> 7) & this.teamMask[White] & ~FILE_A;
            rightCaptures = (activePawns >>> 9) & this.teamMask[White] & ~FILE_H;

            enPassantLeft = (activePawns >>> 7) & enPassantSquare & ~FILE_A;
            enPassantRight = (activePawns >>> 9) & enPassantSquare & ~FILE_H;

            promotionsSinglePushes = singlePushes & RANK_2;
            promotionsLeftCaptures = leftCaptures & RANK_2;
            promotionsRightCaptures = rightCaptures & RANK_2;
        }

        generateMoves(singlePushes, bwB.isWhiteToMove ? -8 : 8, 0, moveList);
        generateMoves(doublePushes, bwB.isWhiteToMove ? -16 : 16, BitwiseMove.PAWN_MOVE_DOUBLE, moveList);
        generateMoves(leftCaptures, bwB.isWhiteToMove ? -7 : 7, BitwiseMove.NORMAL_MOVE, moveList);
        generateMoves(rightCaptures, bwB.isWhiteToMove ? -9 : 9, BitwiseMove.NORMAL_MOVE, moveList);

        generateMovesPromotions(promotionsSinglePushes, bwB.isWhiteToMove ? -16 : 16, moveList);
        generateMovesPromotions(promotionsLeftCaptures, bwB.isWhiteToMove ? -7 : 7, moveList);
        generateMovesPromotions(promotionsRightCaptures, bwB.isWhiteToMove ? -9 : 9, moveList);

        if (enPassantLeft != 0L) {
            generateMoves(enPassantLeft, bwB.isWhiteToMove ? -7 : 7, BitwiseMove.EN_PASSANT_CAPTURE, moveList);
        }
        if (enPassantRight != 0L) {
            generateMoves(enPassantRight, bwB.isWhiteToMove ? -9 : 9, BitwiseMove.EN_PASSANT_CAPTURE, moveList);
        }
    }

    private void generateMoves(long bitboard, int adjustDirection, int moveFlag, MoveList moveList) {
        LSBLoopGenerator loop = new LSBLoopGenerator(bitboard);
        while (loop.hasNext) {
            int toIndex = MovementBitboards.bitboardToIndex.get(loop.getNext());
            int fromIndex = toIndex + adjustDirection;
            moveList.append(new BitwiseMove(fromIndex, toIndex, moveFlag));
        }
    }

    private void generateMovesPromotions(long bitboard, int adjustDirection, MoveList moveList) {
        LSBLoopGenerator loop = new LSBLoopGenerator(bitboard);
        while (loop.hasNext) {
            int toIndex = MovementBitboards.bitboardToIndex.get(loop.getNext());
            int fromIndex = toIndex + adjustDirection;
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_BISHOP));
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_KNIGHT));
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_ROOK));
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_QUEEN));

        }
    }

}