package com.example.instakotlinapp.Home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instakotlinapp.Login.LoginActivity
import com.example.instakotlinapp.Model.Posts
import com.example.instakotlinapp.Model.UserPosts
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.Share.ShareActivity
import com.example.instakotlinapp.utils.BottomNavigationViewHelper
import com.example.instakotlinapp.utils.HomeActivityRecyclerAdapter
import com.example.instakotlinapp.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"

    lateinit var tumGonderiler: ArrayList<UserPosts>

    lateinit var mAuth: FirebaseAuth       //Oturum açma için referans nesnesi
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener   //oturum açma işlemlerini dinleyen bir yapı
    lateinit var mUser: FirebaseUser
    lateinit var mRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        setupAuthListener()    //Kullanıcının oturum açıp açmadığı ile ilgili veriler

        mAuth = FirebaseAuth.getInstance()    //ilk atamalarını burda yapıyoruz
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference
        tumGonderiler = ArrayList<UserPosts>()

        kullaniciPostlariniGetir(mUser.uid)

        initImageLoader()
        setupNavigationView()
        // setupHomeViewPager()

        imgTabShare.setOnClickListener {

            var intent = Intent(this, ShareActivity::class.java)
            startActivity(intent)
        }


    }

    private fun kullaniciPostlariniGetir(kullaniciID: String) {


        mRef.child("users").child(kullaniciID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    //Bu alanları sadece 1 defa okulamk için her defasında okumamak için buraya yazdık
                    var userID = kullaniciID
                    var kullaniciAdi = p0.getValue(Users::class.java)!!.user_name
                    var kullaniciFotoURL =
                        p0.getValue(Users::class.java)!!.kullanici_detaylari!!.profilResmi

                    mRef.child("posts").child(kullaniciID)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.hasChildren()) {   //eğer cocukları varsa(yani gönderiler)
                                    for (ds in p0.children) {

                                        var eklenecekUserPost = UserPosts()

                                        eklenecekUserPost.userID = userID
                                        eklenecekUserPost.userName = kullaniciAdi
                                        eklenecekUserPost.userProfilFotoURL = kullaniciFotoURL
                                        eklenecekUserPost.postID =
                                            ds.getValue(Posts::class.java)!!.post_id
                                        eklenecekUserPost.postURL =
                                            ds.getValue(Posts::class.java)!!.photo_url
                                        eklenecekUserPost.postAciklama =
                                            ds.getValue(Posts::class.java)!!.aciklama
                                        eklenecekUserPost.postYuklenmeTarih =
                                            ds.getValue(Posts::class.java)!!.yüklenme_tarih

                                        tumGonderiler.add(eklenecekUserPost)
                                    }

                                }


                                setupRecyclerView()


                            }

                        })


                }

            })
    }

    private fun setupRecyclerView() {
        var recyclerView = this.recyclerView
        var recyclerAdapter = HomeActivityRecyclerAdapter(this, tumGonderiler)

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
    }


    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)

        //activity'in seçili olduğunu belirtmek
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }
/*

    fun setupHomeViewPager() {

        var homePagerAdapter = HomePagerAdapter(supportFragmentManager)
        homePagerAdapter.addFragment(ShareFragment())  //id=0
        homePagerAdapter.addFragment(HomeFragment())    //id=1
        homePagerAdapter.addFragment(SearchFragment())    //id=2

        //activityMainde bulunan viewPager'a oluşturduğumuz adapter'i atadık
        homeViewPager.adapter = homePagerAdapter

        //fragmentler listeye eklediğimiz sırayla açılır ilk home fragmentinin açılmasını istiyoruz.
        homeViewPager.currentItem = 1   //viewPager'da ilk home fragmenti açılır

    }
*/

    // Uygulamada sadece 1 defa yazıp çağırmak yeterli.
    private fun initImageLoader() {
        var universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }

    //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan listener   Eğer daha önce oturum açmadıysa login activity'e gidecek
    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var user = FirebaseAuth.getInstance().currentUser

                if (user == null) {

                    var intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {

                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }
}
