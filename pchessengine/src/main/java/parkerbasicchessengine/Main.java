package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Scanner;

import javax.swing.JFrame;

import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.parkerfish_v1;

import static parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants.*;

public class Main {

    public final static boolean ai_verses_ai = false;

    /*
     * 00 01 02 03 04 05 06 07
     * 08 09 10 11 12 13 14 15
     * 16 17 18 19 20 21 22 23
     * 24 25 26 27 28 29 30 31
     * 32 33 34 35 36 37 38 39
     * 40 41 42 43 44 45 46 47
     * 48 49 50 51 52 53 54 55
     * 56 57 58 59 60 61 62 63
     * 
     * 1L << index
     * 
     * https://tearth.dev/bitboard-viewer/
     */

    public static void main(String[] args) {

        // TODO

        // Once all of the move gen is workign CORRECTLY, a basic eval function is due
        // Material + position of pieces + king safety should be fine I believe?
        // add an opening book too, then this program should be wrapped up

        //if(true){
        //    return;
        //}

        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.black);

        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));

        frame.setLocationRelativeTo(null);

        boolean playerOneIsHuman = true;
        boolean playerTwoIsHuman = false;

        Board board = new Board();
        frame.add(board);

        frame.setVisible(true);

        board.repaint();

        parkerfish_v1 parkerTest = new parkerfish_v1(board);

        Scanner scanner = new Scanner(System.in);

        while(!board.isGameOver){
            parkerTest.makeMove();
            scanner.nextLine();
        }
        
    }

    // debugging purposes
    public static void printBitboard(long bitboard) {

        System.out.println();
        System.out.println("    a b c d e f g h");
        System.out.println("    _ _ _ _ _ _ _ _");
        for (int rank = 0; rank < 8; rank++) {

            System.out.print("" + (8 - rank) + " | ");

            for (int file = 0; file < 8; file++) {

                int flippedAndMirroredIndex = rank * 8 + file;
                long square = (bitboard >> flippedAndMirroredIndex) & 1;
                System.out.print(square == 1 ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

}
