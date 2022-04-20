package ascii_art;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Algorithms {
    /**
     * Finds the only duplicated number that appears in the given list. Initializes 2 fields. The first runs on all
     * the given list as a linked list - for each number (from 1 to n) skips to the number that is stored in it's
     * index. The second one skips the same way twice. Eventually they will collide becouse the will end up in the
     * same number because the duplicated number creates a loop if we skip in that way in the given list.
     * Finally we skip once for the 2 field while the first one starts skipping from the first index and the second
     * one from the index the collided. The number that they collide on again is the duplicated one in the kist.
     * @param numList list of numbers on n + 1 length with integers from 1 to n.
     * @return the duplicated number.
     */
    public static int findDuplicate(int [] numList) {
        int initial = 0;
        int walker = numList[initial];
        int runner = numList[walker];
        while (walker != runner) {
            walker = numList[walker];
            runner = numList[numList[runner]];
        }
        walker = initial;
        while (walker != runner) {
            walker = numList[walker];
            runner = numList[runner];
        }
        return walker;
    }

    /**
     * Finds the unique morse words in thw given array. Runs through all words and creates a list of each word's
     * characters. Converts each character (letter) to a morse character and concatenates it to the previous
     * characters from this word converted to morse characters. Finally adds it to a set. If this word already
     * was in the set, it doesn't adds it. Returns the length of the created set which includes only the unique
     * morse words.
     * @param words words to convert to morse words.
     * @return The unique morse words.
     */
    public static int uniqueMorseRepresentations(String[] words) {
        Map<Character, String> morseMap = new HashMap<>();
        morseMap.put('a', ".-");
        morseMap.put('b', "-...");
        morseMap.put('c', "-.-.");
        morseMap.put('d', "-..");
        morseMap.put('e', ".");
        morseMap.put('f', "..-.");
        morseMap.put('g', "--.");
        morseMap.put('h', "....");
        morseMap.put('i', "..");
        morseMap.put('j', ".---");
        morseMap.put('k', "-.-");
        morseMap.put('l', ".-..");
        morseMap.put('m', "--");
        morseMap.put('n', "-.");
        morseMap.put('o', "---");
        morseMap.put('p', ".--.");
        morseMap.put('q', "--.-");
        morseMap.put('r', ".-.");
        morseMap.put('s', "...");
        morseMap.put('t', "-");
        morseMap.put('u', "..-");
        morseMap.put('v', "...-");
        morseMap.put('w', ".--");
        morseMap.put('x', "-..-");
        morseMap.put('y', "-.--");
        morseMap.put('z', "--..");
        HashSet<String> morseWords = new HashSet<>();
        for (String word: words) {
            char[] letters = word.toCharArray();
            String morseWord = "";
            for (char letter: letters) {
                morseWord += morseMap.get(letter);
            }
            morseWords.add(morseWord);
        }
        return morseWords.size();
    }
}
