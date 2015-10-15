# sandbox-query-benchmark-jooq-hibernate-jdbc
Quicky: Query Benchmark Using jOOQ / Hibernate / JDBC

[Source](https://github.com/nithril/sandbox-query-benchmark-jooq-hibernate-jdbc/tree/article-quicky-query-benchmark-jooq-hibernate-jdbc)

## How to Create the DB

```
#> mvn clean install -DskipTests=true
#> db/mvn liquibase:update
```

The DB will be created at the project root directory.

## Benchmark

[Hibernate Benchmark](hibernate/src/test/java/demo/HibernateBenchmark.java)

[jOOQ / Hibernate Benchmark](application/src/test/java/demo/SqlBenchmark.java)

Note: Gatling was not used.