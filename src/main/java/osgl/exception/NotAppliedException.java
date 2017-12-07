package osgl.exception;

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


/**
 * Used by function object to indicate that it doesn't apply to the parameter
 * specified. This exception extends {@link FastRuntimeException}
 * thus performs much better than normal RuntimeExceptions. Here is one
 * example of using `NotAppliedException`:
 *
 * ```java
 * Func2<Integer, Integer, Integer> divide1 = new Func2<Integer, Integer, Integer>() {
 *     @Override
 *     public int apply(int n, int d) {
 *         if (d == 0) return NotAppliedException();
 *         return n/d;
 *     }
 * }
 *
 * In the above example thrown out {@code NotAppliedException} when divider is zero is
 * faster than do the calculation directly and let Java thrown out
 */
public class NotAppliedException extends FastRuntimeException {
}
