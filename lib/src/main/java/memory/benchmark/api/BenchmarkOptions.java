package memory.benchmark.api;

import memory.benchmark.api.util.MemoryValueConverter;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public class BenchmarkOptions {

    /**
     * Enum that represents information included to report.
     */
    public enum ReportInformation {

        /**
         * Includes information about heap memory usage.
         *
         * @see memory.benchmark.api.result.MemoryFootprint
         */
        HEAP_MEMORY_FOOTPRINT,

        /**
         * Includes information about non heap memory usage.
         *
         * @see memory.benchmark.api.result.MemoryFootprint
         */
        NON_HEAP_MEMORY_FOOTPRINT,

        /**
         * Includes information about heap memory polls usage.
         *
         * @see memory.benchmark.api.result.MemoryPoolStatisticView
         */
        HEAP_MEMORY_POOL_FOOTPRINT,

        /**
         * Includes information about non heap memory polls usage.
         *
         * @see memory.benchmark.api.result.MemoryPoolStatisticView
         */
        NON_HEAP_MEMORY_POOL_FOOTPRINT,

        /**
         * Includes information about garbage collector usage.
         *
         * @see memory.benchmark.api.result.GcUsage
         */
        GC_USAGE
    }


    /**
     * Enum that represents way to run benchmark tests.
     */
    public enum RunMode {

        /**
         * Run benchmarks in same java process.
         */
        SAME_PROCESS,

        /**
         * Run benchmarks in separate java process.
         */
        SEPARATE_PROCESS
    }


    /**
     *
     */
    public enum ReportFormat {
        /**
         *
         */
        CONSOLE,

        /**
         *
         */
        HTML
    }

    private final Set<ReportInformation> reportInformation;
    private final MemoryValueConverter memoryValueConverter;
    private final RunMode runMode;
    private final TimeUnit gcTimeUnit;
    private final long gcTime;
    private final int remotePort;
    private final int mxBeanRemotePort;
    private final boolean allowedInternalLogging;
    private final ReportFormat reportFormat;
    private final String[] virtualMachineArguments;

    private BenchmarkOptions(Builder builder) {
        this.reportInformation = builder.reportInformation;
        this.memoryValueConverter = builder.memoryValueConverter;
        this.runMode = builder.runMode;
        this.gcTime = builder.gcTime;
        this.gcTimeUnit = builder.gcTimeUnit;
        this.remotePort = builder.remotePort;
        this.mxBeanRemotePort = builder.mxBeanRemotePort;
        this.allowedInternalLogging = builder.allowedInternalLogging;
        this.reportFormat = builder.reportFormat;
        this.virtualMachineArguments = builder.virtualMachineArguments;
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

    public TimeUnit getGcTimeUnit() {
        return gcTimeUnit;
    }

    public long getGcTime() {
        return gcTime;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public int getMxBeanRemotePort() {
        return mxBeanRemotePort;
    }

    public boolean isAllowedInternalLogging() {
        return allowedInternalLogging;
    }

    public ReportFormat getReportFormat() {
        return reportFormat;
    }

    public String[] getVirtualMachineArguments() {
        return virtualMachineArguments;
    }

    /**
     *
     */
    public static class Builder {

        private static final long DEFAULT_GC_TIME = 1;
        private static final TimeUnit DEFAULT_GC_TIME_UNIT = TimeUnit.SECONDS;
        private static final int DEFAULT_REMOTE_PORT = 10000;
        private static final int DEFAULT_MX_BEAN_REMOTE_PORT = 10001;
        private static final ReportFormat DEFAULT_REPORT_FORMAT  = ReportFormat.CONSOLE;

        private Set<ReportInformation> reportInformation;
        private MemoryValueConverter memoryValueConverter;
        private RunMode runMode;
        private TimeUnit gcTimeUnit;
        private long gcTime;
        private int remotePort;
        private int mxBeanRemotePort;
        private boolean allowedInternalLogging;
        private ReportFormat reportFormat;
        private String[] virtualMachineArguments;

        public Builder() {
            reportInformation = EnumSet.allOf(ReportInformation.class);
            memoryValueConverter = MemoryValueConverter.TO_KILO_BYTES;
            runMode = RunMode.SEPARATE_PROCESS;
            gcTimeUnit = DEFAULT_GC_TIME_UNIT;
            gcTime = DEFAULT_GC_TIME;
            remotePort = DEFAULT_REMOTE_PORT;
            mxBeanRemotePort = DEFAULT_MX_BEAN_REMOTE_PORT;
            allowedInternalLogging = true;
            reportFormat = DEFAULT_REPORT_FORMAT;
            virtualMachineArguments = new String[0];
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
         * @param memoryValueConverter
         * @return
         */
        public Builder memoryValueConverter(MemoryValueConverter memoryValueConverter) {
            this.memoryValueConverter = memoryValueConverter;
            return this;
        }

        /**
         * @param runMode
         * @return
         */
        public Builder runMode(RunMode runMode) {
            this.runMode = runMode;
            return this;
        }

        /**
         * @param gcTimeUnit
         * @param gcTime
         * @return
         */
        public Builder gcTimeUnit(TimeUnit gcTimeUnit, long gcTime) {
            this.gcTimeUnit = gcTimeUnit;
            this.gcTime = gcTime;
            return this;
        }

        public Builder remotePort(int remotePort) {
            this.remotePort = remotePort;
            return this;
        }

        /**
         * @param mxBeanRemotePort
         * @return
         */
        public Builder mxBeanRemotePort(int mxBeanRemotePort) {
            this.mxBeanRemotePort = mxBeanRemotePort;
            return this;
        }

        /**
         *
         * @param reportFormat
         * @return
         */
        public Builder reportFormat(ReportFormat reportFormat) {
            this.reportFormat = reportFormat;
            return this;
        }

        /**
         *
         * @param virtualMachineArguments
         * @return
         */
        public Builder virtualMachineArguments(String... virtualMachineArguments) {
            this.virtualMachineArguments = virtualMachineArguments;
            return this;
        }

        public BenchmarkOptions build() {
            return new BenchmarkOptions(this);
        }
    }
}
