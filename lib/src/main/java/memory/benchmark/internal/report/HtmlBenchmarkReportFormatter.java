package memory.benchmark.internal.report;

import freemarker.template.*;
import memory.benchmark.api.Options;
import memory.benchmark.api.result.BenchmarkResult;
import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.StatisticView;

import java.io.*;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static memory.benchmark.internal.util.ThrowableHandlers.printThrowableAction;

public class HtmlBenchmarkReportFormatter implements BenchmarkReportFormatter {

    private static final String CHARTS_TEMPLATE = "charts.ftl";
    private static final String CHART_FUNCTION_TEMPLATE = "chart_func_template.ftl";
    private static final String REPORT_FILE_NAME = "report.html";

    private static final String CHART_NAME = "chartName";
    private static final String MEMORY_VALUE = "memoryValue";
    private static final String TITLE = "title";
    private static final String CHART_DATA_TAG = "chartData";
    private static final String CHART_IDENTIFIER_TAG = "chartIdentifier";
    private static final String CHART_FUNCTIONS_TAG = "chartFunctions";
    private static final String CHART_IDENTIFIERS_TAG = "chartIdentifiers";

    private static final String USED_HEAP_MEMORY_ID_TAG = "used_heap_memory";
    private static final String USED_HEAP_MEMORY_TITLE_TAG = "Used heap memory";

    private static final String COMMITTED_HEAP_MEMORY_ID_TAG = "committed_heap_memory";
    private static final String COMMITTED_HEAP_MEMORY_TITLE_TAG = "Committed heap memory";

    private static final String MAX_HEAP_MEMORY_ID_TAG = "max_heap_memory";
    private static final String MAX_HEAP_MEMORY_TITLE_TAG = "Max heap memory";

    private static final String USED_NON_HEAP_MEMORY_ID_TAG = "used_non_heap_memory";
    private static final String USED_NON_HEAP_MEMORY_TITLE_TAG = "Used non heap memory";

    private static final String COMMITTED_NON_HEAP_MEMORY_ID_TAG = "committed_non_heap_memory";
    private static final String COMMITTED_NON_HEAP_MEMORY_TITLE_TAG = "Committed non heap memory";

    private static final String MAX_NON_HEAP_MEMORY_ID_TAG = "max_non_heap_memory";
    private static final String MAX_NON_HEAP_MEMORY_TITLE_TAG = "Max non heap memory";

    private final Options options;

    public HtmlBenchmarkReportFormatter(Options options) {
        this.options = options;
    }

    @Override
    public void formatReport(List<BenchmarkResult> benchmarkResults) {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HtmlBenchmarkReportFormatter.class, "");
        configuration.setIncompatibleImprovements(new Version(2, 3, 20));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Optional<Writer> fileWriterOptional = Optional.empty();

