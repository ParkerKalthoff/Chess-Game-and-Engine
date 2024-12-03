package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.LSBLoopGenerator;

public class PawnMoveGen extends Constants {

    private BitwiseBoard bwB;
    private MoveList moveList;

    public PawnMoveGen(BitwiseBoard bwB, MoveList moveList) {

        this.bwB = bwB;
        this.moveList = moveList;

    }

    private void genMoves() {

        int team = this.bwB.isWhiteToMove ? White : Black;

        long pawnBitboard = this.bwB.piece_bitboards[team][P];

        if (pawnBitboard == 0) { // skip code if no pawns
            return;
        }

        LSBLoopGenerator bitboardLooper = new LSBLoopGenerator(pawnBitboard);

        while (bitboardLooper.hasNext) {
            long currentPawn = bitboardLooper.getNext();
            long movementBoard = 0L;
            int pawnIndex = MovementBitboards.bitboardToIndex.get(currentPawn);

            if ((currentPawn & MovementBitboards.not_a_file) != 0) {
                long leftAttack = (team == White) ? 1L << MovementBitboards.pawnright[team][pawnIndex]
                        : 1L << MovementBitboards.pawnleft[team][pawnIndex];
                movementBoard |= leftAttack & ~friendlyPieces; 
            }

            if ((currentPawn & MovementBitboards.not_h_file) != 0) {
                long rightAttack = (team == White) ? 1L << MovementBitboards.pawnleft[team][pawnIndex]
                        : 1L << MovementBitboards.pawnright[team][pawnIndex];
                movementBoard |= rightAttack & ~friendlyPieces; 
            }

            // Generate forward moves
            if (!(MovementBitboards.bits_row[pawnIndex] == 7 || MovementBitboards.bits_row[pawnIndex] == 0)) {
                long legalForwardMove = 1L << MovementBitboards.pawnplus[team][pawnIndex];
                movementBoard |= legalForwardMove & ~allPieces; 
            }

            
            if ((team == White && MovementBitboards.bits_row[pawnIndex] == 2) ||
                    (team == Black && MovementBitboards.bits_row[pawnIndex] == 6)) {
                long legalDoubleMove = 1L << MovementBitboards.pawndouble[team][pawnIndex];
                movementBoard |= legalDoubleMove & ~allPieces; 
            }
        }
    }
}
