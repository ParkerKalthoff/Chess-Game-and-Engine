package parkerbasicchessengine;

import javax.management.RuntimeErrorException;

public class ChessEngineManager {

    public IChessGameInput whitePlayer;
    public IChessGameInput blackPlayer;
    private Board board;

    public ChessEngineManager(Board board) {
        this.board = board;
    }

    public void setWhitePlayer(IChessGameInput whitePlayer) {
        this.whitePlayer = whitePlayer;
        whitePlayer.loadPosition(board.convertPostionToFEN());
    }
    
    public void setBlackPlayer(IChessGameInput blackPlayer) {
        this.blackPlayer = blackPlayer;
        blackPlayer.loadPosition(board.convertPostionToFEN());
    }

    public void syncEngine(String move) {
        
        /* Special Case where you're having an engine play itself, no need to sync */
        if(whitePlayer == blackPlayer) {
            return;
        }

        if (board.isWhiteToMove && whitePlayer != null) {
            // Sync white's move after it is made
            System.out.println("Syncing White Engine...");
            whitePlayer.syncEngineBoard(move);

            if(!whitePlayer.getFenString().equals(board.convertPostionToFEN())) {
                System.out.println("<!>Warning! Fen Strings deviated <!>");
                System.out.println("White Engine : " + whitePlayer.getFenString());
                System.out.println("Board        : " + board.convertPostionToFEN());
            }

            whitePlayer.printBoard();

        } else if (!board.isWhiteToMove && blackPlayer != null) {
            // Sync black's move after it is made
            System.out.println("Syncing Black Engine...");
            blackPlayer.syncEngineBoard(move);

            if(!blackPlayer.getFenString().equals(board.convertPostionToFEN())) {
                System.out.println("\n<!> Warning! Fen Strings deviated <!>");
                System.out.println("Black Engine : " + blackPlayer.getFenString());
                System.out.println("Board        : " + board.convertPostionToFEN()+"\n");
            }

            blackPlayer.printBoard();
        }
    }

    public void engineMakeMove() {

        if (board.isWhiteToMove && whitePlayer != null) {
            
            String whiteMove = whitePlayer.engineMakeMove();

            if(whiteMove == null) {
                return;
            }

            board.makeMoveUsingCoordinate(whiteMove);

        } else if (!board.isWhiteToMove && blackPlayer != null) {
            
            String blackMove = blackPlayer.engineMakeMove();
            
            if(blackMove == null) {
                return;
            }

            board.makeMoveUsingCoordinate(blackMove);
        
        } else {
        
            String turn = board.isWhiteToMove ? "White" : "Black";
            throw new RuntimeErrorException(null, "No Engine set for " + turn);
        
        }
    }
}
