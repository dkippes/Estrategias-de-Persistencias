package ar.edu.unq.unidad1.dao.impl

import java.io.File
import java.io.IOException
import java.nio.file.Files

/**
 * TODO: Esta es una implementacion sencilla que tiene como objeto
 * mostrar algunas formas de persistir en archivos (usando distintas
 * herramientas para guardar en distintos formatos)
 *
 * Entre todas las implementaciones de BaseFileDAO existe mucho codigo
 * repetido (relacionado a verificar si el archivo existe o no, abrir
 * y cerrar el archivo, etc). Ese codigo repetido NO deberia existir
 * ya que deberia ser responsabilidad de estar super clase.
 *
 */
open class BaseFileDAO(private val extension: String) {

    protected fun getStorage(nombre: String): File {
        return File("data/$nombre.$extension")
    }

    /**
     * Remueve el archivo si es que existe
     */
    protected fun deleteIfExists(dataFile: File) {
        try {
            Files.deleteIfExists(dataFile.toPath())
        } catch (e: IOException) {
            throw RuntimeException("No se puede eliminar el archivo $dataFile", e)
        }
    }

}