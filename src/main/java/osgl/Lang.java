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

import static osgl.Option.NONE;

import osgl.collection.Pair;
import osgl.collection.T2;
import osgl.collection.T3;
import osgl.collection.Triple;
import osgl.exception.NotAppliedException;
import osgl.stage.ObjectRequire;
import osgl.stage.ObjectPredicate;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.*;

public class Lang {

    public static final class Conf {
        Predicate<String> stringTester = S::notEmpty;
        final List<Predicate<Object>> boolTesters = new ArrayList<>();
        /**
         * Register a boolean tester. A boolean tester is a {@link Function Function&lt;Object, Boolean&gt;} type function
         * that applied to `Object` type parameter and returns a boolean value of the Object been tested. It
         * should throw out {@link NotAppliedException} if the type of the object been tested is not recognized.
         * there is no need to test if the parameter is `null` in the tester as the utility will guarantee the
         * object passed in is not `null`
         *
         * ```java
         *  Osgl.Conf.registerBoolTester((v) -> {
         *      if (v instanceof Score) {
         *          return ((Score)v).intValue() > 60;
         *      }
         *      if (v instanceof Person) {
         *          return ((Person)v).age() > 16;
         *      }
         *      ...
         *      // since we do not recognize the object type, raise the NotAppliedException out
         *      throw new NotAppliedException();
         *  });
         * ```
         *
         * @param tester
         *      the tester function takes an object as parameter and returns boolean
         * @return the `$.conf` instance
         */
        public Conf registerBoolTester(Predicate<Object> tester) {
            boolTesters.add(requireNotNull(tester));
            return this;
        }

        /**
         * Overwrite default boolean tester for String type variable.
         *
         * For example
         *
         * ```java
         * // make it returns `true` if `s` is "true" or "yes"
         * $.conf.overwriteStringBoolTester((s) -> {
         *     return "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s);
         * })
         * ```
         *
         * @param tester
         *      The non null string value tester
         * @return the `$.conf` instance
         */
        public Conf overwriteStringBoolTester(Predicate<String> tester) {
            stringTester = requireNotNull(tester);
            return this;
        }

        public Conf clearBoolTesters() {
            stringTester = S::notEmpty;
            boolTesters.clear();
            return this;
        }
    }

    /**
     * The configuration
     */
    public static final Conf conf = new Conf();

    Lang() {
    }

