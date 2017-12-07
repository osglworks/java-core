package osgl;

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
import org.mockito.junit.MockitoJUnitRunner;
import osgl.stage.ObjectRequire;
import osgl.ut.TestBase;
import util.HashCodeCalculator;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

@RunWith(Enclosed.class)
public class LangTest extends TestBase {

    public static class ConfigTest extends TestBase {

        @Before
        public void reset() {
            $.conf.clearBoolTesters();
        }

        @Test
        public void testRegisterBoolTester() {
            Object o = new Object();
            no($.bool(o));
            Predicate<Object> tester = $::notNull;
            $.conf.registerBoolTester(tester);
            yes($.conf.boolTesters.contains(tester));
            yes($.bool(o));
        }

        @Test(expected = NullPointerException.class)
        public void registerNullTesterShallRaiseNPE() {
            Predicate<Object> tester = null;
            $.conf.registerBoolTester(tester);
        }

        @Test
        public void testOverwriteStringBoolTester() {
            Predicate<String> tester = (s) -> {
                boolean b = Boolean.parseBoolean(s);
                if (b) {
                    return b;
                }
                return "yes".equalsIgnoreCase(s);
            };
            yes($.bool("yes"));
            yes($.bool("no"));
            $.conf.overwriteStringBoolTester(tester);
            yes($.bool("yes"));
            no($.bool("no"));
            no($.bool("blahblah"));
        }

        @Test(expected = NullPointerException.class)
        public void overwriteStringBoolTesterWithNullShallRaiseNPE() {
            Predicate<String> tester = null;
            $.conf.overwriteStringBoolTester(tester);
        }
    }

    public static class BoolTest {
        @Test
        public void testBool() {
            yes($.bool(true));
            no($.bool(false));

            yes($.bool(" "));
            no($.bool(""));

            Collection collection = null;
            no($.bool(collection));

            collection = new ArrayList<>();
            no($.bool(collection));
            collection.add(new Object());
            yes($.bool(collection));

            byte b = 0;
            no($.bool(b));
            b = 1;
            yes($.bool(b));

            char c = ' ';
            yes($.bool(c));
            c = 0;
            no($.bool(c));

            int n = 0;
            no($.bool(n));
            n = 1;
            yes($.bool(n));

            long l = 0L;
            no($.bool(l));
            l = 1L;
            yes($.bool(l));

            float f = 0f;
            no($.bool(f));
            f = 1f;
            yes($.bool(f));
            f = Float.MIN_NORMAL / 2;
            no($.bool(f));

            double d = 0d;
            no($.bool(d));
            d = 1d;
            yes($.bool(d));
            d = Double.MIN_NORMAL / 2;
            no($.bool(d));
            d = -Double.MIN_NORMAL / 2;
            no($.bool(d));
            d = Float.MIN_NORMAL / 2;
            yes($.bool(d));

            BigInteger bi = BigInteger.valueOf(1L);
            yes($.bool(bi));
            bi = BigInteger.valueOf(0);
            no($.bool(bi));
            bi = null;
            no($.bool(bi));

            BigDecimal bd = BigDecimal.valueOf(0.1d);
            yes($.bool(bd));
            bd = BigDecimal.valueOf(Double.MIN_VALUE);
            yes($.bool(bd));
            bd = BigDecimal.valueOf(0);
            no($.bool(bd));
            bd = null;
            no($.bool(bd));

            File file = new File("/not_exists/aaa");
            no($.bool(file));
            file = new File("/");
            yes($.bool(file));
            Supplier<File> fileSupplier = () -> new File("/");
            yes($.bool(fileSupplier));
            fileSupplier = () -> new File("/not_exists/aaa");
            no($.bool(fileSupplier));
            fileSupplier = () -> null;
            no($.bool(fileSupplier));
            fileSupplier = null;
            no($.bool(fileSupplier));
            no($.bool((Object) fileSupplier));

            Path path = null;
            no($.bool(path));
            path = Paths.get("/");
            yes($.bool(path));
            path = Paths.get("/not_exists/aaa");
            no($.bool(path));
            Supplier<Path> pathSupplier = () -> Paths.get("/");
            yes($.bool(pathSupplier));
            pathSupplier = () -> Paths.get("/not_exists/aaa");
            no($.bool(pathSupplier));
            pathSupplier = () -> null;
            no($.bool(pathSupplier));
            pathSupplier = null;
            no($.bool(pathSupplier));
            no($.bool(((Object) pathSupplier)));

            BooleanSupplier booleanSupplier = null;
            no($.bool(booleanSupplier));
            yes($.bool(() -> true));
            no($.bool(() -> false));
            Object o = (BooleanSupplier) () -> true;
            yes($.bool(o));

            IntSupplier intSupplier = null;
            no($.bool(intSupplier));
            no($.bool(() -> 0));
            yes($.bool(() -> 1));
            o = (IntSupplier) () -> 1;
            yes($.bool(o));

            LongSupplier longSupplier = null;
            no($.bool(longSupplier));
            yes($.bool(() -> 1L));
            no($.bool(() -> 0L));
            o = (LongSupplier) () -> 1L;
            yes($.bool(o));

            DoubleSupplier doubleSupplier = null;
            no($.bool(doubleSupplier));
            no($.bool(() -> 0D));
            yes($.bool(() -> 1.1D));
            o = (DoubleSupplier) () -> 1.0D;
            yes($.bool(o));

            int[] ia = {};
            no($.bool(ia));
            ia = new int[]{1};
            yes($.bool(ia));
        }

    }

