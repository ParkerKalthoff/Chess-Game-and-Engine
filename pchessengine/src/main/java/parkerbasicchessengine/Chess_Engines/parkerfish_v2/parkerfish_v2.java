package parkerbasicchessengine.Chess_Engines.parkerfish_v2;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.BoardFactory;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.ISearch;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.Minimax;

public class parkerfish_v2 {

    // Redesigning my engine from the ground up since it became too unwieldy from yoloing every part of it 

    // This class should manage which type of move generation (Minimax, Iterative Deepening, Opening Book, etc)
    // using that to speak my board class

    private ISearch searchAlgorithm;
    public Board board;

    public parkerfish_v2(String fenString) {
        // load position
        this.board = BoardFactory.createBoard(fenString);
        this.searchAlgorithm = new Minimax(board);
    
    } 

    public parkerfish_v2() {
        // new game
        this.board = BoardFactory.createBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        this.searchAlgorithm = new Minimax(board);
    } 

    public void makeMove() {
        this.searchAlgorithm.makeMove();
    }

    public void printBoard() {
        this.searchAlgorithm.printBoard();
    }

}
