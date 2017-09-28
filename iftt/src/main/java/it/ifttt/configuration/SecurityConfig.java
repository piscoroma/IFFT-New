package it.ifttt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import it.ifttt.security.RestAccessDeniedHandler;
import it.ifttt.security.RestUnauthorizedEntryPoint;


@Configuration
@EnableWebSecurity(debug=true)
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService simpleUserDetailsService;
	
	@Autowired
    private RestUnauthorizedEntryPoint restUnauthorizedEntryPoint;
	
	@Autowired
	private RestAccessDeniedHandler restAccessDeniedHandler;
	
	@Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler restAuthenticationFailureHandler;
	
	@Autowired
	private LogoutSuccessHandler restLogoutSuccessHandler;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Configura gli utenti e i ruoli
		auth.userDetailsService(simpleUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Configura le URL da proteggere
		 http
         .headers().disable()
         .csrf().disable()
         
         //.authenticationProvider(authenticationProvider())
           
         .authorizeRequests()
             .antMatchers("/**").permitAll()
             //.antMatchers("/helloWorld", "/index.jsp", "/login").permitAll()
         	 //.antMatchers("/**").authenticated()
             .and()
         .exceptionHandling()
             .authenticationEntryPoint(restUnauthorizedEntryPoint)
             .accessDeniedHandler(restAccessDeniedHandler)
             .and()
         .formLogin()
             //.loginProcessingUrl("/authenticate")
             .successHandler(restAuthenticationSuccessHandler)
             .failureHandler(restAuthenticationFailureHandler)
             .usernameParameter("username")
             .passwordParameter("password")
             .permitAll()
             .and()
         // ???
            /* 
         .apply(new SpringSocialConfigurer()
             .postLoginUrl("/")
             .defaultFailureUrl("/#/login")
             .alwaysUsePostLoginUrl(true))
             .and()*/
         .logout()
             .logoutUrl("/logout")
             .logoutSuccessHandler(restLogoutSuccessHandler)
             .deleteCookies("JSESSIONID")
             .permitAll();/*
             .and()
         // ???
         .rememberMe()
             .tokenRepository(persistentTokenRepository())
             .key(REMEMBER_ME_KEY);*/
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//Configura la catena dei filtri di sicurezza
		super.configure(web);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
	  	return new BCryptPasswordEncoder();
	}
	
	@Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

	
}
