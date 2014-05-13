package memory.benchmark.examples;

import memory.benchmark.api.Options;
import memory.benchmark.api.Runner;
import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.util.MemoryValueConverter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static memory.benchmark.api.Options.Builder;

public class ListMemoryTest {

    public static void main(String... args) {
        Options options = new Builder().
                reportInformation(Options.ReportInformation.HEAP_MEMORY_POOL_FOOTPRINT).
                memoryValueConverter(MemoryValueConverter.TO_MEGA_BYTES).
                runMode(Options.RunMode.SEPARATE_PROCESS).
                reportFormat(Options.ReportFormat.HTML).
                build();

        Runner.run(options, ListMemoryTest.class);
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

    @Benchmark(testTimes = 4)
    public void addArrayList() {
        data.forEach(arrayList::add);
    }

    @Benchmark(testTimes = 4)
    public void addLinkedList() {
        data.forEach(linkedList::add);
    }

    @After
    public void tearDown() {
        arrayList = null;
        linkedList = null;
    }
}
