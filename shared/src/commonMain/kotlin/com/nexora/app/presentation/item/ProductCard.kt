package com.nexora.app.presentation.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexora.app.data.model.item.ItemDto
import com.nexora.app.util.formatPrice
import kotlin.math.round

@Composable
fun ProductCard(
    product: ItemDto,
    onAdd: (ItemDto) -> Unit
) {

    val price = product.saleRate ?: 0.0

    val formattedPrice = Box {
        formatPrice(price)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = MaterialTheme.shapes.medium,

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            // =================================================
            // PRODUCT IMAGE PLACEHOLDER
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Inventory2,

                    contentDescription = "Product",

                    modifier = Modifier.size(42.dp),

                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =================================================
            // PRODUCT NAME
            // =================================================

            Text(
                text = product.itemName,

                fontSize = 15.sp,

                fontWeight = FontWeight.Bold,

                maxLines = 2,

                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // =================================================
            // ITEM CODE
            // =================================================

            product.itemCode?.let { code ->

                Text(
                    text = code,

                    fontSize = 12.sp,

                    color = MaterialTheme.colorScheme.onSurfaceVariant,

                    maxLines = 1,

                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            // =================================================
            // PRICE + STOCK
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "₹$formattedPrice",

                        fontSize = 17.sp,

                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "per ${product.uom}",

                        fontSize = 11.sp,

                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

                Text(
                    text = "Stock: ${product.balanceQty}",

                    fontSize = 11.sp,

                    fontWeight = FontWeight.Medium,

                    color =
                        if (product.balanceQty > 0) {
                            Color(0xFF2E7D32)
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =================================================
            // ADD BUTTON
            // =================================================

            Button(
                onClick = {
                    onAdd(product)
                },

                modifier = Modifier.fillMaxWidth(),

                enabled = product.balanceQty > 0
            ) {

                Icon(
                    imageVector =
                        Icons.Default.AddShoppingCart,

                    contentDescription =
                        "Add to cart"
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text =
                        if (product.balanceQty > 0) {
                            "Add to Cart"
                        } else {
                            "Out of Stock"
                        }
                )
            }
        }
    }
}

