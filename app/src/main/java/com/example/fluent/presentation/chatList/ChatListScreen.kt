package com.example.fluent.presentation.chatList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.fluent.presentation.bottomNavigation.BottomNavigation
import com.example.fluent.presentation.chatList.components.ChatListItem
import com.example.fluent.presentation.chatList.components.ChatTopBarRoot
import com.example.fluent.presentation.profile.ProfileSideBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreenRoot(
    viewModel: ChatListViewModel = koinViewModel(),
    navController: NavController,
    onChatClick: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onNonMenuClick: (String) -> Unit,
    showMenuDrawer: Boolean,
    onShowMenuClose: (Boolean) -> Unit,
    onUserNotLoggedIn: () -> Unit,
    onLogOutButtonClicked: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val keyBoardController = LocalSoftwareKeyboardController.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.getChatList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            topBar = {
                ChatTopBarRoot(
                    scrollBehavior = scrollBehavior,
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = { viewModel.onAction(ChatListAction.OnSearchQueryChange(it)) },
                    onImeSearch = {
                        keyBoardController?.hide()
                        focusManager.clearFocus()
                        viewModel.onAction(ChatListAction.OnSearch)
                    },
                )
            },
            bottomBar = {
                BottomNavigation(
                    navController = navController,
                    showMenuDrawer = showMenuDrawer,
                    onMenuClick = onMenuClick,
                    onNonMenuClick = onNonMenuClick
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 8.dp)
                    .navigationBarsPadding()
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }

                    state.error != null -> {
                        Text(text = state.error)
                    }

                    state.isLoggedId == false -> {
                        onUserNotLoggedIn()
                    }

                    else -> {
                        ChatList(
                            state = state,
                            onAction = { action ->
                                when (action) {
                                    is ChatListAction.OnChatClick -> onChatClick(action.chatId)
                                    else -> Unit
                                }
                                viewModel.onAction(action = action)
                            }
                        )
                    }
                }
            }
        }

        if (showMenuDrawer) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(alpha = 0.1f))
                    .clip(RoundedCornerShape(15))
                    .clickable { onShowMenuClose(false) }
            )
        }

        ProfileSideBar(
            modifier = Modifier.align(Alignment.CenterEnd),
            showMenuDrawer = showMenuDrawer,
            onLogOutButtonClicked = onLogOutButtonClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatList(
    state: ChatListState,
    onAction: (ChatListAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(state.chatList) { chat ->
            ChatListItem(
                chat = chat,
                onChatClick = { onAction(ChatListAction.OnChatClick(it.partner.id)) }
            )
        }
    }
}