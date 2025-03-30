package parkerbasicchessengine;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;

import parkerbasicchessengine.Chess_Engines.parkerfish_v1.BitwiseBoard;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.BitwiseMove;
import parkerbasicchessengine.Chess_Engines.parkerfish_v1.parkerfish_v1;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chessEngineFactory;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.parkerfish_v2;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.search.Minimax;
import parkerbasicchessengine.Utils.randomPositions.randomPosition;

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

    public static void main(String[] args) throws InterruptedException, IOException {

        IChessGameInput engine = chessEngineFactory.Parkerfish_v2();

        MatchManager matchManager = new MatchManager(engine, engine);

        for(int i = 0; i < 500; i++) {
            matchManager.setRandomPosition();
            matchManager.startMatch();
        }

        matchManager.printMatchResultTotals();
    }
}
