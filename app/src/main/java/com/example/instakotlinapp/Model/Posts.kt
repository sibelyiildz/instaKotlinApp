package com.example.instakotlinapp.Model


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     10/05/2020 - 18:26     ║
╚════════════════════════════╝
 */
class Posts {

    var user_id: String? = null
    var post_id: String? = null
    var yüklenme_tarih: String? = null
    var aciklama: String? = null
    var photo_url: String? = null

    constructor()
    constructor(
        user_id: String?,
        post_id: String?,
        yüklenme_tarih: String?,
        aciklama: String?,
        photo_url: String?
    ) {
        this.user_id = user_id
        this.post_id = post_id
        this.yüklenme_tarih = yüklenme_tarih
        this.aciklama = aciklama
        this.photo_url = photo_url
    }

    override fun toString(): String {
        return "Posts(user_id=$user_id, post_id=$post_id, yüklenme_tarih=$yüklenme_tarih, aciklama=$aciklama, photo_url=$photo_url)"
    }


}