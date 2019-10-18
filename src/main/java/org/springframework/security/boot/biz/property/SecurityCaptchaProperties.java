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
package org.springframework.security.boot.biz.property;

import org.springframework.security.boot.biz.authentication.PostRequestAuthenticationProcessingFilter;

public class SecurityCaptchaProperties {

	public static final String DEFAULT_SESSION_CAPTCHA_KEY = "KAPTCHA_SESSION_KEY";
	
	/**
	 * The request parameter name of the captcha
	 */
	private String paramName = PostRequestAuthenticationProcessingFilter.SPRING_SECURITY_FORM_CAPTCHA_KEY;
	/**
	 * Whether to captcha required
	 */
	private boolean required = false;
	
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
 
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
