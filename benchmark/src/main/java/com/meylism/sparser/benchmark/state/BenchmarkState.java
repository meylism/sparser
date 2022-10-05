package com.meylism.sparser.benchmark.state;

import com.google.common.base.Preconditions;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.Reader;
import com.meylism.sparser.core.Sparser;
import com.meylism.sparser.core.deserializer.Deserializer;
import lombok.Getter;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

/**
 * A state class that encompasses all needed by benchmark
 */
@State(Scope.Benchmark)
public class BenchmarkState {
  private static final ServiceLoader<QueryDescription> loader =  ServiceLoader.load(QueryDescription.class);

  @Getter private Reader reader;
  @Getter private Sparser sparser;
  @Getter private Deserializer deserializer;

  // jmh will inject queries automatically
  @Param({})
  @Getter private String query;

  @Param({})
  @Getter private String dataset;

  @Setup
  public void setup()
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    QueryDescription query = getQueryByName(this.query);
    Preconditions.checkNotNull(query, "query cannot be null");

    this.deserializer = query.getDeserializer();
    this.sparser = new Sparser(query.getQuery(), query.getFileFormat(), deserializer);

    File file = new File(dataset);
    this.reader = query.getReader(file);
  }

  @TearDown
  public void finish() {

  }

  private QueryDescription getQueryByName(String name) {
    for(QueryDescription query : loader) {
      if (query.getName().equals(name))
        return query;
    }
    return null;
  }
}
