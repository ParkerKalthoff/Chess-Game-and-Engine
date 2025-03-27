package parkerbasicchessengine.Chess_Engines.parkerfish_v2.search;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;

public interface ISearch {

    public Move makeMove();

    public void makeMove(String move);

    public void printBoard();

    public void setBoard(Board board);
}
