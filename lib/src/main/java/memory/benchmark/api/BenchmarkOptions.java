package memory.benchmark.api;

import memory.benchmark.api.util.MemoryValueConverter;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * Represents general options for running of benchmark classes.
 * Include additional information such as logging, report formatting etc.
 * Cannot be instantiated directly, use {@link memory.benchmark.api.BenchmarkOptions.Builder} instead.
 */
public class BenchmarkOptions {

    /**
     * Enum that represents collected data that will be included to benchmark report.
     */
    public enum ReportInformation {

        /**
         * Includes collected about heap memory usage.
         *
         * @see memory.benchmark.api.result.MemoryFootprint
         * @see java.lang.management.MemoryMXBean#getHeapMemoryUsage()
         */
        HEAP_MEMORY_FOOTPRINT,

        /**
         * Includes information about non heap memory usage.
         *
         * @see memory.benchmark.api.result.MemoryFootprint
         * @see java.lang.management.MemoryMXBean#getNonHeapMemoryUsage()
         */
        NON_HEAP_MEMORY_FOOTPRINT,

        /**
         * Includes information about heap memory pools usage.
         *
         * @see memory.benchmark.api.result.MemoryPoolStatisticView
         * @see java.lang.management.MemoryPoolMXBean
         */
        HEAP_MEMORY_POOL_FOOTPRINT,

        /**
         * Includes information about non heap memory polls usage.
         *
         * @see memory.benchmark.api.result.MemoryPoolStatisticView
         * @see java.lang.management.MemoryPoolMXBean
         */
        NON_HEAP_MEMORY_POOL_FOOTPRINT,

        /**
         * Includes information about garbage collector usage.
         *
         * @see memory.benchmark.api.result.GcUsage
         * @see java.lang.management.GarbageCollectorMXBean
         */
        GC_USAGE
    }


    /**
     * Enum that represents way of benchmark instance running.
     * This operation includes : instantiating of benchmark object and invocation of benchmark methods.
     * Recommended and default {@link memory.benchmark.api.BenchmarkOptions.RunMode} is
     * {@link memory.benchmark.api.BenchmarkOptions.RunMode#SEPARATE_PROCESS}.
     */
    public enum RunMode {

        /**
         * Indicates execution of benchmark class in same process.
         */
        SAME_PROCESS,

        /**
         * Indicates execution of benchmark class in separated java process, which run from current process.
         * Communication between and separate process executes using RMI.
         * Separate process allows to emulate 'sandbox' for each benchmark execution, prevent possible memory leaks
         * from untrusted benchmark code etc.
         */
        SEPARATE_PROCESS
    }


    /**
     * Indicates format of final benchmark report.
     */
    public enum ReportFormat {

        /**
         * Print benchmark to {@link java.lang.System#out} stream.
         */
        CONSOLE,

        /**
         * Creates HTML benchmark report with chart and tables.
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
     * Build immutable instance of {@link memory.benchmark.api.BenchmarkOptions}
     */
    public static class Builder {

        private static final long DEFAULT_GC_TIME = 1;
        private static final TimeUnit DEFAULT_GC_TIME_UNIT = TimeUnit.SECONDS;
        private static final int DEFAULT_REMOTE_PORT = 10000;
        private static final int DEFAULT_MX_BEAN_REMOTE_PORT = 10001;
        private static final ReportFormat DEFAULT_REPORT_FORMAT  = ReportFormat.HTML;

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
            reportInformation = EnumSet.of(ReportInformation.HEAP_MEMORY_FOOTPRINT);
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
         * Set report information for final benchmark report.
         * By default it's {@link memory.benchmark.api.BenchmarkOptions.ReportInformation#HEAP_MEMORY_FOOTPRINT}.
         *
         * @throws java.lang.NullPointerException if arguments is 'null'
         * @return 'this' builder instance.
         */
        public Builder reportInformation(ReportInformation first, ReportInformation... reportInformation) {
            requireNonNull(first);
            requireNonNull(reportInformation);
            this.reportInformation = EnumSet.of(first, reportInformation);
            return this;
        }

        /**
         * Set {@link memory.benchmark.api.util.MemoryValueConverter} for benchmark report.
         * By default it's {@link memory.benchmark.api.util.MemoryValueConverter#TO_KILO_BYTES}
         *
         * @param memoryValueConverter converter for benchmark report.
         * @throws java.lang.NullPointerException if argument is 'null'
         * @return 'this' builder instance.
         */
        public Builder memoryValueConverter(MemoryValueConverter memoryValueConverter) {
            requireNonNull(memoryValueConverter);
            this.memoryValueConverter = memoryValueConverter;
            return this;
        }

