package memory.benchmark.examples;

import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListMemoryTest {

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
