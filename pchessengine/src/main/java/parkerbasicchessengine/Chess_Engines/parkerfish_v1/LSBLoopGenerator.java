package parkerbasicchessengine.Chess_Engines.parkerfish_v1;

public class LSBLoopGenerator {

    private long bitboard;
    
    public boolean hasNext;
    public long currentBit;

    public LSBLoopGenerator(){}

    public void setBitboard(long bitboard){
        this.bitboard = bitboard;

        if(bitboard != 0){
            this.hasNext = true;
        }
    }

    public long getNext(){

        if(!hasNext){
            return 0;
        }

        this.currentBit = this.bitboard & -this.bitboard;
        this.bitboard ^= this.currentBit;
        this.hasNext = bitboard != 0;

        return currentBit;
    }
}
