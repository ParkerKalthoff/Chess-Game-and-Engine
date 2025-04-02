package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.management.RuntimeErrorException;

import parkerbasicchessengine.soundManager;
import parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.chess_board_move_gen.MoveGenerator;
import parkerbasicchessengine.Utils.PieceCoordinateConversion;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

public class Board {

    private int fullMoveCounter;
    private int halfMoveClock;
    public boolean isGameOver;
    public boolean isWhitesTurn;
    public byte castlingRights; // 0bKQkq
    public int enPassantIndex;
    private long boardHash;

    public HashMap<Long, Integer> previousPositions = new HashMap<>();

    public long bitboards[][];
    private Stack<BoardState> boardStateStack;

    private MoveGenerator moveGenerator;

    private ZobristHasher zobristHasher = new ZobristHasher(this);

    public Board(int fullMoveCounter, int halfMoveClock, boolean isGameOver, boolean isWhitesTurn, byte castlingRights,
            int enPassantIndex, long[][] bitboards) {
        this.fullMoveCounter = fullMoveCounter;
        this.halfMoveClock = halfMoveClock;
        this.isGameOver = isGameOver;
        this.isWhitesTurn = isWhitesTurn;
        this.castlingRights = castlingRights;
        this.enPassantIndex = enPassantIndex;
        this.bitboards = bitboards;
        this.boardStateStack = new Stack<BoardState>();

        this.boardHash = zobristHasher.getHash();

        previousPositions.put(boardHash, 1);

        this.moveGenerator = new MoveGenerator(this);
    }

    private static class BoardState {
        public int fullMoveCounter;
        public int halfMoveClock;
        public boolean isGameOver;
        public boolean isWhitesTurn;
        public byte castlingRights;
        public int enPassantIndex;
        public long boardHash;

        BoardState(int fullMoveCounter, int halfMoveClock, boolean isGameOver,
                boolean isWhitesTurn, byte castlingRights, int enPassantIndex,
                long boardHash) {
            this.fullMoveCounter = fullMoveCounter;
            this.halfMoveClock = halfMoveClock;
            this.isGameOver = isGameOver;
            this.isWhitesTurn = isWhitesTurn;
            this.castlingRights = castlingRights;
            this.enPassantIndex = enPassantIndex;
            this.boardHash = boardHash;
        }
    }

    // may be worth switching this to an attribute instead of calculating at runtime

    public boolean hasKingsideCastlingRights(int team) {

        if (team == White) {
            return (this.castlingRights & 0b1000) != 0;
        } else {
            return (this.castlingRights & 0b0010) != 0;
        }

    }

    public boolean hasQueensideCastlingRights(int team) {

        if (team == White) {
            return (this.castlingRights & 0b0100) != 0;
        } else {
            return (this.castlingRights & 0b0001) != 0;
        }

    }

    public ArrayList<Move> getValidMoves() {
        return moveGenerator.generateMoves();
    }

    public boolean isKingInCheck() {
        return moveGenerator.checkCount != 0;
    }

    public boolean isThreeMoveRepitionStalemate() {
        
        for(Integer occurences : previousPositions.values()) {
            if(occurences >= 3) {
                return true;
            }
        }

        return false;
    }

    public long getAllPieces() {
        return getTeamsPieces(White) | getTeamsPieces(Black);
    }

    public long getTeamsPieces(int team) {
        return  this.bitboards[team][K] |
                this.bitboards[team][Q] |
                this.bitboards[team][B] |
                this.bitboards[team][N] |
                this.bitboards[team][R] |
                this.bitboards[team][P];
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
        for (int pieceType = 0; pieceType < 6; pieceType++) {
            if (((whiteTeam[pieceType] | blackTeam[pieceType]) & squareBitboard) != 0) {
                return pieceType;
            }
        }
        return NULL_PIECE;
    }

    private void togglePiece(int square, int team, int piece) {
        try {
            this.bitboards[team][piece] ^= 1L << square;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("out of index, square : "+square+", team : "+team+", piece : "+piece);
            throw e;
        }
    }

    private int convertPieceStringToType(String piece) {

        if(piece.equals("K")){
            return K;
        } else if (piece.equals("Q")) {
            return Q;
        } else if (piece.equals("P")) {
            return P;
        } else if (piece.equals("R")) {
            return R;
        } else if (piece.equals("B")) {
            return B;
        } else if (piece.equals("N")) {
            return N;
        } else {
            return NULL_PIECE;
        }

    }

