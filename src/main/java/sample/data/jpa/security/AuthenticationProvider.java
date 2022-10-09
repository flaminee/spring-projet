package sample.data.jpa.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import sample.data.jpa.dao.ClientDao;
import sample.data.jpa.dao.ProfDao;
import sample.data.jpa.domain.Client;
import sample.data.jpa.domain.Prof;
import sample.data.jpa.domain.Role;
import sample.data.jpa.domain.User;
import sample.data.jpa.dto.UserDto;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public Authentication getAuthentication(String username) {
        UserDetails userDetail = userDetailsService.loadUserByUsername(username);
        if (userDetail == null) return null;

        User user = (User) userDetail;

        UserDto.Auth authenticatedUser = UserDto.Auth.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return new UsernamePasswordAuthenticationToken(authenticatedUser, "", user.getAuthorities());
    }
}
