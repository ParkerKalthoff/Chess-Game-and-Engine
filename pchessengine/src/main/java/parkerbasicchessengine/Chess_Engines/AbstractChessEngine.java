package parkerbasicchessengine.Chess_Engines;

import parkerbasicchessengine.Board;

public abstract class AbstractChessEngine {

    // used to simplify making new boards by implimenting basic functions

    public Board board; // for interfacing with the game

    public String engineName = "Parkers Engine";

    
    public final int positiveInfinity = 9999999;
    public final int negativeInfinity = -positiveInfinity;

    
}
