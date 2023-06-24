package com.prorental.carrental.security.config;

import com.prorental.carrental.security.AuthEntryPointJwt;
import com.prorental.carrental.security.AuthTokenFilter;
import com.prorental.carrental.security.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//bu sinif
//End pointlerin korunacagi config sinifi burasi
//izin verilsin mi verilmesin mi?
//hangi filtreyi kullan diyecegimiz yer burasi
@Configuration// this will added to application context
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//method level security
//@EnableMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    //to handle unauthorized access
    private final AuthEntryPointJwt unAuthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //front end backend cagiriyor. cors disabled olacak ki bu ikisi ayni serverdan calissin.
        //Burada bir dizi ayari ayni anda yaptik. mesela eger endpoint register olarak gelirse izin ver digerlerine
        // otorize et
    http.csrf().disable().cors().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
            and().authorizeHttpRequests().antMatchers("/register").permitAll().antMatchers("/").authenticated();
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().cors().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/register").permitAll()
//                .anyRequest().authenticated();
//    }

    @Override
    public void configure(WebSecurity web){
        System.out.println("Ebeni sikim subform");
    }

}
