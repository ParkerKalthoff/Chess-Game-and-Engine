package parkerbasicchessengine.chess_engine;

public class BitwiseBoard {

   
    // [0 white, 1 black] - [0 king, 1 queen, 2 bishop, 3 knight, 4 rook, 5 pawn]
    public long piece_bitboards[][];
    public int enpassantIndex;
    public boolean whiteToMove;
    public byte castlingRights;
    public int halfMoveClock;
    public int fullMoveCounter;

    public long historical_piece_bitboards[][][];

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
                case 'P' -> piece_bitboards[team][5] |= (1L << i);
                case 'K' -> piece_bitboards[team][0] |= (1L << i);
                case 'Q' -> piece_bitboards[team][1] |= (1L << i);
                case 'B' -> piece_bitboards[team][2] |= (1L << i);
                case 'R' -> piece_bitboards[team][4] |= (1L << i);
                case 'N' -> piece_bitboards[team][3] |= (1L << i);
            }
        }

        this.whiteToMove = parts[1].equals("w");

        if(parts[2].equals("-")){
            this.castlingRights = 0;
        } else {
            if(parts[2].contains("K")){this.castlingRights |= (1 << 3);}
            if(parts[2].contains("Q")){this.castlingRights |= (1 << 2);}
            if(parts[2].contains("k")){this.castlingRights |= (1 << 1);}
            if(parts[2].contains("q")){this.castlingRights |= 1;}
        }


        
        String enPassantSquare = parts[3];
        this.halfMoveClock = Integer.parseInt(parts[4] * 2 + this.whiteToMove ? 0 : 1);
        String fullMove = parts[5];



    }
}
