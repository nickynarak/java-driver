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

import static com.datastax.oss.driver.Assertions.assertThat;

import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.datastax.oss.driver.api.core.config.DriverOption;
import com.datastax.oss.driver.internal.core.config.MockOptions;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;

public class MapBasedDriverConfigTest {

  private Map<String, Map<DriverOption, Object>> optionsMap;
  private MapBasedDriverConfig config;

  @Before
  public void setup() {
    optionsMap = new ConcurrentHashMap<>();
    optionsMap.put(DriverExecutionProfile.DEFAULT_NAME, new ConcurrentHashMap<>());
    config = new MapBasedDriverConfig(optionsMap);
  }

  @Test
  public void should_load_minimal_config_with_no_profiles() {
    addOption(DriverExecutionProfile.DEFAULT_NAME, MockOptions.INT1, 42);
    assertThat(config).hasIntOption(MockOptions.INT1, 42);
  }

  @Test
  public void should_inherit_option_in_profile() {
    addOption(DriverExecutionProfile.DEFAULT_NAME, MockOptions.INT1, 42);
    addProfile("profile1");
    assertThat(config)
        .hasIntOption(MockOptions.INT1, 42)
        .hasIntOption("profile1", MockOptions.INT1, 42);
  }

  @Test
  public void should_override_option_in_profile() {
    addOption(DriverExecutionProfile.DEFAULT_NAME, MockOptions.INT1, 42);
    addOption("profile1", MockOptions.INT1, 43);
    assertThat(config)
        .hasIntOption(MockOptions.INT1, 42)
        .hasIntOption("profile1", MockOptions.INT1, 43);
  }

  @Test
  public void should_create_derived_profile_with_new_option() {
    addOption(DriverExecutionProfile.DEFAULT_NAME, MockOptions.INT1, 42);
    DriverExecutionProfile base = config.getDefaultProfile();
    DriverExecutionProfile derived = base.withInt(MockOptions.INT2, 43);

    assertThat(base.isDefined(MockOptions.INT2)).isFalse();
    assertThat(derived.isDefined(MockOptions.INT2)).isTrue();
    assertThat(derived.getInt(MockOptions.INT2)).isEqualTo(43);
  }

  @Test
  public void should_create_derived_profile_overriding_option() {
    addOption(DriverExecutionProfile.DEFAULT_NAME, MockOptions.INT1, 42);
    DriverExecutionProfile base = config.getDefaultProfile();
    DriverExecutionProfile derived = base.withInt(MockOptions.INT1, 43);

    assertThat(base.getInt(MockOptions.INT1)).isEqualTo(42);
    assertThat(derived.getInt(MockOptions.INT1)).isEqualTo(43);
  }

  @Test
  public void should_create_derived_profile_unsetting_option() {
    addOption(DriverExecutionProfile.DEFAULT_NAME, MockOptions.INT1, 42);
    addOption(DriverExecutionProfile.DEFAULT_NAME, MockOptions.INT2, 43);
    DriverExecutionProfile base = config.getDefaultProfile();
    DriverExecutionProfile derived = base.without(MockOptions.INT2);

    assertThat(base.getInt(MockOptions.INT2)).isEqualTo(43);
    assertThat(derived.isDefined(MockOptions.INT2)).isFalse();
  }

  private void addOption(String profile, DriverOption option, Object value) {
    addProfile(profile).put(option, value);
  }

  private Map<DriverOption, Object> addProfile(String profile) {
    return optionsMap.computeIfAbsent(profile, name -> new ConcurrentHashMap<>());
  }
}
