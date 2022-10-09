package com.meylism.sparser.benchmark;

import com.google.common.base.Preconditions;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.util.*;

public class Driver {
  private static final ServiceLoader<QueryDescription> loader =  ServiceLoader.load(QueryDescription.class);
  private static final Map<String, QueryDescription> queryDefinitions = populateQueryDefinitions();

  public static void main(String[] args) throws Exception {
    Cli cliHandler = new Cli(args);
    CommandLine cli = cliHandler.getCli();
    Preconditions.checkNotNull(cli);

    if (cli.hasOption(BenchmarkOptions.HELP.shortName())) {
      cliHandler.printHelp();
      return;
    }

    if (cli.hasOption(BenchmarkOptions.LIST.shortName())) {
      listQueries();
      return;
    }

    if (cli.hasOption(BenchmarkOptions.QUERY.shortName())) {
      if (!cli.hasOption(BenchmarkOptions.DATASET.shortName())) {
        throw new RuntimeException("Query is provided but dataset is not");
      }
      String[] queries = cli.getOptionValues(BenchmarkOptions.QUERY.shortName());
      String dataset  = cli.getOptionValue(BenchmarkOptions.DATASET.shortName());
      if (!new File(dataset).exists()) {
        throw new RuntimeException("Dataset " + dataset + " doesn't exist");
      }

      validateQueries(queries);
      new Benchmark().bench(queries, dataset);
    }
  }

  private static Map<String, QueryDescription> populateQueryDefinitions() {
    Map<String, QueryDescription> queryDefinitions = new HashMap<>();

    for (QueryDescription qd : loader) {
      if (queryDefinitions.containsKey(qd.getName())) {
        throw new RuntimeException("Duplicate definition of the following query has bee found: " + qd.getName());
      }
      queryDefinitions.put(qd.getName(), qd);
    }

    return queryDefinitions;
  }

  /**
   * Ensure that requested queries actually exist.
   */
  private static void validateQueries(String[] queries) {
    List<String> notFound = new ArrayList<>();
    for (String query : queries) {
      if (queryDefinitions.get(query) == null)
        notFound.add(query);
    }

    if (notFound.size() > 0) {
      StringBuilder sb = new StringBuilder();
      sb.append("The following queries do not exist:\n");
      for (String notFoundQuery : notFound)
        sb.append(notFoundQuery + '\n');
      throw new RuntimeException(sb.toString());
    }
  }

  private static void listQueries() {
    System.out.println("-------- QUERIES --------");
    for(String queryName : queryDefinitions.keySet()){
      QueryDescription selected = queryDefinitions.get(queryName);
      System.out.println(queryName + " - " + selected.getQueryDescription());
    }
  }
}
