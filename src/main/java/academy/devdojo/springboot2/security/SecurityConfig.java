package academy.devdojo.springboot2.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
//Anotacao com a opcao de habilitar o PreAuthorize no post
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories
                .createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser("vini")
                .password(passwordEncoder.encode("test"))
                .roles("USER","ADMIN")
                .and()
                .withUser("panga")
                .password(passwordEncoder.encode("test1@"))
                .roles("USER");
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
                .httpBasic();
    }
}
