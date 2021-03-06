package memory.benchmark.internal.report.html;

import freemarker.template.*;
import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.api.result.*;
import memory.benchmark.internal.report.BenchmarkReportFormatter;
import memory.benchmark.internal.util.Log;

import java.io.*;
import java.lang.management.MemoryType;
import java.util.*;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static memory.benchmark.internal.util.ThrowableHandlers.printThrowableAction;

public class HtmlBenchmarkReportFormatter implements BenchmarkReportFormatter {

    private static final String CHARTS_TEMPLATE_FILE = "report_template.ftl";
    private static final String CHART_FUNCTION_TEMPLATE_FILE = "chart_func_template.ftl";
    private static final String REPORT_FILE_NAME = "report.html";

    private static final String CHART_NAME_KEY = "chartName";
    private static final String MEMORY_VALUE_KEY = "memoryValue";
    private static final String CHART_TITLE_KEY = "title";

    private static final String CHART_DATA_TAG = "chartData";
    private static final String CHART_IDENTIFIER_KEY = "chartIdentifier";
    private static final String CHART_FUNCTIONS_KEY = "chartFunctions";
    private static final String REPORT_SECTIONS_KEY = "reportSections";

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

    private static final String USED_PREFIX = "Used";
    private static final String COMMITTED_POOL_PREFIX = "Committed";
    private static final String MAX_POOL_PREFIX = "Max";
    private static final String TIME_GC_PREFIX = "Time";

    private final BenchmarkOptions options;
    private final Log log;

    private Configuration configuration;
    private List<String> chartFunctions;
    private List<ReportSection> reportSections;
    private List<BenchmarkResult> benchmarkResults;

    public HtmlBenchmarkReportFormatter(BenchmarkOptions options, Log log) {
        this.options = options;
        this.log = log;
    }

