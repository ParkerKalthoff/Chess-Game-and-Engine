package parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.eval_modules;
import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen.HelperFunctions;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen.MagicBitboards;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.evaluate.eval_v1.eval_modules.utils.PieceSquares.*;

public class PieceSquareTables implements IEvaluationModule{

    private static final int EndgameUpperBound = 4800;
    private static final int EndgameLowerBound = 600;

    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

    public int eval() {

        
        float endGameWeight = endGameWeight();

        int eval = 0;

        eval += evaluatePawns(White, endGameWeight);
        eval += evaluateBishops(White);
        eval += evaluateKnights(White);
        eval += evaluateQueen(White);
        eval += evaluateRook(White);
        eval += evaluateKing(White, endGameWeight);

        eval -= evaluatePawns(Black, endGameWeight);
        eval -= evaluateBishops(Black);
        eval -= evaluateKnights(Black);
        eval -= evaluateQueen(Black);
        eval -= evaluateRook(Black);
        eval -= evaluateKing(Black, endGameWeight);

        return eval;

    }

    private int evaluateBishops(int team){
        return evaluatePiece(team, B, bishop_score);
    }

    private int evaluateKnights(int team){
        return evaluatePiece(team, N, knight_score);
    }
    
    private int evaluateQueen(int team){
        return evaluatePiece(team, Q, queen_score);
    }

    private int evaluateRook(int team){
        return evaluatePiece(team, R, rook_score);
    }

    private int evaluatePiece(int team, int pieceType, int[] evaluateBoard){

        long pieces = board.bitboards[team][pieceType];

        int indexes[] = HelperFunctions.bitboardToArray(pieces);

        if(team == Black){
            for(int index : indexes){
                index = Flip[index];
            }
        }

        int score = 0;

        for(int index : indexes){
            score += evaluateBoard[index];
        }

        return score;
    }

    /**
     * 
     * A function that indicates how pieces should behave based on material conditions on the board,
     *  with values closer to 0.0 indicating game is still in opening / midgame, and closer to 1.0 when game is in endgame / late midgame
     * 
     * @return a value between 0.0 - 1.0, with 0.0 being the not endgame and 1.0 being in an endgame
     */
    private float endGameWeight(){

        int combinedPieceScore = 0;

        combinedPieceScore += Long.bitCount(board.bitboards[White][Q]) * 900;
        combinedPieceScore += Long.bitCount(board.bitboards[White][B]) * 300;
        combinedPieceScore += Long.bitCount(board.bitboards[White][N]) * 300;
        combinedPieceScore += Long.bitCount(board.bitboards[White][R]) * 500;
        combinedPieceScore += Long.bitCount(board.bitboards[White][P]) * 100;

        combinedPieceScore += Long.bitCount(board.bitboards[Black][Q]) * 900;
        combinedPieceScore += Long.bitCount(board.bitboards[Black][B]) * 300;
        combinedPieceScore += Long.bitCount(board.bitboards[Black][N]) * 300;
        combinedPieceScore += Long.bitCount(board.bitboards[Black][R]) * 500;
        combinedPieceScore += Long.bitCount(board.bitboards[Black][P]) * 100;

        // Fixed issue where Im dividing ints and then casting it into float, basically not doing anything
        float rawEndgameWeight = (float)(EndgameUpperBound - combinedPieceScore) / (EndgameUpperBound - EndgameLowerBound);
        float endgameWeight = Math.max(0.0f, Math.min(1.0f, rawEndgameWeight));


        return endgameWeight;

    }

    private int evaluateKing(int team, float endgameWeight){

        int kingIndex = Long.numberOfTrailingZeros(board.bitboards[team][K]);

        if(kingIndex == 64) {
            return 0;
        }

        if(team == Black){
            kingIndex = Flip[kingIndex];
        }

        int score = 0;

        long kingQueenSight = MagicBitboards.generateQueenMovementBitboard(kingIndex, board.getAllPieces());
        int kingExposedSquares = Long.bitCount(kingQueenSight) * 2;

        // If you arent in an endgame, you want to punish king being exposed
        score -= (1 - endgameWeight) * kingExposedSquares * 2;

        // If you are in an endgame you want king to be encouraged to take space
        score += endgameWeight * king_endgame_score[kingIndex];

        return score;
    
    }

    private int evaluatePawns(int team, float endgameWeight) {

        long pieces = board.bitboards[team][P];

        int indexes[] = HelperFunctions.bitboardToArray(pieces);

        if(team == Black){
            for(int index : indexes){
                index = Flip[index];
            }
        }

        int score = 0;

        for(int index : indexes){
            //  If you arent in an endgame, use pawn start
            score += (1 - endgameWeight) * pawn_start[index];
            // In endgame use pawn end
            score += (endgameWeight) * pawn_end[index];
        }

        return score;
    }

}