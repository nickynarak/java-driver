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

import com.datastax.dse.driver.api.core.config.DseDriverOption;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfig;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import com.datastax.oss.driver.api.core.config.DriverOption;
import com.datastax.oss.driver.api.core.context.DriverContext;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

public class MapBasedDriverConfigLoader implements DriverConfigLoader {

  private final Map<String, Map<DriverOption, Object>> optionsMap;

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

  public static Map<String, Map<DriverOption, Object>> buildDefaultOptionsMap() {
    Map<String, Map<DriverOption, Object>> optionsMap = new ConcurrentHashMap<>();
    Map<DriverOption, Object> defaultProfileMap = new ConcurrentHashMap<>();
    optionsMap.put(DriverExecutionProfile.DEFAULT_NAME, defaultProfileMap);

    // Skip CONFIG_RELOAD_INTERVAL because the map-based config doesn't need periodic reloading
    defaultProfileMap.put(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(2));
    defaultProfileMap.put(DefaultDriverOption.REQUEST_CONSISTENCY, "LOCAL_ONE");
    defaultProfileMap.put(DefaultDriverOption.REQUEST_PAGE_SIZE, 5000);
    defaultProfileMap.put(DefaultDriverOption.REQUEST_SERIAL_CONSISTENCY, "SERIAL");
    defaultProfileMap.put(DefaultDriverOption.REQUEST_DEFAULT_IDEMPOTENCE, false);
    defaultProfileMap.put(DseDriverOption.GRAPH_TRAVERSAL_SOURCE, "g");
    defaultProfileMap.put(
        DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, "DefaultLoadBalancingPolicy");
    defaultProfileMap.put(DefaultDriverOption.LOAD_BALANCING_POLICY_SLOW_AVOIDANCE, true);
    defaultProfileMap.put(
        DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(500));
    defaultProfileMap.put(
        DefaultDriverOption.CONNECTION_SET_KEYSPACE_TIMEOUT, Duration.ofMillis(500));
    defaultProfileMap.put(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, 1);
    defaultProfileMap.put(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE, 1);
    defaultProfileMap.put(DefaultDriverOption.CONNECTION_MAX_REQUESTS, 1024);
    defaultProfileMap.put(DefaultDriverOption.CONNECTION_MAX_ORPHAN_REQUESTS, 24576);
    defaultProfileMap.put(DefaultDriverOption.CONNECTION_WARN_INIT_ERROR, true);
    defaultProfileMap.put(DefaultDriverOption.RECONNECT_ON_INIT, false);
    defaultProfileMap.put(
        DefaultDriverOption.RECONNECTION_POLICY_CLASS, "ExponentialReconnectionPolicy");
    defaultProfileMap.put(DefaultDriverOption.RECONNECTION_BASE_DELAY, Duration.ofSeconds(1));
    defaultProfileMap.put(DefaultDriverOption.RECONNECTION_MAX_DELAY, Duration.ofSeconds(60));
    defaultProfileMap.put(DefaultDriverOption.RETRY_POLICY_CLASS, "DefaultRetryPolicy");
    defaultProfileMap.put(
        DefaultDriverOption.SPECULATIVE_EXECUTION_POLICY_CLASS, "NoSpeculativeExecutionPolicy");
    defaultProfileMap.put(
        DefaultDriverOption.TIMESTAMP_GENERATOR_CLASS, "AtomicTimestampGenerator");
    defaultProfileMap.put(
        DefaultDriverOption.TIMESTAMP_GENERATOR_DRIFT_WARNING_THRESHOLD, Duration.ofSeconds(1));
    defaultProfileMap.put(
        DefaultDriverOption.TIMESTAMP_GENERATOR_DRIFT_WARNING_INTERVAL, Duration.ofSeconds(10));
    defaultProfileMap.put(DefaultDriverOption.TIMESTAMP_GENERATOR_FORCE_JAVA_CLOCK, false);
    defaultProfileMap.put(DefaultDriverOption.REQUEST_TRACKER_CLASS, "NoopRequestTracker");
    defaultProfileMap.put(
        DefaultDriverOption.REQUEST_THROTTLER_CLASS, "PassThroughRequestThrottler");
    defaultProfileMap.put(
        DefaultDriverOption.METADATA_NODE_STATE_LISTENER_CLASS, "NoopNodeStateListener");
    defaultProfileMap.put(
        DefaultDriverOption.METADATA_SCHEMA_CHANGE_LISTENER_CLASS, "NoopSchemaChangeListener");
    defaultProfileMap.put(
        DefaultDriverOption.ADDRESS_TRANSLATOR_CLASS, "PassThroughAddressTranslator");
    defaultProfileMap.put(DefaultDriverOption.RESOLVE_CONTACT_POINTS, true);
    defaultProfileMap.put(DefaultDriverOption.PROTOCOL_MAX_FRAME_LENGTH, 256L * 1024 * 1024);
    defaultProfileMap.put(DefaultDriverOption.REQUEST_WARN_IF_SET_KEYSPACE, true);
    defaultProfileMap.put(DefaultDriverOption.REQUEST_TRACE_ATTEMPTS, 5);
    defaultProfileMap.put(DefaultDriverOption.REQUEST_TRACE_INTERVAL, Duration.ofMillis(3));
    defaultProfileMap.put(DefaultDriverOption.REQUEST_TRACE_CONSISTENCY, "ONE");
    defaultProfileMap.put(DefaultDriverOption.REQUEST_LOG_WARNINGS, true);
    defaultProfileMap.put(DseDriverOption.CONTINUOUS_PAGING_PAGE_SIZE, 5000);
    defaultProfileMap.put(DseDriverOption.CONTINUOUS_PAGING_PAGE_SIZE_BYTES, false);
    defaultProfileMap.put(DseDriverOption.CONTINUOUS_PAGING_MAX_PAGES, 0);
    defaultProfileMap.put(DseDriverOption.CONTINUOUS_PAGING_MAX_PAGES_PER_SECOND, 0);
    defaultProfileMap.put(DseDriverOption.CONTINUOUS_PAGING_MAX_ENQUEUED_PAGES, 4);
    defaultProfileMap.put(
        DseDriverOption.CONTINUOUS_PAGING_TIMEOUT_FIRST_PAGE, Duration.ofSeconds(2));
    defaultProfileMap.put(
        DseDriverOption.CONTINUOUS_PAGING_TIMEOUT_OTHER_PAGES, Duration.ofSeconds(1));
    defaultProfileMap.put(DseDriverOption.MONITOR_REPORTING_ENABLED, true);
    defaultProfileMap.put(DefaultDriverOption.METRICS_SESSION_ENABLED, Collections.emptyList());
    defaultProfileMap.put(
        DefaultDriverOption.METRICS_SESSION_CQL_REQUESTS_HIGHEST, Duration.ofSeconds(3));
    defaultProfileMap.put(DefaultDriverOption.METRICS_SESSION_CQL_REQUESTS_DIGITS, 3);
    defaultProfileMap.put(
        DefaultDriverOption.METRICS_SESSION_CQL_REQUESTS_INTERVAL, Duration.ofMinutes(5));
    defaultProfileMap.put(
        DefaultDriverOption.METRICS_SESSION_THROTTLING_HIGHEST, Duration.ofSeconds(3));
    defaultProfileMap.put(DefaultDriverOption.METRICS_SESSION_THROTTLING_DIGITS, 3);
    defaultProfileMap.put(
        DefaultDriverOption.METRICS_SESSION_THROTTLING_INTERVAL, Duration.ofMinutes(5));
    defaultProfileMap.put(
        DseDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_HIGHEST,
        Duration.ofSeconds(3));
    defaultProfileMap.put(DseDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_DIGITS, 3);
    defaultProfileMap.put(
        DseDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_INTERVAL,
        Duration.ofMinutes(5));
    defaultProfileMap.put(DefaultDriverOption.METRICS_NODE_ENABLED, Collections.emptyList());
    defaultProfileMap.put(
        DefaultDriverOption.METRICS_NODE_CQL_MESSAGES_HIGHEST, Duration.ofSeconds(3));
    defaultProfileMap.put(DefaultDriverOption.METRICS_NODE_CQL_MESSAGES_DIGITS, 3);
    defaultProfileMap.put(
        DefaultDriverOption.METRICS_NODE_CQL_MESSAGES_INTERVAL, Duration.ofMinutes(5));
    defaultProfileMap.put(DefaultDriverOption.SOCKET_TCP_NODELAY, true);
    defaultProfileMap.put(DefaultDriverOption.HEARTBEAT_INTERVAL, Duration.ofSeconds(30));
    defaultProfileMap.put(DefaultDriverOption.HEARTBEAT_TIMEOUT, Duration.ofMillis(500));
    defaultProfileMap.put(DefaultDriverOption.METADATA_TOPOLOGY_WINDOW, Duration.ofSeconds(1));
    defaultProfileMap.put(DefaultDriverOption.METADATA_TOPOLOGY_MAX_EVENTS, 20);
    defaultProfileMap.put(DefaultDriverOption.METADATA_SCHEMA_ENABLED, true);
    defaultProfileMap.put(
        DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofSeconds(2));
    defaultProfileMap.put(DefaultDriverOption.METADATA_SCHEMA_REQUEST_PAGE_SIZE, 5000);
    defaultProfileMap.put(DefaultDriverOption.METADATA_SCHEMA_WINDOW, Duration.ofSeconds(1));
    defaultProfileMap.put(DefaultDriverOption.METADATA_SCHEMA_MAX_EVENTS, 20);
    defaultProfileMap.put(DefaultDriverOption.METADATA_TOKEN_MAP_ENABLED, true);
    defaultProfileMap.put(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofMillis(500));
    defaultProfileMap.put(
        DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_INTERVAL, Duration.ofMillis(200));
    defaultProfileMap.put(
        DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_TIMEOUT, Duration.ofSeconds(10));
    defaultProfileMap.put(DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_WARN, true);
    defaultProfileMap.put(DefaultDriverOption.PREPARE_ON_ALL_NODES, true);
    defaultProfileMap.put(DefaultDriverOption.REPREPARE_ENABLED, true);
    defaultProfileMap.put(DefaultDriverOption.REPREPARE_CHECK_SYSTEM_TABLE, false);
    defaultProfileMap.put(DefaultDriverOption.REPREPARE_MAX_STATEMENTS, 0);
    defaultProfileMap.put(DefaultDriverOption.REPREPARE_MAX_PARALLELISM, 100);
    defaultProfileMap.put(DefaultDriverOption.REPREPARE_TIMEOUT, Duration.ofMillis(500));
    defaultProfileMap.put(DefaultDriverOption.NETTY_DAEMON, false);
    defaultProfileMap.put(DefaultDriverOption.NETTY_IO_SIZE, 0);
    defaultProfileMap.put(DefaultDriverOption.NETTY_IO_SHUTDOWN_QUIET_PERIOD, 2);
    defaultProfileMap.put(DefaultDriverOption.NETTY_IO_SHUTDOWN_TIMEOUT, 15);
    defaultProfileMap.put(DefaultDriverOption.NETTY_IO_SHUTDOWN_UNIT, "SECONDS");
    defaultProfileMap.put(DefaultDriverOption.NETTY_ADMIN_SIZE, 2);
    defaultProfileMap.put(DefaultDriverOption.NETTY_ADMIN_SHUTDOWN_QUIET_PERIOD, 2);
    defaultProfileMap.put(DefaultDriverOption.NETTY_ADMIN_SHUTDOWN_TIMEOUT, 15);
    defaultProfileMap.put(DefaultDriverOption.NETTY_ADMIN_SHUTDOWN_UNIT, "SECONDS");
    defaultProfileMap.put(DefaultDriverOption.NETTY_TIMER_TICK_DURATION, Duration.ofMillis(100));
    defaultProfileMap.put(DefaultDriverOption.NETTY_TIMER_TICKS_PER_WHEEL, 2048);
    defaultProfileMap.put(DefaultDriverOption.COALESCER_MAX_RUNS, 5);
    defaultProfileMap.put(
        DefaultDriverOption.COALESCER_INTERVAL, Duration.of(10, ChronoUnit.MICROS));

    return optionsMap;
  }
}