    @Override
    public void formatReport(List<BenchmarkResult> benchmarkResults) {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(HtmlBenchmarkReportFormatter.class, "");
        configuration.setIncompatibleImprovements(new Version(2, 3, 20));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Optional<Writer> fileWriterOptional = Optional.empty();

        try {

            Template template = configuration.getTemplate(CHARTS_TEMPLATE_FILE);
            File reportFile = new File(REPORT_FILE_NAME);
            Writer fileWriter = new FileWriter(reportFile);
            fileWriterOptional = Optional.of(fileWriter);
            Map<String, Object> templateParameters = createTemplateParameters(benchmarkResults);
            template.process(templateParameters, fileWriter);

            log.log("Report file : " + reportFile.getAbsoluteFile().getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileWriterOptional.ifPresent(w -> printThrowableAction(w::close));
        }
    }

    private Map<String, Object> createTemplateParameters(List<BenchmarkResult> benchmarkResults) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        Map<Class, List<BenchmarkResult>> mappedBenchmarkResults = mapBenchmarksResult(benchmarkResults);

        chartFunctions = new ArrayList<>();
        reportSections = new ArrayList<>();
        this.benchmarkResults = benchmarkResults;// temp crunch!

        addMemoryUsage();
        addMemoryPool();
        addGcGcUsage();

        parameters.put(CHART_FUNCTIONS_KEY, chartFunctions);
        parameters.put(REPORT_SECTIONS_KEY, reportSections);

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

    private void addMemoryUsage() throws Exception {
        if(options.getReportInformation().contains(BenchmarkOptions.ReportInformation.HEAP_MEMORY_FOOTPRINT)) {
            addHeapMemoryUsage();
        }

        if(options.getReportInformation().contains(BenchmarkOptions.ReportInformation.NON_HEAP_MEMORY_FOOTPRINT)) {
            addNonHeapMemoryUsage();
        }
    }

    private void addHeapMemoryUsage() throws Exception {
        chartFunctions.add(createChartFunction(USED_HEAP_MEMORY_ID_TAG, USED_HEAP_MEMORY_TITLE_TAG,
                benchmarkResults, this::usedHeapMemoryFootprintToChartData));
        reportSections.add(createHeapSection(USED_HEAP_MEMORY_TITLE_TAG, USED_HEAP_MEMORY_ID_TAG,
                MemoryFootprint::getUsedMemoryFootprint));

        chartFunctions.add(createChartFunction(COMMITTED_HEAP_MEMORY_ID_TAG, COMMITTED_HEAP_MEMORY_TITLE_TAG,
                benchmarkResults, this::committedHeapMemoryFootprintToChartData));
        reportSections.add(createHeapSection(COMMITTED_HEAP_MEMORY_TITLE_TAG, COMMITTED_HEAP_MEMORY_ID_TAG,
                MemoryFootprint::getCommittedMemoryFootprint));

        chartFunctions.add(createChartFunction(MAX_HEAP_MEMORY_ID_TAG, MAX_HEAP_MEMORY_TITLE_TAG,
                benchmarkResults, this::maxHeapMemoryFootprintToChartData));
        reportSections.add(createHeapSection(MAX_HEAP_MEMORY_TITLE_TAG, MAX_HEAP_MEMORY_ID_TAG,
                MemoryFootprint::getMaxMemoryFootprint));
    }

    private ReportSection createHeapSection(String title, String chartIdentifier, Function<MemoryFootprint, Long> converter) {
        List<ReportSectionTable> tables = createSectionTables(BenchmarkResult::getHeapMemoryFootprint, converter);
        return createSection(title, chartIdentifier, tables);
    }

    private void addNonHeapMemoryUsage() throws Exception {
        chartFunctions.add(createChartFunction(USED_NON_HEAP_MEMORY_ID_TAG, USED_NON_HEAP_MEMORY_TITLE_TAG,
                benchmarkResults, this::usedNonHeapMemoryFootprintToChartData));
        reportSections.add(createNonHeapSection(USED_NON_HEAP_MEMORY_TITLE_TAG, USED_NON_HEAP_MEMORY_ID_TAG,
                MemoryFootprint::getUsedMemoryFootprint));

        chartFunctions.add(createChartFunction(COMMITTED_NON_HEAP_MEMORY_ID_TAG, COMMITTED_NON_HEAP_MEMORY_TITLE_TAG,
                benchmarkResults, this::committedNonHeapMemoryFootprintToChartData));
        reportSections.add(createNonHeapSection(COMMITTED_NON_HEAP_MEMORY_TITLE_TAG, COMMITTED_NON_HEAP_MEMORY_ID_TAG,
                MemoryFootprint::getCommittedMemoryFootprint));

        chartFunctions.add(createChartFunction(MAX_NON_HEAP_MEMORY_ID_TAG, MAX_NON_HEAP_MEMORY_TITLE_TAG,
                benchmarkResults, this::maxNonHeapMemoryFootprintToChartData));
        reportSections.add(createNonHeapSection(MAX_NON_HEAP_MEMORY_TITLE_TAG, MAX_NON_HEAP_MEMORY_ID_TAG,
                MemoryFootprint::getMaxMemoryFootprint));
    }

    private ReportSection createNonHeapSection(String title, String chartIdentifier, Function<MemoryFootprint, Long> converter) {
        List<ReportSectionTable> tables = createSectionTables(BenchmarkResult::getNonHeapMemoryFootprint, converter);
        return createSection(title, chartIdentifier, tables);
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

    private void addMemoryPool() throws Exception {
        if(options.getReportInformation().contains(BenchmarkOptions.ReportInformation.HEAP_MEMORY_POOL_FOOTPRINT)) {
            addMemoryPool(MemoryType.HEAP);
        }

        if(options.getReportInformation().contains(BenchmarkOptions.ReportInformation.NON_HEAP_MEMORY_POOL_FOOTPRINT)) {
            addMemoryPool(MemoryType.NON_HEAP);
        }
    }

    private void addMemoryPool(MemoryType type) throws Exception {
        addMemoryPoolFootprint(type, USED_PREFIX, MemoryFootprint::getUsedMemoryFootprint);
        addMemoryPoolFootprint(type, COMMITTED_POOL_PREFIX,MemoryFootprint::getCommittedMemoryFootprint);
        addMemoryPoolFootprint(type, MAX_POOL_PREFIX, MemoryFootprint::getMaxMemoryFootprint);
    }

    private void addMemoryPoolFootprint(MemoryType type, String prefix, Function<MemoryFootprint, Long> valueExtractor) throws Exception {
        addChartData(groupMemoryPoolResultByName(type, valueExtractor), prefix, p -> createMemoryPoolSection(p, prefix,valueExtractor));
    }

    private ReportSection createMemoryPoolSection(String poolName, String prefix, Function<MemoryFootprint, Long> converter) {
        List<ReportSectionTable> tables = createSectionTables(r -> getMemoryPoolViewByName(r, poolName), converter);
        String beanName = poolName.replaceAll(" ", "_");
        String name = prefix + beanName;
        String title = prefix + " " + beanName;
        return createSection(title, name, tables);
    }

    private MemoryPoolStatisticView getMemoryPoolViewByName(BenchmarkResult result, String name) {
        return result.getMemoryPoolFootprints().stream().filter(v -> v.getName().equals(name)).collect(toList()).get(0);
    }

    private Map<String, List<ChartData>> groupMemoryPoolResultByName(MemoryType memoryType, Function<MemoryFootprint, Long> valueExtractor) {
        return groupResultsByName(r -> filterMemoryPoolFootprintsByType(r, memoryType), MemoryPoolStatisticView::getName, valueExtractor);
    }

    private List<MemoryPoolStatisticView> filterMemoryPoolFootprintsByType(BenchmarkResult result, MemoryType memoryType) {
        return result.getMemoryPoolFootprints().stream().filter(r -> r.getMemoryType() == memoryType).collect(toList());
    }

    private void addGcGcUsage() throws Exception {
        if(options.getReportInformation().contains(BenchmarkOptions.ReportInformation.GC_USAGE)){
            addGcUsage(TIME_GC_PREFIX, GcUsage::getGcTime);
        }
    }

    private void addGcUsage(String prefix, Function<GcUsage, Long> valueExtractor) throws Exception {
        addChartData(groupGcUsageResultByName(valueExtractor), prefix,
                     p -> createGcUsageSection(p, prefix,valueExtractor));
    }

    private ReportSection createGcUsageSection(String poolName, String prefix, Function<GcUsage, Long> converter) {
        List<ReportSectionTable> tables = createSectionTables(r -> getGcUsageViewByName(r, poolName), converter);
        String beanName = poolName.replaceAll(" ", "_");
        String name = prefix + beanName;
        String title = prefix + " " + beanName;
        return createSection(title, name, tables);
    }

    private GcUsageStatisticView getGcUsageViewByName(BenchmarkResult result, String name) {
        return result.getGcUsages().stream().filter(v -> v.getGcName().equals(name)).collect(toList()).get(0);
    }

    private Map<String, List<ChartData>> groupGcUsageResultByName(Function<GcUsage, Long> valueExtractor) {
        return groupResultsByName(BenchmarkResult::getGcUsages, GcUsageStatisticView::getGcName, valueExtractor, l -> Long.toString(l));
    }

    private <V, T extends StatisticView<V>> Map<String, List<ChartData>> groupResultsByName(Function<BenchmarkResult, List<T>> viewsExtractor,
                                                                                            Function<T, String> nameExtractor,
                                                                                            Function<V, Long> valueExtractor) {
        return groupResultsByName(viewsExtractor, nameExtractor, valueExtractor, options.getMemoryValueConverter()::convert);
    }

    private <V, T extends StatisticView<V>> Map<String, List<ChartData>> groupResultsByName(Function<BenchmarkResult, List<T>> viewsExtractor,
                                                                                            Function<T, String> nameExtractor,
                                                                                            Function<V, Long> valueExtractor,
                                                                                            Function<Long, String> converter) {
        Map<String, List<ChartData>> results = new HashMap<>();
        for(BenchmarkResult result : benchmarkResults) {
            List<T> statisticViews = viewsExtractor.apply(result);
            for(T statisticView : statisticViews) {
                String name = nameExtractor.apply(statisticView);
                List<ChartData> views = results.get(name);
                if(views == null) {
                    views = new ArrayList<>();
                    results.put(name, views);
                }
                views.add(statisticViewToChartData(result, statisticView, valueExtractor, converter));
            }
        }
        return results;
    }

    private void addChartData(Map<String, List<ChartData>> chartData, String prefix, Function<String, ReportSection> mapper) throws Exception {
        for(Map.Entry<String, List<ChartData>> data : chartData.entrySet()) {
            String beanName = data.getKey().replaceAll(" ", "_");
            String name = prefix + beanName;
            String title = prefix + " " + beanName;
            String chart = createChartFunction(name, title, data.getValue(), identity());
            ReportSection reportSection = mapper.apply(data.getKey());
            reportSections.add(reportSection);
            chartFunctions.add(chart);
        }
    }

    private <T> String createChartFunction(String name, String title, List<T> results, Function<T, ChartData> mapper) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CHART_NAME_KEY, name);
        parameters.put(MEMORY_VALUE_KEY, options.getMemoryValueConverter().getSuffix());
        parameters.put(CHART_TITLE_KEY, title);
        parameters.put(CHART_DATA_TAG, mapChartData(results, mapper));
        parameters.put(CHART_IDENTIFIER_KEY, name);
        Template template = configuration.getTemplate(CHART_FUNCTION_TEMPLATE_FILE);
        StringWriter writer = new StringWriter();
        template.process(parameters, writer);
        return writer.toString();
    }

