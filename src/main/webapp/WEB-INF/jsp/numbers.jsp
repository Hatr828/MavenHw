<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String aRaw = (String) request.getAttribute("aRaw");
    String bRaw = (String) request.getAttribute("bRaw");
    String cRaw = (String) request.getAttribute("cRaw");
    String action = (String) request.getAttribute("action");
    String operationLabel = (String) request.getAttribute("operationLabel");
    String aDisplay = (String) request.getAttribute("aDisplay");
    String bDisplay = (String) request.getAttribute("bDisplay");
    String cDisplay = (String) request.getAttribute("cDisplay");
    String result = (String) request.getAttribute("result");
    java.util.List<String> errors = (java.util.List<String>) request.getAttribute("errors");
    Boolean hasInput = (Boolean) request.getAttribute("hasInput");

    if (action == null) {
        action = "max";
    }
    if (aRaw == null) {
        aRaw = "";
    }
    if (bRaw == null) {
        bRaw = "";
    }
    if (cRaw == null) {
        cRaw = "";
    }
    if (operationLabel == null) {
        operationLabel = "";
    }
    if (aDisplay == null) {
        aDisplay = "";
    }
    if (bDisplay == null) {
        bDisplay = "";
    }
    if (cDisplay == null) {
        cDisplay = "";
    }
    if (result == null) {
        result = "";
    }
    if (hasInput == null) {
        hasInput = false;
    }

    String maxChecked = "max".equals(action) ? "checked" : "";
    String minChecked = "min".equals(action) ? "checked" : "";
    String avgChecked = "avg".equals(action) ? "checked" : "";
    boolean showResult = Boolean.TRUE.equals(hasInput) && errors != null && errors.isEmpty() && !result.isEmpty();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Numbers: Max / Min / Average</title>
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
    <h1>Numbers Calculator</h1>
    <form method="get" action="numbers">
      <div class="row">
        <label for="a">Number A</label>
        <input type="number" step="any" id="a" name="a" value="<%= aRaw %>">
      </div>
      <div class="row">
        <label for="b">Number B</label>
        <input type="number" step="any" id="b" name="b" value="<%= bRaw %>">
      </div>
      <div class="row">
        <label for="c">Number C</label>
        <input type="number" step="any" id="c" name="c" value="<%= cRaw %>">
      </div>
      <div class="row actions">
        <label><input type="radio" name="action" value="max" <%= maxChecked %>> Maximum</label>
        <label><input type="radio" name="action" value="min" <%= minChecked %>> Minimum</label>
        <label><input type="radio" name="action" value="avg" <%= avgChecked %>> Average</label>
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
        <div><strong>Numbers:</strong> <%= aDisplay %>, <%= bDisplay %>, <%= cDisplay %></div>
        <div><strong><%= operationLabel %>:</strong></div>
        <input type="text" readonly value="<%= result %>">
      </div>
    <% } %>
  </div>
</body>
</html>
