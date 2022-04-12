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
package com.github.searls.jasmine.thirdpartylibs;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static com.github.searls.jasmine.thirdpartylibs.ProjectClassLoaderHelper.projectClassLoaderOf;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClassPathResourceHandlerTest {
  @Mock
  private Request baseRequest;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private Resource resource;
  @Mock
  private PrintWriter writer;

  private ClassPathResourceHandler subject;

  @BeforeEach
  public void before() {
    String jquery = "src/test/resources/webjars/jquery-1.10.2.jar";
    subject = new ClassPathResourceHandler(projectClassLoaderOf(jquery));
  }

  @Test
  public void whenTargetStartsWithClassPathThenRespondWithResourceContent() throws Exception {
    // given
    when(baseRequest.isHandled()).thenReturn(false);
    when(response.getWriter()).thenReturn(writer);

    // when
    subject.handle("/META-INF/resources/webjars/jquery/1.10.2/jquery.js", baseRequest, request, response);

    // then
    verify(writer).write(contains("jQuery JavaScript Library v1.10.2"));
    verify(baseRequest).setHandled(true);
  }

  @Test
  public void whenResourceIsMissingThenDoNotProcess() throws Exception {
    // given
    when(baseRequest.isHandled()).thenReturn(false);

    // when
    subject.handle("/notExistingResource", baseRequest, request, response);

    // then
    verify(writer, never()).write(anyString());
    verify(baseRequest, never()).setHandled(true);
  }
}
