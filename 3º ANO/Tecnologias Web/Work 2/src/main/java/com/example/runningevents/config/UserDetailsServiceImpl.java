package com.example.runningevents.config;

import com.example.runningevents.model.Utilizador;
import com.example.runningevents.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Utilizador utlzd =  utilizadorRepository.findByEmail(email);

        if(utlzd!=null){
            return new UtilizadorConfig(utlzd);
        }

        throw new UsernameNotFoundException("Email não encontrado.");
    }
}
