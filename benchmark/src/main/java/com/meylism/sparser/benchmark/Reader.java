package com.meylism.sparser.benchmark;

import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public abstract class Reader {
  @Getter private File file;

  protected Reader(File file) {
    this.file = file;
  }

  public abstract List<String> read();
}
