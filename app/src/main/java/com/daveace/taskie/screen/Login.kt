package com.daveace.taskie.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daveace.taskie.R
import com.daveace.taskie.ui.theme.dark
import com.daveace.taskie.ui.theme.light


private val vSpace = 20.dp
private val radius = 10.dp

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.login),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp
        )

        Spacer(modifier.height(vSpace))

        Spacer(modifier.height(vSpace))

        LoginForm(modifier)

        Text(text = stringResource(R.string.alternate_login))

        Spacer(modifier.height(vSpace))

        AlternateLogin(modifier)

        Spacer(modifier.height(vSpace))

        Column {
            Text(text = stringResource(R.string.don_t_have_an_account))

            Spacer(modifier.height(vSpace))

            Text(
                text = stringResource(R.string.sign_up_now),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = modifier.clickable {}
            )
        }
    }

}

@Composable
fun ColumnScope.LoginForm(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        singleLine = true,
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.email),
                contentDescription = ""
            )
        },
        shape = RoundedCornerShape(radius)
    )

    Spacer(modifier.height(vSpace))

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.lock),
                contentDescription = "lock"
            )
        },
        shape = RoundedCornerShape(radius)
    )

    Spacer(modifier.height(vSpace))

    Button(
        modifier = modifier
            .align(alignment = Alignment.Start)
            .padding(top = 8.dp, start = 36.dp, end = 36.dp, bottom = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(radius),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        onClick = {}) {

        Text(text = "Login", fontSize = 25.sp)
    }

}

@Composable
fun ColumnScope.AlternateLogin(modifier: Modifier = Modifier) {
    val borderWidth = 1.dp
    Row(
        modifier = modifier.align(alignment = Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        IconButton(
            modifier = modifier.border(
                width = borderWidth,
                color = if (isSystemInDarkTheme()) light else dark,
                shape = RoundedCornerShape(10.dp)
            ),
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.google),
                contentDescription = stringResource(
                    R.string.google_login
                )
            )
        }

        IconButton(
            modifier = modifier.border(
                width = borderWidth,
                color = if (isSystemInDarkTheme()) light else dark,
                shape = RoundedCornerShape(10.dp)
            ),
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.facebook),
                contentDescription = stringResource(
                    R.string.facebook_login
                )
            )
        }

    }
}

