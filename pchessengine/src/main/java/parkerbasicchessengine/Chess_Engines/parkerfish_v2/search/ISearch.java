package parkerbasicchessengine.Chess_Engines.parkerfish_v2.search;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;

public interface ISearch {

    public Move makeMove();

    public void makeMove(Move move);

    public void printBoard();
}
