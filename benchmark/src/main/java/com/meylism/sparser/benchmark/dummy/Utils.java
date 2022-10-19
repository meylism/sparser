package com.meylism.sparser.benchmark.dummy;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

  public static ArrayList<String> loadJson(final String path) throws IOException {
    File file = new File(path);
    Scanner scanner;
    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(file.getAbsolutePath() + " doesn't exist");
    }
    return readStrings(scanner);
  }

  private static ArrayList<String> readStrings(Scanner scanner) throws IOException {
    ArrayList<String> strings = new ArrayList<>();

    while (scanner.hasNext()){
      strings.add(scanner.nextLine());
    }
    return strings;
  }
}
