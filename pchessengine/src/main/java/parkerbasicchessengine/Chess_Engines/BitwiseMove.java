package parkerbasicchessengine.Chess_Engines;

public class BitwiseMove {

    public int fromSquare;
    public int toSquare;
    public int type;

    public static BitwiseBoard bwB;

    public BitwiseMove(int fromSquare, int toSquare, int type) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.type = type;
    }

    public void setBoard(BitwiseBoard bwB){
        BitwiseMove.bwB = bwB;
    }

    
}
