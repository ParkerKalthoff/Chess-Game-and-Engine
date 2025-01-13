package parkerbasicchessengine.Chess_Engines;

import java.util.HashMap;

import javax.management.RuntimeErrorException;

import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.ZorbistHasher;
import static parkerbasicchessengine.Chess_Engines.ChessEngineUtils.Constants.*;


public class BitwiseBoard{

    // bitwise board is meant to be a lightweight way of encapsulating chess data
    // the intention is to have a way to mutate board state for an engine
    // is not meant for generating legal moves or verifying legal positions
    // [0 white, 1 black] - [0 king, 1 queen, 2 bishop, 3 knight, 4 rook, 5 pawn]
    public long piece_bitboards[][] = {{0L, 0L, 0L, 0L, 0L, 0L}, {0L, 0L, 0L, 0L, 0L, 0L}};
    public int enpassantIndex;
    public boolean isWhiteToMove;
    public byte castlingRights;
    //          xxxx KQkq
    public int halfMoveClock;
    public int fullMoveCounter;

    public boolean isGameOver;

    public boolean kingIsInCheck;

    private ZorbistHasher zorbistHasher = new ZorbistHasher();

    public HashMap<Long, Integer> historicalPostions = new HashMap<>();

    public void printBitboard(long bitboard) {
        System.out.println();
        for (int rank = 7; rank >= 0; rank--) {  // Loop through ranks (8 rows)
            for (int file = 0; file < 8; file++) { // Loop through files (8 columns)
                int squareIndex = rank * 8 + file;  // Calculate index of the current square
                if ((bitboard & (1L << squareIndex)) != 0) {
                    System.out.print("1 ");  // Piece present on this square
                } else {
                    System.out.print("0 ");  // No piece on this square
                }
            }
            System.out.println();  // Move to the next line after each rank
        }
    }

    public BitwiseBoard(String fenString) {

        String parts[] = fenString.split(" ");
        char piecePlacementChars[] = parts[0].replaceAll("/", "").toCharArray();

        int bitboardIndex = 0;
        for (char c : piecePlacementChars) {
            if (Character.isDigit(c)) {
                bitboardIndex += Character.getNumericValue(c);
                continue;
            }

            int team = Character.isUpperCase(c) ? 0 : 1;

            switch (Character.toUpperCase(c)) {
                case 'P' ->
                    this.piece_bitboards[team][5] |= (1L << bitboardIndex);
                case 'K' ->
                    this.piece_bitboards[team][0] |= (1L << bitboardIndex);
                case 'Q' ->
                    this.piece_bitboards[team][1] |= (1L << bitboardIndex);
                case 'B' ->
                    this.piece_bitboards[team][2] |= (1L << bitboardIndex);
                case 'R' ->
                    this.piece_bitboards[team][4] |= (1L << bitboardIndex);
                case 'N' ->
                    this.piece_bitboards[team][3] |= (1L << bitboardIndex);
            }

            bitboardIndex++;
        }

        this.isWhiteToMove = parts[1].equals("w");

        if (parts[2].equals("-")) {
            this.castlingRights = 0;
        } else {
            if (parts[2].contains("K")) {
                this.castlingRights |= (1 << 3);
            }
            if (parts[2].contains("Q")) {
                this.castlingRights |= (1 << 2);
            }
            if (parts[2].contains("k")) {
                this.castlingRights |= (1 << 1);
            }
            if (parts[2].contains("q")) {
                this.castlingRights |= 1;
            }
        }

        if (parts[3].equals("-")) {
            this.enpassantIndex = -1;
        } else {
            this.enpassantIndex = Integer.parseInt(parts[3]);
        }

        this.halfMoveClock = Integer.parseInt(parts[4]) * 2 + (this.isWhiteToMove ? 0 : 1);

        this.fullMoveCounter = Integer.parseInt(parts[5]);

    }

