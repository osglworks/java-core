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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import osgl.exception.E;
import osgl.ut.TestBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@RunWith(MockitoJUnitRunner.class)
public class Proc2Test extends TestBase {

    protected List<String> strings1 = new ArrayList<>();
    protected List<String> strings2 = new ArrayList<>();
    protected Proc2<String, String> addToStrings = (s1, s2) -> {
        strings1.add(s1);
        strings2.add(s2);
    };

    @Mock
    private Proc2<Object, Object> mockProc2;

    @Before
    public void prepare() {
        strings1.clear();
        strings2.clear();
    }

    @Test
    public void itShallCallRunIfInvokeAccept() {
        Object p1 = new Object();
        Object p2 = new Object();
        Mockito.doCallRealMethod().when(mockProc2).accept(p1, p2);
        mockProc2.accept(p1, p2);
        Mockito.verify(mockProc2, Mockito.times(1)).run(p1, p2);
    }

    public static class CompositionTest extends Proc2Test {

        private BiConsumer<String, String> after = (s1, s2) -> {
            strings1.add("'" + s1 + "'");
            strings2.add("'" + s2 + "'");
        };

        private BiConsumer<String, String> before = after;

        @Test
        public void itShallRunAfterProcedureAfterThisProcedure() {
            addToStrings.andThen(after).run("foo", "bar");
            eq("foo", strings1.get(0));
            eq("'foo'", strings1.get(1));
            eq("bar", strings2.get(0));
            eq("'bar'", strings2.get(1));
        }

        @Test
        public void itShallRunBeforeProcedureBeforeThisProcedure() {
            addToStrings.nowThat(before).run("foo", "bar");
            eq("foo", strings1.get(1));
            eq("'foo'", strings1.get(0));
            eq("bar", strings2.get(1));
            eq("'bar'", strings2.get(0));
        }
    }

    public static class FallbackTest extends Proc2Test {
        Proc2<String, String> failCase = (s1, s2) -> {throw E.unexpected();};
        BiConsumer<String, String> fallback = (s1, s2) -> {
            strings1.add("**" + s1 + "**");
            strings2.add("**" + s2 + "**");
        };

        @Test
        public void itShallNotCallfallbackIfNoException() {
            addToStrings.runOrElse("foo", "bar", fallback);
            yes(strings1.contains("foo"));
            no(strings1.contains("**foo**"));
            yes(strings2.contains("bar"));
            no(strings2.contains("**bar**"));

            strings1.clear();
            strings2.clear();
            addToStrings.orElse(fallback).run("foo", "bar");
            yes(strings1.contains("foo"));
            no(strings1.contains("**foo**"));
            yes(strings2.contains("bar"));
            no(strings2.contains("**bar**"));
        }

        @Test
        public void itShallCallFallbackIfExceptionEncountered() {
            failCase.runOrElse("foo", "bar", fallback);
            no(strings1.contains("foo"));
            yes(strings1.contains("**foo**"));
            no(strings2.contains("bar"));
            yes(strings2.contains("**bar**"));

            strings1.clear();
            strings2.clear();
            failCase.orElse(fallback).run("foo", "bar");
            no(strings1.contains("foo"));
            yes(strings1.contains("**foo**"));
            no(strings2.contains("bar"));
            yes(strings2.contains("**bar**"));
        }
    }

    public static class ConversionTest extends Proc2Test {
        @Test
        @SuppressWarnings("ReturnValueIgnored")
        public void testToFunction() {
            addToStrings.toFunction().apply("foo", "bar");
            yes(strings1.contains("foo"));
            yes(strings2.contains("bar"));
        }

        @Test
        public void testCurrying() {
            addToStrings.curry("foo").run("bar");
            yes(strings1.contains("foo"));
            yes(strings2.contains("bar"));
        }
    }

    public static class FactoryTest extends Proc2Test {
        @Test
        public void testOfConsumer() {
            BiConsumer<CharSequence, CharSequence> consumer = (cs1, cs2) -> {
                strings1.add(cs1.toString());
                strings2.add(cs2.toString());
            };
            Proc2.of(consumer).run("foo", "bar");
            yes(strings1.contains("foo"));
            yes(strings2.contains("bar"));
        }
    }
}
