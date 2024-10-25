package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import parkerbasicchessengine.pieces.Bishop;
import parkerbasicchessengine.pieces.King;
import parkerbasicchessengine.pieces.Knight;
import parkerbasicchessengine.pieces.Pawn;
import parkerbasicchessengine.pieces.Piece;
import parkerbasicchessengine.pieces.Queen;
import parkerbasicchessengine.pieces.Rook;


public class Board extends JPanel{

    public int tileSize = 85;

    int rows = 8;
    int cols = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    Input input = new Input(this);

    public Board(){
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        
        addPieces();
    }

    public Piece getPiece(int col, int row){

        for(Piece piece : pieceList){
            if(piece.col == col && piece.row == row){
                return piece;
            }
        }

        return null;
    }

    public void makeMove(Move move){
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;

        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;
        
        capture(move);
    }

    public void capture(Move move){
        pieceList.remove(move.capture);
    }

    public boolean isValidMove(Move move){
        // todo add more valid move checks

        
        return !sameTeam(move.piece, move.capture);

    }

    private boolean sameTeam(Piece piece1, Piece piece2){
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
