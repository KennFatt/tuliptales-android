package dev.kjokken.tuliptales

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.kjokken.tuliptales.manager.ProductManager
import android.util.Log

class MainActivity : AppCompatActivity() {

    private var initialStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.w("MainActivity", "onCreate")
        if (ProductManager.instance?.isInitialized() == true) {
            syncCards()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.w("MainActivity", "onStart")

        if (initialStart) {
            initialStart = false
            return
        }

        /** Synchronize with the recent most data from Database */
        ProductManager.instance!!.fetch()
        syncCards()
    }

    /**
     * Handle a click event when users click the card item.
     *  Then we will navigate them to Product's detail view.
     *
     * @param productId Hardcoded value from each click listener.
     */
    private fun onCardClicked(productId: String) {
        if (!ProductManager.instance!!.isInitialized()) {
            return
        }

        val intent = Intent(this, ProductDetail::class.java)
        val selectedProduct = ProductManager.instance!!.getProductById(productId)
        if (selectedProduct != null) {
            intent.putExtra("id", selectedProduct.id)
            startActivity(intent)
        }
    }

    fun onSucculentCardClicked(view: View?) {
        onCardClicked("succulent")
    }

    fun onGreenLeavesCardClicked(view: View?) {
        onCardClicked("green_leaves")
    }

    fun onBulbousCardClicked(view: View?) {
        onCardClicked("bulbous")
    }

    fun onRosesBunchCardClicked(view: View?) {
        onCardClicked("roses_bunch")
    }

    fun onTulipCardClicked(view: View?) {
        onCardClicked("tulip")
    }

    fun onYellowFlowerCardClicked(view: View?) {
        onCardClicked("yellow_flower")
    }

    /**
     * Synchronize all cards with latest data from the Database.
     */
    @SuppressLint("SetTextI18n")
    private fun syncCards() {
        Log.w("MainActivity", "syncCards")
        val succulent = ProductManager.instance!!.getProductById("succulent")
        val succulentName = findViewById<TextView>(R.id.succulent_card_title)
        val succulentPrice = findViewById<TextView>(R.id.succulent_card_price)
        succulentName.text = succulent!!.name
        succulentPrice.text = "IDR" + succulent.price

        val greenLeaves = ProductManager.instance!!.getProductById("green_leaves")
        val greenLeavesName = findViewById<TextView>(R.id.green_leaves_card_title)
        val greenLeavesPrice = findViewById<TextView>(R.id.green_leaves_card_price)
        greenLeavesName.text = greenLeaves!!.name
        greenLeavesPrice.text = "IDR" + greenLeaves.price

        val bulbous = ProductManager.instance!!.getProductById("bulbous")
        val bulbousName = findViewById<TextView>(R.id.bulbous_card_title)
        val bulbousPrice = findViewById<TextView>(R.id.bulbous_card_price)
        bulbousName.text = bulbous!!.name
        bulbousPrice.text = "IDR" + bulbous.price

        val roses = ProductManager.instance!!.getProductById("roses_bunch")
        val rosesName = findViewById<TextView>(R.id.roses_bunch_card_title)
        val rosesPrice = findViewById<TextView>(R.id.roses_bunch_card_price)
        rosesName.text = roses!!.name
        rosesPrice.text = "IDR" + roses.price

        val tulip = ProductManager.instance!!.getProductById("tulip")
        val tulipName = findViewById<TextView>(R.id.tulip_card_title)
        val tulipPrice = findViewById<TextView>(R.id.tulip_card_price)
        tulipName.text = tulip!!.name
        tulipPrice.text = "IDR" + tulip.price

        val yellow = ProductManager.instance!!.getProductById("yellow_flower")
        val yellowName = findViewById<TextView>(R.id.yellow_flower_card_title)
        val yellowPrice = findViewById<TextView>(R.id.yellow_flower_card_price)
        yellowName.text = yellow!!.name
        yellowPrice.text = "IDR" + yellow.price
    }
}
