package com.utopiatechhub.cashierpos.ui.connection

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.utopiatechhub.cashierpos.data.Order
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.viewmodel.OrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel
import fi.iki.elonen.NanoHTTPD
import org.json.JSONArray
import java.net.InetAddress

class OrderHttpServer(
    private val orderViewModel: OrderViewModel,
    private val otherOrderViewModel: OtherOrderViewModel,
    private val onOrderReceived: (String) -> Unit
) : NanoHTTPD(8080) {

    override fun serve(session: IHTTPSession?): Response {
        if (session?.method == Method.POST) {
            val map = HashMap<String, String>()
            session.parseBody(map)
            val orderData = map["postData"]
            Log.d("HTTP Server", "Received Order: $orderData")

            orderData?.let {
                try {
                    val jsonArray = JSONArray(it)
                    for (i in 0 until jsonArray.length()) {
                        val jsonOrder = jsonArray.getJSONObject(i)

                        // Extract common fields
                        val waiterName = jsonOrder.getString("waiterName")
                        val itemType = jsonOrder.getString("itemType")
                        val itemName = jsonOrder.getString("itemName")
                        val quantity = jsonOrder.getInt("quantity")
                        val totalPrice = jsonOrder.getDouble("totalPrice")
                        val orderDate = jsonOrder.getString("orderDate")

                        // Handle different item types
                        when (itemType) {
                            "food" -> {
                                val order = Order(
                                    waiterName = waiterName,
                                    foodName = itemName,
                                    quantity = quantity,
                                    totalPrice = totalPrice,
                                    orderDate = orderDate
                                )
                                orderViewModel.insertOrder(order)
                            }
                            "cake", "hotDrink", "softDrink", "bread", "juice" -> {
                                val otherOrder = OtherOrder(
                                    waiterName = waiterName,
                                    itemName = itemName,
                                    itemType = itemType,
                                    quantity = quantity,
                                    totalPrice = totalPrice,
                                    orderDate = orderDate
                                )
                                otherOrderViewModel.insertOtherOrder(otherOrder)
                            }
                            else -> {
                                Log.e("HTTP Server", "Unknown item type: $itemType")
                            }
                        }
                    }

                    onOrderReceived("✅ Order Saved to Database!") // Update UI
                    return newFixedLengthResponse(Response.Status.OK, "text/plain", "Order received and saved!")
                } catch (e: Exception) {
                    Log.e("HTTP Server", "Error parsing order: ${e.message}")
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Failed to process order")
                }
            }
        }
        return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", "Invalid request")
    }
}

@Composable
fun ReceiverScreen() {
    val context = LocalContext.current
    var ipAddress by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        ipAddress = getLocalIpAddress(context) ?: "Unknown"

        val intent = Intent(context, OrderReceiverService::class.java)
        context.startForegroundService(intent)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Receiver is running in the background...", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Text("Receiver is running...", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Text("IP Address: $ipAddress")
    }
}

fun getLocalIpAddress(context: Context): String? {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val dhcpInfo = wifiManager.dhcpInfo
    val ipAddress = dhcpInfo.ipAddress
    val formattedIp = InetAddress.getByAddress(
        byteArrayOf(
            (ipAddress and 0xFF).toByte(),
            ((ipAddress shr 8) and 0xFF).toByte(),
            ((ipAddress shr 16) and 0xFF).toByte(),
            ((ipAddress shr 24) and 0xFF).toByte()
        )
    ).hostAddress
    Log.d("LocalIP", "Device IP: $formattedIp")
    return formattedIp
}