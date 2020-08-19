package org.springframework.security.boot.utils;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.boot.biz.SpringSecurityBizMessageSource;
import org.springframework.security.boot.biz.exception.AuthResponse;
import org.springframework.security.boot.biz.exception.AuthResponseCode;
import org.springframework.security.boot.biz.exception.AuthenticationExceptionAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alibaba.fastjson.JSONObject;

import reactor.core.publisher.Mono;

public class ReactiveSecurityResponseUtils {

	protected static Logger logger = LoggerFactory.getLogger(ReactiveSecurityResponseUtils.class);
	protected static MessageSourceAccessor messages = SpringSecurityBizMessageSource.getAccessor();

	public static Mono<Void> handleSuccess(ServerHttpRequest request, ServerHttpResponse response,
			Authentication authentication) {

		// 1、设置状态码和响应头
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		
		// 2、国际化后的异常信息
		String message = messages.getMessage(AuthResponseCode.SC_AUTHC_SUCCESS.getMsgKey(), LocaleContextHolder.getLocale());
		
		// 3、输出JSON格式数据
        String body = JSONObject.toJSONString(AuthResponse.success(message));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
		
	}
	
	public static Mono<Void> handleFailure(ServerHttpRequest request, ServerHttpResponse response, AuthenticationException e) {
		
		logger.debug("Locale : {}" , LocaleContextHolder.getLocale());
		
    	// 2、设置状态码和响应头
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		
		// 3、国际化后的异常信息
		String message = null;
		AuthResponse<String> authResponse = null;
		if (e instanceof AuthenticationExceptionAdapter) {
			
			AuthenticationExceptionAdapter ex = (AuthenticationExceptionAdapter)e;
			message = messages.getMessage(ex.getCode().getMsgKey(), ex.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(ex.getCode().getCode(), message);
			
		} else if (e instanceof UsernameNotFoundException) {
			message = messages.getMessage(AuthResponseCode.SC_AUTHC_USER_NOT_FOUND.getMsgKey(), e.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(AuthResponseCode.SC_AUTHC_USER_NOT_FOUND.getCode(), message);
		} else if (e instanceof BadCredentialsException) {
			message = messages.getMessage(AuthResponseCode.SC_AUTHC_CREDENTIALS_INCORRECT.getMsgKey(), e.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(AuthResponseCode.SC_AUTHC_CREDENTIALS_INCORRECT.getCode(), message);
		}  else if (e instanceof DisabledException) {
			message = messages.getMessage(AuthResponseCode.SC_AUTHC_USER_DISABLED.getMsgKey(), e.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(AuthResponseCode.SC_AUTHC_USER_DISABLED.getCode(), message);
		}  else if (e instanceof LockedException) {
			message = messages.getMessage(AuthResponseCode.SC_AUTHC_USER_LOCKED.getMsgKey(), e.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(AuthResponseCode.SC_AUTHC_USER_LOCKED.getCode(), message);
		}  else if (e instanceof AccountExpiredException) {
			message = messages.getMessage(AuthResponseCode.SC_AUTHC_USER_EXPIRED.getMsgKey(), e.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(AuthResponseCode.SC_AUTHC_USER_EXPIRED.getCode(), message);
		}  else if (e instanceof CredentialsExpiredException) {
			message = messages.getMessage(AuthResponseCode.SC_AUTHC_CREDENTIALS_EXPIRED.getMsgKey(), e.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(AuthResponseCode.SC_AUTHC_CREDENTIALS_EXPIRED.getCode(), message);
		} else {
			message = messages.getMessage(AuthResponseCode.SC_AUTHC_FAIL.getMsgKey(), e.getMessage(), LocaleContextHolder.getLocale());
			authResponse = AuthResponse.of(AuthResponseCode.SC_AUTHC_FAIL.getCode(), message);
		}
		
		// 4、输出JSON格式数据
		String body = JSONObject.toJSONString(authResponse);
		DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
		
	}
	
}
