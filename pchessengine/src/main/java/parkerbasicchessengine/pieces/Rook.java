package parkerbasicchessengine.pieces;

import java.awt.image.BufferedImage;

import parkerbasicchessengine.Board;

public class Rook extends Piece{

    public Rook(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.abbreviation = isWhite ? 'R' : 'r';

        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        this.name = "Rook";

        this.sprite = sheet.getSubimage(4 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row){

        if(col >= 8 || col <= -1 || row >= 8 || row <= -1){
            return false;
        }

        return this.col == col || this.row == row;
    }

    public boolean moveCollidesWithPiece(int col, int row){

        if(this.col > col){
            for(int colIndex = this.col - 1; colIndex > col; colIndex--){

                if(board.getPiece(colIndex, this.row) != null){
                    return true;
                }
            }
        }

        if(this.col < col){
            for(int colIndex = this.col + 1; colIndex < col; colIndex++){

                if(board.getPiece(colIndex, this.row) != null){
                    return true;
                }
            }
        }

        if(this.row > row){
            for(int rowIndex = this.row - 1; rowIndex > row; rowIndex--){

                if(board.getPiece(this.col, rowIndex) != null){
                    return true;
                }
            }
        }

        
        if(this.row < row){
            for(int rowIndex = this.row + 1; rowIndex < row; rowIndex++){

                if(board.getPiece(this.col, rowIndex) != null){
                    return true;
                }
            }
        }

        return false;
    }
}
