<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Memory benchmark</title>
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load('visualization', '1.0', {'packages':['corechart']});
      google.setOnLoadCallback(drawCharts);

      function drawCharts() {
        <#list reportSections as reportSection>
            ${reportSection.chartIdentifier}();
        </#list>
      }

      <#list chartFunctions as chartFunction>
        ${chartFunction}
      </#list>

    </script>
</head>

<body>

    <div style="padding:25px 50px;" >
    <#list reportSections as reportSection>
        <h2 class="sub-header"><strong>${reportSection.reportSectionName}</strong></h2>
        <div style="width:800px; margin:0 auto;" id="${reportSection.chartIdentifier}"></div>
        <table class="table table-striped">
          <thead>
                <tr>
                <th>Benchmark method</th>
                <th>Average(${reportSection.suffix})</th>
                <th>Minimum(${reportSection.suffix})</th>
                <th>Maximum(${reportSection.suffix})</th>
            </tr>
          </thead>
          <#list reportSection.reportSectionTables as reportSectionTable>
          <tbody>
            <tr>
              <td>${reportSectionTable.benchmark}</td>
              <td>${reportSectionTable.average}</td>
              <td>${reportSectionTable.minimum}</td>
              <td>${reportSectionTable.maximum}</td>
            </tr>
          </tbody>
          </#list>
        </table>
    </#list>
    </div>
</body>
</html>