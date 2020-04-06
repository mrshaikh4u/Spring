package com.rs4u.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource dataSource;

	@Value("${security.jwt.client-id}")
	private String clientId;

	@Value("${security.jwt.client-secret}")
	private String clientSecret;

	@Value("#{'${security.jwt.grant-type}'.split(',')}")
	private List<String> grantType;

	@Value("#{'${security.jwt.scope}'.split(',')}")
	private List<String> scopes;

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;

	/*
	 * @Bean public TokenStore tokenStore() { return new InMemoryTokenStore(); }
	 */

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		       .withClient(clientId)
		       .authorizedGrantTypes(grantType.toArray(new String[grantType.size()]))
		       .scopes(scopes.toArray(new String[scopes.size()]))
		       .resourceIds(resourceIds)
		       .accessTokenValiditySeconds(5000)
		       .secret(clientSecret)
		       .refreshTokenValiditySeconds(50000);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
		         .tokenStore(tokenStore());
		// .allowedTokenEndpointRequestMethods(HttpMethod.GET,
		// HttpMethod.POST);
		;
	}

}