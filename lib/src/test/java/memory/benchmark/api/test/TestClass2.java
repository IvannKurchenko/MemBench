package memory.benchmark.api.test;

import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.annotations.After;

import static org.mockito.Mockito.mock;

public class TestClass2 {

    public static final TestClass mockedTestClass = mock(TestClass.class);

    @Before
    public void setUp(){
        mockedTestClass.setUp();
    }

    @Benchmark
    public void benchmark(){
        mockedTestClass.benchmark();
    }

    @After
    public void tearDown(){
        mockedTestClass.tearDown();
    }
}
