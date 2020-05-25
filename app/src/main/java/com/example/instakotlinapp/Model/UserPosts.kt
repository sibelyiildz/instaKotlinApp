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
}