    public String toFenString(){

        StringBuilder fenBuilder = new StringBuilder();
    
        for (int rank = 0; rank < 8; rank++) {
            int emptySquares = 0;
            for (int file = 0; file < 8; file++) {
                int bitboardIndex = rank * 8 + file;
                char pieceChar = ' ';
    
                for (int team = 0; team < 2; team++) {
                    for (int pieceType = 0; pieceType < 6; pieceType++) {
                        if ((this.piece_bitboards[team][pieceType] & (1L << bitboardIndex)) != 0) {
                            pieceChar = switch (pieceType) {
                                case 0 -> 'K';
                                case 1 -> 'Q';
                                case 2 -> 'B';
                                case 3 -> 'N';
                                case 4 -> 'R';
                                case 5 -> 'P';
                                default -> ' ';
                            };
    
                            if (team == 1) pieceChar = Character.toLowerCase(pieceChar);
                            break;
                        }
                    }
                    if (pieceChar != ' ') break;
                }
    
                if (pieceChar == ' ') {
                    emptySquares++;
                } else {
                    if (emptySquares > 0) {
                        fenBuilder.append(emptySquares);
                        emptySquares = 0;
                    }
                    fenBuilder.append(pieceChar);
                }
            }
            if (emptySquares > 0) fenBuilder.append(emptySquares);
            if (rank < 7) fenBuilder.append('/');
        }
    
        fenBuilder.append(' ');
        fenBuilder.append(this.isWhiteToMove ? 'w' : 'b');
    
        fenBuilder.append(' ');
        if (this.castlingRights == 0) {
            fenBuilder.append('-');
        } else {
            if ((this.castlingRights & (1 << 3)) != 0) fenBuilder.append('K');
            if ((this.castlingRights & (1 << 2)) != 0) fenBuilder.append('Q');
            if ((this.castlingRights & (1 << 1)) != 0) fenBuilder.append('k');
            if ((this.castlingRights & 1) != 0) fenBuilder.append('q');
        }
    
        fenBuilder.append(' ');
        if (this.enpassantIndex == -1) {
            fenBuilder.append('-');
        } else {
            int rank = this.enpassantIndex / 8;
            int file = this.enpassantIndex % 8;
            fenBuilder.append((char) ('a' + file)).append(rank + 1);
        }
    
        fenBuilder.append(' ');
        fenBuilder.append(this.halfMoveClock / 2);
    
        fenBuilder.append(' ');
        fenBuilder.append(this.fullMoveCounter);
    
        return fenBuilder.toString();
    }
    

    private long hashPosition() {
        return this.zorbistHasher.generateZorbistHash(this);
    }

    private void addPositionToHistoricalPositions() {

        long hash = hashPosition();

        this.historicalPostions.computeIfAbsent(hash, value -> 0);
        this.historicalPostions.put(hash, this.historicalPostions.get(hash) + 1);

    }

    private void removePositionFromHistoricalPositions() {

        long hash = hashPosition();

        this.historicalPostions.put(hash, this.historicalPostions.get(hash) - 1);
    }

    private void removePositionFromHistoricalPositions(long hash) {
        this.historicalPostions.put(hash, this.historicalPostions.get(hash) - 1);
    }

    public int getPieceType(int square) {

        long bitboardSquare = 1L << square;

        for (int i = 0; i < 2; i++) {
            for (int y = 0; y < 6; y++) {
                if ((piece_bitboards[i][y] & bitboardSquare) != 0) {
                    return y + (i * 6);
                }
            }
        }

        return -1;
    }

    public int getPieceValue(int pieceType) {
        // for chess engine 0 - 11 case

        if (pieceType == -1) {
            return 0;
        }

        if (pieceType > 5) {
            pieceType -= 6;
        }

        switch (pieceType) {
            case 0:
                return 999999;
            case 1:
                return 900;
            case 2:
                return 300;
            case 3:
                return 300;
            case 4:
                return 500;
            case 5:
                return 100;
            case 6:
                return 999999;
            case 7:
                return 900;
            case 8:
                return 300;
            case 9:
                return 300;
            case 10:
                return 500;
            case 11:
                return 100;
            default:
                throw new RuntimeErrorException(null, "getPieceValue cannot assign a value to a piecetype not 0 - 11");
        }
    }

