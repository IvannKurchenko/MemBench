function ${chartName}() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Benchmark');
    data.addColumn('number', 'Memory : ${memoryValue}');
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
    var chart = new google.visualization.ColumnChart(document.getElementById('${chartIdentifier}'));
    chart.draw(data, options);
}