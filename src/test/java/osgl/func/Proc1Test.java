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
import java.util.function.Consumer;

@RunWith(Enclosed.class)
public class Proc1Test {

    @RunWith(MockitoJUnitRunner.class)
    public static class Proc1TestBase extends TestBase {
        protected List<String> strings = new ArrayList<>();
        protected Proc1<String> addToStrings = (s) -> strings.add(s);
        @Mock
        private Proc1<Object> mockProc1;

        @Before
        public void prepare() {
            strings.clear();
        }

        @Test
        public void itShallCallRunIfInvoke() {
            Object param = new Object();
            Mockito.doCallRealMethod().when(mockProc1).accept(param);
            mockProc1.accept(param);
            Mockito.verify(mockProc1, Mockito.times(1)).run(param);
        }
    }

    public static class CompositionTest extends Proc1TestBase {

        private Consumer<String> after = (s) -> strings.add("'" + s + "'");

        private Consumer<String> before = after;

        @Test
        public void itShallRunAfterProcedureAfterThisProcedure() {
            addToStrings.andThen(after).run("foo");
            eq("foo", strings.get(0));
            eq("'foo'", strings.get(1));
        }

        @Test
        public void itShallRunBeforeProcedureBeforeThisProcedure() {
            addToStrings.nowThat(before).run("foo");
            eq("foo", strings.get(1));
            eq("'foo'", strings.get(0));
        }
    }

    public static class FallbackTest extends Proc1TestBase {
        Proc1<String> failCase = (s) -> {throw E.unexpected();};
        Proc1<String> fallback = (s) -> strings.add("**" + s + "**");

        @Test
        public void itShallNotCallfallbackIfNoException() {
            addToStrings.runOrElse("foo", fallback);
            yes(strings.contains("foo"));
            no(strings.contains("**foo**"));

            strings.clear();
            addToStrings.orElse(fallback).run("foo");
            yes(strings.contains("foo"));
            no(strings.contains("**foo**"));
        }

        @Test
        public void itShallCallFallbackIfExceptionEncountered() {
            failCase.runOrElse("foo", fallback);
            no(strings.contains("foo"));
            yes(strings.contains("**foo**"));

            strings.clear();
            failCase.orElse(fallback).run("foo");
            no(strings.contains("foo"));
            yes(strings.contains("**foo**"));
        }
    }

    public static class ConversionTest extends Proc1TestBase {
        @Test
        @SuppressWarnings("ReturnValueIgnored")
        public void testToFunction() {
            addToStrings.toFunction().apply("foo");
            yes(strings.contains("foo"));
        }

        @Test
        public void testCurrying() {
            addToStrings.curry("foo").run();
            yes(strings.contains("foo"));
        }
    }

    public static class FactoryTest extends Proc1TestBase {
        @Test
        public void testOfConsumer() {
            Consumer<CharSequence> consumer = (cs) -> strings.add(cs.toString());
            Proc1.of(consumer).run("foo");
            yes(strings.contains("foo"));
        }
    }
}
