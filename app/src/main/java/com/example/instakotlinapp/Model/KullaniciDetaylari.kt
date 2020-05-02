package com.example.instakotlinapp.Model


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ  ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com ║
╠════════════════════════════╣
║     01/05/2020 - 18:51     ║
╚════════════════════════════╝
 */
class KullaniciDetaylari {

    var takipci: String? = null
    var takipEdilen: String? = null
    var gönderi: String? = null
    var profilResmi: String? = null
    var biyografi: String? = null

    constructor()

    constructor(
        takipci: String?,
        takipEdilen: String?,
        gönderi: String?,
        profilResmi: String?,
        biyografi: String?
    ) {
        this.takipci = takipci
        this.takipEdilen = takipEdilen
        this.gönderi = gönderi
        this.profilResmi = profilResmi
        this.biyografi = biyografi
    }

    override fun toString(): String {
        return "KullaniciDetaylari(takipci=$takipci, takipEdilen=$takipEdilen, gönderi=$gönderi, profilResmi=$profilResmi, biyografi=$biyografi)"
    }


}