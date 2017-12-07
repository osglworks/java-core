package osgl.collection;

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
 * `Triple` is an alias of {@link T3}
 *
 * @param <A>
 *     Type of the first element
 * @param <B>
 *     Type of the second element
 * @param <C>
 *     Type of the third element
 */
public class Triple<A, B, C> extends T3<A, B, C> {
    public Triple(A a, B b, C c) {
        super(a, b, c);
    }
}

