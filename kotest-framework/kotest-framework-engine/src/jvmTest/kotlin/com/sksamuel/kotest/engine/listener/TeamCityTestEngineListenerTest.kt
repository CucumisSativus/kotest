package com.sksamuel.kotest.engine.listener

import io.kotest.assertions.Actual
import io.kotest.assertions.Expected
import io.kotest.assertions.failure
import io.kotest.assertions.print.Printed
import io.kotest.core.SourceRef
import io.kotest.core.descriptors.append
import io.kotest.core.descriptors.toDescriptor
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.core.test.config.ResolvedTestConfig
import io.kotest.engine.listener.TeamCityTestEngineListener
import io.kotest.extensions.system.captureStandardOut
import io.kotest.matchers.shouldBe
import java.io.FileNotFoundException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class TeamCityTestEngineListenerTest : FunSpec() {

   init {

      val a = TestCase(
         TeamCityTestEngineListenerTest::class.toDescriptor().append("a"),
         TestName("a"),
         this@TeamCityTestEngineListenerTest,
         { },
         SourceRef.ClassLineSource("foo.bar.Test", 12),
         TestType.Container,
         ResolvedTestConfig.default,
         null,
         null
      )

      val b = a.copy(
         parent = a,
         name = TestName("b"),
         descriptor = a.descriptor.append("b"),
         source = SourceRef.ClassLineSource("foo.bar.Test", 17),
      )

      val c = b.copy(
         parent = b,
         name = TestName("c"),
         descriptor = b.descriptor.append("c"),
         type = TestType.Test,
         source = SourceRef.ClassLineSource("foo.bar.Test", 33),
      )

      test("should support nested tests") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a")
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(c, TestResult.Success(123.milliseconds))
            listener.testFinished(b, TestResult.Success(324.milliseconds))
            listener.testFinished(a, TestResult.Success(653.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(emptyList())
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='123' locationHint='kotest:class://foo.bar.Test:33' result_status='Success']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='324' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='653' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
"""
      }

      test("should support errors in tests") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(c, TestResult.Error(653.milliseconds, Exception("boom")))
            listener.testFinished(b, TestResult.Success(123.milliseconds))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(emptyList())
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFailed name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='653' locationHint='kotest:class://foo.bar.Test:33' message='boom' result_status='Error']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='653' locationHint='kotest:class://foo.bar.Test:33' result_status='Error']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='123' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
"""
      }

      test("should support ignored tests with reason") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(c, TestResult.Ignored("don't like it"))
            listener.testFinished(b, TestResult.Success(123.milliseconds))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(emptyList())
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testIgnored name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33' message='don|'t like it' result_status='Ignored']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='123' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
"""
      }

      test("should support errors in test suites by adding placeholder test") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(c, TestResult.Success(123.milliseconds))
            listener.testFinished(b, TestResult.Error(653.milliseconds, Exception("boom")))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(emptyList())
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='123' locationHint='kotest:class://foo.bar.Test:33' result_status='Success']
a[testStarted name='Exception' id='Exception' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b']
a[testFailed name='Exception' id='Exception' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' message='boom']
a[testFinished name='Exception' id='Exception' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='653' locationHint='kotest:class://foo.bar.Test:17' result_status='Error']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
"""
      }

      test("should support errors in specs by adding placeholder test") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(c, TestResult.Success(123.milliseconds))
            listener.testFinished(b, TestResult.Success(555.milliseconds))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, Exception("wobble"))
            listener.engineFinished(emptyList())
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='123' locationHint='kotest:class://foo.bar.Test:33' result_status='Success']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='555' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testStarted name='Exception' id='Exception' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest']
a[testFailed name='Exception' id='Exception' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' message='wobble']
a[testFinished name='Exception' id='Exception' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
"""
      }

      test("should support multiline error messages") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(
               c,
               TestResult.Error(
                  123.milliseconds, FileNotFoundException(
                     """
               well this is a
               big
               message"""
                  )
               )
            )
            listener.testFinished(b, TestResult.Success(555.milliseconds))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(emptyList())
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFailed name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='123' locationHint='kotest:class://foo.bar.Test:33' message='well this is a' result_status='Error']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='123' locationHint='kotest:class://foo.bar.Test:33' result_status='Error']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='555' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
"""
      }

      test("should write engine errors") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(c, TestResult.Success(555.milliseconds))
            listener.testFinished(b, TestResult.Success(555.milliseconds))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(listOf(Exception("big whoop")))
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='555' locationHint='kotest:class://foo.bar.Test:33' result_status='Success']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='555' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testStarted name='Engine exception']
a[testFailed name='Engine exception' message='big whoop']
a[testFinished name='Engine exception']
"""
      }

      test("should write multiple engine errors") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(c, TestResult.Success(555.milliseconds))
            listener.testFinished(b, TestResult.Success(555.milliseconds))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(listOf(Exception("big whoop"), Exception("big whoop 2")))
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='555' locationHint='kotest:class://foo.bar.Test:33' result_status='Success']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='555' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testStarted name='Engine exception 1']
a[testFailed name='Engine exception 1' message='big whoop']
a[testFinished name='Engine exception 1']
a[testStarted name='Engine exception 2']
a[testFailed name='Engine exception 2' message='big whoop 2']
a[testFinished name='Engine exception 2']
"""
      }

      test("should use comparison values with a supported exception type") {
         val output = captureStandardOut {
            val listener = TeamCityTestEngineListener("a", details = false)
            listener.engineStarted()
            listener.specStarted(TeamCityTestEngineListenerTest::class)
            listener.testStarted(a)
            listener.testStarted(b)
            listener.testStarted(c)
            listener.testFinished(
               c,
               TestResult.Error(
                  555.milliseconds,
                  failure(Expected(Printed("expected")), Actual(Printed("actual")))
               )
            )
            listener.testFinished(b, TestResult.Success(555.milliseconds))
            listener.testFinished(a, TestResult.Success(324.milliseconds))
            listener.specFinished(TeamCityTestEngineListenerTest::class, null)
            listener.engineFinished(emptyList())
         }
         output shouldBe """a[testSuiteStarted name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
a[testSuiteStarted name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://foo.bar.Test:12']
a[testSuiteStarted name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' locationHint='kotest:class://foo.bar.Test:17']
a[testStarted name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' locationHint='kotest:class://foo.bar.Test:33']
a[testFailed name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='555' locationHint='kotest:class://foo.bar.Test:33' message='expected:<expected> but was:<actual>' type='comparisonFailure' actual='actual' expected='expected' result_status='Error']
a[testFinished name='c' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b -- c' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' duration='555' locationHint='kotest:class://foo.bar.Test:33' result_status='Error']
a[testSuiteFinished name='b' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a -- b' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' duration='555' locationHint='kotest:class://foo.bar.Test:17' result_status='Success']
a[testSuiteFinished name='a' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest/a' parent_id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' duration='324' locationHint='kotest:class://foo.bar.Test:12' result_status='Success']
a[testSuiteFinished name='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' id='com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest' locationHint='kotest:class://com.sksamuel.kotest.engine.listener.TeamCityTestEngineListenerTest:1']
"""
      }
   }
}
