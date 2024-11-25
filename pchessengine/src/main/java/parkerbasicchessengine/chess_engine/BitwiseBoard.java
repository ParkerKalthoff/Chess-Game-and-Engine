public class BitwiseBoard {

   
    // [0 white, 1 black] - [0 king, 1 queen, 2 bishop, 3 knight, 4 rook, 5 pawn]
    public long piece_bitboards[][];
    public int enpassantIndex;
    public boolean whiteToMove;
    public byte castlingRights;
    public int halfMoveClock;
    public int fullMoveCounter;

    public BitwiseBoard(String fenString){
        String parts[] = fenString.split("");

        String piecePlacement = parts[0];

        this.whiteToMove = parts[1].equals("w");

        this.castlingRights = parts[2];
        String enPassantSquare = parts[3];
        this.halfMoveClock = Integer.parseInt(parts[4] * 2 + this.whiteToMove ? 0 : 1);
        String fullMove = parts[5];



    }
}
