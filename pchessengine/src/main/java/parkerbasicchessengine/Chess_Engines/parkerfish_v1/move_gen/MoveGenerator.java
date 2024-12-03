package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;

public class MoveGenerator extends Constants {

    private BitwiseBoard bwB;
    public static long teamMask[] = new long[2];

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
        
        BitwiseMove[] generatedMoves = new BitwiseMove[218 /* Theoretical max */];

        int team = bwB.isWhiteToMove ? 0 : 1;

        // generate piece moves

        
        return generatedMoves;
    }

    

}
