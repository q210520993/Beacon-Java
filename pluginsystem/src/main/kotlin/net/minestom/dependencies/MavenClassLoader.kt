package net.minestom.dependencies

import java.net.URL
import java.net.URLClassLoader

class MavenClassLoader(dependencyJars: Array<URL>, parent: ClassLoader) : URLClassLoader(dependencyJars, parent) {

    public override fun addURL(url: URL) {
        super.addURL(url)
    }

    // 禁止向上委派，实现与主应用的隔离
    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*>? {
        synchronized(getClassLoadingLock(name)) {
            var c = findLoadedClass(name)
            if (c == null) {
                try {
                    c = findClass(name) // 仅从当前依赖加载
                } catch (e: ClassNotFoundException) {
                    throw e
                }
            }
            return c
        }
    }
}