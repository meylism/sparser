package com.meylism.sparser.benchmark;

public enum BenchmarkOptions {
  QUERY() {
    @Override public String shortName() {
      return "q";
    }

    @Override public String longName() {
      return "query";
    }
  },
  DATASET() {
    @Override public String shortName() {
      return "d";
    }

    @Override public String longName() {
      return "dataset";
    }
  },
  HELP {
    @Override public String shortName() {
      return "h";
    }

    @Override public String longName() {
      return "help";
    }
  },
  LIST {
    @Override public String shortName() {
      return "l";
    }

    @Override public String longName() {
      return "list";
    }
  };

  public abstract String shortName();
  public abstract String longName();
}
