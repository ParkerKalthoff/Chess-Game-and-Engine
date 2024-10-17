package parkerbasicchessengine.pieces;


import parkerbasicchessengine.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

public abstract class Piece {

    public int col, row;
    public int xPos, yPos;

    public boolean isWhite;
    public String name;
    public int value;

    BufferedImage sheet;
    {
        try{
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("pieces.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    protected int sheetScale = sheet.getWidth() / 6;

    Image sprite;

    Board board;

    public Piece(Board board){
        this.board = board;
    }

    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite, xPos, yPos, null);
    }

}
