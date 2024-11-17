package parkerbasicchessengine;

import parkerbasicchessengine.pieces.King;
import parkerbasicchessengine.pieces.Pawn;
import parkerbasicchessengine.pieces.Piece;

public class Move {

    int oldCol, oldRow, newCol, newRow;
    Piece piece, capture;

    
    public Move(Board board, Piece piece, int newCol, int newRow){
        // legacy move
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;
        this.capture = board.getPiece(newCol, newRow);

    };

    public Move(Board board, Piece piece, int newCol, int newRow, Piece promotedToPiece){
        // move that can handle promotions
        if(promotedToPiece instanceof Pawn || promotedToPiece instanceof King){
            throw new IllegalArgumentException("PromotedToPiece must be of type Queen, Rook, Bishop, Knight, or null");
        }

        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;

        if(promotedToPiece == null){
            this.piece = piece;
        } else {
            this.piece = promotedToPiece;
        }

        
        this.capture = board.getPiece(newCol, newRow);

    };



}
