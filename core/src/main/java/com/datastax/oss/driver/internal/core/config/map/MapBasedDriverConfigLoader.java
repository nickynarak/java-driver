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
package com.datastax.oss.driver.internal.core.config.map;

import com.datastax.oss.driver.api.core.config.DriverConfig;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.DriverOption;
import com.datastax.oss.driver.api.core.context.DriverContext;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MapBasedDriverConfigLoader implements DriverConfigLoader {

  @NonNull private final Map<String, Map<DriverOption, Object>> optionsMap;

  public MapBasedDriverConfigLoader(@NonNull Map<String, Map<DriverOption, Object>> optionsMap) {
    this.optionsMap = optionsMap;
  }

  @NonNull
  @Override
  public DriverConfig getInitialConfig() {
    return new MapBasedDriverConfig(optionsMap);
  }

  @Override
  public void onDriverInit(@NonNull DriverContext context) {
    // nothing to do
  }

  @NonNull
  @Override
  public CompletionStage<Boolean> reload() {
    return CompletableFuture.completedFuture(true);
  }

  @Override
  public boolean supportsReloading() {
    return true;
  }

  @Override
  public void close() {
    // nothing to do
  }
}
