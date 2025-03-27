package parkerbasicchessengine.Chess_Engines.parkerfish_v2;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

import parkerbasicchessengine.IChessGameInput;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.BoardFactory;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.ISearch;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.Minimax;
import parkerbasicchessengine.Utils.MoveConversion;
import parkerbasicchessengine.Utils.PieceCoordinateConversion;

public class parkerfish_v2 implements IChessGameInput {

    // Redesigning my engine from the ground up since it became too unwieldy from yoloing every part of it 

    // This class should manage which type of move generation (Minimax, Iterative Deepening, Opening Book, etc)

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

    public void printBoard() {
        this.searchAlgorithm.printBoard();
    }

    public String engineMakeMove() {
        System.out.println("Engine making move...");
        Move move = this.searchAlgorithm.makeMove();
        // Convert to coordinate
        return MoveConversion.parkerfish_v2(move);
    }

    public void syncEngineBoard(String move) {
        searchAlgorithm.makeMove(move);
    }

    private int convertPieceStringToType(String piece) {

        if(piece == null) {
            return NULL_PIECE;
        }

        if(piece.equals("K")){
            return K;
        } else if (piece.equals("Q")) {
            return Q;
        } else if (piece.equals("P")) {
            return P;
        } else if (piece.equals("R")) {
            return R;
        } else if (piece.equals("B")) {
            return B;
        } else if (piece.equals("N")) {
            return N;
        } else {
            return NULL_PIECE;
        }

    }

    @Override
    public String getFenString() {
        return board.fenString();
    }
}
