/*-
 * #%L
 * jasmine-maven-plugin
 * %%
 * Copyright (C) 2010 - 2017 Justin Searls
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.searls.jasmine.server;

import com.github.searls.jasmine.config.JasmineConfiguration;
import com.github.searls.jasmine.runner.CreatesRunner;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JasmineResourceHandler extends ResourceHandler {

  private final CreatesRunner createsRunner;
  private final JasmineConfiguration configuration;

  public JasmineResourceHandler(CreatesRunner createsRunner, JasmineConfiguration configuration) {
    this.createsRunner = createsRunner;
    this.configuration = configuration;
  }

  @Override
  public void handle(String target,
                     Request baseRequest,
                     HttpServletRequest request,
                     HttpServletResponse response) throws IOException, ServletException {
    this.createSpecRunnerIfNecessary(target);
    response.addDateHeader("EXPIRES", 0L);
    super.handle(target, baseRequest, baseRequest, response);
  }

  private void createSpecRunnerIfNecessary(String target) throws IOException {
    if ("/".equals(target)) {
      this.createsRunner.create(configuration);
    }
  }

}
