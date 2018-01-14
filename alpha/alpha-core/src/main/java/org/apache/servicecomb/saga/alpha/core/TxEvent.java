/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.saga.alpha.core;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name = "TxEvent")
public class TxEvent {
  @Transient
  public static final long MAX_TIMESTAMP = 253402214400000L; // 9999-12-31 00:00:00

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long surrogateId;

  private String serviceName;
  private String instanceId;
  private Date creationTime;
  private String globalTxId;
  private String localTxId;
  private String parentTxId;
  private String type;
  private String compensationMethod;
  private Date expiryTime;
  private byte[] payloads;
  private int retries;
  private String retriesMethod;

  private TxEvent() {
  }

  public TxEvent(TxEvent event) {
    this(event.surrogateId,
        event.serviceName,
        event.instanceId,
        event.creationTime,
        event.globalTxId,
        event.localTxId,
        event.parentTxId,
        event.type,
        event.compensationMethod,
        event.expiryTime,
        event.payloads);
  }

  public TxEvent(
      String serviceName,
      String instanceId,
      String globalTxId,
      String localTxId,
      String parentTxId,
      String type,
      String compensationMethod,
      byte[] payloads) {
    this(serviceName, instanceId, new Date(), globalTxId, localTxId, parentTxId, type, compensationMethod, 0, payloads);
  }

  public TxEvent(
      String serviceName,
      String instanceId,
      String globalTxId,
      String localTxId,
      String parentTxId,
      String type,
      String compensationMethod,
      int timeout,
      byte[] payloads) {
    this(-1L, serviceName, instanceId, new Date(), globalTxId, localTxId, parentTxId, type, compensationMethod, timeout,
        "", 0, payloads);
  }

  public TxEvent(
      String serviceName,
      String instanceId,
      Date creationTime,
      String globalTxId,
      String localTxId,
      String parentTxId,
      String type,
      String compensationMethod,
      int timeout,
      byte[] payloads) {
    this(-1L, serviceName, instanceId, creationTime, globalTxId, localTxId, parentTxId, type, compensationMethod,
        timeout, "", 0, payloads);
  }

  TxEvent(Long surrogateId,
      String serviceName,
      String instanceId,
      Date creationTime,
      String globalTxId,
      String localTxId,
      String parentTxId,
      String type,
      String compensationMethod,
      int timeout,
      byte[] payloads) {
    this(surrogateId, serviceName, instanceId, creationTime, globalTxId, localTxId, parentTxId, type,
        compensationMethod,
        timeout == 0 ? new Date(MAX_TIMESTAMP) : new Date(creationTime.getTime() + SECONDS.toMillis(timeout)),
        payloads);
  }

  TxEvent(Long surrogateId,
      String serviceName,
      String instanceId,
      Date creationTime,
      String globalTxId,
      String localTxId,
      String parentTxId,
      String type,
      String compensationMethod,
      Date expiryTime,
      String retriesMethod,
      int retries,
      byte[] payloads) {
    this.surrogateId = surrogateId;
    this.serviceName = serviceName;
    this.instanceId = instanceId;
    this.creationTime = creationTime;
    this.globalTxId = globalTxId;
    this.localTxId = localTxId;
    this.parentTxId = parentTxId;
    this.type = type;
    this.compensationMethod = compensationMethod;
    this.expiryTime = expiryTime;
    this.retriesMethod = retriesMethod;
    this.retries = retries;
    this.payloads = payloads;
  }

  public String serviceName() {
    return serviceName;
  }

  public String instanceId() {
    return instanceId;
  }

  public Date creationTime() {
    return creationTime;
  }

  public String globalTxId() {
    return globalTxId;
  }

  public String localTxId() {
    return localTxId;
  }

  public String parentTxId() {
    return parentTxId;
  }

  public String type() {
    return type;
  }

  public String compensationMethod() {
    return compensationMethod;
  }

  public byte[] payloads() {
    return payloads;
  }

  public long id() {
    return surrogateId;
  }

  public Date expiryTime() {
    return expiryTime;
  }

  @Override
  public String toString() {
    return "TxEvent{" +
        "surrogateId=" + surrogateId +
        ", serviceName='" + serviceName + '\'' +
        ", instanceId='" + instanceId + '\'' +
        ", creationTime=" + creationTime +
        ", globalTxId='" + globalTxId + '\'' +
        ", localTxId='" + localTxId + '\'' +
        ", parentTxId='" + parentTxId + '\'' +
        ", type='" + type + '\'' +
        ", compensationMethod='" + compensationMethod + '\'' +
        ", expiryTime='" + expiryTime + '\'' +
        '}';
  }

  public int retries() {
    return retries;
  }

  public String retriesMethod() {
    return retriesMethod;
  }

  public boolean containChildren(TxEvent event) {
    return this.localTxId.equals(event.parentTxId);
  }
}
