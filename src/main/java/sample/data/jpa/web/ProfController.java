package sample.data.jpa.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.data.jpa.AppException;
import sample.data.jpa.Error;
import sample.data.jpa.dao.ProfDao;
import sample.data.jpa.domain.Client;
import sample.data.jpa.domain.Prof;
import sample.data.jpa.dto.UserDto;
import sample.data.jpa.security.JwtUtils;

import java.util.Optional;


@Controller
@RequestMapping("/prof")
public class ProfController {

    @Autowired
    private ProfDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<UserDto> register(@RequestBody UserDto.Register register) {

       if( userDao.findByEmail(register.getEmail()).isPresent() ){
           return ResponseEntity.unprocessableEntity()
                   .header(
                           HttpHeaders.AUTHORIZATION,
                           Error.DUPLICATED_USER.getMessage() //token
                   ).build();
       }
        Prof user = new Prof(
                register.getName(),
                register.getEmail(),
                passwordEncoder.encode(register.getPassword())
        );
        userDao.save(user);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION
                ).body(
                        UserDto.builder()
                                .name(user.getName())
                                .email(user.getEmail())
                                .id(user.getId())
                                .token(jwtUtils.encode(user.getEmail()) )
                                .build()
                );

    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserDto.Auth> getClientByID(@PathVariable("id") Long id) {
        Optional<Prof> prof = userDao.findById(id);
        if( prof.isPresent()){
            return ResponseEntity.ok()
                    .body(
                            UserDto.Auth.builder()
                                    .name(prof.get().getName())
                                    .email(prof.get().getEmail())
                                    .id(prof.get().getId())
                                    .build()
                    );
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/getByEmail/{email}")
    @ResponseBody
    public ResponseEntity<UserDto.Auth> getClientByEmail(@PathVariable("email") String email) {
        Optional<Prof> prof = userDao.findByEmail(email);
        if( prof.isPresent()){
            return ResponseEntity.ok()
                    .body(
                            UserDto.Auth.builder()
                                    .name(prof.get().getName())
                                    .email(prof.get().getEmail())
                                    .id(prof.get().getId())
                                    .build()
                    );
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<UserDto> login(@RequestBody UserDto.Login login) {

        Prof prof = userDao.findByEmail(login.getEmail()).filter(
                user -> passwordEncoder.matches(login.getPassword(), user.getPassword())
        ).orElseThrow(() -> new AppException(Error.LOGIN_INFO_INVALID));

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION
                ).body(
                        UserDto.builder()
                                .name(prof.getName())
                                .email(prof.getEmail())
                                .id(prof.getId())
                                .token(jwtUtils.encode(prof.getEmail()) )
                                .build()
                );
    }
}
