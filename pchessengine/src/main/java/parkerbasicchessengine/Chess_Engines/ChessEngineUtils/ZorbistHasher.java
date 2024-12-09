package parkerbasicchessengine.Chess_Engines.ChessEngineUtils;

import java.util.Random;

import parkerbasicchessengine.Chess_Engines.BitwiseBoard;

public class ZorbistHasher {

    // a Zorbist hasher is a type of algorithm that associates a random number with
    // every unique datapoint in the game
    // so White rooks on A3 gets its own unique hash, Black knights on B3 have a
    // different number than Black knights on B4
    //
    // so in effect, you get a bunch of unique numbers describing different aspects
    // of state of the board
    //
    // When you want to get a hash for a board, you iterate over each of the piece
    // bitboards, and get the unique number for each
    // piece on each position, every time theres a 'hit', you XOR your zorbist key
    // with that number
    // Then you XOR the zorbist key with your castling rights, En passant, and
    // isWhiteToMove numbers
    //
    // So its like generating a unique hash for a given position, this is useful for
    // three-move repitions and memoization for move generation
    // There is a possibility of 2 distinct positions generating the same key, as
    // you use more than 64 bits of data to generate a 64 bit number
    // there a very low posibility of this happening though

    private static long[][] pieceHash = new long[12][64];
    private static long whiteToMoveHash;
    private static long[] enpassantSquareHash = new long[64];
    private static long[] castlingRightsHash = new long[16];

    static {
        Random random;
        random = new Random();

        for (int pieceIndex = 0; pieceIndex < 12; pieceIndex++) {
            for (int positionIndex = 0; positionIndex < 64; positionIndex++) {
                pieceHash[pieceIndex][positionIndex] = random.nextLong();
            }
        }

        whiteToMoveHash = random.nextLong();

        for (int positionIndex = 0; positionIndex < 64; positionIndex++) {
            enpassantSquareHash[positionIndex] = random.nextLong();
        }

        for (int castlingRights = 0; castlingRights < 16; castlingRights++) {
            castlingRightsHash[castlingRights] = random.nextLong();
        }
    }

    public long generateZorbistHash(BitwiseBoard bwB) {

        long zorbistKey = 0;

        // hash position
        for (int teamIndex = 0; teamIndex < 2; teamIndex++) {

            int pieceIndexTeam = (teamIndex * 6);

            for (int pieceIndex = 0; pieceIndex < 6; pieceIndex++) {
                for (int positionIndex = 0; positionIndex < 64; positionIndex++) {

                    long targetSquare = 1L << positionIndex;

                    if ((bwB.piece_bitboards[teamIndex][pieceIndex] & targetSquare) > 0) {
                        zorbistKey ^= pieceHash[pieceIndex + pieceIndexTeam][positionIndex];
                    }
                }
            }
        }

        // hash turn
        if (bwB.isWhiteToMove) {
            zorbistKey ^= whiteToMoveHash;
        }

        // hash enpassant
        if (bwB.enpassantIndex != -1) {
            zorbistKey ^= enpassantSquareHash[bwB.enpassantIndex];
        }

        // hash castling rights
        zorbistKey ^= castlingRightsHash[bwB.castlingRights];

        return zorbistKey;
    }
}
