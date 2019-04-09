/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.security.boot.biz.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.boot.biz.exception.AuthMethodNotSupportedException;
import org.springframework.security.boot.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.alibaba.fastjson.JSONObject;

public class HttpServletRequestAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public HttpServletRequestAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {
		/*
		 * if Rest request return 401 Unauthorized else rediect to specific page
		 */
		if (WebUtils.isPostRequest(request)) {
			
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			
			if (e instanceof BadCredentialsException) {
				JSONObject.writeJSONString(response.getWriter(), HttpServletRequestErrorResponse.of("Invalid username or password", HttpStatus.UNAUTHORIZED));
			} else if (e instanceof AuthMethodNotSupportedException) {
			    JSONObject.writeJSONString(response.getWriter(), HttpServletRequestErrorResponse.of(e.getMessage(), HttpStatus.UNAUTHORIZED));
			} else {
				JSONObject.writeJSONString(response.getWriter(), HttpServletRequestErrorResponse.of("Authentication failed", HttpStatus.UNAUTHORIZED));
			}
		} else {
			super.commence(request, response, e);
		}

	}

}
