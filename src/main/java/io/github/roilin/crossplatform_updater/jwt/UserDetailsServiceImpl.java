package io.github.roilin.crossplatform_updater.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository UserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

}