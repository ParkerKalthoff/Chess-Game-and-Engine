package parkerbasicchessengine.Chess_Engines.parkerfish_v1.ChessEngineUtils;

import java.util.HashMap;
import java.util.Map;

public class MovementBitboards extends Constants{

    // probably remove, this was from an old project and it SUCKS
    
    public static long bit_pawncaptures[][] = new long[2][64]; 
    public static long bit_pawndefends[][] = new long[2][64];
    public static long bit_left[][] = new long[2][64];
    public static long bit_right[][] = new long[2][64];
    public static long bit_pawnmoves[][] = new long[2][64];

    public static final long[] bit_north = generateAllScalarToEdges(8);
    public static final long[] bit_northeast = generateAllScalarToEdges(9);
    public static final long[] bit_east = generateAllScalarToEdges(1);
    public static final long[] bit_southeast = generateAllScalarToEdges(-7);
    public static final long[] bit_south = generateAllScalarToEdges(-8);
    public static final long[] bit_southwest = generateAllScalarToEdges(-9);
    public static final long[] bit_west = generateAllScalarToEdges(-1);
    public static final long[] bit_northwest = generateAllScalarToEdges(7);

    public static final long bit_knightmoves[] = {0x20400L,0x50800L,0xa1100L,0x142200L,0x284400L,0x508800L,0xa01000L,0x402000L,0x2040004L,0x5080008L,0xa110011L,0x14220022L,0x28440044L,0x50880088L,0xa0100010L,0x40200020L,0x204000402L,0x508000805L,0xa1100110aL,0x1422002214L,0x2844004428L,0x5088008850L,0xa0100010a0L,0x4020002040L,0x20400040200L,0x50800080500L,0xa1100110a00L,0x142200221400L,0x284400442800L,0x508800885000L,0xa0100010a000L,0x402000204000L,0x2040004020000L,0x5080008050000L,0xa1100110a0000L,0x14220022140000L,0x28440044280000L,0x50880088500000L,0xa0100010a00000L,0x40200020400000L,0x204000402000000L,0x508000805000000L,0xa1100110a000000L,0x1422002214000000L,0x2844004428000000L,0x5088008850000000L,0xa0100010a0000000L,0x4020002040000000L,0x400040200000000L,0x800080500000000L,0x1100110a00000000L,0x2200221400000000L,0x4400442800000000L,0x8800885000000000L,0x100010a000000000L,0x2000204000000000L,0x4020000000000L,0x8050000000000L,0x110a0000000000L,0x22140000000000L,0x44280000000000L,0x88500000000000L,0x10a00000000000L,0x20400000000000L};
    public static final long bit_bishopmoves[] = {0x8040201008040200L,0x0080402010080500L,0x0000804020110a00L,0x0000008041221400L,0x0000000182442800L,0x0000010204885000L,0x000102040810a000L,0x0102040810204000L,0x4020100804020002L,0x8040201008050005L,0x00804020110a000aL,0x0000804122140014L,0x0000018244280028L,0x0001020488500050L,0x0102040810a000a0L,0x0204081020400040L,0x2010080402000204L,0x4020100805000508L,0x804020110a000a11L,0x0080412214001422L,0x0001824428002844L,0x0102048850005088L,0x02040810a000a010L,0x0408102040004020L,0x1008040200020408L,0x2010080500050810L,0x4020110a000a1120L,0x8041221400142241L,0x0182442800284482L,0x0204885000508804L,0x040810a000a01008L,0x0810204000402010L,0x0804020002040810L,0x1008050005081020L,0x20110a000a112040L,0x4122140014224180L,0x8244280028448201L,0x0488500050880402L,0x0810a000a0100804L,0x1020400040201008L,0x0402000204081020L,0x0805000508102040L,0x110a000a11204080L,0x2214001422418000L,0x4428002844820100L,0x8850005088040201L,0x10a000a010080402L,0x2040004020100804L,0x0200020408102040L,0x0500050810204080L,0x0a000a1120408000L,0x1400142241800000L,0x2800284482010000L,0x5000508804020100L,0xa000a01008040201L,0x4000402010080402L,0x0002040810204080L,0x0005081020408000L,0x000a112040800000L,0x0014224180000000L,0x0028448201000000L,0x0050880402010000L,0x00a0100804020100L,0x0040201008040201L,};
    public static final long bit_rookmoves[] = {0x01010101010101feL,0x02020202020202fdL,0x04040404040404fbL,0x08080808080808f7L,0x10101010101010efL,0x20202020202020dfL,0x40404040404040bfL,0x808080808080807fL,0x010101010101fe01L,0x020202020202fd02L,0x040404040404fb04L,0x080808080808f708L,0x101010101010ef10L,0x202020202020df20L,0x404040404040bf40L,0x8080808080807f80L,0x0101010101fe0101L,0x0202020202fd0202L,0x0404040404fb0404L,0x0808080808f70808L,0x1010101010ef1010L,0x2020202020df2020L,0x4040404040bf4040L,0x80808080807f8080L,0x01010101fe010101L,0x02020202fd020202L,0x04040404fb040404L,0x08080808f7080808L,0x10101010ef101010L,0x20202020df202020L,0x40404040bf404040L,0x808080807f808080L,0x010101fe01010101L,0x020202fd02020202L,0x040404fb04040404L,0x080808f708080808L,0x101010ef10101010L,0x202020df20202020L,0x404040bf40404040L,0x8080807f80808080L,0x0101fe0101010101L,0x0202fd0202020202L,0x0404fb0404040404L,0x0808f70808080808L,0x1010ef1010101010L,0x2020df2020202020L,0x4040bf4040404040L,0x80807f8080808080L,0x01fe010101010101L,0x02fd020202020202L,0x04fb040404040404L,0x08f7080808080808L,0x10ef101010101010L,0x20df202020202020L,0x40bf404040404040L,0x807f808080808080L,0xfe01010101010101L,0xfd02020202020202L,0xfb04040404040404L,0xf708080808080808L,0xef10101010101010L,0xdf20202020202020L,0xbf40404040404040L,0x7f80808080808080L};
    public static final long bit_queenmoves[] = {0x81412111090503feL,0x02824222120a07fdL,0x0404844424150efbL,0x08080888492a1cf7L,0x10101011925438efL,0x2020212224a870dfL,0x404142444850e0bfL,0x8182848890a0c07fL,0x412111090503fe03L,0x824222120a07fd07L,0x04844424150efb0eL,0x080888492a1cf71cL,0x101011925438ef38L,0x20212224a870df70L,0x4142444850e0bfe0L,0x82848890a0c07fc0L,0x2111090503fe0305L,0x4222120a07fd070aL,0x844424150efb0e15L,0x0888492a1cf71c2aL,0x1011925438ef3854L,0x212224a870df70a8L,0x42444850e0bfe050L,0x848890a0c07fc0a0L,0x11090503fe030509L,0x22120a07fd070a12L,0x4424150efb0e1524L,0x88492a1cf71c2a49L,0x11925438ef385492L,0x2224a870df70a824L,0x444850e0bfe05048L,0x8890a0c07fc0a090L,0x090503fe03050911L,0x120a07fd070a1222L,0x24150efb0e152444L,0x492a1cf71c2a4988L,0x925438ef38549211L,0x24a870df70a82422L,0x4850e0bfe0504844L,0x90a0c07fc0a09088L,0x0503fe0305091121L,0x0a07fd070a122242L,0x150efb0e15244484L,0x2a1cf71c2a498808L,0x5438ef3854921110L,0xa870df70a8242221L,0x50e0bfe050484442L,0xa0c07fc0a0908884L,0x03fe030509112141L,0x07fd070a12224282L,0x0efb0e1524448404L,0x1cf71c2a49880808L,0x38ef385492111010L,0x70df70a824222120L,0xe0bfe05048444241L,0xc07fc0a090888482L,0xfe03050911214181L,0xfd070a1222428202L,0xfb0e152444840404L,0xf71c2a4988080808L,0xef38549211101010L,0xdf70a82422212020L,0xbfe0504844424140L};
    public static final long bit_kingmoves[] = {0x302L,0x705L,0xe0aL,0x1c14L,0x3828L,0x7050L,0xe0a0L,0xc040L,0x30203L,0x70507L,0xe0a0eL,0x1c141cL,0x382838L,0x705070L,0xe0a0e0L,0xc040c0L,0x3020300L,0x7050700L,0xe0a0e00L,0x1c141c00L,0x38283800L,0x70507000L,0xe0a0e000L,0xc040c000L,0x302030000L,0x705070000L,0xe0a0e0000L,0x1c141c0000L,0x3828380000L,0x7050700000L,0xe0a0e00000L,0xc040c00000L,0x30203000000L,0x70507000000L,0xe0a0e000000L,0x1c141c000000L,0x382838000000L,0x705070000000L,0xe0a0e0000000L,0xc040c0000000L,0x3020300000000L,0x7050700000000L,0xe0a0e00000000L,0x1c141c00000000L,0x38283800000000L,0x70507000000000L,0xe0a0e000000000L,0xc040c000000000L,0x302030000000000L,0x705070000000000L,0xe0a0e0000000000L,0x1c141c0000000000L,0x3828380000000000L,0x7050700000000000L,0xe0a0e00000000000L,0xc040c00000000000L,0x203000000000000L,0x507000000000000L,0xa0e000000000000L,0x141c000000000000L,0x2838000000000000L,0x5070000000000000L,0xa0e0000000000000L,0x40c0000000000000L};
    public static final long bit_pieces[][] = new long[2][7];
    public static final long bit_units[] = new long[2];
    public static final long bit_all = 0xffffffffffffffffL;
    public static final long mask_cols[] = {0x101010101010101L << 0, 0x101010101010101L << 1, 0x101010101010101L << 2, 0x101010101010101L << 3, 0x101010101010101L << 4, 0x101010101010101L << 5, 0x101010101010101L << 6, 0x101010101010101L << 7};
    public static final long mask_rows[] = {0xffL << 0, 0xffL << 8, 0xffL << 16, 0xffL << 24, 0xffL << 32, 0xffL << 40, 0xffL << 48, 0xffL << 56};
    public static final long bit_between[][] = generateAllSquaresBetween();
    public static final long bit_after[][] = generateAllBitsAfter();
    public static long mask_passed[][] = new long[2][64];
    public static long mask_path[][] = new long[2][64];
    public static long mask[] = new long[64];
    public static long not_mask[] = new long[64];
    public static long mask_isolated[] = new long[64];
    public static final long kingside = 0xf0f0f0f0f0f0f0f0L;
    public static final long queenside = 0x0f0f0f0f0f0f0f0fL;
    public static final long not_a_file = 0xfefefefefefefefeL;
    public static final long not_h_file = 0x7f7f7f7f7f7f7f7fL;

