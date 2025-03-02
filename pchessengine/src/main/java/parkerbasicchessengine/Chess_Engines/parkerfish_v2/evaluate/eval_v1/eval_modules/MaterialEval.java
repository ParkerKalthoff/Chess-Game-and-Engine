package parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.eval_modules;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

public class MaterialEval implements IEvaluationModule {

    private Board board;

    public int eval() {

        int eval = 0;

        eval += Long.bitCount(board.bitboards[White][K]) * 99999;
        eval += Long.bitCount(board.bitboards[White][Q]) * 900;
        eval += Long.bitCount(board.bitboards[White][B]) * 300;
        eval += Long.bitCount(board.bitboards[White][N]) * 300;
        eval += Long.bitCount(board.bitboards[White][R]) * 500;
        eval += Long.bitCount(board.bitboards[White][P]) * 100;

        eval -= Long.bitCount(board.bitboards[Black][K]) * 99999;
        eval -= Long.bitCount(board.bitboards[Black][Q]) * 900;
        eval -= Long.bitCount(board.bitboards[Black][B]) * 300;
        eval -= Long.bitCount(board.bitboards[Black][N]) * 300;
        eval -= Long.bitCount(board.bitboards[Black][R]) * 500;
        eval -= Long.bitCount(board.bitboards[Black][P]) * 100;

        return eval;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

}