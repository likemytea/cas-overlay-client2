package com.chenxing.casoverlayclient;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class CasOverlayClientApplication {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	public static void main(String[] args) {
		SpringApplication.run(CasOverlayClientApplication.class, args);
	}

	// url的前缀
	private static final String CAS_SERVER_URL_PREFIX = "https://test.mycasdomain.com/cas";
	// 本机的名称
	private static final String SERVER_NAME = "https://test.huayubenji.com:8444";

	/**
	 * 登录过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterSingleRegistration() {
		log.info("---登陆过滤器");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new SingleSignOutFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerUrlPrefix", CAS_SERVER_URL_PREFIX);
		registration.setInitParameters(initParameters);
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * 过滤验证器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterValidationRegistration() {
		log.info("---过滤验证器");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerUrlPrefix", CAS_SERVER_URL_PREFIX);
		initParameters.put("serverName", SERVER_NAME);
		initParameters.put("useSession", "true");
		registration.setInitParameters(initParameters);
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * 授权过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterAuthenticationRegistration() {
		log.info("---授权过滤器");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new AuthenticationFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerLoginUrl", CAS_SERVER_URL_PREFIX);
		initParameters.put("serverName", SERVER_NAME);
		initParameters.put("ignorePattern", ".*");
		// 表示过滤所有
		initParameters.put("ignoreUrlPatternType",
				"com.chenxing.casoverlayclient.filter.SimpleUrlPatternMatcherStrategy");

		registration.setInitParameters(initParameters);
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * wraper过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterWrapperRegistration() {
		log.info("---wraper过滤器");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new HttpServletRequestWrapperFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * 添加监听器
	 * 
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
		log.info("---启动监听器");
		ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
		registrationBean.setListener(new SingleSignOutHttpSessionListener());
		registrationBean.setOrder(1);
		return registrationBean;
	}

	/**
	 * @author liuxing 创建日期:2018/02/05 设定首页
	 */
	@Configuration
	public class DefaultView extends WebMvcConfigurerAdapter {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			log.info("---WebMvcConfigurerAdapter");
			// 设定首页为index
			registry.addViewController("/").setViewName("forward:/index");

			// 设定匹配的优先级
			registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

			// 添加视图控制类
			super.addViewControllers(registry);
		}
	}
}
