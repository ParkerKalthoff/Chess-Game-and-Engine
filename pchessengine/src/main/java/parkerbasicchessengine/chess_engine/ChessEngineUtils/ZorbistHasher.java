package parkerbasicchessengine.chess_engine.ChessEngineUtils;

import java.util.Random;

import parkerbasicchessengine.chess_engine.BitwiseBoard;

public class ZorbistHasher {

    public ZorbistHasher(){
        this.computeZorbistTables(0);
    }

    public ZorbistHasher(long seed){
        this.computeZorbistTables(seed);
    }

    private long[][] pieceHash = new long[12][64];
    private long whiteToMoveHash;
    private long[] enpassantSquareHash = new long[64];
    private long[] castlingRightsHash = new long[16];

    private void computeZorbistTables(long seed){
        // Only use this ONCE per board instance, recomputing the tables will break memoization and three move repetion functions for an in progress game
        Random random;
        
        if(seed != 0){
            random = new Random(seed);  
        } else {
            random = new Random();
        }

        for(int pieceIndex = 0; pieceIndex < 12; pieceIndex++){
            for(int positionIndex = 0; positionIndex < 64; positionIndex++){
                pieceHash[pieceIndex][positionIndex] = random.nextLong();
            }
        }

        whiteToMoveHash = random.nextLong();

        for(int positionIndex = 0; positionIndex < 64; positionIndex++){
            enpassantSquareHash[positionIndex] = random.nextLong();
        }

        for(int castlingRights = 0; castlingRights < 16; castlingRights++){
            castlingRightsHash[castlingRights] = random.nextLong();
        }
    }

    public long generateZorbistHash(BitwiseBoard bwB){
        
        long zorbistKey = 0;

        // hash position
        for(int teamIndex = 0; teamIndex < 2; teamIndex++){
            
            int pieceIndexTeam = (teamIndex * 6); 
            
            for(int pieceIndex = 0; pieceIndex < 6; pieceIndex++){
                for(int positionIndex = 0; positionIndex < 64; positionIndex++){
                    
                    long targetSquare = 1L << positionIndex;

                    if((bwB.piece_bitboards[teamIndex][pieceIndex] & targetSquare) > 0){
                        zorbistKey ^= pieceHash[pieceIndex + pieceIndexTeam][positionIndex];
                    }
                }
            }
        }

        // hash turn
        if(bwB.isWhiteToMove){
            zorbistKey ^= whiteToMoveHash;
        }

        // hash enpassant
        if(bwB.enpassantIndex != -1){
            zorbistKey ^= enpassantSquareHash[bwB.enpassantIndex];
        }

        // hash castling rights
        zorbistKey ^= castlingRightsHash[bwB.castlingRights];

        return zorbistKey;
    }  
}
