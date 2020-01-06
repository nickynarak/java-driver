/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.api.core.config;

import com.datastax.dse.driver.api.core.config.DseDriverOption;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.datastax.oss.driver.api.core.session.SessionBuilder;
import com.datastax.oss.driver.internal.core.config.map.MapBasedDriverConfigLoader;
import com.datastax.oss.driver.internal.core.config.typesafe.DefaultDriverConfigLoader;
import com.datastax.oss.driver.internal.core.config.typesafe.DefaultProgrammaticDriverConfigLoaderBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the initialization, and optionally the periodic reloading, of the driver configuration.
 *
 * @see SessionBuilder#withConfigLoader(DriverConfigLoader)
 */
public interface DriverConfigLoader extends AutoCloseable {

  /**
   * Builds an instance using the driver's default implementation (based on Typesafe config), except
   * that application-specific options are loaded from a classpath resource with a custom name.
   *
   * <p>More precisely, configuration properties are loaded and merged from the following
   * (first-listed are higher priority):
   *
   * <ul>
   *   <li>system properties
   *   <li>{@code <resourceBaseName>.conf} (all resources on classpath with this name)
   *   <li>{@code <resourceBaseName>.json} (all resources on classpath with this name)
   *   <li>{@code <resourceBaseName>.properties} (all resources on classpath with this name)
   *   <li>{@code reference.conf} (all resources on classpath with this name). In particular, this
   *       will load the {@code reference.conf} included in the core driver JAR, that defines
   *       default options for all mandatory options.
   * </ul>
   *
   * The resulting configuration is expected to contain a {@code datastax-java-driver} section.
   *
   * <p>The returned loader will honor the reload interval defined by the option {@code
   * basic.config-reload-interval}.
   */
  @NonNull
  static DriverConfigLoader fromClasspath(@NonNull String resourceBaseName) {
    return new DefaultDriverConfigLoader(
        () -> {
          ConfigFactory.invalidateCaches();
          Config config =
              ConfigFactory.defaultOverrides()
                  .withFallback(ConfigFactory.parseResourcesAnySyntax(resourceBaseName))
                  .withFallback(ConfigFactory.defaultReference())
                  .resolve();
          return config.getConfig(DefaultDriverConfigLoader.DEFAULT_ROOT_PATH);
        });
  }

  /**
   * Builds an instance using the driver's default implementation (based on Typesafe config), except
   * that application-specific options are loaded from the given path.
   *
   * <p>More precisely, configuration properties are loaded and merged from the following
   * (first-listed are higher priority):
   *
   * <ul>
   *   <li>system properties
   *   <li>the contents of {@code file}
   *   <li>{@code reference.conf} (all resources on classpath with this name). In particular, this
   *       will load the {@code reference.conf} included in the core driver JAR, that defines
   *       default options for all mandatory options.
   * </ul>
   *
   * The resulting configuration is expected to contain a {@code datastax-java-driver} section.
   *
   * <p>The returned loader will honor the reload interval defined by the option {@code
   * basic.config-reload-interval}.
   */
  @NonNull
  static DriverConfigLoader fromPath(@NonNull Path file) {
    return fromFile(file.toFile());
  }

  /**
   * Builds an instance using the driver's default implementation (based on Typesafe config), except
   * that application-specific options are loaded from the given file.
   *
   * <p>More precisely, configuration properties are loaded and merged from the following
   * (first-listed are higher priority):
   *
   * <ul>
   *   <li>system properties
   *   <li>the contents of {@code file}
   *   <li>{@code reference.conf} (all resources on classpath with this name). In particular, this
   *       will load the {@code reference.conf} included in the core driver JAR, that defines
   *       default options for all mandatory options.
   * </ul>
   *
   * The resulting configuration is expected to contain a {@code datastax-java-driver} section.
   *
   * <p>The returned loader will honor the reload interval defined by the option {@code
   * basic.config-reload-interval}.
   */
  @NonNull
  static DriverConfigLoader fromFile(@NonNull File file) {
    return new DefaultDriverConfigLoader(
        () -> {
          ConfigFactory.invalidateCaches();
          Config config =
              ConfigFactory.defaultOverrides()
                  .withFallback(ConfigFactory.parseFileAnySyntax(file))
                  .withFallback(ConfigFactory.defaultReference())
                  .resolve();
          return config.getConfig(DefaultDriverConfigLoader.DEFAULT_ROOT_PATH);
        });
  }

