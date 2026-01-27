/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.archivebook.archivebook.config;

import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author 7N
 */
@Service
public class AppUserDetailService {
    private UsuarioRepository UsuarioRepository;

    public AppUserDetailService(
            UsuarioRepository UsuarioRepository
    ) {
        this.UsuarioRepository = UsuarioRepository;
    }    
    
    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/user-details-service.html
    // https://medium.com/@davoud.badamchi/building-secure-spring-boot-applications-with-database-authentication-a-comprehensive-guide-6c8171979b5a
    @Bean
    UserDetailsService customUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                Optional<Usuario> optUser = UsuarioRepository.findByCorreo(correo);
                if (optUser.isPresent()) {
                    Usuario appUser = optUser.get();
                    return User.builder()
                            .username(appUser.getCorreo())
                            .password(appUser.getContrasena())
                            //.roles(appUser.getRoles().split(","))
                            //.roles("USER", "ADMIN")
                            .roles(appUser.getRole())
                            .build();
                } else {
                    throw new UsernameNotFoundException(correo);
                }
            }
        };
    }
}
