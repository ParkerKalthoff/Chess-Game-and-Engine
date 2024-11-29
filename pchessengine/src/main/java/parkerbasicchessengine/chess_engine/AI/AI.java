package parkerbasicchessengine.chess_engine.AI;

import parkerbasicchessengine.pieces.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import static parkerbasicchessengine.Main.ai_verses_ai;

import parkerbasicchessengine.Board;
import parkerbasicchessengine.Move;

public class AI {

    Board board;

    public final int positiveInfinity = 9999999;
    public final int negativeInfinity = -positiveInfinity;

    public final int depth = 3;

    public String boardFEN;

    public AI(Board board) {
        this.board = board;
    }

    public void aiMove() {
        if (true) {
            if (board.isWhiteToMove) {
                if (ai_verses_ai) {
                    board.repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                board.repaint();

                if(boardFEN == null){
                    boardFEN = board.convertPostionToFEN();
                }

                makeMove();

            }
        }
    }

    public void makeMove() {

        ArrayList<Move> validMoves = findValidMoves();

        if (validMoves.size() < 1) {
            board.repaint();
            System.out.println(board.isWhiteToMove ? "Black" : "White" + " wins!");
            JOptionPane.showMessageDialog(null, board.isWhiteToMove ? "Black" : "White" + " wins!", "Winner!", JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        Random rng = new Random();
        Move chosenMove = validMoves.get(rng.nextInt(validMoves.size()));

        int low = Integer.MAX_VALUE;
        for (Move move : validMoves) {
            board.makeMove(move);
            int search = search(depth, negativeInfinity, positiveInfinity);
            if (search < low) {
                chosenMove = move;
                low = search;
            }
            board.unMakeMove(move);
        }

        
        board.makeMove(chosenMove);

        String gameState = board.updateGameState(board.isWhiteToMove);

        if (!gameState.equals("")) {
            board.gameState = gameState;
        }

        board.repaint();

        aiMove();
    }

    int search(int depth, int alpha, int beta) {
        if (depth == 0 || board.isGameOver) {
            return evaluate();
        }

        ArrayList<Move> moves = findValidMoves();
        if (moves.isEmpty()) {
            King king = (King) board.findKing(board.isWhiteToMove);

            if(king == null){ //
                return board.isWhiteToMove ? positiveInfinity : negativeInfinity;
            }

            if (king.inCheck(king.col, king.row)) {
                return Integer.MIN_VALUE;
            }
            return 0;
        }

        for (Move move : moves) {

            boolean isFirstMovePrimary = move.piece.isFirstMove;
            boolean isFirstMoveCapture = move.capture != null ? move.capture.isFirstMove : false;

            board.makeMove(move);
            int evaluation = -search(depth - 1, -beta, -alpha);
            board.unMakeMove(move);

            move.piece.isFirstMove = isFirstMovePrimary;

            if(isFirstMoveCapture){
                move.capture.isFirstMove = isFirstMoveCapture;
            }

            if (evaluation >= beta) {
                return beta;
            }

            alpha = Math.max(alpha, evaluation);

        }

        return alpha;

    }

    public int evaluate() {
        int whiteVal = countMaterial(true);
        int blackVal = countMaterial(false);

        int eval = whiteVal - blackVal;

        int perspective = board.isWhiteToMove ? 1 : -1;
        //        System.out.println("eval = " + eval * perspective);
        return eval * perspective;
    }

    public int countMaterial(boolean isWhite) {
        
        int material = 0;

        ArrayList<Integer> teamMaterial = board.pieceList.stream()
            .filter(p -> p.isWhite == isWhite)
            .map(p -> p.value)
            .collect(Collectors.toCollection(ArrayList :: new));

        for(Integer value : teamMaterial){
            material += value;
        }

        return material;
    }

    public void orderMoves(ArrayList<Move> moves) {

        for (Move move : moves) {
            Piece piece = move.piece;

            int moveScoreGuess = 0;
            char pieceCapName = 'e';
            if (move.capture != null) {
                pieceCapName = move.capture.abbreviation;
            }

            if (pieceCapName != 'e') {
                moveScoreGuess = 10 * move.capture.value - piece.value;
            }
            if (piece instanceof Pawn && (piece.isWhite ? piece.row == 0 : piece.row == 7)) {
                moveScoreGuess += move.promotedToPiece.value;
            }

        }

    }

    ArrayList<Move> findValidMoves() {

        ArrayList<Move> moves = new ArrayList<>();

        Piece king = board.findKing(board.isWhiteToMove);

        if (king != null) {

            for (Piece piece : board.pieceList) {
                board.selectedPiece = piece;

                if (board.sameTeam(piece, king)) {

                    for (int row = 0; row < board.rows; row++) {
                        for (int col = 0; col < board.cols; col++) {
                            Piece pieceXY = board.getPiece(col, row);
                            if (piece.canMakeMove(pieceXY, col, row)) {

                                if (piece instanceof Pawn && row == (piece.isWhite ? 0 : 7)) {

                                    moves.add(new Move(this.board, piece, col, row, new Queen(board, piece.col, piece.row, piece.isWhite)));
                                    moves.add(new Move(this.board, piece, col, row, new Rook(board, piece.col, piece.row, piece.isWhite)));
                                    moves.add(new Move(this.board, piece, col, row, new Bishop(board, piece.col, piece.row, piece.isWhite)));
                                    moves.add(new Move(this.board, piece, col, row, new Knight(board, piece.col, piece.row, piece.isWhite)));
                                    
                                } else {

                                    moves.add(new Move(this.board, piece, col, row, null));

                                }
                            }
                        }
                    }
                }
            }
        }

        board.selectedPiece = null;
        return moves;

    }

    public int moveGenerationTest(int depth) {
        if (depth == 0) {
            return 1;
        }

        ArrayList<Move> moves = findValidMoves();

        int totalPositions = 0;

        for (Move move : moves) {

            board.makeMove(move);

            totalPositions += moveGenerationTest(depth - 1);

            board.unMakeMove(move);

        }

        return totalPositions;
    }
}
