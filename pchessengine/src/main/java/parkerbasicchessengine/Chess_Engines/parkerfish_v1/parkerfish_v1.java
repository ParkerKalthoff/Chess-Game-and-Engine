package parkerbasicchessengine.Chess_Engines.parkerfish_v1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import parkerbasicchessengine.Board;
import parkerbasicchessengine.Chess_Engines.AbstractChessEngine;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.MoveGenerator;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.opening_book.OpeningBook;
import parkerbasicchessengine.Move;

import static parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants.*;

public class parkerfish_v1 extends AbstractChessEngine {

    private MoveGenerator moveGenerator;
    private Evaluate evaluate;
    private Board board;
    public BitwiseBoard bwB;

    private OpeningBook oB;
    private boolean inOpening = true;

    private Scanner scanner;

    public parkerfish_v1(Board board) {

        System.out.println(
                """
                         (                                (                                  \r
                         )\\ )               )             )\\ )             )              )  \r
                        (()/(    )  (    ( /(    (   (   (()/(  (       ( /(   (   (   ( /(  \r
                         /(_))( /(  )(   )\\())  ))\\  )(   /(_)) )\\  (   )\\())  )\\  )\\  )\\()) \r
                        (_))  )(_))(()\\ ((_)\\  /((_)(()\\ (_))_|((_) )\\ ((_)\\  ((_)((_)((_)\\  \r
                        | _ \\((_)_  ((_)| |(_)(_))   ((_)| |_   (_)((_)| |(_) \\ \\ / /  / (_) \r
                        |  _// _` || '_|| / / / -_) | '_|| __|  | |(_-<| ' \\   \\ V /   | |   \r
                        |_|  \\__,_||_|  |_\\_\\ \\___| |_|  |_|    |_|/__/|_||_|   \\_/    |_|   \r""");

        this.board = board;

        this.bwB = new BitwiseBoard(board.convertPostionToFEN());

        this.moveGenerator = new MoveGenerator(this.bwB);
        this.evaluate = new Evaluate(this.bwB);
        this.oB = new OpeningBook();

        this.scanner = new Scanner(System.in);
    }

    // minimax algorithm
    // generates all positions and when depth is at zero does an evaluation on all
    // positions
    // and returns the best move
    // alpha beta pruning speeds up this process
    //
    //                  white start
    //                /      \
    //         black move black move
    //                / \ / \
    // White Moves -> 3 -1 4 5
    //
    // In this example, after white makes its initial move, black has a choice
    // the left node, after blacks move, white can get a position of 3 or -1 (higher
    // is better),
    // but on the right side, after blacks move, white can get a position of 4 or 5
    // 'Alpha Beta Pruning' comes in and says, if we know white can choose to get a
    // position of 4
    // Why bother the rest of that node? From blacks perspective, the left node is
    // the best choice.
    //
    // This algorithm is most effective when the evaluations of the moves are
    // already ordered before
    // getting the evaluations, since its expensive to actually generate them and
    // would defeat the purpose,
    // I do a 'move ordering' algorithm that does a super basic scoring of each
    // move, so I give :
    // promotions a boost, a lower value piece capturing a higher value piece a
    // boost, and lower the score
    // for moves that put pieces in front of enemy pawns.

