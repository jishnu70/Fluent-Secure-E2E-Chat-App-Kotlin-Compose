package com.example.fluent.data.mapper

import com.example.fluent.data.dto.AuthUserDto
import com.example.fluent.data.dto.TokenResponseDto
import com.example.fluent.data.dto.UserRegisterResponseDto
import com.example.fluent.domain.models.AuthUser
import com.example.fluent.domain.models.RegisterUserResponse
import com.example.fluent.domain.models.TokenResponse

fun UserRegisterResponseDto.toDomainRegisterUser(): RegisterUserResponse {
    return RegisterUserResponse(
        id = id ?: throw IllegalStateException("id cannot be null"),
        username = username ?: "unknown user",
        email = email ?: "unknown email"
    )
}

fun TokenResponseDto.toDomainTokenResponse(): TokenResponse {
    return TokenResponse(
        accessToken = accessToken ?: throw IllegalStateException("access token cannot be null"),
        refreshToken = refreshToken ?: throw IllegalStateException("refresh token cannot be null"),
        tokenType = tokenType ?: ""
    )
}

fun AuthUser.toDtoUser(): AuthUserDto {
    return AuthUserDto(
        username = username,
        email = email,
        password = password
    )
}