package io.fries.k8s.game.core

import io.fries.k8s.game.pdk.Guesser
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path

data class Plugin(
        private val filePath: String,
        private val mainClass: String
) {
    fun load(): Guesser {
        val classPathUrl: URL = Path.of(filePath).toUri().toURL()
        val classLoader = URLClassLoader(arrayOf(classPathUrl), this.javaClass.classLoader)
        val type = classLoader.loadClass(mainClass)
        val constructor = type.getDeclaredConstructor()
        constructor.isAccessible = true

        return constructor.newInstance() as Guesser
    }
}