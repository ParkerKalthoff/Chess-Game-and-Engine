package parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills;

public class consts {

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
    
    public static final int NULL_SQUARE = -1;
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

    public static final int NULL_PIECE = -1;
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


    // Castling stuff


    // White castling
    public static final int WHITE_KING_START = E1;
    public static final int WHITE_KINGSIDE_ROOK_START = H1;
    public static final int WHITE_QUEENSIDE_ROOK_START = A1;

    // White king end squares
    public static final int WHITE_KINGSIDE_KING_END = G1;
    public static final int WHITE_QUEENSIDE_KING_END = C1;

    // White castling rook destinations
    public static final int WHITE_KINGSIDE_ROOK_END = F1;
    public static final int WHITE_QUEENSIDE_ROOK_END = D1;


    // Squares that must be empty and not attacked
    public static final long WHITE_KINGSIDE_SAFE_SQUARES = (1L << E1) | (1L << F1) | (1L << G1);
    public static final long WHITE_QUEENSIDE_SAFE_SQUARES = (1L << E1) | (1L << D1) | (1L << C1);

    // Squares between king and rook (must be empty but not necessarily safe)
    public static final long WHITE_KINGSIDE_CASTLE_PATH = (1L << F1) | (1L << G1);
    public static final long WHITE_QUEENSIDE_CASTLE_PATH = (1L << B1) | (1L << C1) | (1L << D1);

    // Black castling
    public static final int BLACK_KING_START = E8;
    public static final int BLACK_KINGSIDE_ROOK_START = H8;
    public static final int BLACK_QUEENSIDE_ROOK_START = A8;
    
    // Black king end squares
    public static final int BLACK_KINGSIDE_KING_END = G8;
    public static final int BLACK_QUEENSIDE_KING_END = C8;

    // Black castling rook destinations
    public static final int BLACK_KINGSIDE_ROOK_END = F8;
    public static final int BLACK_QUEENSIDE_ROOK_END = D8;


    // Squares that must be empty and not attacked
    public static final long BLACK_KINGSIDE_SAFE_SQUARES = (1L << E8) | (1L << F8) | (1L << G8);
    public static final long BLACK_QUEENSIDE_SAFE_SQUARES = (1L << E8) | (1L << D8) | (1L << C8);

    // Squares between king and rook (must be empty but not necessarily safe)
    public static final long BLACK_KINGSIDE_CASTLE_PATH = (1L << F8) | (1L << G8);
    public static final long BLACK_QUEENSIDE_CASTLE_PATH = (1L << B8) | (1L << C8) | (1L << D8);




}
