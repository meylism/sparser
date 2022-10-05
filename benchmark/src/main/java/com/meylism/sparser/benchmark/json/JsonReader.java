package com.meylism.sparser.benchmark.json;

import com.meylism.sparser.benchmark.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonReader extends Reader {
  private Scanner scanner;
  private List<String> lines;

  public JsonReader(File file) {
    super(file);
    try {
      this.scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(file.getAbsolutePath() + " doesn't exist");
    }
    lines = new ArrayList<>();
    populateList();
  }

  private void populateList() {
    while (scanner.hasNext()) {
      lines.add(scanner.nextLine());
    }
  }

  @Override public List<String> read() {
    return lines;
  }
}
