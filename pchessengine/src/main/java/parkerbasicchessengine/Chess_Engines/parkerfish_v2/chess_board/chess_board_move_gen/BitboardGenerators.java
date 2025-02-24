package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

import javax.management.RuntimeErrorException;

import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Board;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen.HelperFunctions.*;


public class BitboardGenerators {

    // Library used to generate bitboards

    private Board board;

    public BitboardGenerators(Board board) {
        this.board = board;
    }

    //  [   I USED CHATGPT TO GENERATE JAVADOC COMMENTS, I WRITE MY OWN CODE BWAHAHAHA  ]
    // dont judge me

    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>
    //   Pawns Pawns Pawns Pawns Pawns Pawns Pawns Pawns Pawns Pawns
    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>

    /**
     * Returns a bitboard of pawn moves that result in promotion.
     *
     * @param pawnMoveBitboard The bitboard representing pawn moves.
     * @param team The team (White or Black).
     * @return A bitboard with only the pawn promotion moves.
     */
    public long pawnGetPromotionMoves(long pawnMoveBitboard, int team) {

        if(team == White) {
            return pawnMoveBitboard & RANK_8; // For White, promotion occurs on rank 8.
        } else {
            return pawnMoveBitboard & RANK_1; // For Black, promotion occurs on rank 1.
        }

    }

    /**
     * Returns a bitboard of pawn moves that do not result in promotion.
     *
     * @param pawnMoveBitboard The bitboard representing pawn moves.
     * @param team The team (White or Black).
     * @return A bitboard with pawn moves excluding promotion moves.
     */

    public long pawnGetNonPromotionMoves(long pawnMoveBitboard, int team) {

        if(team == White) {
            return pawnMoveBitboard & ~RANK_8; 
        } else {
            return pawnMoveBitboard & ~RANK_1; 
        }

    }

    /**
     * Calculates the possible single-square forward moves for pawns.
     *
     * @param team The team (White or Black).
     * @return A bitboard representing valid forward pawn moves.
     */

    public long pawnForwardMove(int team) {

        long bitboard;

        long activePawns = this.board.bitboards[team][P]; // Get the active pawns for the given team.

        if(team == White) {
            bitboard = (activePawns >>> 8) & ~this.board.getAllPieces(); // Shift White pawns up and exclude occupied squares.
        } else {
            bitboard = (activePawns << 8) & ~this.board.getAllPieces(); // Shift Black pawns down and exclude occupied squares.
        }

        return bitboard;

    }

    /**
     * Calculates the possible double-square forward moves for pawns from their starting rank.
     *
     * @param team The team (White or Black).
     * @return A bitboard representing valid pawn double moves.
     */

    public long pawnDoubleMove(int team) {

        long bitboard;

        long singlePushes = pawnForwardMove(team); // Get valid single pushes first.

        if(team == White) {
            bitboard = ((singlePushes & RANK_3) >>> 8) & ~this.board.getAllPieces(); // White double move from rank 2.
        } else {
            bitboard = ((singlePushes & RANK_6) << 8) & ~this.board.getAllPieces(); // Black double move from rank 7.
        }

        return bitboard;
    }

    /**
     * Computes the left attack vision for pawns (attacked squares without considering enemy pieces).
     *
     * @param team The team (White or Black).
     * @return A bitboard representing squares the pawns could attack to the left.
     */

    public long pawnLeftAttackVision(int team) {

        long pawns = this.board.bitboards[team][P]; // Get the active pawns for the given team.
        long moveBitboard;

        if(team == White) {
            moveBitboard = (pawns & ~FILE_A) >>> 9; // White pawns attack left by shifting 9 bits down.
        } else {
            moveBitboard = (pawns & ~FILE_A) << 7; // Black pawns attack left by shifting 7 bits up.
        }

        return moveBitboard;
    }

    /**
     * Calculates the legal left attack moves for pawns (considering enemy pieces).
     *
     * @param team The team (White or Black).
     * @return A bitboard representing valid left attacks on enemy pieces.
     */

    public long pawnLeftAttackMoves(int team) {

        int enemyTeam = HelperFunctions.getEnemyTeam(team); // Get the opposing team.

        return pawnLeftAttackVision(team) & board.getTeamsPieces(enemyTeam); // Mask attacks to enemy pieces only.
    }

    
    /**
     * Calculates the legal left attack captures for enpassant square
     *
     * @param team The team (White or Black).
     * @return A bitboard representing valid left attacks on enpassant square.
     */
    public long pawnLeftCaptureEnPassant(int team) {

        if(board.enPassantIndex == -1) {
            return 0L;
        }

        return pawnLeftAttackVision(team) & (1L << board.enPassantIndex); // Mask attacks to enemy pieces only.
    }

    /**
     * Computes the right attack vision for pawns (attacked squares without considering enemy pieces).
     *
     * @param team The team (White or Black).
     * @return A bitboard representing squares the pawns could attack to the right.
     */

    public long pawnRightAttackVision(int team) {

        long pawns = this.board.bitboards[team][P]; // Get the active pawns for the given team.
        long moveBitboard;

        if(team == White) {
            moveBitboard = (pawns & ~FILE_H) >>> 7; // White pawns attack right by shifting 7 bits down.
        } else {
            moveBitboard = (pawns & ~FILE_H) << 9; // Black pawns attack right by shifting 9 bits up.
        }

        return moveBitboard;
    }

    /**
     * Calculates the legal right attack moves for pawns (considering enemy pieces).
     *
     * @param team The team (White or Black).
     * @return A bitboard representing valid right attacks on enemy pieces.
     */
    public long pawnRightAttackMoves(int team) {

        int enemyTeam = getEnemyTeam(team); // Get the opposing team.

        return pawnRightAttackVision(team) & board.getTeamsPieces(enemyTeam); // Mask attacks to enemy pieces only.
    }

