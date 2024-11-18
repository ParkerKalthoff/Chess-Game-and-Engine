package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import parkerbasicchessengine.pieces.Bishop;
import parkerbasicchessengine.pieces.Knight;
import parkerbasicchessengine.pieces.Piece;
import parkerbasicchessengine.pieces.Queen;
import parkerbasicchessengine.pieces.Rook;

public class Input extends MouseAdapter {

    Board board;

    public Input(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        Piece pieceXY = board.getPiece(col, row);

        if (pieceXY != null) {
            board.selectedPiece = pieceXY;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null) {
            board.selectedPiece.xPos = e.getX() - board.tileSize / 2;
            board.selectedPiece.yPos = e.getY() - board.tileSize / 2;

            board.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (board.selectedPiece != null) {

            Move move;
            int col = e.getX() / board.tileSize;
            int row = e.getY() / board.tileSize;

            int colorIndex = board.selectedPiece.isWhite ? 0 : 7;

            if (row == colorIndex) {

                UIManager.put("OptionPane.background", Color.LIGHT_GRAY);
                UIManager.put("Panel.background", Color.LIGHT_GRAY);

                Image[] options = new Image[4];

                options[0] = Piece.sheet.getSubimage(1 * board.selectedPiece.sheetScale, board.selectedPiece.isWhite ? 0 : board.selectedPiece.sheetScale, board.selectedPiece.sheetScale, board.selectedPiece.sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
                options[1] = Piece.sheet.getSubimage(2 * board.selectedPiece.sheetScale, board.selectedPiece.isWhite ? 0 : board.selectedPiece.sheetScale, board.selectedPiece.sheetScale, board.selectedPiece.sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
                options[2] = Piece.sheet.getSubimage(3 * board.selectedPiece.sheetScale, board.selectedPiece.isWhite ? 0 : board.selectedPiece.sheetScale, board.selectedPiece.sheetScale, board.selectedPiece.sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
                options[3] = Piece.sheet.getSubimage(4 * board.selectedPiece.sheetScale, board.selectedPiece.isWhite ? 0 : board.selectedPiece.sheetScale, board.selectedPiece.sheetScale, board.selectedPiece.sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);

                ImageIcon[] icons = new ImageIcon[options.length];
                for (int i = 0; i < options.length; i++) {
                    icons[i] = new ImageIcon(options[i]);
                }

                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Choose a piece for promotion:",
                        "Pawn Promotion",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        icons,
                        icons[0]
                );

                Piece promoteToPiece = switch (choice) {
                    case 1 ->
                        new Queen(board, board.selectedPiece.col, board.selectedPiece.row, board.selectedPiece.isWhite);
                    case 2 ->
                        new Bishop(board, board.selectedPiece.col, board.selectedPiece.row, board.selectedPiece.isWhite);
                    case 3 ->
                        new Knight(board, board.selectedPiece.col, board.selectedPiece.row, board.selectedPiece.isWhite);
                    case 4 ->
                        new Rook(board, board.selectedPiece.col, board.selectedPiece.row, board.selectedPiece.isWhite);
                    default ->
                        throw new IllegalArgumentException("Unexpected value: " + choice);
                };

                promoteToPiece.isFirstMove = false;

                move = new Move(board, board.selectedPiece, col, row, promoteToPiece);

            } else {
                move = new Move(board, board.selectedPiece, col, row);
            }

            if (board.isValidMove(move)) {
                board.makeMove(move);
            } else {
                board.selectedPiece.xPos = board.selectedPiece.col * board.tileSize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tileSize;
            }
            
            //board.selectedPiece = null;
            board.repaint();

        }
    }
}
