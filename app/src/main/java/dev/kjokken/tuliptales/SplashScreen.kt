package dev.kjokken.tuliptales

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import dev.kjokken.tuliptales.manager.ProductManager

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /** Initialize and fetch the first data from Database */
        ProductManager.instance!!.noop()

        /** Start MainActivity after 750ms */
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 750)
    }
}