        try {

            Template template = configuration.getTemplate(CHARTS_TEMPLATE);
            Writer fileWriter = new FileWriter(new File(REPORT_FILE_NAME));
            fileWriterOptional = Optional.of(fileWriter);
            Map<String, Object> templateParameters = createTemplateParameters(configuration, benchmarkResults);
            template.process(templateParameters, fileWriter);

        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        } finally {
            fileWriterOptional.ifPresent(w -> printThrowableAction(w::close));
        }
    }

    private Map<String, Object> createTemplateParameters(Configuration configuration, List<BenchmarkResult> benchmarkResults) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<>();
        Map<Class, List<BenchmarkResult>> mappedBenchmarkResults = mapBenchmarksResult(benchmarkResults);

        List<String> chartFunctions = new ArrayList<>();
        List<String> chartIdentifiers = new ArrayList<>();

        if(options.getReportInformation().contains(Options.ReportInformation.HEAP_MEMORY_FOOTPRINT)) {
            addHeapMemoryUsage(configuration, benchmarkResults, chartFunctions, chartIdentifiers);
        }

        if(options.getReportInformation().contains(Options.ReportInformation.NON_HEAP_MEMORY_FOOTPRINT)) {
            addNonHeapMemoryUsage(configuration, benchmarkResults, chartFunctions, chartIdentifiers);
        }

        parameters.put(CHART_FUNCTIONS_TAG, chartFunctions);
        parameters.put(CHART_IDENTIFIERS_TAG, chartIdentifiers);

        return parameters;
    }

    private Map<Class, List<BenchmarkResult>> mapBenchmarksResult(List<BenchmarkResult> benchmarkResults) {
        Map<Class, List<BenchmarkResult>> classListMap = new HashMap<>();

        for(BenchmarkResult benchmarkResult : benchmarkResults) {
            Class clazz = benchmarkResult.getBenchmarkClass();
            List<BenchmarkResult> results = classListMap.get(clazz);
            if(results == null) {
                results = new ArrayList<>();
                classListMap.put(clazz, results);
            }
            results.add(benchmarkResult);
        }

        return classListMap;
    }

    private void addHeapMemoryUsage(Configuration configuration, List<BenchmarkResult> benchmarkResults, List<String> chartFunctions, List<String> chartIdentifiers) throws IOException, TemplateException {
        chartFunctions.add(createUsedHeapMemoryFootprintChartFunction(configuration, benchmarkResults));
        chartIdentifiers.add(USED_HEAP_MEMORY_ID_TAG);

        chartFunctions.add(createCommittedHeapMemoryFootprintChartFunction(configuration, benchmarkResults));
        chartIdentifiers.add(COMMITTED_HEAP_MEMORY_ID_TAG);

        chartFunctions.add(createMaxHeapMemoryFootprintChartFunction(configuration, benchmarkResults));
        chartIdentifiers.add(MAX_HEAP_MEMORY_ID_TAG);
    }

    private void addNonHeapMemoryUsage(Configuration configuration, List<BenchmarkResult> benchmarkResults, List<String> chartFunctions, List<String> chartIdentifiers) throws IOException, TemplateException {
        chartFunctions.add(createUsedNonHeapMemoryFootprintChartFunction(configuration, benchmarkResults));
        chartIdentifiers.add(USED_NON_HEAP_MEMORY_ID_TAG);

        chartFunctions.add(createCommittedNonHeapMemoryFootprintChartFunction(configuration, benchmarkResults));
        chartIdentifiers.add(COMMITTED_NON_HEAP_MEMORY_ID_TAG);

        chartFunctions.add(createMaxNonHeapMemoryFootprintChartFunction(configuration, benchmarkResults));
        chartIdentifiers.add(MAX_NON_HEAP_MEMORY_ID_TAG);
    }


    private String createUsedHeapMemoryFootprintChartFunction(Configuration configuration, List<BenchmarkResult> benchmarkResult) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CHART_NAME, USED_HEAP_MEMORY_ID_TAG);
        parameters.put(MEMORY_VALUE, "Mb");
        parameters.put(TITLE, USED_HEAP_MEMORY_TITLE_TAG);
        parameters.put(CHART_DATA_TAG, mapChartData(benchmarkResult, this::usedHeapMemoryFootprintToChartData));
        parameters.put(CHART_IDENTIFIER_TAG, USED_HEAP_MEMORY_ID_TAG);
        return createChartFunction(configuration, parameters);
    }

    private String createCommittedHeapMemoryFootprintChartFunction(Configuration configuration, List<BenchmarkResult> benchmarkResult) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CHART_NAME, COMMITTED_HEAP_MEMORY_ID_TAG);
        parameters.put(MEMORY_VALUE, "Mb");
        parameters.put(TITLE, COMMITTED_HEAP_MEMORY_TITLE_TAG);
        parameters.put(CHART_DATA_TAG, mapChartData(benchmarkResult, this::committedHeapMemoryFootprintToChartData));
        parameters.put(CHART_IDENTIFIER_TAG, COMMITTED_HEAP_MEMORY_ID_TAG);
        return createChartFunction(configuration, parameters);
    }

    private String createMaxHeapMemoryFootprintChartFunction(Configuration configuration, List<BenchmarkResult> benchmarkResult) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CHART_NAME, MAX_HEAP_MEMORY_ID_TAG);
        parameters.put(MEMORY_VALUE, "Mb");
        parameters.put(TITLE, MAX_HEAP_MEMORY_TITLE_TAG);
        parameters.put(CHART_DATA_TAG, mapChartData(benchmarkResult, this::maxHeapMemoryFootprintToChartData));
        parameters.put(CHART_IDENTIFIER_TAG, MAX_HEAP_MEMORY_ID_TAG);
        return createChartFunction(configuration, parameters);
    }

    private String createUsedNonHeapMemoryFootprintChartFunction(Configuration configuration, List<BenchmarkResult> benchmarkResult) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CHART_NAME, USED_NON_HEAP_MEMORY_ID_TAG);
        parameters.put(MEMORY_VALUE, "Mb");
        parameters.put(TITLE, USED_NON_HEAP_MEMORY_TITLE_TAG);
        parameters.put(CHART_DATA_TAG, mapChartData(benchmarkResult, this::usedNonHeapMemoryFootprintToChartData));
        parameters.put(CHART_IDENTIFIER_TAG, USED_NON_HEAP_MEMORY_ID_TAG);
        return createChartFunction(configuration, parameters);
    }

    private String createCommittedNonHeapMemoryFootprintChartFunction(Configuration configuration, List<BenchmarkResult> benchmarkResult) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CHART_NAME, COMMITTED_NON_HEAP_MEMORY_ID_TAG);
        parameters.put(MEMORY_VALUE, "Mb");
        parameters.put(TITLE, COMMITTED_NON_HEAP_MEMORY_TITLE_TAG);
        parameters.put(CHART_DATA_TAG, mapChartData(benchmarkResult, this::committedNonHeapMemoryFootprintToChartData));
        parameters.put(CHART_IDENTIFIER_TAG, COMMITTED_NON_HEAP_MEMORY_ID_TAG);
        return createChartFunction(configuration, parameters);
    }

    private String createMaxNonHeapMemoryFootprintChartFunction(Configuration configuration, List<BenchmarkResult> benchmarkResult) throws IOException, TemplateException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CHART_NAME, MAX_NON_HEAP_MEMORY_ID_TAG);
        parameters.put(MEMORY_VALUE, "Mb");
        parameters.put(TITLE, MAX_NON_HEAP_MEMORY_TITLE_TAG);
        parameters.put(CHART_DATA_TAG, mapChartData(benchmarkResult, this::maxNonHeapMemoryFootprintToChartData));
        parameters.put(CHART_IDENTIFIER_TAG, MAX_NON_HEAP_MEMORY_ID_TAG);
        return createChartFunction(configuration, parameters);
    }

    private String createChartFunction(Configuration configuration, Map<String, Object> parameters) throws IOException, TemplateException {
        Template template = configuration.getTemplate(CHART_FUNCTION_TEMPLATE);
        StringWriter writer = new StringWriter();
        template.process(parameters, writer);
        return writer.toString();
    }

    private List<ChartData> mapChartData(List<BenchmarkResult> benchmarkResult, Function<BenchmarkResult, ChartData> mapper){
        return benchmarkResult.stream().map(mapper).collect(toList());
    }

    private ChartData usedHeapMemoryFootprintToChartData(BenchmarkResult benchmarkResult) {
        return statisticViewToChartData(benchmarkResult, benchmarkResult.getHeapMemoryFootprint(),
                                        MemoryFootprint::getUsedMemoryFootprint);
    }

    private ChartData committedHeapMemoryFootprintToChartData(BenchmarkResult benchmarkResult) {
        return statisticViewToChartData(benchmarkResult, benchmarkResult.getHeapMemoryFootprint(),
                                        MemoryFootprint::getCommittedMemoryFootprint);
    }

    private ChartData maxHeapMemoryFootprintToChartData(BenchmarkResult benchmarkResult) {
        return statisticViewToChartData(benchmarkResult, benchmarkResult.getHeapMemoryFootprint(),
                                        MemoryFootprint::getMaxMemoryFootprint);
    }

    private ChartData usedNonHeapMemoryFootprintToChartData(BenchmarkResult benchmarkResult) {
        return statisticViewToChartData(benchmarkResult, benchmarkResult.getNonHeapMemoryFootprint(),
                                        MemoryFootprint::getUsedMemoryFootprint);
    }

    private ChartData committedNonHeapMemoryFootprintToChartData(BenchmarkResult benchmarkResult) {
        return statisticViewToChartData(benchmarkResult, benchmarkResult.getHeapMemoryFootprint(),
                                        MemoryFootprint::getCommittedMemoryFootprint);
    }

    private ChartData maxNonHeapMemoryFootprintToChartData(BenchmarkResult benchmarkResult) {
        return statisticViewToChartData(benchmarkResult, benchmarkResult.getHeapMemoryFootprint(),
                                        MemoryFootprint::getMaxMemoryFootprint);
    }

    private <T> ChartData statisticViewToChartData(BenchmarkResult benchmarkResult, StatisticView<T> statisticView, AverageValueExtractor<T> valueExtractor) {
        String title = benchmarkResult.getBenchmarkMethod().getName();
        long value = valueExtractor.extract(statisticView.getAverageValue());
        String convertedValue = options.getMemoryValueConverter().convert(value);
        convertedValue = convertedValue.substring(0, convertedValue.length() - 2);
        return new ChartData(title, convertedValue);
    }

    @FunctionalInterface
    private static interface AverageValueExtractor<T> {
        long extract(T value);
    }
}
