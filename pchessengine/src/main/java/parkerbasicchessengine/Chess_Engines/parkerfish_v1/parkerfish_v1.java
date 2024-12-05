package parkerbasicchessengine.Chess_Engines.parkerfish_v1;


import parkerbasicchessengine.Board;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.MoveGenerator;
import parkerbasicchessengine.Chess_Engines.AbstractChessEngine;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;

public class parkerfish_v1 extends AbstractChessEngine {

    private MoveGenerator moveGenerator;


    public BitwiseBoard bwB = new BitwiseBoard(board.convertPostionToFEN());

    private int search(int depth, int alpha, int beta){

        // minimax algorithm
        // generates all positions and when depth is at zero does an evaluation on all positions
        // and returns the best move

        // alpha beta pruning speeds up this process
        //
        //                         white start
        //                          /        \
        //                    black move    black move
        //                   /        \    /          \
        //  White Moves ->  3         -1  4            5
        //
        //  In this example, after white makes its initial move, black has a choice
        //  the left node, after blacks move, white can get a position of 3 or -1 (higher is better),
        //  but on the right side, after blacks move, white can get a position of 4 or 5
        //  'Alpha Beta Pruning' comes in and says, if we know white can choose to get a position of 4
        //   Why bother the rest of that node? From blacks perspective, the left node is the best choice.
        //
        //   This algorithm is most effective when the evaluations of the moves are already ordered before
        //   getting the evaluations, since its expensive to actually generate them and would defeat the purpose,
        //   I do a 'move ordering' algorithm that does a super basic scoring of each move, so I give :
        //   promotions a boost, a lower value piece capturing a higher value piece a boost, and lower the score
        //   for moves that put pieces in front of enemy pawns. 


        if(depth == 0){
            return evaluate();
        }

        BitwiseMove moves[] = moveGenerator.generateMoves();

        if(moves.length == 0){

            if(false /* Player in Check*/){
                return negativeInfinity;
            }
            
            return 0;

        }

        for(BitwiseMove move : moves){
            bwB.movePiece(move);
            int eval = -this.search(depth - 1, -beta, -alpha);
            bwB.unmovePiece(move);

            if(eval >= beta){
                // determines if opponent will likely avoid position
                return beta;
            }

            alpha = Math.max(alpha, eval);

        }

        return alpha;
    }

    @SuppressWarnings("unused")
    public void orderMoves(BitwiseMove[] moves){
        
        // alpha beta pruning is most effective when best moves are searched first and worst last
        // this function give an extremely rough eval to each position to then optimize the search function

        for(BitwiseMove move : moves){

            int moveScoreGuess = 0;
            int movePieceType = bwB.getPieceType(move.getFromSquare());
            int capturePieceType = bwB.getPieceType(move.getToSquare());
            
            if(capturePieceType != -1){
                // priortize good captures
                moveScoreGuess = 10 * bwB.getPieceValue(capturePieceType) - bwB.getPieceValue(movePieceType);
            }

            if(move.isPromoting()){
                moveScoreGuess += bwB.getPieceValue(move.getFlag());
            }

            // TODO 
            if(false /* move lands into enemy pawns */){
                movePieceType -= bwB.getPieceValue(move.getToSquare());
            }
        }

    }

    public int evaluate(){
        int eval = 1;

        return eval;
    }

}
