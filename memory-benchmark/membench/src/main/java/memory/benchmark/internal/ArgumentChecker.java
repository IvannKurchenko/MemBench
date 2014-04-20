package memory.benchmark.internal;

import java.util.List;

class ArgumentChecker {

    static <T> List<T> checkListSize(List<T> list, int requiredSize) {
        if(list.size() > requiredSize){
            throw new IllegalArgumentException();
        }
        return list;
    }
}
