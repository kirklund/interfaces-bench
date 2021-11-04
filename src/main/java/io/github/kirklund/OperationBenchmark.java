/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kirklund;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@Measurement(iterations = 5, time = 120, timeUnit = SECONDS)
@Warmup(iterations = 1, time = 30, timeUnit = SECONDS)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(NANOSECONDS)
@State(Scope.Benchmark)
@SuppressWarnings("all")
public class OperationBenchmark {

  private Operation methodRefOperation;
  private Operation lambdaOperation;
  private Operation implementedOperation;
  private Operation anonymousOperation;

  @Setup
  public void setUp() {
    System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());

    methodRefOperation = StaticOperation::execute;
    lambdaOperation = () -> StaticOperation.execute();
    implementedOperation = new NonStaticOperation();
    anonymousOperation = new Operation() {
      @Override
      public boolean execute() {
        return StaticOperation.execute();
      }
    };
  }

  @Benchmark @Threads(1)
  public void staticOperation_threads1(Blackhole blackhole) {
    blackhole.consume(StaticOperation.execute());
  }
  @Benchmark @Threads(100)
  public void staticOperation_threads100(Blackhole blackhole) {
    blackhole.consume(StaticOperation.execute());
  }

  @Benchmark @Threads(1)
  public void implementedOperation_threads1(Blackhole blackhole) {
    blackhole.consume(implementedOperation.execute());
  }
  @Benchmark @Threads(100)
  public void implementedOperation_threads100(Blackhole blackhole) {
    blackhole.consume(implementedOperation.execute());
  }

  @Benchmark @Threads(1)
  public void methodRefOperation_threads1(Blackhole blackhole) {
    blackhole.consume(methodRefOperation.execute());
  }
  @Benchmark @Threads(100)
  public void methodRefOperation_threads100(Blackhole blackhole) {
    blackhole.consume(methodRefOperation.execute());
  }

  @Benchmark @Threads(1)
  public void lambdaOperation_threads1(Blackhole blackhole) {
    blackhole.consume(lambdaOperation.execute());
  }
  @Benchmark @Threads(100)
  public void lambdaOperation_threads100(Blackhole blackhole) {
    blackhole.consume(lambdaOperation.execute());
  }

  @Benchmark @Threads(1)
  public void anonymousOperation_threads1(Blackhole blackhole) {
    blackhole.consume(anonymousOperation.execute());
  }
  @Benchmark @Threads(100)
  public void anonymousOperation_threads100(Blackhole blackhole) {
    blackhole.consume(anonymousOperation.execute());
  }
}
