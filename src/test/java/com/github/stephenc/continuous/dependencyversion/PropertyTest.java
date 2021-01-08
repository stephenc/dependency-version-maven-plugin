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

package com.github.stephenc.continuous.dependencyversion;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PropertyTest {

    @Test
    void matches() {
        DefaultArtifact artifact =
                new DefaultArtifact("foo", "bar", "1.0", "test", "pom", null, new DefaultArtifactHandler());
        Property instance = new Property();
        instance.setArtifactId("bar");
        assertThat(instance.matches(artifact), is(true));
        instance.setGroupId("foo");
        assertThat(instance.matches(artifact), is(true));
        instance.setGroupId("foo2");
        assertThat(instance.matches(artifact), is(false));
        instance.setGroupId(null);
        assertThat(instance.matches(artifact), is(true));
        instance.setArtifactId("manchu");
        assertThat(instance.matches(artifact), is(false));
        instance.setGroupId("foo");
        assertThat(instance.matches(artifact), is(false));
    }

}
