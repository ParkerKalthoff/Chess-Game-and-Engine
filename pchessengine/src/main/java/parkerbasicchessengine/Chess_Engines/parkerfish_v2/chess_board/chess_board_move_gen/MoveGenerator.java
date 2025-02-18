package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen;
import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

import java.util.ArrayList;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen.BitboardGenerators;


public class MoveGenerator {

    // This class is meant to generate move objects for legal moves

    private Board board;
    private BitboardGenerators bitboardGenerators;

    private ArrayList<Move> moveList;

    // An array that stores a valid pin mask of moves
    // So if a bishop was pinned to a king by another bishop, they should still be able to move along that axis
    // If a rook was pinned instead, movement should be restricted
    // This should be cleared after the board switches the position, or atleast after all moves are generated
    private long pins[];

    // If multiple checks on king, this should be used to skip move gen
    private int checkCount;
    
    // a Mask to be able to quickly mask out moves that dont prevent check for non king pieces
    // Starts as a full bitboard, and then will be 'and'ed when pins come up
    private long checkMask;

    private long enemyVision;

    public MoveGenerator(Board board) {

        this.board = board;
        this.bitboardGenerators = new BitboardGenerators(board); 

    }

    private void resetState() {

        this.moveList = new ArrayList<>(); 
        this.pins = new long[64];
        this.checkCount = 0;
        this.checkMask = 0xFFFFFFFFFFFFFFFFL;
        this.enemyVision = 0L;

    }

    public ArrayList<Move> generateMoves() {

        resetState();

        int team = this.board.getActiveTeam();
        int enemyTeam = this.board.getInactiveTeam();

        updateOpponentCoverage(enemyTeam);

        return moveList;

    }

    private void updateOpponentCoverage(int enemyTeam) {

        // Variable names are based on perspetive of the active team, this calculates the enemy's vision

        int friendlyTeam = HelperFunctions.getEnemyTeam(enemyTeam);

        boolean isWhite = enemyTeam == White;

        long friendlyKing = this.board.bitboards[friendlyTeam][K];

        // checking pawns

        long pawnLeftAttack = bitboardGenerators.pawnLeftAttackVision(enemyTeam);

        if((pawnLeftAttack & friendlyKing) != 0L) {
            
            long attackingPawn;

            this.checkCount++;

            if(isWhite) {
                attackingPawn = friendlyKing << 9; 
            } else {
                attackingPawn = friendlyKing >>> 7;
            }

            this.checkMask &= attackingPawn;
        }

        this.enemyVision |= pawnLeftAttack; 

        long pawnRightAttack = bitboardGenerators.pawnRightAttackVision(enemyTeam);

        if((pawnRightAttack & friendlyKing) != 0L) {
            
            long attackingPawn;

            this.checkCount++;

            if(isWhite) {
                attackingPawn = friendlyKing << 7; 
            } else {
                attackingPawn = friendlyKing >>> 9;
            }

            this.checkMask &= attackingPawn;
        }

        this.enemyVision |= pawnRightAttack; 

    }




}
