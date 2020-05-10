package com.example.instakotlinapp.utils

import com.example.instakotlinapp.Model.Users


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     18/04/2020 - 18:01     ║
╚════════════════════════════╝
 */
class EventbusDataEvents {

    internal class EmailGönder(var email: String)

    internal class KullaniciBilgileriniGonder(var kullanici: Users?)

    internal class PaylasilacakResmiGonder(var resimYolu: String)
}