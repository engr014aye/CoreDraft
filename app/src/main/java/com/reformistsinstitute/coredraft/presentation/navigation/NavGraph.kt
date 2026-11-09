package com.reformistsinstitute.coredraft.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.reformistsinstitute.coredraft.di.AppContainer
import com.reformistsinstitute.coredraft.presentation.editor.NoteEditorScreen
import com.reformistsinstitute.coredraft.presentation.editor.NoteEditorViewModel
import com.reformistsinstitute.coredraft.presentation.home.HomeScreen
import com.reformistsinstitute.coredraft.presentation.home.HomeViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    container: AppContainer,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeOut(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeOut(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        }
    ) {
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(
                    getNotesUseCase = container.getNotesUseCase,
                    searchNotesUseCase = container.searchNotesUseCase,
                    deleteNoteUseCase = container.deleteNoteUseCase,
                    togglePinUseCase = container.togglePinUseCase,
                    saveNoteUseCase = container.saveNoteUseCase
                )
            )
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToEditor = { noteId ->
                    navController.navigate(Screen.Editor.createRoute(noteId))
                }
            )
        }

        composable(
            route = Screen.Editor.route,
            arguments = listOf(
                navArgument(Screen.Editor.ARG_NOTE_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val noteIdString = backStackEntry.arguments?.getString(Screen.Editor.ARG_NOTE_ID)
            val noteId = noteIdString?.toLongOrNull()

            val editorViewModel: NoteEditorViewModel = viewModel(
                factory = NoteEditorViewModel.provideFactory(
                    noteId = noteId,
                    getNoteByIdUseCase = container.getNoteByIdUseCase,
                    saveNoteUseCase = container.saveNoteUseCase,
                    deleteNoteUseCase = container.deleteNoteUseCase
                )
            )

            NoteEditorScreen(
                viewModel = editorViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
