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

import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

/**
 * Base class for {@link Mojo}s that set properties to the version of dependencies (useful if you need to filter a
 * transitive dependency's version into some resource).
 */
public abstract class AbstractDependencyVersionMojo extends AbstractMojo {
    /**
     * The properties to set. Each property {@code name} must also specify the {@code artifactId} to match. If the
     * dependency is unique that will suffice, if there are multiple dependencies with the same {@code artifactId} then
     * you will also need to specify the {@code groupId}
     */
    @Parameter
    protected List<Property> properties;
    /**
     * If the resolved version is {@code null}  should we fail the build.
     */
    @Parameter(property = "failOnMissingVersion", defaultValue = "true")
    protected boolean failOnMissingVersion;
    /**
     * Favour the {@link Artifact#getSelectedVersion()} rather than {@link Artifact#getVersion()}.
     */
    @Parameter(property = "useSelectedVersion", defaultValue = "true")
    protected boolean useSelectedVersion;
    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    protected void setProperty(String propertyName, String propertyValue) {
        if (StringUtils.isNotBlank(propertyName)) {
            getLog().info("Setting property '" + propertyName + "' to '" + propertyValue + "'");
            project.getProperties().setProperty(propertyName, propertyValue);
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        for (Property p : properties) {
            if (StringUtils.isBlank(p.getName())) {
                throw new MojoExecutionException("Missing property name in " + p);
            }
            if (StringUtils.isBlank(p.getArtifactId())) {
                throw new MojoExecutionException("Missing artifactId selector for property " + p.getName());
            }
            Artifact target = null;
            for (Artifact artifact : project.getArtifacts()) {
                if (!p.matches(artifact)) {
                    continue;
                }
                if (target == null) {
                    target = artifact;
                } else if (!target.getGroupId().equals(artifact.getGroupId())) {
                    // you could have multiple dependencies differing by classifier / type. Maven will force all to
                    // have the same version so only fail if the groupIds differ.
                    throw new MojoFailureException(
                            "Multiple artifacts match for property " + p.getName() + " at least " + target.getGroupId()
                                    + " and " + artifact.getGroupId()
                                    + " match. Specify the correct groupId for the property.");
                }
            }
            if (target == null) {
                throw new MojoFailureException(
                        "No artifacts match " + (StringUtils.isBlank(p.getGroupId()) ? "*" : p.getGroupId()) + ":" + p
                                .getArtifactId() + " for property " + p.getName());
            }
            String version;
            if (useSelectedVersion) {
                try {
                    version = target.getSelectedVersion().toString();
                } catch (OverConstrainedVersionException e) {
                    if (failOnMissingVersion) {
                        throw new MojoFailureException(
                                "Could not determine version of " + target.getGroupId() + ":" + target.getArtifactId()
                                        + " for property " + p.getName()
                        );
                    }
                    getLog().warn(e);
                    continue;
                }
                if (StringUtils.isBlank(version)) {
                    // missing or a version range
                    if (failOnMissingVersion) {
                        throw new MojoFailureException(
                                "Could not determine version of " + target.getGroupId() + ":" + target.getArtifactId()
                                        + " for property " + p.getName()
                        );
                    }
                }
            } else {
                version = target.getVersion();
                if (StringUtils.isBlank(version)) {
                    // missing or a version range
                    if (failOnMissingVersion) {
                        throw new MojoFailureException(
                                "Could not determine version of " + target.getGroupId() + ":" + target.getArtifactId()
                                        + " for property " + p.getName()
                        );
                    }
                } else if (version.contains(",") || version.matches("[(\\[].*[)\\]]")) {
                    // missing or a version range
                    if (failOnMissingVersion) {
                        throw new MojoFailureException(
                                "Could not determine version of " + target.getGroupId() + ":" + target.getArtifactId()
                                        + " from specification " + version + " for property " + p.getName()
                        );
                    }
                }
            }
            setProperty(p.getName(), version);
        }
    }
}
