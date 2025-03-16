package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen;
import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

import java.util.ArrayList;
import java.util.Arrays;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move;



public class MoveGenerator {

    // This class is meant to generate move objects for legal moves

    private Board board;
    private BitboardGenerators bitboardGenerators;

    private ArrayList<Move> moveList;

    // An array that stores a valid pin mask of moves
    // So if a bishop was pinned to a king by another bishop, they should still be able to move along that axis
    // If a rook was pinned instead, movement should be restricted
    // This should be cleared after the board switches the position, or atleast after all moves are generated
    private long pins[]; 

    // If multiple checks on king, this should be used to skip move gen
    public int checkCount;
    
    // a Mask to be able to quickly mask out moves that dont prevent check for non king pieces
    // Starts as a full bitboard, and then will be 'and'ed when pins come up
    // TODO : switch back to private after testing
    public long checkMask;

    private long enemyVision;

    public MoveGenerator(Board board) {

        this.board = board;
        this.bitboardGenerators = new BitboardGenerators(board); 

    }

    private void resetState() {

        this.pins = new long[64];
        Arrays.fill(this.pins, 0xFFFFFFFFFFFFFFFFL);

        this.checkCount = 0;
        this.checkMask = 0xFFFFFFFFFFFFFFFFL;
        this.enemyVision = 0L;

    }

    public ArrayList<Move> generateMoves() {

        
        this.moveList = new ArrayList<>();

        resetState();

        int team = this.board.getActiveTeam();
        int enemyTeam = this.board.getInactiveTeam();

        updateOpponentCoverage(enemyTeam);

        generateFriendlyTeamMoves(team);

        return moveList;

    }

    private void generateFriendlyTeamMoves(int team) {

        generateFriendlyKingNormalMoves(team);

        if(this.checkCount >= 2) {
            // Double check means only king can move, so we can skip move generation steps
            return;
        }

        generateCastlingKingMoves(team);

        generateMajorMinorPieceMoves(team, Q);
        generateMajorMinorPieceMoves(team, R);        
        generateMajorMinorPieceMoves(team, N);
        generateMajorMinorPieceMoves(team, B);
        generatePawnMoves(team);

    }

    private void generatePawnMoves(int team) {
        
        boolean isWhite = team == White;

        long rightAttacks = bitboardGenerators.pawnRightAttackMoves(team);

        long rightAttacksPromoting = bitboardGenerators.pawnGetPromotionMoves(rightAttacks, team);
        long rightAttacksNotPromoting = bitboardGenerators.pawnGetNonPromotionMoves(rightAttacks, team);
        long rightAttacksEnPassant = bitboardGenerators.pawnRightCaptureEnPassant(team);

        int rightAttackAdjustBy = isWhite ? SW : NW;

        pawnPromotionBitboardToMoves(rightAttackAdjustBy, rightAttacksPromoting, team);
        pawnBitboardToMoves(rightAttackAdjustBy, rightAttacksNotPromoting, team, false, false);
        pawnBitboardToMoves(rightAttackAdjustBy, rightAttacksEnPassant, team, false, true);
        
        long leftAttacks = bitboardGenerators.pawnLeftAttackMoves(team);

        long leftAttacksPromoting = bitboardGenerators.pawnGetPromotionMoves(leftAttacks, team);
        long leftAttacksNotPromoting = bitboardGenerators.pawnGetNonPromotionMoves(leftAttacks, team);
        long leftAttacksEnPassant = bitboardGenerators.pawnLeftCaptureEnPassant(team);

        int leftAttackAdjustBy = isWhite ? SW : NW;

        pawnPromotionBitboardToMoves(leftAttackAdjustBy, leftAttacksPromoting, team);
        pawnBitboardToMoves(leftAttackAdjustBy, leftAttacksNotPromoting, team, false, false);
        pawnBitboardToMoves(leftAttackAdjustBy, leftAttacksEnPassant, team, false, true);

        long forwardMoves = bitboardGenerators.pawnForwardMove(team);
        long forwardMovesPromoting = bitboardGenerators.pawnGetPromotionMoves(forwardMoves, team);
        long forwardMovesNotPromoting = bitboardGenerators.pawnGetNonPromotionMoves(forwardMoves, team);
        
        int forwardMoveAdjustBy = isWhite ? SOUTH : NORTH;

        pawnPromotionBitboardToMoves(forwardMoveAdjustBy, forwardMovesPromoting, team);
        pawnBitboardToMoves(forwardMoveAdjustBy, forwardMovesNotPromoting, team, false, false);

        long doubleMove = bitboardGenerators.pawnDoubleMove(team);

        int doubleMoveAdjustBy = isWhite ? SOUTH + SOUTH: NORTH + NORTH;

        pawnBitboardToMoves(doubleMoveAdjustBy, doubleMove, team, true, false);

    }

