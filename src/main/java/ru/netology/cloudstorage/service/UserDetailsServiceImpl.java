package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.exception.BadCredentials;
import ru.netology.cloudstorage.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static ru.netology.cloudstorage.exception.Constants.USER_NOT_FOUND;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new BadCredentials(USER_NOT_FOUND.get()));
    }
}