    public static int bits_row[] = {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,5,5,5,5,5,5,5,5,6,6,6,6,6,6,6,6,7,7,7,7,7,7,7,7};
    public static int bits_rank[] = {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7};
    
    public static int rank[][] = {{7, 6, 5, 4, 3, 2, 1, 0, 7, 6, 5, 4, 3, 2, 1, 0, 7, 6, 5, 4, 3, 2, 1, 0, 7, 6, 5, 4, 3, 2, 1, 0, 7, 6, 5, 4, 3, 2, 1, 0, 7, 6, 5, 4, 3, 2, 1, 0, 7, 6, 5, 4, 3, 2, 1, 0, 7, 6, 5, 4, 3, 2, 1, 0}, {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7}};

    public static int lsb_64_tables[] = {63, 30,  3, 32, 59, 14, 11, 33, 60, 24, 50,  9, 55, 19, 21, 34, 61, 29,  2, 53, 51, 23, 41, 18, 56, 28,  1, 43, 46, 27,  0, 35, 62, 31, 58,  4,  5, 49, 54,  6, 15, 52, 12, 40,  7, 42, 45, 16, 25, 57, 48, 13, 10, 39,  8, 44, 20, 47, 38, 22, 17, 37, 36, 26};

    // (A 1), (B 2), (C 3), (D 4), (E 5), (F 6), (G 7), (H 8)
    public static int pawnplus[][] = new int[2][64];
    public static int pawndouble[][] = new int[2][64];
    public static int pawnleft[][] = new int[2][64];
    public static int pawnright[][] = new int[2][64];

