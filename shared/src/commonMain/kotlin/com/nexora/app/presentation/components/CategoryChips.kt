package com.nexora.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class CategoryLookup(
    val lookupID: Int,
    val lookupText: String
)

@Composable
fun CategoryChips(
    categories: List<CategoryLookup>,
    selectedId: Int?,
    onSelect: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {

    val scrollState = rememberScrollState()

    val list = remember(categories) {
        listOf(
            CategoryLookup(
                lookupID = -1,
                lookupText = "All"
            )
        ) + categories
    }

    val canScrollLeft = scrollState.value > 2

    val canScrollRight =
        scrollState.value < scrollState.maxValue - 2

    LaunchedEffect(list) {
        scrollState.scrollTo(0)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp),
        contentAlignment = Alignment.Center
    ) {

        /*
         * ==============================
         * CATEGORY SCROLLER
         * ==============================
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(
                    horizontal = if (
                        canScrollLeft || canScrollRight
                    ) {
                        32.dp
                    } else {
                        4.dp
                    }
                ),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            list.forEach { category ->

                val isAll =
                    category.lookupID == -1

                val isSelected =
                    if (isAll) {
                        selectedId == null
                    } else {
                        selectedId == category.lookupID
                    }

                Surface(
                    onClick = {
                        onSelect(
                            if (isAll) {
                                null
                            } else {
                                category.lookupID
                            }
                        )
                    },
                    shape = CircleShape,
                    color = when {
                        isSelected && isDarkMode ->
                            Color(0x402196F3)

                        isSelected ->
                            Color(0x1A2196F3)

                        isDarkMode ->
                            Color(0x0DFFFFFF)

                        else ->
                            Color.White
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = when {
                            isSelected ->
                                Color(0x662196F3)

                            isDarkMode ->
                                Color(0x1AFFFFFF)

                            else ->
                                Color(0x1A000000)
                        }
                    )
                ) {

                    Text(
                        text = category.lookupText,
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 7.dp
                        ),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            isSelected ->
                                Color(0xFF1976D2)

                            isDarkMode ->
                                Color.White

                            else ->
                                Color(0xFF222222)
                        }
                    )
                }
            }
        }

        /*
         * ==============================
         * LEFT ARROW
         * ==============================
         */

        if (canScrollLeft) {

            IconButton(
                onClick = {
                    kotlinx.coroutines.GlobalScope.launch {
                        scrollState.animateScrollTo(
                            (scrollState.value - 220)
                                .coerceAtLeast(0)
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(30.dp)
                    .height(30.dp)
                    .background(
                        if (isDarkMode) {
                            Color(0xE6000000)
                        } else {
                            Color.White
                        },
                        CircleShape
                    )
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous categories"
                )
            }
        }

        /*
         * ==============================
         * RIGHT ARROW
         * ==============================
         */

        if (canScrollRight) {

            IconButton(
                onClick = {
                    kotlinx.coroutines.GlobalScope.launch {
                        scrollState.animateScrollTo(
                            (scrollState.value + 220)
                                .coerceAtMost(
                                    scrollState.maxValue
                                )
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(30.dp)
                    .height(30.dp)
                    .background(
                        if (isDarkMode) {
                            Color(0xE6000000)
                        } else {
                            Color.White
                        },
                        CircleShape
                    )
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "More categories"
                )
            }
        }
    }
}