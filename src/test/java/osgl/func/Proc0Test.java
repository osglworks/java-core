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

public class Proc0Test extends TestBase {

    List<String> strings = new ArrayList<>();
    Runnable runnable = () -> strings.add("foo");
    Proc0 proc0 = () -> strings.add("bar");

    @Before
    public void prepare() {
        strings.clear();
    }

    public static class OfRunnableTest {

        @Test
        public void itShallKeepRunnableLogic() {
            final String[] sa = {"foo"};
            Runnable runnable = () -> sa[0] = "bar";
            Proc0 proc0 = Proc0.of(runnable);
            proc0.run();
            eq("bar", sa[0]);
        }

        @Test
        public void itShallNotCreateNewInstanceIfRunnableIsProc0() {
            Runnable runnable = Proc0.NIL;
            Proc0 proc0 = Proc0.of(runnable);
            same(runnable, proc0);
        }
    }

    public static class NowThatAndThenTest extends Proc0Test {

        @Test
        public void testNowThat() {
            proc0.nowThat(runnable).run();
            eq("foo", strings.get(0));
            eq("bar", strings.get(1));
        }

        @Test
        public void testAndThen() {
            proc0.andThen(runnable).run();
            eq("bar", strings.get(0));
            eq("foo", strings.get(1));
        }
    }

    public static class FallbackTest extends Proc0Test {
        Proc0 failCase = E::unexpected;
        Proc0 fallback = () -> strings.add("fallback");

        @Test
        public void itShallNotCallFallbackIfProcNotFail() {
            proc0.runOrElse(fallback);
            yes(strings.contains("bar"));
            no(strings.contains("fallback"));
            strings.clear();
            proc0.orElse(fallback).run();
            yes(strings.contains("bar"));
            no(strings.contains("fallback"));
        }

        @Test
        public void itShallCallFallbackIfProcFailed() {
            failCase.runOrElse(fallback);
            yes(strings.contains("fallback"));
            strings.clear();
            failCase.orElse(fallback).run();
            yes(strings.contains("fallback"));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class ConversionTest extends Proc0Test {

        @Mock
        Func0<Object> mockFunc;

        @Test
        public void testToFunction() {
            Func0<String> func = proc0.toFunction();
            func.apply();
            yes(strings.contains("bar"));
        }

        @Test
        public void testOfFunction() {
            Proc0.of(mockFunc);
            Mockito.verify(mockFunc, Mockito.times(1)).toProcedure();
        }

    }

    public static class NullabilityTest extends Proc0Test {

        @Test(expected = NullPointerException.class)
        public void testAndThen_Runnable() {
            proc0.andThen(null);
        }

        @Test(expected = NullPointerException.class)
        public void testNowThat_Runnable() {
            proc0.nowThat(null);
        }

        @Test(expected = NullPointerException.class)
        public void testOrElse_Runnable() {
            proc0.orElse(null);
        }

        @Test(expected = NullPointerException.class)
        public void testRunOrElse_Runnable() {
            proc0.runOrElse(null);
        }

        @Test(expected = NullPointerException.class)
        public void testOf_Runnable() {
            Runnable runnable = null;
            Proc0.of(runnable);
        }

        @Test(expected = NullPointerException.class)
        public void testOf_Func0() {
            Func0<?> func = null;
            Proc0.of(func);
        }
    }

}
