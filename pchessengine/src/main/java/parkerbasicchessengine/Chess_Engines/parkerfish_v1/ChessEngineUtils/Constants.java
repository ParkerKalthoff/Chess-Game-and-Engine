package parkerbasicchessengine.Chess_Engines.parkerfish_v1.ChessEngineUtils;

public class Constants {
    
    public static final int A8 = 0;
    public static final int B8 = 1;
    public static final int C8 = 2;
    public static final int D8 = 3;
    public static final int E8 = 4;
    public static final int F8 = 5;
    public static final int G8 = 6;
    public static final int H8 = 7;

    public static final int A7 = 8;
    public static final int B7 = 9;
    public static final int C7 = 10;
    public static final int D7 = 11;
    public static final int E7 = 12;
    public static final int F7 = 13;
    public static final int G7 = 14;
    public static final int H7 = 15;

    public static final int A6 = 16;
    public static final int B6 = 17;
    public static final int C6 = 18;
    public static final int D6 = 19;
    public static final int E6 = 20;
    public static final int F6 = 21;
    public static final int G6 = 22;
    public static final int H6 = 23;

    public static final int A5 = 24;
    public static final int B5 = 25;
    public static final int C5 = 26;
    public static final int D5 = 27;
    public static final int E5 = 28;
    public static final int F5 = 29;
    public static final int G5 = 30;
    public static final int H5 = 31;

    public static final int A4 = 32;
    public static final int B4 = 33;
    public static final int C4 = 34;
    public static final int D4 = 35;
    public static final int E4 = 36;
    public static final int F4 = 37;
    public static final int G4 = 38;
    public static final int H4 = 39;

    public static final int A3 = 40;
    public static final int B3 = 41;
    public static final int C3 = 42;
    public static final int D3 = 43;
    public static final int E3 = 44;
    public static final int F3 = 45;
    public static final int G3 = 46;
    public static final int H3 = 47;

    public static final int A2 = 48;
    public static final int B2 = 49;
    public static final int C2 = 50;
    public static final int D2 = 51;
    public static final int E2 = 52;
    public static final int F2 = 53;
    public static final int G2 = 54;
    public static final int H2 = 55;

    public static final int A1 = 56;
    public static final int B1 = 57;
    public static final int C1 = 58;
    public static final int D1 = 59;
    public static final int E1 = 60;
    public static final int F1 = 61;
    public static final int G1 = 62;
    public static final int H1 = 63;
    
    // Directions

    public static final int NORTH = -8;
    public static final int NE = -7;
    public static final int EAST = 1;
    public static final int SE = 9;
    public static final int SOUTH = 8;
    public static final int SW = 7;
    public static final int WEST = -1;
    public static final int NW = -9;

    // pieces

    public static final int K = 0;
    public static final int Q = 1;
    public static final int B = 2;
    public static final int N = 3;
    public static final int R = 4;
    public static final int P = 5;
    
    // teams

    public static final int White = 0;
    public static final int Black = 1;

    // files 

    //  ^
    //  |
    //  v

    public static final long FILE_A = 0x0101010101010101L;
    public static final long FILE_B = 0x0202020202020202L;
    public static final long FILE_C = 0x0404040404040404L;
    public static final long FILE_D = 0x0808080808080808L;
    public static final long FILE_E = 0x1010101010101010L;
    public static final long FILE_F = 0x2020202020202020L;
    public static final long FILE_G = 0x4040404040404040L;
    public static final long FILE_H = 0x8080808080808080L;

    // Ranks <->
    public static final long RANK_8 = 0x00000000000000FFL;
    public static final long RANK_7 = 0x000000000000FF00L;
    public static final long RANK_6 = 0x0000000000FF0000L;
    public static final long RANK_5 = 0x00000000FF000000L;
    public static final long RANK_4 = 0x000000FF00000000L;
    public static final long RANK_3 = 0x0000FF0000000000L;
    public static final long RANK_2 = 0x00FF000000000000L;
    public static final long RANK_1 = 0xFF00000000000000L;

    // White castle
    public static final int KING_SIDE_WHITE_CASTLE = 62;
    public static final int QUEEN_SIDE_WHITE_CASTLE = 58;

    public static final long MASK_KING_WHITE = (1L << 62) | (1L << 61);
    public static final long MASK_QUEEN_WHITE = (1L << 58) | (1L << 57) | (1L << 59);

    // Black castle
    public static final int KING_SIDE_BLACK_CASTLE = 6;
    public static final int QUEEN_SIDE_BLACK_CASTLE = 2;

    public static final long MASK_KING_BLACK = (1L << 6) | (1L << 5);
    public static final long MASK_QUEEN_BLACK = (1L << 2) | (1L << 3) | (1L << 1);

    // Rook spots
    public static final long BLACK_KING_ROOK = 1L << 7;
    public static final long BLACK_QUEEN_ROOK = 1L << 0;
    public static final long BLACK_KING_ROOK_CASTLED = 1L << 5;
    public static final long BLACK_QUEEN_ROOK_CASTLED = 1L << 3;


    public static final long WHITE_KING_ROOK = 1L << 63;
    public static final long WHITE_QUEEN_ROOK = 1L << 56;
    public static final long WHITE_KING_ROOK_CASTLED = 1L << 61;
    public static final long WHITE_QUEEN_ROOK_CASTLED = 1L << 59;

    public static final int WHITE_CASTLE_BITS = 0b1100;
    public static final int BLACK_CASTLE_BITS =  0b0011;

    // 0b KQkq

    public static final int WHITE_KING_CASTLE_BIT = 0b1000;
    public static final int WHITE_QUEEN_CASTLE_BIT = 0b0100;
    public static final int BLACK_KING_CASTLE_BIT =  0b0010;
    public static final int BLACK_QUEEN_CASTLE_BIT =  0b0001;

    // methods from old project

    public static final long bit_between[][] = generateAllSquaresBetween();

    public static final long bit_after[][] = generateAllBitsAfter();
    

    // help functions

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



}
