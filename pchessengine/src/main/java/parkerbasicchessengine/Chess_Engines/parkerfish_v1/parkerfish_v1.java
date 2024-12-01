package parkerbasicchessengine.Chess_Engines.parkerfish_v1;


import parkerbasicchessengine.Board;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.AbstractChessEngine;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.MoveGenerator;

public class parkerfish_v1 extends AbstractChessEngine {

    private MoveGenerator moveGenerator;

    public BitwiseBoard bwB = new BitwiseBoard(board.convertPostionToFEN());

    private int search(int depth, int alpha, int beta){
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

    public void orderMoves(BitwiseMove[] moves){

        for(BitwiseMove move : moves){

            int moveScoreGuess = 0;
            int movePieceType = bwB.getPieceType(move.getFromSquare());
            int capturePieceType = bwB.getPieceType(move.getToSquare());
            
            if(capturePieceType != -1){
                moveScoreGuess = 10 * bwB.getPieceValue(capturePieceType) - bwB.getPieceValue(movePieceType);
            }

            

        }

    }

    public int evaluate(){
        int eval;

        return eval;
    }

}
