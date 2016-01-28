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
public class SqlBenchmark {

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
/*

	@Benchmark
	public Object reference(BenchmarkSpringState state) {
		return state.getAuthorQueries().reference();
	}

	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksJooqStreamedGroupBy(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksJooqStreamedGroupBy();
	}


	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksJdbcStreamed(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksJdbcStreamed();
	}


	@Benchmark
	public Object findAuthorsWithBooksJooqOldFashionGroupBy(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksJooqOldFashionGroupBy();
	}



	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksJdbc(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksJdbc();
	}

	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksJooqIntoGroup(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksJooqIntoGroup();
	}


	@Benchmark
	public Collection<AuthorWithBooks> findAuthorsWithBooksLazyGroupBy(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksJooqFetchLazyOldFashionGroupBy();
	}*/


	@Benchmark
	public Object findAuthorsWithBooksJooqOldFashionGroupBy(BenchmarkSpringState state) {
		return state.getAuthorQueries().findAuthorsWithBooksJooqOldFashionGroupBy();
	}


	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(SqlBenchmark.class.getSimpleName())
				.warmupIterations(3)
				.warmupTime(TimeValue.seconds(3))
				.measurementIterations(3)
				.measurementTime(TimeValue.seconds(3))
				.threads(1)
				.jvmArgs("-Xmx1g"
						,"-XX:MaxInlineSize=100"
						,"-XX:MaxRecursiveInlineLevel=3"
						, "-XX:MaxInlineLevel=18")
				.forks(1)
				.build();
		new Runner(opt).run();
	}


}