    public void makeMove() {

        BitwiseMove[] legalMoves = moveGenerator.generateMoves();

        if (legalMoves.length == 0) {
            if (moveGenerator.activeKingInCheck) {
                System.out.println("Checkmate!");
            } else {
                System.out.println("Stalemate!");
            }
            return;
        }

        int depth = 3;
        int alpha = -9999999;
        int beta = 9999999;
        BitwiseMove bestMove = null;
        int bestEval = Integer.MIN_VALUE;

        
        String boardFenString = this.bwB.toFenString();
        System.out.println(boardFenString);

        if (inOpening) {

            if (bwB.fullMoveCounter > 7) {

                this.inOpening = false;

            } else {

                bestMove = this.oB.getRandomMove(boardFenString);

                System.out.println(bwB.bitboardsToString());

                if (bestMove == null) {
                    this.inOpening = false;
                }

            }

        }

        if (bestMove == null) {

            scanner.nextLine();

            // Best move being null means either the game is out of the opening or theres
            // not
            // a position in file (even if the move is right in the opening)

            for (BitwiseMove move : legalMoves) {

                byte preMoveCastlingRights = bwB.castlingRights;

                int previousHalfMoveClock = this.bwB.halfMoveClock;

                int capturePieceType = -1;

                try{
                    capturePieceType = bwB.movePiece(move);
                } catch(ArrayIndexOutOfBoundsException e){
                    System.out.println(bwB.toFenString());
                    System.out.println("Moves : ");
                    for(BitwiseMove m : legalMoves){
                        System.out.println(" - "+m);
                    throw e;
                    }

                    System.out.println("\n Move caused error :" + move);
                }

                int eval = search(depth - 1, alpha, beta);

                bwB.unmovePiece(move, capturePieceType, preMoveCastlingRights, previousHalfMoveClock);

                if (eval > bestEval) {
                    bestEval = eval;
                    bestMove = move;
                }
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
        } else {
            System.out.println("null move");
        }
    }

    private int search(int depth, int alpha, int beta) {

        long white_king_bitboard = this.bwB.piece_bitboards[White][K];
        long black_king_bitboard = this.bwB.piece_bitboards[Black][K];

        if (depth == 0 || white_king_bitboard == 0L || black_king_bitboard == 0L) {
            // base case, eval at depth 0, also adding safeguard if king is captured
            return evaluate();
        }

        BitwiseMove moves[] = moveGenerator.generateMoves();

        if (moves.length == 0) {

            if (moveGenerator.activeKingInCheck) { // king in check with 0 moves means game over
                System.out.println("king in check");
                return negativeInfinity;
            } else {
                return 0; // stalemate so tie
            }
        }

        moves = orderMoves(moves);

        System.out.println("Moves to be processed :");
        for (BitwiseMove move : moves) {
            System.out.println("\t - "+move);
        }

        for (BitwiseMove move : moves) {

            byte preMoveCastlingRights = bwB.castlingRights;
            int previousHalfMoveClock = bwB.halfMoveClock;

            long oldBitboards[][] = new long[2][6];


            oldBitboards[0] = bwB.piece_bitboards[0].clone();
            oldBitboards[1] = bwB.piece_bitboards[1].clone();

            int capturePieceType = bwB.movePiece(move);

            System.out.println(bwB.bitboardsToString());
            new Scanner(System.in).nextLine();

            long inoperationBitboard[][] = new long[2][6];            
            inoperationBitboard[0] = bwB.piece_bitboards[0].clone();
            inoperationBitboard[1] = bwB.piece_bitboards[1].clone();

            try{
                int eval = -search(depth - 1, -beta, -alpha);
                
                if (eval >= beta) {
                    // determines if opponent will likely avoid position (from bots perspective)
                    return beta;
                }
    
                alpha = Math.max(alpha, eval);

            } catch (Exception e){ 
                System.out.println("=======================");
                System.out.println(e);
                System.out.println("  -- Error on move --  ");
                System.out.println(" - Depth : "+depth);
                System.out.println();
                System.out.println(" - move considered : " + move.getFromSquare() + " to " + move.getToSquare() + " : " + move.getFlag());
                System.out.println(move);
                System.out.println("Intended Piece : "+move.source);
                System.out.println();
                System.out.println("Half move clock : "+ previousHalfMoveClock);
                System.out.println(bwB.bitboardsToString());
                System.out.println();
                System.out.println(" - Evaluation : " + this.evaluate());
                System.out.println("   - Alpha : "+ alpha);
                System.out.println("   - Beta : "+ beta);
            }
            
            bwB.unmovePiece(move, capturePieceType, preMoveCastlingRights, previousHalfMoveClock);

            if(!Arrays.deepEquals(oldBitboards, bwB.piece_bitboards)){

                System.out.println("----------");
                System.out.println("pre \n");
                System.out.println(BitwiseBoard.bitboardsToString(oldBitboards));

                System.out.println(move);

                System.out.println("infix \n");
                System.out.println(BitwiseBoard.bitboardsToString(inoperationBitboard));

                System.out.println(move);

                System.out.println("post \n");
                System.out.println(bwB.bitboardsToString());

                System.out.println("----------");

                //throw new RuntimeErrorException(null, "Runtime Error : Pre Move and Post Undo Move Bitboards misalign");
            }

        }
        return alpha;
    }

    public BitwiseMove[] orderMoves(BitwiseMove[] moves) {

        // alpha beta pruning is most effective when best moves are searched first and
        // worst last
        // this function give an extremely rough eval to each position to then optimize
        // the search function

        MoveGenerator moveGen = new MoveGenerator(this.bwB);

        long enemyPawnVision = moveGen.generatePawnRightAttacksBitboards(false);
        enemyPawnVision |= moveGen.generatePawnLeftAttacksBitboards(false);

        Map<BitwiseMove, Integer> moveScores = new HashMap<>();

        for (BitwiseMove move : moves) {

            int moveScoreGuess = 0;

            int movePieceType = bwB.getPieceType(move.getFromSquare());

            // System.out.println(movePieceType);

            int toSquare = move.getToSquare();

            int capturePieceType = bwB.getPieceType(toSquare);

            boolean canBeCapturedByEnemyPawns = (enemyPawnVision & (1L << toSquare)) != 0L;

            if (capturePieceType != -1) {
                // priortize good captures
                moveScoreGuess = 10 * bwB.getPieceValue(capturePieceType) - bwB.getPieceValue(movePieceType);
            }

            if (move.isPromoting()) {
                moveScoreGuess += bwB.getPieceValue(move.getFlag());
            }

            if (canBeCapturedByEnemyPawns) {
                moveScoreGuess -= bwB.getPieceValue(movePieceType);
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

    public int evaluate() {
        return evaluate.getEval();
    }

}
