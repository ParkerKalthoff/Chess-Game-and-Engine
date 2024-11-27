package parkerbasicchessengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import parkerbasicchessengine.pieces.*;

public class Board extends JPanel {

    public boolean muteSound = false;

    HashMap<String, Integer> pieceHistory = new HashMap<>();

    public int tileSize = 85;
    public int enPassantTile = -1;

    public int rows = 8;
    public int cols = 8;

    final String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public ArrayList<Piece> pieceList = new ArrayList<>();
    public ArrayList<Piece> capturedPieceList = new ArrayList<>();

    public Piece selectedPiece;

    public int halfMoveClock = 0;
    public int fullMoveCounter = 1;

    public boolean isWhiteToMove = true;
    public boolean isGameOver = false;

    public String gameState = "";

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

        this.gameState = updateGameState(this.isWhiteToMove);

        if(!this.gameState.equals("")){
            this.isGameOver = true;
        }

    }

    public void unMakeMove(Move move) {
        if (move != null) {

            Piece piece = move.piece;
            Piece capture = move.capture;
            Piece promo = move.promotedToPiece;
            int col = move.oldCol;
            int row = move.oldRow;

            piece.col = col;
            piece.row = row;
            piece.xPos = col * tileSize;
            piece.yPos = row * tileSize;

            if (capture != null) {
                pieceList.add(capture);
                capturedPieceList.remove(capture);
            }
            if (promo != null) {
                pieceList.add(promo);
                capturedPieceList.remove(promo);
            }

            if(this.isWhiteToMove == false){
                this.fullMoveCounter--;
                this.halfMoveClock--;
            }
    
            // flip whose turns it is >:)
            this.isWhiteToMove = !this.isWhiteToMove;

        }
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
        capturedPieceList.add(piece);
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

    public String updateGameState(boolean isWhiteToMove){
        Piece king = findKing(isWhiteToMove);

        if(insufficientMaterial(true) && insufficientMaterial(false)) {
            return "Insufficient Material";
        }

        if(checkScanner.isGameOver(king)){
            if(checkScanner.isKingChecked(new Move(this, king, king.col, king.row))){
                return isWhiteToMove ? "Black Wins" : "White Wins";
            } else {
                return "Stalemate";
            }
        }

        if(this.halfMoveClock >= 100){
            return "50 Move Rule";
        }
        return "";

    }

    public String convertPostionToFEN(){
        Piece pieces[] = new Piece[64];

        for(Piece piece : this.pieceList){
            pieces[piece.col + piece.row * 8] = piece;
        }

        int openSpaces = 0;

        StringBuilder fenString = new StringBuilder("");        

        // build position
        for (int i = 0; i < 64; i++) {

            if (pieces[i] != null) {
                if (openSpaces > 0) {
                    fenString.append(openSpaces);
                    openSpaces = 0;
                }
                fenString.append(pieces[i].abbreviation);
            } else {
                openSpaces++;
            }
    
            if ((i + 1) % 8 == 0) {
                if (openSpaces > 0) {
                    fenString.append(openSpaces);
                    openSpaces = 0;
                }
                if (i != 63) {
                    fenString.append("/");
                }
            }
        }

        // turn
        fenString.append(" ");
        fenString.append(this.isWhiteToMove ? "w" : "b");

        // castling rights

        fenString.append(" ");
        boolean blankCastlingString = true;

        Piece wkr = getPiece(7, 7);
        if(wkr instanceof Rook && wkr.isWhite == true && wkr.isFirstMove && this.findKing(true).isFirstMove){
            fenString.append("K");
            blankCastlingString = false;
        }
        Piece wqr = getPiece(0, 7);
        if(wqr instanceof Rook && wqr.isWhite == true && wqr.isFirstMove && this.findKing(true).isFirstMove){
            fenString.append("Q");
            blankCastlingString = false;
        }
        Piece bkr = getPiece(7, 0);
        if(bkr instanceof Rook && bkr.isWhite == false && bkr.isFirstMove && this.findKing(false).isFirstMove){
            fenString.append("k");
            blankCastlingString = false;
        }
        Piece bqr = getPiece(0, 0);
        if(bqr instanceof Rook && bqr.isWhite == false && bqr.isFirstMove && this.findKing(false).isFirstMove){
            fenString.append("q");
            blankCastlingString = false;
        }

        if(blankCastlingString){
            fenString.append("-");
        }

        // enpassant
        fenString.append(" ");
        fenString.append(this.enPassantTile == -1 ? "-" : "" + ((char) ('a' + this.enPassantTile % 8)) + (8 - (this.enPassantTile / 8)));

        // half move 
        fenString.append(" ");
        fenString.append(this.halfMoveClock / 2);

        // full move
        fenString.append(" ");
        fenString.append(this.fullMoveCounter);

        return fenString.toString();
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
