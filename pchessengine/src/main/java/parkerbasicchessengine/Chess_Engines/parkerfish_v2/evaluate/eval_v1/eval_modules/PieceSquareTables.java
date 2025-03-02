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

        int friendlyTeam = board.isWhitesTurn ? White : Black;
        int enemyTeam = board.isWhitesTurn ? Black : White;

        int eval = 0;

        eval += evaluatePawns(friendlyTeam);
        eval += evaluateBishops(friendlyTeam);
        eval += evaluateKnights(friendlyTeam);
        eval += evaluateQueen(friendlyTeam);
        eval += evaluateRook(friendlyTeam);
        eval += evaluateKing(friendlyTeam);

        eval -= evaluatePawns(enemyTeam);
        eval -= evaluateBishops(enemyTeam);
        eval -= evaluateKnights(enemyTeam);
        eval -= evaluateQueen(enemyTeam);
        eval -= evaluateRook(enemyTeam);
        eval -= evaluateKing(enemyTeam);

        return eval;

    }

    private int evaluatePawns(int team){
        return evaluatePiece(team, P, pawn_score);
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

        if(team == White){
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


        float rawEndgameWeight = (EndgameUpperBound - combinedPieceScore) / (EndgameUpperBound - EndgameLowerBound);
        float endgameWeight = Math.max(0, Math.min(1, rawEndgameWeight));

        return endgameWeight;
    }

    private int evaluateKing(int team){

        int kingIndex = Long.numberOfTrailingZeros(board.bitboards[team][K]);

        if(kingIndex == 64) { // no king
            return 0;
        }

        if(team == White){
            kingIndex = Flip[kingIndex];
        }

        float endgameWeight = endGameWeight();
        int score = 0;

        long kingQueenSight = MagicBitboards.generateQueenMovementBitboard(kingIndex, board.getAllPieces());
        int kingExposedSquares = Long.bitCount(kingQueenSight) * 2;

        score -= (1 - endgameWeight) * kingExposedSquares * 2;

        score += endgameWeight * king_endgame_score[kingIndex];

        return score;
    }

}
