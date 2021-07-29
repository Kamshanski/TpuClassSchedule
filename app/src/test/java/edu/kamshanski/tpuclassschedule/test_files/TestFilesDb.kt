package edu.kamshanski.tpuclassschedule.test_files

import androidx.annotation.IntRange
import edu.kamshanski.tpuclassschedule.model.loader.responses.SourceResponse
import edu.kamshanski.tpuclassschedule.model.loader.responses.WeekScheduleResponse
import okhttp3.ResponseBody
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun loadRaspFile(file: TestFile) : String {
    val path = Paths.get("C:\\_Coding\\Android\\TpuClassSchedule\\app\\src\\test\\java\\edu\\kamshanski\\tpuclassschedule\\test_files\\${file.filePath}")
    val bytes = Files.readAllBytes(path)
    val payload = String(bytes)
    return payload
}

fun printDirectory() {
    val currentRelativePath: Path = Paths.get("")
    val s: String = currentRelativePath.toAbsolutePath().toString()
    println(s)
}

abstract class TestFile (
    val fileName: String
) {
    protected abstract val fileDirectory: String
    protected abstract val fileExtension: String
    val filePath
        get() = fileDirectory + fileName + fileExtension
    val payload: String
        get() = loadRaspFile(this)
}

class RaspTestFile(
    val link: String,
    @IntRange(from = 2003, to = 2099) val year: Int,
    @IntRange(from = 1, to = 52) val week: Int,
    val participantFullName: String,
    val sourceResponse: SourceResponse? = null,
    val weekScheduleResponse: WeekScheduleResponse? = null,
) : TestFile("$link $year $week - $participantFullName"){
    override val fileDirectory: String
        get() = "\\rasp_files\\"
    override val fileExtension: String
        get() = ".html"
    val asResponseBody: ResponseBody
        get() = RaspFilesDb.loadAsResponseBody(this)
}