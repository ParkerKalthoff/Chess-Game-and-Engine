package parkerbasicchessengine.Chess_Engines.parkerfish_v1;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import parkerbasicchessengine.Board;
import parkerbasicchessengine.Chess_Engines.AbstractChessEngine;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.MoveGenerator;
import parkerbasicchessengine.Move;

public class parkerfish_v1 extends AbstractChessEngine {

    private MoveGenerator moveGenerator;
    private Board board;
    public BitwiseBoard bwB; 

    public parkerfish_v1(Board board){

        System.out.println(
            """
             (                                (                                  \r
             )\\ )               )             )\\ )             )              )  \r
            (()/(    )  (    ( /(    (   (   (()/(  (       ( /(   (   (   ( /(  \r
             /(_))( /(  )(   )\\())  ))\\  )(   /(_)) )\\  (   )\\())  )\\  )\\  )\\()) \r
            (_))  )(_))(()\\ ((_)\\  /((_)(()\\ (_))_|((_) )\\ ((_)\\  ((_)((_)((_)\\  \r
            | _ \\((_)_  ((_)| |(_)(_))   ((_)| |_   (_)((_)| |(_) \\ \\ / /  / (_) \r
            |  _// _` || '_|| / / / -_) | '_|| __|  | |(_-<| ' \\   \\ V /   | |   \r
            |_|  \\__,_||_|  |_\\_\\ \\___| |_|  |_|    |_|/__/|_||_|   \\_/    |_|   \r""" 
        );

        this.board = board;

        System.out.println("board");

        this.bwB = new BitwiseBoard(board.convertPostionToFEN());

        System.out.println("bwb");

        this.moveGenerator = new MoveGenerator(this.bwB);

        System.out.println("move gen");
        
    }


    Scanner scanner = new Scanner(System.in);
    
    // minimax algorithm
    // generates all positions and when depth is at zero does an evaluation on all positions
    // and returns the best move
    // alpha beta pruning speeds up this process
    //
    //                         white start
    //                          /        \
    //                    black move    black move
    //                   /        \    /          \
    //  White Moves ->  3         -1  4            5
    //
    //  In this example, after white makes its initial move, black has a choice
    //  the left node, after blacks move, white can get a position of 3 or -1 (higher is better),
    //  but on the right side, after blacks move, white can get a position of 4 or 5
    //  'Alpha Beta Pruning' comes in and says, if we know white can choose to get a position of 4
    //   Why bother the rest of that node? From blacks perspective, the left node is the best choice.
    //
    //   This algorithm is most effective when the evaluations of the moves are already ordered before
    //   getting the evaluations, since its expensive to actually generate them and would defeat the purpose,
    //   I do a 'move ordering' algorithm that does a super basic scoring of each move, so I give :
    //   promotions a boost, a lower value piece capturing a higher value piece a boost, and lower the score
    //   for moves that put pieces in front of enemy pawns. 

    public void makeMove() {
        BitwiseMove[] legalMoves = moveGenerator.generateMoves();
    
        if (legalMoves.length == 0) {
            if (legalMoves.length == 0 /* <--- fake code - Player in check */) {
                System.out.println("Checkmate!");
            } else {
                System.out.println("Stalemate!");
            }
            return;
        }
    
        int depth = 3; 
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        BitwiseMove bestMove = null;
        int bestEval = Integer.MIN_VALUE;
    
        for (BitwiseMove move : legalMoves) {

            byte preMoveCastlingRights = bwB.castlingRights;
            int capturePieceType = bwB.movePiece(move);

            System.out.println(bwB.bitboardsToString()); 
            int eval = -search(depth - 1, -beta, -alpha);

            bwB.unmovePiece(move, capturePieceType, preMoveCastlingRights);
    
            if (eval > bestEval) {
                bestEval = eval;
                bestMove = move;
            }
        }
    
        if (bestMove != null) {
            bwB.movePiece(bestMove);

            int oldCol = bestMove.getFromSquare() % 8;
            int oldRow = bestMove.getFromSquare() / 8;
            int newCol = bestMove.getToSquare() % 8;
            int newRow = bestMove.getToSquare() / 8;

            board.makeMove(new Move(board, board.getPiece(oldCol, oldRow), newCol, newRow));
            System.out.println("Best move: " + bestMove);
        }
    }

