package parkerbasicchessengine.pieces;

import java.awt.image.BufferedImage;

import parkerbasicchessengine.Board;

public class Queen extends Piece{

    public Queen(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.abbreviation = isWhite ? 'Q' : 'q';

        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;

        this.name = "Queen";

        this.sprite = sheet.getSubimage(1 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row){
        
        if(col >= 8 || col <= -1 || row >= 8 || row <= -1){
            return false;
        }

        return this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    public boolean moveCollidesWithPiece(int col, int row){

        if(this.col == col || this.row == row){
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

        } else {
            
            // up left
            if(this.col > col && this.row > row){
                for(int i = 1; i < Math.abs(this.col - col); i++){

                    if(board.getPiece(this.col - i, this.row - i) != null){
                        return true;
                    }
                }
            }

            // up right
            if(this.col < col && this.row > row){
                for(int i = 1; i < Math.abs(this.col - col); i++){

                    if(board.getPiece(this.col + i, this.row - i) != null){
                        return true;
                    }
                }
            }

            // down left
            if(this.col > col && this.row < row){
                for(int i = 1; i < Math.abs(this.col - col); i++){

                    if(board.getPiece(this.col - i, this.row + i) != null){
                        return true;
                    }
                }
            }

            // down right
            if(this.col < col && this.row < row){
                for(int i = 1; i < Math.abs(this.col - col); i++){

                    if(board.getPiece(this.col + i, this.row + i) != null){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
