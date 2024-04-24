package com.ycblog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Login {

    @NotBlank(message = "input your email.")
    private String email;
    @NotBlank(message = "input your password.")
    private String password;
}
