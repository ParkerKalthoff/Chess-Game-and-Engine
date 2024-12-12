package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen;

import java.util.ArrayList;
import java.util.List;

public class SlidingMovementGeneration {

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

    public static final long[] bishopMask = {
            18049651735527936L, 70506452091904L, 275415828992L, 1075975168L,
            38021120L, 8657588224L, 2216338399232L, 567382630219776L,
            9024825867763712L, 18049651735527424L, 70506452221952L, 275449643008L,
            9733406720L, 2216342585344L, 567382630203392L, 1134765260406784L,
            4512412933816832L, 9024825867633664L, 18049651768822272L, 70515108615168L,
            2491752130560L, 567383701868544L, 1134765256220672L, 2269530512441344L,
            2256206450263040L, 4512412900526080L, 9024834391117824L, 18051867805491712L,
            637888545440768L, 1135039602493440L, 2269529440784384L, 4539058881568768L,
            1128098963916800L, 2256197927833600L, 4514594912477184L, 9592139778506752L,
            19184279556981248L, 2339762086609920L, 4538784537380864L, 9077569074761728L,
            562958610993152L, 1125917221986304L, 2814792987328512L, 5629586008178688L,
            11259172008099840L, 22518341868716544L, 9007336962655232L, 18014673925310464L,
            2216338399232L, 4432676798464L, 11064376819712L, 22137335185408L,
            44272556441600L, 87995357200384L, 35253226045952L, 70506452091904L,
            567382630219776L, 1134765260406784L, 2832480465846272L, 5667157807464448L,
            11333774449049600L, 22526811443298304L, 9024825867763712L, 18049651735527936L
    };

    public static final long[] RookMagics = { 468374916371625120L, -18014536083709955L, 2531023729696186408L,
            6093370314119450896L, -4616191284553057801L, -2312633627470463109L, -5769128751359197191L,
            5404321144167858432L, 2111097758984580L, -18023333124643906L, -1153009470106763777L, 4938760079889530922L,
            7699325603589095390L, 9078693890218258431L, 578149610753690728L, -8950200569809517824L,
            1155209038552629657L, -9222667799120035836L, 1835781998207181184L, 509120063316431138L,
            -1812701049577015809L, -70441792405505L, -8823057443588141304L, 4648737361302392899L, 738591182849868645L,
            1732936432546219272L, 2400543327507449856L, 5188164365601475096L, -8032168728528355300L,
            1162492212166789136L, -9049895335649340670L, 622413200109881612L, 7998357718131801918L,
            7719627227008073923L, -2265310576047169536L, -4785418251797537L, 1267153596645440L, -17609500172353L,
            1214021438038606600L, 4650128814733526084L, -8790599173841600512L, -2322205099264001L, 3695311799139303489L,
            -7849737847564074984L, -10697169502601218L, -17600776273953L, 3458977943764860944L, 39125045590687766L,
            -9219290638262991232L, 6476955465732358656L, 1270314852531077632L, 2882448553461416064L,
            -6899505145505755135L, 1856618300822323264L, 2573991788166144L, 4936544992551831040L, -4755802324304297985L,
            -2594074210270199809L, -144115325519024203L, -5764608624157524137L, -4616189626779263634L,
            -144115291222180097L, 7924083509981736956L, 4734295326018586370L };

    public static final long[] BishopMagics = { -1936904541167133697L, -4054940162754347393L, 1848771770702627364L,
            347925068195328958L, 5189277761285652493L, 3750937732777063343L, -16895603191584276L, -576672006997803009L,
            -1731223986234591243L, 2459353627279607168L, 7061705824611107232L, 8089129053103260512L,
            7414579821471224013L, -8926097042819430062L, -1303803439544926211L, 9187037984654475102L,
            4933695867036173873L, 3035992416931960321L, -3394583510638385920L, 5876081268917084809L,
            1153484746652717320L, 6365855841584713735L, 2463646859659644933L, 1453259901463176960L,
            -8637884643987643128L, 2829141021535244552L, 576619101540319252L, 5804014844877275314L,
            4774660099383771136L, 328785038479458864L, 2360590652863023124L, 569550314443282L, -882769545950916049L,
            -6748642186175962060L, 5764964460729992192L, 6953579832080335136L, 1318441160687747328L,
            8090717009753444376L, -1695571432508978687L, 5558033503209157252L, -1346587537462057960L,
            7899286223048400564L, 4845135427956654145L, 2368485888099072L, 2399033289953272320L, 6976678428284034058L,
            3134241565013966284L, 8661609558376259840L, -1170938712315559937L, -3055694008192894465L,
            -6917537844175277193L, -8570327799458951168L, -2013951671112417031L, -6471038576696688036L,
            -6989608654360581637L, -8682994821610931570L, -1486190662631039042L, -2882866716890439937L,
            -3452007189126279153L, -9005446704759007222L, -3909097950277352448L, -8558196911494394228L,
            -306528494514644250L, -72062011481006597L };

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

