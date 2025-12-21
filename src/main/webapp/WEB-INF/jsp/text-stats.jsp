<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String text = (String) request.getAttribute("text");
    Boolean hasInput = (Boolean) request.getAttribute("hasInput");
    Integer vowelCount = (Integer) request.getAttribute("vowelCount");
    Integer consonantCount = (Integer) request.getAttribute("consonantCount");
    Integer punctuationCount = (Integer) request.getAttribute("punctuationCount");
    String vowelsList = (String) request.getAttribute("vowelsList");
    String consonantsList = (String) request.getAttribute("consonantsList");
    String punctuationList = (String) request.getAttribute("punctuationList");

    if (text == null) {
        text = "";
    }
    if (hasInput == null) {
        hasInput = false;
    }
    if (vowelCount == null) {
        vowelCount = 0;
    }
    if (consonantCount == null) {
        consonantCount = 0;
    }
    if (punctuationCount == null) {
        punctuationCount = 0;
    }
    if (vowelsList == null) {
        vowelsList = "";
    }
    if (consonantsList == null) {
        consonantsList = "";
    }
    if (punctuationList == null) {
        punctuationList = "";
    }

    String vowelsDisplay = vowelsList.isEmpty() ? "-" : vowelsList;
    String consonantsDisplay = consonantsList.isEmpty() ? "-" : consonantsList;
    String punctuationDisplay = punctuationList.isEmpty() ? "-" : punctuationList;
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Text Statistics</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 40px; background: #f7f7fb; }
    .panel { max-width: 820px; padding: 24px; background: #fff; border-radius: 8px; }
    textarea { width: 100%; min-height: 140px; padding: 8px; }
    .row { margin-bottom: 12px; }
    .result { margin-top: 16px; padding: 12px; background: #f1f6ff; border-radius: 6px; }
    .label { font-weight: bold; }
    .list { margin-top: 6px; }
  </style>
</head>
<body>
  <div class="panel">
    <h1>Text Statistics</h1>
    <form method="post" action="text-stats">
      <div class="row">
        <label class="label" for="text">Text</label>
      </div>
      <div class="row">
        <textarea id="text" name="text"><%= text %></textarea>
      </div>
      <div class="row">
        <button type="submit">Analyze</button>
      </div>
    </form>

    <% if (hasInput) { %>
      <div class="result">
        <div><span class="label">Vowels:</span> <%= vowelCount %></div>
        <div class="list"><%= vowelsDisplay %></div>
        <div><span class="label">Consonants:</span> <%= consonantCount %></div>
        <div class="list"><%= consonantsDisplay %></div>
        <div><span class="label">Punctuation:</span> <%= punctuationCount %></div>
        <div class="list"><%= punctuationDisplay %></div>
      </div>
    <% } %>
  </div>
</body>
</html>
