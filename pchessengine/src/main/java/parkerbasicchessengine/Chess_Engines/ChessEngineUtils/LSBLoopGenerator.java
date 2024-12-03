package parkerbasicchessengine.Chess_Engines.ChessEngineUtils;

import javax.management.RuntimeErrorException;

public class LSBLoopGenerator {

    // efficient object for looping over a bitboard

    private long bitboard;
    
    public boolean hasNext;
    private long currentBit;

    public LSBLoopGenerator(long bitboard){
        this.bitboard = bitboard;
        this.hasNext = bitboard != 0;
    }

    public void setBitboard(long bitboard){
        this.bitboard = bitboard;
        this.hasNext = bitboard != 0;
    }

    public long getNext(){

        if(!hasNext){
            throw new RuntimeErrorException(null, "LSBLoopGenerator is trying to access next bit after bitboard is empty, check .hasNext before running");
        }

        this.currentBit = this.bitboard & -this.bitboard;
        this.bitboard ^= this.currentBit;
        this.hasNext = bitboard != 0;

        return currentBit;
    }
}