    /**
     * Evaluate an object's bool value. The rules are:
     * <table summary="boolean value evaluation rules">
     * <thead>
     * <tr>
     * <th>case</th><th>bool value</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr><td>{@code null}</td><td>{@code false}</td></tr>
     * <tr><td>{@link Option#NONE}</td><td>{@code false}</td></tr>
     * <tr><td>String</td><td>{@link S#notEmpty(String) S.notEmpty(v)}</td></tr>
     * <tr><td>Collection</td><td>{@link java.util.Collection#isEmpty() !v.isEmpty()}</td></tr>
     * <tr><td>Array</td><td>length of the array &gt; 0</td></tr>
     * <tr><td>Byte</td><td>{@code v != 0}</td></tr>
     * <tr><td>Char</td><td>{@code v != 0}</td></tr>
     * <tr><td>Integer</td><td>{@code v != 0}</td></tr>
     * <tr><td>Long</td><td>{@code v != 0L}</td></tr>
     * <tr><td>Float</td><td>{@code Math.abs(v) > Float.MIN_NORMAL}</td></tr>
     * <tr><td>Double</td><td>{@code Math.abs(v) > Double.MIN_NORMAL}</td></tr>
     * <tr><td>BigInteger</td><td>{@code !BigInteger.ZERO.equals(v)}</td></tr>
     * <tr><td>BigDecimal</td><td>{@code !BigDecimal.ZERO.equals(v)}</td></tr>
     * <tr><td>File</td><td>{@link java.io.File#exists() v.exists()}</td></tr>
     * <tr><td>{@link Supplier}</td><td>{@code bool(v.get())}</td></tr>
     * <tr><td>Other types</td><td>{@code false}</td></tr>
     * </tbody>
     * </table>
     *
     * @param v the value to be evaluated
     * @return `true` if v evaluate to true, `false` otherwise
     */
    public static boolean bool(Object v) {
        if (null == v || NONE == v) {
            return false;
        }
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        if (v instanceof String) {
            return conf.stringTester.test((String) v);
        }
        if (v instanceof Collection) {
            return !((Collection) v).isEmpty();
        }
        if (v.getClass().isArray()) {
            return 0 < Array.getLength(v);
        }
        if (v instanceof Number) {
            if (v instanceof Float) {
                return bool((float) (Float) v);
            }
            if (v instanceof Double) {
                return bool((double) (Double) v);
            }
            if (v instanceof BigInteger) {
                return bool((BigInteger) v);
            }
            if (v instanceof BigDecimal) {
                return bool((BigDecimal) v);
            }
            return bool(((Number) v).intValue());
        }
        if (v instanceof File) {
            return bool((File) v);
        }
        if (v instanceof Path) {
            return bool((Path) v);
        }
        for (Predicate<Object> tester : conf.boolTesters) {
            try {
                return (tester.test(v));
            } catch (RuntimeException e) {
                // ignore
            }
        }
        if (v instanceof Supplier) {
            return bool(((Supplier) v).get());
        }
        if (v instanceof BooleanSupplier) {
            return bool(((BooleanSupplier) v).getAsBoolean());
        }
        if (v instanceof IntSupplier) {
            return bool(((IntSupplier) v).getAsInt());
        }
        if (v instanceof LongSupplier) {
            return bool(((LongSupplier) v).getAsLong());
        }
        if (v instanceof DoubleSupplier) {
            return bool(((DoubleSupplier) v).getAsDouble());
        }
        return false;
    }

    public static boolean bool(boolean v) {
        return v;
    }

    public static boolean bool(BooleanSupplier supplier) {
        if (null == supplier) {
            return false;
        }
        return supplier.getAsBoolean();
    }

    /**
     * Do bool evaluation on a String.
     *
     * @param s the string to be evaluated
     * @return {@code true} if s is not empty
     * @see S#empty(String)
     */
    public static boolean bool(String s) {
        return conf.stringTester.test(s);
    }

    /**
     * Do bool evaluation on a collection.
     *
     * @param c the collection to be evaluated
     * @return {@code true} if the collection is not empty
     * @see java.util.Collection#isEmpty()
     */
    public static boolean bool(Collection<?> c) {
        return null != c && !c.isEmpty();
    }

    /**
     * Do bool evaluation on a byte value
     *
     * @param v the value to be evaluated
     * @return {@code true} if the value != 0
     */
    public static boolean bool(byte v) {
        return 0 != v;
    }

    /**
     * Do bool evaluation on a char value
     *
     * @param v the value to be evaluated
     * @return {@code true} if the value != 0
     */
    public static boolean bool(char v) {
        return 0 != v;
    }

    /**
     * Do bool evaluation on a int value
     *
     * @param v the value to be evaluated
     * @return {@code true} if the value != 0
     */
    public static boolean bool(int v) {
        return 0 != v;
    }

    public static boolean bool(IntSupplier supplier) {
        if (null == supplier) {
            return false;
        }
        return bool(supplier.getAsInt());
    }

    /**
     * Do bool evaluation on a long value
     *
     * @param v the value to be evaluated
     * @return {@code true} if the value != 0
     */
    public static boolean bool(long v) {
        return 0L != v;
    }

    public static boolean bool(LongSupplier supplier) {
        if (null == supplier) {
            return false;
        }
        return bool(supplier.getAsLong());
    }

    /**
     * Do bool evaluation on a float value
     *
     * @param v the value to be evaluated
     * @return {@code true} if {@code Math.abs(v) > Float.MIN_NORMAL}
     */
    public static boolean bool(float v) {
        return Math.abs(v) > Float.MIN_NORMAL;
    }

