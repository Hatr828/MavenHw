package org.brainacad.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TextStatsServlet", urlPatterns = {"/text-stats"})
public class TextStatsServlet extends HttpServlet {

    private static final String VOWELS = "aeiouy";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("text", "");
        req.setAttribute("hasInput", false);
        req.setAttribute("vowelCount", 0);
        req.setAttribute("consonantCount", 0);
        req.setAttribute("punctuationCount", 0);
        req.setAttribute("vowelsList", "");
        req.setAttribute("consonantsList", "");
        req.setAttribute("punctuationList", "");
        req.getRequestDispatcher("/WEB-INF/jsp/text-stats.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String text = req.getParameter("text");
        if (text == null) {
            text = "";
        }

        int vowelCount = 0;
        int consonantCount = 0;
        int punctuationCount = 0;
        StringBuilder vowelsList = new StringBuilder();
        StringBuilder consonantsList = new StringBuilder();
        StringBuilder punctuationList = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                if (isVowel(ch)) {
                    vowelCount++;
                    appendWithSpace(vowelsList, ch);
                } else {
                    consonantCount++;
                    appendWithSpace(consonantsList, ch);
                }
            } else if (isPunctuation(ch)) {
                punctuationCount++;
                appendWithSpace(punctuationList, ch);
            }
        }

        req.setAttribute("text", escapeHtml(text));
        req.setAttribute("hasInput", true);
        req.setAttribute("vowelCount", vowelCount);
        req.setAttribute("consonantCount", consonantCount);
        req.setAttribute("punctuationCount", punctuationCount);
        req.setAttribute("vowelsList", escapeHtml(vowelsList.toString()));
        req.setAttribute("consonantsList", escapeHtml(consonantsList.toString()));
        req.setAttribute("punctuationList", escapeHtml(punctuationList.toString()));
        req.getRequestDispatcher("/WEB-INF/jsp/text-stats.jsp").forward(req, resp);
    }

    private static boolean isVowel(char ch) {
        char lower = Character.toLowerCase(ch);
        return VOWELS.indexOf(lower) >= 0;
    }

    private static boolean isPunctuation(char ch) {
        switch (Character.getType(ch)) {
            case Character.CONNECTOR_PUNCTUATION:
            case Character.DASH_PUNCTUATION:
            case Character.START_PUNCTUATION:
            case Character.END_PUNCTUATION:
            case Character.INITIAL_QUOTE_PUNCTUATION:
            case Character.FINAL_QUOTE_PUNCTUATION:
            case Character.OTHER_PUNCTUATION:
                return true;
            default:
                return false;
        }
    }

    private static void appendWithSpace(StringBuilder builder, char ch) {
        if (builder.length() > 0) {
            builder.append(' ');
        }
        builder.append(ch);
    }

    private static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
