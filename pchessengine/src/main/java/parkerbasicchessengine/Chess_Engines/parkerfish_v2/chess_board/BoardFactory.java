package parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board;

import static parkerbasicchessengine.Chess_Engines.parkerfish_v2.utills.consts.*;

public class BoardFactory {

    public static Board createBoard(String fenString) {

        int fullMoveCounter;
        int halfMoveClock;
        boolean isWhitesTurn;
        byte castlingRights; // 0bKQkq
        int enPassantIndex;
        long bitboards[][] = new long[2][6];

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
                    bitboards[team][P] |= (1L << bitboardIndex);
                case 'K' ->
                    bitboards[team][K] |= (1L << bitboardIndex);
                case 'Q' ->
                    bitboards[team][Q] |= (1L << bitboardIndex);
                case 'B' ->
                    bitboards[team][B] |= (1L << bitboardIndex);
                case 'R' ->
                    bitboards[team][R] |= (1L << bitboardIndex);
                case 'N' ->
                    bitboards[team][N] |= (1L << bitboardIndex);
            }

            bitboardIndex++;
        }

        isWhitesTurn = parts[1].equals("w");

        castlingRights = 0;

        if (!parts[2].equals("-")) {

            if (parts[2].contains("K")) {
                castlingRights |= (1 << 3);
            }
            if (parts[2].contains("Q")) {
                castlingRights |= (1 << 2);
            }
            if (parts[2].contains("k")) {
                castlingRights |= (1 << 1);
            }
            if (parts[2].contains("q")) {
                castlingRights |= 1;
            }
        }

        if (parts[3].equals("-")) {
            enPassantIndex = -1;
        } else {
            enPassantIndex = Integer.parseInt(parts[3]);
        }

        halfMoveClock = Integer.parseInt(parts[4]) * 2 + (isWhitesTurn ? 0 : 1);

        fullMoveCounter = Integer.parseInt(parts[5]);

        // just gonna assume a game isnt over lol

        return new Board(fullMoveCounter, halfMoveClock, false, isWhitesTurn, castlingRights, enPassantIndex, bitboards);

    }

}
