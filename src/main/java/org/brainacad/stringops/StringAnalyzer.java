package org.brainacad.stringops;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringAnalyzer {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y'));

    public boolean isPalindrome(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string is required");
        }

        StringBuilder normalized = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                normalized.append(Character.toLowerCase(ch));
            }
        }

        int left = 0;
        int right = normalized.length() - 1;
        while (left < right) {
            if (normalized.charAt(left) != normalized.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public int countVowels(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string is required");
        }

        int count = 0;
        for (char ch : input.toLowerCase(Locale.ROOT).toCharArray()) {
            if (Character.isLetter(ch) && VOWELS.contains(ch)) {
                count++;
            }
        }
        return count;
    }

    public int countConsonants(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string is required");
        }

        int count = 0;
        for (char ch : input.toLowerCase(Locale.ROOT).toCharArray()) {
            if (Character.isLetter(ch) && !VOWELS.contains(ch)) {
                count++;
            }
        }
        return count;
    }

    public int countOccurrences(String text, String word) {
        if (text == null || word == null) {
            throw new IllegalArgumentException("Text and word are required");
        }
        String trimmedWord = word.trim();
        if (trimmedWord.isEmpty()) {
            throw new IllegalArgumentException("Word cannot be empty");
        }

        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(trimmedWord) + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
