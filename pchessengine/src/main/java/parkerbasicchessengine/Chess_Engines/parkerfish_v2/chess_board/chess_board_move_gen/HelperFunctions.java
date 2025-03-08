package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen;

public class HelperFunctions {

    // A general utils class for methods that are needed but violate the principal of being out of scope for a certain class, 
    // or generally just removing overly lengthy methods that produce an obvious output, but make a class too confusing because of method length

    public static long bitsBetween[][] = new long[64][64];

    public static final long knightMoves[] = { 0x20400L, 0x50800L, 0xa1100L, 0x142200L, 0x284400L, 0x508800L, 0xa01000L,
        0x402000L, 0x2040004L, 0x5080008L, 0xa110011L, 0x14220022L, 0x28440044L, 0x50880088L, 0xa0100010L,
        0x40200020L, 0x204000402L, 0x508000805L, 0xa1100110aL, 0x1422002214L, 0x2844004428L, 0x5088008850L,
        0xa0100010a0L, 0x4020002040L, 0x20400040200L, 0x50800080500L, 0xa1100110a00L, 0x142200221400L,
        0x284400442800L, 0x508800885000L, 0xa0100010a000L, 0x402000204000L, 0x2040004020000L, 0x5080008050000L,
        0xa1100110a0000L, 0x14220022140000L, 0x28440044280000L, 0x50880088500000L, 0xa0100010a00000L,
        0x40200020400000L, 0x204000402000000L, 0x508000805000000L, 0xa1100110a000000L, 0x1422002214000000L,
        0x2844004428000000L, 0x5088008850000000L, 0xa0100010a0000000L, 0x4020002040000000L, 0x400040200000000L,
        0x800080500000000L, 0x1100110a00000000L, 0x2200221400000000L, 0x4400442800000000L, 0x8800885000000000L,
        0x100010a000000000L, 0x2000204000000000L, 0x4020000000000L, 0x8050000000000L, 0x110a0000000000L,
        0x22140000000000L, 0x44280000000000L, 0x88500000000000L, 0x10a00000000000L, 0x20400000000000L };
        
    public static final long kingMoves[] = { 0x302L, 0x705L, 0xe0aL, 0x1c14L, 0x3828L, 0x7050L, 0xe0a0L, 0xc040L,
        0x30203L, 0x70507L, 0xe0a0eL, 0x1c141cL, 0x382838L, 0x705070L, 0xe0a0e0L, 0xc040c0L, 0x3020300L, 0x7050700L,
        0xe0a0e00L, 0x1c141c00L, 0x38283800L, 0x70507000L, 0xe0a0e000L, 0xc040c000L, 0x302030000L, 0x705070000L,
        0xe0a0e0000L, 0x1c141c0000L, 0x3828380000L, 0x7050700000L, 0xe0a0e00000L, 0xc040c00000L, 0x30203000000L,
        0x70507000000L, 0xe0a0e000000L, 0x1c141c000000L, 0x382838000000L, 0x705070000000L, 0xe0a0e0000000L,
        0xc040c0000000L, 0x3020300000000L, 0x7050700000000L, 0xe0a0e00000000L, 0x1c141c00000000L, 0x38283800000000L,
        0x70507000000000L, 0xe0a0e000000000L, 0xc040c000000000L, 0x302030000000000L, 0x705070000000000L,
        0xe0a0e0000000000L, 0x1c141c0000000000L, 0x3828380000000000L, 0x7050700000000000L, 0xe0a0e00000000000L,
        0xc040c00000000000L, 0x203000000000000L, 0x507000000000000L, 0xa0e000000000000L, 0x141c000000000000L,
        0x2838000000000000L, 0x5070000000000000L, 0xa0e0000000000000L, 0x40c0000000000000L };

    static {
        initializeBitsBetween();
    }

    public static int getEnemyTeam(int team) {
        return team ^ 1;
    }

    // Bits Between gives you a bitboard of the vision between two indicies
    // This will be useful in implimenting actions like pins, pass pawns potentially, etc
    // Its implimented as a 2D array so look ups are fast 

    private static void initializeBitsBetween() {
        for (int from = 0; from < 64; from++) {
            for (int to = 0; to < 64; to++) {
                bitsBetween[from][to] = calculatePathBits(from, to);
            }
        }
    }

    private static long calculatePathBits(int start, int end) {
        
        int startRank = start / 8, startFile = start % 8;
        int endRank = end / 8, endFile = end % 8;
    
        if (start == end) return 0L;
    
        long pathBits = 0L;
    
        if (startRank == endRank) { // Horizontal movement
            
            int lowerFile = Math.min(startFile, endFile) + 1;
            int upperFile = Math.max(startFile, endFile);
            
            for (int file = lowerFile; file < upperFile; file++) {
                pathBits |= 1L << (startRank * 8 + file);
            }

        } else if (startFile == endFile) { // Vertical movement
            
            int lowerRank = Math.min(startRank, endRank) + 1;
            int upperRank = Math.max(startRank, endRank);
            
            for (int rank = lowerRank; rank < upperRank; rank++) {
                pathBits |= 1L << (rank * 8 + startFile);
            }

        } else if (Math.abs(startRank - endRank) == Math.abs(startFile - endFile)) { // Diagonal movement
            
            int rankStep = (endRank > startRank) ? 1 : -1;
            int fileStep = (endFile > startFile) ? 1 : -1;
    
            int rank = startRank + rankStep;
            int file = startFile + fileStep;
    
            while (rank != endRank && file != endFile) {

                pathBits |= 1L << (rank * 8 + file);
                
                rank += rankStep;
                file += fileStep;
            }
        }
    
        return pathBits;
    }

    /**
     * Used to convert a bitboard of indicies to an array for indicies for iteration
     * @param bitboard a bitboard of either a pieces vision, or a bitboard of pieces to get all of the indicies.
     * @return Returns an array of integers, if bitboard is blank, it will return an empty 0 length array
     */
    public static int[] bitboardToArray(long bitboard){

        int length = Long.bitCount(bitboard);

        int bitIndicies[] = new int[length];

        for(int i = 0; i < length; i++){
           
            long leastSignificantBit = bitboard & -bitboard;

            bitIndicies[i] = Long.numberOfTrailingZeros(leastSignificantBit);

            bitboard &= ~leastSignificantBit; 
            
        }

        return bitIndicies;
    }
    


}
