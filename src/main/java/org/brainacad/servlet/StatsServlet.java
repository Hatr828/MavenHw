package org.brainacad.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "StatsServlet", urlPatterns = {"/numbers"})
public class StatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aRaw = req.getParameter("a");
        String bRaw = req.getParameter("b");
        String cRaw = req.getParameter("c");
        String actionRaw = req.getParameter("action");
        String action = actionRaw == null ? "max" : actionRaw;

        boolean hasInput = aRaw != null || bRaw != null || cRaw != null || actionRaw != null;
        List<String> errors = new ArrayList<>();
        Double a = null;
        Double b = null;
        Double c = null;
        Double result = null;
        String operationLabel = null;

        if (hasInput) {
            a = parseDouble(aRaw, "A", errors);
            b = parseDouble(bRaw, "B", errors);
            c = parseDouble(cRaw, "C", errors);

            if (errors.isEmpty()) {
                switch (action) {
                    case "min":
                        operationLabel = "Minimum";
                        result = Math.min(a, Math.min(b, c));
                        break;
                    case "avg":
                        operationLabel = "Average";
                        result = (a + b + c) / 3.0;
                        break;
                    case "max":
                    default:
                        action = "max";
                        operationLabel = "Maximum";
                        result = Math.max(a, Math.max(b, c));
                        break;
                }
            }
        }

        List<String> escapedErrors = new ArrayList<>();
        for (String error : errors) {
            escapedErrors.add(escapeHtml(error));
        }

        req.setAttribute("aRaw", escapeHtml(valueOrEmpty(aRaw)));
        req.setAttribute("bRaw", escapeHtml(valueOrEmpty(bRaw)));
        req.setAttribute("cRaw", escapeHtml(valueOrEmpty(cRaw)));
        req.setAttribute("action", action);
        req.setAttribute("errors", escapedErrors);
        req.setAttribute("hasInput", hasInput);
        req.setAttribute("operationLabel", escapeHtml(valueOrEmpty(operationLabel)));
        req.setAttribute("aDisplay", escapeHtml(formatNumber(a)));
        req.setAttribute("bDisplay", escapeHtml(formatNumber(b)));
        req.setAttribute("cDisplay", escapeHtml(formatNumber(c)));
        req.setAttribute("result", escapeHtml(formatNumber(result)));

        req.getRequestDispatcher("/WEB-INF/jsp/numbers.jsp").forward(req, resp);
    }

    private static Double parseDouble(String raw, String label, List<String> errors) {
        if (raw == null || raw.trim().isEmpty()) {
            errors.add("Missing number for " + label + ".");
            return null;
        }
        try {
            return Double.valueOf(raw.trim());
        } catch (NumberFormatException ex) {
            errors.add("Invalid number for " + label + ": " + raw);
            return null;
        }
    }

    private static String formatNumber(Double value) {
        return value == null ? "" : value.toString();
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
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
