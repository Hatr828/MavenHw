<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Survey</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 32px; background: #f7f7f7; color: #222; }
    .card { background: #fff; padding: 24px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,.08); }
    label { display: block; margin: 12px 0 6px; }
    input, select { width: 100%; padding: 8px 10px; border: 1px solid #ccc; border-radius: 6px; }
    .row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .actions { margin-top: 16px; }
    .btn { background: #2d6cdf; color: #fff; border: 0; padding: 10px 16px; border-radius: 6px; cursor: pointer; }
    .errors { background: #ffe6e6; border: 1px solid #ffb3b3; padding: 12px; border-radius: 6px; margin-bottom: 16px; }
    .result { background: #e9f7ef; border: 1px solid #b7e4c7; padding: 12px; border-radius: 6px; margin-top: 16px; }
  </style>
</head>
<body>
<%
  String fullName = (String) request.getAttribute("fullName");
  String phone = (String) request.getAttribute("phone");
  String email = (String) request.getAttribute("email");
  String age = (String) request.getAttribute("age");
  String gender = (String) request.getAttribute("gender");
  Boolean subscribe = (Boolean) request.getAttribute("subscribe");
  boolean isSubscribed = subscribe != null && subscribe;

  if (fullName == null) { fullName = ""; }
  if (phone == null) { phone = ""; }
  if (email == null) { email = ""; }
  if (age == null) { age = ""; }
  if (gender == null) { gender = ""; }

  List<String> errors = (List<String>) request.getAttribute("errors");
  boolean success = Boolean.TRUE.equals(request.getAttribute("success"));
  String genderLabel = "";
  if ("male".equals(gender)) { genderLabel = "Male"; }
  else if ("female".equals(gender)) { genderLabel = "Female"; }
  else if ("other".equals(gender)) { genderLabel = "Other"; }
%>

<div class="card">
  <h1>Survey</h1>

  <% if (errors != null && !errors.isEmpty()) { %>
    <div class="errors">
      <strong>Please check your data:</strong>
      <ul>
        <% for (String error : errors) { %>
          <li><%= error %></li>
        <% } %>
      </ul>
    </div>
  <% } %>

  <form method="post" action="<%= request.getContextPath() %>/survey">
    <label for="fullName">Full name</label>
    <input id="fullName" name="fullName" type="text" value="<%= fullName %>" required>

    <div class="row">
      <div>
        <label for="phone">Phone</label>
        <input id="phone" name="phone" type="tel" value="<%= phone %>" required>
      </div>
      <div>
        <label for="email">Email</label>
        <input id="email" name="email" type="email" value="<%= email %>" required>
      </div>
    </div>

    <div class="row">
      <div>
        <label for="age">Age</label>
        <input id="age" name="age" type="number" min="1" max="120" value="<%= age %>" required>
      </div>
      <div>
        <label for="gender">Gender</label>
        <select id="gender" name="gender" required>
          <option value="" <%= gender.isEmpty() ? "selected" : "" %>>Select...</option>
          <option value="male" <%= "male".equals(gender) ? "selected" : "" %>>Male</option>
          <option value="female" <%= "female".equals(gender) ? "selected" : "" %>>Female</option>
          <option value="other" <%= "other".equals(gender) ? "selected" : "" %>>Other</option>
        </select>
      </div>
    </div>

    <label>
      <input type="checkbox" name="subscribe" <%= isSubscribed ? "checked" : "" %>>
      Do you want to subscribe to our newsletter?
    </label>

    <div class="actions">
      <button class="btn" type="submit">Submit</button>
    </div>
  </form>

  <% if (success) { %>
    <div class="result">
      <strong>Submitted data:</strong>
      <ul>
        <li>Full name: <%= fullName %></li>
        <li>Phone: <%= phone %></li>
        <li>Email: <%= email %></li>
        <li>Age: <%= age %></li>
        <li>Gender: <%= genderLabel %></li>
        <li>Newsletter: <%= isSubscribed ? "Yes" : "No" %></li>
      </ul>
    </div>
  <% } %>
</div>
</body>
</html>
