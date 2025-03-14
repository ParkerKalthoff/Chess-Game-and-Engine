package parkerbasicchessengine.Chess_Engines.parkerfish_v2.search;

import java.util.ArrayList;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.IEvaluate;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.Evaluator_v1;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

public class Minimax implements ISearch {

    // Implementation for Minimax with A-B pruning for maximum epicness

    private Board board;
    private IEvaluate evalModule;

    private final int DEPTH = 3;

    public int nodesGenerated;
    public int positionsEvaluated;

    public Minimax(Board board) {
        this.board = board;
        this.evalModule = new Evaluator_v1(board);
    }

    public void printBoard() {
        System.out.println(this.board.toString());
    }

    public void makeMove() {

        this.nodesGenerated = 0;
        this.positionsEvaluated = 0;

        long startTime = System.currentTimeMillis();

        ArrayList<Move> validMoves = board.getValidMoves();

        nodesGenerated += validMoves.size();

        if (validMoves.size() < 1) {

            System.out.println((board.isWhitesTurn ? "Black" : "White") + " wins!");
            return;

        }

        int high = Integer.MIN_VALUE; // Change to MIN_VALUE for maximization

        Move bestMove = null;

        System.out.println("Checking These Moves : ");

        for (Move move : validMoves) {

            board.makeMove(move);

            int searchEval = minimax(DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE); //depth -1 here.

            System.out.println("\t" + move + "\n\t\tEval of : " + searchEval);

            if (searchEval > high) { // Change to > for maximization

                bestMove = move;

                high = searchEval;
            }

            board.unmakeMove(move);

        }

        System.out.println("[Minimax] " + validMoves.size() + " Potential Moves in this position with the evaluation for this position at " + this.evalModule.evaluate() + " for " + (board.isWhitesTurn ? "White" : "Black") + " team");

        System.out.println("[Minimax] Finished Search of depth " + DEPTH + " in " + (System.currentTimeMillis() - startTime) + " miliseconds");

        System.out.println("[Minimax] " + nodesGenerated + " nodes generated, with " + positionsEvaluated + " positions evaluated");

        System.out.println("[Minimax] Best Move : " + bestMove);

        board.makeMove(bestMove);
    }

    private int minimax(int depth, int alpha, int beta) {

        if (depth == 0 || board.isGameOver) {

            if (board.isGameOver) {
                System.out.println("[DEBUG] IsGameOver : true");
            }

            positionsEvaluated += 1;

            int eval = evalModule.evaluate();

            if (eval == Integer.MIN_VALUE) {
                System.out.println("[DEBUG] EvalModule returned " + Integer.MIN_VALUE);
            }

            return eval;

        }

        ArrayList<Move> moves = board.getValidMoves();

        nodesGenerated += moves.size();

        if (moves.isEmpty()) {

            System.out.println("[DEBUG] No valid moves found at depth " + depth);

            long kingBitboard = board.bitboards[board.getActiveTeam()][K];

            if (kingBitboard == 0L) {
                // king captured (should pause before, but safeguard)
                positionsEvaluated += 1;

                return evalModule.evaluate();
            }

            if (board.isKingInCheck()) {

                // checkmate
                positionsEvaluated += 1;

                if (board.isWhitesTurn) {
                    return Integer.MIN_VALUE + depth; // Black is checkmating
                } else {
                    return Integer.MAX_VALUE - depth; // White is checkmating
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
}