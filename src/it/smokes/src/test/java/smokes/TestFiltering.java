/*
 * Copyright 2021 Stephen Connolly.
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smokes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TestFiltering {
    @Test
    void given__transitive_dependency_from_jackson__when__filtering_resources__then__snakeyaml_version_injected()
            throws IOException {
        Properties properties = new Properties();
        InputStream is = getClass().getResourceAsStream("/transitive.properties");
        try {
            properties.load(is);
        } finally {
            is.close();
        }
        assertThat(properties.getProperty("snakeyaml"), is("1.26"));
    }
}
