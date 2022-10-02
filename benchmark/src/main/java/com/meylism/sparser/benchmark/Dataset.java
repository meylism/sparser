package com.meylism.sparser.benchmark;

public enum Dataset {
  GHARCHIVE() {
    @Override public String getAbsolutePathAsString() {
      return "/Users/meylismatiyev/Downloads/data-gharchive/gharchive.json";
    }

    @Override public String getDatasetAsString() {
      return "gharhive";
    }
  };

  public abstract String getAbsolutePathAsString();
  public abstract String getDatasetAsString();
}
