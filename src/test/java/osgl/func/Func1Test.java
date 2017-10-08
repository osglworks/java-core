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

import org.junit.Test;
import osgl.ut.TestBase;

import java.util.function.Function;

public class Func1Test extends TestBase {

    private static class TargetBase implements Func1<String, Integer> {
        @Override
        public Integer apply(String s) {
            return s.length();
        }
    }

    public static class ApplyOrElseTest extends TestBase {

        private static class WithoutException extends TargetBase {}

        private static class WithException extends TargetBase {
            @Override
            public Integer apply(String s) {
                throw new RuntimeException();
            }
        }

        private static class Fallback extends TargetBase {
            @Override
            public Integer apply(String s) {
                return s.length() * 2;
            }
        }

        @Test
        public void itShallNotCallFallbackIfNoExceptionEncountered() {
            WithoutException target = new WithoutException();
            eq(3, target.applyOrElse("foo", 5));
            eq(3, target.applyOrElse("foo", new Fallback()));
            eq(3, target.orElse(5).apply("foo"));
            eq(3, target.orElse(new Fallback()).apply("foo"));
        }

        @Test
        public void itShallCallFallbackIfExceptionEncountered() {
            WithException target = new WithException();
            eq(5, target.applyOrElse("foo", 5));
            eq(5, target.orElse(5).apply("foo"));
            eq(6, target.applyOrElse("foo", new Fallback()));
            eq(6, target.orElse(new Fallback()).apply("foo"));
        }

    }

    private static Function<Integer, Integer> DOUBLE = (n) -> n * 2;

    @Test
    public void andThenTest() {
        Func1<String, Integer> func = new TargetBase().andThen(DOUBLE);
        eq(6, func.apply("foo"));
    }

    private static Func1<Class, String> CLASS_NAME = Class::getSimpleName;

    @Test
    public void nowThatTest() {
        Func0<Integer> func0 = new TargetBase().nowThat(() -> "foo");
        eq(3, func0.apply());

        Func1<Class, Integer> func1 = new TargetBase().nowThat(CLASS_NAME);
        eq(5, func1.apply(Func1.class));
    }

    @Test
    public void curryingTest() {
        TargetBase func1 = new TargetBase();
        Func0<Integer> curry = func1.currying("foo");
        eq(func1.apply("foo"), curry.apply());
    }

    @Test
    public void testDumb() {
        Func1<Integer, Integer> x = Func1.dumb();
        isNull(x.apply(333));
        isNull(Func1.dumb().apply(new Object()));
        same(Func1.dumb(), Func1.dumb());
        same(Func1.NIL, Func1.dumb());
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