    public void makeMoveUsingCoordinate(String moveCoord) {

        String fromSquareString = moveCoord.substring(0,2);
        String toSquareString = moveCoord.substring(2,4);
        String promotionPieceString = moveCoord.length() == 5 ? moveCoord.substring(4,5) : null;
        
        int fromSquare = PieceCoordinateConversion.DecodeCoord.parkerfish_v2(fromSquareString);
        int toSquare = PieceCoordinateConversion.DecodeCoord.parkerfish_v2(toSquareString);
        int team = this.isWhitesTurn ? White : Black;
        int pieceType = this.getPieceAt(fromSquare);
        int capturePieceType = this.getPieceAt(toSquare);
        boolean captureFlag = capturePieceType != -1 || (toSquare == enPassantIndex && pieceType == P);
        boolean promotionFlag = promotionPieceString != null;
        int promotionPiece = promotionFlag ? convertPieceStringToType(promotionPieceString) : NULL_PIECE; 
        boolean pawnDoubleMove = pieceType == P && Math.abs(fromSquare - toSquare) == 16;
        boolean enPassant = pieceType == P && captureFlag && toSquare == this.enPassantIndex;
        boolean castling = pieceType == K && Math.abs(fromSquare - toSquare) > 1;

        if(pieceType == NULL_PIECE) {
            System.out.println("int fromSquare =\r\n" + fromSquare
                                + "int toSquare =\r\n" + toSquare
                                + "int team =\r\n" + team
                                + "int pieceType =\r\n" + pieceType
                                + "int capturePieceType =\r\n" + capturePieceType
                                + "boolean captureFlag =\r\n" + captureFlag
                                + "boolean promotionFlag =\r\n" + promotionFlag
                                + "int promotionPiece =\r\n" + promotionPiece
                                + "boolean pawnDoubleMove =\r\n" + pawnDoubleMove
                                + "boolean enPassant =\r\n" + enPassant
                                + "boolean castling =" +castling);
            throw new RuntimeErrorException(null, "Missing Piece after conversion");
        }

        if(promotionFlag && promotionPiece == NULL_PIECE) {
            throw new RuntimeErrorException(null, "Promotion Flag is true without given piece");
        }

        Move move = new Move(fromSquare, toSquare, team, pieceType, captureFlag, capturePieceType, promotionFlag, promotionPiece, pawnDoubleMove, enPassant, castling);
        
        this.makeMove(move);
    }

    public void makeMove(Move move) {

        if (isGameOver) {
            throw new RuntimeErrorException(null, "Attempting to make move when game is over");
        }

        boardStateStack.add(new BoardState(fullMoveCounter, halfMoveClock, isGameOver, isWhitesTurn, castlingRights, enPassantIndex, boardHash));

        zobristHasher.preMoveHashAdjustment();

        if (move.getPieceType() == P || move.isCapture()) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }

        if (!isWhitesTurn) {
            fullMoveCounter++;
        }

        togglePiece(move.getFromSquare(), move.getTeam(), move.getPieceType());

        if (move.isPromotion()) {
            togglePiece(move.getToSquare(), move.getTeam(), move.getPromotionPiece());
        } else {
            togglePiece(move.getToSquare(), move.getTeam(), move.getPieceType());
        }

        if (move.isCapture()) {

            if (move.isEnPassant()) {

                int capturedPawnSquare = this.isWhitesTurn ? move.getToSquare() + SOUTH : move.getToSquare() + NORTH;
                togglePiece(capturedPawnSquare, move.getCapturePieceTeam(), P);

            } else {

                togglePiece(move.getToSquare(), move.getCapturePieceTeam(), move.getCapturePieceType());

                if(move.getCapturePieceType() == R) {

                    if (this.isWhitesTurn) {
                        if (move.getToSquare() == A8) {
                            castlingRights &= 0b1110; // clear q
                        }
                        else if (move.getToSquare() == H8) { 
                            castlingRights &= 0b1101; // clear k
                        }
                    } else {
                        if (move.getToSquare() == A1) {
                            castlingRights &= 0b1011; // clear Q
                        }
                        else if (move.getToSquare() == H1) {
                            castlingRights &= 0b0111; // clear K
                        }
                    }                    
                }
            }
        }

        if (move.getPieceType() == R) {
            if (move.getFromSquare() == A8) {
                castlingRights &= 0b1110;
            } else if (move.getFromSquare() == H8) {
                castlingRights &= 0b1101;
            } else if (move.getFromSquare() == A1) {
                castlingRights &= 0b1011;
            } else if (move.getFromSquare() == H1) {
                castlingRights &= 0b0111;
            }
        }

        if(move.getPieceType() == K) {

            if(isWhitesTurn) {
                castlingRights &= 0b0011;
            } else {
                castlingRights &= 0b1100;
            }
        }

        if (move.isPawnDoubleMove()) {
            enPassantIndex = (move.getToSquare() + move.getFromSquare()) / 2;
        } else {
            enPassantIndex = -1;
        }

        if (move.isCastling()) {
            int rookFrom, rookTo;
            if (move.getFromSquare() < move.getToSquare()) {
                rookFrom = move.getTeam() == White ? H1 : H8;
                rookTo = move.getTeam() == White ? F1 : F8;
            } else {
                rookFrom = move.getTeam() == White ? A1 : A8;
                rookTo = move.getTeam() == White ? D1 : D8;
            }
            togglePiece(rookFrom, move.getTeam(), R);
            togglePiece(rookTo, move.getTeam(), R);
        }

        
        this.boardHash = zobristHasher.postMoveHashAdjustment(move);