    public static Map<Long, Short> bitboardToIndex = new HashMap<>();

    public final static int[] Flip = {
        56, 57, 58, 59, 60, 61, 62, 63,
        48, 49, 50, 51, 52, 53, 54, 55,
        40, 41, 42, 43, 44, 45, 46, 47,
        32, 33, 34, 35, 36, 37, 38, 39,
        24, 25, 26, 27, 28, 29, 30, 31,
        16, 17, 18, 19, 20, 21, 22, 23,
        8, 9, 10, 11, 12, 13, 14, 15,
        0, 1, 2, 3, 4, 5, 6, 7
    };

    public MovementBitboards(){
        SetPawnBits();
        initializeBitboardToIndex();
    }

    public static long SetBit(long bb, int square){
        return bb | (1L << square);
    }

    public static long  SetBitFalse(long bb, int square){
        return bb &= ~mask[square];
    }

    public static long[] generateAllScalarToEdges(int scalar){
        long bb[] = new long[64];

        for(int i = 0; i < 64; i++){
            bb[i] = scalarToBoardEdge(i, scalar);
        }

        return bb;
    }

    private static long scalarToBoardEdge(int startIndex, int scalar){

        if(!(scalar == 1 || scalar == -1 || scalar == 8 || scalar == -8 || scalar == 9 || scalar == -9 || scalar == 7 || scalar == -7)){throw new java.lang.RuntimeException("Incorrect inputs");}

        long bb = 1L << startIndex;
        int currentSquare = startIndex;
        int nextSquare;

        while (true) {
            nextSquare = currentSquare + scalar;
            if (nextSquare >= 64 || nextSquare < 0) {break;}
            if (Math.abs((nextSquare % 8) - (currentSquare % 8)) > 1) {break;}
            currentSquare = nextSquare;
            bb |= 1L << currentSquare;
        }

        return bb;
    }

