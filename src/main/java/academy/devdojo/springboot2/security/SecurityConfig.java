package academy.devdojo.springboot2.security;

import academy.devdojo.springboot2.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2

//Anotacao com a opcao de habilitar o PreAuthorize no post
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DevDojoUserDetailsService devDojoUserDetailsService;

    /*Filtros de autentição do spring
        * BasicAuthenticationFilter - Usa encode base64.
        * UsernamePasswordAuthenticationFilter - usuario e senha
        * DefaultLoginPageGeneratingFilter
        * DefaultLogoutPageGeneratingFilter
        * FilterSecurityInterceptor
     */


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.devDojoUserDetailsService)
                .passwordEncoder(PasswordEncoderFactories
                        .createDelegatingPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /* httponly false é necessário para aplicacoes react, node entre outras
        conseguirem pegar o valor do cookie
         */

        //  csrf deve ser habilitado em ambiente de producao
        http.csrf().disable()
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()
                .authorizeRequests().anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }
}
