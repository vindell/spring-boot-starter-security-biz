/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Security Logout Properties
 * @author 		： <a href="https://github.com/hiwepy">wandl</a>
 */
@Getter
@Setter
@ToString
public class SecurityLogoutProperties {

	/** 注销地址：会话注销后的重定向地址 */
	private String logoutUrl;
	private String pathPatterns = "/logout";
	/** 注销地址：会话注销后的重定向地址 */
	private String logoutSuccessUrl;

	private boolean invalidateHttpSession = true;
	private boolean clearAuthentication = true;

}
