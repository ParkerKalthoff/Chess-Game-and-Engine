package parkerbasicchessengine.Chess_Engines.parkerfish_v1.move_gen.opening_book;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import parkerbasicchessengine.Chess_Engines.BitwiseMove;

public class OpeningBook {

    private HashMap<String, ArrayList<Pair<String, Integer>>> moveHashmap;
    private Random random;

    public BitwiseMove getPopularMove(String fenString){

        ArrayList<Pair<String, Integer>> moves = moveHashmap.get(fenString);

        if(moves == null){
            return null;
        }

        Pair<String, Integer> popularMove = null;

        for(Pair<String, Integer> move : moves){

            if(popularMove == null || move.getValue() > popularMove.getValue()){
                popularMove = move;
            }

        }

        return null;

    }

    public OpeningBook(){

        final String filename = "opening_book_raw.txt";

        moveHashmap = new HashMap<>();

        try {
            
            String fileContent = new String(Files.readAllBytes(Paths.get(filename)));

            String[] moveInfoArray = fileContent.split(";");

            String[][] fenMoveFrequencyStringArray = new String[moveInfoArray.length][3];

            for (int i = 0; i < moveInfoArray.length; i++) {
                fenMoveFrequencyStringArray[i] = moveInfoArray[i].split(":");
            }

            for(String[] moveInfo : fenMoveFrequencyStringArray){

                String fenString = moveInfo[0];
                String move = moveInfo[1];
                int freq = Integer.parseInt(moveInfo[2]);

                Pair<String, Integer> moveFreqPair = new Pair<>(move, freq);

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

    public class Pair<K, V> {
        private final K key;
        private final V value;
    
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    
        public K getKey() {
            return key;
        }
    
        public V getValue() {
            return value;
        }
    }

}

