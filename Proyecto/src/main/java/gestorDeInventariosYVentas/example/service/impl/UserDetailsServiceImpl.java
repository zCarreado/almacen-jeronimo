package gestorDeInventariosYVentas.example.service.impl;

import gestorDeInventariosYVentas.example.dto.input.AuthCreateUserRequest;
import gestorDeInventariosYVentas.example.dto.input.AuthLoginRequest;
import gestorDeInventariosYVentas.example.dto.output.AuthResponse;
import gestorDeInventariosYVentas.example.model.UserEntity;
import gestorDeInventariosYVentas.example.repository.UserEntityRepository;
import gestorDeInventariosYVentas.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private  JwtUtils jwtUtils;

    @Autowired
    private  UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Buscar el usuario en la base de datos
        UserEntity userEntity = userEntityRepository.findUserEntityByUserName(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));

        //Convertir los rolesEnum en GrantedAuthority
        Set<GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

        return User.builder()
                .username(userEntity.getUserName())
                .password(userEntity.getPassword())
                .authorities(authorities)
                .accountLocked(!userEntity.isAccountNoLocked())
                .accountExpired(!userEntity.isAccountNoExpired())
                .credentialsExpired(!userEntity.isCredencialNoExpired())
                .disabled(!userEntity.isEnabled())
                .build();
    }

    public AuthResponse loginUser (AuthLoginRequest authLoginRequest){
        String username = authLoginRequest.getUsername();
        String password = authLoginRequest.getPassword();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(username,"User logged in successfully", accessToken, true);
    }

    public Authentication authenticate (String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
        String username = authCreateUserRequest.getUsername();
        String password = authCreateUserRequest.getPassword();
        Set<String> rolesRequest = authCreateUserRequest.getRoles();

        Set<UserEntity.rolesEnum> roles = rolesRequest.stream()
                .map(roleName -> {
                    try {
                        return UserEntity.rolesEnum.valueOf(roleName); // Convertir nombre de rol a rolesEnum
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid role: " + roleName);
                    }
                })
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("The roles specified do not exist.");
        }

        UserEntity userEntity = UserEntity.builder()
                .userName(username)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credencialNoExpired(true)
                .build();

        UserEntity savedUser = userEntityRepository.save(userEntity);

        String accessToken = jwtUtils.createToken(new UsernamePasswordAuthenticationToken(
                savedUser.getUserName(),
                savedUser.getPassword(),
                savedUser.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList())
        ));

        return new AuthResponse(
                savedUser.getUserName(),
                "User created successfully",
                accessToken,
                true
        );
    }
}
