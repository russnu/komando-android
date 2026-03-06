package org.russel.komandoandroid.data.repository

import org.russel.komandoandroid.data.model.request.FcmTokenRequest
import org.russel.komandoandroid.data.remote.DeviceApi

class DeviceRepository(private val api: DeviceApi) {

    suspend fun registerToken(token: String): Result<String> {

        val request = FcmTokenRequest(
            token = token,
//            deviceName = Build.MODEL
        )

        val response = api.registerDevice(request)

        return if (response.isSuccessful) {
            Result.success("Device registered")
        } else {
            Result.failure(Exception("Failed to register device"))
        }
    }
}