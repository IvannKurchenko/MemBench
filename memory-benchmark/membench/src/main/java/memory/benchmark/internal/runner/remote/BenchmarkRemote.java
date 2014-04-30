package memory.benchmark.internal.runner.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BenchmarkRemote extends Remote {

    void invoke(String benchmarkMethod) throws RemoteException;
}
