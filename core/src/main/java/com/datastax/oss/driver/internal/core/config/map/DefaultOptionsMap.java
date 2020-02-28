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

import com.datastax.oss.driver.api.core.config.DriverOption;
import com.datastax.oss.driver.api.core.config.map.OptionsMap;
import com.datastax.oss.driver.api.core.config.map.TypedDriverOption;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultOptionsMap implements OptionsMap {

  private final ConcurrentHashMap<String, Map<DriverOption, Object>> map =
      new ConcurrentHashMap<>();

  @Nullable
  @Override
  public <ValueT> ValueT put(
      @NonNull String profile, @NonNull TypedDriverOption<ValueT> option, @NonNull ValueT value) {
    Objects.requireNonNull(option, "option");
    Objects.requireNonNull(value, "value");
    Object previous = getProfileMap(profile).put(option.getRawOption(), value);
    return cast(previous);
  }

  @Nullable
  @Override
  public <ValueT> ValueT get(@NonNull String profile, @NonNull TypedDriverOption<ValueT> option) {
    Objects.requireNonNull(option, "option");
    Object result = getProfileMap(profile).get(option.getRawOption());
    return cast(result);
  }

  @Nullable
  @Override
  public <ValueT> ValueT remove(
      @NonNull String profile, @NonNull TypedDriverOption<ValueT> option) {
    Objects.requireNonNull(option, "option");
    Object previous = getProfileMap(profile).remove(option.getRawOption());
    return cast(previous);
  }

  @NonNull
  @Override
  public Map<String, Map<DriverOption, Object>> asRawMap() {
    return map;
  }

  @NonNull
  private Map<DriverOption, Object> getProfileMap(@NonNull String profile) {
    Objects.requireNonNull(profile, "profile");
    return map.computeIfAbsent(profile, p -> new ConcurrentHashMap<>());
  }

  // Isolate the suppressed warning for retrieval. The cast should always succeed unless the user
  // messes with asMap() directly.
  @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
  @Nullable
  private <ValueT> ValueT cast(@Nullable Object value) {
    return (ValueT) value;
  }

  public static OptionsMap driverDefaults() {
    DefaultOptionsMap source = new DefaultOptionsMap();

    // Skip CONFIG_RELOAD_INTERVAL because the map-based config doesn't need periodic reloading
    source.put(TypedDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(2));
    source.put(TypedDriverOption.REQUEST_CONSISTENCY, "LOCAL_ONE");
    source.put(TypedDriverOption.REQUEST_PAGE_SIZE, 5000);
    source.put(TypedDriverOption.REQUEST_SERIAL_CONSISTENCY, "SERIAL");
    source.put(TypedDriverOption.REQUEST_DEFAULT_IDEMPOTENCE, false);
    source.put(TypedDriverOption.GRAPH_TRAVERSAL_SOURCE, "g");
    source.put(TypedDriverOption.LOAD_BALANCING_POLICY_CLASS, "DefaultLoadBalancingPolicy");
    source.put(TypedDriverOption.LOAD_BALANCING_POLICY_SLOW_AVOIDANCE, true);
    source.put(TypedDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(500));
    source.put(TypedDriverOption.CONNECTION_SET_KEYSPACE_TIMEOUT, Duration.ofMillis(500));
    source.put(TypedDriverOption.CONNECTION_POOL_LOCAL_SIZE, 1);
    source.put(TypedDriverOption.CONNECTION_POOL_REMOTE_SIZE, 1);
    source.put(TypedDriverOption.CONNECTION_MAX_REQUESTS, 1024);
    source.put(TypedDriverOption.CONNECTION_MAX_ORPHAN_REQUESTS, 24576);
    source.put(TypedDriverOption.CONNECTION_WARN_INIT_ERROR, true);
    source.put(TypedDriverOption.RECONNECT_ON_INIT, false);
    source.put(TypedDriverOption.RECONNECTION_POLICY_CLASS, "ExponentialReconnectionPolicy");
    source.put(TypedDriverOption.RECONNECTION_BASE_DELAY, Duration.ofSeconds(1));
    source.put(TypedDriverOption.RECONNECTION_MAX_DELAY, Duration.ofSeconds(60));
    source.put(TypedDriverOption.RETRY_POLICY_CLASS, "DefaultRetryPolicy");
    source.put(
        TypedDriverOption.SPECULATIVE_EXECUTION_POLICY_CLASS, "NoSpeculativeExecutionPolicy");
    source.put(TypedDriverOption.TIMESTAMP_GENERATOR_CLASS, "AtomicTimestampGenerator");
    source.put(
        TypedDriverOption.TIMESTAMP_GENERATOR_DRIFT_WARNING_THRESHOLD, Duration.ofSeconds(1));
    source.put(
        TypedDriverOption.TIMESTAMP_GENERATOR_DRIFT_WARNING_INTERVAL, Duration.ofSeconds(10));
    source.put(TypedDriverOption.TIMESTAMP_GENERATOR_FORCE_JAVA_CLOCK, false);
    source.put(TypedDriverOption.REQUEST_TRACKER_CLASS, "NoopRequestTracker");
    source.put(TypedDriverOption.REQUEST_THROTTLER_CLASS, "PassThroughRequestThrottler");
    source.put(TypedDriverOption.METADATA_NODE_STATE_LISTENER_CLASS, "NoopNodeStateListener");
    source.put(TypedDriverOption.METADATA_SCHEMA_CHANGE_LISTENER_CLASS, "NoopSchemaChangeListener");
    source.put(TypedDriverOption.ADDRESS_TRANSLATOR_CLASS, "PassThroughAddressTranslator");
    source.put(TypedDriverOption.RESOLVE_CONTACT_POINTS, true);
    source.put(TypedDriverOption.PROTOCOL_MAX_FRAME_LENGTH, 256L * 1024 * 1024);
    source.put(TypedDriverOption.REQUEST_WARN_IF_SET_KEYSPACE, true);
    source.put(TypedDriverOption.REQUEST_TRACE_ATTEMPTS, 5);
    source.put(TypedDriverOption.REQUEST_TRACE_INTERVAL, Duration.ofMillis(3));
    source.put(TypedDriverOption.REQUEST_TRACE_CONSISTENCY, "ONE");
    source.put(TypedDriverOption.REQUEST_LOG_WARNINGS, true);
    source.put(TypedDriverOption.CONTINUOUS_PAGING_PAGE_SIZE, 5000);
    source.put(TypedDriverOption.CONTINUOUS_PAGING_PAGE_SIZE_BYTES, false);
    source.put(TypedDriverOption.CONTINUOUS_PAGING_MAX_PAGES, 0);
    source.put(TypedDriverOption.CONTINUOUS_PAGING_MAX_PAGES_PER_SECOND, 0);
    source.put(TypedDriverOption.CONTINUOUS_PAGING_MAX_ENQUEUED_PAGES, 4);
    source.put(TypedDriverOption.CONTINUOUS_PAGING_TIMEOUT_FIRST_PAGE, Duration.ofSeconds(2));
    source.put(TypedDriverOption.CONTINUOUS_PAGING_TIMEOUT_OTHER_PAGES, Duration.ofSeconds(1));
    source.put(TypedDriverOption.MONITOR_REPORTING_ENABLED, true);
    source.put(TypedDriverOption.METRICS_SESSION_ENABLED, Collections.emptyList());
    source.put(TypedDriverOption.METRICS_SESSION_CQL_REQUESTS_HIGHEST, Duration.ofSeconds(3));
    source.put(TypedDriverOption.METRICS_SESSION_CQL_REQUESTS_DIGITS, 3);
    source.put(TypedDriverOption.METRICS_SESSION_CQL_REQUESTS_INTERVAL, Duration.ofMinutes(5));
    source.put(TypedDriverOption.METRICS_SESSION_THROTTLING_HIGHEST, Duration.ofSeconds(3));
    source.put(TypedDriverOption.METRICS_SESSION_THROTTLING_DIGITS, 3);
    source.put(TypedDriverOption.METRICS_SESSION_THROTTLING_INTERVAL, Duration.ofMinutes(5));
    source.put(
        TypedDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_HIGHEST,
        Duration.ofSeconds(3));
    source.put(TypedDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_DIGITS, 3);
    source.put(
        TypedDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_INTERVAL,
        Duration.ofMinutes(5));
    source.put(TypedDriverOption.METRICS_NODE_ENABLED, Collections.emptyList());
    source.put(TypedDriverOption.METRICS_NODE_CQL_MESSAGES_HIGHEST, Duration.ofSeconds(3));
    source.put(TypedDriverOption.METRICS_NODE_CQL_MESSAGES_DIGITS, 3);
    source.put(TypedDriverOption.METRICS_NODE_CQL_MESSAGES_INTERVAL, Duration.ofMinutes(5));
    source.put(TypedDriverOption.SOCKET_TCP_NODELAY, true);
    source.put(TypedDriverOption.HEARTBEAT_INTERVAL, Duration.ofSeconds(30));
    source.put(TypedDriverOption.HEARTBEAT_TIMEOUT, Duration.ofMillis(500));
    source.put(TypedDriverOption.METADATA_TOPOLOGY_WINDOW, Duration.ofSeconds(1));
    source.put(TypedDriverOption.METADATA_TOPOLOGY_MAX_EVENTS, 20);
    source.put(TypedDriverOption.METADATA_SCHEMA_ENABLED, true);
    source.put(TypedDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofSeconds(2));
    source.put(TypedDriverOption.METADATA_SCHEMA_REQUEST_PAGE_SIZE, 5000);
    source.put(TypedDriverOption.METADATA_SCHEMA_WINDOW, Duration.ofSeconds(1));
    source.put(TypedDriverOption.METADATA_SCHEMA_MAX_EVENTS, 20);
    source.put(TypedDriverOption.METADATA_TOKEN_MAP_ENABLED, true);
    source.put(TypedDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofMillis(500));
    source.put(TypedDriverOption.CONTROL_CONNECTION_AGREEMENT_INTERVAL, Duration.ofMillis(200));
    source.put(TypedDriverOption.CONTROL_CONNECTION_AGREEMENT_TIMEOUT, Duration.ofSeconds(10));
    source.put(TypedDriverOption.CONTROL_CONNECTION_AGREEMENT_WARN, true);
    source.put(TypedDriverOption.PREPARE_ON_ALL_NODES, true);
    source.put(TypedDriverOption.REPREPARE_ENABLED, true);
    source.put(TypedDriverOption.REPREPARE_CHECK_SYSTEM_TABLE, false);
    source.put(TypedDriverOption.REPREPARE_MAX_STATEMENTS, 0);
    source.put(TypedDriverOption.REPREPARE_MAX_PARALLELISM, 100);
    source.put(TypedDriverOption.REPREPARE_TIMEOUT, Duration.ofMillis(500));
    source.put(TypedDriverOption.NETTY_DAEMON, false);
    source.put(TypedDriverOption.NETTY_IO_SIZE, 0);
    source.put(TypedDriverOption.NETTY_IO_SHUTDOWN_QUIET_PERIOD, 2);
    source.put(TypedDriverOption.NETTY_IO_SHUTDOWN_TIMEOUT, 15);
    source.put(TypedDriverOption.NETTY_IO_SHUTDOWN_UNIT, "SECONDS");
    source.put(TypedDriverOption.NETTY_ADMIN_SIZE, 2);
    source.put(TypedDriverOption.NETTY_ADMIN_SHUTDOWN_QUIET_PERIOD, 2);
    source.put(TypedDriverOption.NETTY_ADMIN_SHUTDOWN_TIMEOUT, 15);
    source.put(TypedDriverOption.NETTY_ADMIN_SHUTDOWN_UNIT, "SECONDS");
    source.put(TypedDriverOption.NETTY_TIMER_TICK_DURATION, Duration.ofMillis(100));
    source.put(TypedDriverOption.NETTY_TIMER_TICKS_PER_WHEEL, 2048);
    source.put(TypedDriverOption.COALESCER_MAX_RUNS, 5);
    source.put(TypedDriverOption.COALESCER_INTERVAL, Duration.of(10, ChronoUnit.MICROS));

    return source;
  }
}
