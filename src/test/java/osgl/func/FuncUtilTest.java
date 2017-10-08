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
import java.util.function.Supplier;

public class FuncUtilTest extends TestBase {

    @Test
    public void f0ShallConvertSupplierToFunc0() {
        Supplier<Integer> supplier = () -> 5;
        Func0<Integer> func0 = FuncUtil.f0(supplier);
        eq(supplier.get(), func0.apply());
    }

    @Test
    public void f0ShallReturnSupplierDirectlyIfItIsAFunc0() {
        Func0<Integer> supplier = () -> 5;
        Func0<Integer> func0 = FuncUtil.f0(supplier);
        same(supplier, func0);
    }

    @Test
    public void f1ShallConvertFunctionToFunc1() {
        Function<Integer, Integer> function = (n) -> n * 2;
        Func1<Integer, Integer> func1 = FuncUtil.f1(function);
        eq(function.apply(5), func1.apply(5));
    }

    @Test
    public void f1ShallReturnFunctionDirectlyIfItIsAFunc1() {
        Func1<Integer, Integer> function = (n) -> n * 2;
        Func1<Integer, Integer> func1 = FuncUtil.f1(function);
        same(function, func1);
    }

}
