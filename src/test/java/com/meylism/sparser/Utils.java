package com.meylism.sparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Utils {
    public static ArrayList<String> loadJson(final String resourceName) throws
        IOException {
      final InputStream stream = Utils.class.getResourceAsStream("/" + resourceName);
      return readFromInputStream(stream);
    }

    private static ArrayList<String> readFromInputStream(InputStream inputStream)
        throws IOException {
      ArrayList<String> lines = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = br.readLine()) != null) {
          lines.add(line);
        }
      }
      return lines;
    }
}
