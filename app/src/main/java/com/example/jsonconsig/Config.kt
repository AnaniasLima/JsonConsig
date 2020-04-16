package com.example.jsonconsig

import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.FileInputStream
import java.lang.Exception
import java.nio.channels.FileChannel
import java.nio.charset.Charset

enum class ConfigType(val type: Int, val token: String) {
    SERVER          ( 0, "SERVER"),
    CREDIT_VALUE    ( 1, "CREDIT_VALUE"),
    ALARM_TIME      ( 2, "ALARM_TIME"),
    DEMO_TIME       ( 3, "DEMO_TIME"),
    TRY_TIME        ( 4, "TRY_TIME"),
    DURING_TRY_AUDIO( 5, "DURING_TRY_AUDIO"),
    ON_LOSE_AUDIO   ( 6, "ON_LOSE_AUDIO"),
    ON_WIN_VIDEO    ( 7, "ON_WIN_VIDEO"),
    ON_DEMO_VIDEO   ( 8, "ON_DEMO_VIDEO"),
    MONEY_VIDEO     ( 9, "MONEY_VIDEO"),
    CARD_VIDEO      (10, "CARD_VIDEO");

    companion object {
        fun getByToken(token: String): ConfigType? {
            for (value in values()) {
                if (value.token == token) {
                    return value
                }
            }
            return null
        }
    }
}


class Config (file: String) {

    companion object {
        var msgErro: String? = null
        var server: Server? = null
        var creditValue: Int = 0
        var alarmTime: Int
        var demoTime: Int
        var tryTime: Int
        var audioTry: Media? = null
        var audioLose: Media? = null
        var videoWin: Media? = null
        var videosDemo = ArrayList<Media>()
        var cardVideo: Media? = null
        var moneyVideo: Media? = null
    }

    init {
        var jsonObject : JSONObject? = null

        try {
            jsonObject = JSONObject(readJsonFile(file))
        } catch (e: Exception) {
            msgErro = "Arquivo invalido"
            Timber.e("Erro: %s", e.message.toString())
        }

        if ( jsonObject != null ) {
            try {
                for (value in ConfigType.values()) {
                    msgErro = value.token
                    Timber.i("Carregando configuracao id:%d  valor:%s", value.type, value.token)
                    when(value) {
                        ConfigType.SERVER            -> server      = getServer(jsonObject.getJSONObject(value.token))

                        ConfigType.CREDIT_VALUE      -> creditValue = jsonObject.getInt(value.token)
                        ConfigType.ALARM_TIME        -> alarmTime   = jsonObject.getInt(value.token)
                        ConfigType.DEMO_TIME         -> demoTime    = jsonObject.getInt(value.token)
                        ConfigType.TRY_TIME          -> tryTime     = jsonObject.getInt(value.token)

                        ConfigType.ON_DEMO_VIDEO     -> videosDemo  = getDemoVideos(jsonObject.getJSONArray(value.token))
                        ConfigType.DURING_TRY_AUDIO  -> audioTry    = getMedia(jsonObject.getJSONObject(value.token))
                        ConfigType.ON_LOSE_AUDIO     -> audioLose   = getMedia(jsonObject.getJSONObject(value.token))
                        ConfigType.ON_WIN_VIDEO      -> videoWin    = getMedia(jsonObject.getJSONObject(value.token))
                        ConfigType.MONEY_VIDEO       -> moneyVideo  = getMedia(jsonObject.getJSONObject(value.token))
                        ConfigType.CARD_VIDEO        -> cardVideo   = getMedia(jsonObject.getJSONObject(value.token))
                    }
                }
            } catch (e: Exception) {
                Timber.e("Erro: %s", e.message.toString())
            }
            msgErro = null
        }


        videosDemo.forEach {
            Timber.i("videosDemo: %s", it.filename)
        }

        Timber.i("server host: %s", server?.host)
        Timber.i("server port: %d", server?.port)

    }



    private fun readJsonFile(file : String): String {
        val mtStream = FileInputStream(file)
        var jsonString = ""

        mtStream.use { stream ->
            val fileChannel = stream.channel
            val mappedByteBuffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()
            )

            Timber.i("File size : %d", fileChannel.size())
            jsonString = Charset.defaultCharset().decode(mappedByteBuffer).toString()
            Timber.i("Lido: [%s]", jsonString)

        }
        return jsonString
    }


    private fun getServer(jsonObject: JSONObject): Server {
        return Server(
            jsonObject.getString("host"),
            jsonObject.getInt("port"),
            jsonObject.getString("username"),
            jsonObject.getString("password")
        )
    }

    private fun getMedia(jsonObject: JSONObject): Media {
        return Media(
            jsonObject.getString("filename"),
            jsonObject.getInt("volume")
        )
    }


    private fun getDemoVideos(jsonArray: JSONArray): ArrayList<Media> {
        var medias = ArrayList<Media>()
        var x = 0
        while (x < jsonArray.length()) {
            medias.add( Media(
                jsonArray.getJSONArray(x).getString(0),
                jsonArray.getJSONArray(x).getInt(1)
           ))
            x++
        }
        return medias
    }

}


