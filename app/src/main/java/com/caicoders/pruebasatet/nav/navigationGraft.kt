package com.caicoders.pruebasatet.nav

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caicoders.pruebasatet.utils.CheckOrientation
import com.caicoders.pruebasatet.utils.Tracker.TrackerModel
import com.caicoders.pruebasatet.view.ballon.BallonModel
import com.caicoders.pruebasatet.view.calibration.CalibrationModel
import com.caicoders.pruebasatet.view.calibration.StartGame
import com.caicoders.pruebasatet.view.home.HomeScreen
import com.caicoders.pruebasatet.view.login.LoginScreen
import com.caicoders.pruebasatet.view.reports.ReportPreview
import com.caicoders.pruebasatet.view.stages.BallonScreen
import com.caicoders.pruebasatet.view.training.TrainingScreen

@Composable
fun NavigationGraft(context: Activity, trackerModel: TrackerModel) {
    val navController = rememberNavController()
    val ballonModel = BallonModel()
    val calibrationModel = CalibrationModel()

    NavHost(navController = navController, startDestination = Graft.Login.route) {
        composable(Graft.Login.route) {
            CheckOrientation()
            LoginScreen(navController, context)
        }
        composable(Graft.Home.route) {
            CheckOrientation()
            HomeScreen(navController)
        }

        composable(Graft.Training.route) {
            CheckOrientation()
            TrainingScreen(navController)
        }
        composable(Graft.Calibration.route) {
            CheckOrientation()
            StartGame(trackerModel = trackerModel, ballonModel, calibrationModel)
        }

        composable(Graft.Report.route){
            CheckOrientation()
            ReportPreview()
        }
    }
}