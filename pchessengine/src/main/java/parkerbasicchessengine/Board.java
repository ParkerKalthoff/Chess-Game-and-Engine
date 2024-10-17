package parkerbasicchessengine;

import javax.swing.*;

import parkerbasicchessengine.pieces.*;
import java.awt.*;
import java.util.ArrayList;


public class Board extends JPanel{

    public int tileSize = 85;

    int rows = 8;
    int cols = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Board(){
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        addPieces();
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

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

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

        for(Piece piece : pieceList){
            piece.paint(g2d);
        }

    }

    

}