    public int movePiece(BitwiseMove move) {
        // handles moving a piece, returns the last captured piece

        int fromSquare = move.getFromSquare();
        int toSquare = move.getToSquare();
        int flag = move.getFlag();

        int pieceType = getPieceType(fromSquare);

        int team = pieceType <= 5 ? 0 : 1;

        //System.out.println("=================");
//
        //System.out.println(bitboardsToString());
//
        //System.out.println();
//
        //System.out.println(move);
        //System.out.println(move.source);

        int boardPieceType = pieceType - (6 * team);
        // used to fix an issue where I have piecetype represented as both [0 or 1][0 - 5] OR [0 - 11]

        int capturePieceType = getPieceType(toSquare); // should be -1 if no capture
        int captureTeam = team ^ 1;

        int captureBoardPieceType = capturePieceType;

        if(capturePieceType != -1){
            captureBoardPieceType = captureBoardPieceType % 6;
        }
        
        /*
        System.out.println("-------------------");

        System.out.println("fromSquare "+ fromSquare); 
        System.out.println("toSquare "+ toSquare); 
        System.out.println("flag "+ flag); 
        System.out.println("pieceType "+ pieceType); 
        System.out.println("team "+ team); 
        System.out.println("boardPieceType "+ boardPieceType); 
        System.out.println("capturePieceType "+ capturePieceType); 
        System.out.println("captureTeam "+ captureTeam); 
        System.out.println("captureBoardPieceType "+ captureBoardPieceType); 
         */
        switch (flag) {
            case BitwiseMove.NORMAL_MOVE:

                performMove(team, boardPieceType, fromSquare, toSquare, captureTeam, captureBoardPieceType, capturePieceType, -1, -1);

                break;

            case BitwiseMove.PAWN_MOVE_DOUBLE:

                int newEnPassantSquare = (fromSquare + toSquare) / 2;

                performMove(team, boardPieceType, fromSquare, toSquare, captureTeam, captureBoardPieceType, -1, -1, newEnPassantSquare);

                break;

            case BitwiseMove.EN_PASSANT_CAPTURE:

                performMove(team, boardPieceType, fromSquare, toSquare, captureTeam, captureBoardPieceType, -1, -1, -1);

                // manual capture :o
                this.piece_bitboards[captureTeam][captureBoardPieceType] ^= (1L << this.enpassantIndex);

                this.enpassantIndex = -1;

                break;

            case BitwiseMove.CASTLE_KINGSIDE:

                // handling this special move type manually :o
                this.piece_bitboards[team][boardPieceType] ^= (1L << fromSquare);
                this.piece_bitboards[team][boardPieceType] ^= (1L << toSquare);

                if (this.isWhiteToMove) {

                    this.piece_bitboards[team][R] ^= (1L << WHITE_KING_ROOK);
                    this.piece_bitboards[team][R] ^= (1L << WHITE_KING_ROOK_CASTLED);

                } else {

                    this.piece_bitboards[team][R] ^= (1L << BLACK_KING_ROOK);
                    this.piece_bitboards[team][R] ^= (1L << BLACK_KING_ROOK_CASTLED);

                }

                int activeCastlingBits = this.isWhiteToMove ? WHITE_CASTLE_BITS : BLACK_CASTLE_BITS;
                this.castlingRights &= ~activeCastlingBits;
                this.enpassantIndex = -1;

                break;

            case BitwiseMove.CASTLE_QUEENSIDE:

                this.piece_bitboards[team][boardPieceType] ^= (1L << fromSquare);
                this.piece_bitboards[team][boardPieceType] ^= (1L << toSquare);

                if (this.isWhiteToMove) {

                    this.piece_bitboards[team][4] ^= (1L << WHITE_QUEEN_ROOK);
                    this.piece_bitboards[team][4] ^= (1L << WHITE_QUEEN_ROOK_CASTLED);

                } else {

                    this.piece_bitboards[team][4] ^= (1L << BLACK_QUEEN_ROOK);
                    this.piece_bitboards[team][4] ^= (1L << BLACK_QUEEN_ROOK_CASTLED);

                }

                int activeCastlingBit = this.isWhiteToMove ? WHITE_CASTLE_BITS : BLACK_CASTLE_BITS;
                this.castlingRights &= ~activeCastlingBit;
                this.enpassantIndex = -1;

                break;

            case BitwiseMove.PROMOTE_TO_BISHOP:
                performMove(team, boardPieceType, fromSquare, toSquare, captureTeam, captureBoardPieceType, capturePieceType, BitwiseMove.PROMOTE_TO_BISHOP, -1);
                break;

            case BitwiseMove.PROMOTE_TO_QUEEN:
                performMove(team, boardPieceType, fromSquare, toSquare, captureTeam, captureBoardPieceType, capturePieceType, BitwiseMove.PROMOTE_TO_QUEEN, -1);
                break;

            case BitwiseMove.PROMOTE_TO_KNIGHT:
                performMove(team, boardPieceType, fromSquare, toSquare, captureTeam, captureBoardPieceType, capturePieceType, BitwiseMove.PROMOTE_TO_KNIGHT, -1);
                break;

            case BitwiseMove.PROMOTE_TO_ROOK:
                performMove(team, boardPieceType, fromSquare, toSquare, captureTeam, captureBoardPieceType, capturePieceType, BitwiseMove.PROMOTE_TO_ROOK, -1);
                break;

            default:
                break;
        }

        this.addPositionToHistoricalPositions();

        this.isWhiteToMove = !this.isWhiteToMove;
        
        return capturePieceType;

    }