  /**
   * Builds an instance using the driver's default implementation (based on Typesafe config), except
   * that application-specific options are loaded from the given URL.
   *
   * <p>More precisely, configuration properties are loaded and merged from the following
   * (first-listed are higher priority):
   *
   * <ul>
   *   <li>system properties
   *   <li>the contents of {@code url}
   *   <li>{@code reference.conf} (all resources on classpath with this name). In particular, this
   *       will load the {@code reference.conf} included in the core driver JAR, that defines
   *       default options for all mandatory options.
   * </ul>
   *
   * The resulting configuration is expected to contain a {@code datastax-java-driver} section.
   *
   * <p>The returned loader will honor the reload interval defined by the option {@code
   * basic.config-reload-interval}.
   */
  @NonNull
  static DriverConfigLoader fromUrl(@NonNull URL url) {
    return new DefaultDriverConfigLoader(
        () -> {
          ConfigFactory.invalidateCaches();
          Config config =
              ConfigFactory.defaultOverrides()
                  .withFallback(ConfigFactory.parseURL(url))
                  .withFallback(ConfigFactory.defaultReference())
                  .resolve();
          return config.getConfig(DefaultDriverConfigLoader.DEFAULT_ROOT_PATH);
        });
  }

  /**
   * Starts a builder that allows configuration options to be overridden programmatically.
   *
   * <p>For example:
   *
   * <pre>{@code
   * DriverConfigLoader loader =
   *     DriverConfigLoader.programmaticBuilder()
   *         .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(5))
   *         .startProfile("slow")
   *         .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30))
   *         .endProfile()
   *         .build();
   * }</pre>
   *
   * produces the same overrides as:
   *
   * <pre>
   * datastax-java-driver {
   *   basic.request.timeout = 5 seconds
   *   profiles {
   *     slow {
   *       basic.request.timeout = 30 seconds
   *     }
   *   }
   * }
   * </pre>
   *
   * The resulting loader still uses the driver's default implementation (based on Typesafe config),
   * except that the programmatic configuration takes precedence. More precisely, configuration
   * properties are loaded and merged from the following (first-listed are higher priority):
   *
   * <ul>
   *   <li>system properties
   *   <li>properties that were provided programmatically
   *   <li>{@code application.conf} (all resources on classpath with this name)
   *   <li>{@code application.json} (all resources on classpath with this name)
   *   <li>{@code application.properties} (all resources on classpath with this name)
   *   <li>{@code reference.conf} (all resources on classpath with this name). In particular, this
   *       will load the {@code reference.conf} included in the core driver JAR, that defines
   *       default options for all mandatory options.
   * </ul>
   *
   * Note that {@code application.*} is entirely optional, you may choose to only rely on the
   * driver's built-in {@code reference.conf} and programmatic overrides.
   *
   * <p>The resulting configuration is expected to contain a {@code datastax-java-driver} section.
   *
   * <p>The loader will honor the reload interval defined by the option {@code
   * basic.config-reload-interval}.
   *
   * <p>Note that the returned builder is <b>not thread-safe</b>.
   */
  @NonNull
  static ProgrammaticDriverConfigLoaderBuilder programmaticBuilder() {
    return new DefaultProgrammaticDriverConfigLoaderBuilder();
  }

