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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import osgl.exception.E;
import osgl.ut.TestBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@RunWith(Enclosed.class)
public class Func2Test {

    @RunWith(MockitoJUnitRunner.class)
    @Ignore
    public static class Func2TestBase extends TestBase {
        protected List<String> strings1 = new ArrayList<>();
        protected List<String> strings2 = new ArrayList<>();

        protected Func2<String, String, Integer> addToStrings = (s1, s2) -> {
            strings1.add(s1);
            strings2.add(s2);
            return s1.length() + s2.length();
        };

        @Mock
        private Func2<Object, Object, Object> mockFunc2;

        @Before
        public void prepare() {
            strings1.clear();
            strings2.clear();
        }

    }


    public static class CompositionTest extends Func2TestBase {

        private Function<Integer, Integer> after = (n) -> n * 10;

        @Test
        public void itShallRunAfterProcedureAfterThisProcedure() {
            eq(60, addToStrings.andThen(after).apply("foo", "bar"));
            eq("foo", strings1.get(0));
            eq("bar", strings2.get(0));
        }

    }

    public static class FallbackTest extends Func2TestBase {
        Func2<String, String, Integer> failCase = (s1, s2) -> {throw E.unexpected();};
        BiFunction<String, String, Integer> fallback = (s1, s2) -> {
            strings1.add("**" + s1 + "**");
            strings2.add("**" + s2 + "**");
            return s1.length() + s2.length() + 8;
        };

        @Test
        public void itShallNotCallfallbackIfNoException() {
            eq(6, addToStrings.applyOrElse("foo", "bar", fallback));
            yes(strings1.contains("foo"));
            no(strings1.contains("**foo**"));
            yes(strings2.contains("bar"));
            no(strings2.contains("**bar**"));

            strings1.clear();
            strings2.clear();
            eq(6, addToStrings.orElse(fallback).apply("foo", "bar"));
            yes(strings1.contains("foo"));
            no(strings1.contains("**foo**"));
            yes(strings2.contains("bar"));
            no(strings2.contains("**bar**"));
        }

        @Test
        public void itShallCallFallbackIfExceptionEncountered() {
            eq(14, failCase.applyOrElse("foo", "bar", fallback));
            no(strings1.contains("foo"));
            yes(strings1.contains("**foo**"));
            no(strings2.contains("bar"));
            yes(strings2.contains("**bar**"));

            strings1.clear();
            strings2.clear();
            eq(14, failCase.orElse(fallback).apply("foo", "bar"));
            no(strings1.contains("foo"));
            yes(strings1.contains("**foo**"));
            no(strings2.contains("bar"));
            yes(strings2.contains("**bar**"));
        }
    }

    public static class ConversionTest extends Func2TestBase {
        @Test
        @SuppressWarnings("ReturnValueIgnored")
        public void testToFunction() {
            addToStrings.toProcedure().run("foo", "bar");
            yes(strings1.contains("foo"));
            yes(strings2.contains("bar"));
        }

        @Test
        public void testCurrying() {
            eq(6, addToStrings.curry("bar").apply("foo"));
            yes(strings1.contains("foo"));
            yes(strings2.contains("bar"));
        }
    }

    public static class FactoryTest extends Func2TestBase {
        @Test
        public void testOfConsumer() {
            BiFunction<CharSequence, CharSequence, Integer> consumer = (cs1, cs2) -> {
                strings1.add(cs1.toString());
                strings2.add(cs2.toString());
                return cs1.length() + cs2.length();
            };
            eq(6, Func2.of(consumer).apply("foo", "bar"));
            yes(strings1.contains("foo"));
            yes(strings2.contains("bar"));
        }
    }
}
