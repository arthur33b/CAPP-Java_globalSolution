package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.repository.ProfessorRepository;
import org.example.repository.AlunoRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tenta buscar como professor
        return professorRepository.findByEmail(email)
                .map(professor -> User.builder()
                        .username(professor.getEmail())
                        .password(professor.getSenha())
                        .authorities(new ArrayList<>())
                        .build())
                .or(() -> alunoRepository.findByEmail(email)
                        .map(aluno -> User.builder()
                                .username(aluno.getEmail())
                                .password(aluno.getSenha())
                                .authorities(new ArrayList<>())
                                .build()))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}
