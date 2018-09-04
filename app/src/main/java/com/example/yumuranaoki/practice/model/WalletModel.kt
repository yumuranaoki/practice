package com.example.yumuranaoki.practice

data class Wallet(val privateKey: String, val publicKey: String)

class NewWallet {
    val information: String = "this is private key"
    val version: Number = 1
}