    /**
     * Do bool evaluation on a double value
     *
     * @param v the value to be evaluated
     * @return {@code true} if {@code Math.abs(v) > Double.MIN_NORMAL}
     */
    public static boolean bool(double v) {
        return Math.abs(v) > Double.MIN_NORMAL;
    }

    /**
     * Do boolean evaluation on a double supplier
     *
     * @param supplier the function provides the double value to be evaluated.
     * @return `true` if the supplied value is evaluated to `true`
     * @see #bool(double)
     */
    public static boolean bool(DoubleSupplier supplier) {
        if (null == supplier) {
            return false;
        }
        return bool(supplier.getAsDouble());
    }

    /**
     * Do bool evaluation on a BigDecimal value.
     *
     * @param v the value to be evaluated
     * @return {@code true} if {@code !BigDecimal.ZERO.equals(v)}
     */
    public static boolean bool(BigDecimal v) {
        return null != v && !BigDecimal.ZERO.equals(v);
    }

    /**
     * Do bool evaluation on a BigInteger value.
     *
     * @param v the value to be evaluated
     * @return {@code true} if {@code !BigInteger.ZERO.equals(v)}
     */
    public static boolean bool(BigInteger v) {
        return null != v && !BigInteger.ZERO.equals(v);
    }

    /**
     * Do bool evaluation on a {@link File} instance.
     *
     * @param v the file to be evaluated
     * @return {@code true} if {@code v.exists()}
     */
    public static boolean bool(File v) {
        return null != v && v.exists();
    }

    /**
     * Do bool evaluation on a {@link Path} value.
     *
     * @param v the value to be evaluated
     * @return `true` if the resource represented by path `v` exists
     */
    public static boolean bool(Path v) {
        return null != v && bool(v.toFile());
    }

    /**
     * Do bool evaluation on an {@link Supplier} instance.
     *
     * This will call the {@link Supplier#get()} method and continue
     * to do bool evaluation on the return value
     *
     * @param v the function to be evaluated
     * @return {@code bool(v.apply())}
     */
    public static boolean bool(Supplier<?> v) {
        if (null == v) {
            return false;
        }
        return bool(v.get());
    }

    public static <T> ObjectRequire<T> require(T target) {
        return new ObjectRequire<>(target);
    }

    /**
     * Returns the `target` object if it is not `null` or
     * raise a {@link NullPointerException} if the `target`
     * object is `null`.
     *
     * @param target
     *      The target object to be ensured.
     * @param <T>
     *      The type of the target object.
     * @return
     *      The target object if it is not `null`
     * @throws NullPointerException
     *      if the target object is `null`.
     */
    public static <T> T requireNotNull(T target) {
        if (null == target) {
            throw new NullPointerException();
        }
        return target;
    }

    /**
     * Alias of {@link #deepEquals(Object, Object)}.
     *
     * @param a
     *         the first object
     * @param b
     *         the second object
     * @return
     *         `true` if the first object equals to the second object
     */
    public static boolean deq(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (null == a || null == b) {
            return false;
        }
        Class<?> ca = a.getClass();
        if (!ca.isArray()) {
            return a.equals(b);
        }
        Class<?> cb = b.getClass();
        if (ca != cb) return false;
        if (ca == boolean[].class) {
            return Arrays.equals((boolean[]) a, (boolean[]) b);
        } else if (ca == byte[].class) {
            return Arrays.equals((byte[]) a, (byte[]) b);
        } else if (ca == int[].class) {
            return Arrays.equals((int[]) a, (int[]) b);
        } else if (ca == char[].class) {
            return Arrays.equals((char[]) a, (char[]) b);
        } else if (ca == long[].class) {
            return Arrays.equals((long[]) a, (long[]) b);
        } else if (ca == float[].class) {
            return Arrays.equals((float[]) a, (float[]) b);
        } else if (ca == double[].class) {
            return Arrays.equals((double[]) a, (double[]) b);
        } else if (ca == short[].class) {
            return Arrays.equals((short[]) a, (short[]) b);
        } else {
            return Arrays.deepEquals((Object[]) a, (Object[]) b);
        }
    }

