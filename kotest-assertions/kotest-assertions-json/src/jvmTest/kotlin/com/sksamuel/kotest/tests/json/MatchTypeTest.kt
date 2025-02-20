package com.sksamuel.kotest.tests.json

import io.kotest.assertions.json.shouldBeJsonArray
import io.kotest.assertions.json.shouldBeJsonObject
import io.kotest.assertions.json.shouldBeValidJson
import io.kotest.assertions.json.shouldNotBeJsonArray
import io.kotest.assertions.json.shouldNotBeJsonObject
import io.kotest.assertions.json.shouldNotBeValidJson
import io.kotest.core.spec.style.StringSpec

class MatchTypeTest : StringSpec() {


   init {
      "should be valid json" {
         """
            {"key":"value"}
         """.shouldBeValidJson()
      }
      "should not be valid json" {
         """
            {notvalid}
         """.shouldNotBeValidJson()
      }


      "should be json array" {
         """
            ["abc","def"]
         """.shouldBeJsonArray()
         """
            [1,2,3]
         """.shouldBeJsonArray()
         """
            [{"key":"value"},{"key2":"value2"}]
         """.shouldBeJsonArray()
      }

      "should not be JsonArray" {
         """
            {"key":"value"}
         """.shouldNotBeJsonArray()

         """
            {"array":["value", "value2"]}
         """.shouldNotBeJsonArray()
      }

      "should be JsonObject" {
         """
            {"key":"value"}
         """.shouldBeJsonObject()

         """
            {"array":["value", "value2"]}
         """.shouldBeJsonObject()

      }

      "should not be JsonObject" {
         """
            ["abc","def"]
         """.shouldNotBeJsonObject()
         """
            [1,2,3]
         """.shouldNotBeJsonObject()
         """
            [{"key":"value"},{"key2":"value2"}]
         """.shouldNotBeJsonObject()
      }

   }
}

