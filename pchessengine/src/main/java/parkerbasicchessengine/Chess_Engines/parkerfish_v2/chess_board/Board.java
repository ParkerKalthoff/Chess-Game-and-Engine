package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board;
import java.util.Stack;

import javax.management.RuntimeErrorException;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

public class Board {

    private int fullMoveCounter;
    private int halfMoveClock;
    private boolean isGameOver;
    private boolean isWhitesTurn;
    private byte castlingRights; // 0bKQkq
    public int enPassantIndex;

    public long bitboards[][];
    private Stack<BoardState> boardStateStack;

    public Board(int fullMoveCounter, int halfMoveClock, boolean isGameOver, boolean isWhitesTurn, byte castlingRights, int enPassantIndex, long[][] bitboards) {
        this.fullMoveCounter = fullMoveCounter;
        this.halfMoveClock = halfMoveClock;
        this.isGameOver = isGameOver;
        this.isWhitesTurn = isWhitesTurn;
        this.castlingRights = castlingRights;
        this.enPassantIndex = enPassantIndex;
        this.bitboards = bitboards;
        this.boardStateStack = new Stack<BoardState>();
    }

    private static class BoardState {
        public int fullMoveCounter;
        public int halfMoveClock;
        public boolean isGameOver;
        public boolean isWhitesTurn;
        public byte castlingRights;
        public int enPassantIndex;

        BoardState(int fullMoveCounter, int halfMoveClock, boolean isGameOver,
                   boolean isWhitesTurn, byte castlingRights, int enPassantIndex) {
            this.fullMoveCounter = fullMoveCounter;
            this.halfMoveClock = halfMoveClock;
            this.isGameOver = isGameOver;
            this.isWhitesTurn = isWhitesTurn;
            this.castlingRights = castlingRights;
            this.enPassantIndex = enPassantIndex;
        }
    }

    // may be worth switching this to an attribute instead of calculating at runtime
    
    public boolean hasKingsideCastlingRights(int team){

        if(team == White) {
            return (this.castlingRights & 0b1000) != 0;
        } else {
            return (this.castlingRights & 0b0010) != 0;
        }

    }

    public boolean hasQueensideCastlingRights(int team){

        if(team == White) {
            return (this.castlingRights & 0b0100) != 0;
        } else {
            return (this.castlingRights & 0b0001) != 0;
        }

    }

    public long getAllPieces() {
        return getTeamsPieces(White) | getTeamsPieces(Black);
    }

    public long getTeamsPieces(int team) {
        return this.bitboards[team][K] | this.bitboards[team][Q] | this.bitboards[team][R] | this.bitboards[team][B] |  this.bitboards[team][N] | this.bitboards[team][P];
    }

    public int getActiveTeam() {
        return this.isWhitesTurn ? White : Black;
    }

    public int getInactiveTeam() {
        return this.isWhitesTurn ? Black : White;
    }

    public int getPieceAt(int square) {

        long squareBitboard = 1L << square;

        long whiteTeam[] = bitboards[White];
        long blackTeam[] = bitboards[Black];

        // Cycles through the piece types
        for(int pieceType = 0; pieceType < 6; pieceType++){
            if(((whiteTeam[pieceType] | blackTeam[pieceType]) & squareBitboard) != 0){
                return pieceType;
            }
        }
        return -1;
    }

    private void togglePiece(int square, int team, int piece) {
        this.bitboards[team][piece] ^= 1L << square;
    }

    public void makeMove(Move move) {

        if (isGameOver) {
            throw new RuntimeErrorException(null, "Attempting to make move when game is over");
        }
    
        boardStateStack.add(new BoardState(fullMoveCounter, halfMoveClock, isGameOver, isWhitesTurn, castlingRights, enPassantIndex));
    
        if (move.getPieceType() == P || move.isCapture()) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }
    
        if (!isWhitesTurn) {
            fullMoveCounter++;
        }
    
        isWhitesTurn = !isWhitesTurn;
    
        togglePiece(move.getFromSquare(), move.getTeam(), move.getPieceType());
    
        if (move.isPromotion()) {
            togglePiece(move.getToSquare(), move.getTeam(), move.getPromotionPiece());
        } else {
            togglePiece(move.getToSquare(), move.getTeam(), move.getPieceType());
        }
    
