package parkerbasicchessengine;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class test_rook_magics {

    public static final long rookMagics[] = {
            0x0080001020400080L, 0x0040001000200040L, 0x0080081000200080L, 0x0080040800100080L,
            0x0080020400080080L, 0x0080010200040080L, 0x0080008001000200L, 0x0080002040800100L,
            0x0000800020400080L, 0x0000400020005000L, 0x0000801000200080L, 0x0000800800100080L,
            0x0000800400080080L, 0x0000800200040080L, 0x0000800100020080L, 0x0000800040800100L,
            0x0000208000400080L, 0x0000404000201000L, 0x0000808010002000L, 0x0000808008001000L,
            0x0000808004000800L, 0x0000808002000400L, 0x0000010100020004L, 0x0000020000408104L,
            0x0000208080004000L, 0x0000200040005000L, 0x0000100080200080L, 0x0000080080100080L,
            0x0000040080080080L, 0x0000020080040080L, 0x0000010080800200L, 0x0000800080004100L,
            0x0000204000800080L, 0x0000200040401000L, 0x0000100080802000L, 0x0000080080801000L,
            0x0000040080800800L, 0x0000020080800400L, 0x0000020001010004L, 0x0000800040800100L,
            0x0000204000808000L, 0x0000200040008080L, 0x0000100020008080L, 0x0000080010008080L,
            0x0000040008008080L, 0x0000020004008080L, 0x0000010002008080L, 0x0000004081020004L,
            0x0000204000800080L, 0x0000200040008080L, 0x0000100020008080L, 0x0000080010008080L,
            0x0000040008008080L, 0x0000020004008080L, 0x0000800100020080L, 0x0000800041000080L,
            0x00FFFCDDFCED714AL, 0x007FFCDDFCED714AL, 0x003FFFCDFFD88096L, 0x0000040810002101L,
            0x0001000204080011L, 0x0001000204000801L, 0x0001000082000401L, 0x0001FFFAABFAD1A2L
    };

    public static final long[] rookMask = {
            0x000101010101017EL, 0x000202020202027CL, 0x000404040404047AL, 0x0008080808080876L,
            0x001010101010106EL, 0x002020202020205EL, 0x004040404040403EL, 0x008080808080807EL,
            0x0001010101017E00L, 0x0002020202027C00L, 0x0004040404047A00L, 0x0008080808087600L,
            0x0010101010106E00L, 0x0020202020205E00L, 0x0040404040403E00L, 0x0080808080807E00L,
            0x00010101017E0100L, 0x00020202027C0200L, 0x00040404047A0400L, 0x0008080808760800L,
            0x00101010106E1000L, 0x00202020205E2000L, 0x00404040403E4000L, 0x00808080807E8000L,
            0x000101017E010100L, 0x000202027C020200L, 0x000404047A040400L, 0x0008080876080800L,
            0x001010106E101000L, 0x002020205E202000L, 0x004040403E404000L, 0x008080807E808000L,
            0x0001017E01010100L, 0x0002027C02020200L, 0x0004047A04040400L, 0x0008087608080800L,
            0x0010106E10101000L, 0x0020205E20202000L, 0x0040403E40404000L, 0x0080807E80808000L,
            0x00017E0101010100L, 0x00027C0202020200L, 0x00047A0404040400L, 0x0008760808080800L,
            0x00106E1010101000L, 0x00205E2020202000L, 0x00403E4040404000L, 0x00807E8080808000L,
            0x007E010101010100L, 0x007C020202020200L, 0x007A040404040400L, 0x0076080808080800L,
            0x006E101010101000L, 0x005E202020202000L, 0x003E404040404000L, 0x007E808080808000L,
            0x7E01010101010100L, 0x7C02020202020200L, 0x7A04040404040400L, 0x7608080808080800L,
            0x6E10101010101000L, 0x5E20202020202000L, 0x3E40404040404000L, 0x7E80808080808000L
    };

    public static final int[] relavantBits = 
          { 55, 55, 55, 55, 55, 55, 55, 55,
            55, 52, 52, 52, 52, 52, 52, 55,
            55, 52, 52, 52, 52, 52, 52, 55,
            55, 52, 52, 52, 52, 52, 52, 55,
            55, 52, 52, 52, 52, 52, 52, 55,
            55, 52, 52, 52, 52, 52, 52, 55,
            55, 52, 52, 52, 52, 52, 52, 55,
            55, 55, 55, 55, 55, 55, 55, 55 };

    public static long[][] magicIndexToMoves = new long[64][4096];

    private static void printBitboard(long bitboard) {
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                int square = rank * 8 + file;
                boolean isSet = (bitboard & (1L << square)) != 0;
                System.out.print(isSet ? "1 " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void test() {
        for (int rookIndex = 0; rookIndex < 64; rookIndex++) {
            for (long blockers = 0; blockers < 64; blockers++) {
                int magicIndex = (int) ((blockers * rookMagics[rookIndex]) >>> (relavantBits[rookIndex]));
                long moves = calculateRookMove(rookIndex, blockers);
                magicIndexToMoves[rookIndex][magicIndex] = moves;
            }
        }
        try {
            saveRookMovesBinary(magicIndexToMoves, "RookMagicIndexToMove.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

    private long calculateRookMove(int rookIndex, long relevantBlockers) {
        long rookMove = 0L;        
    
        if (rookIndex / 8 != 0) {
            int square = rookIndex + 8;
            while (square < 64 && (relevantBlockers & (1L << square)) == 0) {
                rookMove |= (1L << square);
                square += 8;
            }
        }
    
        if (rookIndex / 8 != 7) {
            int square = rookIndex - 8;
            while (square >= 0 && (relevantBlockers & (1L << square)) == 0) {
                rookMove |= (1L << square);
                square -= 8;
            }
        }
    
        if (rookIndex % 8 != 7) {
            int square = rookIndex + 1;
            while (square % 8 != 0 && (relevantBlockers & (1L << square)) == 0) {
                rookMove |= (1L << square);
                square += 1;
            }
        }
    
        if (rookIndex % 8 != 0) {  
            int square = rookIndex - 1;
            while (square % 8 != 7 && (relevantBlockers & (1L << square)) == 0) {  
                rookMove |= (1L << square);
                square -= 1;
            }
        }
    
        return rookMove;
    }
    
    public long genRookMoves(int rookIndex, long blockerBitboard) {

        long blockers = blockerBitboard & rookMask[rookIndex];
        int magicIndex = (int) ((blockers * rookMagics[rookIndex]) >>> (64 - relavantBits[rookIndex]));
        long possibleMoves = magicIndexToMoves[rookIndex][magicIndex];

        return possibleMoves;
    }

    public static void saveRookMovesBinary(long[][] rookMoves, String fileName) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName))) {
            for (int i = 0; i < rookMoves.length; i++) {
                for (int j = 0; j < rookMoves[i].length; j++) {
                    out.writeLong(rookMoves[i][j]);
                }
            }
        }
    }

}
