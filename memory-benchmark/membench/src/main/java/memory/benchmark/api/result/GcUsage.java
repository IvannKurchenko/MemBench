package memory.benchmark.api.result;

public class GcUsage {

    private final String name;
    private final long gcCount;
    private final long gcTime;

    public GcUsage(String name, long gcCount, long gcTime) {
        this.name = name;
        this.gcCount = gcCount;
        this.gcTime = gcTime;
    }

    public String getName() {
        return name;
    }

    public long getGcCount() {
        return gcCount;
    }

    public long getGcTime() {
        return gcTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GcUsage gcUsage = (GcUsage) o;

        if (gcCount != gcUsage.gcCount) return false;
        if (gcTime != gcUsage.gcTime) return false;
        if (name != null ? !name.equals(gcUsage.name) : gcUsage.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (gcCount ^ (gcCount >>> 32));
        result = 31 * result + (int) (gcTime ^ (gcTime >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "GcUsage{" +
                "name='" + name + '\'' +
                ", gcCount=" + gcCount +
                ", gcTime=" + gcTime +
                '}';
    }
}
