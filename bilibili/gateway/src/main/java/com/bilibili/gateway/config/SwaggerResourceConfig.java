package com.bilibili.gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component
@Primary
@AllArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

	/**
	 * swagger2默认的url后缀（v2或v3）
	 */
	private static final String SWAGGER2URL = "/v2/api-docs";
//	private static final String PREFIX = "/api-docs";  // 统一前缀

	/**
	 * 网关路由
	 */
	@Resource
	private final RouteLocator routeLocator;


	/**
	 * 汇总所有微服务的swagger文档路径(访问swagger页面时，会调用此方法获取docs)
	 *
	 * @return
	 */
	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		// 从网关路由中获取所有服务的host名
		List<String> routeHosts = new LinkedList<>();
		routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
				.subscribe(route -> routeHosts.add(route.getUri().getHost()));
		// 将文档列表排序，服务多的时候以免显得混乱
		Collections.sort(routeHosts);
		// 不需要展示Swagger的服务列表,在swagger ui页面右上角下拉框中，将不再展示(改成你自己不需要展示的服务)
		List<String> ignoreServers = Arrays
				.asList( "sever1", "server2");
		// 记录已经添加过的微服务（有些服务部署多个节点，过滤掉重复的）
		Set<String> docsUrls = new HashSet<>();
		for (String host : routeHosts) {
			if (!ignoreServers.contains(host)) {
				String url = "/" + host + SWAGGER2URL;
				if (docsUrls.add(url)) {
					SwaggerResource swaggerResource = new SwaggerResource();
					swaggerResource.setUrl(url);
					swaggerResource.setName(host);
					resources.add(swaggerResource);
				}
			}
		}
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location, String version) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(version);
		return swaggerResource;
	}
}
