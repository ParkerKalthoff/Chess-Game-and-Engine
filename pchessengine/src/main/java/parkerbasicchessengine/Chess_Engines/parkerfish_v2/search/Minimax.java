package parkerbasicchessengine.Chess_Engines.parkerfish_v2.search;

import java.util.ArrayList;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.IEvaluate;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.Evaluator_v1;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

public class Minimax implements ISearch {

    // Implimentation for Minimax with A-B pruning for maximum epicness

    private Board board;
    private IEvaluate evalModule;

    private final int POSITIVE_INFINITY = Integer.MAX_VALUE;
    private final int NEGATIVE_INFINITY = Integer.MIN_VALUE;
    private final int DEPTH = 7;

    public Minimax(Board board) {
        this.board = board;
        this.evalModule = new Evaluator_v1(board);
    }

    public void printBoard() {
        System.out.println(this.board.toString());
    }

    public void makeMove() {

        ArrayList<Move> validMoves = board.getValidMoves();

        printBoard();

        System.out.println(validMoves.size());

        if (validMoves.size() < 1) {

            System.out.println(board.isWhitesTurn ? "Black" : "White" + " wins!");
            return;

        }

        int low = Integer.MAX_VALUE;

        Move bestMove = null;

        for (Move move : validMoves) {

            board.makeMove(move);

            printBoard();

            int searchEval = minimax(DEPTH, NEGATIVE_INFINITY, POSITIVE_INFINITY);

            if (searchEval < low) {

                bestMove = move;

                low = searchEval;
            }

            board.unmakeMove(move);

            printBoard();
        }

        board.makeMove(bestMove);

    }

    private int minimax(int depth, int alpha, int beta) {
        if (depth == 0 || board.isGameOver) {
            return evalModule.evaluate();
        }

        System.out.println(this.board.toString());

        ArrayList<Move> moves = board.getValidMoves();

        if (moves.isEmpty()) {

            long kingBitboard = board.bitboards[board.getActiveTeam()][K];

            if (kingBitboard == 0L) {
                // king captured (should pause before, but safeguard)
                System.out.println("\t[Minimax] : King captured");
                return NEGATIVE_INFINITY;
            }

            if (board.isKingInCheck()) {
                // checkmate
                System.out.println("\t[Minimax] : Checkmate");
                return NEGATIVE_INFINITY;
            }

            return 0;

        }

        for (Move move : moves) {

            board.makeMove(move);
            int evaluation = -minimax(depth - 1, -beta, -alpha);
            board.unmakeMove(move);

            if (evaluation >= beta) {
                return beta;
            }

            alpha = Math.max(alpha, evaluation);

        }

        return alpha;

    }
}
