/*
 * Copyright 2012 JBoss, by Red Hat, Inc
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

package org.jboss.errai.security.shared;

import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.common.client.api.annotations.Alias;
import org.jboss.errai.common.client.api.interceptor.InterceptedCall;
import org.jboss.errai.security.client.local.SecurityRoleInterceptor;
import org.jboss.errai.ui.shared.api.annotations.style.StyleBinding;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the service can only be accessed by logged-in users who belong
 * to all of the given security roles.
 * <p>
 * This annotation can appear anywhere the {@link Service} annotation can
 * appear.
 *
 * @see Service
 */
@Alias
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@InterceptedCall(SecurityRoleInterceptor.class)
@StyleBinding
@InterceptorBinding
public @interface RequireRoles {

  /**
   * The set of roles that the calling user must belong to in order to invoke
   * the target service.
   */
  @Nonbinding
  String[] value();
}
