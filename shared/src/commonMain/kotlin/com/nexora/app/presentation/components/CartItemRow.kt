package com.nexora.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.nexora.app.data.api.ItemApiService
import com.nexora.app.data.model.order.OrderItem
import com.nexora.app.util.imageUrlsFromDocuments
import com.nexora.app.util.normalizeImageUrl
import kotlinx.coroutines.launch

private const val FALLBACK_IMAGE = ""

@Composable
fun CartItemRow(
    item: OrderItem?,
    isMobile: Boolean = false,
    onTempQtyChange: (Int, Int) -> Unit,
    onApplyQty: suspend (Int, Int) -> Unit,
    onRemove: suspend (Int) -> Unit,
    isDarkMode: Boolean = false,
    disabled: Boolean = false,
    modifier: Modifier = Modifier
) {

    if (item == null) {
        return
    }

    val scope =
        rememberCoroutineScope()

    var localQty by remember(
        item.orderLineItemId
    ) {
        mutableStateOf(
            item.itemQty ?: 1
        )
    }

    var isSyncing by remember {
        mutableStateOf(false)
    }

    var imageUrl by remember {
        mutableStateOf(
            FALLBACK_IMAGE
        )
    }

    var imageUrls by remember {
        mutableStateOf<List<String>>(
            emptyList()
        )
    }

    var imageFailed by remember {
        mutableStateOf(false)
    }

    /*
     * ==================================
     * SYNC QUANTITY
     * ==================================
     */

    LaunchedEffect(item.itemQty) {

        localQty =
            item.itemQty ?: 1
    }

    /*
     * ==================================
     * LOAD IMAGE
     * ==================================
     */

    LaunchedEffect(
        item.entityItemLineId
    ) {

        val id =
            item.entityItemLineId
                ?: return@LaunchedEffect

        try {

            val docs =
                ItemApiService.getDocumentsByItemLine(id)

            val urls = imageUrlsFromDocuments(docs)

            imageUrls = urls

            imageUrl =
                urls.firstOrNull()
                    ?: normalizeImageUrl(item.fileName)
                        .ifEmpty { FALLBACK_IMAGE }
            imageFailed = imageUrl.isEmpty()

        } catch (
            _: Exception
        ) {

            imageUrls = emptyList()

            imageUrl =
                normalizeImageUrl(item.fileName)
                    .ifEmpty { FALLBACK_IMAGE }
            imageFailed = imageUrl.isEmpty()
        }
    }

    val hasChanged =
        localQty != (
                item.itemQty ?: 1
                )

    val lineTotal =
        item.netAmount ?: 0.0

    val unitPrice =
        item.salePrice ?: 0.0

    val discount =
        item.totalDiscAmt ?: 0.0

    /*
     * ==================================
     * MAIN CARD
     * ==================================
     */

    Surface(
        modifier = modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(10.dp),

        color =
            if (isDarkMode) {
                Color(0xFF191919)
            } else {
                Color.White
            },

        border =
            androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDarkMode) {
                    Color(0x1AFFFFFF)
                } else {
                    Color(0x1A000000)
                }
            )
    ) {

        Column(
            modifier =
                Modifier.padding(10.dp)
        ) {

            /*
             * ==================================
             * PRODUCT INFORMATION
             * ==================================
             */

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.Top
            ) {

                if (imageUrl.isEmpty() || imageFailed) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (isDarkMode) {
                                    Color(0x1AFFFFFF)
                                } else {
                                    Color(0x1A000000)
                                },
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "Item image",
                            tint = Color.Gray
                        )
                    }
                } else {
                    AsyncImage(
                        model = imageUrl,

                        contentDescription =
                            item.itemName,

                        modifier = Modifier
                            .size(56.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (isDarkMode) {
                                    Color(0x1AFFFFFF)
                                } else {
                                    Color(0x1A000000)
                                },
                                RoundedCornerShape(8.dp)
                            ),

                        contentScale =
                            ContentScale.Crop,

                        onError = {
                            imageFailed = true
                        },

                        onSuccess = {
                            imageFailed = false
                        }
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            item.itemName
                                ?: "Item",

                        fontSize = 14.sp,

                        fontWeight =
                            FontWeight.ExtraBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "Rs $unitPrice / unit",

                        fontSize = 11.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color.Gray
                    )

                    /*
                     * DISCOUNT
                     */

                    if (discount > 0) {

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Surface(
                            shape =
                                RoundedCornerShape(50.dp),

                            color =
                                Color(0xFFE53935)
                        ) {

                            Text(
                                text =
                                    "Save Rs $discount",

                                modifier =
                                    Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 3.dp
                                    ),

                                color =
                                    Color.White,

                                fontSize = 10.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }

            /*
             * ==================================
             * IMAGE THUMBNAILS
             * ==================================
             */

            if (imageUrls.size > 1) {

                LazyRow(
                    modifier =
                        Modifier.padding(
                            top = 8.dp
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
                                "${item.itemName}-${index + 1}",

                            modifier = Modifier
                                .size(28.dp)
                                .clip(
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    imageUrl = url
                                },

                            contentScale =
                                ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            /*
             * ==================================
             * QUANTITY + TOTAL
             * ==================================
             */

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * QUANTITY CONTROL
                 */

                Row(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isDarkMode) {
                                Color(0x24FFFFFF)
                            } else {
                                Color(0x24000000)
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .background(
                            if (isDarkMode) {
                                Color(0x0DFFFFFF)
                            } else {
                                Color(0x05000000)
                            }
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    IconButton(
                        enabled =
                            !disabled,

                        onClick = {

                            val qty =
                                (localQty.toInt() - 1L)
                                    .coerceAtLeast(1L)

                            localQty = qty

                            onTempQtyChange(
                                item.orderLineItemId.toInt(),
                                qty.toInt()
                            )
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Remove,

                            contentDescription =
                                "Decrease quantity"
                        )
                    }

                    Text(
                        text =
                            localQty.toString(),

                        modifier =
                            Modifier.width(40.dp),

                        textAlign =
                            androidx.compose.ui.text.style.TextAlign.Center,

                        fontWeight =
                            FontWeight.ExtraBold
                    )

                    IconButton(
                        enabled =
                            !disabled,

                        onClick = {

                            val qty =
                                localQty.toInt() + 1

                            localQty = qty

                            onTempQtyChange(
                                item.orderLineItemId.toInt(),
                                qty
                            )
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Add,

                            contentDescription =
                                "Increase quantity"
                        )
                    }
                }

                /*
                 * APPLY BUTTON
                 */

                if (
                    hasChanged &&
                    !disabled
                ) {

                    Button(
                        enabled =
                            !isSyncing,

                        onClick = {

                            scope.launch {

                                isSyncing = true

                                try {

                                    onApplyQty(
                                        item.orderLineItemId.toInt(),
                                        localQty.toInt()
                                    )

                                } finally {

                                    isSyncing = false
                                }
                            }
                        },

                        modifier =
                            Modifier.size(
                                width = 48.dp,
                                height = 38.dp
                            ),

                        contentPadding =
                            androidx.compose.foundation.layout
                                .PaddingValues(0.dp),

                        shape =
                            RoundedCornerShape(8.dp)
                    ) {

                        if (isSyncing) {

                            Text(
                                "...",
                                fontWeight =
                                    FontWeight.Bold
                            )

                        } else {

                            Icon(
                                imageVector =
                                    Icons.Default.Check,

                                contentDescription =
                                    "Update quantity"
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            /*
             * ==================================
             * TOTAL + REMOVE
             * ==================================
             */

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text =
                            "Rs $lineTotal",

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Black
                    )

                    Text(
                        text = "Total",

                        fontSize = 10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color.Gray
                    )
                }

                IconButton(
                    enabled =
                        !disabled,

                    onClick = {

                        scope.launch {

                            onRemove(
                                item.orderLineItemId.toInt()
                            )
                        }
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.DeleteOutline,

                        contentDescription =
                            "Remove",

                        tint =
                            Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}
