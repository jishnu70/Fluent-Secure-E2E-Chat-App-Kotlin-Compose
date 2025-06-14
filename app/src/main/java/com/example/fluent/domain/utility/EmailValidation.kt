package com.example.fluent.domain.utility

import android.util.Patterns

@JvmInline
value class Email(val value: String) {
    init {
        require(Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            "Invalid email format: $value"
        }
    }
}