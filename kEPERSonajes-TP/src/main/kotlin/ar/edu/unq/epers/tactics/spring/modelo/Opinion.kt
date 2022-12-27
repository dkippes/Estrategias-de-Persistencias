package ar.edu.unq.epers.tactics.spring.modelo

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Validated
class Opinion {
    var descripcion: String? = null

    @NotBlank
    var hechaPor: String? = null

    @Min(0)
    @Max(10)
    var puntuacion: Int? = null

    constructor(descripcion: String, hechaPor: String, puntuacion: Int) {
        this.descripcion = descripcion
        this.hechaPor = hechaPor
        this.puntuacion = puntuacion
    }
}