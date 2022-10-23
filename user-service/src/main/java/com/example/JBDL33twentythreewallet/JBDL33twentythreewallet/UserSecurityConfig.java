package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()// if not added, it will not return a JSON, only return HTML response
                .and()
                .csrf().disable() //needs to be disabled in order for the non secure post request to work
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/user/**").permitAll() //signup for new account: enable for everyone to create their own account
                .antMatchers("/user/**").hasAuthority(UserConstants.USER_AUTHORITY) //user driven actions
                .antMatchers("/**").hasAnyAuthority(UserConstants.ADMIN_AUTHORITY, UserConstants.SERVICE_AUTHORITY) // admin or service (txn) driven actions
                .and()
                .formLogin();
    }

}
