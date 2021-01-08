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

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Sets properties to the resolved version of based on dependencies inclusive of those dependencies with scope
 * {@code compile} or {@code runtime}.
 */
@Mojo(
        name = "set",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES, // available before generate-resources
        requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME // forces resolution of compile/runtime poms
)
public class RuntimeDependencyVersionMojo extends AbstractDependencyVersionMojo{
}
