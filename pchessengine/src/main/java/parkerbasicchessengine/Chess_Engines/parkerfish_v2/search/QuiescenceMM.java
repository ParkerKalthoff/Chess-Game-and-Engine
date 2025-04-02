package parkerbasicchessengine.Chess_Engines.parkerfish_v2.search;

import java.util.ArrayList;
import java.util.List;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.IEvaluate;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.Evaluator_v1;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.Utils.MoveOrdering;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

public class QuiescenceMM implements ISearch {

    // Implementation for Minimax with A-B pruning

    public Board board;
    private IEvaluate evalModule;

    private final int DEPTH = 4; // Piles
    private final int QUIESCENCE_DEPTH_LIMIT = 2; // Piles (This is ontop of the regular depth so I keep it low)

    public int nodesGenerated;
    public int positionsEvaluated;

    public QuiescenceMM(IEvaluate evaluater) {
        this.evalModule = evaluater;
    }

    public void setBoard(Board board) {
        this.board = board;
        this.evalModule.setBoard(board);
    }

    public void printBoard() {
        System.out.println(this.board.toString());
    }

    public void makeMove(String move) {
        board.makeMoveUsingCoordinate(move);
    }

    public Move makeMove() {

        this.nodesGenerated = 0;
        this.positionsEvaluated = 0;

        //long startTime = System.currentTimeMillis();

        ArrayList<Move> validMoves = board.getValidMoves();

        nodesGenerated += validMoves.size();

        if (validMoves.size() < 1) {

            System.out.println((board.isWhitesTurn ? "Black" : "White") + " wins!");
            return null;

        }

        int bestEval = board.isWhitesTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        Move bestMove = null;

        //System.out.println("Checking These Moves : ");

        for (Move move : validMoves) {

            board.makeMove(move);
            
            int searchEval = minimax(DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            
            //System.out.println("\t" + move + "\n\t\tEval of : " + searchEval);
            
            board.unmakeMove(move);

            // Fixed Black maximizing on first step
            if (board.isWhitesTurn ? searchEval > bestEval : searchEval < bestEval) {
            
                bestEval = searchEval;
            
                bestMove = move;
            
            }
        }

        /*
        System.out.println("[Minimax] " + validMoves.size() + " Potential Moves in this position with the evaluation for this position at " + this.evalModule.evaluate() + " for " + (board.isWhitesTurn ? "White" : "Black") + " team");

        System.out.println("[Minimax] Finished Search of depth " + DEPTH + " in " + (System.currentTimeMillis() - startTime) + " miliseconds");

        System.out.println("[Minimax] " + nodesGenerated + " nodes generated, with " + positionsEvaluated + " positions evaluated");
        */

        //System.out.println("[Minimax] Best Move : " + bestMove);

        //System.out.println("[Minimax] Fen String : " + this.board.fenString());

        board.makeMove(bestMove);

        System.out.println(board);

        return bestMove;
    }

    private int minimax(int depth, int alpha, int beta) {

        if (depth == 0 || board.isGameOver) {

            int eval = quiescenceSearch(alpha, beta, 1);

            return eval;

        }

        ArrayList<Move> moves = board.getValidMoves();

        moves = MoveOrdering.orderMoves(moves);

        

        if (moves.isEmpty()) {

            long kingBitboard = board.bitboards[board.getActiveTeam()][K];

            int recencyBias = board.isWhitesTurn ? - (DEPTH - depth - 1) : (DEPTH - depth - 1);

            if (kingBitboard == 0L) {
                // king captured (should pause before, but safeguard)

                return evalModule.evaluate() + recencyBias;
            }

            if (board.isKingInCheck()) {


                if (board.isWhitesTurn) {
                    return Integer.MIN_VALUE - recencyBias; // Black is checkmating
                } else {
                    return Integer.MAX_VALUE - recencyBias; // White is checkmating
                }
            }

            // stalemate
            return 0;

        }

        if (board.isWhitesTurn) { // White / Maximizing Player

            int maxEval = Integer.MIN_VALUE;

            for (Move move : moves) {

                board.makeMove(move);

                int evaluation = minimax(depth - 1, alpha, beta);

                board.unmakeMove(move);

                maxEval = Math.max(maxEval, evaluation);

                alpha = Math.max(alpha, evaluation);

                if (beta <= alpha) {
                    break;
                }

            }
            return maxEval;

        } else { // Black / Minimizing player

            int minEval = Integer.MAX_VALUE;

            for (Move move : moves) {

                board.makeMove(move);

                int evaluation = minimax(depth - 1, alpha, beta);

                board.unmakeMove(move);

                minEval = Math.min(minEval, evaluation);

                beta = Math.min(beta, evaluation);

                if (beta <= alpha) {
                    break;

                }
            }
            return minEval;
        }
    }

    private int quiescenceSearch(int alpha, int beta, int depth) {
        int standingPat = evalModule.evaluate();
    
        if (standingPat >= beta) {
            return beta;
        }
    
        alpha = Math.max(alpha, standingPat);
    
        ArrayList<Move> moves = board.getValidMoves();
    
        // Remove quiet moves
        ArrayList<Move> nonQuietMoves = new ArrayList<>(moves.stream()
                               .filter(Move::isCapture)
                               .toList());
    
        nonQuietMoves = MoveOrdering.orderMoves(nonQuietMoves);
    
        if (moves.isEmpty()) {
            long kingBitboard = board.bitboards[board.getActiveTeam()][K];
    
            if (kingBitboard == 0L) { // king captured
                return evalModule.evaluate();
            }
    
            if (board.isKingInCheck()) {
                return board.isWhitesTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE; // Checkmate
            }
    
            return 0; // Stalemate
        }
    
        if (board.isGameOver || nonQuietMoves.isEmpty() || depth >= QUIESCENCE_DEPTH_LIMIT) {
            return standingPat;
        }
    
        if (board.isWhitesTurn) { // Maximizing Player
            int maxEval = Integer.MIN_VALUE;
    
            for (Move move : nonQuietMoves) {
                board.makeMove(move);
                int evaluation = quiescenceSearch(alpha, beta, depth + 1);
                board.unmakeMove(move);
    
                maxEval = Math.max(maxEval, evaluation);
                alpha = Math.max(alpha, evaluation);
    
                if (beta <= alpha) break;
            }
            return maxEval;
    
        } else { // Minimizing Player
            int minEval = Integer.MAX_VALUE;
    
            for (Move move : nonQuietMoves) {
                board.makeMove(move);
                int evaluation = quiescenceSearch(alpha, beta, depth + 1);
                board.unmakeMove(move);
    
                minEval = Math.min(minEval, evaluation);
                beta = Math.min(beta, evaluation);
    
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }    
}