package parkerbasicchessengine.Chess_Engines.parkerfish_v1;

import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;

public class MoveGenerator {

    private BitwiseBoard bwB;

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
    }

    public BitwiseMove[] generateMoves(){
        
        BitwiseMove[] generatedMoves = new BitwiseMove[218 /* Theoretical max */];

        int team = bwB.isWhiteToMove ? 0 : 1;

        // generate piece moves

        
        return generatedMoves;
    }

}
