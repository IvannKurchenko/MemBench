package memory.benchmark.internal.util;

public interface Factory<T,A> {

    default T create(A argument){
        throw new UnsupportedOperationException();
    }

    default T create(){
        throw new UnsupportedOperationException();
    }
}
