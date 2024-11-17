package parkerbasicchessengine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import parkerbasicchessengine.Move;


import parkerbasicchessengine.pieces.Bishop;
import parkerbasicchessengine.pieces.King;
import parkerbasicchessengine.pieces.Knight;
import parkerbasicchessengine.pieces.Pawn;
import parkerbasicchessengine.pieces.Piece;
import parkerbasicchessengine.pieces.Queen;
import parkerbasicchessengine.pieces.Rook;


public class Board extends JPanel{

    public int tileSize = 85;

    public int enPassantTile = -1;

    int rows = 8;
    int cols = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    Input input = new Input(this);

    CheckScanner checkScanner = new CheckScanner(this);

    public Board(){
        this.setPreferredSize(new Dimension(this.cols * this.tileSize, this.rows * this.tileSize));
        
        this.addMouseListener(this.input);
        this.addMouseMotionListener(this.input);
        
        addPieces();
    }

    public Piece getPiece(int col, int row){

        for(Piece piece : this.pieceList){
            if(piece.col == col && piece.row == row){
                return piece;
            }
        }

        return null;
    }

    public King findKing(boolean isWhite){

        for(Piece piece : this.pieceList){
            if(piece instanceof King && piece.isWhite == isWhite){
                return (King) piece;
            }
        }
        System.out.println("No king!");

        return null;
    }

    public void makeMove(Move move){

        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        } else {

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;

        move.piece.xPos = move.newCol * this.tileSize;
        move.piece.yPos = move.newRow * this.tileSize;
        
        move.piece.isFirstMove = false;

        capture(move.capture);
        }
    }

    public void movePawn(Move move){
        
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if(getTileNum(move.newCol, move.newRow) == this.enPassantTile){
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }

        if(Math.abs(move.piece.row - move.newRow) == 2){
            this.enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            this.enPassantTile = -1;
        }

        // promote :D

            capture(move.piece);
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;

        move.piece.xPos = move.newCol * this.tileSize;
        move.piece.yPos = move.newRow * this.tileSize;
        
        move.piece.isFirstMove = false;

        capture(move.capture);
    }  

    public void capture(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move) {

        boolean isMovementPatternValid = move.piece.isValidMovement(move.newCol, move.newRow);
        
        boolean isTargetSquareValid = !sameTeam(move.piece, move.capture);
        
        boolean hasNoCollision = !move.piece.moveCollidesWithPiece(move.newCol, move.newRow);

        boolean kingIsSafe = !checkScanner.isKingChecked(move);
    
        return isMovementPatternValid && isTargetSquareValid && hasNoCollision && kingIsSafe;
    }
    

    public boolean sameTeam(Piece piece1, Piece piece2){
        return !(piece1 == null || piece2 == null || piece1.isWhite != piece2.isWhite);
    }

    public void addPieces(){
        // Black

        // Back rank
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));

        
        // Pawns

        for(int i = 0; i < 8; i++){
            pieceList.add(new Pawn(this, i, 1, false));
        }

        // White
        // Pawns

        for(int i = 0; i < 8; i++){
            pieceList.add(new Pawn(this, i, 6, true));
        }

        // Backrank
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));

    }

    public int getTileNum(int col, int row){
        return row * 8 + col;
    }

    
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        // paints squares
        for(int rowIndex = 0; rowIndex < rows; rowIndex++){
            
            for(int colIndex = 0; colIndex < cols; colIndex++){

                if((rowIndex + colIndex) % 2 == 0){
                    g2d.setColor(new Color(153,76,0));
                } else {
                    g2d.setColor(new Color(255,229,204));
                }
                g2d.fillRect(colIndex * tileSize, rowIndex * tileSize, tileSize, tileSize);
            }
        }

        // paints valid piece movement tiles
        if(selectedPiece != null){
            for(int rowIndex = 0; rowIndex < rows; rowIndex++){
            
                for(int colIndex = 0; colIndex < cols; colIndex++){

                    if(isValidMove(new Move(this, selectedPiece, rowIndex, colIndex))){
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(rowIndex * tileSize, colIndex * tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        for(Piece piece : pieceList){
            piece.paint(g2d);
        }

    }
}
