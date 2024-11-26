package parkerbasicchessengine.chess_engine;

import parkerbasicchessengine.Board;

abstract class abstract_chess_engine {

    private Board board;
    public int depth;
    public String engineName;

    public final int positiveInfinity = 9999999;
    public final int negativeInfinity = -positiveInfinity;

}
