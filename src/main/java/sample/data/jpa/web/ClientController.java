package sample.data.jpa.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.data.jpa.dao.ClientDao;
import sample.data.jpa.domain.Client;
import sample.data.jpa.dto.UserDto;

import sample.data.jpa.AppException;
import sample.data.jpa.Error;
import sample.data.jpa.security.AuthenticationProvider;
import sample.data.jpa.security.JwtUtils;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class ClientController {

    @Autowired
    private ClientDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<UserDto> register(@RequestBody UserDto.Register register) {

        userDao.findByEmail(register.getEmail()).ifPresent(
                entity -> {
                    throw new AppException(Error.DUPLICATED_USER);
                }
        );
        Client client = new Client(
                register.getName(),
                register.getEmail(),
                passwordEncoder.encode(register.getPassword())
        );
        userDao.save(client);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION
                ).body(
                        UserDto.builder()
                                .name(client.getName())
                                .email(client.getEmail())
                                .id(client.getId())
                                .token( jwtUtils.encode(client.getEmail()))
                                .build()
                );
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserDto.Auth> getClientByID(@PathVariable("id") Long id) {
        Optional<Client> client = userDao.findById(id);
        if( client.isPresent()){
            return ResponseEntity.ok()
                    .body(
                            UserDto.Auth.builder()
                                    .name(client.get().getName())
                                    .email(client.get().getEmail())
                                    .id(client.get().getId())
                                    .build()
                    );
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/getByEmail/{email}")
    @ResponseBody
    public ResponseEntity<UserDto.Auth> getClientByEmail(@PathVariable("email") String email) {
        Optional<Client> client = userDao.findByEmail(email);
        if( client.isPresent()){
            return ResponseEntity.ok()
                    .body(
                            UserDto.Auth.builder()
                                    .name(client.get().getName())
                                    .email(client.get().getEmail())
                                    .id(client.get().getId())
                                    .build()
                    );
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<UserDto> login(@Valid @RequestBody UserDto.Login login) {

        Client client = userDao.findByEmail(login.getEmail()).filter(
                user -> passwordEncoder.matches(login.getPassword(), user.getPassword())
        ).orElseThrow(() -> new AppException(Error.LOGIN_INFO_INVALID));
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION
                ).body(
                        UserDto.builder()
                                .name(client.getName())
                                .email(client.getEmail())
                                .id(client.getId())
                                .token( jwtUtils.encode(client.getEmail()))
                                .build()
                );
    }
}