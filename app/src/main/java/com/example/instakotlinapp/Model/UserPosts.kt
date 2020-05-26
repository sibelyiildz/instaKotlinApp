package com.example.instakotlinapp.Model


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ  ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com ║
╠════════════════════════════╣
║     25/05/2020 - 22:12     ║
╚════════════════════════════╝
 */

//Kullanıcının paylaştığı resmi recyclerda gösterirken gerekli olan bilgileri tek .atı altında topladım.
//Öbür türlü resimler ve kullanıcı adı vs farklı yerlerde tutulduğu için uğraştırırdı

//Kısacası recycler view için veri kaynağımı bir kerede oluşturdum.

class UserPosts {

    var postID: String? = null
    var postAciklama: String? = null
    var postURL: String? = null
    var userID: String? = null
    var userName: String? = null
    var userProfilFotoURL: String? = null
    var postYuklenmeTarih: Long? = null

    constructor(
        postID: String?,
        postAciklama: String?,
        postURL: String?,
        userID: String?,
        userName: String?,
        userProfilFotoURL: String?,
        postYuklenmeTarih: Long?
    ) {
        this.postID = postID
        this.postAciklama = postAciklama
        this.postURL = postURL
        this.userID = userID
        this.userName = userName
        this.userProfilFotoURL = userProfilFotoURL
        this.postYuklenmeTarih = postYuklenmeTarih
    }

    constructor()

    override fun toString(): String {
        return "UserPosts(postID=$postID, postAciklama=$postAciklama, postURL=$postURL, userID=$userID, userName=$userName, userProfilFotoURL=$userProfilFotoURL, postYuklenmeTarih=$postYuklenmeTarih)"
    }


}