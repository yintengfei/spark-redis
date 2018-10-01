package com.redislabs.provider.redis.df

import com.redislabs.provider.redis.df.Person.{TableNamePrefix, generateTableName}
import com.redislabs.provider.redis.rdd.RedisStandaloneSuite
import org.apache.spark.sql.redis.RedisFormat
import org.scalatest.Matchers

/**
  * @author The Viet Nguyen
  */
class FilteredDataframeStandaloneSuite extends RedisStandaloneSuite with DefaultTestDataset
  with Matchers {

  test("select none fields") {
    val tableName = generateTableName(TableNamePrefix)
    writeDf(tableName)
    val actualDf = spark.read.format(RedisFormat)
      .load(tableName)
      .select()
      .cache()
    actualDf.count() shouldBe expectedDf.count()
    actualDf.collect().foreach { r =>
      r.length shouldBe 0
    }
  }

  test("select all fields") {
    val tableName = generateTableName(TableNamePrefix)
    writeDf(tableName)
    val actualDf = spark.read.format(RedisFormat)
      .load(tableName)
      .select("name", "age", "address", "salary")
      .cache()
    verifyDf(actualDf)
  }

  test("select partial fields") {
    val tableName = generateTableName(TableNamePrefix)
    writeDf(tableName)
    val actualDf = spark.read.format(RedisFormat)
      .load(tableName)
      .select("name", "salary")
      .cache()
    verifyPartialDf(actualDf)
  }
}
