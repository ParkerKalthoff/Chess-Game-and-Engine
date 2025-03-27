package parkerbasicchessengine;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

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
     * Converting actual chess coordinates / custom board coordinates to engine coordinates 
     * 
     * actual/board | a|0 b|1 c|2 d|3 e|4 f|5 g|6 h|7  <-col->
     * -------------+-------------------------------------
     *      8|0     |  00  01  02  03  04  05  06  07
     *      7|1     |  08  09  10  11  12  13  14  15
     *      6|2     |  16  17  18  19  20  21  22  23
     *      5|3     |  24  25  26  27  28  29  30  31
     *      4|4     |  32  33  34  35  36  37  38  39
     *      3|5     |  40  41  42  43  44  45  46  47
     *      2|6     |  48  49  50  51  52  53  54  55
     *      1|7     |  56  57  58  59  60  61  62  63
     *    <-row->
     * 
     * 1L << index
     * 
     * https://tearth.dev/bitboard-viewer/
     */

    public static void main(String[] args) throws InterruptedException {

        // Need to build gui for testing engine matchups!!!, maybe some automation platform

        // Using this to test specific positions to ensure interboard-operability
        boolean manualInputMode = true;
        
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.black);

        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));

        frame.setLocationRelativeTo(null);

        Board board = new Board();

        frame.add(board);

        frame.setVisible(true);

        board.repaint();

        IChessGameInput testEngine = new parkerfish_v2();
        
        board.engineManager.setBlackPlayer(testEngine);
        board.engineManager.setWhitePlayer(testEngine);
        

        while (!board.isGameOver) {
            if (board.engineManager.whitePlayer == null || manualInputMode) {
                board.awaitingPlayerMove = true;
                while (board.awaitingPlayerMove) {
                    Thread.sleep(100);
                }
            } else {
                board.engineManager.whitePlayer.engineMakeMove();
            }
        
            System.out.println(" - White made move");

            board.repaint();

            if (board.engineManager.whitePlayer == null || manualInputMode) {
                board.awaitingPlayerMove = true;
                while (board.awaitingPlayerMove) {
                    Thread.sleep(100);
                }
            } else {
                board.engineManager.engineMakeMove();
            }

            System.out.println(" - Black made move");
            
            board.repaint();
        }
    }
}
