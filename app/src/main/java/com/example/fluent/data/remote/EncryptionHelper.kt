package com.example.fluent.data.remote

import android.util.Base64
import android.util.Log
import java.security.InvalidKeyException
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
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
    fun decrypt(encryptedMessage: String, privateKey: PrivateKey): String? {
        try {
            val cipher = cipher
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val decodedBytes = Base64.decode(encryptedMessage, Base64.NO_WRAP)
            Log.d("EncryptionHelper", "Base64 decoded length: ${decodedBytes.size}")
            Log.d("EncryptionHelper", "🔓 Decrypting with: ${Base64.decode(encryptedMessage, Base64.NO_WRAP).size} bytes")
            val decryptedBytes = cipher.doFinal(decodedBytes)
            return String(decryptedBytes, Charsets.UTF_8)
        } catch (e: BadPaddingException) {
            Log.e("EncryptionHelper", "Decryption failed: BadPaddingException - ${e.message}", e)
            return null
        } catch (e: IllegalBlockSizeException) {
            Log.e("EncryptionHelper", "Decryption failed: IllegalBlockSizeException - ${e.message}", e)
            return null
        } catch (e: InvalidKeyException) {
            Log.e("EncryptionHelper", "Decryption failed: InvalidKeyException - ${e.message}", e)
            return null
        } catch (e: IllegalArgumentException) {
            Log.e("EncryptionHelper", "Decryption failed: Invalid Base64 format - ${e.message}", e)
            return null
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Decryption failed: ${e.javaClass.simpleName} - ${e.message}", e)
            return null
        }
    }
}