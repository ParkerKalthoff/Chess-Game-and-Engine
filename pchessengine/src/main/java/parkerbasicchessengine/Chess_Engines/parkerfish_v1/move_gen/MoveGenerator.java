package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.LSBLoopGenerator;

public class MoveGenerator extends Constants {

    private BitwiseBoard bwB;
    public long teamMask[] = new long[2];

    private long generateTeamMask(int team){

        long teamMask = 0L;

        long[] team_bitboards = bwB.piece_bitboards[team]; 

        for(long pieceMask : team_bitboards){

            teamMask |= pieceMask;

        }
        
        return teamMask;
    }

    public MoveGenerator(BitwiseBoard bwB){
        this.bwB = bwB;

        this.teamMask[White] = generateTeamMask(White);
        this.teamMask[Black] = generateTeamMask(Black);
    }

    public BitwiseMove[] generateMoves(){
        
        MoveList moveList = new MoveList();

        pawnMoveGen(moveList);

        
        return moveList.moves;
    }

    private void pawnMoveGen(MoveList moveList) {

        int team = bwB.isWhiteToMove ? White : Black;
        int opponent = bwB.isWhiteToMove ? Black : White;
        long emptySquares = ~(this.teamMask[White] | this.teamMask[Black]);
        long activePawns = bwB.piece_bitboards[team][P];

        long singlePushes;
        long doublePushes;
        long leftCaptures;
        long rightCaptures;
        long enPassantSquare;
        long enPassantMoves;
        long promotions;
    
        if (bwB.isWhiteToMove) {
            singlePushes = (activePawns << 8) & emptySquares;
            doublePushes = ((singlePushes & RANK_3) << 8) & emptySquares;
    
            leftCaptures = (activePawns << 7) & this.teamMask[Black] & ~FILE_H;
            rightCaptures = (activePawns << 9) & this.teamMask[Black] & ~FILE_A;
    
            enPassantSquare = (bwB.enpassantIndex >= 0 && bwB.enpassantIndex < 64) ? (1L << bwB.enpassantIndex) : 0L;
            enPassantMoves = (activePawns << 7 & enPassantSquare & ~FILE_H) |
                                  (activePawns << 9 & enPassantSquare & ~FILE_A);
    
            promotions = (singlePushes & RANK_7) | (leftCaptures & RANK_7) | (rightCaptures & RANK_7);
        } else {
            singlePushes = (activePawns >>> 8) & emptySquares;
            doublePushes = ((singlePushes & RANK_6) >>> 8) & emptySquares; 
    
            leftCaptures = (activePawns >>> 7) & this.teamMask[White] & ~FILE_A; 
            rightCaptures = (activePawns >>> 9) & this.teamMask[White] & ~FILE_H;
    
            enPassantSquare = (bwB.enpassantIndex >= 0 && bwB.enpassantIndex < 64) ? (1L << bwB.enpassantIndex) : 0L;
            enPassantMoves = (activePawns >>> 7 & enPassantSquare & ~FILE_A) |
                                  (activePawns >>> 9 & enPassantSquare & ~FILE_H);

            promotions = (singlePushes & RANK_2) | (leftCaptures & RANK_2) | (rightCaptures & RANK_2);
        }

        LSBLoopGenerator loop = new LSBLoopGenerator(singlePushes);
        
        int adjustment = bwB.isWhiteToMove ? -8 : 8; 

        while(loop.hasNext){
            long attackBit = loop.getNext();

            int attackIndex = MovementBitboards.bitboardToIndex.get(attackBit);
            int fromIndex = attackIndex + adjustment;


            moveList.append(new BitwiseMove(0, attackIndex, fromIndex));

        }



    }
}    