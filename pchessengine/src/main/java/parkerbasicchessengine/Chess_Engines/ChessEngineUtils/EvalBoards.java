package parkerbasicchessengine.Chess_Engines.ChessEngineUtils;

public class EvalBoards extends Constants {

    final int[] piece_value = { 100, 300, 300, 500, 900, 10000};

    static final int[] col = {
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 1, 2, 3, 4, 5, 6, 7
    };

    static final int[] row = {
            0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7
    };

    // how far you are from a corner

    static final int[] nwdiag = {
            14, 13, 12, 11, 10,  9,  8,  7,
            13, 12, 11, 10,  9,  8,  7,  6,
            12, 11, 10,  9,  8,  7,  6,  5,
            11, 10,  9,  8,  7,  6,  5,  4,
            10,  9,  8,  7,  6,  5,  4,  3,
             9,  8,  7,  6,  5,  4,  3,  2,
             8,  7,  6,  5,  4,  3,  2,  1,
             7,  6,  5,  4,  3,  2,  1,  0
    };

    final static int[] nediag = {
            7, 8, 9, 10, 11, 12, 13, 14,
            6, 7, 8,  9, 10, 11, 12, 13,
            5, 6, 7,  8,  9, 10, 11, 12,
            4, 5, 6,  7,  8,  9, 10, 11,
            3, 4, 5,  6,  7,  8,  9, 10,
            2, 3, 4,  5,  6,  7,  8,  9,
            1, 2, 3,  4,  5,  6,  7,  8,
            0, 1, 2,  3,  4,  5,  6,  7
    };

    final static int[] Flip = {
            56, 57, 58, 59, 60, 61, 62, 63,
            48, 49, 50, 51, 52, 53, 54, 55,
            40, 41, 42, 43, 44, 45, 46, 47,
            32, 33, 34, 35, 36, 37, 38, 39,
            24, 25, 26, 27, 28, 29, 30, 31,
            16, 17, 18, 19, 20, 21, 22, 23,
             8,  9, 10, 11, 12, 13, 14, 15,
             0,  1,  2,  3,  4,  5,  6,  7
    };

    final static int[] pawn_score = {
              0,   0,   0,   0,   0,   0,   0,   0,
              0,   2,   4, -12, -12,   4,   2,   0,
              0,   2,   4,   4,   4,   4,   2,   0,
              0,   2,   4,   8,   8,   4,   2,   0,
              0,   2,   4,   8,   8,   4,   2,   0,
              4,   8,  10,  16,  16,  10,   8,   4,
            100, 100, 100, 100, 100, 100, 100, 100,
              0,   0,   0,   0,   0,   0,   0,   0
    };

    final static int[] knight_score = {
             -30, -20, -10, -8, -8, -10, -20, -30,
             -16,  -6,  -2,  0,  0,  -2,  -6, -16,
              -8,  -2,   4,  6,  6,   4,  -2,  -8,
              -5,   0,   6,  8,  8,   6,   0,  -5,
              -5,   0,   6,  8,  8,   6,   0,  -5,
             -10,  -2,   4,  6,  6,   4,  -2, -10,
             -20, -10,  -2,  0,  0,  -2, -10, -20,
            -150, -20, -10, -5, -5, -10, -20, -150
    };

    final static int[] bishop_score = {
            -10, -10, -12, -10, -10, -12, -10, -10,
              0,   4,   4,   4,   4,   4,   4,   0,
              2,   4,   6,   6,   6,   6,   4,   2,
              2,   4,   6,   8,   8,   6,   4,   2,
              2,   4,   6,   8,   8,   6,   4,   2,
              2,   4,   6,   6,   6,   6,   4,   2,
            -10,   4,   4,   4,   4,   4,   4, -10,
            -10, -10, -10, -10, -10, -10, -10, -10
    };

    final static int[] rook_score = {
             4,  4,  4,  6,  6,  4,  4,  4,
             0,  0,  0,  0,  0,  0,  0,  0,
             0,  0,  0,  0,  0,  0,  0,  0,
             0,  0,  0,  0,  0,  0,  0,  0,
             0,  0,  0,  0,  0,  0,  0,  0,
            20, 20, 20, 20, 20, 20, 20, 20,
            10, 10, 10, 10, 10, 10, 10, 10
    };

    final static int[] queen_score = {
            -10, -10, -6, -4, -4, -6, -10, -10,
            -10,   2,  2,  2,  2,  2,   2, -10,
              2,   2,  2,  3,  3,  2,   2,   2,
              2,   2,  3,  4,  4,  3,   2,   2,
              2,   2,  3,  4,  4,  3,   2,   2,
              2,   2,  2,  3,  3,  2,   2,   2,
            -10,   2,  2,  2,  2,  2,   2, -10,
            -10, -10,  2,  2,  2,  2, -10, -10
    };

    final static int[] king_score = {
             20,  20,  20, -40,  10, -60,  20,  20,
             15,  20, -25, -30, -30, -45,  20,  15,
            -48, -48, -48, -48, -48, -48, -48, -48,
            -48, -48, -48, -48, -48, -48, -48, -48,
            -48, -48, -48, -48, -48, -48, -48, -48,
            -48, -48, -48, -48, -48, -48, -48, -48,
            -48, -48, -48, -48, -48, -48, -48, -48,
            -48, -48, -48, -48, -48, -48, -48, -48
    };

    final static int[] king_endgame_score = {
             0,  8, 16, 18, 18, 16,  8,  0,
             8, 16, 24, 32, 32, 24, 16,  8,
            16, 24, 32, 40, 40, 32, 24, 16,
            25, 32, 40, 48, 48, 40, 32, 25,
            25, 32, 40, 48, 48, 40, 32, 25,
            16, 24, 32, 40, 40, 32, 24, 16,
             8, 16, 24, 32, 32, 24, 16,  8,
             0,  8, 16, 18, 18, 16,  8,  0
    };

    final static int[] passed_score = {
             0,  0,  0,  0,  0,  0,  0,  0,
             0,  0,  0,  0,  0,  0,  0,  0,
            60, 60, 60, 60, 60, 60, 60, 60,
            30, 30, 30, 30, 30, 30, 30, 30,
            15, 15, 15, 15, 15, 15, 15, 15,
             8,  8,  8,  8,  8,  8,  8,  8,
             8,  8,  8,  8,  8,  8,  8,  8,
             0,  0,  0,  0,  0,  0,  0,  0

    };

    static final int[] castle_mask = {
            13, 15, 15, 15, 12, 15, 15, 14,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
             7, 15, 15, 15,  3, 15, 15, 11
    };
}

    