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
    var kullanici_detaylari: KullaniciDetaylari? = null  //Composition

    constructor()

    constructor(
        email: String?,
        password: String?,
        user_name: String?,
        adi_soyadi: String?,
        user_id: String?,
        kullanici_detaylari: KullaniciDetaylari?
    ) {
        this.email = email
        this.password = password
        this.user_name = user_name
        this.adi_soyadi = adi_soyadi
        this.user_id = user_id
        this.kullanici_detaylari = kullanici_detaylari
    }

    override fun toString(): String {
        return "Users(email=$email, password=$password, user_name=$user_name, adi_soyadi=$adi_soyadi, user_id=$user_id, kullanici_detaylari=$kullanici_detaylari)"
    }


}