package com.meylism.sparser;

import lombok.*;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class Query {
  private ArrayList<String> rawFilters;

  public Query() {
    rawFilters = new ArrayList<>();
  }

  public void add(String rf) {
    rawFilters.add(rf);
  }
}
