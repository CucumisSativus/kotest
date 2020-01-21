package com.sksamuel.kt.extensions.system

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.FreeSpecScope
import io.kotest.core.spec.style.WordSpec
import io.kotest.extensions.system.OverrideMode
import io.kotest.extensions.system.SystemEnvironmentTestListener
import io.kotest.extensions.system.withEnvironment
import io.kotest.inspectors.forAll
import io.kotest.shouldBe
import io.kotest.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kotlin.reflect.KClass

class SystemEnvironmentExtensionTest : FreeSpec() {

   private val key = "SystemEnvironmentExtensionTestFoo"
   private val value = "SystemEnvironmentExtensionTestBar"

   private val mode: OverrideMode = mockk {
      every { override(any(), any()) } answers {
         firstArg<Map<String, String>>().plus(secondArg<Map<String, String>>()).toMutableMap()
      }
   }

   init {
      "Should set environment to specific map" - {
         executeOnAllEnvironmentOverloads {
            System.getenv(key) shouldBe value
         }
      }

      "Should return original environment to its place after execution" - {
         val before = System.getenv().toMap()

         executeOnAllEnvironmentOverloads {
            System.getenv() shouldNotBe before
         }
         System.getenv() shouldBe before

      }

      "Should return the computed value" - {
         val results = executeOnAllEnvironmentOverloads { "RETURNED" }

         results.forAll {
            it shouldBe "RETURNED"
         }
      }
   }

   private suspend fun <T> FreeSpecScope.executeOnAllEnvironmentOverloads(block: suspend () -> T): List<T> {
      val results = mutableListOf<T>()

      "String String overload" {
         results += withEnvironment(key, value, mode) { block() }
      }

      "Pair overload" {
         results += withEnvironment(key to value, mode) { block() }
      }

      "Map overload" {
         results += withEnvironment(mapOf(key to value), mode) { block() }
      }

      return results
   }

}

class SystemEnvironmentTestListenerTest : WordSpec() {

   override fun listeners() =
      listOf(SystemEnvironmentTestListener("mop", "dop", mode = OverrideMode.SetOrOverride), listener)

   private val listener = object : TestListener {
      override fun prepareSpec(kclass: KClass<out Spec>) {
         System.getenv("mop") shouldBe null
      }

      override fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
         System.getenv("mop") shouldBe null
      }
   }

   init {
      "sys environment extension" should {
         "set environment variable" {
            System.getenv("mop") shouldBe "dop"
         }
      }
   }
}
