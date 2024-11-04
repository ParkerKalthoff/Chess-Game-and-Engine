package parkerbasicchessengine.pieces;

import java.awt.image.BufferedImage;

import parkerbasicchessengine.Board;

public class Pawn extends Piece{

    public Pawn(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;

        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        this.name = "Pawn";

        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }    
    
    @Override
    public boolean isValidMovement(int col, int row){
        
        int colorIndex = isWhite ? 1 : -1;

        // Pawn forward 1

        if(this.col == col && row == this.row - colorIndex && board.getPiece(col, row) == null){
            return true;
        }

        // Pawn forward 2

        if(isFirstMove && this.col == col && row == this.row - colorIndex * 2 && board.getPiece(col, row) == null && board.getPiece(col, row + colorIndex) == null){
            return true;
        }

        // take left

        if(col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col, row) != null){
            return true;
        }

        // take right

        if(col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row) != null){
            return true;
        }

        // En passant capture left
        if (col == this.col - 1 && row == this.row - colorIndex && board.getTileNum(col, row) == board.enPassantTile &&  board.getPiece(col, this.row) != null &&  board.getPiece(col, this.row).isWhite != this.isWhite) {
            return true;
        }

        // En passant capture right
        if (col == this.col + 1 && row == this.row - colorIndex && board.getTileNum(col, row) == board.enPassantTile &&  board.getPiece(col, this.row) != null &&  board.getPiece(col, this.row).isWhite != this.isWhite) {
            return true;
        }


        return false;
    }
}
