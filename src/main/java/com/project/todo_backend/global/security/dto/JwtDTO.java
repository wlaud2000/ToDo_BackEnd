package com.project.todo_backend.global.security.dto;

import lombok.Builder;

@Builder
public record JwtDTO (

        String accessToken,

        String refreshToken
){

}
