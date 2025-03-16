package parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.Utils;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;

public class MoveOrdering {

    // Using a move orderer for minimax to try to boost search speed

    public static ArrayList<Move> orderMoves(ArrayList<Move> moves) {

        ArrayList<MoveScorePair> moveScorePairs = new ArrayList<>();

        for(Move move : moves) {
            moveScorePairs.add(new MoveScorePair(move, getMoveScore(move)));
        }
        

        moveScorePairs.sort(Comparator.comparingInt(MoveScorePair::getScore).reversed());

        // create new arraylist for the moves in order based on greatest to least greatest
        return moveScorePairs.stream()
                    .map(MoveScorePair::getMove)
                    .collect(Collectors.toCollection(ArrayList::new));

    }

    private static int getMoveScore(Move move) {

        int score = 0;

        if(move.isCapture()) {
            
            int pieceScore = getPieceValue(move.getPieceType());
            int capturePieceScore = getPieceValue(move.getCapturePieceType());

            if(pieceScore < capturePieceScore) {

                // Likely a good capture since a less valuable piece captures a more valuable
                score += 1000 + pieceScore - capturePieceScore;

            } else {

                // Negative Weight for likely bad capture
                score += -500 + pieceScore - capturePieceScore;

            }

        }

        if(move.isPromotion()) {

            // Promotion is likely the best move possible
            score += 2000;

        }

        if(move.isCastling()) {

            // castling is likely better for king safety
            score += 200;

        }        

        return score;
    }

    private static int getPieceValue(int pieceType) {
        switch (pieceType) {
            case P: 
                return 100;
            case N: 
                return 320;
            case B: 
                return 330;
            case R: 
                return 500;
            case Q: 
                return 900;
            case K: 
                return 20000;
            default:
                return 0;
        }
    }

    private static class MoveScorePair {

        private final Move move;
        private final int score;
        
        public MoveScorePair(Move move, int score) {
            this.move = move;
            this.score = score;
        }
    
        public Move getMove() {
            return this.move;
        }
    
        public int getScore() {
            return this.score;
        }
    }
}
