package com.moneytracker.presentation.screen.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.moneytracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthUiClient @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(activityContext: Context): String? {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts = false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                context = activityContext,
                request = request
            )
            Log.d("GoogleAuthUiClient", "Credential result: ${result.credential.type}")
            handleSignInResult(result)
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Sign-in failed", e)
            null
        }
    }

    private fun handleSignInResult(result: GetCredentialResponse): String? {
        val credential = result.credential
        Log.d("GoogleAuthUiClient", "Handling result, type: ${credential.type}")

        return when {
            credential is GoogleIdTokenCredential -> {
                Log.d("GoogleAuthUiClient", "Got GoogleIdTokenCredential")
                credential.idToken
            }
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    Log.d("GoogleAuthUiClient", "Parsed GoogleIdTokenCredential from data")
                    googleIdTokenCredential.idToken
                } catch (e: Exception) {
                    Log.e("GoogleAuthUiClient", "Error parsing Google ID token", e)
                    null
                }
            }
            else -> {
                Log.e("GoogleAuthUiClient", "Unexpected credential type: ${credential.type}")
                null
            }
        }
    }
}
