package com.example.fluent.data.remote

import androidx.datastore.core.Serializer
import com.example.fluent.data.dto.TokenResponseDto
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<TokenResponseDto> {
    override val defaultValue: TokenResponseDto
        get() = TokenResponseDto()

    override suspend fun readFrom(input: InputStream): TokenResponseDto {
        return Json.decodeFromString(
            TokenResponseDto.serializer(),
            input.readBytes().decodeToString()
        )
    }

    override suspend fun writeTo(
        t: TokenResponseDto,
        output: OutputStream
    ) {
        output.write(Json.encodeToString(TokenResponseDto.serializer(), t).encodeToByteArray())
    }
}