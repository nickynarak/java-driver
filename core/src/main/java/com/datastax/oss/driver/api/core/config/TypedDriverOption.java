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
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class TypedDriverOption<ValueT> {

  private final DriverOption rawOption;
  private final GenericType<ValueT> expectedType;

  public TypedDriverOption(
      @NonNull DriverOption rawOption, @NonNull GenericType<ValueT> expectedType) {
    this.rawOption = Objects.requireNonNull(rawOption);
    this.expectedType = Objects.requireNonNull(expectedType);
  }

  @NonNull
  public DriverOption getRawOption() {
    return rawOption;
  }

  @NonNull
  public GenericType<ValueT> getExpectedType() {
    return expectedType;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    } else if (other instanceof TypedDriverOption) {
      TypedDriverOption<?> that = (TypedDriverOption<?>) other;
      return this.rawOption.equals(that.rawOption) && this.expectedType.equals(that.expectedType);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(rawOption, expectedType);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TypedDriverOption.class.getSimpleName() + "[", "]")
        .add("rawOption=" + rawOption)
        .add("expectedType=" + expectedType)
        .toString();
  }

  public static final TypedDriverOption<List<String>> CONTACT_POINTS =
      new TypedDriverOption<>(DefaultDriverOption.CONTACT_POINTS, GenericType.listOf(String.class));
  public static final TypedDriverOption<String> SESSION_NAME =
      new TypedDriverOption<>(DefaultDriverOption.SESSION_NAME, GenericType.STRING);
  public static final TypedDriverOption<String> SESSION_KEYSPACE =
      new TypedDriverOption<>(DefaultDriverOption.SESSION_KEYSPACE, GenericType.STRING);
  public static final TypedDriverOption<Duration> CONFIG_RELOAD_INTERVAL =
      new TypedDriverOption<>(DefaultDriverOption.CONFIG_RELOAD_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<Duration> REQUEST_TIMEOUT =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<String> REQUEST_CONSISTENCY =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_CONSISTENCY, GenericType.STRING);
  public static final TypedDriverOption<Integer> REQUEST_PAGE_SIZE =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_PAGE_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<String> REQUEST_SERIAL_CONSISTENCY =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_SERIAL_CONSISTENCY, GenericType.STRING);
  public static final TypedDriverOption<Boolean> REQUEST_DEFAULT_IDEMPOTENCE =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_DEFAULT_IDEMPOTENCE, GenericType.BOOLEAN);
  public static final TypedDriverOption<String> LOAD_BALANCING_POLICY_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, GenericType.STRING);
  public static final TypedDriverOption<String> LOAD_BALANCING_LOCAL_DATACENTER =
      new TypedDriverOption<>(
          DefaultDriverOption.LOAD_BALANCING_LOCAL_DATACENTER, GenericType.STRING);
  public static final TypedDriverOption<String> LOAD_BALANCING_FILTER_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.LOAD_BALANCING_FILTER_CLASS, GenericType.STRING);
  public static final TypedDriverOption<Duration> CONNECTION_INIT_QUERY_TIMEOUT =
      new TypedDriverOption<>(
          DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Duration> CONNECTION_SET_KEYSPACE_TIMEOUT =
      new TypedDriverOption<>(
          DefaultDriverOption.CONNECTION_SET_KEYSPACE_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Integer> CONNECTION_MAX_REQUESTS =
      new TypedDriverOption<>(DefaultDriverOption.CONNECTION_MAX_REQUESTS, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> CONNECTION_MAX_ORPHAN_REQUESTS =
      new TypedDriverOption<>(
          DefaultDriverOption.CONNECTION_MAX_ORPHAN_REQUESTS, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> CONNECTION_WARN_INIT_ERROR =
      new TypedDriverOption<>(DefaultDriverOption.CONNECTION_WARN_INIT_ERROR, GenericType.BOOLEAN);
  public static final TypedDriverOption<Integer> CONNECTION_POOL_LOCAL_SIZE =
      new TypedDriverOption<>(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> CONNECTION_POOL_REMOTE_SIZE =
      new TypedDriverOption<>(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> RECONNECT_ON_INIT =
      new TypedDriverOption<>(DefaultDriverOption.RECONNECT_ON_INIT, GenericType.BOOLEAN);
  public static final TypedDriverOption<String> RECONNECTION_POLICY_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.RECONNECTION_POLICY_CLASS, GenericType.STRING);
  public static final TypedDriverOption<Duration> RECONNECTION_BASE_DELAY =
      new TypedDriverOption<>(DefaultDriverOption.RECONNECTION_BASE_DELAY, GenericType.DURATION);
  public static final TypedDriverOption<Duration> RECONNECTION_MAX_DELAY =
      new TypedDriverOption<>(DefaultDriverOption.RECONNECTION_MAX_DELAY, GenericType.DURATION);
  public static final TypedDriverOption<String> RETRY_POLICY_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.RETRY_POLICY_CLASS, GenericType.STRING);
  public static final TypedDriverOption<String> SPECULATIVE_EXECUTION_POLICY_CLASS =
      new TypedDriverOption<>(
          DefaultDriverOption.SPECULATIVE_EXECUTION_POLICY_CLASS, GenericType.STRING);
  public static final TypedDriverOption<Integer> SPECULATIVE_EXECUTION_MAX =
      new TypedDriverOption<>(DefaultDriverOption.SPECULATIVE_EXECUTION_MAX, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> SPECULATIVE_EXECUTION_DELAY =
      new TypedDriverOption<>(
          DefaultDriverOption.SPECULATIVE_EXECUTION_DELAY, GenericType.DURATION);
  public static final TypedDriverOption<String> AUTH_PROVIDER_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.AUTH_PROVIDER_CLASS, GenericType.STRING);
  public static final TypedDriverOption<String> AUTH_PROVIDER_USER_NAME =
      new TypedDriverOption<>(DefaultDriverOption.AUTH_PROVIDER_USER_NAME, GenericType.STRING);
  public static final TypedDriverOption<String> AUTH_PROVIDER_PASSWORD =
      new TypedDriverOption<>(DefaultDriverOption.AUTH_PROVIDER_PASSWORD, GenericType.STRING);
  public static final TypedDriverOption<String> SSL_ENGINE_FACTORY_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.SSL_ENGINE_FACTORY_CLASS, GenericType.STRING);
  public static final TypedDriverOption<List<String>> SSL_CIPHER_SUITES =
      new TypedDriverOption<>(
          DefaultDriverOption.SSL_CIPHER_SUITES, GenericType.listOf(String.class));
  public static final TypedDriverOption<Boolean> SSL_HOSTNAME_VALIDATION =
      new TypedDriverOption<>(DefaultDriverOption.SSL_HOSTNAME_VALIDATION, GenericType.BOOLEAN);
  public static final TypedDriverOption<String> SSL_KEYSTORE_PATH =
      new TypedDriverOption<>(DefaultDriverOption.SSL_KEYSTORE_PATH, GenericType.STRING);
  public static final TypedDriverOption<String> SSL_KEYSTORE_PASSWORD =
      new TypedDriverOption<>(DefaultDriverOption.SSL_KEYSTORE_PASSWORD, GenericType.STRING);
  public static final TypedDriverOption<String> SSL_TRUSTSTORE_PATH =
      new TypedDriverOption<>(DefaultDriverOption.SSL_TRUSTSTORE_PATH, GenericType.STRING);
  public static final TypedDriverOption<String> SSL_TRUSTSTORE_PASSWORD =
      new TypedDriverOption<>(DefaultDriverOption.SSL_TRUSTSTORE_PASSWORD, GenericType.STRING);
  public static final TypedDriverOption<String> TIMESTAMP_GENERATOR_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.TIMESTAMP_GENERATOR_CLASS, GenericType.STRING);
  public static final TypedDriverOption<Boolean> TIMESTAMP_GENERATOR_FORCE_JAVA_CLOCK =
      new TypedDriverOption<>(
          DefaultDriverOption.TIMESTAMP_GENERATOR_FORCE_JAVA_CLOCK, GenericType.BOOLEAN);
  public static final TypedDriverOption<Duration> TIMESTAMP_GENERATOR_DRIFT_WARNING_THRESHOLD =
      new TypedDriverOption<>(
          DefaultDriverOption.TIMESTAMP_GENERATOR_DRIFT_WARNING_THRESHOLD, GenericType.DURATION);
  public static final TypedDriverOption<Duration> TIMESTAMP_GENERATOR_DRIFT_WARNING_INTERVAL =
      new TypedDriverOption<>(
          DefaultDriverOption.TIMESTAMP_GENERATOR_DRIFT_WARNING_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<String> REQUEST_TRACKER_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_TRACKER_CLASS, GenericType.STRING);
  public static final TypedDriverOption<Boolean> REQUEST_LOGGER_SUCCESS_ENABLED =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_LOGGER_SUCCESS_ENABLED, GenericType.BOOLEAN);
  public static final TypedDriverOption<Duration> REQUEST_LOGGER_SLOW_THRESHOLD =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_LOGGER_SLOW_THRESHOLD, GenericType.DURATION);
  public static final TypedDriverOption<Boolean> REQUEST_LOGGER_SLOW_ENABLED =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_LOGGER_SLOW_ENABLED, GenericType.BOOLEAN);
  public static final TypedDriverOption<Boolean> REQUEST_LOGGER_ERROR_ENABLED =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_LOGGER_ERROR_ENABLED, GenericType.BOOLEAN);
  public static final TypedDriverOption<Integer> REQUEST_LOGGER_MAX_QUERY_LENGTH =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_LOGGER_MAX_QUERY_LENGTH, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> REQUEST_LOGGER_VALUES =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_LOGGER_VALUES, GenericType.BOOLEAN);
  public static final TypedDriverOption<Integer> REQUEST_LOGGER_MAX_VALUE_LENGTH =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_LOGGER_MAX_VALUE_LENGTH, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> REQUEST_LOGGER_MAX_VALUES =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_LOGGER_MAX_VALUES, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> REQUEST_LOGGER_STACK_TRACES =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_LOGGER_STACK_TRACES, GenericType.BOOLEAN);
  public static final TypedDriverOption<String> REQUEST_THROTTLER_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_THROTTLER_CLASS, GenericType.STRING);
  public static final TypedDriverOption<Integer> REQUEST_THROTTLER_MAX_CONCURRENT_REQUESTS =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_THROTTLER_MAX_CONCURRENT_REQUESTS, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> REQUEST_THROTTLER_MAX_REQUESTS_PER_SECOND =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_THROTTLER_MAX_REQUESTS_PER_SECOND, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> REQUEST_THROTTLER_MAX_QUEUE_SIZE =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_THROTTLER_MAX_QUEUE_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> REQUEST_THROTTLER_DRAIN_INTERVAL =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_THROTTLER_DRAIN_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<String> METADATA_NODE_STATE_LISTENER_CLASS =
      new TypedDriverOption<>(
          DefaultDriverOption.METADATA_NODE_STATE_LISTENER_CLASS, GenericType.STRING);
  public static final TypedDriverOption<String> METADATA_SCHEMA_CHANGE_LISTENER_CLASS =
      new TypedDriverOption<>(
          DefaultDriverOption.METADATA_SCHEMA_CHANGE_LISTENER_CLASS, GenericType.STRING);
  public static final TypedDriverOption<String> ADDRESS_TRANSLATOR_CLASS =
      new TypedDriverOption<>(DefaultDriverOption.ADDRESS_TRANSLATOR_CLASS, GenericType.STRING);
  public static final TypedDriverOption<String> PROTOCOL_VERSION =
      new TypedDriverOption<>(DefaultDriverOption.PROTOCOL_VERSION, GenericType.STRING);
  public static final TypedDriverOption<String> PROTOCOL_COMPRESSION =
      new TypedDriverOption<>(DefaultDriverOption.PROTOCOL_COMPRESSION, GenericType.STRING);
  public static final TypedDriverOption<Long> PROTOCOL_MAX_FRAME_LENGTH =
      new TypedDriverOption<>(DefaultDriverOption.PROTOCOL_MAX_FRAME_LENGTH, GenericType.LONG);
  public static final TypedDriverOption<Boolean> REQUEST_WARN_IF_SET_KEYSPACE =
      new TypedDriverOption<>(
          DefaultDriverOption.REQUEST_WARN_IF_SET_KEYSPACE, GenericType.BOOLEAN);
  public static final TypedDriverOption<Integer> REQUEST_TRACE_ATTEMPTS =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_TRACE_ATTEMPTS, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> REQUEST_TRACE_INTERVAL =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_TRACE_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<String> REQUEST_TRACE_CONSISTENCY =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_TRACE_CONSISTENCY, GenericType.STRING);
  public static final TypedDriverOption<List<String>> METRICS_SESSION_ENABLED =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_SESSION_ENABLED, GenericType.listOf(String.class));
  public static final TypedDriverOption<List<String>> METRICS_NODE_ENABLED =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_NODE_ENABLED, GenericType.listOf(String.class));
  public static final TypedDriverOption<Duration> METRICS_SESSION_CQL_REQUESTS_HIGHEST =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_SESSION_CQL_REQUESTS_HIGHEST, GenericType.DURATION);
  public static final TypedDriverOption<Integer> METRICS_SESSION_CQL_REQUESTS_DIGITS =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_SESSION_CQL_REQUESTS_DIGITS, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> METRICS_SESSION_CQL_REQUESTS_INTERVAL =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_SESSION_CQL_REQUESTS_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<Duration> METRICS_SESSION_THROTTLING_HIGHEST =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_SESSION_THROTTLING_HIGHEST, GenericType.DURATION);
  public static final TypedDriverOption<Integer> METRICS_SESSION_THROTTLING_DIGITS =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_SESSION_THROTTLING_DIGITS, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> METRICS_SESSION_THROTTLING_INTERVAL =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_SESSION_THROTTLING_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<Duration> METRICS_NODE_CQL_MESSAGES_HIGHEST =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_NODE_CQL_MESSAGES_HIGHEST, GenericType.DURATION);
  public static final TypedDriverOption<Integer> METRICS_NODE_CQL_MESSAGES_DIGITS =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_NODE_CQL_MESSAGES_DIGITS, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> METRICS_NODE_CQL_MESSAGES_INTERVAL =
      new TypedDriverOption<>(
          DefaultDriverOption.METRICS_NODE_CQL_MESSAGES_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<Boolean> SOCKET_TCP_NODELAY =
      new TypedDriverOption<>(DefaultDriverOption.SOCKET_TCP_NODELAY, GenericType.BOOLEAN);
  public static final TypedDriverOption<Boolean> SOCKET_KEEP_ALIVE =
      new TypedDriverOption<>(DefaultDriverOption.SOCKET_KEEP_ALIVE, GenericType.BOOLEAN);
  public static final TypedDriverOption<Boolean> SOCKET_REUSE_ADDRESS =
      new TypedDriverOption<>(DefaultDriverOption.SOCKET_REUSE_ADDRESS, GenericType.BOOLEAN);
  public static final TypedDriverOption<Integer> SOCKET_LINGER_INTERVAL =
      new TypedDriverOption<>(DefaultDriverOption.SOCKET_LINGER_INTERVAL, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> SOCKET_RECEIVE_BUFFER_SIZE =
      new TypedDriverOption<>(DefaultDriverOption.SOCKET_RECEIVE_BUFFER_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> SOCKET_SEND_BUFFER_SIZE =
      new TypedDriverOption<>(DefaultDriverOption.SOCKET_SEND_BUFFER_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> HEARTBEAT_INTERVAL =
      new TypedDriverOption<>(DefaultDriverOption.HEARTBEAT_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<Duration> HEARTBEAT_TIMEOUT =
      new TypedDriverOption<>(DefaultDriverOption.HEARTBEAT_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Duration> METADATA_TOPOLOGY_WINDOW =
      new TypedDriverOption<>(DefaultDriverOption.METADATA_TOPOLOGY_WINDOW, GenericType.DURATION);
  public static final TypedDriverOption<Integer> METADATA_TOPOLOGY_MAX_EVENTS =
      new TypedDriverOption<>(
          DefaultDriverOption.METADATA_TOPOLOGY_MAX_EVENTS, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> METADATA_SCHEMA_ENABLED =
      new TypedDriverOption<>(DefaultDriverOption.METADATA_SCHEMA_ENABLED, GenericType.BOOLEAN);
  public static final TypedDriverOption<Duration> METADATA_SCHEMA_REQUEST_TIMEOUT =
      new TypedDriverOption<>(
          DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Integer> METADATA_SCHEMA_REQUEST_PAGE_SIZE =
      new TypedDriverOption<>(
          DefaultDriverOption.METADATA_SCHEMA_REQUEST_PAGE_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<List<String>> METADATA_SCHEMA_REFRESHED_KEYSPACES =
      new TypedDriverOption<>(
          DefaultDriverOption.METADATA_SCHEMA_REFRESHED_KEYSPACES,
          GenericType.listOf(String.class));
  public static final TypedDriverOption<Duration> METADATA_SCHEMA_WINDOW =
      new TypedDriverOption<>(DefaultDriverOption.METADATA_SCHEMA_WINDOW, GenericType.DURATION);
  public static final TypedDriverOption<Integer> METADATA_SCHEMA_MAX_EVENTS =
      new TypedDriverOption<>(DefaultDriverOption.METADATA_SCHEMA_MAX_EVENTS, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> METADATA_TOKEN_MAP_ENABLED =
      new TypedDriverOption<>(DefaultDriverOption.METADATA_TOKEN_MAP_ENABLED, GenericType.BOOLEAN);
  public static final TypedDriverOption<Duration> CONTROL_CONNECTION_TIMEOUT =
      new TypedDriverOption<>(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Duration> CONTROL_CONNECTION_AGREEMENT_INTERVAL =
      new TypedDriverOption<>(
          DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<Duration> CONTROL_CONNECTION_AGREEMENT_TIMEOUT =
      new TypedDriverOption<>(
          DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Boolean> CONTROL_CONNECTION_AGREEMENT_WARN =
      new TypedDriverOption<>(
          DefaultDriverOption.CONTROL_CONNECTION_AGREEMENT_WARN, GenericType.BOOLEAN);
  public static final TypedDriverOption<Boolean> PREPARE_ON_ALL_NODES =
      new TypedDriverOption<>(DefaultDriverOption.PREPARE_ON_ALL_NODES, GenericType.BOOLEAN);
  public static final TypedDriverOption<Boolean> REPREPARE_ENABLED =
      new TypedDriverOption<>(DefaultDriverOption.REPREPARE_ENABLED, GenericType.BOOLEAN);
  public static final TypedDriverOption<Boolean> REPREPARE_CHECK_SYSTEM_TABLE =
      new TypedDriverOption<>(
          DefaultDriverOption.REPREPARE_CHECK_SYSTEM_TABLE, GenericType.BOOLEAN);
  public static final TypedDriverOption<Integer> REPREPARE_MAX_STATEMENTS =
      new TypedDriverOption<>(DefaultDriverOption.REPREPARE_MAX_STATEMENTS, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> REPREPARE_MAX_PARALLELISM =
      new TypedDriverOption<>(DefaultDriverOption.REPREPARE_MAX_PARALLELISM, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> REPREPARE_TIMEOUT =
      new TypedDriverOption<>(DefaultDriverOption.REPREPARE_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Integer> NETTY_IO_SIZE =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_IO_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> NETTY_IO_SHUTDOWN_QUIET_PERIOD =
      new TypedDriverOption<>(
          DefaultDriverOption.NETTY_IO_SHUTDOWN_QUIET_PERIOD, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> NETTY_IO_SHUTDOWN_TIMEOUT =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_IO_SHUTDOWN_TIMEOUT, GenericType.INTEGER);
  public static final TypedDriverOption<String> NETTY_IO_SHUTDOWN_UNIT =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_IO_SHUTDOWN_UNIT, GenericType.STRING);
  public static final TypedDriverOption<Integer> NETTY_ADMIN_SIZE =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_ADMIN_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> NETTY_ADMIN_SHUTDOWN_QUIET_PERIOD =
      new TypedDriverOption<>(
          DefaultDriverOption.NETTY_ADMIN_SHUTDOWN_QUIET_PERIOD, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> NETTY_ADMIN_SHUTDOWN_TIMEOUT =
      new TypedDriverOption<>(
          DefaultDriverOption.NETTY_ADMIN_SHUTDOWN_TIMEOUT, GenericType.INTEGER);
  public static final TypedDriverOption<String> NETTY_ADMIN_SHUTDOWN_UNIT =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_ADMIN_SHUTDOWN_UNIT, GenericType.STRING);
  public static final TypedDriverOption<Integer> COALESCER_MAX_RUNS =
      new TypedDriverOption<>(DefaultDriverOption.COALESCER_MAX_RUNS, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> COALESCER_INTERVAL =
      new TypedDriverOption<>(DefaultDriverOption.COALESCER_INTERVAL, GenericType.DURATION);
  public static final TypedDriverOption<Boolean> RESOLVE_CONTACT_POINTS =
      new TypedDriverOption<>(DefaultDriverOption.RESOLVE_CONTACT_POINTS, GenericType.BOOLEAN);
  public static final TypedDriverOption<Duration> NETTY_TIMER_TICK_DURATION =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_TIMER_TICK_DURATION, GenericType.DURATION);
  public static final TypedDriverOption<Integer> NETTY_TIMER_TICKS_PER_WHEEL =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_TIMER_TICKS_PER_WHEEL, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> REQUEST_LOG_WARNINGS =
      new TypedDriverOption<>(DefaultDriverOption.REQUEST_LOG_WARNINGS, GenericType.BOOLEAN);
  public static final TypedDriverOption<Boolean> NETTY_DAEMON =
      new TypedDriverOption<>(DefaultDriverOption.NETTY_DAEMON, GenericType.BOOLEAN);
  public static final TypedDriverOption<String> CLOUD_SECURE_CONNECT_BUNDLE =
      new TypedDriverOption<>(DefaultDriverOption.CLOUD_SECURE_CONNECT_BUNDLE, GenericType.STRING);
  public static final TypedDriverOption<Boolean> LOAD_BALANCING_POLICY_SLOW_AVOIDANCE =
      new TypedDriverOption<>(
          DefaultDriverOption.LOAD_BALANCING_POLICY_SLOW_AVOIDANCE, GenericType.BOOLEAN);

  public static final TypedDriverOption<String> APPLICATION_NAME =
      new TypedDriverOption<>(DseDriverOption.APPLICATION_NAME, GenericType.STRING);
  public static final TypedDriverOption<String> APPLICATION_VERSION =
      new TypedDriverOption<>(DseDriverOption.APPLICATION_VERSION, GenericType.STRING);
  public static final TypedDriverOption<String> AUTH_PROVIDER_AUTHORIZATION_ID =
      new TypedDriverOption<>(DseDriverOption.AUTH_PROVIDER_AUTHORIZATION_ID, GenericType.STRING);
  public static final TypedDriverOption<String> AUTH_PROVIDER_SERVICE =
      new TypedDriverOption<>(DseDriverOption.AUTH_PROVIDER_SERVICE, GenericType.STRING);
  public static final TypedDriverOption<String> AUTH_PROVIDER_LOGIN_CONFIGURATION =
      new TypedDriverOption<>(
          DseDriverOption.AUTH_PROVIDER_LOGIN_CONFIGURATION, GenericType.STRING);
  public static final TypedDriverOption<Map<String, String>> AUTH_PROVIDER_SASL_PROPERTIES =
      new TypedDriverOption<>(
          DseDriverOption.AUTH_PROVIDER_SASL_PROPERTIES,
          GenericType.mapOf(GenericType.STRING, GenericType.STRING));
  public static final TypedDriverOption<Integer> CONTINUOUS_PAGING_PAGE_SIZE =
      new TypedDriverOption<>(DseDriverOption.CONTINUOUS_PAGING_PAGE_SIZE, GenericType.INTEGER);
  public static final TypedDriverOption<Boolean> CONTINUOUS_PAGING_PAGE_SIZE_BYTES =
      new TypedDriverOption<>(
          DseDriverOption.CONTINUOUS_PAGING_PAGE_SIZE_BYTES, GenericType.BOOLEAN);
  public static final TypedDriverOption<Integer> CONTINUOUS_PAGING_MAX_PAGES =
      new TypedDriverOption<>(DseDriverOption.CONTINUOUS_PAGING_MAX_PAGES, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> CONTINUOUS_PAGING_MAX_PAGES_PER_SECOND =
      new TypedDriverOption<>(
          DseDriverOption.CONTINUOUS_PAGING_MAX_PAGES_PER_SECOND, GenericType.INTEGER);
  public static final TypedDriverOption<Integer> CONTINUOUS_PAGING_MAX_ENQUEUED_PAGES =
      new TypedDriverOption<>(
          DseDriverOption.CONTINUOUS_PAGING_MAX_ENQUEUED_PAGES, GenericType.INTEGER);
  public static final TypedDriverOption<Duration> CONTINUOUS_PAGING_TIMEOUT_FIRST_PAGE =
      new TypedDriverOption<>(
          DseDriverOption.CONTINUOUS_PAGING_TIMEOUT_FIRST_PAGE, GenericType.DURATION);
  public static final TypedDriverOption<Duration> CONTINUOUS_PAGING_TIMEOUT_OTHER_PAGES =
      new TypedDriverOption<>(
          DseDriverOption.CONTINUOUS_PAGING_TIMEOUT_OTHER_PAGES, GenericType.DURATION);
  public static final TypedDriverOption<Duration>
      CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_HIGHEST =
          new TypedDriverOption<>(
              DseDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_HIGHEST,
              GenericType.DURATION);
  public static final TypedDriverOption<Integer>
      CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_DIGITS =
          new TypedDriverOption<>(
              DseDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_DIGITS,
              GenericType.INTEGER);
  public static final TypedDriverOption<Duration>
      CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_INTERVAL =
          new TypedDriverOption<>(
              DseDriverOption.CONTINUOUS_PAGING_METRICS_SESSION_CQL_REQUESTS_INTERVAL,
              GenericType.DURATION);
  public static final TypedDriverOption<String> GRAPH_READ_CONSISTENCY_LEVEL =
      new TypedDriverOption<>(DseDriverOption.GRAPH_READ_CONSISTENCY_LEVEL, GenericType.STRING);
  public static final TypedDriverOption<String> GRAPH_WRITE_CONSISTENCY_LEVEL =
      new TypedDriverOption<>(DseDriverOption.GRAPH_WRITE_CONSISTENCY_LEVEL, GenericType.STRING);
  public static final TypedDriverOption<String> GRAPH_TRAVERSAL_SOURCE =
      new TypedDriverOption<>(DseDriverOption.GRAPH_TRAVERSAL_SOURCE, GenericType.STRING);
  public static final TypedDriverOption<String> GRAPH_SUB_PROTOCOL =
      new TypedDriverOption<>(DseDriverOption.GRAPH_SUB_PROTOCOL, GenericType.STRING);
  public static final TypedDriverOption<Boolean> GRAPH_IS_SYSTEM_QUERY =
      new TypedDriverOption<>(DseDriverOption.GRAPH_IS_SYSTEM_QUERY, GenericType.BOOLEAN);
  public static final TypedDriverOption<String> GRAPH_NAME =
      new TypedDriverOption<>(DseDriverOption.GRAPH_NAME, GenericType.STRING);
  public static final TypedDriverOption<Duration> GRAPH_TIMEOUT =
      new TypedDriverOption<>(DseDriverOption.GRAPH_TIMEOUT, GenericType.DURATION);
  public static final TypedDriverOption<Boolean> MONITOR_REPORTING_ENABLED =
      new TypedDriverOption<>(DseDriverOption.MONITOR_REPORTING_ENABLED, GenericType.BOOLEAN);
}
