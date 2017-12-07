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

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import osgl.exception.E;
import osgl.ut.TestBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RunWith(Enclosed.class)
public class Func1Test {

    @RunWith(MockitoJUnitRunner.class)
    public static class Func1TestBase extends TestBase {

        protected List<String> strings = new ArrayList<>();
        protected Func1<String, Integer> addToStrings = (s) -> {strings.add(s); return s.length();};
        @Mock
        protected Func1<Object, Object> mockFunc1;

        @Before
        public void prepare() {
            strings.clear();
        }

        @Test
        public void testNil() {
            Func1<Integer, Integer> x = Func1.nil();
            isNull(x.apply(333));
            isNull(Func1.nil().apply(new Object()));
            same(Func1.nil(), Func1.nil());
            same(Func1.NIL, Func1.nil());
        }

        @Test
        public void testIdentity() {
            Func1<Integer, Integer> x = Func1.identity();
            Func1<String, String> y = Func1.identity();

            same(x, y);
            same(5, x.apply(5));
            same("foo", y.apply("foo"));
            same(Func1.IDENTITY, Func1.identity());
        }

        @Test
        public void testConstant() {
            Func1<Integer, Integer> x = Func1.constant(0);
            eq(0, x.apply(5));
            eq(0, x.apply(4));

            Func1<Integer, String> y = Func1.constant("");
            eq("", y.apply(5));
            eq("", y.apply(4));
        }

    }

    public static class CompositionTest extends Func1TestBase {

        private static class Foo {}

        private Function<Class, String> before = Class::getSimpleName;

        private Function<Integer, Integer> after = (n) -> n * 2;

        @Test
        public void itShallRunAfterProcedureAfterThisProcedure() {
            eq(6, addToStrings.andThen(after).apply("foo"));
        }

        @Test
        public void itShallRunBeforeProcedureBeforeThisProcedure() {
            eq(3, addToStrings.nowThat(before).apply(Foo.class));
            yes(strings.contains("Foo"));
        }
    }

    public static class FallbackTest extends Func1TestBase {

        Func1<String, Integer> failCase = (s) -> {throw E.unexpected();};
        Func1<String, Integer> fallback = (s) -> {strings.add("**" + s + "**"); return s.length() + 4;};

        @Test
        public void itShallNotCallfallbackIfNoException() {
            eq(3, addToStrings.applyOrElse("foo", fallback));
            yes(strings.contains("foo"));

            strings.clear();
            eq(3, addToStrings.orElse(fallback).apply("foo"));
            yes(strings.contains("foo"));

            strings.clear();
            eq(3, addToStrings.applyOrElse("foo", 5));
            yes(strings.contains("foo"));
        }

        @Test
        public void itShallCallFallbackIfExceptionEncountered() {
            eq(7, failCase.applyOrElse("foo", fallback));
            yes(strings.contains("**foo**"));
            no(strings.contains("foo"));

            strings.clear();
            eq(7, failCase.orElse(fallback).apply("foo"));
            yes(strings.contains("**foo**"));
            no(strings.contains("foo"));

            strings.clear();
            eq(7, failCase.applyOrElse("foo", 7));
            no(strings.contains("**foo**"));
            no(strings.contains("foo"));
        }
    }

    public static class ConversionTest extends Func1TestBase {
        @Test
        public void testToFunction() {
            addToStrings.toProcedure().run("foo");
            yes(strings.contains("foo"));
        }

        @Test
        public void testCurrying() {
            eq(3, addToStrings.curry("foo").apply());
            yes(strings.contains("foo"));
        }

        @Test
        @SuppressWarnings("ReturnValueIgnored")
        public void testLift() {
            Mockito.when(mockFunc1.lift()).thenCallRealMethod();
            Object param = new Object();
            mockFunc1.lift().apply(param);
            Mockito.verify(mockFunc1, Mockito.times(1)).apply(param);
        }
    }

    public static class FactoryTest extends Func1TestBase {
        @Test
        public void testOfProcedure() {
            Proc1<CharSequence> procedure = (cs) -> strings.add(cs.toString());
            isNull(Func1.of(procedure).apply("foo"));
            yes(strings.contains("foo"));
        }
    }
}
