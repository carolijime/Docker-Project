package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber; // will be acting as a username in case of spring security

    @NotBlank
    private String email;

    @NotBlank
    private String password;


    private String country;
    private String dob;

    @NotBlank
    private String identifierValue;

    @NotNull
    private UserIdentifier userIdentifier;

    public User to(){
        return User.builder()
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .password(this.password)
                .country(this.country)
                .dob(this.dob)
                .identifierValue(this.identifierValue)
                .userIdentifier(this.userIdentifier)
                .build();
    }

}
