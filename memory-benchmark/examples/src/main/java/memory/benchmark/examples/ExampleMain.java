package memory.benchmark.examples;

import memory.benchmark.api.Options;
import memory.benchmark.api.Runner;
import memory.benchmark.api.report.StringReportFormatter;
import memory.benchmark.api.report.ReportFormatter;
import memory.benchmark.api.result.Result;
import memory.benchmark.api.util.MemoryValueConverter;

import java.util.List;

public class ExampleMain {

    public static void main(String... args){
        ReportFormatter<String> formatter = new StringReportFormatter(MemoryValueConverter.TO_MEGA_BYTES);
        Options options = new Options.Builder().build();
        List<Result> resultList = Runner.run(options, ListMemoryTest.class);
        System.out.println(formatter.formatReport(resultList));
    }
}
