package com.example.fluent.presentation.authentication.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fluent.R
import com.example.fluent.presentation.authentication.AuthAction
import com.example.fluent.presentation.authentication.AuthState
import com.example.fluent.presentation.authentication.AuthViewModel
import com.example.fluent.presentation.authentication.login.components.CustomDefaultBtn
import com.example.fluent.presentation.authentication.login.components.CustomTextField
import com.example.fluent.presentation.authentication.register.components.DefaultBackArrow
import com.example.fluent.ui.theme.Peach
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreenRoot(
    viewModel: AuthViewModel = koinViewModel(),
    onLoginButtonClicked: () -> Unit,
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.onAction(AuthAction.OnRegisterNewUser(true))
    }

    SignUpScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is AuthAction.OnToggleRegisterLoginMode -> onLoginButtonClicked()
                else -> Unit
            }
            viewModel.onAction(action = action)
        },
        onBackClick = onBackClick,
        onRegisterSuccess = onRegisterSuccess
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SignUpScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val errorState = remember { mutableStateOf(state.error != null) }
    val animate = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.isAuthenticated -> {
                onRegisterSuccess()
            }

            state.error != null -> {
                errorState.value = true
            }

            else -> {
                AnimatedContent(
                    targetState = animate.value,
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { value ->
                                value
                            }
                        ).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { value ->
                                    -value
                                }
                            )
                        )
                    }
                ) {
                    if (it) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Box(modifier = Modifier.weight(0.7f)) {
                                    DefaultBackArrow {
                                        onBackClick()
                                    }
                                }
                                Box(modifier = Modifier.weight(1.0f)) {
                                    Text(
                                        text = "Sign Up",
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(50.dp))
                            Text(
                                text = "Register Account",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Complete your details or continue\nwith social media.",
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                            CustomTextField(
                                placeholder = "example@email.com",
                                trailingIcon = R.drawable.mail,
                                label = "Email",
                                errorState = errorState,
                                keyboardType = KeyboardType.Email,
                                visualTransformation = VisualTransformation.None,
                                onChanged = { newEmail ->
                                    onAction(AuthAction.OnUserNameChange(newEmail.text))
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            CustomTextField(
                                placeholder = "********",
                                trailingIcon = R.drawable.lock,
                                label = "Password",
                                keyboardType = KeyboardType.Password,
                                errorState = errorState,
                                visualTransformation = PasswordVisualTransformation(),
                                onChanged = { newPass ->
                                    onAction(AuthAction.OnPasswordChange(newPass.text))
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            CustomTextField(
                                placeholder = "********",
                                trailingIcon = R.drawable.lock,
                                label = "Confirm Password",
                                keyboardType = KeyboardType.Password,
                                errorState = errorState,
                                visualTransformation = PasswordVisualTransformation(),
                                onChanged = { newPass ->
                                    onAction(AuthAction.OnConfirmPasswordChange(newPass.text))
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            if (errorState.value) {
                                Text(
                                    text = state.error ?: "password do not match",
                                    color = Color.Red
                                )
                            }
                            CustomDefaultBtn(shapeSize = 50f, btnText = "Continue") {
                                //email pattern
                                onAction(AuthAction.OnSubmit)
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 50.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        space = 10.dp,
                                        alignment = Alignment.CenterHorizontally
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(
                                                Peach,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.google_icon),
                                            contentDescription = "Google Login Icon"
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(
                                                Peach,
                                                shape = CircleShape
                                            )
                                            .clickable {},
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.twitter),
                                            contentDescription = "Twitter Login Icon"
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(
                                                Peach,
                                                shape = CircleShape
                                            )
                                            .clickable {},
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.facebook_2),
                                            contentDescription = "Facebook Login Icon"
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 30.dp)
                                        .clickable {},
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "By continuing you confirm that you agree",
                                        color = Color.Black
                                    )
                                    Row()
                                    {
                                        Text(
                                            text = "with our ",
                                            color = Color.Black,
                                        )
                                        Text(
                                            text = "Terms & Condition",
                                            color = Color.Black,
                                            modifier = Modifier.clickable {}
                                        )
                                    }
                                    Row {
                                        Text(text = "Already have an account? ", color = Color.Black)
                                        Text(
                                            text = "Sign In",
                                            color = Color.Black,
                                            modifier = Modifier.clickable {
                                                onAction(AuthAction.OnToggleRegisterLoginMode)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Box(modifier = Modifier.weight(0.7f)) {
                                    DefaultBackArrow {
                                        animate.value = !animate.value
                                    }
                                }
                                Box(modifier = Modifier.weight(1.0f)) {
                                    Text(
                                        text = "Sign Up",
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 50.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 30.dp)
                                    .clickable {

                                    },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "By continuing you confirm that you agree",
                                    color = Color.Black
                                )
                                Row()
                                {
                                    Text(
                                        text = "with our ",
                                        color = Color.Black,
                                    )
                                    Text(
                                        text = "Terms & Condition",
                                        color = Peach,
                                        modifier = Modifier.clickable {}
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}