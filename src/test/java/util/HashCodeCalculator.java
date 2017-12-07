package util;

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

import osgl.$;

import java.lang.reflect.Array;

public class HashCodeCalculator {

    private final int hcInit;
    private final int hcFact;

    public HashCodeCalculator(int hcInit, int hcFact) {
        this.hcInit = hcInit;
        this.hcFact = hcFact;
    }

    public int hashCode(Object array) {
        int len = Array.getLength(array);
        int ret = hcInit;
        for (int i = 0; i < len; ++i) {
            ret = ret * hcFact + $.hc(Array.get(array, i));
        }
        return ret;
    }

    public int hashCode(Iterable<?> iterable) {
        int ret = hcInit;
        for (Object v : iterable) {
            ret = ret * hcFact + $.hc(v);
        }
        return ret;
    }

}
