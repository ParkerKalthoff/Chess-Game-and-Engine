import java.util.*;

public class BoardGeneration{

    public static void main(String[] args) {
        initiatedStandardChess();
    }

    public static void initiatedStandardChess(){
        long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;

        String chessboard[][] = {
            {"r","n","b","q","k","b","n","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {"R","N","B","Q","K","B","N","R"},
        };

        arrayToBitboards(chessboard, WP, WN, WB, WR, WQ, WK, BP , BN, BB, BR, BQ, BK);
    }

    public static void initiateChess960(){
        long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;

        String chessboard[][] = {
            {" "," "," "," "," "," "," "," "},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {" "," "," "," "," "," "," "," "},
        };

        int random1 = (int) Math.random() * 8;
        chessboard[0][random1] = "b";
        chessboard[7][random1] = "B";

        int random2 = (int) Math.random() * 8;

        while (random2 % 2 == random1 % 2 ) {
            random2 = (int) Math.random() * 8;
        }
        chessboard[0][random2] = "b";
        chessboard[7][random2] = "B";

        int random3 = (int) Math.random() * 8;

        while (random3 == random2 || random3 == random1) {
            random2 = (int) Math.random() * 8;
        }
        chessboard[0][random2] = "b";
        chessboard[7][random2] = "B";

        arrayToBitboards(chessboard, WP, WN, WB, WR, WQ, WK, BP , BN, BB, BR, BQ, BK);
    }

    public static void arrayToBitboards(String[][] chessBoard, long WP, long WN, long WB, long WR, long WQ, long WK, long BP , long BN, long BB, long BR, long BQ, long BK){

        for(int i = 0; i < 64; i++){
            long bit = 1L << i;

            switch (chessBoard[i/8][i%8]){
                case "P" : WP |= bit;break;
                case "N" : WN |= bit;break;
                case "B" : WB |= bit;break;
                case "R" : WR |= bit;break;
                case "Q" : WQ |= bit;break;
                case "K" : WK |= bit;break;
                case "p" : BP |= bit;break;
                case "n" : BN |= bit;break;
                case "b" : BB |= bit;break;
                case "r" : BR |= bit;break;
                case "q" : BQ |= bit;break;
                case "k" : BK |= bit;break;
            }
        }
        drawArray(WP, WN, WB, WR, WQ, WK, BP , BN, BB, BR, BQ, BK);
    }

    public static long convertStringToBitboard(String Binary){
        if(Binary.charAt(0) == '0'){
            return Long.parseLong(Binary,2);
        } else {
            return Long.parseLong("1"+Binary.substring(2), 2) * 2;
        }
    }

    public static void drawArray(long WP, long WN, long WB, long WR, long WQ, long WK, long BP , long BN, long BB, long BR, long BQ, long BK){
        String chessBoard[][] = new String[8][8];

        for(int i = 0; i < 64; i++){
            chessBoard[i/8][i%8] = " ";
        }

        for(int i = 0; i < 64; i++){
            if(((WP >> i) & 1) == 1){chessBoard[i/8][i%8] = "P";}
            if(((WN >> i) & 1) == 1){chessBoard[i/8][i%8] = "N";}
            if(((WB >> i) & 1) == 1){chessBoard[i/8][i%8] = "B";}
            if(((WR >> i) & 1) == 1){chessBoard[i/8][i%8] = "R";}
            if(((WQ >> i) & 1) == 1){chessBoard[i/8][i%8] = "Q";}
            if(((WK >> i) & 1) == 1){chessBoard[i/8][i%8] = "K";}
            if(((BP >> i) & 1) == 1){chessBoard[i/8][i%8] = "p";}
            if(((BN >> i) & 1) == 1){chessBoard[i/8][i%8] = "n";}
            if(((BB >> i) & 1) == 1){chessBoard[i/8][i%8] = "b";}
            if(((BR >> i) & 1) == 1){chessBoard[i/8][i%8] = "r";}
            if(((BQ >> i) & 1) == 1){chessBoard[i/8][i%8] = "q";}
            if(((BK >> i) & 1) == 1){chessBoard[i/8][i%8] = "k";}
        }

        for(int i = 0; i < 8; i++){
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }


}