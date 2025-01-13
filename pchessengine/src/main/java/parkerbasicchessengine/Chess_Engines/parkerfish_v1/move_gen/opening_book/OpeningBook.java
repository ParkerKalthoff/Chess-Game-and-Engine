package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.opening_book;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import parkerbasicchessengine.Chess_Engines.BitwiseMove;

public class OpeningBook {

    // crappy opening book concept I made
    // a little janky but I was interesting in learning concepts

    private HashMap<String, ArrayList<Triple<String, Integer, Integer>>> moveHashmap;
    private Random random = new Random();

    public BitwiseMove getPopularMove(String fenString){

        ArrayList<Triple<String, Integer, Integer>> moves = moveHashmap.get(fenString);

        if(moves == null){
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

    public OpeningBook(){

        final String filename = "pchessengine\\src\\main\\java\\parkerbasicchessengine\\Chess_Engines\\parkerfish_v1\\move_gen\\opening_book\\processed_moves.txt";

        moveHashmap = new HashMap<>();

        try {
            
            String fileContent = new String(Files.readAllBytes(Paths.get(filename)));

            String[] moveInfoArray = fileContent.split(";");

            String[][] fenMoveFrequencyStringArray = new String[moveInfoArray.length][4];

            for (int i = 0; i < moveInfoArray.length; i++) {
                fenMoveFrequencyStringArray[i] = moveInfoArray[i].split(":");
            }

            for(String[] moveInfo : fenMoveFrequencyStringArray){

                String fenString = moveInfo[0];
                String move = moveInfo[1];
                int freq = Integer.parseInt(moveInfo[2].strip());
                int moveType = Integer.parseInt(moveInfo[3].strip());

                Triple<String, Integer, Integer> moveFreqPair = new Triple<String, Integer, Integer>(move, freq, moveType);

                if(!moveHashmap.containsKey(fenString)){
                    moveHashmap.put(fenString, new ArrayList<>());
                }

                moveHashmap.get(fenString).add(moveFreqPair);
            }

        } catch (IOException e) {
            System.out.println(e);
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
    }

}

