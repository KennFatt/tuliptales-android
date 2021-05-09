package dev.kjokken.tuliptales

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.kjokken.tuliptales.manager.ProductManager
import dev.kjokken.tuliptales.model.Product
import kotlin.math.max

class ProductDetail : AppCompatActivity() {

    /** Model object for current view */
    private var selectedProduct: Product? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        /** Store the selected model to local state */
        selectedProduct = intent.getStringExtra("id")?.let {
            ProductManager.instance!!.getProductById(
                it
            )
        }

        /** Synchronize the view with the model */
        if (selectedProduct != null) {
            val productImage = findViewById<ImageView>(R.id.view_detail_image)
            val productName = findViewById<TextView>(R.id.view_detail_name)
            val productPrice = findViewById<TextView>(R.id.view_detail_price)
            val productStock = findViewById<TextView>(R.id.view_detail_stock)

            when (selectedProduct!!.id) {
                "bulbous" -> productImage.setImageResource(R.drawable.product_image_bulbous)
                "green_leaves" -> productImage.setImageResource(R.drawable.product_image_green_leaves)
                "roses_bunch" -> productImage.setImageResource(R.drawable.product_image_roses_bunch)
                "succulent" -> productImage.setImageResource(R.drawable.product_image_succulent)
                "tulip" -> productImage.setImageResource(R.drawable.product_image_tulip)
                "yellow_flower" -> productImage.setImageResource(R.drawable.product_image_yellow_flower)
            }

            productName.text = selectedProduct!!.name
            productPrice.text = "IDR" + selectedProduct!!.price
            productStock.text = if (selectedProduct!!.stock > 0) selectedProduct!!.stock.toString() else "Out of stock"
        }
    }

    /**
     * Navigate back to the main menu
     */
    private fun navigateToMainMenu() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }

    fun onNavigateBackToMenuButtonClicked(view: View?) {
        navigateToMainMenu()
    }

    fun onCheckoutButtonClicked(view: View?) {
        if (selectedProduct == null) {
            return
        }

        if (selectedProduct!!.stock <= 0) {
            Toast.makeText(this, "Product is out of stock!", Toast.LENGTH_LONG).show()
            return
        }

        /** Handling the checkout logic */
        val newStock = max(0, selectedProduct!!.stock - 1)
        ProductManager.instance
            ?.onCheckoutSucceed {
                selectedProduct!!.stock = newStock
                Toast.makeText(this, "Successfully purchase the product", Toast.LENGTH_SHORT).show()
                navigateToMainMenu()
            }
            ?.checkout(selectedProduct!!)
    }
}