    public static final long[][] rookMagicIndexToMoves = new long[64][4096];
    public static final long[][] bishopMagicIndexToMoves = new long[64][4096];

    static {
        generateMoves();
    }

    /*
     * -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
     * -- -- -- --
     */
    /*
     * -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
     * -- -- -- --
     */

    // These methods generate bitboards for their respective piece, it assumes capture for all blocking pieces
    // make sure to nand out friendly pieces!

    public static long generateBishopMovementBitboard(int bishopIndex, long blockerBitboard) {
        long blockers = blockerBitboard & bishopMask[bishopIndex];
        int magicIndex = (int) ((blockers * BishopMagics[bishopIndex]) >>> (BishopShifts[bishopIndex]));
        long possibleMoves = bishopMagicIndexToMoves[bishopIndex][magicIndex];

        return possibleMoves;
    }

    public static long generateRookMovementBitboard(int rookIndex, long blockerBitboard) {

        long blockers = blockerBitboard & rookMask[rookIndex];
        int magicIndex = (int) ((blockers * RookMagics[rookIndex]) >>> (RookShifts[rookIndex]));
        long possibleMoves = rookMagicIndexToMoves[rookIndex][magicIndex];

        return possibleMoves;
    }

    public static long generateQueenMovementBitboard(int queenIndex, long blockerBitboard){
        return generateBishopMovementBitboard(queenIndex, blockerBitboard) | generateRookMovementBitboard(queenIndex, blockerBitboard);
    }

    /*
     * -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
     * -- -- -- --
     */
    /*
     * -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
     * -- -- -- --
     */

    private static long calculateRookMove(int rookIndex, long relevantBlockers) {
        // calculates the actual rook move based on the blocker mask and saves it to an
        // array
        long rookMove = 0L;

        for (int square = rookIndex + 8; square < 64; square += 8) {
            rookMove |= (1L << square);
            if ((relevantBlockers & (1L << square)) != 0)
                break;
        }

        for (int square = rookIndex - 8; square >= 0; square -= 8) {
            rookMove |= (1L << square);
            if ((relevantBlockers & (1L << square)) != 0)
                break;
        }

        for (int square = rookIndex + 1; square % 8 != 0; square++) {
            rookMove |= (1L << square);
            if ((relevantBlockers & (1L << square)) != 0)
                break;
        }

        for (int square = rookIndex - 1; square % 8 != 7 && square >= 0; square--) {
            rookMove |= (1L << square);
            if ((relevantBlockers & (1L << square)) != 0)
                break;
        }

        return rookMove;
    }

    private static long calculateBishopMove(int bishopIndex, long relevantBlockers) {
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

    public static void generateMoves() {

        for (int rookIndex = 0; rookIndex < 64; rookIndex++) {
            long currentBitboard = rookMask[rookIndex];

            List<Long> blockerMasks = generatePermutations(currentBitboard);

            for (Long blockerMask : blockerMasks) {
                int magicIndex = (int) ((blockerMask * RookMagics[rookIndex]) >>> (RookShifts[rookIndex]));
                rookMagicIndexToMoves[rookIndex][magicIndex] = calculateRookMove(rookIndex, blockerMask);
            }
        }
        System.out.println("(Sliding Move Generation) Done with Rook move generation...");

        for (int bishopIndex = 0; bishopIndex < 64; bishopIndex++) {
            long currentBitboard = bishopMask[bishopIndex];

            List<Long> blockerMasks = generatePermutations(currentBitboard);

            for (Long blockerMask : blockerMasks) {
                int magicIndex = (int) ((blockerMask * BishopMagics[bishopIndex]) >>> (BishopShifts[bishopIndex]));
                bishopMagicIndexToMoves[bishopIndex][magicIndex] = calculateBishopMove(bishopIndex, blockerMask);
            }
        }
        System.out.println("(Sliding Move Generation) Done with Bishop move generation...");
    }

    private static List<Long> generatePermutations(long bitmask) {
        // utility for generating permuations of a bitmask
        // 101 -> {000, 001, 100, 101}
        // used to speed up the inital setup of precomputing moves
        // initally I tried doing a for loop from 0 to 2^64 ;(
        // you live and learn

        List<Long> permutations = new ArrayList<>();

        long subset = 0;
        do {
            permutations.add(subset);
            subset = (subset - bitmask) & bitmask;
        } while (subset != 0);

        return permutations;
    }
}
