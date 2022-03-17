package com.bhagavad.hifivedemo.server

import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.bhagavad.hifivedemo.util.AppConstants

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

const val HEADER_LATITUDE_KEY = "latitude";
const val HEADER_LONGITUDE_KEY = "longitude";
const val HEADER_TIME_ZONE = "timezone";
const val HEADER_TIME_ZONE_OFFSET = "timezoneoffset";

public class ApiClientBuilder {


    companion object {
        var API_BASE_URL = AppConstants.BASE_URL;
        internal var retrofit: Retrofit? = null
        var httpClient = OkHttpClient.Builder();

        fun getClient(): Retrofit {
            httpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .addHeader("Accept", "application/json")
                    //.addHeader("language", "en")
                    .addHeader("version", AppConstants.APP_VERSION)
                    .addHeader("Device-Type", AppConstants.DEVICE_TYPE)
                    .addHeader("Fcm-Token", AppConstants.DEVICE_TOKEN)
                    .addHeader("X-User-Token", AppConstants.LOGIN_TOKEN)
                    //.addHeader("timezone", AppConstants.TIME_ZONE)
                   // .addHeader("studentid", AppConstants.STUDENT_ID)
                val request = requestBuilder.build()

                return@addInterceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)  // <-- this is the important line!

            if (retrofit == null) {
                Log.v("API_BASE_URL", "API_BASE_URL: $API_BASE_URL")
                retrofit = Retrofit.Builder().baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build()).build()
            }
            return retrofit!!

        }


        var hostnameVerifier: HostnameVerifier =
            HostnameVerifier { hostname, session -> true }

        internal val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })


        /*fun getSSLFactory(): SSLSocketFactory? {

            try {
                val context = SSLContext.getInstance("TLS")
                context.init(null, trustAllCerts, java.security.SecureRandom())
                return context.socketFactory
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return null
        }*/
    }


}