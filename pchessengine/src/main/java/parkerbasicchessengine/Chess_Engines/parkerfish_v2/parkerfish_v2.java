package parkerbasicchessengine.Chess_Engines.parkerfish_v2;

import parkerbasicchessengine.IChessGameInput;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.BoardFactory;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.ISearch;
import parkerbasicchessengine.Utils.MoveConversion;

public class parkerfish_v2 implements IChessGameInput {

    // Redesigning my engine from the ground up since it became too unwieldy from yoloing every part of it 

    // This class should manage which type of move generation (Minimax, Iterative Deepening, Opening Book, etc)

    private ISearch searchAlgorithm;
    public Board board;
    public String name;

    public parkerfish_v2(ISearch searchAlgorithm, String fenString) {
        // load position
        this.board = BoardFactory.createBoard(fenString);
        this.searchAlgorithm = searchAlgorithm;
        this.searchAlgorithm.setBoard(board);
    } 

    public parkerfish_v2(ISearch searchAlgorithm) {
        // new game
        this.board = BoardFactory.createBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        
        this.searchAlgorithm = searchAlgorithm;
        this.searchAlgorithm.setBoard(board);
    }

    public void loadPosition(String fenString) {

        this.board = BoardFactory.createBoard(fenString);
        searchAlgorithm.setBoard(board);

    }

    public void printBoard() {
        this.searchAlgorithm.printBoard();
    }

    public String engineMakeMove() {
        System.out.println("Engine making move...");
        Move move = this.searchAlgorithm.makeMove();
        // Convert to coordinate

        if(move == null) {
            return null;
        }

        return MoveConversion.parkerfish_v2(move);
    }

    public void syncEngineBoard(String move) {
        searchAlgorithm.makeMove(move);
    }

    @Override
    public String getFenString() {
        return board.fenString();
    }

    public String getName() {
        return this.name;
    }
}
