package osgl;

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

import org.junit.Test;
import osgl.ut.TestBase;

public class StringsTest extends TestBase {

    @Test
    public void nullStringIsEmpty() {
        String s = null;
        yes(Strings.empty(s));
        yes(Strings.isEmpty(s));
        no(Strings.notEmpty(s));
    }

    @Test
    public void stringWithNoContentIsEmpty() {
        String s = "";
        yes(Strings.empty(s));
        yes(Strings.isEmpty(s));
        no(Strings.notEmpty(s));
    }

    @Test
    public void stringWithContentIsNotEmpty() {
        String s = " ";
        no(Strings.empty(s));
        no(Strings.isEmpty(s));
        yes(Strings.notEmpty(s));
    }

}