    /**
     * Check if two objects are equals to each other. The comparison will do
     * array deep equal matching if needed.
     *
     * @param a
     *         the first object
     * @param b
     *         the second object
     * @return
     *         `true` if the first object equals to the second object
     */
    public static boolean deepEquals(Object a, Object b) {
        return deq(a, b);
    }

    /**
     * Alias of {@link #equals(Object, Object)}.
     *
     * @param a
     *         the first object
     * @param b
     *         the second object
     * @return
     *         `true` if a equals b or `false` otherwise
     * @see #ne(Object, Object)
     */
    public static boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Check if two objects are equal to each other.
     *
     * The equality evaluation logic follows the standard
     * java {@link Object#equals(Object)} contract.
     *
     * @param a
     *         the first object
     * @param b
     *         the second object
     * @return
     *         `true` if `a == b`
     * @see #ne(Object, Object)
     */
    public static boolean equals(Object a, Object b) {
        return eq(a, b);
    }

    /**
     * Alias of {@link #notEquals(Object, Object)}.
     *
     * @param a
     *         the first object
     * @param b
     *         the second object
     * @return
     *         `false` if `a` equals to `b` or `true` otherwise
     * @see #eq(Object, Object)
     */
    public static boolean ne(Object a, Object b) {
        return !eq(a, b);
    }

    /**
     * Check if two objects are not equal to each other.
     *
     * The equality evaluation logic follows the standard
     * java {@link Object#equals(Object)} contract.
     *
     * @param a
     *         the first object
     * @param b
     *         the second object
     * @return
     *         `false` if `a` equals to `b` or `true` otherwise
     * @see #equals(Object, Object)
     */
    public static boolean notEquals(Object a, Object b) {
        return ne(a, b);
    }

    /**
     * The magic number used as base to
     * calculate object hash code.
     */
    static final int HC_INIT = 17;

    /**
     * The magic number used as a factor
     * to calculate object hash code.
     */
    static final int HC_FACT = 37;

    /**
     * Alias of {@link #hc(Iterable)}.
     *
     * @param iterable
     *      The iterable
     * @return hash code of the iterable
     */
    public static int hc(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return iterable.hashCode();
        }
        int ret = HC_INIT;
        for (Object v : iterable) {
            ret = ret * HC_FACT + hc(v);
        }
        return ret;
    }

    /**
     * Calculate hash code of an Iterable.
     *
     * Note This method will loop through the iterable
     * to get hash code of each element inside.
     *
     * @param iterable
     *      The iterable
     * @return hash code of the iterable
     */
    public static int hashCode(Iterable<?> iterable) {
        return hc(iterable);
    }

    /**
     * Alias of {@link #hc(boolean)}.
     *
     * @param v the boolean value
     * @return hash code of `v`
     */
    public static int hc(boolean v) {
        return Boolean.hashCode(v);
    }

