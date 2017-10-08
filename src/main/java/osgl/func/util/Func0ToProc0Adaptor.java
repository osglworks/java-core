package osgl.func.util;

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

import osgl.func.Func0;
import osgl.func.Proc0;

import java.util.Objects;

/**
 * Adapt a {@link Func0} instance to {@link Proc0} type.
 */
public class Func0ToProc0Adaptor implements Proc0 {

    private Func0<?> func;

    /**
     * Construct the adaptor with a non-null {@link Func0} instance.
     *
     * @param func
     *      The function instance
     * @throws NullPointerException
     *      if the `func` instance is `null`
     */
    public Func0ToProc0Adaptor(Func0<?> func) {
        Objects.requireNonNull(func);
        this.func = func;
    }

    /**
     * Implement the {@link Proc0#run()} by calling
     * {@link Func0#apply()} method and discard the
     * return value.
     */
    @Override
    public void run() {
        func.apply();
    }

}
