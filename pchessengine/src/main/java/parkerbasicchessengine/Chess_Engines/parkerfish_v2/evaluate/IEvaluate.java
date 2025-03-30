package parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;

public interface IEvaluate {

    public int evaluate();

    public void setBoard(Board board);
}