    /**
     * Calculate hash code of a boolean value.
     *
     * This method will call {@link Boolean#hashCode(boolean)} to
     * return the hash code of the boolean value supplied.
     *
     * @param v the boolean value
     * @return hash code of `v`
     */
    public static int hashCode(boolean v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(boolean)}.
     *
     * @param a the boolean array
     * @return hash code of `a`
     */
    public static int hc(boolean[] a) {
        int ret = HC_INIT;
        for (boolean b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate hash code of a boolean array.
     *
     * @param a the boolean array
     * @return hash code of `a`
     */
    public static int hashCode(boolean[] a) {
        return hc(a);
    }

    /**
     * Alias of {@link #hashCode(byte)}.
     *
     * @param v a byte value
     * @return hash code of `v`
     */
    public static int hc(byte v) {
        return (int) v;
    }

    /**
     * Calculate hash code of a byte value.
     *
     * @param v the byte value
     * @return hash code of `v`
     */
    public static int hashCode(byte v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(byte[])}.
     *
     * @param a the byte array
     * @return hash code of `a`
     */
    public static int hc(byte[] a) {
        int ret = HC_INIT;
        for (byte b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate the hash code of a byte array.
     *
     * @param a a byte array
     * @return hash code of `a`
     */
    public static int hashCode(byte[] a) {
        return hc(a);
    }

    /**
     * Alias of {@link #hashCode(short)}.
     *
     * @param v a short value
     * @return hash code of `v`
     */
    public static int hc(short v) {
        return (int) v;
    }

    /**
     * Calculate hash code of a short value.
     *
     * @param v the short value
     * @return hash code of `v`
     */
    public static int hashCode(short v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(short[])}.
     *
     * @param a the short array
     * @return hash code of `a`
     */
    public static int hc(short[] a) {
        int ret = HC_INIT;
        for (short b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate the hash code of a short array.
     *
     * @param a a short array
     * @return hash code of `a`
     */
    public static int hashCode(short[] a) {
        return hc(a);
    }


    /**
     * Alias of {@link #hashCode(char)}.
     *
     * @param v the char value
     * @return hash code of `v`
     */
    public static int hc(char v) {
        return (int) v;
    }

    /**
     * Calculate hash code of a char value.
     *
     * @param v a char value
     * @return hash code of `v`
     */
    public static int hashCode(char v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(char[])}.
     *
     * @param a the char array
     * @return hash code of `a`
     */
    public static int hc(char[] a) {
        int ret = HC_INIT;
        for (char b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate the hash code of a char array.
     *
     * @param a a char array
     * @return hash code of `a`
     */
    public static int hashCode(char[] a) {
        return hc(a);
    }

    /**
     * Alias of {@link #hashCode(int)}.
     *
     * @param v a int value
     * @return hash code of `v`
     */
    public static int hc(int v) {
        return v;
    }

    /**
     * Calculate hash code of an int value.
     *
     * @param v the int value
     * @return hash code of `v`
     */
    public static int hashCode(int v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(int[])}.
     *
     * @param a the int array
     * @return hash code of `a`
     */
    public static int hc(int[] a) {
        int ret = HC_INIT;
        for (int b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate the hash code of a int array.
     *
     * @param a a int array
     * @return hash code of `a`
     */
    public static int hashCode(int[] a) {
        return hc(a);
    }


    /**
     * Alias of {@link #hashCode(float)}.
     *
     * @param v a float value
     * @return hash code of `v`
     */
    public static int hc(float v) {
        return Float.floatToIntBits(v);
    }

    /**
     * Calculate hash code of a float value.
     *
     * @param v the float value
     * @return hash code of `v`
     */
    public static int hashCode(float v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(float[])}.
     *
     * @param a the float array
     * @return hash code of `a`
     */
    public static int hc(float[] a) {
        int ret = HC_INIT;
        for (float b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate the hash code of a float array.
     *
     * @param a a float array
     * @return hash code of `a`
     */
    public static int hashCode(float[] a) {
        return hc(a);
    }

    /**
     * Alias of {@link #hashCode(long)}.
     *
     * @param v a long value
     * @return hash code of `v`
     */
    public static int hc(long v) {
        return (int) (v ^ (v >> 32));
    }

    /**
     * Calculate hash code of a long value.
     *
     * @param v the long value
     * @return hash code of `v`
     */
    public static int hashCode(long v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(long[])}.
     *
     * @param a the long array
     * @return hash code of `a`
     */
    public static int hc(long[] a) {
        int ret = HC_INIT;
        for (long b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate the hash code of a long array.
     *
     * @param a a long array
     * @return hash code of `a`
     */
    public static int hashCode(long[] a) {
        return hc(a);
    }

    /**
     * Alias of {@link #hashCode(double)}.
     *
     * @param v a double value
     * @return hash code of `v`
     */
    public static int hc(double v) {
        return hc(Double.doubleToLongBits(v));
    }

    /**
     * Calculate hash code of a double value.
     *
     * @param v the double value
     * @return hash code of `v`
     */
    public static int hashCode(double v) {
        return hc(v);
    }

    /**
     * Calculate hashcode of double array specified.
     *
     * @param a the double array
     * @return the hash code
     */
    public static int hc(double[] a) {
        int ret = HC_INIT;
        for (double b : a) {
            ret = ret * HC_FACT + hc(b);
        }
        return ret;
    }

    /**
     * Calculate the hash code of a double array.
     *
     * @param a a double array
     * @return hash code of `a`
     */
    public static int hashCode(double[] a) {
        return hc(a);
    }

    /**
     * Alias of {@link #hashCode(Object)}.
     *
     * @param v object on which hash code to be calculated
     * @return the calculated hash code
     */
    public static int hc(Object v) {
        return hc_(v);
    }

    /**
     * Calculate hash code of object specified.
     *
     * @param v object on which hash code to be calculated
     * @return the calculated hash code
     */
    public static int hashCode(Object v) {
        return hc(v);
    }

    /**
     * Alias of {@link #hashCode(Object, Object)}.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @return the calculated hash code
     */
    public static int hc(Object o1, Object o2) {
        int i = HC_INIT;
        i = HC_FACT * i + hc_(o1);
        i = HC_FACT * i + hc_(o2);
        return i;
    }

    /**
     * Calculate hashcode of objects specified.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @return the calculated hash code
     */
    public static int hashCode(Object o1, Object o2) {
        return hc(o1, o2);
    }

    /**
     * Alias of {@link #hashCode(Object, Object, Object)}.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @param o3 object 3
     * @return the calculated hash code
     */
    public static int hc(Object o1, Object o2, Object o3) {
        int i = HC_INIT;
        i = HC_FACT * i + hc_(o1);
        i = HC_FACT * i + hc_(o2);
        i = HC_FACT * i + hc_(o3);
        return i;
    }

    /**
     * Calculate hashcode of objects specified.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @param o3 object 3
     * @return the calculated hash code
     */
    public static int hashCode(Object o1, Object o2, Object o3) {
        return hc(o1, o2, o3);
    }

    /**
     * Alias of {@link #hashCode(Object, Object, Object, Object)}.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @param o3 object 3
     * @param o4 object 4
     * @return the calculated hash code
     */
    public static int hc(Object o1, Object o2, Object o3, Object o4) {
        int i = HC_INIT;
        i = HC_FACT * i + hc_(o1);
        i = HC_FACT * i + hc_(o2);
        i = HC_FACT * i + hc_(o3);
        i = HC_FACT * i + hc_(o4);
        return i;
    }

    /**
     * Alias of {@link #hashCode(Object, Object, Object, Object, Object)}.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @param o3 object 3
     * @param o4 object 4
     * @return the calculated hash code
     */
    public static int hashCode(Object o1, Object o2, Object o3, Object o4) {
        return hc(o1, o2, o3, o4);
    }

    /**
     * Calculate hashcode of objects specified.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @param o3 object 3
     * @param o4 object 4
     * @param o5 object 5
     * @return the calculated hash code
     */
    public static int hc(Object o1, Object o2, Object o3, Object o4, Object o5) {
        int i = HC_INIT;
        i = HC_FACT * i + hc_(o1);
        i = HC_FACT * i + hc_(o2);
        i = HC_FACT * i + hc_(o3);
        i = HC_FACT * i + hc_(o4);
        i = HC_FACT * i + hc_(o5);
        return i;
    }

    /**
     * Calculate hashcode of objects specified.
     *
     * @param o1 object 1
     * @param o2 object 2
     * @param o3 object 3
     * @param o4 object 4
     * @param o5 object 5
     * @return the calculated hash code
     */
    public static int hashCode(Object o1, Object o2, Object o3, Object o4, Object o5) {
        return hc(o1, o2, o3, o4, o5);
    }

    /**
     * Alias of {@link #hashCode(Object, Object, Object, Object, Object, Object...)}
     *
     * @param o1   object 1
     * @param o2   object 2
     * @param o3   object 3
     * @param o4   object 4
     * @param o5   object 5
     * @param args other objects
     * @return the calculated hash code
     */
    public static int hc(Object o1, Object o2, Object o3, Object o4, Object o5, Object... args) {
        int i = hc(o1, o2, o3, o4, o5);
        for (Object v : args) {
            i = HC_FACT * i + hc(v);
        }
        return i;
    }

    /**
     * Calculate hashcode of objects specified.
     *
     * @param o1   object 1
     * @param o2   object 2
     * @param o3   object 3
     * @param o4   object 4
     * @param o5   object 5
     * @param others other objects
     * @return the calculated hash code
     */
    public static int hashCode(Object o1, Object o2, Object o3, Object o4, Object o5, Object... others) {
        return hc(o1, o2, o3, o4, o5, others);
    }

    private static int hc_(Object v) {
        if (null == v) {
            return 0;
        }
        Class c = v.getClass();
        if (c.isArray()) {
            if (c == int[].class) {
                return hc((int[]) v);
            } else if (c == long[].class) {
                return hc((long[]) v);
            } else if (c == char[].class) {
                return hc((char[]) v);
            } else if (c == byte[].class) {
                return hc((byte[]) v);
            } else if (c == double[].class) {
                return hc((double[]) v);
            } else if (c == float[].class) {
                return hc((float[]) v);
            } else if (c == short[].class) {
                return hc((short[]) v);
            } else if (c == boolean[].class) {
                return hc((boolean[]) v);
            }
            int len = Array.getLength(v);
            int hc = HC_INIT;
            for (int i = 0; i < len; ++i) {
                hc = HC_FACT * hc + hc_(Array.get(v, i));
            }
            return hc;
        } else {
            return v.hashCode();
        }
    }


    public static <T> ObjectPredicate<T> is(T target) {
        return new ObjectPredicate<>(target);
    }

    /**
     * Alias of {@link Option#none()}.
     *
     * @param <T>
     *      The option value type.
     * @return
     *      {@link Option#NONE}.
     * @see #any(Object)
     * @see #some(Object)
     */
    public static <T> Option<T> none() {
        return Option.none();
    }

    /**
     * Test if the `target` object is `null`
     *
     * @param target
     *      the object to be tested
     * @param <T>
     *      the type of the object
     * @return
     *      `true` if the target object is not `null` or `false` otherwise.
     */
    public static <T> boolean notNull(T target) {
        return null != target;
    }

    /**
     * Alias of {@link Option#ofNullable(Object)}.
     *
     * @param value
     *      The value.
     * @param <T>
     *      The type of the value.
     * @return
     *      {@link Option#NONE} if the value is `null` or
     *      a {@link Option.Some} that contains the non-null value.
     * @see #none()
     * @see #some(Object)
     */
    public static <T> Option<T> any(T value) {
        return Option.ofNullable(value);
    }

    /**
     * Alias of {@link Option#some(Object)}.
     *
     * @param value
     *      a non-null value
     * @param <T>
     *      The type of the value
     * @return
     *      a {@link Option.Some} instance contains the value
     * @throws NullPointerException
     *      if the `value` specified is `null`
     * @see #any(Object)
     * @see #none()
     */
    public static <T> Option<T> some(T value) {
        return Option.some(value);
    }

    /**
     * Alias of {@link T2#of(Object, Object)}.
     *
     * @param a
     *     Param to be assigned to the first element
     * @param b
     *     Param to be assigned to the second element
     * @param <A>
     *     The type of the first element
     * @param <B>
     *     The type of the second element
     * @return
     *      A `Binary` instance.
     */
    public static <A, B> Pair<A, B> T2(A a, B b) {
        return T2.of(a, b);
    }

    /**
     * Alias of {@link T3#of(Object, Object, Object)}.
     *
     * @param a
     *     Param to be assigned to the first element
     * @param b
     *     Param to be assigned to the second element
     * @param b
     *     Param to be assigned to the third element
     * @param <A>
     *     The type of the first element
     * @param <B>
     *     The type of the second element
     * @param <B>
     *     The type of the third element
     * @return
     *      A `Triple` instance.
     */
    public static <A, B, C> Triple<A, B, C> T3(A a, B b, C c) {
        return T3.of(a, b, c);
    }

}
