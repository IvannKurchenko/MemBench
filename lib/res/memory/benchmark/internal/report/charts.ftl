<html>
<head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load('visualization', '1.0', {'packages':['corechart']});
      google.setOnLoadCallback(drawCharts);

      function drawCharts() {
        <#list chartIdentifiers as chartIdentifier>
            ${chartIdentifier}();
        </#list>
      }

      <#list chartFunctions as chartFunction>
        ${chartFunction}
      </#list>

    </script>
</head>

<body>
    <#list chartIdentifiers as chartIdentifier>
        <div style="width:800px; margin:0 auto;" id="${chartIdentifier}"></div>
    </#list>
</body>
</html>