    /**
     * Calculates the legal right attack captures for enpassant square
     *
     * @param team The team (White or Black).
     * @return A bitboard representing valid right attacks on enpassant square.
     */
    public long pawnRightCaptureEnPassant(int team) {

        if(board.enPassantIndex == -1) {
            return 0L;
        }

        return pawnLeftAttackVision(team) & (1L << board.enPassantIndex); // Mask attacks to enemy pieces only.
    }


    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>
    //   Rook Rook Rook Rook Rook Rook Rook Rook Rook Rook Rook Rook 
    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>

    /**
     * Calculates the attack vision for a rook from a given board index.
     *
     * @param index The board index of the rook (0-63).
     * @return A bitboard representing all squares the rook can attack or move to.
     */
    public long rookVision(int index) {

        long allPieces = this.board.getAllPieces(); // Get the occupied squares on the board.

        long rookAttackVision = MagicBitboards.generateRookMovementBitboard(index, allPieces);

        return rookAttackVision;
    }

    /**
     * Calculates the legal moves for a rook from a given board index, considering the team.
     *
     * @param index The board index of the rook (0-63).
     * @param team The team (White or Black).
     * @return A bitboard representing valid rook moves.
     */
    public long rookMoves(int index, int team) {

        long rookAttackVision = rookVision(index);

        long rookMoves = rookAttackVision & ~this.board.getTeamsPieces(team); // Exclude moves blocked by own pieces.

        return rookMoves;
    }

    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>
    //   BISHOP BISHOP BISHOP BISHOP BISHOP BISHOP BISHOP BISHOP BISHOP BISHOP BISHOP BISHOP 
    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>

    /**
     * Calculates the attack vision for a bishop from a given board index.
     *
     * @param index The board index of the bishop (0-63).
     * @return A bitboard representing all squares the bishop can attack or move to.
     */
    public long bishopVision(int index) {

        long allPieces = this.board.getAllPieces(); // Get the occupied squares on the board.

        long bishopAttackVision = MagicBitboards.generateBishopMovementBitboard(index, allPieces);

        return bishopAttackVision;
    }

    /**
     * Calculates the legal moves for a bishop from a given board index, considering the team.
     *
     * @param index The board index of the bishop (0-63).
     * @param team The team (White or Black).
     * @return A bitboard representing valid bishop moves.
     */
    public long bishopMoves(int index, int team) {

        long bishopAttackVision = bishopVision(index);

        long bishopMoves = bishopAttackVision & ~this.board.getTeamsPieces(team); // Exclude moves blocked by own pieces.

        return bishopMoves;
    }

    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>
    //   QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN QUEEN  
    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>

    /**
     * Calculates the attack vision for a queen from a given board index.
     *
     * @param index The board index of the queen (0-63).
     * @return A bitboard representing all squares the queen can attack or move to.
     */
    public long queenVision(int index) {

        long allPieces = this.board.getAllPieces(); // Get the occupied squares on the board.

        long queenAttackVision = MagicBitboards.generateQueenMovementBitboard(index, allPieces); // Combine rook and bishop moves.

        return queenAttackVision;
    }

    /**
     * Calculates the legal moves for a queen from a given board index, considering the team.
     *
     * @param index The board index of the queen (0-63).
     * @param team The team (White or Black).
     * @return A bitboard representing valid queen moves.
     */
    public long queenMoves(int index, int team) {

        long queenAttackVision = queenVision(index);

        long queenMoves = queenAttackVision & ~this.board.getTeamsPieces(team); // Exclude moves blocked by own pieces.

        return queenMoves;
    }

    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>
    //   KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT KNIGHT   
    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>

    /**
     * Calculates the attack vision for a knight from a given board index.
     *
     * @param index The board index of the knight (0-63).
     * @return A bitboard representing all squares the knight can attack or move to.
     */
    public long knightVision(int index) {
        
        return knightMoves[index];

    }

    /**
     * Calculates the legal moves for a knight from a given board index, considering the team.
     *
     * @param index The board index of the knight (0-63).
     * @param team The team (White or Black).
     * @return A bitboard representing valid knight moves.
     */
    public long knightMoves(int index, int team) {
        
        long knightSight = knightMoves[index];

        return knightSight & ~this.board.getTeamsPieces(team);

    }

    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>
    //   KING KING KING KING KING KING KING KING KING KING KING KING  
    // <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>> <<< >>>

    // This will only account for kings vision, this will not do castling

    /**
     * Calculates the attack vision for a king from a given board index.
     *
     * @param index The board index of the king (0-63).
     * @return A bitboard representing all squares the king can attack or move to.
     */
    public long kingVision(int index) {
        
        return kingMoves[index];

    }

    /**
     * Calculates the legal moves for a king from a given board index, considering the team.
     *
     * @param index The board index of the king (0-63).
     * @param team The team (White or Black).
     * @return A bitboard representing valid king moves, not accounting for enemy vision.
     */
    public long kingMoves(int index, int team) {
        
        long kingSight = kingMoves[index];

        return kingSight & ~this.board.getTeamsPieces(team);

    }

    /**
     * Helps abstract getting bitboard for Move gen methods
     * @param index
     * @param team
     * @param piece
     * @return a movement bitboard
     */
    public long pieceMoves(int index, int team, int piece) {
        switch (piece) {
            case K:
                return kingMoves(index, team);
            
            case Q:
                return queenMoves(index, team);
            
            case R:
                return rookMoves(index, team);
            
            case B:
                return bishopMoves(index, team);
            
            case N:
                return knightMoves(index, team);
            
            default:
                throw new RuntimeErrorException(null, "Illegal piece: " + piece);
        }
    }    
}