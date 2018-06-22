/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static  int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();


    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            //Filling ArrayList and HashSet with the word.
            wordList.add(word);
            wordSet.add(word);

            //Hashmap of Sorted Key to All words in ArrayList
            ArrayList<String> temp = new ArrayList<String>();
            if ( lettersToWord.containsKey(sortLetters(word))) {
                temp = lettersToWord.get(sortLetters(word));
                temp.add(word);
                lettersToWord.put(sortLetters(word), temp);
            }
            else {
                temp.add(word);
                lettersToWord.put(sortLetters(word), temp);
            }

            // Populating word to size
            if (  sizeToWords.containsKey(word.length())) {
                temp = sizeToWords.get(word.length());
                temp.add(word);
                sizeToWords.put(word.length(),temp);
            }
            else {
                temp = new ArrayList<String>();
                temp.add(word);
                sizeToWords.put(word.length(),temp);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if ( wordSet.contains(word)) {
            if ( !containsBase(word , base))
                return true;
            else
                return false;
        } else
            return false;
    }

    public  boolean containsBase(String word, String base) {
        if (base.length() > word.length())
            return false;

        for (int i = 0; i < word.length() - base.length() + 1; i++) {
            if ( word.substring(i, i + base.length()).equals(base))
                return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {

        if ( lettersToWord.containsKey(sortLetters(targetWord)))
            return lettersToWord.get(sortLetters(targetWord));
        else
            return new ArrayList<>();
    }

    public  String sortLetters(String word) {
        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String alphabet  = "abcdefeghjiklmnopqrstuvwxyz";
        for ( int i = 0; i < alphabet.length(); i++) {
            String temp = word + alphabet.charAt(i);
            if ( lettersToWord.containsKey(sortLetters(temp))) {
                for ( String s : lettersToWord.get(sortLetters(temp))) {
                    result.add(s);
                }
            }
        }
        return result;
    }




    public String pickGoodStarterWord() {
        boolean stop = false;
        String word = "";
        ArrayList<String> temp = sizeToWords.get(wordLength);
        while ( !stop ) {
            word =  temp.get(random.nextInt(temp.size()));
            if ( getAnagramsWithOneMoreLetter(word).size() >= MIN_NUM_ANAGRAMS) {
                stop = true;
                if ( wordLength < MAX_WORD_LENGTH )
                    wordLength++;
               }

        }
        return word;
    }
}
