package com.nexora.app.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val orderLineItemId: Long = 0,
    val pkId: String? = null,
    val orderId: Long = 0,
    val entityItemLineId: Long = 0,
    val itemName: String? = null,
    val itemQty: Double = 0.0,
    val salePrice: Double = 0.0,
    val subTotalAmt: Double = 0.0,
    val totalDiscAmt: Double = 0.0,
    val netAmount: Double = 0.0,
    val grandAmt: Double? = null,
    val createdDate: String = "",
    val modifiedDate: String = "",
    val fileName: String? = null
)

@Serializable
data class CreateInvoiceRequest(
    val purchaseOrderId: Long,
    val sellerOrWarehouseId: Long,
    val buyerCompanyId: Long
)

@Serializable
data class Order(
    val itemName: String? = null,
    val orderId: Long = 0,
    val userName: String? = null,
    val buyerId: Long = 0,
    val sellerId: Long = 0,
    val subTotalAmt: Double = 0.0,
    val totalAmt: Double? = null,
    val totalDiscAmt: Double? = null,
    val discountAmt: Double? = null,
    val discount: Double? = null,
    val totalTaxAmt: Double? = null,
    val taxAmt: Double? = null,
    val tax: Double? = null,
    val grandAmt: Double = 0.0,
    val items: List<OrderItem> = emptyList(),
    val invStatus: String? = null,
    val createdDate: String? = null,
    val description: String? = null
)

@Serializable
data class OrderRequest(
    val orderId: Long? = null,
    val buyerId: Long,
    val sellerId: Long,
    val itemName: String,
    val uom: String? = null,
    val itemId: Long? = null,
    val itemCode: String? = null,
    val quantity: Double,
    val price: Double,
    val entityItemLineId: Long,
    val entityItemLineStockId: Long,
    val userId: Long,
    val siteId: Long,
    val orderLineItemId: Long? = null
)

@Serializable
data class PlaceOrderRequest(
    val buyerId: Long,
    val sourceOrderId: Long,
    val userName: String,
    val comments: String,
    val workflowName: String,
    val isComment: Boolean,
    val guestKey: String? = null,
   val modifiedBy: Long,
    val userId: Long
)

@Serializable
data class PurchaseOrderItem(
    val orderLineItemId: Long,
    val orderId: Long,
    val entityItemLineId: Long,
    val itemName: String,
    val quantityReq: Double,
    val quantityActual: Double,
    val itemBatchNumber: String,
    val createdDate: String
)

@Serializable
data class UpdatePurchaseItemRequest(
    val quantityReq: Double? = null,
    val quantityActual: Double? = null,
    val itemBatchNumber: String? = null
)

@Serializable
data class Company(
    val companyName: String,
    val gstNo: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    val address: String? = null,
    val logo: String? = null
)

@Serializable
data class Customer(
    val customerName: String,
    val mobile: String? = null,
    val email: String? = null,
    val address: String? = null
)

@Serializable
data class InvoiceItem(
    val srNo: Long,
    val orderLineItemId: Long,
    val orderId: Long,
    val entityItemId: Long? = null,
    val entityItemLineId: Long,
    val itemQty: Double,
    val quantityReq: Double? = null,
    val quantityActual: Double? = null,
    val salePrice: Double,
    val subTotalAmt: Double,
    val totalDiscAmt: Double,
    val netAmount: Double,
    val gst: Double? = null,
    val deliveryCharges: Double? = null,
    val grandAmt: Double?=null,
    val createdDate: String,
    val modifiedDate: String,
    val modifiedBy: Long,
    val taxPer: Double? = null,
    val breakupTax1Per: Double? = null,
    val breakupTax1Amt: Double? = null,
    val breakupTax2Per: Double? = null,
    val breakupTax2Amt: Double? = null,
    val breakupTax3Per: Double? = null,
    val breakupTax3Amt: Double? = null,
    val breakupTax4Per: Double? = null,
    val breakupTax4Amt: Double? = null,
    val hsnCode: String,
    val taxAmt: Double? = null,
    val uom: String,
    val itemName: String,
    val itemCode: String? = null
)

@Serializable
data class InvoiceResponse(
    val orderId: Long,
    val buyerId: Long? = null,
    val userName: String? = null,
    val invoiceNo: String? = null,
    val invoiceDate: String,
    val status: String,
    val company: Company,
    val customer: Customer,
    val items: List<InvoiceItem>,
    val subTotal: Double,
    val tax: Double,
    val discount: Double,
    val grandTotal: Double
)
