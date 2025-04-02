package parkerbasicchessengine.Chess_Engines.parkerfish_v2;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.Minimax;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.QuiescenceMM;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.IEvaluate;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.Evaluator_v1;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.ISearch;

public class chessEngineFactory {

    public static parkerfish_v2 Parkerfish_Minimax_v2() {

        IEvaluate evaluator = new Evaluator_v1();

        ISearch searchAlgorithm = new Minimax(evaluator);

        parkerfish_v2 engine = new parkerfish_v2(searchAlgorithm);
        
        engine.name = "Parkerfish MiniMax v1";

        return engine;
    }

    public static parkerfish_v2 Parkerfish_Quiescence_v2() {

        IEvaluate evaluator = new Evaluator_v1();

        ISearch searchAlgorithm = new QuiescenceMM(evaluator);

        parkerfish_v2 engine = new parkerfish_v2(searchAlgorithm);
        
        engine.name = "Parkerfish Qui. v1";

        return engine;
    }
}
