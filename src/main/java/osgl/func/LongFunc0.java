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

import java.util.function.LongSupplier;

/**
 * An interface that bridge OSGL {@link Func0}`<Long>` and
 * JDK {@link LongSupplier}.
 */
@FunctionalInterface
public interface LongFunc0 extends Func0<Long>, LongSupplier {

    /**
     * When applied call the {@link #getAsLong()} ()} method.
     *
     * @return
     *      a result
     */
    @Override
    default Long apply() {
        return getAsLong();
    }

}
