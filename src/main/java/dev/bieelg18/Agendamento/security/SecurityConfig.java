package dev.bieelg18.Agendamento.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final NaoAutenticadoHandler naoAutenticadoHandler;
    private final SemPermissaoHandler semPermissaoHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception{
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(naoAutenticadoHandler)
                        .accessDeniedHandler(semPermissaoHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        //Rotas de cadastro e login, qualquer usuário pode acessar
                        .requestMatchers(HttpMethod.POST, "/usuarios", "/auth/login")
                        .permitAll()

                        //Rota POST para criar um novo agendamento
                                .requestMatchers(HttpMethod.POST, "/agendamentos")
                                .authenticated()

                        //Rota para alterar os dados de cadastro de quem chamou
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/me")
                        .authenticated()

                        //Rota para listar os próprios agendamentos
                        .requestMatchers(HttpMethod.GET, "/agendamentos/me")
                        .authenticated()

                        //Rotas GET que precisa ter permissão de ADMIN
                        .requestMatchers(HttpMethod.GET, "/usuarios", "/usuarios/all",
                                "/usuarios/buscar", "/servicos", "/agendamentos", "/agendamentos/status")
                        .hasRole("ADMIN")

                        //Rotas PATCH que precisa ter permissão de ADMIN
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/permissao/**",
                                "/servicos/**")
                        .hasRole("ADMIN")

                        //Rotas DELETE que precisa ter permissão de ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**",
                                "/servicos/**", "/agendamentos/**")
                        .hasRole("ADMIN")

                        //Rota POST que precisa ter permissão de ADMIN
                        .requestMatchers(HttpMethod.POST, "/servicos")
                        .hasRole("ADMIN")

                        //Rota que precisa ter permissão de PROFISSIONAL
                        .requestMatchers(HttpMethod.PATCH, "/agendamentos/status/**")
                        .hasRole("PROFISSIONAL")


                ).addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}
