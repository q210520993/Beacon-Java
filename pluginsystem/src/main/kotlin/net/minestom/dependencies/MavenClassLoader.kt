package net.minestom.dependencies

import java.net.URL
import java.net.URLClassLoader

class MavenClassLoader(dependencyJars: Array<URL>, parent: ClassLoader) : URLClassLoader(dependencyJars, parent) {

    public override fun addURL(url: URL) {
        super.addURL(url)
    }

    // 修改 loadClass 方法，手动向父加载器委派系统类的加载
    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*>? {
        synchronized(getClassLoadingLock(name)) {
            var c = findLoadedClass(name)

            // 首先尝试加载 Java 系统类
            if (c == null) {
                try {
                    if (name.startsWith("java.") || name.startsWith("javax.")) {
                        return parent.loadClass(name) // 委派给父加载器加载系统类
                    }

                    c = findClass(name) // 尝试从当前依赖加载类
                } catch (e: ClassNotFoundException) {
                    if (parent != null) {
                        c = parent.loadClass(name) // 尝试向上委派加载
                    }
                }
            }

            if (resolve) {
                resolveClass(c)
            }

            return c
        }
    }
}