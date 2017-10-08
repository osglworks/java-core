/**
 * The package contains classes/utilities relevant to functional programming.
 *
 * OSGL build up it's functional programming support around a set of interfaces named `ProcX` and `FuncX` where
 * `X` is a digital range from `0` to `5`, indicating the number of parameters the `Proc` or `Func` takes.
 *
 * ### Procedure and Function
 *
 * The term `Proc` and `Func` is picked to model the concept of <a target="_blank" href="http://www.differencebetween.info/difference-between-function-and-procedure">procedure and function</a>. The main differences between procedure and function is that the former does not have return value while function does.
 *
 * The naming convention of procedure and functions:
 *
 * * `{@link osgl.func.Proc0 Proc0}` and `{@link osgl.func.Func0 Func0}` - takes no parameter
 * * `Proc1` and `Func1` - takes one parameter
 * * `Proc2` and `Func2` - takes two parameters
 * * ...
 *
 * The logic of `ProcX` is implemented in `run` method; the logic of `FuncX` is implemented in `apply` method.
 *
 * ### Interactive with JDK utilities
 *
 * * `Proc0` extends `Runnable`
 * * `Func0` extends `Supplier`
 * * `BooleanFunc0` extends `BooleanSupplier`
 * * `DoubleFunc0` extends `DoubleSupplier`
 * * `IntFunc0` extends `IntSupplier`
 * * `LongFunc0` extends `LongSupplier`
 * * `Proc1` extends JDK 8 `Consumer`
 * * `Func1` extends JDK 8 `Function`
 * * ...
 *
 * ### Convert between Procedure and Function
 *
 * * `ProcX` has `toFunction()` method to convert the procedure to function in a way that return `null` after calling `ProcX.run(...)` method.
 * * `FuncX` has `toProcedure()` method to convert the function to procedure by calling `FuncX.apply(...)` method and ignore the return value.
 *
 * ### Breakout
 *
 * The `breakOut()` and `breakOut(Object payload)` utility methods can be used by function implementation to break out a loop from inside function logic. For example,
 *
 * ```java
 * final C.List<Integer> lists = C.List(1,2,3,4,5);
 * final Var<Integer> var = $.var();
 * list.forEach((n) -> {
 * if (n > 3 && n % 2 > 0) {
 * var.set(n);
 * breakOut();
 * }
 * });
 * ```
 *
 */
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

