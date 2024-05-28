package com.example.myapplication;
import android.content.Context;

import java.io.*;
import java.util.*;


public class WordsFile  {

    int[] lettersOccurLvl1 = new int[26];
    char[] alphabet = new char[26];
    List<String> wordsList = new ArrayList<>();
    List<Float> wordsRate = new ArrayList<>();

    public WordsFile (Context c){
        WordStorage data = new WordStorage(c);
        char letter = 'A';
        for (int i = 0; i < 26; i++) {
            alphabet[i] = letter;
            letter++;
        }

        this.calculateNbOfOccur(data);
        Collections.sort(wordsList, new AlphabetComparator(alphabet));
        for(String s : wordsList){
            wordsRate.add(rateWordLevel(s));
        }
        System.out.println("Sorted Words: " + wordsList);
        System.out.println("words rate:"+wordsRate);
    }


    void sort(){

        for (int i = 0 ; i<25; i++){
            int max = i ;
            for(int j=i+1 ; j<26; j++){
                if(lettersOccurLvl1[max]<lettersOccurLvl1[j]){
                    max = j ;
                }
            }
            int temp = lettersOccurLvl1[i];
            char tempL = alphabet[i] ;

            lettersOccurLvl1[i]=lettersOccurLvl1[max];
            alphabet[i]=alphabet[max];

            lettersOccurLvl1[max]= temp;
            alphabet[max]=tempL;
        }
    }



    public void  calculateNbOfOccur(WordStorage data){
       for (String word : data.getWords()) {
                if(!word.isEmpty()){
                    wordsList.add(word);
                    int asciiCode = (int) word.charAt(0);
                    lettersOccurLvl1[asciiCode >= 97 ? asciiCode - 97 : asciiCode - 65]++;
                }
            }
        sort();
    }








    public int maxOccurOfLetter(String word){
        int max = 0;
        for(int i=0; i<word.length();i++){
            int cpt=0 ;
            for(int j=0 ; j<word.length();j++){
                if(word.charAt(i)==word.charAt(j))
                    cpt++ ;
            }
            if(cpt>max)
                max=cpt;
        }
        return max;
    }

    public float rateWordLevel(String word){
        float n1 = (float) (maxOccurOfLetter(word)*0.1);
        return (float) (word.length()*0.4 - n1);
    }
}

class AlphabetComparator implements Comparator<String> {
    private final char[] alphabet;

    public AlphabetComparator(char[] alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public int compare(String word1, String word2) {
        char firstLetter1 = word1.toUpperCase().charAt(0);
        char firstLetter2 = word2.toUpperCase().charAt(0);

        // Find the index of the first letter in the alphabet array
        int index1 = indexOfLetter(firstLetter1);
        int index2 = indexOfLetter(firstLetter2);

        // Compare the indices
        return Integer.compare(index1, index2);
    }

    private int indexOfLetter(char letter) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == letter) {
                return i;
            }
        }
        return -1; // Not found
    }

}
