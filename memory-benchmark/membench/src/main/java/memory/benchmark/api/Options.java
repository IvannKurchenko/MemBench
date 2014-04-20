package memory.benchmark.api;

public class Options {

    private final int testCount;

    private Options(Builder builder) {
        this.testCount = builder.testCount;
    }

    public int getTestCount() {
        return testCount;
    }

    public static class Builder {

        private int testCount;

        public Builder testCount(int testCount) {
            this.testCount = testCount;
            return this;
        }

        public Options build(){
            return new Options(this);
        }
    }
}
