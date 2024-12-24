package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import javax.management.RuntimeErrorException;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.LSBLoopGenerator;

public class MoveGenerator2 extends Constants {

    private static final long knightMoves[] = { 0x20400L, 0x50800L, 0xa1100L, 0x142200L, 0x284400L, 0x508800L, 0xa01000L,
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
        
    private static final long kingMoves[] = { 0x302L, 0x705L, 0xe0aL, 0x1c14L, 0x3828L, 0x7050L, 0xe0a0L, 0xc040L,
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
    private long teamMask[] = new long[2];
    private long allPieces;
    private long enemyVision;

    // These variables are used to have check detection
    private boolean activeKingInCheck;
    
    private int activeKingIndex;

    // blocker mask, where a piece can only block or capture if only single attacker
    private long checkBlockingMask;
    
    // Multiple checks forces only a king move
    private boolean multipleChecksOnActiveKing;


    private MoveGenerator2(BitwiseBoard bwB) {
        this.bwB = bwB;
    }

    private BitwiseMove[] generateMoves() {

        int activeTeam = this.bwB.isWhiteToMove ? White : Black;

        activeKingIndex = Long.numberOfTrailingZeros(this.bwB.piece_bitboards[activeTeam][K]);

        if( activeKingIndex == 64 || this.bwB.isGameOver){ // no active king
            return new BitwiseMove[0];
        }

        // reset state
        this.enemyVision = 0L;

        this.activeKingInCheck = false;    
        this.checkBlockingMask = 0L;
        this.multipleChecksOnActiveKing = false;

        this.teamMask[White] = generateTeamMask(White);
        this.teamMask[Black] = generateTeamMask(Black);
        this.allPieces = this.teamMask[White] | this.teamMask[Black];

        // populates the check prevention
        generateEnemyVision();

        MoveList moveList = new MoveList();

        

        return moveList.toArray();
    }

    private long generateTeamMask(int team) {
        long mask = 0L;
        for (long pieceMask : bwB.piece_bitboards[team]) {
            mask |= pieceMask;
        }
        return mask;
    }

    private BitwiseMove generateActiveTeamMoves(MoveList moveList){

        // generate rooks


        
    }

    private void generateEnemyVision(){

        int team = this.bwB.isWhiteToMove ? Black : White;
        
        LSBLoopGenerator bitboardIterator = new LSBLoopGenerator(this.bwB.piece_bitboards[team][R]);

        while(bitboardIterator.hasNext){
           int index = Long.numberOfTrailingZeros(bitboardIterator.getNext());
           generateRookBitboard(index, false); 
        }

        bitboardIterator.setBitboard(this.bwB.piece_bitboards[team][R]);

        while(bitboardIterator.hasNext){
           int index = Long.numberOfTrailingZeros(bitboardIterator.getNext());
           generateBishopBitboard(index, false); 
        }

        bitboardIterator.setBitboard(this.bwB.piece_bitboards[team][Q]);

        while(bitboardIterator.hasNext){
           int index = Long.numberOfTrailingZeros(bitboardIterator.getNext());
           generateQueenBitboard(index, false); 
        }

        bitboardIterator.setBitboard(this.bwB.piece_bitboards[team][N]);

        while(bitboardIterator.hasNext){
           int index = Long.numberOfTrailingZeros(bitboardIterator.getNext());
           generateKnightBitboard(index, false); 
        }

        generatePawnRightAttacksBitboards(false);
        generatePawnLeftAttacksBitboards(false);
        
    }

    // Move gen

    private long generateRookBitboard(int index, boolean isActive) {
        
        int team = this.bwB.isWhiteToMove == isActive ? White : Black;

        long moveBitboard = SlidingMovementGeneration.generateRookMovementBitboard(index, team);

        if(!isActive){
            detectCheck(index, team, moveBitboard);
            this.enemyVision |= moveBitboard; 
        }

        return moveBitboard;
    }

    private long generateBishopBitboard(int index, boolean isActive) {
        
        int team = this.bwB.isWhiteToMove == isActive ? White : Black;

        long moveBitboard = SlidingMovementGeneration.generateBishopMovementBitboard(index, team);

        if(!isActive){
            detectCheck(index, team, moveBitboard);
            this.enemyVision |= moveBitboard; 
        }

        return moveBitboard;
    }

    private long generateQueenBitboard(int index, boolean isActive) {
        
        int team = this.bwB.isWhiteToMove == isActive ? White : Black;

        long moveBitboard = SlidingMovementGeneration.generateQueenMovementBitboard(index, team);

        if(!isActive){
            detectCheck(index, team, moveBitboard);
            this.enemyVision |= moveBitboard; 
        }

        return moveBitboard;
    }

    private long generateKnightBitboard(int index, boolean isActive){

        long moveBitboard = kingMoves[index];
        int team = this.bwB.isWhiteToMove == isActive ? White : Black;

        if(!isActive){
            detectCheck(index, team, moveBitboard);
            this.enemyVision |= moveBitboard;
        }

        return moveBitboard;
    }

    private long generateKingNormalMovesBitboard(int index, boolean isActive) {
        
        long moveBitboard = kingMoves[index];

        if(isActive){
            moveBitboard = moveBitboard & ~this.enemyVision;
        } else {
            this.enemyVision |= moveBitboard;
        }

        return moveBitboard;
    }

    private long generatePawnLeftAttacksBitboards(boolean isActive) {

        int team = this.bwB.isWhiteToMove == isActive ? White : Black;

        long pawns = this.bwB.piece_bitboards[team][P];
        long moveBitboard;

        if(this.bwB.isWhiteToMove){
            moveBitboard = (pawns << 7) & ~FILE_H;
        } else {
            moveBitboard = (pawns >>> 7) & ~FILE_A;
        }

        if(isActive){
            moveBitboard &= this.teamMask[team ^ 1];
        } else {
            enemyVision |= moveBitboard;
            detectCheck(team, team, moveBitboard);
        }

        return moveBitboard;
    }

    private long generatePawnRightAttacksBitboards(boolean isActive) {
        
        int team = this.bwB.isWhiteToMove == isActive ? White : Black;

        long pawns = this.bwB.piece_bitboards[team][P];
        long moveBitboard;

        if(this.bwB.isWhiteToMove){
            moveBitboard = (pawns << 9) & ~FILE_A;
        } else {
            moveBitboard = (pawns >>> 9) & ~FILE_H;
        }

        if(isActive){
            moveBitboard &= this.teamMask[team ^ 1];
        } else {
            enemyVision |= moveBitboard;
            detectCheck(team, team, moveBitboard);
        }

        return moveBitboard;
    }

    private long generatePawnForwardMovesBitboard(boolean isActive) {
        
        int team = this.bwB.isWhiteToMove == isActive ? White : Black;

        long bitboard;

        long activePawns = this.bwB.piece_bitboards[team][P];

        if(team == White){
            bitboard = (activePawns << 8) & ~this.allPieces;
        } else {
            bitboard = (activePawns >>> 8) & ~this.allPieces;
        }
        
        return bitboard;
    }

    private long generatePawnDoubleMovesBitboard(boolean isActive) {
        
        int team = this.bwB.isWhiteToMove == isActive ? White : Black;
        
        long bitboard;

        long singlePushes = generatePawnForwardMovesBitboard(isActive); 

        if(team == White){
            bitboard = ((singlePushes & RANK_3) << 8) & ~this.allPieces;
        } else {
            bitboard = ((singlePushes & RANK_6) >>> 8) & ~this.allPieces;
        }
        
        return bitboard;
    }

    private void detectCheck(int index, int team, long moveBitboard){

        if((moveBitboard & this.bwB.piece_bitboards[team^1][K]) != 0){

            if(this.activeKingInCheck){ // kings already checked by another piece
                this.checkBlockingMask = 0L; // no move can block check
            } else {
                this.activeKingInCheck = true;
                this.checkBlockingMask |= (1L << index | bit_between[index][this.activeKingIndex]);
            }
        }
    }

    // generate friendly team moves

    private void generateFriendlyPieceMoves(MoveList moveList, int pieceType) {
        int team = this.bwB.isWhiteToMove ? White : Black;
    
        LSBLoopGenerator iterator = new LSBLoopGenerator(this.bwB.piece_bitboards[team][pieceType]);
    
        while (iterator.hasNext) {
            int pieceIndex = Long.numberOfTrailingZeros(iterator.getNext());
            long movementBitboard;
    
            switch (pieceType) {
                case R:
                    movementBitboard = generateRookBitboard(pieceIndex, true);
                    break;
                case B:
                    movementBitboard = generateBishopBitboard(pieceIndex, true);
                    break;
                case Q:
                    movementBitboard = generateQueenBitboard(pieceIndex, true);
                    break;
                case N:
                    movementBitboard = generateKnightBitboard(pieceIndex, true);
                    break;
                default:
                    throw new RuntimeErrorException(null,"Illegal Piece:" + pieceType);
            }
    
            if (this.activeKingInCheck) {
                if (this.multipleChecksOnActiveKing) {
                    continue;
                }
                movementBitboard &= this.checkBlockingMask;
            }
    
            MoveUtils.generateMovesFromBitboardSinglePiece(movementBitboard, moveList, BitwiseMove.NORMAL_MOVE, pieceIndex);
        }
    }
    
    private void generateFriendlyRookMoves(MoveList moveList) {
        generateFriendlyPieceMoves(moveList, R);
    }
    
    private void generateFriendlyBishopMoves(MoveList moveList) {
        generateFriendlyPieceMoves(moveList, B);
    }
    
    private void generateFriendlyQueenMoves(MoveList moveList) {
        generateFriendlyPieceMoves(moveList, Q);
    }
    
    private void generateFriendlyKnightMoves(MoveList moveList) {
        generateFriendlyPieceMoves(moveList, N);
    }

    private void generateFriendlyPawnMoves(MoveList moveList){

        int team = this.bwB.isWhiteToMove ? White : Black;

        long singlePushes = generatePawnForwardMovesBitboard(true);
        long doublePushes = generatePawnDoubleMovesBitboard(true);

        long rightCaptures = generatePawnRightAttacksBitboards(true);
        long leftCaptures = generatePawnLeftAttacksBitboards(true);

        int enpassantIndex = this.bwB.enpassantIndex;

        if(enpassantIndex != -1){

            long enpasantRightCapture = rightCaptures & (1L << enpassantIndex);
            long enpasantLeftCapture = leftCaptures & (1L << enpassantIndex);

            rightCaptures &= ~enpasantRightCapture;
            leftCaptures &= ~enpasantLeftCapture;

            MoveUtils.generateMovesFromBitboardMultiplePieces(enpasantLeftCapture, moveList, BitwiseMove.EN_PASSANT_CAPTURE, bwB.isWhiteToMove ? -7 : 7);
            MoveUtils.generateMovesFromBitboardMultiplePieces(enpasantRightCapture, moveList, BitwiseMove.EN_PASSANT_CAPTURE,  bwB.isWhiteToMove ? -9 : 9);

        }

    }

}

class MoveUtils {
    public static void generateMovesFromBitboardSinglePiece(long moveBitboard, MoveList moveList, int moveFlag, int index) {
        LSBLoopGenerator loop = new LSBLoopGenerator(moveBitboard);
        while (loop.hasNext) {
            int toIndex = Long.numberOfTrailingZeros(loop.getNext()); 
            moveList.append(new BitwiseMove(index, toIndex, moveFlag));
        }
    }

    public static void generateMovesFromBitboardMultiplePieces(long movesBitboard, MoveList moveList, int moveFlag, int adjustBy) {
        LSBLoopGenerator loop = new LSBLoopGenerator(movesBitboard);
        while (loop.hasNext) {
            int toIndex = Long.numberOfTrailingZeros(loop.getNext());
            int fromIndex = toIndex - adjustBy; 
            moveList.append(new BitwiseMove(fromIndex, toIndex, moveFlag));
        }
    }
}