package memory.benchmark.examples;

import memory.benchmark.api.Options;
import memory.benchmark.api.Runner;
import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.report.ReportFormatter;
import memory.benchmark.api.report.StringReportFormatter;
import memory.benchmark.api.result.Result;
import memory.benchmark.api.util.MemoryValueConverter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListMemoryTest {

    public static void main(String... args){
        ReportFormatter<String> formatter = new StringReportFormatter(MemoryValueConverter.TO_MEGA_BYTES);
        Options options = new Options.Builder().build();
        List<Result> resultList = Runner.run(options, ListMemoryTest.class);
        System.out.println(formatter.formatReport(options, resultList));
    }

    private static final int TEST_DATA_COUNT = 1_000_000;

    private final ArrayList<Integer> data = new ArrayList<>();
    private List<Integer> arrayList;
    private List<Integer> linkedList;

    public ListMemoryTest() {
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            data.add(i);
        }
        data.trimToSize();
    }

    @Before
    public void setUp() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
    }

    @Benchmark
    public void addArrayList() {
        data.forEach(arrayList::add);
    }

    @Benchmark
    public void addLinkedList() {
        data.forEach(linkedList::add);
    }

    @After
    public void tearDown() {
        arrayList = null;
        linkedList = null;
    }
}
