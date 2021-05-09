package dev.kjokken.tuliptales.model

/**
 * Product Model.
 *
 * @param id Firebase document's unique identifier
 * @param name Product's name
 * @param price Product's price
 * @param stock Product's stock
 */
class Product(var id: String, var name: String, var price: Long, var stock: Long) {
    override fun toString(): String {
        return "Product{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", stock=" + stock +
            '}'
    }
}