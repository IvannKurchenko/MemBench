MemoryBenchmark
========
[![Build Status](https://travis-ci.org/IvannKurchenko/MemoryBenchmark.svg?branch=master)](https://travis-ci.org/IvannKurchenko/MemoryBenchmark)

Overview
========
MemoryBenchmark - it's simple library that provide possibility to write unit-like memory benchmark tests. 

How it works
========
All memory measurments library performs using memory related MXBeans : 
* MemoryMXBean
* MemoryPoolMXBean
* GarbageCollectorMXBean



Library supports two mode of running tests :
* Same process
* Separate proceess

Benchmark lifecycle
========
There is three main annotation that describe benchmark class :
* Before (optional)
* Benchmark (mandatory)
* After (optional)


Example
========
Simple memory benchmark test looks in the next way :
```
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
```
To run benchmark tests :
```
public static void main(String... args) {
  BenchmarkOptions options = new Builder().build();
  MemoryBenchmarkRunner.run(options, SimpleBenchmarkTest.class);
}
```
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
