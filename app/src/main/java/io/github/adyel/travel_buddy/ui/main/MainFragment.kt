package io.github.adyel.travel_buddy.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.adyel.travel_buddy.MainActivity
import io.github.adyel.travel_buddy.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val RC_SIGN_IN = 1
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuthListener : FirebaseAuth.AuthStateListener

    private lateinit var user : FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuthListener = FirebaseAuth.AuthStateListener{
            if (it.currentUser != null){
                user = it.currentUser!!
                Toast.makeText(context, "You are logged in ", Toast.LENGTH_SHORT).show()
            }else{
                createSignInIntent()
            }
        }

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        button.setOnClickListener { firebaseAuth.signOut() }

    }


    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    private fun createSignInIntent(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

}
