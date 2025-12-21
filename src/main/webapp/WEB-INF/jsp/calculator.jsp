<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String aRaw = (String) request.getAttribute("aRaw");
    String bRaw = (String) request.getAttribute("bRaw");
    String op = (String) request.getAttribute("op");
    String operationLabel = (String) request.getAttribute("operationLabel");
    String result = (String) request.getAttribute("result");
    java.util.List<String> errors = (java.util.List<String>) request.getAttribute("errors");
    Boolean hasInput = (Boolean) request.getAttribute("hasInput");

    if (aRaw == null) {
        aRaw = "";
    }
    if (bRaw == null) {
        bRaw = "";
    }
    if (op == null) {
        op = "add";
    }
    if (operationLabel == null) {
        operationLabel = "";
    }
    if (result == null) {
        result = "";
    }
    if (hasInput == null) {
        hasInput = false;
    }

    String addChecked = "add".equals(op) ? "checked" : "";
    String subChecked = "sub".equals(op) ? "checked" : "";
    String mulChecked = "mul".equals(op) ? "checked" : "";
    String divChecked = "div".equals(op) ? "checked" : "";
    String powChecked = "pow".equals(op) ? "checked" : "";
    String percentChecked = "percent".equals(op) ? "checked" : "";
    boolean showResult = Boolean.TRUE.equals(hasInput) && errors != null && errors.isEmpty() && !result.isEmpty();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Calculator</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 40px; background: #f8f8fb; }
    .panel { max-width: 720px; padding: 24px; background: #fff; border-radius: 8px; }
    .row { margin-bottom: 12px; }
    label { display: inline-block; min-width: 120px; }
    input[type="text"], input[type="number"] { padding: 6px 8px; width: 200px; }
    .actions label { min-width: auto; margin-right: 12px; }
    .error { color: #b00020; margin-bottom: 10px; }
    .result { margin-top: 16px; padding: 12px; background: #f1f6ff; border-radius: 6px; }
    .result input { font-weight: bold; }
  </style>
</head>
<body>
  <div class="panel">
    <h1>Calculator</h1>
    <form method="get" action="calculator">
      <div class="row">
        <label for="a">Number A</label>
        <input type="number" step="any" id="a" name="a" value="<%= aRaw %>">
      </div>
      <div class="row">
        <label for="b">Number B</label>
        <input type="number" step="any" id="b" name="b" value="<%= bRaw %>">
      </div>
      <div class="row actions">
        <label><input type="radio" name="op" value="add" <%= addChecked %>> Add</label>
        <label><input type="radio" name="op" value="sub" <%= subChecked %>> Subtract</label>
        <label><input type="radio" name="op" value="mul" <%= mulChecked %>> Multiply</label>
        <label><input type="radio" name="op" value="div" <%= divChecked %>> Divide</label>
        <label><input type="radio" name="op" value="pow" <%= powChecked %>> Power</label>
        <label><input type="radio" name="op" value="percent" <%= percentChecked %>> Percent (A% of B)</label>
      </div>
      <div class="row">
        <button type="submit">Calculate</button>
      </div>
    </form>

    <% if (errors != null && !errors.isEmpty()) { %>
      <% for (String error : errors) { %>
        <div class="error"><%= error %></div>
      <% } %>
    <% } %>

    <% if (showResult) { %>
      <div class="result">
        <div><strong><%= operationLabel %> result:</strong></div>
        <input type="text" readonly value="<%= result %>">
      </div>
    <% } %>
  </div>
</body>
</html>