    private static long[][] generateAllBitsAfter(){
        long bitsAfter[][] = new long[64][64];
        for(int i = 0; i < 64; i++){
            for(int y = 0; y < 64; y++){
                bitsAfter[i][y] = getBitsAfter(i, y);
            }   
        }
        return bitsAfter;
    }

    private static long getBitsAfter(int startIndex, int endIndex) {
        
        int startIndexRow = startIndex / 8;
        int startIndexCol = startIndex % 8;
        int endIndexRow = endIndex / 8;
        int endIndexCol = endIndex % 8;

        if (startIndexRow == endIndexRow) {
            // horizontal move ----
            return scalarToBoardEdge(endIndex, startIndex > endIndex ? -1 : 1);
        } else if (startIndexCol == endIndexCol) {
            // Vertical move ||||
            return scalarToBoardEdge(endIndex, startIndex > endIndex ? -8 : 8);
        } else if (Math.abs(startIndexRow - endIndexRow) == Math.abs(startIndexCol - endIndexCol)) {
            // Diagonal move /\/\/\\/
            int direction;
            if (startIndex < endIndex) {
                direction = (startIndexRow < endIndexRow) ? 9 : 7;
            } else {
                direction = (startIndexRow < endIndexRow) ? -7 : -9;
            }
            return scalarToBoardEdge(endIndex, direction);
        } else {
            return 0L;
        }
    }
    
    private static long[][] generateAllSquaresBetween(){
        long[][] allSquaresBetween = new long[64][64];
        for (int startIndex = 0; startIndex < 64; startIndex++) {
            for(int endIndex = 0; endIndex < 64; endIndex++){
                allSquaresBetween[startIndex][endIndex] = getSquaresBetween(startIndex, endIndex);
            }
        }
        return allSquaresBetween;
    }

    private static long getSquaresBetween(int startIndex, int endIndex) {
        
        if (startIndex == endIndex) {
            return 0L;
        }
        
        long squaresBetween = 0L;
        
        int startRow = startIndex / 8;
        int startCol = startIndex % 8;
        int endRow = endIndex / 8;
        int endCol = endIndex % 8;
        
        if (startRow == endRow) { // same row
            int step = (startIndex < endIndex) ? 1 : -1;
            for (int i = startIndex + step; i != endIndex; i += step) {
                squaresBetween |= 1L << i;
            }
        } else if (startCol == endCol) { // same column
            int step = (startIndex < endIndex) ? 8 : -8;
            for (int i = startIndex + step; i != endIndex; i += step) {
                squaresBetween |= 1L << i;
            }
        } else if (Math.abs(startRow - endRow) == Math.abs(startCol - endCol)) { // diagonal
            int rowStep = (startRow < endRow) ? 1 : -1;
            int colStep = (startCol < endCol) ? 1 : -1;
            for (int i = startIndex + 8 * rowStep + colStep; i != endIndex; i += 8 * rowStep + colStep) {
                squaresBetween |= 1L << i;
            }
        } else {
            return 0L; 
        }
        
        return squaresBetween;
    }

