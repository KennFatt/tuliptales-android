package dev.kjokken.tuliptales.manager

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dev.kjokken.tuliptales.model.Product
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

/**
 * The main class that responsible for our business logic.
 * - Data Fetching and Synchronizing
 * - Checkout Logic
 *
 * This class has been migrated into kotlin from Singleton pattern in Java.
 */
class ProductManager private constructor() {

    private val db = FirebaseFirestore.getInstance()
    private var isInitialized = false

    /** Data that match our "products/" collection */
    private val products = HashMap<String, Product>()

    /** Callback to be fired after checkout succeed */
    private var onCheckoutSucceed: Consumer<Void?>? = null

    fun noop() { /* no-op */
    }

    /**
     * Fetching the data from "products/" collection.
     *  It will store most recent data into our `products` property.
     *
     * @param init Whether it is the first fetch or not
     * @see products
     */
    fun fetch(init: Boolean = false) {
        db.collection("products")
            .get()
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }

                for (doc in it.result!!) {
                    storeProductInformation(doc.id, doc.data, init)
                }
            }
    }

    /**
     * Handle the checkout logic.
     * It tries to:
     *  - Create new data for new transaction document
     *  - Update the existing stock of the product
     *
     * These two processes are run in batch and it will call the `onCheckoutSucceed` callback
     *  after the process completed.
     *
     * @param product Product model
     * @see onCheckoutSucceed
     */
    fun checkout(product: Product) {
        /** Path: 'products/${product.id}/transactions/${transaction.id}/' */
        val transactionData = HashMap<String, Any>()
        transactionData["timestamp"] = Timestamp(Date())
        transactionData["income"] = product.price

        val productDocRef = db.collection("products").document(product.id)
        val transactionsColRef = productDocRef.collection("transactions")
        db.runBatch {
            /** First, update its stock */
            productDocRef.update("stock", product.stock - 1)

            /** Add new document into the `.../transactions/` collection */
            transactionsColRef.add(transactionData)
        }.addOnSuccessListener {
            if (onCheckoutSucceed != null) {
                onCheckoutSucceed!!.accept(null)
                onCheckoutSucceed = null
            }
        }
    }

    fun isInitialized(): Boolean {
        return isInitialized
    }

    /**
     * Attach new callback for each checkout process.
     *
     * @param onCheckoutSucceedCallback Callback after checkout succeed
     * @return ProductManager
     */
    fun onCheckoutSucceed(onCheckoutSucceedCallback: Consumer<Void?>?): ProductManager? {
        this.onCheckoutSucceed = onCheckoutSucceedCallback
        return instance
    }

    /**
     * Get Product model by its id.
     *
     * @param id Product's document ID
     * @return Product|null
     */
    fun getProductById(id: String): Product? {
        return products[id]
    }

    private fun storeProductInformation(docId: String, docData: Map<String, Any>, update: Boolean = false) {
        if (update && docId in products) {
            val p = products[docId]!!

            p.name = docData["name"] as String
            p.price = docData["price"] as Long
            p.stock = docData["stock"] as Long

            return
        }

        products[docId] = Product(
            docId,
            docData["name"].toString(),
            docData["price"] as Long,
            docData["stock"] as Long
        )
    }

    companion object {
        var instance: ProductManager? = null
            get() {
                if (field == null) {
                    field = ProductManager()
                }
                return field
            }
            private set
    }

    init {
        fetch(true)
    }
}