package com.meylism.sparser.benchmark;

import com.google.common.base.Preconditions;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

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
      String query = cli.getOptionValue(BenchmarkOptions.QUERY.shortName());
      String dataset  = cli.getOptionValue(BenchmarkOptions.DATASET.shortName());
      if (!new File(dataset).exists()) {
        throw new RuntimeException("Dataset " + dataset + " doesn't exist");
      }

      validateQuery(query);
      new Benchmark().bench(query, dataset);
    }
  }

  private static Map<String, QueryDescription> populateQueryDefinitions() {
    Map<String, QueryDescription> queryDefinitions = new HashMap<>();

    for (QueryDescription qd : loader) {
      queryDefinitions.put(qd.getName(), qd);
    }

    return queryDefinitions;
  }

  /**
   * Ensure that requested queries actually exist.
   */
  private static void validateQuery(String query) {
    if (queryDefinitions.get(query) == null) {
      String sb = "The following query does not exist:\n" + query + "\n";
      throw new RuntimeException(sb);
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