    public void unmovePiece(BitwiseMove move, int capturePieceType, byte previousCastlingRights) {

        int fromSquare = move.getFromSquare();
        int toSquare = move.getToSquare();
        int flag = move.getFlag();

        int pieceType = getPieceType(toSquare);
        int team = pieceType <= 5 ? 0 : 1;
        int boardPieceType = pieceType - (6 * team);

        this.castlingRights = previousCastlingRights;

        if (capturePieceType != -1) {
            int captureTeam = capturePieceType > 5 ? 1 : 0;
            int captureBoardPieceType = capturePieceType % 6;

            this.piece_bitboards[captureTeam][captureBoardPieceType] ^= (1L << toSquare);
        }

        switch (flag) {
            case BitwiseMove.NORMAL_MOVE:
                performUndoMove(team, boardPieceType, fromSquare, toSquare);
                break;

            case BitwiseMove.PAWN_MOVE_DOUBLE:
                performUndoMove(team, boardPieceType, fromSquare, toSquare);
                this.enpassantIndex = -1;
                break;

            case BitwiseMove.EN_PASSANT_CAPTURE:
                performUndoMove(team, boardPieceType, fromSquare, toSquare);
                int captureSquare = (toSquare + fromSquare) / 2;
                int captureTeam = team ^ 1;
                this.piece_bitboards[captureTeam][P] ^= (1L << captureSquare);
                this.enpassantIndex = captureSquare;
                break;

            case BitwiseMove.CASTLE_KINGSIDE:
                performUndoMove(team, boardPieceType, fromSquare, toSquare);
                if (team == 1) { // White
                    this.piece_bitboards[team][R] ^= (1L << WHITE_KING_ROOK_CASTLED);
                    this.piece_bitboards[team][R] ^= (1L << WHITE_KING_ROOK);
                } else { // Black
                    this.piece_bitboards[team][R] ^= (1L << BLACK_KING_ROOK_CASTLED);
                    this.piece_bitboards[team][R] ^= (1L << BLACK_KING_ROOK);
                }
                break;

            case BitwiseMove.CASTLE_QUEENSIDE:
                performUndoMove(team, boardPieceType, fromSquare, toSquare);
                if (team == 1) { // White
                    this.piece_bitboards[team][R] ^= (1L << WHITE_QUEEN_ROOK_CASTLED);
                    this.piece_bitboards[team][R] ^= (1L << WHITE_QUEEN_ROOK);
                } else { // Black
                    this.piece_bitboards[team][R] ^= (1L <<BLACK_QUEEN_ROOK_CASTLED);
                    this.piece_bitboards[team][R] ^= (1L <<BLACK_QUEEN_ROOK);
                }
                break;

            case BitwiseMove.PROMOTE_TO_BISHOP:
                this.piece_bitboards[team][BitwiseMove.PROMOTE_TO_BISHOP] ^= (1L << toSquare);
                this.piece_bitboards[team][P] ^= (1L << fromSquare);
                break;

            case BitwiseMove.PROMOTE_TO_QUEEN:
                this.piece_bitboards[team][BitwiseMove.PROMOTE_TO_QUEEN] ^= (1L << toSquare);
                this.piece_bitboards[team][P] ^= (1L << fromSquare);
                break;

            case BitwiseMove.PROMOTE_TO_KNIGHT:
                this.piece_bitboards[team][BitwiseMove.PROMOTE_TO_KNIGHT] ^= (1L << toSquare);
                this.piece_bitboards[team][P] ^= (1L << fromSquare);
                break;

            case BitwiseMove.PROMOTE_TO_ROOK:
                this.piece_bitboards[team][BitwiseMove.PROMOTE_TO_ROOK] ^= (1L << toSquare);
                this.piece_bitboards[team][P] ^= (1L << fromSquare);
                break;

            default:
                throw new RuntimeErrorException(null, "Flag does not match pattern in undo move");
        }

        this.isWhiteToMove = !this.isWhiteToMove; 
    }

