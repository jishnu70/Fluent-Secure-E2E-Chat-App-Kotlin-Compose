package com.example.fluent.domain.utility

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey

object KeyManager {
    private const val TAG = "KeyManager"
    private const val KEY_ALIAS = "fluent_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private var isInitialized = false

    init {
        initializeKeyPair()
    }

    @Synchronized
    private fun initializeKeyPair() {
        if (isInitialized) {
            Log.d(TAG, "KeyManager already initialized, skipping")
            return
        }
        try {
            if (!keyExists()) {
                generateKeyPair()
                Log.d(TAG, "New key pair generated for alias: $KEY_ALIAS")
            } else {
                Log.d(TAG, "Key pair already exists for alias: $KEY_ALIAS")
            }
            isInitialized = true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize key pair: ${e.message}", e)
            throw RuntimeException("KeyManager initialization failed", e)
        }
    }

    private fun keyExists(): Boolean {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
            val exists = keyStore.containsAlias(KEY_ALIAS)
            Log.d(TAG, "Key exists check for alias $KEY_ALIAS: $exists")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "Error checking key existence: ${e.message}", e)
            false
        }
    }

    private fun generateKeyPair() {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE
            )
            keyPairGenerator.initialize(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setKeySize(2048)
                    .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .build()
            )
            keyPairGenerator.generateKeyPair()
            Log.d(TAG, "Key pair generated successfully for alias: $KEY_ALIAS")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate key pair: ${e.message}", e)
            throw RuntimeException("Key generation failed", e)
        }
    }

    fun getPublicKey(): String {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
            val publicKey = keyStore.getCertificate(KEY_ALIAS)?.publicKey
                ?: throw IllegalStateException("Public key not found for alias: $KEY_ALIAS")
            val encodedKey = Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
            Log.d(TAG, "Retrieved public key: $encodedKey")
            return encodedKey
        } catch (e: Exception) {
            Log.e(TAG, "Failed to retrieve public key: ${e.message}", e)
            throw RuntimeException("Public key retrieval failed", e)
        }
    }

    fun getPrivateKey(): PrivateKey? {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
            val privateKey = (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.PrivateKeyEntry)?.privateKey
            Log.d(TAG, "Private key retrieved successfully")
            return privateKey
        } catch (e: Exception) {
            Log.e(TAG, "Failed to retrieve private key: ${e.message}", e)
            return null
        }
    }
}