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
 * Adapt a {@link Proc0} instance to {@link Func0} type.
 *
 * @param <R>
 *     The type of the return value when {@link Func0#apply()} is called
 */
public class Proc0ToFunc0Adaptor<R> implements Func0<R> {

    private Proc0 proc;

    /**
     * Construct the adaptor with a non-null {@link Proc0} instance.
     *
     * @param proc
     *      the procedure instance
     * @throws NullPointerException
     *      if the `proc` instance is `null`
     */
    public Proc0ToFunc0Adaptor(Proc0 proc) {
        Objects.requireNonNull(proc);
        this.proc = proc;
    }

    /**
     * Implement the {@link Func0#apply()} by calling {@link Proc0#run()} on
     * the `proc` instance, and return `null`.
     *
     * @return
     *      `null`
     */
    @Override
    public R apply() {
        proc.run();
        return null;
    }
}
