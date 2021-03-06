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
package org.apache.qpid.jms.transports;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import javax.net.ssl.SSLContext;

import io.netty.buffer.ByteBuf;

/**
 * Base class for all QpidJMS Transport instances.
 */
public interface Transport {

    /**
     * Performs the connect operation for the implemented Transport type
     * such as a TCP socket connection, SSL/TLS handshake etc.
     *
     * @param sslContextOverride
     *          a user-provided SSLContext to use if establishing a secure
     *          connection, overrides applicable URI configuration
     *
     * @throws IOException if an error occurs while attempting the connect.
     */
    void connect(SSLContext sslContextOverride) throws IOException;

    /**
     * @return true if transport is connected or false if the connection is down.
     */
    boolean isConnected();

    /**
     * @return true if transport is connected using a secured channel (SSL).
     */
    boolean isSecure();

    /**
     * Close the Transport, no additional send operations are accepted.
     *
     * @throws IOException if an error occurs while closing the connection.
     */
    void close() throws IOException;

    /**
     * Request that the Transport provide an output buffer sized for the given
     * value.
     *
     * @param size
     *        the size necessary to hold the outgoing bytes.
     *
     * @return a new ByteBuf allocated for sends operations.
     *
     * @throws IOException if an error occurs while allocating the send buffer.
     */
    ByteBuf allocateSendBuffer(int size) throws IOException;

    /**
     * Sends a chunk of data over the Transport connection.
     *
     * @param output
     *        The buffer of data that is to be transmitted.
     *
     * @throws IOException if an error occurs during the send operation.
     */
    void send(ByteBuf output) throws IOException;

    /**
     * Gets the currently set TransportListener instance
     *
     * @return the current TransportListener or null if none set.
     */
    TransportListener getTransportListener();

    /**
     * Sets the Transport Listener instance that will be notified of incoming data or
     * error events.
     *
     * @param listener
     *        The new TransportListener instance to use (cannot be null).
     *
     * @throws IllegalArgumentException if the given listener is null.
     */
    void setTransportListener(TransportListener listener);

    /**
     * @return the TransportOptions instance that holds the configuration for this Transport.
     */
    TransportOptions getTransportOptions();

    /**
     * @return the URI of the remote peer that this Transport connects to.
     */
    URI getRemoteLocation();

    /**
     * @return the local principal for a Transport that is using a secure connection.
     */
    Principal getLocalPrincipal();

    /**
     * Sets the Maximum Frame Size the transport should accept from the remote.  This option
     * is not applicable to all transport types, those that support validating the incoming
     * frame size should apply the configured value.
     *
     * @param maxFrameSize
     * 		The maximum frame size to accept from the remote.
     */
    void setMaxFrameSize(int maxFrameSize);

    /**
     * Returns the currently configured maximum frame size setting.
     *
     * @return the current max frame size setting for this transport.
     */
    int getMaxFrameSize();

}
