package memory.benchmark.internal.runner.remote;

import memory.benchmark.api.exception.BenchmarkRunException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.mockito.Mockito.*;

public class RemoteBenchmarkMethodInvokerTest {

    private BenchmarkRemote benchmarkRemote;
    private Process remoteProcess;
    private BenchmarkProcess benchmarkProcess;
    private Optional<Method> beforeMethod;
    private List<Method> testMethods;
    private Optional<Method> afterMethod;

    @Before
    public void setUp() throws Exception {
        benchmarkRemote = mock(BenchmarkRemote.class);
        remoteProcess = mock(Process.class);
        benchmarkProcess = new BenchmarkProcess(remoteProcess, benchmarkRemote);
        beforeMethod = Optional.of(TestClass.class.getMethod("setUp"));
        testMethods = asList(TestClass.class.getMethod("benchmarkMethod"));
        afterMethod = Optional.of(TestClass.class.getMethod("tearDown"));
    }

    @Test
    public void invokeBeforeMethodPresent() throws RemoteException {
        RemoteBenchmarkMethodInvoker methodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, beforeMethod, empty(), emptyList());
        methodInvoker.invokeBefore();
        verify(benchmarkRemote).invoke("setUp");
        verify(benchmarkRemote).gc();
        verifyNoMoreInteractions(benchmarkRemote);
    }

    @Test
    public void invokeBeforeMethodNotPresent() throws RemoteException {
        RemoteBenchmarkMethodInvoker methodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, empty(), empty(), emptyList());
        methodInvoker.invokeBefore();
        verify(benchmarkRemote).gc();
        verifyNoMoreInteractions(benchmarkRemote);
    }

    @Test(expected = BenchmarkRunException.class)
    public void invokeBeforeMethodThrowsException() throws RemoteException {
        doThrow(new RuntimeException()).when(benchmarkRemote).invoke(anyString());
        RemoteBenchmarkMethodInvoker benchmarkMethodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, beforeMethod, null, null);
        benchmarkMethodInvoker.invokeBefore();
    }

    @Test
    public void invokeAfterMethodPresent() throws RemoteException {
        RemoteBenchmarkMethodInvoker methodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, empty(), afterMethod, emptyList());
        methodInvoker.invokeAfter();
        verify(benchmarkRemote).invoke("tearDown");
        verify(benchmarkRemote).gc();
        verifyNoMoreInteractions(benchmarkRemote);
    }

    @Test
    public void invokeAfterMethodNotPresent() throws RemoteException {
        RemoteBenchmarkMethodInvoker methodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, empty(), afterMethod, emptyList());
        methodInvoker.invokeAfter();
        verify(benchmarkRemote).invoke("tearDown");
        verify(benchmarkRemote).gc();
        verifyNoMoreInteractions(benchmarkRemote);
    }

    @Test(expected = BenchmarkRunException.class)
    public void invokeAfterMethodThrowsException() throws RemoteException {
        doThrow(new RuntimeException()).when(benchmarkRemote).invoke("tearDown");
        RemoteBenchmarkMethodInvoker benchmarkMethodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, empty(), afterMethod, emptyList());
        benchmarkMethodInvoker.invokeAfter();
    }

    @Test
    public void invokeBenchmark() throws RemoteException {
        RemoteBenchmarkMethodInvoker methodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, empty(), empty(), testMethods);
        methodInvoker.invokeBenchmark(testMethods.get(0));
        verify(benchmarkRemote).invoke("benchmarkMethod");
        verifyNoMoreInteractions(benchmarkRemote);
    }

    @Test(expected = BenchmarkRunException.class)
    public void invokeBenchmarkThrowsException() throws RemoteException {
        doThrow(new RuntimeException()).when(benchmarkRemote).invoke("benchmarkMethod");
        RemoteBenchmarkMethodInvoker benchmarkMethodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, empty(), empty(), testMethods);
        benchmarkMethodInvoker.invokeBenchmark(testMethods.get(0));
    }

    @Test
    public void close(){
        RemoteBenchmarkMethodInvoker benchmarkMethodInvoker = new RemoteBenchmarkMethodInvoker(benchmarkProcess, empty(), empty(), testMethods);
        benchmarkMethodInvoker.close();
        verify(remoteProcess).destroy();
        verifyNoMoreInteractions(remoteProcess);
    }

    private static class TestClass {
        @memory.benchmark.api.annotations.Before
        public void setUp(){}

        @memory.benchmark.api.annotations.Benchmark
        public void benchmarkMethod(){}

        @memory.benchmark.api.annotations.After
        public void tearDown(){}
    }
}
