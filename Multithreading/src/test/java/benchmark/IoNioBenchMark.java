package benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class IoNioBenchMark {

    @Param({
            "C:\\Users\\kgromov\\Desktop\\nds-compilier-stuff\\Stubbles_Gateways.txt",
            "C:\\Users\\kgromov\\Desktop\\nds-compilier-stuff\\Conditions_in_Routing.docx",
            "C:\\Users\\kgromov\\Desktop\\nds-compilier-stuff\\RDF Reference Manual v2018.Q1.pdf",

    })
    private String filePath;

    @Benchmark
    public void bufferedFileReader_JDK_1_7(Blackhole bh) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                bh.consume(strCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void scannerAndFile_JDK_1_7_8(Blackhole bh) {
        File file = new File(filePath);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                bh.consume(sc.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void filesBufferedReader_JDK_1_8(Blackhole bh) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))){
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                bh.consume(strCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void filesReadAll_JDK_1_8(Blackhole bh) {
        try {
            List<String> allLines =  Files.readAllLines(Paths.get(filePath));
            bh.consume(allLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