    private <T> List<ChartData> mapChartData(List<T> benchmarkResult, Function<T, ChartData> mapper){
        return benchmarkResult.stream().map(mapper).collect(toList());
    }

    private <T> ChartData statisticViewToChartData(BenchmarkResult benchmarkResult, StatisticView<T> statisticView,
                                                   Function<T,Long> valueExtractor) {
        return statisticViewToChartData(benchmarkResult, statisticView, valueExtractor,
                                        options.getMemoryValueConverter()::convert);
    }

    private <T> ChartData statisticViewToChartData(BenchmarkResult benchmarkResult, StatisticView<T> statisticView,
                                                   Function<T,Long> valueExtractor, Function<Long, String> converter) {

        String title = benchmarkResult.getBenchmarkMethod().getName();
        long value = valueExtractor.apply(statisticView.getAverageValue());
        String convertedValue = converter.apply(value);
        convertedValue = convertedValue.substring(0, convertedValue.length());
        return new ChartData(title, convertedValue);
    }

    private ReportSection createSection(String reportSectionName, String chartIdentifier, List<ReportSectionTable> reportSectionTables) {
        String suffix = options.getMemoryValueConverter().getSuffix();
        return new ReportSection(reportSectionName, chartIdentifier, suffix, reportSectionTables);
    }

    private <T> List<ReportSectionTable> createSectionTables(Function<BenchmarkResult, StatisticView<T>> mapper,
                                                             Function<T, Long> converter) {
        return benchmarkResults.stream().map(r-> createSectionTable(r, mapper, converter)).collect(toList());
    }

    private <T> ReportSectionTable createSectionTable(BenchmarkResult result,
                                                      Function<BenchmarkResult, StatisticView<T>> mapper,
                                                      Function<T, Long> converter) {

        return createSectionTable(result, mapper, converter, options.getMemoryValueConverter()::convert);
    }

    private <T> ReportSectionTable createSectionTable(BenchmarkResult result,
                                                      Function<BenchmarkResult, StatisticView<T>> mapper,
                                                      Function<T, Long> converter,
                                                      Function<Long, String> valueConverter) {

        String benchmark = result.getBenchmarkMethod().getName();
        StatisticView<T> statisticView = mapper.apply(result);
        String average = converter.andThen(valueConverter).apply(statisticView.getAverageValue());
        String minimum = converter.andThen(valueConverter).apply(statisticView.getMinimumValue());
        String maximum = converter.andThen(valueConverter).apply(statisticView.getMaximumValue());
        return new ReportSectionTable(benchmark, average, minimum, maximum);
    }
}
