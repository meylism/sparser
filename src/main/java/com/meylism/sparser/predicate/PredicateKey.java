package com.meylism.sparser.predicate;

import lombok.Getter;

import java.util.List;

/**
 * Representation of a key in a SQL query.
 *
 * For example:
 * CREATE TABLE imaginaryTable (employee struct<name:struct<firstname:string,lastname:string>, age int, ...>, ...);
 * SELECT employee.name.firstname FROM imaginaryTable;
 *
 * Then:
 * tableName : imaginaryTable
 * columnName: employee
 * fieldNames: name,firstname (in the order of access)
 */
public class PredicateKey {
  @Getter  private String tableName;
  @Getter private String columnName;
  @Getter private List<String> fieldNames;

  public PredicateKey(String tableName, String columnName, List<String> fieldNames) {
    this.tableName = tableName;
    this.columnName = columnName;
    this.fieldNames = fieldNames;
  }

  public PredicateKey(String columnName, List<String> fieldNames) {
    this(null, columnName, fieldNames);
  }

  public PredicateKey (String columnName) {
    this(null, columnName, null);
  }

  private Boolean hasFieldNames() {
    if (fieldNames == null) return false;
    else if (fieldNames.size() == 0) return false;
    return true;
  }
}
