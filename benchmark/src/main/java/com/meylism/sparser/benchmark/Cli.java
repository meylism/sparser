package com.meylism.sparser.benchmark;

import org.apache.commons.cli.*;

public class Cli {
  private final Options options;
  private final String[] args;

  public Cli(String[] args) {
    this.options = initOptions();
    this.args = args;
  }

  public CommandLine getCli() {
    CommandLine cli = null;

    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println("Error while parsing arguments - " + e.getMessage());
      printHelp();
      System.exit(1);
    }

    return cli;
  }

  private Options initOptions() {
    Option query = Option.builder(BenchmarkOptions.QUERY.shortName())
        .longOpt(BenchmarkOptions.QUERY.longName())
        .hasArgs()
        .desc("Names of the queries to be benchmarked")
        .build();

    Option dataset = Option.builder(BenchmarkOptions.DATASET.shortName())
        .longOpt(BenchmarkOptions.DATASET.longName())
        .hasArg()
        .desc("Path to the dataset to be used to benchmark the query")
        .build();

    Option help = Option.builder(BenchmarkOptions.HELP.shortName())
        .longOpt(BenchmarkOptions.HELP.longName())
        .desc("Ask for help")
        .build();

    Option list = Option.builder(BenchmarkOptions.LIST.shortName())
        .longOpt(BenchmarkOptions.LIST.longName())
        .desc("List available queries")
        .build();

    Options options = new Options();
    options.addOption(query);
    options.addOption(dataset);
    options.addOption(help);
    options.addOption(list);

    return options;
  }

  public void printHelp() {
    new HelpFormatter().printHelp("java -jar <jar>", options, true);
  }
}
