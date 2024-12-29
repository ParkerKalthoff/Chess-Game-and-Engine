package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import parkerbasicchessengine.Chess_Engines.parkerfish_v1.parkerfish_v1;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.MoveGenerator2;
import parkerbasicchessengine.Chess_Engines.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants;


public class Main {
    
    public final static boolean ai_verses_ai = false;
    
    public static void main(String[] args){
        
        /*
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.black);

        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));

        frame.setLocationRelativeTo(null);

        boolean playerOneIsHuman = true;
        boolean playerTwoIsHuman = true;

        Board board = new Board();
        frame.add(board);

        frame.setVisible(true);

        board.repaint();

        System.out.println("test");

        parkerfish_v1 parkerTest = new parkerfish_v1(board); 
        parkerTest.makeMove();       

        */

        long bitboard = (1L << Constants.A5) | (1L << Constants.C5);

        BitwiseBoard board = new BitwiseBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        MoveGenerator2 movegen = new MoveGenerator2(board);

        printBitboard(board.piece_bitboards[0][Constants.K]);

        System.out.println();

        printBitboard(1L << 62); // king side white castle

        
        printBitboard(1L << 58); // queen side white castle

        printBitboard(1L << 62 | 1L << 61); // mask king
        
        printBitboard(1L << 58 | 1L << 57 | 1L << 59); // queen mask

        // black
        
        printBitboard(1L << 6); // king side black castle
        printBitboard(1L << 2); // queen side black castle

        printBitboard(1L << 6 | 1L << 5); // mask king vision check
        printBitboard(1L << 2 | 1L << 3 | 1L << 1); // queen mask vision check

        // rook spots

        printBitboard(1L << 0); // black queen rook
        printBitboard(1L << 7); // black king rook
        printBitboard(1L << 63); // white king rook
        printBitboard(1L << 56); // white queen rook

    }

    public static void printBitboard(long bitboard) {

        System.out.println();

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {

                int flippedAndMirroredIndex = rank * 8 + file;
                long square = (bitboard >> flippedAndMirroredIndex) & 1;
                System.out.print(square == 1 ? "1 " : "0 ");
            }
            System.out.println();
        }
    }
    
}
