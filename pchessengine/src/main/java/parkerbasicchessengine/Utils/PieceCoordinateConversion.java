package parkerbasicchessengine.Utils;

public class PieceCoordinateConversion {

    public class EncodeCoord {

        public static String board(int col, int row) {
            char rowStr = (char) ('0' + (8 - row));
            char colStr = (char) ('A' + col);
            return "" + colStr + "" + rowStr;
        }

        public static String parkerfish_v2(int index) {
            if (index < 0 || index > 63) {
                throw new IllegalArgumentException("Invalid index: " + index);
            }

            int rankIndex = index / 8;
            int fileIndex = index % 8;

            char file = (char) ('A' + fileIndex);
            char rank = (char) ('8' - rankIndex);

            return String.valueOf(file) + rank;
        }
    }

    public class DecodeCoord {

        public static int parkerfish_v2(String coord) {
            if (coord == null || coord.length() != 2) {
                throw new IllegalArgumentException("Invalid coordinate format: " + coord);
            }

            char file = coord.charAt(0);
            char rank = coord.charAt(1);

            if (file < 'A' || file > 'H' || rank < '1' || rank > '8') {
                throw new IllegalArgumentException("Invalid chess coordinate: " + coord);
            }

            int fileIndex = file - 'A';
            int rankIndex = '8' - rank;

            return rankIndex * 8 + fileIndex;
        }

        public static int[] board(String coord) {
            if (coord == null || coord.length() != 2) {
                throw new IllegalArgumentException("Invalid coordinate format: " + coord);
            }

            char colChar = coord.charAt(0);
            char rowChar = coord.charAt(1);

            if (colChar < 'A' || colChar > 'H' || rowChar < '1' || rowChar > '8') {
                throw new IllegalArgumentException("Invalid chess coordinate: " + coord);
            }

            int col = colChar - 'A';
            int row = 8 - (rowChar - '0');

            return new int[] { col, row };
        }

    }

}
