package memory.benchmark.internal.collect;

import java.util.List;

public class StatisticCollector {

    //TODO : rewrite to functional style!
    public static <T> Statistic getStatistic(List<T> afterValueItems, List<T> beforeValueItems, ValueExtractor<T> valueExtractor) {
        long max = 0, min = 0;
        double sum = 0;

        for (int i = 0; i < afterValueItems.size(); i++) {
            long beforeValue = valueExtractor.getValue(beforeValueItems.get(i));
            long afterValue = valueExtractor.getValue(afterValueItems.get(i));
            long value = afterValue - beforeValue;

            if(i > 0 ) {
                if (value < min) {
                    min = value;
                }

                if (value > max) {
                    min = value;
                }
            }   else {

                max = value;
                min = value;
            }

            sum += value;
        }
        return new Statistic(max, min, Math.round(sum / afterValueItems.size()));
    }

    @FunctionalInterface
    public interface ValueExtractor<T> {
        long getValue(T o);
    }

    public static class Statistic {
        public final long max, min, average;

        public Statistic(long max, long min, long average) {
            this.max = max;
            this.min = min;
            this.average = average;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Statistic statistic = (Statistic) o;

            if (average != statistic.average) return false;
            if (max != statistic.max) return false;
            if (min != statistic.min) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (max ^ (max >>> 32));
            result = 31 * result + (int) (min ^ (min >>> 32));
            result = 31 * result + (int) (average ^ (average >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "Statistic{" +
                    "max=" + max +
                    ", min=" + min +
                    ", average=" + average +
                    '}';
        }
    }
}
