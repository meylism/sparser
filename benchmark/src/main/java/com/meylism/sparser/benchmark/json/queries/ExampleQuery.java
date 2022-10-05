package com.meylism.sparser.benchmark.json.queries;

import com.google.auto.service.AutoService;
import com.meylism.sparser.benchmark.QueryDescription;
import com.meylism.sparser.benchmark.Reader;
import com.meylism.sparser.benchmark.json.JacksonDeserializer;
import com.meylism.sparser.benchmark.json.JsonReader;
import com.meylism.sparser.core.FileFormat;
import com.meylism.sparser.core.deserializer.Deserializer;
import com.meylism.sparser.core.predicate.Predicate;
import com.meylism.sparser.core.predicate.PredicateKey;
import com.meylism.sparser.core.predicate.SimplePredicate;
import com.meylism.sparser.core.predicate.junction.Or;
import com.meylism.sparser.core.predicate.simple.ExactMatchPredicate;

import java.io.File;

// This is annotation necessary to be able to discover queries at run-time.
@AutoService(QueryDescription.class)
public class ExampleQuery implements QueryDescription {
  @Override public String getName() {
    // Queries are identified by their names and are therefore expected to be unique across the project.
    // You will get warned if queries share a name together.
    return "example";
  }

  @Override public String getQueryDescription() {
    return "WHERE text = 'Putin' OR text = 'Biden'"; // or any other description with words
  }

  @Override public Predicate getQuery() {
    Or<SimplePredicate> or = new Or<>();

    SimplePredicate putinMatch = new ExactMatchPredicate(new PredicateKey("text"), "Putin");
    SimplePredicate bidenMatch = new ExactMatchPredicate(new PredicateKey("text"), "Biden");

    or.add(putinMatch);
    or.add(bidenMatch);

    return or;
  }

  @Override public FileFormat getFileFormat() {
    return FileFormat.JSON;
  }

  @Override public Reader getReader(File file) {
    // going to fetch the provided dataset to benchmarks
    return new JsonReader(file);
  }

  @Override public Deserializer getDeserializer() {
    return new JacksonDeserializer();
  }
}
