package memory.benchmark.api;

import memory.benchmark.api.util.MemoryValueConverter;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public class Options {



    /**
     * Enum that represents information included to report.
     */
    public enum ReportInformation {

        /**
         * Includes information about heap memory usage.
         * @see  memory.benchmark.api.result.MemoryFootprint
         */
        HEAP_MEMORY_FOOTPRINT,

        /**
         * Includes information about non heap memory usage.
         * @see  memory.benchmark.api.result.MemoryFootprint
         */
        NON_HEAP_MEMORY_FOOTPRINT,

        /**
         * Includes information about heap memory polls usage.
         * @see  memory.benchmark.api.result.MemoryPoolFootprint
         */
        HEAP_MEMORY_POOL_FOOTPRINT,

        /**
         * Includes information about non heap memory polls usage.
         * @see  memory.benchmark.api.result.MemoryPoolFootprint
         */
        NON_HEAP_MEMORY_POOL_FOOTPRINT,

        /**
         * Includes information about garbage collector usage.
         * @see  memory.benchmark.api.result.GcUsage
         */
        GC_USAGE;
    }


    /**
     *
     */
    public enum RunMode{

        /**
         *
         */
        SAME_PROCESS,

        /**
         *
         */
        SEPARATE_PROCESS;
    }
    private final Set<ReportInformation> reportInformation;

    private final MemoryValueConverter memoryValueConverter;
    private final RunMode runMode;
    private Options(Builder builder) {
        this.reportInformation = builder.reportInformation;
        this.memoryValueConverter = builder.memoryValueConverter;
        this.runMode = builder.runMode;
    }

    public Set<ReportInformation> getReportInformation() {
        return reportInformation;
    }

    public MemoryValueConverter getMemoryValueConverter() {
        return memoryValueConverter;
    }

    public RunMode getRunMode() {
        return runMode;
    }

    /**
     *
     */
    public static class Builder {

        private Set<ReportInformation> reportInformation;
        private MemoryValueConverter memoryValueConverter;
        private RunMode runMode;

        public Builder() {
            reportInformation = EnumSet.allOf(ReportInformation.class);
        }

        /**
         *
         */
        public Builder reportInformation(ReportInformation first, ReportInformation... reportInformation) {
            requireNonNull(first);
            requireNonNull(reportInformation);
            this.reportInformation = EnumSet.of(first, reportInformation);
            return this;
        }

        /**
         *
         * @param memoryValueConverter
         * @return
         */
        public Builder memoryValueConverter(MemoryValueConverter memoryValueConverter) {
            this.memoryValueConverter = memoryValueConverter;
            return this;
        }

        /**
         *
         * @param runMode
         * @return
         */
        public Builder runMode(RunMode runMode) {
            this.runMode = runMode;
            return this;
        }

        public Options build() {
            return new Options(this);
        }
    }
}
