package com.meylism.sparser.core.operator.utf;

import org.junit.Test;

import static org.junit.Assert.*;

public class UTF8KeyValueMatchFilterTest {

  @Test
  public void test() {
    String record = "{\"VendorID\": 2, \"tpep_pickup_datetime\": 1613510635000000, \"tpep_dropoff_datetime\": "
        + "1613512283000000, \"passenger_count\": 2.0, \"trip_distance\": 12.02, \"RatecodeID\": 1.0, \"store_and_fwd_flag\": \"N\", \"PULocationID\": 132, \"DOLocationID\": 2, \"payment_type\": 1, \"fare_amount\": 37.0, \"extra\": 0.5, \"mta_tax\": 0.5, \"tip_amount\": 11.0, \"tolls_amount\":         5.0, \"improvement_surcharge\": 0.3, \"total_amount\": 54.3, \"congestion_surcharge\": 0.0, \"airport_fee\": null}";

    String record2 = "{\"VendorID\": 2, \"tpep_pickup_datetime\": 1613509210000000, \"tpep_dropoff_datetime\": "
        + "1613509445000000, \"passenger_count\": 1.0, \"trip_distance\": 0.68, \"RatecodeID\": 1.0, \"store_and_fwd_flag\": \"N\", \"PULocationID\": 170, \"DOLocationID\": 233, \"payment_type\": 1, \"fare_amount\": 4.5, \"extra\": 0.5, \"mta_tax\": 0.5, \"tip_amount\": 1.66, \"tolls_amount\":         0.0, \"improvement_surcharge\": 0.3, \"total_amount\": 9.96, \"congestion_surcharge\": 2.5, \"airport_fee\": null}";
    UTF8KeyValueMatchFilter ky = new UTF8KeyValueMatchFilter("DOLocationID", "2", null);
    boolean res = ky.evaluate(record);
    boolean res2 = ky.evaluate(record2);
    assertEquals(res, true);
    assertEquals(res2, false);
  }
}