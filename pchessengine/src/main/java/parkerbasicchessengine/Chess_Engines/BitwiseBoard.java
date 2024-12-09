package parkerbasicchessengine.Chess_Engines;

import java.util.HashMap;

import javax.management.RuntimeErrorException;

import parkerbasicchessengine.Board;
import parkerbasicchessengine.Chess_Engines.ChessEngineUtils.ZorbistHasher;

public class BitwiseBoard {

    // bitwise board is meant to be a lightweight way of encapsulating chess data
    // the intention is to have a way to mutate board state for an engine
    // is not meant for generating legal moves or verifying legal positions

    // [0 white, 1 black] - [0 king, 1 queen, 2 bishop, 3 knight, 4 rook, 5 pawn]
    public long piece_bitboards[][];
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

    public BitwiseBoard(String fenString) {

        String parts[] = fenString.split(" ");
        char piecePlacementChars[] = parts[0].toCharArray();

        for (int i = 0; i < piecePlacementChars.length; i++) {

            if (piecePlacementChars[i] == '/') {
                continue;
            }

            if (Character.isDigit(piecePlacementChars[i])) {
                i += Character.getNumericValue(piecePlacementChars[i]) - 1;
                continue;
            }

            int team = Character.isUpperCase(piecePlacementChars[i]) ? 0 : 1;

            switch (Character.toUpperCase(piecePlacementChars[i])) {
                case 'P' ->
                    this.piece_bitboards[team][5] |= (1L << i);
                case 'K' ->
                    this.piece_bitboards[team][0] |= (1L << i);
                case 'Q' ->
                    this.piece_bitboards[team][1] |= (1L << i);
                case 'B' ->
                    this.piece_bitboards[team][2] |= (1L << i);
                case 'R' ->
                    this.piece_bitboards[team][4] |= (1L << i);
                case 'N' ->
                    this.piece_bitboards[team][3] |= (1L << i);
            }
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

        if(parts[3].equals("-")){
            this.enpassantIndex = -1;
        } else {
            this.enpassantIndex = Integer.parseInt(parts[3]);
        }

        this.halfMoveClock = Integer.parseInt(parts[4]) * 2 + (this.isWhiteToMove ? 0 : 1);

        this.fullMoveCounter = Integer.parseInt(parts[5]);
    }

    private long hashPosition(){
        return this.zorbistHasher.generateZorbistHash(this);
    }

    private void addPositionToHistoricalPositions(){

        long hash = hashPosition();

        this.historicalPostions.computeIfAbsent(hash, value -> 0);
        this.historicalPostions.put(hash, this.historicalPostions.get(hash) + 1); 

    }
    
    private void removePositionFromHistoricalPositions(){

        long hash = hashPosition();

        this.historicalPostions.put(hash, this.historicalPostions.get(hash) - 1); 
    }

    private void removePositionFromHistoricalPositions(long hash){
        this.historicalPostions.put(hash, this.historicalPostions.get(hash) - 1); 
    }

    public int getPieceType(int square){

        long bitboardSquare = 1L << square;

        for(int i = 0; i < 2; i++){
            for(int y = 0; y < 6; y++){
                if((piece_bitboards[i][y] & bitboardSquare) != 0){
                    return y + (i * 6);
                }
            }
        }

        return -1;
    }

    public int getPieceValue(int pieceType){
        // for chess engine 0 - 11 case

        if(pieceType == -1){
            return 0;
        }

        if(pieceType > 5){
            pieceType -= 6;
        }

        switch (pieceType) {
            case 0: return 999999;
            case 1: return 900;
            case 2: return 300;
            case 3: return 300;
            case 4: return 500;
            case 5: return 100;
            case 6: return 999999;
            case 7: return 900;
            case 8: return 300;
            case 9: return 300;
            case 10: return 500;
            case 11: return 100;
            default:    
                throw new RuntimeErrorException(null, "getPieceValue cannot assign a value to a piecetype not 0 - 11");
        }
    }

    public int movePiece(BitwiseMove move){
        // handles moving a piece, returns the last captured piece

        int fromSquare = move.getFromSquare();
        int toSquare = move.getToSquare();
        int flag =  move.getFlag();

        int pieceType = getPieceType(fromSquare);

        int team = pieceType > 5 ? 0 : 1;
        int boardPieceType = pieceType - (6 * team); 
        // used to fix an issue where I have piecetype represented as both [0 or 1][0 - 5] OR [0 - 11]

        int capturePieceType = getPieceType(toSquare); // should be -1 if no capture
        int captureTeam = team ^ 1;
        int enemyBoardPieceType = pieceType - (6 * captureTeam); 

        switch (flag) {
            case BitwiseMove.NORMAL_MOVE:

                this.piece_bitboards[team][boardPieceType] ^= (1L << fromSquare);
                this.piece_bitboards[team][boardPieceType] ^= (1L << toSquare);

                if(boardPieceType == 0){
                    int activeCastlingBits = this.isWhiteToMove ? 0b1100 : 0b0011;
                    this.castlingRights &= ~activeCastlingBits;
                }

                if(boardPieceType == 4){
                    int activeCastlingBits = this.isWhiteToMove ? 0b1100 : 0b0011;
                    this.castlingRights &= ~activeCastlingBits;
                }

                if(capturePieceType != -1){
                    this.piece_bitboards[captureTeam][enemyBoardPieceType] ^= (1L << toSquare);
                }

                break;
        
            default:
                break;
        }

        return capturePieceType;
        
    }

    
    public void unmovePiece(BitwiseMove move){
        
        throw new UnsupportedOperationException("unmake move is not implimented");
    }

    public boolean canCastleQueenside(){
        return (this.castlingRights & (1 << (this.isWhiteToMove ? 2 : 0))) != 0;
    }

    public boolean canCastleKingside(){
        return (this.castlingRights & (2 << (this.isWhiteToMove ? 2 : 0))) != 0;
    }
}
