package parkerbasicchessengine.Chess_Engines.parkerfish_v1;

import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;

public class MoveGenerator {

    private BitwiseBoard bwB;

    private long generateTeamMask(int team){
        long teamMask;

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
        return null;
    }

}
