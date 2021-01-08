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

import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.util.StringUtils;

/**
 * Details of the property we want to set to the version of a matching artifact.
 */
public class Property {
    private String name;
    private String artifactId;
    private String groupId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Property{");
        sb.append("name='").append(name).append('\'');
        if (StringUtils.isBlank(groupId)) {
            sb.append(", groupId=*");
        } else {
            sb.append(", groupId='").append(groupId).append('\'');
        }
        sb.append(", artifactId='").append(artifactId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean matches(Artifact artifact) {
        return artifact.getArtifactId().equals(artifactId) &&
                (StringUtils.isBlank(groupId) || artifact.getGroupId().equals(groupId));
    }
}
