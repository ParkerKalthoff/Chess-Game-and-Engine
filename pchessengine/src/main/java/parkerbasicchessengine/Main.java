package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class Main {
    
    public final static boolean ai_verses_ai = false;
    
    public static void main(String[] args){


        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.black);

        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));

        frame.setLocationRelativeTo(null);

        boolean playerOneIsHuman = false;
        boolean playerTwoIsHuman = true;



        Board board = new Board();
        frame.add(board);

        frame.setVisible(true);

        board.repaint();

        while(true){
         
            if(board.isWhiteToMove){

                if(playerOneIsHuman){

                } else {
                    board.ai.aiMove(true);
                }

            } else {

                if(playerTwoIsHuman){

                } else {
                    board.ai.aiMove(false);
                }

            }

        }

    }
}
