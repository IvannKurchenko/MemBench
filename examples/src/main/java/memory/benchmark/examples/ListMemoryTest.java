package memory.benchmark.examples;

import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.api.MemoryBenchmarkRunner;
import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.util.MemoryValueConverter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static memory.benchmark.api.BenchmarkOptions.Builder;

public class ListMemoryTest {

    public static void main(String... args) {
        BenchmarkOptions options = new Builder().
                reportInformation(BenchmarkOptions.ReportInformation.HEAP_MEMORY_FOOTPRINT).
                memoryValueConverter(MemoryValueConverter.TO_MEGA_BYTES).
                runMode(BenchmarkOptions.RunMode.SEPARATE_PROCESS).
                reportFormat(BenchmarkOptions.ReportFormat.HTML).
                virtualMachineArguments("-Xmx1000M").
                build();

        MemoryBenchmarkRunner.run(options, ListMemoryTest.class);
    }

    private static final int TEST_DATA_COUNT = 1_000_000;

    private List<Integer> arrayList;
    private List<Integer> linkedList;
    private Integer[] arrayInteger;
    private int[] arrayInt;

    @Before
    public void setUp() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
    }

    @Benchmark(testTimes = 6)
    public void arrayInteger() {
        arrayInteger = new Integer[TEST_DATA_COUNT];
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            arrayInteger[i] = i;
        }
    }

    @Benchmark(testTimes = 6)
    public void arrayInt() {
        arrayInt = new int[TEST_DATA_COUNT];
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            arrayInt[i] = i;
        }
    }

    @Benchmark(testTimes = 6)
    public void arrayList() {
        fillList(arrayList);
    }

    @Benchmark(testTimes = 6)
    public void linkedList() {
        fillList(linkedList);
    }

    @After
    public void tearDown() {
        arrayInteger = null;
        arrayInt = null;
        arrayList = null;
        linkedList = null;
    }

    private void fillList(List<Integer> list) {
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            list.add(i);
        }
    }
}
