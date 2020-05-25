package com.example.instakotlinapp.Share

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.Model.Posts
import com.example.instakotlinapp.Profile.YukleniyorFragment
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_share_ileri.*
import kotlinx.android.synthetic.main.fragment_share_ileri.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ShareIleriFragment : Fragment() {

    var secilenResimYolu: String? = null
    var photoURI: Uri? = null

    lateinit var mAuth: FirebaseAuth       //Oturum açma için referans nesnesi
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser
    lateinit var mStorageReference: StorageReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_share_ileri, container, false)

        //UniversalImageLoader imageView'da gösterme
        UniversalImageLoader.setImage(secilenResimYolu!!, view!!.imgSecilenResim, null, "file://")

        //resmi uri formatına çeviriyoruz, ancak bu şekilde firebase atabilirim
        //photoURI = Uri.parse("file://" + secilenResimYolu)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference
        mStorageReference = FirebaseStorage.getInstance().reference


        //paylaş butonuna tıklanıldığında resmin yüklendiği kısım
        view.tvIleriButton.setOnClickListener {

            var dialogYukleniyor = YukleniyorFragment()
            dialogYukleniyor.show(activity!!.supportFragmentManager, "YukleniyorFragmenti")
            dialogYukleniyor.isCancelable = false


            val profilePicReference = mStorageReference.child("users").child(mUser.uid)
                .child(photoURI!!.lastPathSegment!!)
            val uploadTask = profilePicReference.putFile(photoURI!!)

            uploadTask.addOnFailureListener {
                Toast.makeText(activity, "Hata oluştu ", Toast.LENGTH_SHORT).show()

            }.addOnSuccessListener { it ->
                var urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    profilePicReference.downloadUrl
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        photoURI = null
                        dialogYukleniyor.dismiss()

                        veriTabaninaBilgileriYaz(it.result.toString())

                    }
                }

                /*   //ÇAlışmadııı
                   uploadTask.addOnProgressListener { object : OnProgressListener<UploadTask.TaskSnapshot>{
                       override fun onProgress(p0: UploadTask.TaskSnapshot) {
                           var progress= 100 * p0!!.bytesTransferred / p0!!.totalByteCount
                           dialogYukleniyor.tvBilgi.text="%"+progress.toString()+ " yüklendi..."

                       }

                   } }*/
            }

/*
            //Firebase işlemlerini yaptığım kısım
        var uploadTask = mStorageReference.child("users").child(mUser.uid).child(photoURI.lastPathSegment!!)
            .putFile(photoURI!!)
            .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot>{
                override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                    if(p0!!.isSuccessful){  //Resim başarılı bir şekilde Storage'ye yüklenmişsse
                        dialogYukleniyor.dismiss()

                        veriTabaninaBilgileriYaz(p0!!.getResult().downloadUrl.toString())
                    }
                }

            })
            .addOnFailureListener(object : OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Toast.makeText(activity,"Hata oluştu "+p0!!.message, Toast.LENGTH_SHORT).show()
                }

            })*/

        }

        return view
    }

    private fun veriTabaninaBilgileriYaz(yuklenenFotoUrl: String) {

        var postID = mRef.child("posts").child(mUser.uid).push().key
        var yuklenenPost =
            Posts(mUser.uid, postID, "", etPostAciklama.text.toString(), yuklenenFotoUrl)

        mRef.child("posts").child(mUser.uid).child(postID.toString()).setValue(yuklenenPost)
        mRef.child("posts").child(mUser.uid).child(postID.toString()).child("yüklenme_tarih")
            .setValue(ServerValue.TIMESTAMP)

    }



    //////////////////////////////////////////////  EVENTBUS  /////////////////////////////////////////////

    //Gelen yayını dinleyip verileri alması için erekli metot
    @Subscribe(sticky = true)
    internal fun onSecilenResimEvent(secilenResim: EventbusDataEvents.PaylasilacakResmiGonder) {

        secilenResimYolu = secilenResim.resimYolu
    }

    //EventBus'ı eklemek için gerekli metotlar(Eventbustan yanın beklediğini söyleyen metotlar)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
//////////////////////////////////////////////  EVENTBUS  /////////////////////////////////////////////
}
