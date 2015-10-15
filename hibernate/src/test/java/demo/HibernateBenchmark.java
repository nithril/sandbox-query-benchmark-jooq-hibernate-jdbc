package demo;

import java.util.Collection;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import demo.domain.AuthorWithBooks;
import demo.service.AuthorQueries;

/**
 * Created by nlabrot on 10/10/15.
 */


public class HibernateBenchmark {

	@State(Scope.Benchmark)
	public static class BenchmarkSpringState {

		private ConfigurableApplicationContext configurableApplicationContext;
		private AuthorQueries authorQueries;

		@Setup(Level.Trial)
		public void init() {
			configurableApplicationContext = SpringApplication.run(Application.class);
			authorQueries = configurableApplicationContext.getBean(AuthorQueries.class);
		}
		public AuthorQueries getAuthorQueries() {
			return authorQueries;
		}
	}


	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksUsingCriteria(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksUsingCriteria();
	}


	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksUsingNamedQuery(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksUsingNamedQuery();
	}

	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksUsingSpringData(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksUsingSpringData();
	}


	public static void main(String[] args) throws RunnerException {

		Options opt = new OptionsBuilder()
				.include(HibernateBenchmark.class.getSimpleName())
				.warmupIterations(5)
				.warmupTime(TimeValue.seconds(5))
				.measurementIterations(5)
				.measurementTime(TimeValue.seconds(5))
				.threads(1)
				.forks(1)
				.jvmArgs("-Xmx1g")
				.build();
		new Runner(opt).run();
	}
}