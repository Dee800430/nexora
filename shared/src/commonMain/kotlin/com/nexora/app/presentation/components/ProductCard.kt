package com.nexora.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.nexora.app.data.api.ItemApiService
import com.nexora.app.data.model.item.ItemDto
import com.nexora.app.util.imageUrlsFromDocuments
import com.nexora.app.util.normalizeImageUrl

private const val FALLBACK_IMAGE = ""

@Composable
fun ProductCard(
    product: ItemDto,
    onAdd: (ItemDto) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {

    val balanceQty =
        product.balanceQty ?: 0.0

    val isOutOfStock =
        balanceQty <= 0

    val stockText = when {

        isOutOfStock ->
            "Out of stock"

        balanceQty <= 5 ->
            "Only ${balanceQty.toInt()} left"

        else ->
            "In stock"
    }

    val stockColor = when {

        isOutOfStock ->
            Color(0xFFD32F2F)

        balanceQty <= 5 ->
            Color(0xFFED6C02)

        else ->
            Color(0xFF2E7D32)
    }

    val unitText = buildString {

        val qty =
            product.itemQty?.toString() ?: ""

        val uom =
            product.uom ?: ""

        append(qty)

        if (
            qty.isNotEmpty() &&
            uom.isNotEmpty()
        ) {
            append(" ")
        }

        append(uom)
    }.trim()

    var imageUrls by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var selectedImage by remember {
        mutableStateOf(
            normalizeImageUrl(product.fileName)
        )
    }

    var imageLoading by remember {
        mutableStateOf(true)
    }

    var imageFailed by remember {
        mutableStateOf(false)
    }

    /*
     * =====================================
     * LOAD PRODUCT IMAGES
     * =====================================
     */
// In ProductCard.kt - Update the LaunchedEffect

// In ProductCard.kt - Update the selectedImage assignment

    LaunchedEffect(product.entityItemLineId) {
        imageLoading = true
        imageFailed = false
        try {
            val imageLineId = product.entityItemLineId ?: product.itemLineId

            if (imageLineId == null) {
                // Use fileName but clean it
                val fallback = product.fileName?.let {
                    if (it.startsWith("item-lines/")) {
                        "https://res.cloudinary.com/dic3vj6tg/image/upload/v1786057593/$it"
                    } else {
                        normalizeImageUrl(it)
                    }
                } ?: ""
                selectedImage = fallback
                imageFailed = fallback.isEmpty()
                return@LaunchedEffect
            }

            val docs = ItemApiService.getDocumentsByItemLine(imageLineId)
            val urls = imageUrlsFromDocuments(docs)

            imageUrls = urls

            // 👇 USE THE API URL DIRECTLY - DON'T NORMALIZE IT
            selectedImage = urls.firstOrNull()
                ?: product.fileName?.let {
                    if (it.startsWith("item-lines/")) {
                        "https://res.cloudinary.com/dic3vj6tg/image/upload/v1786057593/$it"
                    } else {
                        normalizeImageUrl(it)
                    }
                } ?: ""

            imageFailed = selectedImage.isEmpty()

        } catch (_: Exception) {
            val fallback = product.fileName?.let {
                if (it.startsWith("item-lines/")) {
                    "https://res.cloudinary.com/dic3vj6tg/image/upload/v1786057593/$it"
                } else {
                    normalizeImageUrl(it)
                }
            } ?: ""
            selectedImage = fallback
            imageFailed = fallback.isEmpty()
            imageUrls = emptyList()
        } finally {
            imageLoading = false
        }
    }
    /*
     * =====================================
     * CARD
     * =====================================
     */

    Card(
        modifier = modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (isDarkMode) {
                    Color(0xFF151515)
                } else {
                    Color.White
                }
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column {

            /*
             * ==============================
             * IMAGE
             * ==============================
             */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        if (isDarkMode) {
                            Color(0xFF101010)
                        } else {
                            Color(0xFFF8FAFC)
                        }
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                if (imageLoading) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(28.dp),
                        strokeWidth = 2.dp
                    )
                }

                if (selectedImage.isEmpty() || imageFailed) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Product image",
                        modifier = Modifier.size(46.dp),
                        tint = Color(0xFF9AA4B2)
                    )
                } else {
                    AsyncImage(
                        model = selectedImage,
                        contentDescription = product.itemName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(7.dp),
                        contentScale = ContentScale.Fit,
                        onLoading = {
                            println("🔍 COIL: Loading image - $selectedImage")
                        },
                        onSuccess = {
                            println("🔍 COIL: SUCCESS - Image loaded!")
                            imageFailed = false
                            imageLoading = false
                        },
                        onError = { error ->
                            println("🔍 COIL: ERROR - ${error.result.throwable.message}")
                            println("🔍 COIL: Failed URL - $selectedImage")
                            imageFailed = true
                            imageLoading = false
                        }
                    )
                }

                /*
                 * ==============================
                 * STOCK BADGES
                 * ==============================
                 */

                Row(
                    modifier = Modifier
                        .align(
                            Alignment.TopStart
                        )
                        .padding(7.dp),

                    horizontalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {

                    Surface(
                        shape = RoundedCornerShape(
                            50
                        ),
                        color = stockColor
                    ) {

                        Text(
                            text = stockText,
                            modifier =
                                Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    if (
                        !isOutOfStock &&
                        balanceQty <= 5
                    ) {

                        Surface(
                            shape =
                                RoundedCornerShape(50),
                            color =
                                Color(0xFFE0E0E0)
                        ) {

                            Text(
                                text = "Fast",
                                modifier =
                                    Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                fontSize = 10.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }

            /*
             * ==============================
             * IMAGE THUMBNAILS
             * ==============================
             */

            if (imageUrls.size > 1) {

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),

                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    itemsIndexed(
                        imageUrls
                    ) { index, url ->

                        AsyncImage(
                            model = url,

                            contentDescription =
                                "${product.itemName}-${index + 1}",

                            modifier = Modifier
                                .size(34.dp)
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedImage = url
                                },

                            contentScale =
                                ContentScale.Crop
                        )
                    }
                }
            }

            /*
             * ==============================
             * PRODUCT BODY
             * ==============================
             */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),

                verticalArrangement =
                    Arrangement.spacedBy(5.dp)
            ) {

                Text(
                    text =
                        product.itemName
                            ?: "Product",

                    fontSize = 14.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    maxLines = 2,

                    overflow =
                        TextOverflow.Ellipsis
                )

                /*
                 * PRICE
                 */

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "₹${product.saleRate ?: 0}",

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Black
                    )

                    if (
                        unitText.isNotEmpty()
                    ) {

                        Text(
                            text = unitText,
                            fontSize = 10.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                Color.Gray
                        )
                    }
                }

                /*
                 * STOCK
                 */

                Text(
                    text =
                        "Available: ${balanceQty.toInt()}",

                    fontSize = 10.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        if (isOutOfStock) {
                            Color(0xFFD32F2F)
                        } else {
                            Color.Gray
                        }
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )

                /*
                 * ADD TO CART
                 */

                Button(
                    onClick = {
                        onAdd(product)
                    },

                    enabled =
                        !isOutOfStock,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(8.dp)
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.AddShoppingCart,

                        contentDescription =
                            "Add to cart",

                        modifier =
                            Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.size(5.dp)
                    )

                    Text(
                        text =
                            if (isOutOfStock) {
                                "Out of Stock"
                            } else {
                                "Add to Cart"
                            },

                        fontSize = 12.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}
