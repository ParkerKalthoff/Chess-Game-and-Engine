package parkerbasicchessengine;

public interface IChessGameInput {

    String engineMakeMove();

    void syncEngineBoard(String move);

    String getFenString();

    void printBoard();

    void loadPosition(String fenString);

    String getName();
}
