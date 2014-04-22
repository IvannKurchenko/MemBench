package memory.benchmark.api.result;

import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;

/**
 *
 */
public class MemoryPoolFootprint extends MemoryFootprint {

    private final String poolName;
    private final MemoryType memoryType;

    public MemoryPoolFootprint(MemoryUsage before, MemoryUsage after, String poolName, MemoryType memoryType) {
        super(before, after);
        this.poolName = poolName;
        this.memoryType = memoryType;
    }

    public MemoryPoolFootprint(long usedMemoryFootprint, long maxMemoryFootprint, long committedMemoryFootprint,String poolName, MemoryType memoryType) {
        super(usedMemoryFootprint, maxMemoryFootprint, committedMemoryFootprint);
        this.poolName = poolName;
        this.memoryType = memoryType;
    }

    /**
     *
     * @return
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     *
     * @return
     */
    public MemoryType getMemoryType() {
        return memoryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MemoryPoolFootprint that = (MemoryPoolFootprint) o;

        if (memoryType != that.memoryType) return false;
        if (poolName != null ? !poolName.equals(that.poolName) : that.poolName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (poolName != null ? poolName.hashCode() : 0);
        result = 31 * result + (memoryType != null ? memoryType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MemoryPoolFootprint{" +
                "poolName='" + poolName + '\'' +
                ", memoryType=" + memoryType +
                '}';
    }
}
