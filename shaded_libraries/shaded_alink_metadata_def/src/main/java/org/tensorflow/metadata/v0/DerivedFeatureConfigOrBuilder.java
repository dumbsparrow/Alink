// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: statistics.proto

package org.tensorflow.metadata.v0;

public interface DerivedFeatureConfigOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tensorflow.metadata.v0.DerivedFeatureConfig)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.tensorflow.metadata.v0.AllowlistDeriver allowlist = 1;</code>
   * @return Whether the allowlist field is set.
   */
  boolean hasAllowlist();
  /**
   * <code>.tensorflow.metadata.v0.AllowlistDeriver allowlist = 1;</code>
   * @return The allowlist.
   */
  AllowlistDeriver getAllowlist();
  /**
   * <code>.tensorflow.metadata.v0.AllowlistDeriver allowlist = 1;</code>
   */
  AllowlistDeriverOrBuilder getAllowlistOrBuilder();

  public DerivedFeatureConfig.TypeCase getTypeCase();
}