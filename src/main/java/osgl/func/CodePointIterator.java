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

import osgl.$;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

class CodePointIterator implements PrimitiveIterator.OfInt {

    private int cur = 0;
    private final CharSequence charSequence;

    CodePointIterator(CharSequence charSequence) {
        this.charSequence = $.requireNotNull(charSequence);
    }

    public boolean hasNext() {
        return cur < charSequence.length();
    }

    public int nextInt() {
        if (hasNext()) {
            return Character.codePointAt(charSequence, cur++);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void forEachRemaining(IntConsumer block) {
        int len = charSequence.length();
        for (; cur < len; cur++) {
            block.accept(Character.codePointAt(charSequence, cur++));
        }
    }


    static CodePointIterator of(CharSequence charSequence) {
        return new CodePointIterator(charSequence);
    }
}
