package com.misohe.misohe.utils

sealed class AuthState {
    object SUCCESS : AuthState()
    object FAILED : AuthState()
}