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
package com.datastax.oss.driver.api.core.config.map;

import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.datastax.oss.driver.api.core.config.DriverOption;
import com.datastax.oss.driver.internal.core.config.map.DefaultOptionsMap;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Map;

public interface OptionsMap {

  @NonNull
  static OptionsMap empty() {
    return new DefaultOptionsMap();
  }

  @NonNull
  static OptionsMap driverDefaults() {
    return DefaultOptionsMap.driverDefaults();
  }

  @Nullable
  <ValueT> ValueT put(
      @NonNull String profile, @NonNull TypedDriverOption<ValueT> option, @NonNull ValueT value);

  @Nullable
  default <ValueT> ValueT put(@NonNull TypedDriverOption<ValueT> option, @NonNull ValueT value) {
    return put(DriverExecutionProfile.DEFAULT_NAME, option, value);
  }

  @Nullable
  <ValueT> ValueT get(@NonNull String profile, @NonNull TypedDriverOption<ValueT> option);

  @Nullable
  default <ValueT> ValueT get(@NonNull TypedDriverOption<ValueT> option) {
    return get(DriverExecutionProfile.DEFAULT_NAME, option);
  }

  @Nullable
  <ValueT> ValueT remove(@NonNull String profile, @NonNull TypedDriverOption<ValueT> option);

  @Nullable
  default <ValueT> ValueT remove(@NonNull TypedDriverOption<ValueT> option) {
    return remove(DriverExecutionProfile.DEFAULT_NAME, option);
  }

  @NonNull
  Map<String, Map<DriverOption, Object>> asRawMap();
}
