package sample.data.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.*;


@Getter
@AllArgsConstructor
@Builder
public class UserDto {

    private String name;
    private String email;
    private String token;
    private Long id;


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiIgnore
    public static class Register {
        @NotNull
        private String name;
        @NotNull
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Login {
        @NotNull
        @Email
        private String email;
        @NotBlank
        @Size(min = 8, max = 32)
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Auth {
        private Long id;
        private String email;
        private String name;
    }
}
