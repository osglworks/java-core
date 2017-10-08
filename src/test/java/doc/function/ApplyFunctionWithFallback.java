package doc.function;

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

import osgl.func.Func1;

public class ApplyFunctionWithFallback {
    public static void main(String[] args) {
        Func1<String, Integer> getStringLength = (s) -> s.length();
        String s1 = "foo", s2 = null;
        int fallbackValue = 0;
        Func1<String, Integer> fallbackFunc = Func1.constant(fallbackValue);

        System.out.println(getStringLength.applyOrElse(s1, fallbackValue)); // print 3
        System.out.println(getStringLength.applyOrElse(s2, fallbackValue)); // print 0
        System.out.println(getStringLength.orElse(fallbackValue).apply(s1)); // print 3
        System.out.println(getStringLength.orElse(fallbackValue).apply(s2)); // print 0

        System.out.println(getStringLength.applyOrElse(s1, fallbackFunc)); // print 3
        System.out.println(getStringLength.applyOrElse(s2, fallbackFunc)); // print 0
        System.out.println(getStringLength.orElse(fallbackFunc).apply(s1)); // print 3
        System.out.println(getStringLength.orElse(fallbackFunc).apply(s2)); // print 0
    }
}
