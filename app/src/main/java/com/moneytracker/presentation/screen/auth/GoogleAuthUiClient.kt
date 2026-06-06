package com.moneytracker.presentation.screen.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
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

    suspend fun signIn(activityContext: Context): Result<String?> {
        val googleIdOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = context.getString(R.string.default_web_client_id)
        )
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
            Result.success(handleSignInResult(result))
        } catch (e: androidx.credentials.exceptions.GetCredentialCancellationException) {
            Log.d("GoogleAuthUiClient", "Sign-in cancelled by user")
            Result.success(null) // Treat cancellation as success with null token
        } catch (e: androidx.credentials.exceptions.NoCredentialException) {
            Log.e("GoogleAuthUiClient", "No credentials available", e)
            Result.failure(Exception("No Google accounts found. Please add an account in Settings."))
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Sign-in failed with exception: ${e.javaClass.simpleName}", e)
            Result.failure(e)
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
