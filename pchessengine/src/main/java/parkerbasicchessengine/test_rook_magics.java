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

    public static final bishopMask = 
              { 9241421688590303744L, 36099303471056128L, 141012904249856L, 550848566272L, 
                6480472064L, 1108177604608L, 283691315142656L, 72624976668147712L, 
                4620710844295151618L, 9241421688590368773L, 36099303487963146L, 141017232965652L, 
                1659000848424L, 283693466779728L, 72624976676520096L, 145249953336262720L, 
                2310355422147510788L, 4620710844311799048L, 9241421692918565393L, 36100411639206946L, 
                424704217196612L, 72625527495610504L, 145249955479592976L, 290499906664153120L, 1
                155177711057110024L, 2310355426409252880L, 4620711952330133792L, 9241705379636978241L, 
                108724279602332802L, 145390965166737412L, 290500455356698632L, 580999811184992272L, 
                577588851267340304L, 1155178802063085600L, 2310639079102947392L, 4693335752243822976L, 
                9386671504487645697L, 326598935265674242L, 581140276476643332L, 1161999073681608712L, 
                288793334762704928L, 577868148797087808L, 1227793891648880768L, 2455587783297826816L, 
                4911175566595588352L, 9822351133174399489L, 1197958188344280066L, 2323857683139004420L, 
                144117404414255168L, 360293502378066048L, 720587009051099136L, 1441174018118909952L, 
                2882348036221108224L, 5764696068147249408L, 11529391036782871041L, 4611756524879479810L, 
                567382630219904L, 1416240237150208L, 2833579985862656L, 5667164249915392L, 
                11334324221640704L, 22667548931719168L, 45053622886727936L, 18049651735527937L}

    public static final int[] RookShifts = 
              { 52, 52, 52, 52, 52, 52, 52, 52, 
                53, 53, 53, 54, 53, 53, 54, 53, 
                53, 54, 54, 54, 53, 53, 54, 53, 
                53, 54, 53, 53, 54, 54, 54, 53, 
                52, 54, 53, 53, 53, 53, 54, 53, 
                52, 53, 54, 54, 53, 53, 54, 53, 
                53, 54, 54, 54, 53, 53, 54, 53, 
                52, 53, 53, 53, 53, 53, 53, 52 };

    public static final int[] BishopShifts = 
                { 58, 60, 59, 59, 59, 59, 60, 58, 
                  60, 59, 59, 59, 59, 59, 59, 60, 
                  59, 59, 57, 57, 57, 57, 59, 59, 
                  59, 59, 57, 55, 55, 57, 59, 59, 
                  59, 59, 57, 55, 55, 57, 59, 59, 
                  59, 59, 57, 57, 57, 57, 59, 59, 
                  60, 60, 59, 59, 59, 59, 60, 60, 
                  58, 60, 59, 59, 59, 59, 59, 58 };

    public static long[] RookMagics = 
                { 468374916371625120L, 18428729537625841661L, 2531023729696186408L, 6093370314119450896L, 
                  13830552789156493815L, 16134110446239088507L, 12677615322350354425L, 5404321144167858432L, 
                  2111097758984580L, 18428720740584907710L, 17293734603602787839L, 4938760079889530922L, 
                  7699325603589095390L, 9078693890218258431L, 578149610753690728L, 9496543503900033792L, 
                  1155209038552629657L, 9224076274589515780L, 1835781998207181184L, 509120063316431138L, 
                  16634043024132535807L, 18446673631917146111L, 9623686630121410312L, 4648737361302392899L, 
                  738591182849868645L, 1732936432546219272L, 2400543327507449856L, 5188164365601475096L, 
                  10414575345181196316L, 1162492212166789136L, 9396848738060210946L, 622413200109881612L, 
                  7998357718131801918L, 7719627227008073923L, 16181433497662382080L, 18441958655457754079L, 
                  1267153596645440L, 18446726464209379263L, 1214021438038606600L, 4650128814733526084L, 
                  9656144899867951104L, 18444421868610287615L, 3695311799139303489L, 10597006226145476632L, 
                  18436046904206950398L, 18446726472933277663L, 3458977943764860944L, 39125045590687766L, 
                  9227453435446560384L, 6476955465732358656L, 1270314852531077632L, 2882448553461416064L, 
                  11547238928203796481L, 1856618300822323264L, 2573991788166144L, 4936544992551831040L, 
                  13690941749405253631L, 15852669863439351807L, 18302628748190527413L, 12682135449552027479L, 
                  13830554446930287982L, 18302628782487371519L, 7924083509981736956L, 4734295326018586370L };

    public static long[] BishopMagics = 
                { 16509839532542417919L, 14391803910955204223L, 1848771770702627364L, 347925068195328958L, 
                  5189277761285652493L, 3750937732777063343L, 18429848470517967340L, 17870072066711748607L, 
                  16715520087474960373L, 2459353627279607168L, 7061705824611107232L, 8089129053103260512L, 
                  7414579821471224013L, 9520647030890121554L, 17142940634164625405L, 9187037984654475102L, 
                  4933695867036173873L, 3035992416931960321L, 15052160563071165696L, 5876081268917084809L, 
                  1153484746652717320L, 6365855841584713735L, 2463646859659644933L, 1453259901463176960L, 
                  9808859429721908488L, 2829141021535244552L, 576619101540319252L, 5804014844877275314L, 
                  4774660099383771136L, 328785038479458864L, 2360590652863023124L, 569550314443282L, 
                  17563974527758635567L, 11698101887533589556L, 5764964460729992192L, 6953579832080335136L, 
                  1318441160687747328L, 8090717009753444376L, 16751172641200572929L, 5558033503209157252L, 
                  17100156536247493656L, 7899286223048400564L, 4845135427956654145L, 2368485888099072L, 
                  2399033289953272320L, 6976678428284034058L, 3134241565013966284L, 8661609558376259840L, 
                  17275805361393991679L, 15391050065516657151L, 11529206229534274423L, 9876416274250600448L, 
                  16432792402597134585L, 11975705497012863580L, 11457135419348969979L, 9763749252098620046L, 
                  16960553411078512574L, 15563877356819111679L, 14994736884583272463L, 9441297368950544394L, 
                  14537646123432199168L, 9888547162215157388L, 18140215579194907366L, 18374682062228545019L };


    public static long[][] rookMagicIndexToMoves = new long[64][4096];
    public static long[][] bishopMagicIndexToMoves = new long[64][4096];

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
        generateMoves();

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
        int magicIndex = (int) ((blockers * rookMagics[rookIndex]) >>> (64 - RookShifts[rookIndex]));
        long possibleMoves = rookMagicIndexToMoves[rookIndex][magicIndex];

        return possibleMoves;
    }

    public static void saveRookMovesBinary(long[][] rookMoves, String fileName) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName))) {
            for (long[] rookMove : rookMoves) {
                for (int j = 0; j < rookMove.length; j++) {
                    out.writeLong(rookMove[j]);
                }
            }
        }
    }

    public void generateMoves() {
        // Generate rook moves
        for (int rookIndex = 0; rookIndex < 64; rookIndex++) {
            for (long blockers = 0; blockers < (1L << RookShifts[rookIndex]); blockers++) {
                int magicIndex = (int) ((blockers * rookMagics[rookIndex]) >>> (64 - RookShifts[rookIndex]));
                long moves = calculateRookMove(rookIndex, blockers);
                rookMagicIndexToMoves[rookIndex][magicIndex] = moves;
            }
        }
    
        // Generate bishop moves
        for (int bishopIndex = 0; bishopIndex < 64; bishopIndex++) {
            for (long blockers = 0; blockers < (1L << BishopShifts[bishopIndex]); blockers++) {
                int magicIndex = (int) ((blockers * BishopMagics[bishopIndex]) >>> (64 - BishopShifts[bishopIndex]));
                long moves = calculateBishopMove(bishopIndex, blockers);
                bishopMagicIndexToMoves[bishopIndex][magicIndex] = moves;
            }
        }
    
        try {
            saveRookMovesBinary(rookMagicIndexToMoves, "RookMagicIndexToMove.dat");
            saveRookMovesBinary(bishopMagicIndexToMoves, "BishopMagicIndexToMove.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        System.out.println("Done");
    }
    
    private long calculateBishopMove(int bishopIndex, long relevantBlockers) {
        long bishopMove = 0L;
    
        // Northeast
        int square = bishopIndex + 9;
        while (square < 64 && square % 8 != 0 && (relevantBlockers & (1L << square)) == 0) {
            bishopMove |= (1L << square);
            square += 9;
        }
    
        // Northwest
        square = bishopIndex + 7;
        while (square < 64 && square % 8 != 7 && (relevantBlockers & (1L << square)) == 0) {
            bishopMove |= (1L << square);
            square += 7;
        }
    
        // Southeast
        square = bishopIndex - 7;
        while (square >= 0 && square % 8 != 0 && (relevantBlockers & (1L << square)) == 0) {
            bishopMove |= (1L << square);
            square -= 7;
        }
    
        // Southwest
        square = bishopIndex - 9;
        while (square >= 0 && square % 8 != 7 && (relevantBlockers & (1L << square)) == 0) {
            bishopMove |= (1L << square);
            square -= 9;
        }
    
        return bishopMove;
    }
    
    public long genBishopMoves(int bishopIndex, long blockerBitboard) {
        long blockers = blockerBitboard & bishopMask[bishopIndex];
        int magicIndex = (int) ((blockers * bishopMagics[bishopIndex]) >>> (64 - relavantBits[bishopIndex]));
        long possibleMoves = bishopMagicIndexToMoves[bishopIndex][magicIndex];
    
        return possibleMoves;
    }
    

}
