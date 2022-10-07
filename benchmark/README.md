# Benchmarks for Sparser

This is a separate project to facilitate benchmarking custom queries to see how they do with & without Sparser in place.

## Adding a Query

A query is expected to implement [QueryDescription](./src/main/java/com/meylism/sparser/benchmark/QueryDescription.java)
interface, which is essentially a query representation in this project. Pay attention to the comments in the example 
query below.

```java
// This annotation is necessary to be able to discover queries at run-time.
@AutoService(QueryDescription.class)
public class ExampleQuery implements QueryDescription {
  @Override public String getName() {
    // Queries are identified by their names and are therefore expected to be unique across the project.
    // You will get warned if queries share a name together.
    return "example";
  }

  @Override public String getQueryDescription() {
    return "WHERE text = 'Putin' OR text = 'Biden'"; // or any other description with words
  }

  @Override public Predicate getQuery() {
    Or<SimplePredicate> or = new Or<>();

    SimplePredicate putinMatch = new ExactMatchPredicate(new PredicateKey("text"), "Putin");
    SimplePredicate bidenMatch = new ExactMatchPredicate(new PredicateKey("text"), "Biden");

    or.add(putinMatch);
    or.add(bidenMatch);

    return or;
  }

  @Override public FileFormat getFileFormat() {
    return FileFormat.JSON;
  }

  @Override public Reader getReader(File file) {
    // going to fetch the provided dataset to benchmarks
    return new JsonReader(file);
  }

  @Override public Deserializer getDeserializer() {
    return new JacksonDeserializer();
  }
}
```

## Running Benchmarks

Running `mvn package` inside the project yields an uber-JAR with a name `benchmark.jar`. Ask for help(by running 
`java -jar target/benchmark.jar --help`) or refer to the synopsys below:

```
usage: java -jar <jar> [-d <arg>] [-h] [-l] [-q <arg>]
 -d,--dataset <arg>   Path to the dataset to be used to benchmark the
                      query
 -h,--help            Ask for help
 -l,--list            List available queries
 -q,--query <arg>     The name of the query to be benchmarked
```

For example, to run the query above:

`java -jar target/benchmark.jar --query example --dataset path-to-your-dataset`

## Getting Insight into Benchmarks

In addition to what [JMH](https://github.com/openjdk/jmh) provides with respect to benchmarking, you can do any kind 
of analysis of your interest to understand results better. (Please have a look at state objects at
[JMH samples](https://github.com/openjdk/jmh/tree/master/jmh-samples/src/main/java/org/openjdk/jmh/samples).)

As an example, here I try to get the selectivity of the given query:

```java
// An object instantiated from this class is injected into each benchmark method.
@State(Scope.Benchmark)
public class BenchmarkStatsState {
  public int recordsSoFar;
  public int filteredRecords;

  @TearDown // this fixture method runs after each benchmark. In this case it publishes my findings.
  public void publishStats() {
    System.out.println("\n ------------ STATISTICS ------------ \n");
    System.out.println("Selectivity: ");
    System.out.print((float) (recordsSoFar-filteredRecords)/(float) recordsSoFar);
  }
}
```

```java
@Benchmark
  public void sparser(BenchmarkState state, BenchmarkStatsState statsState, Blackhole blackhole) throws Exception {
    for (String record : state.getReader().read()) {
      if (state.getSparser().filter(record)) {
        statsState.filteredRecords++;
      } else {
        blackhole.consume(state.getDeserializer().deserialize(record));
      }
      statsState.recordsSoFar++;
    }
  }
```



