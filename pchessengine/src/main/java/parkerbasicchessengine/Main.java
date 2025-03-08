package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Scanner;

import javax.swing.JFrame;

import parkerbasicchessengine.Chess_Engines.parkerfish_v1.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.parkerfish_v1;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.parkerfish_v2;

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

        parkerfish_v2 engine = new parkerfish_v2();
        Scanner scanner = new Scanner(System.in);

        while(true) {

            engine.makeMove();

            scanner.nextLine();

        }            


        /*
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.black);

        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));

        frame.setLocationRelativeTo(null);

        boolean playerOneIsHuman = true;
        boolean playerTwoIsHuman = false;

        Board board = new Board();
        // incorrect promotion is causing out of bounds error
        frame.add(board);

        frame.setVisible(true);

        board.repaint();

        parkerfish_v1 parkerTest = new parkerfish_v1(board);

        Scanner scanner = new Scanner(System.in);

        System.out.println(parkerTest.bwB.toFenString());

        while(!board.isGameOver){
            parkerTest.makeMove();
            board.repaint();
        }
         */
    }
}
