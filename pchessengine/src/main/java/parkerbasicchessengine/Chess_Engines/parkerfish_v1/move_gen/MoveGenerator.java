package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.LSBLoopGenerator;

public class MoveGenerator extends Constants {

    public static final long knightMoves[] = { 0x20400L, 0x50800L, 0xa1100L, 0x142200L, 0x284400L, 0x508800L, 0xa01000L,
            0x402000L, 0x2040004L, 0x5080008L, 0xa110011L, 0x14220022L, 0x28440044L, 0x50880088L, 0xa0100010L,
            0x40200020L, 0x204000402L, 0x508000805L, 0xa1100110aL, 0x1422002214L, 0x2844004428L, 0x5088008850L,
            0xa0100010a0L, 0x4020002040L, 0x20400040200L, 0x50800080500L, 0xa1100110a00L, 0x142200221400L,
            0x284400442800L, 0x508800885000L, 0xa0100010a000L, 0x402000204000L, 0x2040004020000L, 0x5080008050000L,
            0xa1100110a0000L, 0x14220022140000L, 0x28440044280000L, 0x50880088500000L, 0xa0100010a00000L,
            0x40200020400000L, 0x204000402000000L, 0x508000805000000L, 0xa1100110a000000L, 0x1422002214000000L,
            0x2844004428000000L, 0x5088008850000000L, 0xa0100010a0000000L, 0x4020002040000000L, 0x400040200000000L,
            0x800080500000000L, 0x1100110a00000000L, 0x2200221400000000L, 0x4400442800000000L, 0x8800885000000000L,
            0x100010a000000000L, 0x2000204000000000L, 0x4020000000000L, 0x8050000000000L, 0x110a0000000000L,
            0x22140000000000L, 0x44280000000000L, 0x88500000000000L, 0x10a00000000000L, 0x20400000000000L };
            
    public static final long kingMoves[] = { 0x302L, 0x705L, 0xe0aL, 0x1c14L, 0x3828L, 0x7050L, 0xe0a0L, 0xc040L,
            0x30203L, 0x70507L, 0xe0a0eL, 0x1c141cL, 0x382838L, 0x705070L, 0xe0a0e0L, 0xc040c0L, 0x3020300L, 0x7050700L,
            0xe0a0e00L, 0x1c141c00L, 0x38283800L, 0x70507000L, 0xe0a0e000L, 0xc040c000L, 0x302030000L, 0x705070000L,
            0xe0a0e0000L, 0x1c141c0000L, 0x3828380000L, 0x7050700000L, 0xe0a0e00000L, 0xc040c00000L, 0x30203000000L,
            0x70507000000L, 0xe0a0e000000L, 0x1c141c000000L, 0x382838000000L, 0x705070000000L, 0xe0a0e0000000L,
            0xc040c0000000L, 0x3020300000000L, 0x7050700000000L, 0xe0a0e00000000L, 0x1c141c00000000L, 0x38283800000000L,
            0x70507000000000L, 0xe0a0e000000000L, 0xc040c000000000L, 0x302030000000000L, 0x705070000000000L,
            0xe0a0e0000000000L, 0x1c141c0000000000L, 0x3828380000000000L, 0x7050700000000000L, 0xe0a0e00000000000L,
            0xc040c00000000000L, 0x203000000000000L, 0x507000000000000L, 0xa0e000000000000L, 0x141c000000000000L,
            0x2838000000000000L, 0x5070000000000000L, 0xa0e0000000000000L, 0x40c0000000000000L };

    private BitwiseBoard bwB;
    public long teamMask[] = new long[2];
    public long allPieces;

