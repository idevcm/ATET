package com.caicoders.pruebasatet.nav

sealed class Graft(val route: String) {
    object Login: Graft("Login")
    object Home: Graft("Home")
    object Training: Graft("Training")
    object Calibration: Graft("Calibration")
    object Report: Graft("Report")
}