package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import parkerbasicchessengine.pieces.Bishop;
import parkerbasicchessengine.pieces.King;
import parkerbasicchessengine.pieces.Knight;
import parkerbasicchessengine.pieces.Pawn;
import parkerbasicchessengine.pieces.Piece;
import parkerbasicchessengine.pieces.Queen;
import parkerbasicchessengine.pieces.Rook;

public class Board extends JPanel {

    public boolean muteSound = false;

    public int tileSize = 85;
    public int enPassantTile = -1;

    int rows = 8;
    int cols = 8;

    String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    public int halfMoveClock = 0;
    public int fullMoveCounter = 1;

    private boolean isWhiteToMove = true;
    public boolean isGameOver = false;

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public Board() {
        this.setPreferredSize(new Dimension(this.cols * this.tileSize, this.rows * this.tileSize));

        this.addMouseListener(this.input);
        this.addMouseMotionListener(this.input);

        loadPositionFromFEN(this.fenStartingPosition);
    }

    public Piece getPiece(int col, int row) {

        for (Piece piece : this.pieceList) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }

        return null;
    }

    public King findKing(boolean isWhite) {

        for (Piece piece : this.pieceList) {
            if (piece instanceof King && piece.isWhite == isWhite) {
                return (King) piece;
            }
        }

        return null;
    }

    public void makeMove(Move move) {

        if (move.piece instanceof Pawn) {
            this.halfMoveClock = 0;
            movePawn(move);
        } else {
            this.enPassantTile = -1;
        }

        if (move.piece instanceof King) {
            moveKing(move);
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * this.tileSize;
        move.piece.yPos = move.newRow * this.tileSize;
        move.piece.isFirstMove = false;

        if(move.capture != null){
            this.halfMoveClock = 0;
            capture(move.capture);
        }

        // Checks if finished blacks turn for incrementing next turn
        if(this.isWhiteToMove == false){
            this.fullMoveCounter++;
        }

        // flip whose turns it is >:)
        this.isWhiteToMove = !this.isWhiteToMove;

        updateGameState();
    }

    private void moveKing(Move move) {

        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;

            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else {
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * this.tileSize;

        }

    }

    public void movePawn(Move move) {
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if (getTileNum(move.newCol, move.newRow) == this.enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }

        if (Math.abs(move.piece.row - move.newRow) == 2) {
            this.enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            this.enPassantTile = -1;
        }

        // Handle Promotion
        if (move.piece instanceof Pawn && (move.newRow == 0 || move.newRow == 7)) {
            pieceList.remove(move.piece);
            pieceList.add(move.promotedToPiece);
            move.piece = move.promotedToPiece;
        }
    }

    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move) {

        boolean isCorrectTurn = move.piece.isWhite == this.isWhiteToMove;

        boolean isMovementPatternValid = move.piece.isValidMovement(move.newCol, move.newRow);

        boolean isTargetSquareValid = !sameTeam(move.piece, move.capture);

        boolean hasNoCollision = !move.piece.moveCollidesWithPiece(move.newCol, move.newRow);

        boolean kingIsSafe = !checkScanner.isKingChecked(move);

        if(move.piece instanceof King king && king.canCastle(move.newCol, move.newRow)){ 
            return isCorrectTurn && isTargetSquareValid && hasNoCollision && kingIsSafe;
        }

        return isCorrectTurn && isMovementPatternValid && isTargetSquareValid && hasNoCollision && kingIsSafe;
    }

    public boolean sameTeam(Piece piece1, Piece piece2) {
        return !(piece1 == null || piece2 == null || piece1.isWhite != piece2.isWhite);
    }

    public void loadPositionFromFEN(String fenString) {
        
        this.pieceList.clear();

        String[] parts = fenString.split(" ");


        // load piece positions
        String position = parts[0]; 
        int row = 0;
        int col = 0;

        for(int i = 0; i < position.length(); i++){
            char ch = position.charAt(i);

            if(ch == '/'){
                row++;
                col = 0;
            } else if (Character.isDigit(ch)){
                col += Character.getNumericValue(ch);
            } else {

                boolean isWhite = Character.isUpperCase(ch);

                switch (Character.toLowerCase(ch)) {
                    case 'p' -> {pieceList.add(new Pawn(this, col, row, isWhite));}
                    case 'r' -> {pieceList.add(new Rook(this, col, row, isWhite));}
                    case 'b' -> {pieceList.add(new Bishop(this, col, row, isWhite));}
                    case 'n' -> {pieceList.add(new Knight(this, col, row, isWhite));}
                    case 'q' -> {pieceList.add(new Queen(this, col, row, isWhite));}
                    case 'k' -> {pieceList.add(new King(this, col, row, isWhite));}

                    default -> throw new AssertionError();
                }

                col++;
            }
        }

        // turn
        this.isWhiteToMove = parts[1].equals("w");

        // load castling rights

        Piece bqr = getPiece(0, 0);
        if(bqr instanceof Rook && bqr.isWhite == false){
            bqr.isFirstMove = parts[2].contains("q");
        }
        Piece bkr = getPiece(7, 0);
        if(bkr instanceof Rook && bkr.isWhite == false){
            bkr.isFirstMove = parts[2].contains("k");
        }
        Piece wqr = getPiece(0, 7);
        if(wqr instanceof Rook && wqr.isWhite == true){
            wqr.isFirstMove = parts[2].contains("Q");
        }
        Piece wkr = getPiece(7, 7);
        if(wkr instanceof Rook && wkr.isWhite == true){
            wkr.isFirstMove = parts[2].contains("K");
        }

        // enpassant

        if(parts[3].equals("-")){
            this.enPassantTile = -1;
        } else {
            this.enPassantTile = (7 - (parts[3].charAt(1) - '1') * 8 + (parts[3].charAt(0) - 'a'));
        }

        // half move

        this.halfMoveClock = Integer.parseInt(parts[4]);

        // full move

        this.fullMoveCounter = Integer.parseInt(parts[5]);
    }

    private void updateGameState(){
        Piece king = findKing(this.isWhiteToMove);

        if(insufficientMaterial(true) && insufficientMaterial(false)) {
            System.out.println("Insufficient Material");
            this.isGameOver = true;
        }

        if(checkScanner.isGameOver(king)){
            if(checkScanner.isKingChecked(new Move(this, king, king.col, king.row))){
                System.out.println(this.isWhiteToMove ? "Black Wins" : "White Wins");
            } else {
                System.out.println("Stalemate");
            }
            this.isGameOver = true;
        }

        if(this.halfMoveClock >= 100){
            System.out.println("50 Move Rule");
            this.isGameOver = true;
        }

    }

    public String convertPostionToFEN(){
        Piece pieces[] = new Piece[64];

        for(Piece piece : this.pieceList){
            pieces[piece.col + piece.row * 8] = piece;
        }

        int openSpaces = 0;
        String fenString;

        for(int i = 0; i < 64; i++){
            if(pieces[i] == null){
                openSpaces++;
                continue;
            } else {

                if(openSpaces != 0){
                    
                }

            }
            
        }
    }

    private boolean insufficientMaterial(boolean isWhite){

        ArrayList<String> activePieces = this.pieceList.stream()
            .filter(p -> p.isWhite == isWhite)
            .map(p -> p.name)
            .collect(Collectors.toCollection(ArrayList :: new));

        if(activePieces.contains("Queen") || activePieces.contains("Rook") || activePieces.contains("Pawn")){
            return false;
        }
        
        return activePieces.size() < 3;
    }

    public int getTileNum(int col, int row) {
        return row * 8 + col;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // paints squares
        for (int rowIndex = 0; rowIndex < this.rows; rowIndex++) {

            for (int colIndex = 0; colIndex < this.cols; colIndex++) {

                if ((rowIndex + colIndex) % 2 == 0) {
                    g2d.setColor(new Color(153, 76, 0));
                } else {
                    g2d.setColor(new Color(255, 229, 204));
                }
                g2d.fillRect(colIndex * tileSize, rowIndex * tileSize, tileSize, tileSize);
            }
        }

        // paints valid piece movement tiles
        if (selectedPiece != null) {
            for (int rowIndex = 0; rowIndex < rows; rowIndex++) {

                for (int colIndex = 0; colIndex < cols; colIndex++) {

                    if (isValidMove(new Move(this, selectedPiece, rowIndex, colIndex))) {
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(rowIndex * tileSize, colIndex * tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        for (Piece piece : this.pieceList) {
            piece.paint(g2d);
        }

    }
}
