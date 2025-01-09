package parkerbasicchessengine.Chess_Engines.parkerfish_v1;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.BitboardToArrayIndicies;

import static parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants.*;
import static parkerbasicchessengine.Chess_Engines.ChessEngineUtils.EvalBoards.*;


public class Evaluate {

    private BitwiseBoard bwB;

    final int EndgameUpperBound = 48;
    final int EndgameLowerBound = 6;

    public Evaluate(BitwiseBoard bwB){
        this.bwB = bwB;
    }

    public int getEval(){

        int whiteScore = 0;
        int blackScore = 0;

        whiteScore += getMaterialScore(White);
        blackScore += getMaterialScore(Black);

        whiteScore += evaluateBishops(White);
        blackScore += evaluateBishops(Black);

        whiteScore += evaluateKnights(White);
        blackScore += evaluateKnights(Black);

        whiteScore += evaluateRook(White);
        blackScore += evaluateRook(Black);
        
        whiteScore += evaluateQueen(White);
        blackScore += evaluateQueen(Black);
        
        whiteScore += evaluateKing(White);
        blackScore += evaluateKing(Black);

        whiteScore += evaluatePawns(White);
        blackScore += evaluatePawns(Black);

        return whiteScore - blackScore;
    }

    private int getMaterialScore(int team){
        
        int score = 0;

        score += Long.bitCount(bwB.piece_bitboards[team][K]) * 999999;
        score += Long.bitCount(bwB.piece_bitboards[team][Q]) * 900;
        score += Long.bitCount(bwB.piece_bitboards[team][N]) * 300;
        score += Long.bitCount(bwB.piece_bitboards[team][B]) * 300;
        score += Long.bitCount(bwB.piece_bitboards[team][R]) * 500;
        score += Long.bitCount(bwB.piece_bitboards[team][P]) * 100;

        return score;
    }

    private int getMaterialScoreWithoutKing(int team){
        return getMaterialScore(team) - 999999;
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

        long pieces = bwB.piece_bitboards[team][pieceType];

        int indexes[] = BitboardToArrayIndicies.bitboardToIndicies(pieces);

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

    private float endGameWeight(){

        int combinedPieceScore = getMaterialScoreWithoutKing(White) + getMaterialScoreWithoutKing(Black);

        float rawEndgameWeight = (EndgameUpperBound - combinedPieceScore) / (EndgameUpperBound - EndgameLowerBound);
        float endgameWeight = Math.max(0, Math.min(1, rawEndgameWeight));

        return endgameWeight;
    }

    private int evaluateKing(int team){

        int kingIndex = Long.numberOfTrailingZeros(bwB.piece_bitboards[team][K]);

        if(kingIndex == 64){ // no king
            return 0;
        }

        float endgameWeight = endGameWeight();
        int score = 0;

        score += (1 - endgameWeight) * king_score[kingIndex];
        score += endgameWeight * king_endgame_score[kingIndex];

        return score;
    }



}