    public static class RequireTest {

        @Test
        public void testRequire() {
            Object o = new Object();
            eq(new ObjectRequire<>(o), $.require(o));
        }

        @Test(expected = NullPointerException.class)
        public void requireNotNullShallRaiseNPEIfTargetIsNull() {
            $.requireNotNull(null);
        }

        @Test
        public void requireNotNullShallReturnObjectIfNotNull() {
            Object o = new Object();
            same(o, $.requireNotNull(o));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class EqualityTest {

        @Test
        public void nullValuesShallBeEqualToEachOther() {
            yes($.equals(null, null));
        }

        @Test
        public void leftNullShallNotEqualToNonNullValue() {
            no($.equals(null, new Object()));
        }

        @Test
        public void nonNullValueShallNotEqualToRightNull() {
            no($.equals(new Object(), null));
        }

        @Test
        public void sameNonNullShallBeEqualToEachOther() {
            Object o = new Object();
            yes($.equals(o, o));
        }

        @Test
        public void itShallDelegateToObjectEqualMethodForNonNullValue() {
            final AtomicInteger ai = new AtomicInteger(0);
            Object o = new Object() {
                @Override
                public boolean equals(Object obj) {
                    ai.incrementAndGet();
                    return super.equals(obj);
                }
            };
            no($.equals(o, new Object()));
            eq(1, ai.get());
        }

        @Test
        public void itShallCallEqWhenCallingNe() {
            final AtomicInteger ai = new AtomicInteger(0);
            Object o = new Object() {
                @Override
                public boolean equals(Object obj) {
                    ai.incrementAndGet();
                    return super.equals(obj);
                }
            };
            yes($.notEquals(o, new Object()));
            eq(1, ai.get());
        }

        @Test
        public void testDeepEquals() {
            yes($.deepEquals(null, null));
            no($.deepEquals(null, new Object()));
            no($.deepEquals(new Object(), null));
            no($.deepEquals(new Object(), new Object()));
            Object o1 = new Object();
            Object o2 = new Object();
            Object[] a1 = {o1, o2};
            Object[] a2 = {o1, o2};
            Object[] a3 = {o1};
            Object[] a4 = {o1, o2, new Object()};
            Object[] a5 = {o2, o1};
            no($.equals(a1, a2));
            yes($.deepEquals(a1, a2));
            no($.deepEquals(a1, a3));
            no($.deepEquals(a1, a4));
            no($.deepEquals(a1, a5));
            Object[][] aa1 = {{o1, o2}, {o2, o1}};
            Object[][] aa2 = {{o1, o2}, {o2, o1}};
            yes($.deepEquals(aa1, aa2));
            Object[][] aa3 = {{o1, o2}, {o2, o1}, {o1, o1}};
            no($.deepEquals(aa1, aa3));

            int[][] iaa1 = {{1, 2}, {3, 4}};
            int[][] iaa2 = {{1, 2}, {3, 4}};
            yes($.deepEquals(iaa1, iaa2));

            int[][] iaa3 = {{2, 1}, {3, 4}};
            no($.deepEquals(iaa1, iaa3));
        }

    }

    public static class HashCodeTest {

        private final HashCodeCalculator hc = new HashCodeCalculator($.HC_INIT, $.HC_FACT);

        @Test
        public void testBoolean() {
            ne($.hashCode(true), $.hashCode(false));
            eq($.hashCode(true), $.hashCode(true));
            eq($.hashCode(false), $.hashCode(false));
            Object o = true;
            eq(o.hashCode(), $.hashCode(o));
            o = false;
            eq(o.hashCode(), $.hashCode(o));
        }

        @Test
        public void testBooleanArray() {
            boolean[] ba = {true, false, true, true};
            eq(hc.hashCode(ba), $.hashCode(ba));
            Object o = ba;
            eq($.hashCode(o), $.hashCode(ba));
        }

        @Test
        public void testShort() {
            short s1 = 0, s2 = 1;
            eq(0, $.hashCode(s1));
            eq(1, $.hashCode(s2));
            Object o = s2;
            $.eq($.hashCode(o), $.hashCode(s1));
        }

        @Test
        public void testShortArray() {
            short[] sa = {3, 1, 7, 5};
            eq(hc.hashCode(sa), $.hashCode(sa));
            Object o = sa;
            eq($.hashCode(o), $.hashCode(sa));
        }

        @Test
        public void testByte() {
            byte b = 0;
            eq(0, $.hashCode(b));
            b = 8;
            eq(8, $.hashCode(b));
            Object o = b;
            eq($.hashCode(o), $.hashCode(b));
        }

        @Test
        public void testByteArray() {
            byte[] sa = {3, 1, 7, 53};
            eq(hc.hashCode(sa), $.hashCode(sa));
            Object o = sa;
            eq($.hashCode(o), $.hashCode(sa));
        }

        @Test
        public void testChar() {
            char c = 0;
            eq(0, $.hashCode(c));
            c = 8;
            eq(8, $.hashCode(c));
            Object o = c;
            eq($.hashCode(o), $.hashCode(c));
        }

        @Test
        public void testCharArray() {
            char[] ca = {'中', 'c', 0, 17};
            eq(hc.hashCode(ca), $.hashCode(ca));
            Object o = ca;
            eq($.hashCode(o), $.hashCode(ca));
        }

        @Test
        public void testInt() {
            int i = 0;
            eq(0, $.hashCode(i));
            i = 98;
            eq(98, $.hashCode(i));
            Object o = i;
            eq($.hashCode(o), $.hashCode(i));
        }

        @Test
        public void testIntArray() {
            int[] ia = {3, 1, 399, Integer.MAX_VALUE, 3};
            eq(hc.hashCode(ia), $.hashCode(ia));
            Object o = ia;
            eq($.hashCode(o), $.hashCode(ia));
        }

        @Test
        public void testFloat() {
            float f = 0f;
            eq(0, $.hashCode(f));
            f = 9342.433f;
            eq(Float.floatToIntBits(f), $.hashCode(f));
            Object o = f;
            eq($.hashCode(o), $.hashCode(f));
        }

        @Test
        public void testFloatArray() {
            float[] fa = {0.3f, 33f, 987};
            eq(hc.hashCode(fa), $.hashCode(fa));
            Object o = fa;
            eq($.hashCode(o), $.hashCode(fa));
        }

        @Test
        public void testLong() {
            long l = 43243252394L;
            eq((int)(l ^ l >> 32), $.hashCode(l));
            Object o = l;
            eq($.hashCode(o), $.hashCode(l));
        }

        @Test
        public void testLongArray() {
            long[] la = {Long.MAX_VALUE, -33, 234250239};
            eq(hc.hashCode(la), $.hashCode(la));
            Object o = la;
            eq($.hashCode(o), $.hashCode(la));
        }

        @Test
        public void testDouble() {
            double d = 32432.5438975439873d;
            eq($.hashCode(Double.doubleToLongBits(d)), $.hashCode(d));
            Object o = d;
            eq($.hashCode(o), $.hashCode(d));
        }

        @Test
        public void testDoubleArray() {
            double[] da = {Double.MIN_NORMAL, Double.MIN_VALUE, Double.MAX_VALUE, 32432d};
            eq($.hashCode(da), $.hashCode(da));
            Object o = da;
            eq($.hashCode(o), $.hashCode(da));
        }

        @Test
        public void testObject() {
            Object o = new Object();
            eq(o.hashCode(), $.hashCode(o));
        }

        @Test
        public void testObject2() {
            Object o1 = new Object();
            Object o2 = new Object();
            Object[] oa = {o1, o2};
            eq(hc.hashCode(oa), $.hashCode(o1, o2));
        }

        @Test
        public void testObject3() {
            Object o1 = new Object();
            Object o2 = new Object();
            Object o3 = new Object();
            Object[] oa = {o1, o2, o3};
            eq(hc.hashCode(oa), $.hashCode(o1, o2, o3));
        }

        @Test
        public void testObject4() {
            Object o1 = new Object();
            Object o2 = new Object();
            Object o3 = new Object();
            Object o4 = new Object();
            Object[] oa = {o1, o2, o3, o4};
            eq(hc.hashCode(oa), $.hashCode(o1, o2, o3, o4));
        }

        @Test
        public void testObject5() {
            Object o1 = new Object();
            Object o2 = new Object();
            Object o3 = new Object();
            Object o4 = new Object();
            Object o5 = new Object();
            Object[] oa = {o1, o2, o3, o4, o5};
            eq(hc.hashCode(oa), $.hashCode(o1, o2, o3, o4, o5));
        }

        @Test
        public void testObjectN() {
            Object o1 = new Object();
            Object o2 = new Object();
            Object o3 = new Object();
            Object o4 = new Object();
            Object o5 = new Object();
            Object[] oa = {o1, o2, o3, o4, o5, o1, o2, o4};
            eq(hc.hashCode(oa), $.hashCode(o1, o2, o3, o4, o5, o1, o2, o4));
        }

    }

}
