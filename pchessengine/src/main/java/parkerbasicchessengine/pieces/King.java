package parkerbasicchessengine.pieces;

import java.awt.image.BufferedImage;

import parkerbasicchessengine.Board;
import parkerbasicchessengine.Move;

public class King extends Piece {

    public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;

        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        this.name = "King";

        this.sprite = sheet.getSubimage(0, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {

        if (col >= 8 || col <= -1 || row >= 8 || row <= -1) {
            return false;
        }

        return Math.abs((col - this.col) * (row - this.row)) == 1 || Math.abs((col - this.col)) + Math.abs((row - this.row)) == 1;
    }

    public boolean canCastle(int col, int row) {

        if (this.row != row) {
            return false;
        }

        if (col == 6) {
            Piece rook = board.getPiece(7, row);
            if (rook != null && rook.isFirstMove && this.isFirstMove) {
                return board.getPiece(5, row) == null 
                    && board.getPiece(6, row) == null 
                    && !board.checkScanner.isKingChecked(new Move(board, this, 5, row));
            }
        } 

        if (col == 2) {
            Piece rook = board.getPiece(0, row);
            if (rook != null && rook.isFirstMove && this.isFirstMove) {
                return board.getPiece(3, row) == null 
                    && board.getPiece(2, row) == null 
                    && board.getPiece(1, row) == null 
                    && !board.checkScanner.isKingChecked(new Move(board, this, 3, row));
            }
        }

        return false;
    }
}
