package com.daveace.taskie.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import com.daveace.taskie.R
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val space = 20.dp
private val radius = 10.dp

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier.height(space))
        Spacer(modifier.height(space))
        SignUpForm(modifier)
        Spacer(modifier.height(space))
        Row {
            Text(
                text = stringResource(R.string.already_have_an_account),
            )
            Spacer(modifier.width(space))
            Text(
                text = stringResource(R.string.login),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = modifier.clickable {}
            )
        }
    }
}

@Composable
fun ColumnScope.SignUpForm(modifier: Modifier = Modifier) {

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    OutlinedTextField(
        value = fullName,
        onValueChange = { fullName = it },
        label = { Text("Full Name") },
        singleLine = true,
        shape = RoundedCornerShape(radius),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.account),
                contentDescription = "account icon",
                modifier = modifier
            )
        }
    )

    Spacer(modifier.height(space))

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        singleLine = true,
        shape = RoundedCornerShape(radius),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.email),
                contentDescription = "email icon",
                modifier = modifier
            )
        }
    )

    Spacer(modifier.height(space))

    OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Phone") },
        singleLine = true,
        shape = RoundedCornerShape(radius),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.phone),
                contentDescription = "email icon",
                modifier = modifier
            )
        }
    )

    Spacer(modifier.height(space))

    Button(
        modifier = modifier
            .align(alignment = Alignment.Start)
            .padding(top = 8.dp, start = 36.dp, end = 36.dp, bottom = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(radius),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        onClick = {}) {

        Text(text = stringResource(R.string.sign_up), fontSize = 25.sp)
    }
}