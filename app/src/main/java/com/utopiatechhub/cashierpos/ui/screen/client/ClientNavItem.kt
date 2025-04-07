package com.utopiatechhub.cashierpos.ui.screen.client

sealed class ClientNavItem(val title: String, val route: String) {
    data object SenderScreen : ClientNavItem("Sender Screen", "sender_Screen")
}