        /**
         * Set {@link memory.benchmark.api.BenchmarkOptions.RunMode} of benchmark executions.
         * By default it's {@link memory.benchmark.api.BenchmarkOptions.RunMode#SEPARATE_PROCESS}
         *
         * @param runMode mode of benchmark executions.
         * @throws java.lang.NullPointerException if argument is 'null'
         * @return 'this' builder instance.
         */
        public Builder runMode(RunMode runMode) {
            requireNonNull(runMode);
            this.runMode = runMode;
            return this;
        }

        /**
         * After each invocation of any benchmark class method, library trying to run garbage collection
         * execution using {@link System#gc()}. After this invocation current thread blocks on 'gcTime' time.
         * This invocation executes to get as much clean memory consumption data as it possible.
         * Set 'gcTime' lower than '0' if you don't want to invoke {@link System#gc()}.
         * By default it's {@link java.util.concurrent.TimeUnit#SECONDS}, 1.
         *
         * @param gcTimeUnit time unit of execution thread blocking.
         * @param gcTime time value of execution thread blocking.
         * @throws java.lang.NullPointerException if 'gcTimeUnit' is 'null'
         * @return 'this' builder instance.
         */
        public Builder gcTimeUnit(TimeUnit gcTimeUnit, long gcTime) {
            requireNonNull(gcTimeUnit);
            this.gcTimeUnit = gcTimeUnit;
            this.gcTime = gcTime;
            return this;
        }

        /**
         * Remote port used for RMI server to register accessing for remote benchmark object.
         * Used for {@link memory.benchmark.api.BenchmarkOptions.RunMode#SEPARATE_PROCESS}
         * By default it's 10000.
         *
         * @see memory.benchmark.api.BenchmarkOptions.RunMode#SEPARATE_PROCESS
         * @param remotePort for benchmark RMI server.
         * @throws java.lang.IllegalArgumentException if 'remotePort' lower than 0.
         * @return 'this' builder instance.
         */
        public Builder benchmarkRemotePort(int remotePort) {
            checkValidArgument(remotePort > 0, "Remote port cannot be lower than 0");
            this.remotePort = remotePort;
            return this;
        }

        /**
         * Remote port used for RMI to register {@link javax.management.MBeanServer}.
         * Used for {@link memory.benchmark.api.BenchmarkOptions.RunMode#SEPARATE_PROCESS}.
         * By default it's 10001.
         *
         * @see memory.benchmark.api.BenchmarkOptions.RunMode#SEPARATE_PROCESS
         * @param mxBeanRemotePort for benchmark {@link javax.management.MBeanServer} server.
         * @throws java.lang.IllegalArgumentException if 'remotePort' lower than 0.
         * @return 'this' builder instance.
         */
        public Builder mxBeanRemotePort(int mxBeanRemotePort) {
            this.mxBeanRemotePort = mxBeanRemotePort;
            return this;
        }

        /**
         * Set {@link memory.benchmark.api.BenchmarkOptions.ReportFormat}
         * By default it's {@link memory.benchmark.api.BenchmarkOptions.ReportFormat#HTML}
         *
         * @param reportFormat format of final benchmark report.
         * @throws java.lang.NullPointerException if reportFormat is 'null'.
         * @return 'this' builder instance.
         */
        public Builder reportFormat(ReportFormat reportFormat) {
            requireNonNull(reportFormat);
            this.reportFormat = reportFormat;
            return this;
        }

        /**
         * Set additional arguments for virtual machine which used to run separate process with benchmark.
         * Used for {@link memory.benchmark.api.BenchmarkOptions.RunMode#SEPARATE_PROCESS}.
         *
         * @param virtualMachineArguments VM arguments for separate benchmark process.
         * @throws java.lang.NullPointerException if virtualMachineArguments is 'null'.
         * @return 'this' builder instance.
         */
        public Builder virtualMachineArguments(String... virtualMachineArguments) {
            requireNonNull(virtualMachineArguments);
            checkValidArgument(virtualMachineArguments.length > 0, "Arguments should be not empty");
            this.virtualMachineArguments = virtualMachineArguments;
            return this;
        }

        /**
         * Creates immutable instance of {@link memory.benchmark.api.BenchmarkOptions}.
         * @return new instance of options.
         */
        public BenchmarkOptions build() {
            return new BenchmarkOptions(this);
        }

        /**
         *
         * @param allowedInternalLogging
         * @return
         */
        public Builder allowedInternalLogging(boolean allowedInternalLogging){
            this.allowedInternalLogging = allowedInternalLogging;
            return this;
        }

        private static void checkValidArgument(boolean validCondition, String errorMessage) {
            if(!validCondition) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }
}
