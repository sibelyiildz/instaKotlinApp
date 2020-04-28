package com.example.instakotlinapp.Model

/**
 * Code with ❤
 * ╔════════════════════════════╗
 * ║  Created by Sibel YILDIZ   ║
 * ╠════════════════════════════╣
 * ║ sibelyldz2012@gmail.com    ║
 * ╠════════════════════════════╣
 * ║     24/04/2020 - 00:29     ║
 * ╚════════════════════════════╝
 */
class Users {
    var email: String? = null
    var password: String? = null
    var user_name: String? = null
    var adi_soyadi: String? = null
    var user_id: String? = null

    constructor()
    constructor(
        email: String?, password: String?, user_name: String?, adi_soyadi: String?, user_id: String?
    ) {
        this.email = email
        this.password = password
        this.user_name = user_name
        this.adi_soyadi = adi_soyadi
        this.user_id = user_id
    }
}