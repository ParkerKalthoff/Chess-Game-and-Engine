package parkerbasicchessengine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

import parkerbasicchessengine.Utils.randomPositions.randomPosition;

public class MatchManager {

    private Board board;
    private JFrame frame;

    private HashMap<String, Integer> gameResults = new HashMap<>();

    public MatchManager(IChessGameInput whitePlayer, IChessGameInput blackPlayer) {
        
        board = new Board();

        if (whitePlayer != null) {
            board.engineManager.setWhitePlayer(whitePlayer);
        }
        if (blackPlayer != null) {
            board.engineManager.setBlackPlayer(blackPlayer);
        }

        setupUI(whitePlayer, blackPlayer);

    }

    private void setupUI(IChessGameInput whitePlayer, IChessGameInput blackPlayer) {

        frame = new JFrame("Chess Match");
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(board.tileSize * 8 + 15, board.tileSize * 9));
        frame.setLocationRelativeTo(null);
        frame.add(board, BorderLayout.CENTER);

        String whitePlayerName = (whitePlayer != null) ? whitePlayer.getName() : "Human";
        String blackPlayerName = (blackPlayer != null) ? blackPlayer.getName() : "Human";
        String versusText = whitePlayerName + " vs " + blackPlayerName;

        JLabel nameLabel = new JLabel(versusText, JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        nameLabel.setForeground(Color.WHITE);
        frame.add(nameLabel, BorderLayout.SOUTH);

        frame.setVisible(true);
        board.repaint();
    }

    public void setPosition(String fenString) {
        board.loadPositionFromFEN(fenString);
        board.repaint();
    }

    public void setRandomPosition() {
        
        String randomPositionFen;
        
        try {
            randomPositionFen = randomPosition.getRandomPosition();
            board.loadPositionFromFEN(randomPositionFen);
            board.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startMatch() throws InterruptedException {
        
        while (!board.isGameOver) {
            if (board.engineManager.whitePlayer == null) {
                board.awaitingPlayerMove = true;
                while (board.awaitingPlayerMove) {
                    Thread.sleep(100);
                }
            } else {
                board.engineManager.engineMakeMove();
            }

            System.out.println(" - White made move");
            board.repaint();

            if (board.engineManager.blackPlayer == null) {
                board.awaitingPlayerMove = true;
                while (board.awaitingPlayerMove) {
                    Thread.sleep(100);
                }
            } else {
                board.engineManager.engineMakeMove();
            }

            System.out.println(" - Black made move\n");
            board.repaint();
        }

        System.out.println("Game over : " + board.gameState);

        gameResults.putIfAbsent(board.gameState, 0);
        gameResults.computeIfPresent(board.gameState, (key, value) -> value + 1);        

        System.out.println(board.convertPostionToFEN());
    }

    public void printMatchResultTotals() {
        
        for (Map.Entry<String, Integer> entry : gameResults.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " times");
        }
    }
    
}