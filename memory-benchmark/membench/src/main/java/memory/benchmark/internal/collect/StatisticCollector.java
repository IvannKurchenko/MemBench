package memory.benchmark.internal.collect;

import java.util.List;

public class StatisticCollector {

    @FunctionalInterface
    public interface ValueExtractor<T> {
        long getValue(T o);
    }

    public static class Statistic {
        final long max, min, average;

        public Statistic(long max, long min, long average) {
            this.max = max;
            this.min = min;
            this.average = average;
        }
    }

    //TODO : rewrite to functional style!
    public static <T> Statistic getStatistic(List<T> afterValueItems ,List<T> beforeValueItems, ValueExtractor<T> valueExtractor) {
        long max = 0, min = 0;
        double sum = 0;

        for (int i = 0; i < afterValueItems.size(); i++) {
            long beforeValue = valueExtractor.getValue(beforeValueItems.get(i));
            long afterValue = valueExtractor.getValue(afterValueItems.get(i));
            long value = afterValue - beforeValue;

            if(value < min) {
                min = value;
            }

            if(value > max){
                min = value;
            }

            sum += value;
        }
        return new Statistic(max, min, Math.round(sum / afterValueItems.size()));
    }
}