  /**
   * Builds an instance backed by a Java map.
   *
   * <p>This is the simplest implementation. It is intended for clients who wish to manage all the
   * configuration in memory. It does not depend on Typesafe config, and does not load from any
   * file; in particular, the driver's built-in {@code reference.conf} file is ignored, the caller
   * must explicitly provide all mandatory options.
   *
   * <p>The provided map contains execution profiles indexed by name. There MUST be a default
   * profile under the name {@link DriverExecutionProfile#DEFAULT_NAME}. Each profile is itself a
   * map, that associates values to {@link DriverOption} instances:
   *
   * <pre>
   * Map&lt;String, Map&lt;DriverOption, Object&gt;&gt; optionsMap = new ConcurrentHashMap&lt;&gt;();
   * Map&lt;DriverOption, Object&gt; defaultProfileMap = new ConcurrentHashMap&lt;&gt;();
   * optionsMap.put(DriverExecutionProfile.DEFAULT_NAME, defaultProfileMap);
   * defaultProfileMap.put(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(5));
   *
   * DriverConfigLoader loader = DriverConfigLoader.fromMap(optionsMap);
   * CqlSession session = CqlSession.builder().withConfigLoader(loader).build();
   * </pre>
   *
   * Built-in {@code DriverOption} instances are defined in the two enums {@link
   * DefaultDriverOption} and {@link DseDriverOption}.
   *
   * <p>For historical reasons, {@code DriverOption} does not encode type information (this is a
   * limitation of the original design and can't be changed easily without breaking backward
   * compatibility); to find out the expected type for each option, refer to the javadocs of the two
   * enums.
   *
   * <p>Not all options are required. For more details, refer to the {@code reference.conf} file
   * shipped with the driver.
   *
   * <p>For convenience, {@link #buildDefaultOptionsMap()} initializes a map pre-filled with a
   * default profile containing all of the driver's built-in defaults. This yields a configuration
   * equivalent to the driver's {@code reference.conf} file, and can be used as a starting point if
   * you only need to customize a few options:
   *
   * <pre>
   * // This creates a configuration equivalent to the built-in reference.conf:
   * Map&lt;String, Map&lt;DriverOption, Object&gt;&gt; optionsMap =
   *     DriverConfigLoader.buildDefaultOptionsMap();
   *
   * optionsMap
   *     .get(DriverExecutionProfile.DEFAULT_NAME)
   *     .put(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(5));
   *
   * DriverConfigLoader loader = DriverConfigLoader.fromMap(optionsMap);
   * </pre>
   *
   * <p>All maps must be thread-safe. If you want the ability to change the options after
   * construction, use a concurrent or synchronized implementation ({@link ConcurrentHashMap} is a
   * good choice); otherwise, you can use an immutable implementation.
   *
   * <p>If the contents of the map are modified at runtime, this will be reflected immediately in
   * the configuration. You don't need to call {@link #reload()} explicitly. Note however that,
   * depending on the option, the driver might not react to a configuration change immediately, or
   * ever. This is again documented in {@code reference.conf}.
   */
  @NonNull
  static DriverConfigLoader fromMap(@NonNull Map<String, Map<DriverOption, Object>> optionsMap) {
    return new MapBasedDriverConfigLoader(optionsMap);
  }

  /**
   * Builds a new map containing the built-in defaults, for use with {@link #fromMap(Map)}.
   *
   * <p>The returned map is modifiable and thread-safe. Each call returns a different instance.
   */
  @NonNull
  static Map<String, Map<DriverOption, Object>> buildDefaultOptionsMap() {
    return MapBasedDriverConfigLoader.buildDefaultOptionsMap();
  }

  /**
   * Loads the first configuration that will be used to initialize the driver.
   *
   * <p>If this loader {@linkplain #supportsReloading() supports reloading}, this object should be
   * mutable and reflect later changes when the configuration gets reloaded.
   */
  @NonNull
  DriverConfig getInitialConfig();

  /**
   * Called when the driver initializes. For loaders that periodically check for configuration
   * updates, this is a good time to grab an internal executor and schedule a recurring task.
   */
  void onDriverInit(@NonNull DriverContext context);

  /**
   * Triggers an immediate reload attempt.
   *
   * @return a stage that completes once the attempt is finished, with a boolean indicating whether
   *     the configuration changed as a result of this reload. If so, it's also guaranteed that
   *     internal driver components have been notified by that time; note however that some react to
   *     the notification asynchronously, so they may not have completely applied all resulting
   *     changes yet. If this loader does not support programmatic reloading &mdash; which you can
   *     check by calling {@link #supportsReloading()} before this method &mdash; the returned
   *     object will fail immediately with an {@link UnsupportedOperationException}.
   */
  @NonNull
  CompletionStage<Boolean> reload();

  /**
   * Whether this implementation supports programmatic reloading with the {@link #reload()} method.
   */
  boolean supportsReloading();

  /**
   * Called when the cluster closes. This is a good time to release any external resource, for
   * example cancel a scheduled reloading task.
   */
  @Override
  void close();
}