    public int piecesCheckingKingCount;
    public long enemyVision;
    public long blockCheck;

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
    }

    public BitwiseMove[] generateMoves() {

        this.teamMask[White] = generateTeamMask(White);
        this.teamMask[Black] = generateTeamMask(Black);
        this.allPieces = this.teamMask[White] | this.teamMask[Black];
        
        MoveList moveList = new MoveList();

        // pass in a moveList to save moves, otherwise move generator assumes 
        // you are generating opposition vision and pins

        pawnMoveGen(moveList);
        //slidingPieceMoveGen(moveList);
        //knightMoveGen(moveList);
        kingMoveGen(moveList);

        BitwiseMove[] endMoveList = moveList.toArray();

        for(int i = 0; i < moveList.index; i++){
            endMoveList[i] = moveList.moves[i];
        }

        return endMoveList;
    }

    private void kingMoveGen(MoveList moveList) {
        int team = bwB.isWhiteToMove ? White : Black;

        int kingIndex = Long.numberOfTrailingZeros(bwB.piece_bitboards[team][K]);

        generateSlidingMoves(MoveGenerator.kingMoves[kingIndex], kingIndex, BitwiseMove.NORMAL_MOVE, moveList);

        if (bwB.canCastleKingside()) {
            moveList.append(new BitwiseMove(kingIndex, bwB.isWhiteToMove ? 6 : 62, BitwiseMove.CASTLE_KINGSIDE));
        }

        if (bwB.canCastleQueenside()) {
            moveList.append(new BitwiseMove(kingIndex, bwB.isWhiteToMove ? 2 : 58, BitwiseMove.CASTLE_QUEENSIDE));
        }

    }

    private void knightMoveGen(MoveList moveList) {

        int team = bwB.isWhiteToMove ? White : Black;
        LSBLoopGenerator knightLooper = new LSBLoopGenerator(bwB.piece_bitboards[team][N]);

        int knightIndex;

        while (knightLooper.hasNext) {

            knightIndex = Long.numberOfTrailingZeros(knightLooper.getNext());

            long knightMoves = MoveGenerator.knightMoves[knightIndex];

            generateSlidingMoves(knightMoves, knightIndex, BitwiseMove.NORMAL_MOVE, moveList);

        }
    }

    private void slidingPieceMoveGen(MoveList moveList) {

        int team = bwB.isWhiteToMove ? White : Black;

        // gen bishop moves
        LSBLoopGenerator pieceLoop = new LSBLoopGenerator(bwB.piece_bitboards[team][B]);
        int currentPiece;

        long friendlyPieces = this.teamMask[team];

        while (pieceLoop.hasNext) {
            currentPiece = Long.numberOfTrailingZeros(pieceLoop.getNext());
            long bishopMovementBitboard = SlidingMovementGeneration.generateBishopMovementBitboard(currentPiece,
                    this.allPieces);
            bishopMovementBitboard = bishopMovementBitboard & ~(friendlyPieces);
            generateSlidingMoves(bishopMovementBitboard, currentPiece, BitwiseMove.NORMAL_MOVE, moveList);
        }

        // rook
        pieceLoop.setBitboard(bwB.piece_bitboards[team][R]);
        while (pieceLoop.hasNext) {
            currentPiece = Long.numberOfTrailingZeros(pieceLoop.getNext());
            long rookMovementBitboard = SlidingMovementGeneration.generateBishopMovementBitboard(currentPiece,
                    this.allPieces);
            rookMovementBitboard = rookMovementBitboard & ~(friendlyPieces);
            generateSlidingMoves(rookMovementBitboard, currentPiece, BitwiseMove.NORMAL_MOVE, moveList);
        }

        // queen
        pieceLoop.setBitboard(bwB.piece_bitboards[team][Q]);
        while (pieceLoop.hasNext) {
            currentPiece = Long.numberOfTrailingZeros(pieceLoop.getNext());
            long queenMovementBitboard = SlidingMovementGeneration.generateBishopMovementBitboard(currentPiece,
                    this.allPieces);
            queenMovementBitboard = queenMovementBitboard & ~(friendlyPieces);
            generateSlidingMoves(queenMovementBitboard, currentPiece, BitwiseMove.NORMAL_MOVE, moveList);
        }
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

        generatePawnMoves(singlePushes, bwB.isWhiteToMove ? -8 : 8, 0, moveList);
        generatePawnMoves(doublePushes, bwB.isWhiteToMove ? -16 : 16, BitwiseMove.PAWN_MOVE_DOUBLE, moveList);
        generatePawnMoves(leftCaptures, bwB.isWhiteToMove ? -7 : 7, BitwiseMove.NORMAL_MOVE, moveList);
        generatePawnMoves(rightCaptures, bwB.isWhiteToMove ? -9 : 9, BitwiseMove.NORMAL_MOVE, moveList);

        generateMovesPromotions(promotionsSinglePushes, bwB.isWhiteToMove ? -16 : 16, moveList);
        generateMovesPromotions(promotionsLeftCaptures, bwB.isWhiteToMove ? -7 : 7, moveList);
        generateMovesPromotions(promotionsRightCaptures, bwB.isWhiteToMove ? -9 : 9, moveList);

        if (enPassantLeft != 0L) {
            generatePawnMoves(enPassantLeft, bwB.isWhiteToMove ? -7 : 7, BitwiseMove.EN_PASSANT_CAPTURE, moveList);
        }
        if (enPassantRight != 0L) {
            generatePawnMoves(enPassantRight, bwB.isWhiteToMove ? -9 : 9, BitwiseMove.EN_PASSANT_CAPTURE, moveList);
        }
        
    }

    private void generatePawnMoves(long bitboard, int adjustDirection, int moveFlag, MoveList moveList) {
        LSBLoopGenerator loop = new LSBLoopGenerator(bitboard);
        while (loop.hasNext) {
            int toIndex = Long.numberOfTrailingZeros(loop.getNext());
            int fromIndex = toIndex + adjustDirection;
            moveList.append(new BitwiseMove(fromIndex, toIndex, moveFlag));
        }
    }

    private void generateSlidingMoves(long bitboard, int fromIndex, int moveFlag, MoveList moveList) {
        LSBLoopGenerator loop = new LSBLoopGenerator(bitboard);
        while (loop.hasNext) {
            int toIndex = Long.numberOfTrailingZeros(loop.getNext());
            moveList.append(new BitwiseMove(fromIndex, toIndex, moveFlag));
        }
    }

    private void generateMovesPromotions(long bitboard, int adjustDirection, MoveList moveList) {
        LSBLoopGenerator loop = new LSBLoopGenerator(bitboard);
        while (loop.hasNext) {
            int toIndex = Long.numberOfTrailingZeros(loop.getNext());
            int fromIndex = toIndex + adjustDirection;
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_BISHOP));
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_KNIGHT));
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_ROOK));
            moveList.append(new BitwiseMove(fromIndex, toIndex, BitwiseMove.PROMOTE_TO_QUEEN));

        }
    }
}