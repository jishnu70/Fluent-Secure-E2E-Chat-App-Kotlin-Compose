package com.example.fluent.data.mapper

import android.util.Base64
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

fun String.toRSAPublicKey(): PublicKey {
    val keyBytes = Base64.decode(this, Base64.NO_WRAP)
    val spec = X509EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    return keyFactory.generatePublic(spec)
}