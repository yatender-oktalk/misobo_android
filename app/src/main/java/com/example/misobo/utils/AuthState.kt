package com.example.misobo.utils

sealed class AuthState {
    object SUCCESS : AuthState()
    object FAILED : AuthState()
}