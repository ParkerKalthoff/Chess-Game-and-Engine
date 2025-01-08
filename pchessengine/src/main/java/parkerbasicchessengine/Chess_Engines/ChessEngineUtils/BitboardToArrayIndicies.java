package parkerbasicchessengine.Chess_Engines.ChessEngineUtils;

public class BitboardToArrayIndicies {

    public static int[] bitboardToIndicies(long bitboard){

        int length = Long.bitCount(bitboard);

        int indicies[] = new int[length];

        LSBLoopGenerator looper = new LSBLoopGenerator(bitboard);
        
        for(int i = 0; i < length; i++){

            indicies[i] = Long.numberOfTrailingZeros(looper.getNext());
        
        }

        return indicies;
    }
}
