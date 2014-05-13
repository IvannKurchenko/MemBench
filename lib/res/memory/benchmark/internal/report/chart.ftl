<html>
<head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load('visualization', '1.0', {'packages':['corechart']});
      google.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Benchmark');
        data.addColumn('number', 'Memory');
        data.addRows([
            <#list chartData as data>
                ['${data.title}', ${data.value}],
            </#list>
        ]);

        var options = {
                'title':'${title}',
                'width':600,
                'height':400
                };
        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>
</head>

<body>
    <div id="chart_div"></div>
</body>
</html>