    private void generateMajorMinorPieceMoves(int team, int piece) {

        long pieceBitboard = board.bitboards[team][piece];

        int pieces[] = HelperFunctions.bitboardToArray(pieceBitboard);

        for(int pieceIndex : pieces) {

            long moveBitboard = bitboardGenerators.pieceMoves(pieceIndex, team, piece);

            long pinValidatedMoveBitboard = moveBitboard & pins[pieceIndex];

            bitboardToMoves(pieceIndex, pinValidatedMoveBitboard, team, piece);

        }

    }

    private void generateCastlingKingMoves(int team) {

        generateKingsideCastlingMoves(team);
        generateQueensideCastlingMoves(team);

    }

    private void generateQueensideCastlingMoves(int team) {

        if (!this.board.hasQueensideCastlingRights(team)) {
            return;
        }
    
        boolean isWhite = (team == White);
    
        long safeSquares = isWhite ? WHITE_QUEENSIDE_SAFE_SQUARES : BLACK_QUEENSIDE_SAFE_SQUARES;
        long blockerSquares = isWhite ? WHITE_QUEENSIDE_CASTLE_PATH : BLACK_QUEENSIDE_CASTLE_PATH;
    
        boolean castlingPathSafe = (enemyVision & safeSquares) == 0L;
        boolean castlingPathClear = (board.getAllPieces() & blockerSquares) == 0L;
    
        if (castlingPathSafe && castlingPathClear) {
            int kingStart = isWhite ? WHITE_KING_START : BLACK_KING_START;
            int kingEnd = isWhite ? WHITE_QUEENSIDE_KING_END : BLACK_QUEENSIDE_KING_END;
    
            Move move = new Move(kingStart, kingEnd, team, K, false, NULL_PIECE, false, NULL_PIECE, false, false, true);
            moveList.add(move);
        }
    }
    

    private void generateKingsideCastlingMoves(int team) {

        if (!this.board.hasKingsideCastlingRights(team)) {
            return;
        }
    
        boolean isWhite = (team == White);
    
        long safeSquares = isWhite ? WHITE_KINGSIDE_SAFE_SQUARES : BLACK_KINGSIDE_SAFE_SQUARES;
        long blockerSquares = isWhite ? WHITE_KINGSIDE_CASTLE_PATH : BLACK_KINGSIDE_CASTLE_PATH;
    
        boolean castlingPathSafe = (enemyVision & safeSquares) == 0L;
        boolean castlingPathClear = (board.getAllPieces() & blockerSquares) == 0L;
    
        if (castlingPathSafe && castlingPathClear) {
            int kingStart = isWhite ? WHITE_KING_START : BLACK_KING_START;
            int kingEnd = isWhite ? WHITE_KINGSIDE_KING_END : BLACK_KINGSIDE_KING_END;
    
            Move move = new Move(kingStart, kingEnd, team, K, false, NULL_PIECE, false, NULL_PIECE, false, false, true);
            moveList.add(move);
        }
    }
    
    

    private void generateFriendlyKingNormalMoves(int team) {

        int kingIndex = Long.numberOfTrailingZeros(board.bitboards[team][K]);

        // King moves is all squares not occupied by pieces on kings team
        long unvalidatedKingMoves = bitboardGenerators.kingMoves(kingIndex, team);

        long validatedKingMoves = unvalidatedKingMoves & ~enemyVision;

        bitboardToMoves(kingIndex, validatedKingMoves, team, K);

    }

    private void pawnBitboardToMoves(int adjustBy, long bitboard, int team, boolean pawnDoubleMove, boolean enPassant) {
        
        long validatedBitboard = bitboard & checkMask;

        int toSquares[] = HelperFunctions.bitboardToArray(validatedBitboard);

        for(int toSquare : toSquares) {

            int fromSquare = toSquare + adjustBy;

            if((pins[fromSquare] & (1L << toSquare)) == 0) {
                // Piece is pinned and is trying to move illegally
                continue;
            }
            
            int capturePiece = this.board.getPieceAt(toSquare);
            boolean captureFlag = capturePiece != NULL_PIECE; 

            Move move = new Move(fromSquare, toSquare, team, P, captureFlag, capturePiece, false, NULL_PIECE, pawnDoubleMove, enPassant, false);
            moveList.add(move);
        }

    }

