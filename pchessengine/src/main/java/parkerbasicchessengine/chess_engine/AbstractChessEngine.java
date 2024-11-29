package parkerbasicchessengine.chess_engine;

import java.util.Random;

import parkerbasicchessengine.Board;

abstract class AbstractChessEngine {

    // used to simplify making new boards by implimenting basic functions

    private Board board; // for interfacing with the game

    public String engineName;

    
    public final int positiveInfinity = 9999999;
    public final int negativeInfinity = -positiveInfinity;

    
}
