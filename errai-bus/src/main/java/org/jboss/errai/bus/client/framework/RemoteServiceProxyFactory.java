/*
 * Copyright 2011 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.bus.client.framework;

import com.google.gwt.core.client.GWT;

import java.util.HashMap;
import java.util.Map;

public class RemoteServiceProxyFactory implements ProxyProvider {
  private static Map<Class, Object> remoteProxies = new HashMap<Class, Object>();

  @Override
  @SuppressWarnings({"unchecked"})
  public <T> T getRemoteProxy(Class<T> proxyType) {
    if (remoteProxies.isEmpty()) {
      throw new RuntimeException("There are no remote proxies registered yet. If this error is encountered on the server, ");
    }
    return (T) remoteProxies.get(proxyType);
  }

  public static void addRemoteProxy(Class proxyType, Object proxy) {
    remoteProxies.put(proxyType, proxy);
  }
}