    private void pawnPromotionBitboardToMoves(int adjustBy, long bitboard, int team) {

        long validatedBitboard = bitboard & checkMask;

        int toSquares[] = HelperFunctions.bitboardToArray(validatedBitboard);

        for(int toSquare : toSquares) {

            int fromSquare = toSquare + adjustBy;
            
            int capturePiece = this.board.getPieceAt(toSquare);
            boolean captureFlag = capturePiece != NULL_PIECE; 

            Move queenPromote = new Move(fromSquare, toSquare, team, P, captureFlag, capturePiece, true, Q, false, false, false);
            Move rookPromote = new Move(fromSquare, toSquare, team, P, captureFlag, capturePiece, true, R, false, false, false);
            Move bishopPromote = new Move(fromSquare, toSquare, team, P, captureFlag, capturePiece, true, B, false, false, false);
            Move knightPromote = new Move(fromSquare, toSquare, team, P, captureFlag, capturePiece, true, N, false, false, false);
            
            moveList.add(queenPromote);
            moveList.add(rookPromote);
            moveList.add(bishopPromote);
            moveList.add(knightPromote);
        }

    }

    /**
     * An object builder for moves, does not handle promotions, enpassant, double forwards, and castling since this is for non pawn pieces 
     * @param index : the index of the piece
     * @param bitboard : a validated movement bitboard
     */
    private void bitboardToMoves(int index, long bitboard, int team, int piece) {

        long validatedBitboard = bitboard;
        
        if(piece != K) {
            validatedBitboard &= checkMask;
        }
            
        int toSquares[] = HelperFunctions.bitboardToArray(validatedBitboard);

        for(int toSquare : toSquares) {
            
            int capturePiece = this.board.getPieceAt(toSquare);
            boolean captureFlag = capturePiece != NULL_PIECE;

            Move move = new Move(index, toSquare, team, piece, captureFlag, capturePiece, false, NULL_PIECE, false, false, false);

            this.moveList.add(move);

        }

    }

