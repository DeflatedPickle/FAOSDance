package com.deflatedpickle.faosdance.autoupdate

import com.deflatedpickle.faosdance.util.GlobalValues
import org.apache.commons.io.FileUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object UpdateUtil {
    val releasesResult = khttp.get("https://api.github.com/repos/DeflatedPickle/FAOSDance/releases")
    val releaseObject = releasesResult.jsonArray[0] as JSONObject

    val codeSource = GlobalValues::class.java.protectionDomain.codeSource.location.path
    var installPath = ""

    var isOutdated = false

    var updateDialog: UpdateDialog? = null

    init {
        if (releaseObject["tag_name"] != GlobalValues.version) {
            // You don't have the latest version
            isOutdated = true
        }
    }

    fun downloadLatest() {
        val jarResult = khttp.get(((releaseObject["assets"] as JSONArray)[0] as JSONObject)["browser_download_url"] as String)
        // Writes the jar content to a file named after the release, in the same location as the running program
        installPath = "${codeSource}FAOSDance-${releaseObject["tag_name"]}.jar"
        FileUtils.writeByteArrayToFile(File(installPath), jarResult.content)
    }

    fun increaseProgress() {
        if (updateDialog != null) {
            updateDialog!!.progressBar.value = updateDialog!!.progressBar.value + 1
        }
    }

    fun update() {
        downloadLatest()
        increaseProgress()
        Runtime.getRuntime().exec("java -jar ${installPath.trimStart('/')}")
        increaseProgress()
        System.exit(0)
    }
}