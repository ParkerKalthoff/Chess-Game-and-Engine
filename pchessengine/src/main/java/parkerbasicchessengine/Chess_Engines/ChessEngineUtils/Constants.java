package parkerbasicchessengine.Chess_Engines.ChessEngineUtils;

public class Constants {
    
    public static final int A1 = 0;
    public static final int B1 = 1;
    public static final int C1 = 2;
    public static final int D1 = 3;
    public static final int E1 = 4;
    public static final int F1 = 5;
    public static final int G1 = 6;
    public static final int H1 = 7;

    public static final int A2 = 8;
    public static final int B2 = 9;
    public static final int C2 = 10;
    public static final int D2 = 11;
    public static final int E2 = 12;
    public static final int F2 = 13;
    public static final int G2 = 14;
    public static final int H2 = 15;

    public static final int A3 = 16;
    public static final int B3 = 17;
    public static final int C3 = 18;
    public static final int D3 = 19;
    public static final int E3 = 20;
    public static final int F3 = 21;
    public static final int G3 = 22;
    public static final int H3 = 23;

    public static final int A4 = 24;
    public static final int B4 = 25;
    public static final int C4 = 26;
    public static final int D4 = 27;
    public static final int E4 = 28;
    public static final int F4 = 29;
    public static final int G4 = 30;
    public static final int H4 = 31;

    public static final int A5 = 32;
    public static final int B5 = 33;
    public static final int C5 = 34;
    public static final int D5 = 35;
    public static final int E5 = 36;
    public static final int F5 = 37;
    public static final int G5 = 38;
    public static final int H5 = 39;

    public static final int A6 = 40;
    public static final int B6 = 41;
    public static final int C6 = 42;
    public static final int D6 = 43;
    public static final int E6 = 44;
    public static final int F6 = 45;
    public static final int G6 = 46;
    public static final int H6 = 47;

    public static final int A7 = 48;
    public static final int B7 = 49;
    public static final int C7 = 50;
    public static final int D7 = 51;
    public static final int E7 = 52;
    public static final int F7 = 53;
    public static final int G7 = 54;
    public static final int H7 = 55;

    public static final int A8 = 56;
    public static final int B8 = 57;
    public static final int C8 = 58;
    public static final int D8 = 59;
    public static final int E8 = 60;
    public static final int F8 = 61;
    public static final int G8 = 62;
    public static final int H8 = 63;
    
    // Directions

    public static final int NORTH = 0;
    public static final int NE = 1;
    public static final int EAST = 2;
    public static final int SE = 3;
    public static final int SOUTH = 4;
    public static final int SW = 5;
    public static final int WEST = 6;
    public static final int NW = 7;

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
    public static final long RANK_1 = 0x00000000000000FFL;
    public static final long RANK_2 = 0x000000000000FF00L;
    public static final long RANK_3 = 0x0000000000FF0000L;
    public static final long RANK_4 = 0x00000000FF000000L;
    public static final long RANK_5 = 0x000000FF00000000L;
    public static final long RANK_6 = 0x0000FF0000000000L;
    public static final long RANK_7 = 0x00FF000000000000L;
    public static final long RANK_8 = 0xFF00000000000000L;

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
