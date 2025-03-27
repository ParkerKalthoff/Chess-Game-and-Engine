package parkerbasicchessengine.Utils;

public class MoveConversion {

    public static String board(parkerbasicchessengine.Move move) {
        
        String fromSquare = PieceCoordinateConversion.EncodeCoord.board(move.oldCol, move.oldRow);
        String toSquare = PieceCoordinateConversion.EncodeCoord.board(move.newCol, move.newRow);
        String promotion = move.promotedToPiece == null ? "" : (""+move.promotedToPiece.abbreviation).toUpperCase();

        return fromSquare + toSquare + promotion;

    };

    public static String parkerfish_v2(parkerbasicchessengine.Chess_Engines.parkerfish_v2.chess_board.Move move) {

        String fromSquare = PieceCoordinateConversion.EncodeCoord.parkerfish_v2(move.getFromSquare());
        String toSquare = PieceCoordinateConversion.EncodeCoord.parkerfish_v2(move.getToSquare());
        
        String pieceStrs[] = new String[]{"K","Q","B","N","R","P"};
        String promotion = move.isCapture() ? pieceStrs[move.getCapturePieceType()] : ""; 

        return fromSquare + toSquare + promotion;

    };
}