        if (move.isCapture()) {
            if (move.isEnPassant()) {
                int capturedPawnSquare = isWhitesTurn ? move.getToSquare() + 8 : move.getToSquare() - 8;
                togglePiece(capturedPawnSquare, move.getCapturePieceTeam(), P);
            } else {
                togglePiece(move.getToSquare(), move.getCapturePieceTeam(), move.getCapturePieceType());
            }
            halfMoveClock = 0;
        }
    
        if (move.isPawnDoubleMove()) {
            enPassantIndex = (move.getToSquare() + move.getFromSquare()) / 2;
        } else {
            enPassantIndex = -1;
        }
    
        if (move.isCastling()) {
            int rookFrom, rookTo;
            if (move.getFromSquare() > move.getToSquare()) {
                rookFrom = move.getTeam() == White ? H1 : H8;
                rookTo = move.getTeam() == White ? F1 : F8;
            } else {
                rookFrom = move.getTeam() == White ? A1 : A8;
                rookTo = move.getTeam() == White ? D1 : D8;
            }
            togglePiece(rookFrom, move.getTeam(), R);
            togglePiece(rookTo, move.getTeam(), R);
        }
    
        if (halfMoveClock >= 100 || bitboards[White][K] == 0L || bitboards[Black][K] == 0L) {
            isGameOver = true;
        }
    }

    public void unmakeMove(Move move) {
        if (boardStateStack.isEmpty()) {
            throw new RuntimeErrorException(null, "No moves to unmake");
        }
    
        BoardState previousState = boardStateStack.pop();
        this.fullMoveCounter = previousState.fullMoveCounter;
        this.halfMoveClock = previousState.halfMoveClock;
        this.isGameOver = previousState.isGameOver;
        this.isWhitesTurn = previousState.isWhitesTurn;
        this.castlingRights = previousState.castlingRights;
        this.enPassantIndex = previousState.enPassantIndex;
    
        if (move.isCastling()) {
            int rookFrom, rookTo;
            if (move.getFromSquare() > move.getToSquare()) {
                rookFrom = move.getTeam() == White ? H1 : H8;
                rookTo = move.getTeam() == White ? F1 : F8;
            } else {
                rookFrom = move.getTeam() == White ? A1 : A8;
                rookTo = move.getTeam() == White ? D1 : D8;
            }
            togglePiece(rookTo, move.getTeam(), R);
            togglePiece(rookFrom, move.getTeam(), R);
        }
    
        if (move.isPromotion()) {
            togglePiece(move.getToSquare(), move.getTeam(), move.getPromotionPiece());
            togglePiece(move.getFromSquare(), move.getTeam(), P);
        } else {
            togglePiece(move.getToSquare(), move.getTeam(), move.getPieceType());
            togglePiece(move.getFromSquare(), move.getTeam(), move.getPieceType());
        }
    
        if (move.isCapture()) {
            togglePiece(move.getToSquare(), move.getCapturePieceTeam(), move.getCapturePieceType());
        }
    
        if (move.isEnPassant()) {
            int capturedPawnSquare = isWhitesTurn ? move.getToSquare() + 8 : move.getToSquare() - 8;
            togglePiece(capturedPawnSquare, move.getCapturePieceTeam(), P);
        }
    }
    
    // Deep copy method for potential use in multithreading

    public Board deepCopy() {
        long[][] bitboardsCopy = new long[bitboards.length][];
        for (int i = 0; i < bitboards.length; i++) {
            bitboardsCopy[i] = bitboards[i].clone();
        }
    
        Stack<BoardState> boardStateStackCopy = new Stack<>();
        for (BoardState state : this.boardStateStack) {
            boardStateStackCopy.add(new BoardState(
                state.fullMoveCounter,
                state.halfMoveClock,
                state.isGameOver,
                state.isWhitesTurn,
                state.castlingRights,
                state.enPassantIndex
            ));
        }
    
        Board board = new Board(
            this.fullMoveCounter,
            this.halfMoveClock,
            this.isGameOver,
            this.isWhitesTurn,
            this.castlingRights,
            this.enPassantIndex,
            bitboardsCopy
        );
    
        board.boardStateStack = boardStateStackCopy;
    
        return board;
    }
    
}
