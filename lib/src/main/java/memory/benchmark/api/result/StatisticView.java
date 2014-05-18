package memory.benchmark.api.result;

/**
 * Represents holder that contains main measured statistic results of benchmark :
 * minimum, maximum and average values of related benchmark.
 */
public class StatisticView<T> {

    private final T minimumValue;
    private final T maximumValue;
    private final T averageValue;
    private final T singleValue;

    public StatisticView(T singleValue) {
        this.minimumValue = null;
        this.maximumValue = null;
        this.averageValue = null;
        this.singleValue = singleValue;
    }

    public StatisticView(T minimumValue, T maximumValue, T averageValue) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.averageValue = averageValue;
        this.singleValue = null;
    }

    /**
     * @return minimum value.
     */
    public T getMinimumValue() {
        return minimumValue;
    }

    /**
     * @return maximum value.
     */
    public T getMaximumValue() {
        return maximumValue;
    }

    /**
     * @return average value
     */
    public T getAverageValue() {
        return averageValue;
    }

    /**
     * @return single statistic value
     */
    public T getSingleValue() {
        return singleValue;
    }

    /**
     * @return 'true' if contains just one value, 'false' otherwise.
     */
    public boolean containsSingleValue() {
        return singleValue != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticView that = (StatisticView) o;

        if (averageValue != null ? !averageValue.equals(that.averageValue) : that.averageValue != null) return false;
        if (maximumValue != null ? !maximumValue.equals(that.maximumValue) : that.maximumValue != null) return false;
        if (minimumValue != null ? !minimumValue.equals(that.minimumValue) : that.minimumValue != null) return false;
        if (singleValue != null ? !singleValue.equals(that.singleValue) : that.singleValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = minimumValue != null ? minimumValue.hashCode() : 0;
        result = 31 * result + (maximumValue != null ? maximumValue.hashCode() : 0);
        result = 31 * result + (averageValue != null ? averageValue.hashCode() : 0);
        result = 31 * result + (singleValue != null ? singleValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatisticView{" +
                "minimumValue=" + minimumValue +
                ", maximumValue=" + maximumValue +
                ", averageValue=" + averageValue +
                ", singleValue=" + singleValue +
                '}';
    }
}
