package osgl.func;

/*-
 * #%L
 * OSGL Core
 * %%
 * Copyright (C) 2017 OSGL (Open Source General Library)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import osgl.exception.E;
import osgl.ut.TestBase;

import java.util.Random;
import java.util.function.*;

@RunWith(MockitoJUnitRunner.class)
public class Func0Test extends TestBase {

    @Mock
    Func0<String> testTarget;

    @Test
    public void nilShallReturnNull() {
        isNull(Func0.NIL.apply());
    }

    @Test
    public void getShallDelegateToApply() {
        when(testTarget.get()).thenCallRealMethod();
        testTarget.get();
        verify(testTarget, times(1)).apply();
    }

    public static class FallbackTest extends Func0Test {

        Func0<String> fallback = () -> "fallback";

        Func0<String> successCase = () -> "foo";

        Func0<String> errorCase = () -> {throw E.unexpected();};

        @Test
        public void fallbackShallNotBeCalledIfNoException() {
            eq("foo", successCase.applyOrElse(fallback));
            eq("foo", successCase.orElse(fallback).apply());
        }

        @Test
        public void fallbackShallBeCalledIfExceptionEncountered() {
            eq("fallback", errorCase.applyOrElse(fallback));
            eq("fallback", errorCase.orElse(fallback).apply());
        }

    }

    public static class CompositionTest extends Func0Test {
        Function<String, Integer> after = String::length;
        Func0<String> testTarget = () -> "foo";

        @Test
        public void testAndThen() {
            eq(3, testTarget.andThen(after).apply());
        }
    }

    public static class ConversionTest extends Func0Test {

        @Test
        public void testToProcedure() {
            when(testTarget.toProcedure()).thenCallRealMethod();
            Proc0 proc = testTarget.toProcedure();
            proc.run();
            verify(testTarget, times(1)).apply();
        }

    }

    public static class FactoryTest extends Func0Test {

        Random r = new Random();

        @Test
        public void testConstantOfObject() {
            Object obj = new Object();
            same(obj, Func0.constant(obj).apply());
        }

        @Test
        public void testConstantOfBoolean() {
            yes(Func0.constant(true).getAsBoolean());
        }

        @Test
        public void testConstantOfDouble() {
            double d = r.nextDouble();
            eq(d, Func0.constant(d).getAsDouble());
        }

        @Test
        public void testConstentOfInt() {
            int n = r.nextInt();
            eq(n, Func0.constant(n).getAsInt());
        }

        @Test
        public void testConstantOfLong() {
            long l = r.nextLong();
            eq(l, Func0.constant(l).getAsLong());
        }

        @Test
        public void test_of_Supplier() {
            Supplier<String> s = () -> "foo";
            Func0<String> f = Func0.of(s);
            eq("foo", f.apply());
        }

        @Test
        public void test_of_BooleanSupplier() {
            BooleanSupplier s = () -> true;
            BooleanFunc0 f = Func0.of(s);
            yes(f.getAsBoolean());
        }

        @Test
        public void test_of_DoubleSupplier() {
            double d = r.nextDouble();
            DoubleSupplier s = () -> d;
            eq(d, Func0.of(s).getAsDouble());
        }

        @Test
        public void test_of_IntSupplier() {
            int n = r.nextInt();
            IntSupplier s = () -> n;
            eq(n, Func0.of(s).getAsInt());
        }

        @Test
        public void test_of_LongSupper() {
            long l = r.nextLong();
            LongSupplier s = () -> l;
            eq(l, Func0.of(s).getAsLong());
        }

    }

}
