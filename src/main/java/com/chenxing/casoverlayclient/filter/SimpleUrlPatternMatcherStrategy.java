package com.chenxing.casoverlayclient.filter;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于过滤不需要登录的url，需要实现UrlPatternMatcherStrategy 接口，在matches 添加不需要用户登录的类
 */
public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public boolean matches(String url) {
		log.info("---系统2-过滤不需要登陆的url");
		// 当含有loginout的字段，就可以不用登录了
		return url.contains("/loginOut/success");
	}

	/**
	 * 正则表达式的规则，这个地方可以是web传递过来的
	 */
	@Override
	public void setPattern(String pattern) {
		log.info("---配置正则表达式的规则");
		// TODO Auto-generated method stub

	}

}
