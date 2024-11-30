package parkerbasicchessengine.Chess_Engines;

public class BitwiseMove {

    public int fromSquare;
    public int toSquare;
    public int capturePieceType;
    public int flag;
    public int promoteTo;

    public BitwiseMove(int fromSquare, int toSquare, int capturePieceType) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.capturePieceType = capturePieceType;
    }

    public BitwiseMove(int fromSquare, int toSquare, int capturePieceType, int flag) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.capturePieceType = capturePieceType;
        this.flag = flag;
    }   

    public BitwiseMove(int fromSquare, int toSquare, int capturePieceType, int flag, int promoteTo) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.capturePieceType = capturePieceType;
        this.flag = flag;
        this.promoteTo = promoteTo;
    }   
}