    /**
     * Populates enemy vision to help with legal king move generation
     * Calculates Checks and Required Blocking path for single checks
     * @param enemyTeam The opponents team represented as an int (White - 0 : Black - 1)
     * @return Nothing, This function populates variables for the move generator
     */
    private void updateOpponentCoverage(int enemyTeam) {

        // Variable names are based on perspetive of the active team, this calculates the enemy's vision

        int friendlyTeam = HelperFunctions.getEnemyTeam(enemyTeam);

        boolean isWhite = enemyTeam == White;

        long friendlyKing = this.board.bitboards[friendlyTeam][K];
        int friendlyKingIndex = Long.numberOfTrailingZeros(friendlyKing);

        assert friendlyKingIndex < 64 : "Friendly King does not exist on king bitboard";
        // TODO : keep for duration of testing

        long[] enemyTeamPieces = this.board.bitboards[enemyTeam];

        // checking pawns

        long pawnLeftAttack = bitboardGenerators.pawnLeftAttackVision(enemyTeam);

        if((pawnLeftAttack & friendlyKing) != 0L) {
            
            long attackingPawn;

            this.checkCount++;

            if(isWhite) {
                attackingPawn = friendlyKing << 9; 
            } else {
                attackingPawn = friendlyKing >>> 7;
            }

            this.checkMask &= attackingPawn;
        }

        this.enemyVision |= pawnLeftAttack; 

        long pawnRightAttack = bitboardGenerators.pawnRightAttackVision(enemyTeam);

        if((pawnRightAttack & friendlyKing) != 0L) {
            
            long attackingPawn;

            this.checkCount++;

            if(isWhite) {
                attackingPawn = friendlyKing << 7; 
            } else {
                attackingPawn = friendlyKing >>> 9;
            }

            this.checkMask &= attackingPawn;
        }

        this.enemyVision |= pawnRightAttack; 


        // Checking Rooks

        
        int rooks[] = HelperFunctions.bitboardToArray(enemyTeamPieces[R]);

        
        long friendlyKingRookVision = bitboardGenerators.rookVision(friendlyKingIndex);


        for(int rookIndex : rooks) {

            long rookVision = bitboardGenerators.rookVision(rookIndex);

            this.enemyVision |= rookVision;

            if((rookVision & friendlyKing) != 0L) {
    
                this.checkCount++;

                long rookSightline = HelperFunctions.bitsBetween[rookIndex][friendlyKingIndex];

                this.checkMask &= rookSightline | (1L << rookIndex);

            }

            // Getting the F Kings 'Bishop Vision' (Doesnt filter out friendly pieces) and 
            // Getting the E Bishop 'Bishop Vision' (And filtering out enemy pieces) and
            // Getting the Bits between since we only care about pins and not overlapping vision anywhere 

            // manually calculating since we already have the vision
            long rookMoves = rookVision & ~this.board.getTeamsPieces(enemyTeam);

            long friendlyKingToEnemyRookSightline = friendlyKingRookVision & rookMoves & HelperFunctions.bitsBetween[friendlyKingIndex][rookIndex];

            // Potential pinner
            if(friendlyKingToEnemyRookSightline != 0L) {

                int pinnedPieceIndex = Long.numberOfTrailingZeros(friendlyKingToEnemyRookSightline);

                assert pinnedPieceIndex != 0 : "Pinning Error";

                pins[pinnedPieceIndex] = HelperFunctions.bitsBetween[friendlyKingIndex][rookIndex] | (1L << rookIndex);

            }

        }

        int bishops[] = HelperFunctions.bitboardToArray(enemyTeamPieces[B]);

        long friendlyKingBishopVision = bitboardGenerators.bishopVision(friendlyKingIndex);

        for(int bishopIndex : bishops) {

            long bishopVision = bitboardGenerators.bishopVision(bishopIndex);

            this.enemyVision |= bishopVision;

            if((bishopVision & friendlyKing) != 0L) {
    
                this.checkCount++;

                long bishopSightline = HelperFunctions.bitsBetween[bishopIndex][friendlyKingIndex];

                this.checkMask &= bishopSightline | (1L << bishopIndex);

            }

            // Getting the F Kings 'Bishop Vision' (Doesnt filter out friendly pieces) and 
            // Getting the E Bishop 'Bishop Vision' (And filtering out enemy pieces) and
            // Getting the Bits between since we only care about pins and not overlapping vision anywhere 

            // manually calculating since we already have the vision
            long bishopMoves = bishopVision & ~this.board.getTeamsPieces(enemyTeam);

            long friendlyKingToEnemyBishopSightline = friendlyKingBishopVision & bishopMoves & HelperFunctions.bitsBetween[friendlyKingIndex][bishopIndex];

            // Potential pinner
            if(friendlyKingToEnemyBishopSightline != 0L) {

                int pinnedPieceIndex = Long.numberOfTrailingZeros(friendlyKingToEnemyBishopSightline);

                assert pinnedPieceIndex != 0 : "Pinning Error";

                pins[pinnedPieceIndex] = HelperFunctions.bitsBetween[friendlyKingIndex][bishopIndex] | (1L << bishopIndex);

            }

        }

        int queens[] = HelperFunctions.bitboardToArray(enemyTeamPieces[Q]);

        long friendlyKingQueenVision = bitboardGenerators.queenVision(friendlyKingIndex);

        for(int queenIndex : queens) {

            long queenVision = bitboardGenerators.queenVision(queenIndex);

            this.enemyVision |= queenVision;

            // check for check
            if((queenVision & friendlyKing) != 0L) {
                
                this.checkCount++;

                long queenSightline = HelperFunctions.bitsBetween[queenIndex][friendlyKingIndex];

                this.checkMask &= queenSightline | (1L << queenIndex);

            }

            // Getting the F Kings 'Queen Vision' (Doesnt filter out friendly pieces) and 
            // Getting the E Queens 'Queen Vision' (And filtering out enemy pieces) and
            // Getting the Bits between since we only care about pins and not overlapping vision anywhere 

            // manually calculating since we already have the vision
            long queenMoves = queenVision & ~this.board.getTeamsPieces(enemyTeam);

            long friendlyKingToEnemyQueenSightline = friendlyKingQueenVision & queenMoves & HelperFunctions.bitsBetween[friendlyKingIndex][queenIndex];

            // Potential pinner
            if(friendlyKingToEnemyQueenSightline != 0L) {

                int pinnedPieceIndex = Long.numberOfTrailingZeros(friendlyKingToEnemyQueenSightline);

                assert pinnedPieceIndex != 0 : "Pinning Error";

                pins[pinnedPieceIndex] = HelperFunctions.bitsBetween[friendlyKingIndex][queenIndex] | (1L << queenIndex);

            }

        }

        int knights[] = HelperFunctions.bitboardToArray(enemyTeamPieces[N]);

        for(int knightIndex : knights) {

            long knightVision = bitboardGenerators.knightVision(knightIndex);

            this.enemyVision |= knightVision;

            if((knightVision & friendlyKing) != 0L) {
    
                this.checkCount++;

                this.checkMask &= (1L << knightIndex);

            }

        }

        int enemyKingIndex = Long.numberOfTrailingZeros(enemyTeamPieces[K]);

        assert enemyKingIndex < 64 : "Enemy King does not exist on king bitboard.";

        this.enemyVision |= bitboardGenerators.kingVision(enemyKingIndex);

    }

    public static void printBitboard(long bitboard) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int index = row * 8 + col;
                boolean isSet = (bitboard & (1L << index)) != 0;
                System.out.print(isSet ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

}