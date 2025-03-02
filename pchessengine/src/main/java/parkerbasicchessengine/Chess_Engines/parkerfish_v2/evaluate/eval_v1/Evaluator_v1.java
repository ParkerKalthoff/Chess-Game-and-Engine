package parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.IEvaluate;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.eval_modules.IEvaluationModule;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.eval_modules.MaterialEval;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.eval_modules.PieceSquareTables;

public class Evaluator_v1 implements IEvaluate{

    private IEvaluationModule evalModules[] = {
        new MaterialEval(),
        new PieceSquareTables()
    };

    private Board board;

    public Evaluator_v1(Board board) {

        this.board = board;

        for(IEvaluationModule evalModule : evalModules){
            evalModule.setBoard(board);
        }

    }
    
    public int evaluate() {

        int eval = 0;

        // using a numeric for loop since theres a slight performance boost
        for(int i = 0; i < evalModules.length; i++){
            eval += evalModules[i].eval();
        }

        if(board.isWhitesTurn) {
            return eval;
        } else {
            return - eval;
        }

    }

}
