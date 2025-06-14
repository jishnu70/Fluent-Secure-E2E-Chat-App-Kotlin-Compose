package com.example.fluent.domain.utility

import android.util.Base64
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher
import kotlin.io.encoding.ExperimentalEncodingApi

class EncryptionHelper {
    private val cipher: Cipher
        get() = Cipher.getInstance("RSA/ECB/PKCS1Padding")

    @OptIn(ExperimentalEncodingApi::class)
    fun encrypt(message: String, receiverPublicKey: PublicKey): String {
        val cipher = cipher
        cipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey)
        val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decrypt(encryptedMessage: String, privateKey: PrivateKey): String {
        val cipher = cipher
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedMessage, Base64.NO_WRAP))
        return String(decryptedBytes, Charsets.UTF_8)
    }
}