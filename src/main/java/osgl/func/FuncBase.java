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

/**
 * The base for all functions defined in OSGL library.
 *
 * The `FuncBase` provides default methods used by all functions
 */
interface FuncBase {

    /**
     * throw out a {@link Break} instance.
     */
    default void breakOut() {
        FuncUtil.breakOut();
    }

    /**
     * Throw out a {@link Break} instance with payload
     * object specified.
     *
     * @param payload
     *      the payload
     */
    default void breakOut(Object payload) {
        FuncUtil.breakOut(payload);
    }
}
