package parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.eval_modules;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;

public interface IEvaluationModule {

    public int eval();

    
    public void setBoard(Board board);

}
