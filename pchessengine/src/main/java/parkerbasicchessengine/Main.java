package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import parkerbasicchessengine.Chess_Engines.parkerfish_v1.parkerfish_v1;

public class Main {
    
    public final static boolean ai_verses_ai = false;
    
    public static void main(String[] args){
        
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
    }
}
