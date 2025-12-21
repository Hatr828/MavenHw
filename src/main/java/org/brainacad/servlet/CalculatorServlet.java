package org.brainacad.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CalculatorServlet", urlPatterns = {"/calculator"})
public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aRaw = req.getParameter("a");
        String bRaw = req.getParameter("b");
        String opRaw = req.getParameter("op");
        String op = opRaw == null ? "add" : opRaw;

        boolean hasInput = aRaw != null || bRaw != null || opRaw != null;
        List<String> errors = new ArrayList<>();
        Double a = null;
        Double b = null;
        Double result = null;
        String operationLabel = null;

        if (hasInput) {
            a = parseDouble(aRaw, "A", errors);
            b = parseDouble(bRaw, "B", errors);

            if (errors.isEmpty()) {
                switch (op) {
                    case "sub":
                        operationLabel = "Subtraction";
                        result = a - b;
                        break;
                    case "mul":
                        operationLabel = "Multiplication";
                        result = a * b;
                        break;
                    case "div":
                        operationLabel = "Division";
                        if (b == 0.0) {
                            errors.add("Division by zero is not allowed.");
                        } else {
                            result = a / b;
                        }
                        break;
                    case "pow":
                        operationLabel = "Power";
                        result = Math.pow(a, b);
                        break;
                    case "percent":
                        operationLabel = "Percent (A% of B)";
                        result = (a * b) / 100.0;
                        break;
                    case "add":
                    default:
                        op = "add";
                        operationLabel = "Addition";
                        result = a + b;
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
        req.setAttribute("op", op);
        req.setAttribute("errors", escapedErrors);
        req.setAttribute("hasInput", hasInput);
        req.setAttribute("operationLabel", escapeHtml(valueOrEmpty(operationLabel)));
        req.setAttribute("aDisplay", escapeHtml(formatNumber(a)));
        req.setAttribute("bDisplay", escapeHtml(formatNumber(b)));
        req.setAttribute("result", escapeHtml(formatNumber(result)));
        req.getRequestDispatcher("/WEB-INF/jsp/calculator.jsp").forward(req, resp);
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
