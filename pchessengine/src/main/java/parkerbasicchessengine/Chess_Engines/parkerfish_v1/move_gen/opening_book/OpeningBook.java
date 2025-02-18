package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.opening_book;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import parkerbasicchessengine.Chess_Engines.parkerfish_v1.BitwiseMove;

public class OpeningBook {

    // crappy opening book concept I made
    // a little janky but I was interesting in learning concepts

    private HashMap<String, ArrayList<Triple<String, Integer, Integer>>> moveHashmap;
    private Random random = new Random();

    public BitwiseMove getPopularMove(String fenString){

        System.out.println("Looking for FEN: " + fenString);
        System.out.println("Moves found: " + moveHashmap.get(fenString));

        ArrayList<Triple<String, Integer, Integer>> moves = moveHashmap.get(fenString);

        if(moves == null){
            System.out.println("OpeningBook.java : Position doesnt exist in opening book, returning null");
            // doesnt exist return null 
            return null;
        }

        Triple<String, Integer, Integer> popularMove = null;

        for(Triple<String, Integer, Integer> move : moves){
            if(popularMove == null || move.getFreq() > popularMove.getFreq()){
                popularMove = move;
            }
        }

        BitwiseMove boardMove = convertStringToBitwiseMove(popularMove);

        return boardMove;

    }

    /**
    * Returns a 'random' move that is present in the opening book. This method weights it by popularity
    * So deviations can and will happen, but will be uncommon
    * @param fenString the current position of the board
    * @return returns a random bitwise move in the opening book
    */
    public BitwiseMove getRandomMove(String fenString){

        ArrayList<Triple<String, Integer, Integer>> moves = moveHashmap.get(fenString);

        if(moves == null){
            // doesnt exist, return null
            return null;
        }

        int totalWeight = 0;

        for(Triple<String, Integer, Integer> move : moves){
            totalWeight += move.getFreq();
        }

        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;

        for (Triple<String, Integer, Integer> move : moves) {
            cumulativeWeight += move.getFreq();
            if (cumulativeWeight > randomValue) {
                BitwiseMove randomMove = convertStringToBitwiseMove(move);
                return randomMove;
            }
        }

        return null;
    }

    private BitwiseMove convertStringToBitwiseMove(Triple<String, Integer, Integer> moveInfo){
        
        String move = moveInfo.getMove();
        int type = moveInfo.getType();

        String fromString = move.substring(0, 2);
        String toString = move.substring(2, 4);

        int fromIndex = coordToIndex(fromString);
        int toIndex = coordToIndex(toString);

        BitwiseMove boardMove = new BitwiseMove(fromIndex, toIndex, type);

        return boardMove;
    }

    public OpeningBook() {
        final String filename = "pchessengine\\src\\main\\java\\parkerbasicchessengine\\Chess_Engines\\parkerfish_v1\\move_gen\\opening_book\\processed_moves.txt";
    
        moveHashmap = new HashMap<>();
    
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filename)));
    
            String[] moveInfoArray = fileContent.split(";");
    
            for (String entry : moveInfoArray) {
                if (entry.strip().isEmpty()) continue;
    
                String[] parts = entry.split(":");
                if (parts.length != 4) {
                    System.out.println("Malformed entry: " + entry);
                    continue;
                }
    
                String fenString = parts[0].strip();
                String move = parts[1].strip();
                int freq = Integer.parseInt(parts[2].strip());
                int moveType = Integer.parseInt(parts[3].strip());
    
                Triple<String, Integer, Integer> moveFreqPair = new Triple<>(move, freq, moveType);
    
                moveHashmap.computeIfAbsent(fenString, k -> new ArrayList<>()).add(moveFreqPair);
            }
    
        } catch (IOException e) {
            System.out.println("Error reading opening book file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number in opening book file: " + e.getMessage());
        }
    }
    

    public static int coordToIndex(String coord) {
        if (coord == null || coord.length() != 2) {
            return -1;
        }

        char file = coord.charAt(0); 
        char rank = coord.charAt(1); 

        if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
            return -1; 
        }

        int fileIndex = file - 'a';
        int rankIndex = '8' - rank;

        return rankIndex * 8 + fileIndex;
    }

    /* Pair util class */

    public class Triple<K, V, T> {
        private final K move;
        private final V freq;
        private final T type;
    
        public Triple(K move, V freq, T type) {
            this.move = move;
            this.freq = freq;
            this.type = type;
        }
    
        public K getMove() {
            return this.move;
        }
    
        public V getFreq() {
            return this.freq;
        }

        public T getType() {
            return this.type;
        }

        public String toString(){
            return "("+move+", "+freq+")";
        }
    }

}