    public static int getEdge(int sq, int plus){
        // returns the square at the edge 
        sq += plus;
        while(bits_rank[sq] > 0 && bits_rank[sq] < 7 && bits_row[sq] > 0 && bits_row[sq] < 7){
            sq += plus;
        }
        return sq;
    }

    private static void SetPawnBits(){
        // cannot reverse engineer this part into my code so im just copying out of the book wtf
        for(int x = 0; x < 64; x++){
            mask[x] = SetBit(mask[x], x);

            not_mask[x] = ~mask[x];
                pawnleft[0][x] = -1;
                pawnleft[1][x] = -1;
                pawnright[0][x] = -1;
                pawnright[1][x] = -1;

            if(bits_rank[x] > 0){
                if(bits_row[x] < 7){
                    pawnleft[0][x] = x + 7;
                }
                if(bits_row[x] > 0){
                    pawnleft[0][x] = x - 9;
                }
            }

            if(bits_rank[x] < 7){
                if(bits_row[x] < 7){
                    pawnright[0][x] = x + 9;
                }
                if(bits_row[x] > 0){
                    pawnright[0][x] = x - 7;
                }
            }

            if(bits_rank[x] > 0){
                
                if(bits_row[x] < 7){
                    bit_pawncaptures[0][x] = SetBit(bit_pawncaptures[0][x], pawnleft[0][x]);
                    bit_left[0][x] = SetBit(bit_left[0][x], pawnleft[0][x]);
                }
                
                if(bits_row[x] > 0){
                    bit_pawncaptures[1][x] = SetBit(bit_pawncaptures[1][x], pawnleft[1][x]);
                    bit_left[1][x] = SetBit(bit_left[1][x], pawnleft[1][x]);
                }
            }

            if(bits_rank[x] < 7){
                
                if(bits_row[x] < 7){
                    bit_pawncaptures[0][x] = SetBit(bit_pawncaptures[0][x], pawnright[0][x]);
                    bit_left[0][x] = SetBit(bit_left[0][x], pawnleft[0][x]);
                }
                
                if(bits_row[x] > 0){
                    bit_pawncaptures[1][x] = SetBit(bit_pawncaptures[1][x], pawnright[1][x]);
                    bit_left[1][x] = SetBit(bit_left[1][x], pawnleft[1][x]);
                }
            
            }

            bit_pawndefends[0][x] = bit_pawncaptures[1][x];
            bit_pawndefends[1][x] = bit_pawncaptures[0][x];
            
            if(bits_row[x] < 7){
                pawnplus[0][x] = x + 8;
            }

            if(bits_row[x] < 6){
                pawndouble[0][x] = x + 16;
            }

            if(bits_row[x] > 0){
                pawnplus[1][x] = x - 8;
            }

            if(bits_row[x] > 1){
                pawndouble[1][x] = x - 16;
            }
        }

        for(int x = 0; x < 64; x++){
            for(int y = 0; y < 64; y++){
                if(Math.abs(bits_rank[x] - bits_rank[y]) < 2){
                    if(bits_row[x] < bits_row[y] && bits_row[y] < 7){
                        mask_passed[0][x] = SetBit(mask_passed[0][x], y);
                    }
                    if(bits_row[x] < bits_row[y] && bits_row[y] < 0){
                        mask_passed[0][x] = SetBit(mask_passed[0][x], y);
                    }
                }

                if(Math.abs(bits_rank[x] - bits_rank[y]) == 1){
                    mask_isolated[x] = SetBit(mask_isolated[x], y);
                }

                if(bits_rank[x] == bits_rank[y]){
                    if(bits_row[x] == bits_row[y]){
                        if(bits_row[x] < bits_row[y]){
                            mask_path[0][x] = SetBit(mask_path[0][x],y);
                        }
                        if(bits_row[x] > bits_row[y]){
                            mask_path[1][x] = SetBit(mask_path[1][x],y);
                        }
                    }
                }
            }
        }
    }

    public void initializeBitboardToIndex(){

        for(int i = 0; i < 64; i++){
            bitboardToIndex.put(1L << i, (short) i);
        }
    }
}