package parkerbasicchessengine.pieces;

import java.awt.image.BufferedImage;

import parkerbasicchessengine.Board;

public class Knight extends Piece{

    public Knight(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.abbreviation = isWhite ? 'N' : 'n';

        this.value = 300;

        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        this.name = "Knight";

        this.sprite = sheet.getSubimage(3 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row){

        if(col >= 8 || col <= -1 || row >= 8 || row <= -1){
            return false;
        }

        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }
}
