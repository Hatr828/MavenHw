package org.brainacad.stringops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringAnalyzerTest {

    private final StringAnalyzer analyzer = new StringAnalyzer();

    @Test
    public void detectsPalindromeIgnoringCaseAndPunctuation() {
        assertTrue(analyzer.isPalindrome("A man, a plan, a canal: Panama"));
    }

    @Test
    public void countsVowels() {
        assertEquals(8, analyzer.countVowels("OpenAI makes models"));
    }

    @Test
    public void countsConsonants() {
        assertEquals(9, analyzer.countConsonants("OpenAI makes models"));
    }

    @Test
    public void countsWordOccurrencesCaseInsensitive() {
        String text = "hello HELLO Hello world";
        assertEquals(3, analyzer.countOccurrences(text, "hello"));
    }

    @Test
    public void countsWordOccurrencesWithHyphenBoundaries() {
        String text = "end-to-end end";
        assertEquals(3, analyzer.countOccurrences(text, "end"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnNullPalindromeInput() {
        analyzer.isPalindrome(null);
    }
}
