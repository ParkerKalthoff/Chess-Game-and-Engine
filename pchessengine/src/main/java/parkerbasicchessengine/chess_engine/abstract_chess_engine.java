package parkerbasicchessengine;
import parkerbasicchessengine.Board;

abstract class abstract_chess_engine {

    private Board board;
    public int depth;
    public final String engineName;

    public final int positiveInfinity = 9999999;
    public final int negativeInfinity = -positiveInfinity;

}
