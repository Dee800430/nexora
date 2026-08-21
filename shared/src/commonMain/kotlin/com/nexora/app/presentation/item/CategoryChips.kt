package com.nexora.app.presentation.item


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexora.app.data.model.item.Lookup
import kotlinx.coroutines.launch

@Composable
fun CategoryChips(
    categories: List<Lookup>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {

    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    /*
     * =========================================
     * SCROLL STATE
     * =========================================
     */

    val canScrollLeft by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 0
        }
    }

    val canScrollRight by remember {
        derivedStateOf {
            listState.canScrollForward
        }
    }

    /*
     * =========================================
     * MAIN CONTAINER
     * =========================================
     */

    Box(
        modifier = modifier
            .fillMaxWidth(),

        contentAlignment = Alignment.Center
    ) {

        /*
         * =====================================
         * CATEGORY LIST
         * =====================================
         */

        LazyRow(
            state = listState,

            modifier = Modifier
                .fillMaxWidth(),

            horizontalArrangement = Arrangement.spacedBy(
                8.dp
            ),

            contentPadding = PaddingValues(
                start = if (canScrollLeft) {
                    42.dp
                } else {
                    4.dp
                },

                end = if (canScrollRight) {
                    42.dp
                } else {
                    4.dp
                }
            )
        ) {

            /*
             * =================================
             * ALL CATEGORY
             * =================================
             */

            item(
                key = "all"
            ) {

                CategoryChip(
                    text = "All",

                    selected = selectedId == null,

                    onClick = {
                        onSelect(null)
                    }
                )
            }

            /*
             * =================================
             * API CATEGORIES
             * =================================
             */

            items(
                items = categories,

                key = { category ->
                    category.lookupID
                        ?: category.hashCode().toLong()
                }
            ) { category ->

                CategoryChip(
                    text = category.lookupText,

                    selected =
                        selectedId == category.lookupID,

                    onClick = {
                        onSelect(category.lookupID)
                    }
                )
            }
        }

        /*
         * =========================================
         * LEFT BUTTON
         * =========================================
         */

        if (canScrollLeft) {

            Surface(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(
                        RoundedCornerShape(50)
                    ),

                shape = RoundedCornerShape(50),

                tonalElevation = 4.dp,

                shadowElevation = 4.dp
            ) {

                IconButton(
                    onClick = {

                        coroutineScope.launch {

                            val target =
                                (
                                        listState
                                            .firstVisibleItemIndex - 3
                                        ).coerceAtLeast(0)

                            listState.animateScrollToItem(
                                index = target
                            )
                        }
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.ChevronLeft,

                        contentDescription =
                            "Previous categories"
                    )
                }
            }
        }

        /*
         * =========================================
         * RIGHT BUTTON
         * =========================================
         */

        if (canScrollRight) {

            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(
                        RoundedCornerShape(50)
                    ),

                shape = RoundedCornerShape(50),

                tonalElevation = 4.dp,

                shadowElevation = 4.dp
            ) {

                IconButton(
                    onClick = {

                        coroutineScope.launch {

                            val target =
                                listState
                                    .firstVisibleItemIndex + 3

                            listState.animateScrollToItem(
                                index = target
                            )
                        }
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.ChevronRight,

                        contentDescription =
                            "More categories"
                    )
                }
            }
        }
    }
}


/*
 * =====================================================
 * INDIVIDUAL CATEGORY CHIP
 * =====================================================
 */

@Composable
private fun CategoryChip(
    text: String?,
    selected: Boolean,
    onClick: () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    Surface(
        onClick = onClick,

        shape = RoundedCornerShape(50),

        color = if (selected) {

            colors.primary.copy(
                alpha = 0.12f
            )

        } else {

            colors.surface
        },

        border = BorderStroke(
            width = 1.dp,

            color = if (selected) {

                colors.primary.copy(
                    alpha = 0.4f
                )

            } else {

                colors.outline.copy(
                    alpha = 0.25f
                )
            }
        )
    ) {

        if (text != null) {

            Text(
                text = text,

                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 7.dp
                ),

                fontSize = 13.sp,

                fontWeight = FontWeight.Bold,

                color = if (selected) {

                    colors.primary

                } else {

                    colors.onSurface
                }
            )
        }
    }
}

