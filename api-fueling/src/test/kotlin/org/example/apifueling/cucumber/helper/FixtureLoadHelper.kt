package org.example.apifueling.cucumber.helper

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class FixtureLoadHelper(
    private val resourceLoader: ResourceLoader
) {

    companion object {
        private const val PREFIX = "classpath:/"
        private const val BASE_PATH = "features"
    }

    fun load(fixturePath: String): InputStream =
        resourceLoader.getResource("$PREFIX$BASE_PATH$fixturePath").let {
            if (!it.isFile) {
                throw RuntimeException("$fixturePath is not a file")
            }
            it.file.inputStream()
        }

    fun loadAsString(fixturePath: String): String = load(fixturePath).reader().use { it.readText() }
}