        this.previousPositions.compute(boardHash, (k, v) -> (v == null) ? 1 : v + 1);


        if (halfMoveClock >= 100 || bitboards[White][K] == 0L || bitboards[Black][K] == 0L || previousPositions.get(boardHash) >= 3) {
            isGameOver = true;
        }
        
        this.isWhitesTurn = !this.isWhitesTurn;
    }

    public void unmakeMove(Move move) {
        if (boardStateStack.isEmpty()) {
            throw new RuntimeErrorException(null, "No moves to unmake");
        }

        this.previousPositions.compute(this.boardHash, (k, v) -> v - 1);
    
        BoardState previousState = boardStateStack.pop();
        this.fullMoveCounter = previousState.fullMoveCounter;
        this.halfMoveClock = previousState.halfMoveClock;
        this.isGameOver = previousState.isGameOver;
        this.isWhitesTurn = previousState.isWhitesTurn;
        this.castlingRights = previousState.castlingRights;
        this.enPassantIndex = previousState.enPassantIndex;
        this.boardHash = previousState.boardHash;
    
        if (move.isCastling()) {
            int rookFrom, rookTo;
            if (move.getFromSquare() < move.getToSquare()) {
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
            if (move.isEnPassant()) {
                int capturedPawnSquare = isWhitesTurn ? move.getToSquare() + 8 : move.getToSquare() - 8;
                togglePiece(capturedPawnSquare, move.getCapturePieceTeam(), P);
            }
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
                    state.enPassantIndex,
                    state.boardHash));
        }

        Board board = new Board(
                this.fullMoveCounter,
                this.halfMoveClock,
                this.isGameOver,
                this.isWhitesTurn,
                this.castlingRights,
                this.enPassantIndex,
                bitboardsCopy);

        board.boardStateStack = boardStateStackCopy;

        return board;
    }

    private String getPieceString(int pieceType, boolean team) {

        switch (pieceType) {
            case P:
                return team ? "P" : "p";
            case K:
                return team ? "K" : "k";
            case B:
                return team ? "B" : "b";
            case N:
                return team ? "N" : "n";
            case Q:
                return team ? "Q" : "q";
            case R:
                return team ? "R" : "r";
            case NULL_PIECE:
                return ".";
            default:
                throw new RuntimeErrorException(null, "Invalid Input");
        }
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("    a b c d e f g h\n");
        sb.append("   ________________\n");

        for (int rank = 0; rank <= 7; rank++) {

            sb.append(8-rank + " | ");

            for (int file = 0; file < 8; file++) {

                int square = rank * 8 + file;
                String pieceStr = ".";

                for (int team = 0; team < 2; team++) {

                    for (int piece = 0; piece < 6; piece++) {

                        if ((bitboards[team][piece] & (1L << square)) != 0) {
                            pieceStr = getPieceString(piece, team == White);
                            break;
                        }
                    }
                }

                sb.append(pieceStr).append(" ");

            }

            sb.append("\n");

        }

        return sb.toString();

    }

    public String fenString() {
        StringBuilder fen = new StringBuilder();
    
        for (int rank = 0; rank < 8; rank++) {
            int emptySquares = 0;
    
            for (int file = 0; file < 8; file++) {
                int square = rank * 8 + file;
                String pieceStr = ".";
    
                for (int team = 0; team < 2; team++) {
                    for (int piece = 0; piece < 6; piece++) {
                        if ((bitboards[team][piece] & (1L << square)) != 0) {
                            pieceStr = getPieceString(piece, team == White);
                            break;
                        }
                    }
                    if (!pieceStr.equals(".")) {
                        break;
                    }
                }
    
                if (pieceStr.equals(".")) {
                    emptySquares++;
                } else {
                    if (emptySquares > 0) {
                        fen.append(emptySquares);
                        emptySquares = 0;
                    }
                    fen.append(pieceStr);
                }
            }
    
            if (emptySquares > 0) {
                fen.append(emptySquares);
            }
            if (rank < 7) {
                fen.append("/");
            }
        }
    
        fen.append(isWhitesTurn ? " w" : " b");
    
        String castling = "";
    
        if (hasKingsideCastlingRights(White))
            castling += "K";
    
        if (hasQueensideCastlingRights(White))
            castling += "Q";
    
        if (hasKingsideCastlingRights(Black))
            castling += "k";
    
        if (hasQueensideCastlingRights(Black))
            castling += "q";
    
        fen.append(castling.isEmpty() ? " - " : " " + castling + " ");
    
        fen.append(enPassantIndex == -1 ? "- " : PieceCoordinateConversion.EncodeCoord.parkerfish_v2(enPassantIndex)+ " ");
    
        fen.append(halfMoveClock).append(" ");
    
        fen.append(fullMoveCounter);
    
        return fen.toString();
    }
}
