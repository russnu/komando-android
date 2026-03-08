package org.russel.komandoandroid.data.remote

import org.russel.komandoandroid.data.model.request.FcmTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface DeviceApi {
    @POST("/devices/register")
    suspend fun registerDevice(@Body request: FcmTokenRequest): Response<String>

    @HTTP(method = "DELETE", path = "/devices/unregister", hasBody = true)
    suspend fun unregisterDevice(@Body request: FcmTokenRequest): Response<String>
}