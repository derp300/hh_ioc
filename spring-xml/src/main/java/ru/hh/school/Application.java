package ru.hh.school;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@State(Scope.Thread)
public class Application {

    ApplicationContext context;
    AreaService service;

    @Setup(Level.Trial)
    public void init() {
        context = new ClassPathXmlApplicationContext("beans-common.xml", "beans-prod.xml");
        service = context.getBean(AreaService.class);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void contextStartCold() {
        new ClassPathXmlApplicationContext("beans-common.xml", "beans-prod.xml");
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void contextStartHot() {
        new ClassPathXmlApplicationContext("beans-common.xml", "beans-prod.xml");
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getBeanCold() {
        context.getBean(AreaService.class);
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void getBeanHot() {
        context.getBean(AreaService.class);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Application.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }
}
