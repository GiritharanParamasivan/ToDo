package uk.ac.tees.mad.s3216191

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private val Context.gdprDataStore by preferencesDataStore(name = "gdpr_preferences")

class GDPRPreferenceManager(private val context: Context) {


    private val GDPR_CONSENT_KEY = booleanPreferencesKey("gdpr_consent")


    val gdprConsent: Flow<Boolean> = context.gdprDataStore.data
        .catch { exception ->
            // Handle exceptions
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[GDPR_CONSENT_KEY] ?: false
        }


    suspend fun setGDPRAccepted(isAccepted: Boolean) {
        context.gdprDataStore.edit { preferences ->
            preferences[GDPR_CONSENT_KEY] = isAccepted
        }
    }
}
