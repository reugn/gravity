package com.github.reugn.gravity.bench;

import com.github.reugn.gravity.bench.state.Before;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import com.github.reugn.gravity.matcher.BMHMatcher;
import com.github.reugn.gravity.matcher.ContainsMatcher;
import com.github.reugn.gravity.matcher.Matcher;
import com.github.reugn.gravity.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * ./gradlew jmh
 *
 * Benchmark                                         Mode  Cnt  Score   Error   Units
 * MatcherBenchmark.bmhMatcherBenchmark             thrpt       4.580          ops/ms
 * MatcherBenchmark.containsMatcherBenchmark        thrpt       5.910          ops/ms
 * MatcherBenchmark.stringBMHMatcherBenchmark       thrpt       5.446          ops/ms
 * MatcherBenchmark.stringContainsMatcherBenchmark  thrpt       5.968          ops/ms
 */
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
public class MatcherBenchmark {

    @Benchmark
    public void containsMatcherBenchmark(Before b) throws IOException {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Matcher m = new ContainsMatcher();
        m.match(b.pattern, is, 8);
    }

    @Benchmark
    public void bmhMatcherBenchmark(Before b) throws IOException {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Matcher m = new BMHMatcher();
        m.match(b.pattern, is, 8);
    }

    @Benchmark
    public void stringContainsMatcherBenchmark(Before b) {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Matcher m = new ContainsMatcher();
        m.match(b.pattern, Utils.toStr(is));
    }

    @Benchmark
    public void stringBMHMatcherBenchmark(Before b) {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Matcher m = new BMHMatcher();
        m.match(b.pattern, Utils.toStr(is));
    }
}
