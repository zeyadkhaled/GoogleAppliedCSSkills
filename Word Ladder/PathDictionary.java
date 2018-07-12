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

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static HashSet<String> words = new HashSet<>();
    private static HashMap<String, HashSet<String>> neighbors = new HashMap<>();
    static int count = 0;
    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }
            words.add(word);
        }

        //Going over the dictionary and comparing each word to each other word in the dictionary
        for ( String word : words) {
            for (String neighbor : words) {
                if ( !word.equals(neighbor) && isNeighbours( word , neighbor)) {
                    if ( neighbors.containsKey(word))
                        neighbors.get(word).add(neighbor);
                     else {
                        HashSet<String> tmp = new HashSet<>();
                        tmp.add(neighbor);
                        neighbors.put(word, tmp);
                    }
                }
            }
        }
    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    public boolean isNeighbours(String word1, String word2) {
        int c = 0;
        if ( word1.length() != word2.length())
            return false;

        for( int i = 0 ; i < word1.length(); i++) {
            if( word1.charAt(i)!= word2.charAt(i))
                c++;
            if(c > 1)
                return false;
        }
            return true;
    }

    private ArrayList<String> neighbours(String word) {
        HashSet<String> tmp = neighbors.get(word);
        if ( tmp != null)
            return new ArrayList<>(neighbors.get(word));
        return null;
    }

    public String[] findPath(String start, String end) {
        ArrayDeque<ArrayList<String>> deque = new ArrayDeque<>();
        HashSet<String> visited = new HashSet<>();
        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(start);
        deque.addLast(pathList);

        while( !deque.isEmpty())  {
            ArrayList<String> pathFinder = deque.pollFirst();

            if( pathFinder.size() > 8)
                continue;

            String tempEnd = pathFinder.get( pathFinder.size() - 1);
            ArrayList<String> neighbours = neighbours(tempEnd);

            if ( neighbours == null)
                return null;

            visited.add(tempEnd);
            for( int j = 0; j < neighbours.size(); j++) {

                String newWord = neighbours.get(j);
                ArrayList<String> tempPath = new ArrayList<>();

                for(int k = 0; k < pathFinder.size(); k++)  {
                    tempPath.add(pathFinder.get(k));
                }

                if(newWord.equals(end)) {
                    if ( pathFinder.size() < 6)
                        continue;

                    tempPath.add(end);
                    Log.e("final",""+ tempPath.toString());
                    return tempPath.toArray(new String[tempPath.size()]);
                }

                if(visited.contains(newWord))
                    continue;

                tempPath.add(newWord);
                deque.addLast(tempPath);
                Log.d("newPath" , tempPath.toString());
            }
        }
        return null;
    }
}
