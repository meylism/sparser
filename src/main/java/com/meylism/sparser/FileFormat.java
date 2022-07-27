package com.meylism.sparser;

import com.meylism.sparser.deserializer.Deserializer;
import com.meylism.sparser.deserializer.JacksonDeserializer;

public enum FileFormat {
  JSON {
    @Override
    public Deserializer getDefaultDeserializer() {
      return new JacksonDeserializer();
    }
  };

  /**
   * Deserializer to be used while calibrating.
   *
   * @return Deserializer
   */
  public Deserializer getDefaultDeserializer() {return null;}
}
