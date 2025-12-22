package org.brainacad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SurveyServlet extends HttpServlet {
    private static final String JSP_PATH = "/WEB-INF/jsp/survey.jsp";
    private static final Set<String> ALLOWED_GENDERS =
            new HashSet<>(Arrays.asList("male", "female", "other"));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String fullName = normalize(request.getParameter("fullName"));
        String phone = normalize(request.getParameter("phone"));
        String email = normalize(request.getParameter("email"));
        String ageRaw = normalize(request.getParameter("age"));
        String gender = normalize(request.getParameter("gender"));
        boolean subscribe = request.getParameter("subscribe") != null;

        List<String> errors = new ArrayList<>();

        if (fullName.isEmpty() || !fullName.matches("^[\\p{L}][\\p{L}\\s'-]{1,}$")) {
            errors.add("Please enter a valid full name.");
        }
        if (phone.isEmpty() || !phone.matches("^\\+?[0-9\\s\\-()]{7,20}$")) {
            errors.add("Please enter a valid phone number.");
        }
        if (email.isEmpty() || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            errors.add("Please enter a valid email.");
        }

        if (ageRaw.isEmpty()) {
            errors.add("Please enter your age.");
        } else {
            try {
                int age = Integer.parseInt(ageRaw);
                if (age < 1 || age > 120) {
                    errors.add("Age must be between 1 and 120.");
                }
            } catch (NumberFormatException ex) {
                errors.add("Age must be a number.");
            }
        }

        if (!ALLOWED_GENDERS.contains(gender)) {
            errors.add("Please select a gender.");
        }

        request.setAttribute("fullName", fullName);
        request.setAttribute("phone", phone);
        request.setAttribute("email", email);
        request.setAttribute("age", ageRaw);
        request.setAttribute("gender", gender);
        request.setAttribute("subscribe", subscribe);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(JSP_PATH).forward(request, response);
            return;
        }

        request.setAttribute("success", true);
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
