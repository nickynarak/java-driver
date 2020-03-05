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
package com.datastax.oss.driver.api.core.type.codec;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.shaded.guava.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * A {@link TypeCodec} that maps instances of {@code InnerT}, a supported Java type, to instances of
 * a target {@code OuterT} Java type.
 *
 * <p>This codec can be used to provide support for Java types that are not natively handled by the
 * driver, as long as there is a conversion path to and from another supported Java type.
 *
 * <p>See the <a
 * href="https://docs.datastax.com/en/developer/java-driver/latest/manual/core/custom_codecs/">driver
 * documentation on custom codecs</a> for more information and a few usage examples.
 *
 * @param <InnerT> The "inner" Java type; must be a driver supported Java type (that is, there must
 *     exist a codec registered for it).
 * @param <OuterT> The "outer", or target Java type; this codec will handle the mapping to and from
 *     {@code InnerT} and {@code OuterT}.
 */
public abstract class MappingCodec<InnerT, OuterT> implements TypeCodec<OuterT> {

  protected final TypeCodec<InnerT> innerCodec;
  protected final GenericType<OuterT> outerJavaType;

  /**
   * Creates a new mapping codec providing support for {@code OuterT} based on an existing codec for
   * {@code InnerT}.
   *
   * @param innerCodec The inner codec to use to handle instances of InnerT; must not be null.
   * @param outerJavaType The outer Java type; must not be null.
   */
  public MappingCodec(
      @NonNull TypeCodec<InnerT> innerCodec, @NonNull GenericType<OuterT> outerJavaType) {
    this.innerCodec = Objects.requireNonNull(innerCodec, "innerCodec cannot be null");
    this.outerJavaType = Objects.requireNonNull(outerJavaType, "outerJavaType cannot be null");
  }

  /** @return The type of {@code OuterT}. */
  @NonNull
  @Override
  public GenericType<OuterT> getJavaType() {
    return outerJavaType;
  }

  /** @return The type of {@code InnerT}. */
  public GenericType<InnerT> getInnerJavaType() {
    return innerCodec.getJavaType();
  }

  /**
   * Whether this codec is capable of encoding the given Java object.
   *
   * <p>This implementation first checks if the value's runtime type is compatible with {@code
   * OuterT}, then checks if the converted value's runtime type is compatible with {@code InnerT}.
   *
   * <p>The object's Java type is inferred from its runtime (raw) type, contrary to {@link
   * #accepts(GenericType)} which is capable of handling generic types. Therefore, this method can
   * only handle non-parameterized types; codecs handling parameterized types, such as collection
   * types, must override this method and perform some sort of "manual" inspection of the actual
   * type parameters.
   *
   * <p>Contrary to other {@code accept} methods, this method's default implementation is
   * <em>covariant</em> with respect to the passed argument (through the usage of {@link
   * Class#isAssignableFrom(Class)}) and <em>it's strongly recommended not to modify this
   * behavior</em>. This means that, by default, a codec will accept <em>any subtype</em> of the
   * Java type that it has been created for. This is so because codec lookups by arbitrary Java
   * objects only make sense when attempting to encode, never when attempting to decode, and indeed
   * the {@linkplain #encode(Object, ProtocolVersion) encode} method is covariant with {@code
   * JavaTypeT}.
   */
  @Override
  public boolean accepts(@NonNull Object value) {
    Preconditions.checkNotNull(value);
    if (getJavaType().getRawType().isAssignableFrom(value.getClass())) {
      try {
        @SuppressWarnings("unchecked")
        OuterT outer = (OuterT) value;
        InnerT inner = outerToInner(outer);
        return inner == null || innerCodec.accepts(inner);
      } catch (Exception ignored) {
        // fall through
      }
    }
    return false;
  }

  @NonNull
  @Override
  public DataType getCqlType() {
    return innerCodec.getCqlType();
  }

  @Override
  public ByteBuffer encode(OuterT value, @NonNull ProtocolVersion protocolVersion) {
    return innerCodec.encode(outerToInner(value), protocolVersion);
  }

  @Override
  public OuterT decode(ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
    return innerToOuter(innerCodec.decode(bytes, protocolVersion));
  }

  @NonNull
  @Override
  public String format(OuterT value) {
    return innerCodec.format(outerToInner(value));
  }

  @Override
  public OuterT parse(String value) {
    return innerToOuter(innerCodec.parse(value));
  }

  /**
   * Converts from an instance of the inner Java type to an instance of the outer Java type. Used
   * when deserializing or parsing.
   *
   * @param value The value to convert; may be null.
   * @return The converted value; may be null.
   */
  @Nullable
  protected abstract OuterT innerToOuter(@Nullable InnerT value);

  /**
   * Converts from an instance of the outer Java type to an instance of the inner Java type. Used
   * when serializing or formatting.
   *
   * @param value The value to convert; may be null.
   * @return The converted value; may be null.
   */
  @Nullable
  protected abstract InnerT outerToInner(@Nullable OuterT value);
}
