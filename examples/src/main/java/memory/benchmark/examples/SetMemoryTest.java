package memory.benchmark.examples;

import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.api.MemoryBenchmarkRunner;
import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.util.MemoryValueConverter;

import java.util.*;

public class SetMemoryTest {

    public static void main(String... args) {
        BenchmarkOptions options = new BenchmarkOptions.Builder().memoryValueConverter(MemoryValueConverter.TO_MEGA_BYTES).build();
        MemoryBenchmarkRunner.run(options, SetMemoryTest.class);
    }

    private static final int TEST_DATA_COUNT = 1_000_000;

    private Set<Integer> hashSet;
    private Set<Integer> treeSet;
    private Integer[] arrayInteger;
    private int[] arrayInt;

    @Before
    public void setUp() {
        hashSet = new HashSet<>();
        treeSet = new TreeSet<>();
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
    public void hashSet() {
        fillSet(hashSet);
    }

    @Benchmark(testTimes = 6)
    public void treeSet() {
        fillSet(treeSet);
    }

    @After
    public void tearDown() {
        arrayInteger = null;
        arrayInt = null;
        hashSet = null;
        treeSet = null;
    }

    private void fillSet(Set<Integer> set) {
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            set.add(i);
        }
    }
}
