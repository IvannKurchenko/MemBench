MemoryBenchmark
========
[![Build Status](https://travis-ci.org/IvannKurchenko/MemoryBenchmark.svg?branch=master)](https://travis-ci.org/IvannKurchenko/MemoryBenchmark)

Overview
========
MemoryBenchmark - it's simple library that provide possibility to write unit-like memory benchmark tests.

How it works
========
Benchmark lifecycle consist of three phases, which describes with next annotations :
* @Before (optional) - initialization method.
* @Benchmark (mandatory) - code under the benchmark test.
* @After (optional) - cleaning method

This cycle library executes before each benchmark running.
Result of memory benchmark test execution based on difference of memory consumption between before @Benchmark annotated 
method invocation and after it.
All memory measurement library performs using memory related MXBeans : 
* [MemoryMXBean] (http://docs.oracle.com/javase/7/docs/api/java/lang/management/MemoryMXBean.html)
* [MemoryPoolMXBean] (http://docs.oracle.com/javase/7/docs/api/java/lang/management/MemoryPoolMXBean.html)
* [GarbageCollectorMXBean] (http://docs.oracle.com/javase/7/docs/api/java/lang/management/GarbageCollectorMXBean.html)

There is two test running modes :
* Same process - creating and running benchmark test object in same with library process .
* Separate process (recommended) - creating and running benchmark test object in separate process. Library run separate process where 
  instantiate new benchmark test object and communicates with it though RMI. Separate process emulates 'sandbox' and 
  allows to run test in 'clean' environment.


Example
========
Simple memory benchmark test looks in the next way :
<pre><code>
public class SimpleBenchmarkTest {
    @Before
    public void setUp() {
      Initialization...
    }

    @Benchmark(testTimes = 2)
    public void benchmark() {
        Test...
    }

    @After
    public void tearDown() {
      Clear....
    }
}
</code></pre>
To run benchmark tests :
<pre><code>
public static void main(String... args) {
  BenchmarkOptions options = new Builder().build();
  MemoryBenchmarkRunner.run(options, SimpleBenchmarkTest.class);
}
</code></pre>
See more [here](https://github.com/IvannKurchenko/MemoryBenchmark/tree/master/examples/src/main/java/memory/benchmark/examples)

JavaDoc
========

Maven
========

Used third party code
========
* [Mockito](http://code.google.com/p/mockito/)
* [Freemarker](http://freemarker.org/)
* [Google Charts](https://developers.google.com/chart/?hl=uk)
* [Bootstrap](http://getbootstrap.com/)