    private int search(int depth, int alpha, int beta){

        

        if(depth == 0){
            return evaluate();
        }

        BitwiseMove moves[] = moveGenerator.generateMoves();

        if(moves.length == 0){

            if(false /* Player in Check*/){
                return negativeInfinity;
            }
            
            return 0;

        }

        moves = orderMoves(moves);

        for(BitwiseMove move : moves){

            byte preMoveCastlingRights = bwB.castlingRights;
            int capturePieceType = bwB.movePiece(move);

            
            System.out.println(bwB.bitboardsToString()); 

            scanner.nextLine();

            int eval = -search(depth - 1, -beta, -alpha);
            bwB.unmovePiece(move, capturePieceType, preMoveCastlingRights);

            if(eval >= beta){
                // determines if opponent will likely avoid position
                return beta;
            }

            alpha = Math.max(alpha, eval);

        }

        return alpha;
    }

    public BitwiseMove[] orderMoves(BitwiseMove[] moves){
        
        // alpha beta pruning is most effective when best moves are searched first and worst last
        // this function give an extremely rough eval to each position to then optimize the search function

        long enemyPawns = bwB.piece_bitboards[!bwB.isWhiteToMove ? 0 : 1][5];
        long enemyPawnVision = (enemyPawns << 7) & enemyPawns & ~ (Constants.FILE_H) | (enemyPawns << 9) & enemyPawns & ~ (Constants.FILE_A);

        Map<BitwiseMove, Integer> moveScores = new HashMap<>();

        for(BitwiseMove move : moves){

            int moveScoreGuess = 0;

            int movePieceType = bwB.getPieceType(move.getFromSquare());

            int toSquare = move.getToSquare();

            int capturePieceType = bwB.getPieceType(toSquare);

            boolean canBeCapturedByEnemyPawns = ((enemyPawnVision & (1L << toSquare)) != 0);
            
            if(capturePieceType != -1){
                // priortize good captures
                moveScoreGuess = 10 * bwB.getPieceValue(capturePieceType) - bwB.getPieceValue(movePieceType);
            }

            if(move.isPromoting()){
                moveScoreGuess += bwB.getPieceValue(move.getFlag());
            }

            if(canBeCapturedByEnemyPawns){
                moveScoreGuess -= bwB.getPieceValue(move.getToSquare());
            }
            moveScores.put(move, moveScoreGuess);
        }

    // using a stream to convert hashmap to sorted array
    return moveScores.entrySet()
            .stream()
            .sorted((move1, move2) -> Integer.compare(move2.getValue(), move1.getValue()))
            .map(Map.Entry::getKey)
            .toArray(BitwiseMove[]::new);
    }

    public int evaluate(){
        
        // using a placeholder of checking material difference

        // plan to account for piece placement, connected pawns, is castled weighted by remaining material

        int whiteScore = 0;
        int blackScore = 0;
        
        whiteScore += Long.bitCount(bwB.piece_bitboards[0][0]) * 999999;
        whiteScore += Long.bitCount(bwB.piece_bitboards[0][1]) * 900;
        whiteScore += Long.bitCount(bwB.piece_bitboards[0][2]) * 300;
        whiteScore += Long.bitCount(bwB.piece_bitboards[0][3]) * 300;
        whiteScore += Long.bitCount(bwB.piece_bitboards[0][4]) * 500;
        whiteScore += Long.bitCount(bwB.piece_bitboards[0][5]) * 100;

        blackScore += Long.bitCount(bwB.piece_bitboards[1][0]) * 999999;
        blackScore += Long.bitCount(bwB.piece_bitboards[1][1]) * 900;
        blackScore += Long.bitCount(bwB.piece_bitboards[1][2]) * 300;
        blackScore += Long.bitCount(bwB.piece_bitboards[1][3]) * 300;
        blackScore += Long.bitCount(bwB.piece_bitboards[1][4]) * 500;
        blackScore += Long.bitCount(bwB.piece_bitboards[1][5]) * 100;

        return whiteScore - blackScore;
    }

}