    public boolean canCastleQueenside() {
        return (this.castlingRights & (1 << (this.isWhiteToMove ? 2 : 0))) != 0;
    }

    public boolean canCastleKingside() {
        return (this.castlingRights & (2 << (this.isWhiteToMove ? 2 : 0))) != 0;
    }

    public String bitboardsToString() {

        StringBuilder sb = new StringBuilder();

        char[][] pieceRepresentation = {
            {'K', 'Q', 'B', 'N', 'R', 'P'},
            {'k', 'q', 'b', 'n', 'r', 'p'} 
        };

        sb.append("    a b c d e f g h\n");
        sb.append("    _ _ _ _ _ _ _ _\n");
        for (int rank = 0; rank < 8; rank++) {
            sb.append("" + (8 - rank) + " | ");  
            for (int file = 0; file < 8; file++) { 
                boolean piecePlaced = false;
                
                for (int color = 0; color < 2; color++) {
                    for (int pieceType = 0; pieceType < 6; pieceType++) {
                        long bitboard = piece_bitboards[color][pieceType];
                        if ((bitboard & (1L << (rank * 8 + file))) != 0) {
                            sb.append(pieceRepresentation[color][pieceType]+" ");
                            piecePlaced = true;
                            break;
                        }
                    }
                    if (piecePlaced) {
                        break;
                    }
                }

                if (!piecePlaced) {
                    sb.append(". ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //-------------
    private void performMove(int team, int boardPieceType, int fromSquare, int toSquare, int captureTeam, int captureBoardPieceType, int capturePieceType, int promotionType, int newEnPassantSquare) {
        /*
        System.out.println();
        System.out.println("int team " + team); 
        System.out.println("int boardPieceType " + boardPieceType); 
        System.out.println("int fromSquare " + fromSquare); 
        System.out.println("int toSquare " + toSquare); 
        System.out.println("int captureTeam " + captureTeam); 
        System.out.println("int captureBoardPieceType " + captureBoardPieceType); 
        System.out.println("int capturePieceType " + capturePieceType); 
        System.out.println("int promotionType " + promotionType); 
        System.out.println("int newEnPassantSquare " + newEnPassantSquare);
         */

        this.piece_bitboards[team][boardPieceType] ^= (1L << fromSquare);
        this.piece_bitboards[team][boardPieceType] ^= (1L << toSquare);

        if (promotionType != -1) {
            this.piece_bitboards[team][boardPieceType] ^= (1L << toSquare); // Remove pawn
            this.piece_bitboards[team][promotionType] ^= (1L << toSquare); // Add promoted piece
        }

        if (boardPieceType == 0) { // king
            int activeCastlingBits = this.isWhiteToMove ? 0b1100 : 0b0011;
            this.castlingRights &= ~activeCastlingBits;
        }

        if (boardPieceType == 4) { // rook
            int activeCastlingBits;

            if (this.isWhiteToMove) { // is white
                activeCastlingBits = (fromSquare == 0) ? 0b1000 : 0b0100;
            } else { // is black
                activeCastlingBits = (fromSquare == 56) ? 0b0010 : 0b0001;
            }

            this.castlingRights &= ~activeCastlingBits;
        }

        if (captureBoardPieceType == R) { // rook
            int activeCastlingBits;

            if (!this.isWhiteToMove) { // black capturing white
                activeCastlingBits = (toSquare == 63) ? 0b1000 : 0b0100;
            } else { // white capturing black
                activeCastlingBits = (toSquare == 7) ? 0b0010 : 0b0001;
            }

            this.castlingRights &= ~activeCastlingBits;
        }

        if (capturePieceType != -1) {
            //System.out.println("-- inmove : capture team"+captureTeam+", capture type : "+captureBoardPieceType+" --");
            this.piece_bitboards[captureTeam][captureBoardPieceType] ^= (1L << toSquare);
        }

        this.enpassantIndex = -1;
    }

    private void performUndoMove(int team, int boardPieceType, int fromSquare, int toSquare) {
        this.piece_bitboards[team][boardPieceType] ^= (1L << toSquare);
        this.piece_bitboards[team][boardPieceType] ^= (1L << fromSquare);
    }
}
