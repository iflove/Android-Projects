package com.androidz.base_modules.baseAndroid.utils.commons.io

import java.io.File
import java.nio.file.Files
import java.util.*

/**
 * 作用描述:
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/5/27
 * 修改日期 2022/5/27
 * 版权 pub
 */
object FileUtils {
    /**
     * Returns the size of the specified file or directory. If the provided
     * [File] is a regular file, then the file's length is returned.
     * If the argument is a directory, then the size of the directory is
     * calculated recursively. If a directory or subdirectory is security
     * restricted, its size will not be included.
     *
     *
     * Note that overflow is not detected, and the return value may be negative if
     * overflow occurs. See [.sizeOfAsBigInteger] for an alternative
     * method that does not overflow.
     *
     *
     * @param file the regular file or directory to return the size
     * of (must not be `null`).
     *
     * @return the length of the file, or recursive size of the directory,
     * provided (in bytes).
     *
     * @throws NullPointerException     if the file is `null`.
     * @throws IllegalArgumentException if the file does not exist.
     *
     * @since 2.0
     */
    @JvmStatic
    fun sizeOf(file: File): Long {
        return if (file.isDirectory) sizeOfDirectory0(file) else file.length()
    }

    /**
     * Counts the size of a directory recursively (sum of the length of all files).
     *
     *
     * Note that overflow is not detected, and the return value may be negative if
     * overflow occurs. See [.sizeOfDirectoryAsBigInteger] for an alternative
     * method that does not overflow.
     *
     *
     * @param directory directory to inspect, must not be `null`.
     * @return size of directory in bytes, 0 if directory is security restricted, a negative number when the real total
     * is greater than [Long.MAX_VALUE].
     * @throws NullPointerException if the directory is `null`.
     */
    @JvmStatic
    fun sizeOfDirectory(directory: File): Long {
        return sizeOfDirectory0(directory)
    }

    /**
     * Gets the size of a directory.
     *
     * @param directory the directory to check
     * @return the size
     * @throws NullPointerException if the directory is `null`.
     */
    @JvmStatic
    private fun sizeOfDirectory0(directory: File): Long {
        Objects.requireNonNull(directory, "directory")
        val files = directory.listFiles()
            ?: // null if security restricted
            return 0L
        var size: Long = 0
        for (file in files) {
            if (!isSymlink(file.path)) {
                size += sizeOf0(file)
                if (size < 0) {
                    break
                }
            }
        }
        return size
    }

    /**
     * Tests whether the specified file is a symbolic link rather than an actual file.
     *
     *
     * This method delegates to [Files.isSymbolicLink]
     *
     *
     * @param file the file to test.
     * @return true if the file is a symbolic link, see [Files.isSymbolicLink].
     * @since 2.0
     * @see Files.isSymbolicLink
     */
    //@RequiresApi(Build.VERSION_CODES.O)
    //fun isSymlink(file: File?): Boolean {
    //    return file != null && Files.isSymbolicLink(file.toPath())
    //}

    /**
     *
     */
    @JvmStatic
    fun isSymlink(path: String): Boolean {
        val file = File(path)
        return file.canonicalFile != file.absoluteFile
    }

    /**
     * Gets the size of a file.
     *
     * @param file the file to check.
     * @return the size of the file.
     * @throws NullPointerException if the file is `null`.
     */
    private fun sizeOf0(file: File): Long {
        Objects.requireNonNull(file, "file")
        return if (file.isDirectory) {
            sizeOfDirectory0(file)
        } else file.length()
        // will be 0 if file does not exist
    }
}