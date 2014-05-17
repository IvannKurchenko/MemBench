package memory.benchmark.examples;

import memory.benchmark.api.Options;
import memory.benchmark.api.Runner;
import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.util.MemoryValueConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static memory.benchmark.api.Options.Builder;

public class ListMemoryTest {

    public static void main(String... args) {
        Options options = new Builder().
                reportInformation(Options.ReportInformation.HEAP_MEMORY_FOOTPRINT, Options.ReportInformation.GC_USAGE).
                memoryValueConverter(MemoryValueConverter.TO_MEGA_BYTES).
                runMode(Options.RunMode.SEPARATE_PROCESS).
                reportFormat(Options.ReportFormat.HTML).
                build();

        Runner.run(options, ListMemoryTest.class);
    }

    private static final int TEST_DATA_COUNT = 1_000_000;

    private List<Integer> arrayList;
    private List<Integer> linkedList;

    @Before
    public void setUp() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
    }

    @Benchmark(testTimes = 4)
    public void arrayList() {
        fillList(arrayList);
    }

    @Benchmark(testTimes = 4)
    public void linkedList() {
        fillList(linkedList);
    }

    @After
    public void tearDown() {
        arrayList = null;
        linkedList = null;
    }

    private void fillList(List<Integer> list) {
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            list.add(i);
        }
    }
}
