package parkerbasicchessengine;
import parkerbasicchessengine.Board;

abstract class abstract_chess_engine {

    private Board board;
    public int depth;

    public final int positiveInfinity = 9999999;
    public final int negativeInfinity = -positiveInfinity;

    public long piece_bitboards[][];
    // [0 white, 1 black] - [0 king, 1 queen, 2 bishop, 3 knight, 4 rook, 5 pawn]

    public void convertBoardToBitboards(){

        this.piece_bitboards =  = new long[2][6];

        for(Piece piece : board.pieceList){
            
            int team = piece.isWhite ? 0 : 1;
            int piece = pieceBitboardIndex(piece);

            this.piece_bitboards[team][piece] = this.piece_bitboards[team][piece] | (1L << piece.index());
        }
    }

    private int pieceBitboardIndex(Piece piece){

        switch (piece.abbreviation.toUpperCase()) {
            case 'K': return 0;
            case 'Q': return 1;
            case 'B': return 2;
            case 'N': return 3;
            case 'R': return 4;
            case 'P': return 5;
            default:
                System.out.println("Unknown Piece");
                return -1;
        }
    }


}
