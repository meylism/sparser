package com.meylism.sparser.benchmark;

import com.google.common.base.Preconditions;
import com.meylism.sparser.core.Sparser;
import com.meylism.sparser.core.deserializer.Deserializer;
import lombok.Getter;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

@State(Scope.Benchmark)
public class BenchmarkState {
  private static final ServiceLoader<QueryDescription> loader =  ServiceLoader.load(QueryDescription.class);
  @Getter private Reader reader;
  @Getter private Sparser sparser;
  @Getter private Deserializer deserializer;

  @Param({"trial"})
  @Getter private String query;

  @Setup
  public void setup()
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    QueryDescription query = getQueryByName(this.query);
    Preconditions.checkNotNull(query, "query");

    this.deserializer = query.getDeserializer().newInstance();
    this.sparser = new Sparser(query.getQuery(), query.getFileFormat(), deserializer);

    File file = new File(query.getDataset().getAbsolutePathAsString());
    Constructor<?> constructorForReader = query.getReader().getDeclaredConstructor(file.getClass());
    constructorForReader.setAccessible(true);
    Reader reader = (Reader) constructorForReader.newInstance(new Object[]{file});
    this.reader = reader;
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
