package ar.edu.unq.epers.tactics.spring.controllers.dto

import ar.edu.unq.epers.tactics.spring.modelo.habilidades.*
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.Randomizador
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = AtaqueDTO::class, name = "Attack"),
    JsonSubTypes.Type(value = DefensaDTO::class, name = "Defend"),
    JsonSubTypes.Type(value = CurarDTO::class, name = "Heal"),
    JsonSubTypes.Type(value = AtaqueMagicoDTO::class, name = "MagicAttack"),
    JsonSubTypes.Type(value = MeditarDTO::class, name = "Meditate"),
    JsonSubTypes.Type(value = HabilidadNulaDTO::class, name = "Nothing")
)
abstract class HabilidadDTO() {
    companion object {

        fun desdeModelo(habilidad: Habilidad): HabilidadDTO {
            return when (habilidad) {
                is Ataque -> AtaqueDTO(
                    "ATAQUE_FISICO",
                    habilidad.danoFisico,
                    habilidad.precisionFisica,
                    AventureroDTO.desdeModelo(habilidad.aventureroReceptor)
                )
                is Defensa -> DefensaDTO(
                    "DEFENDER",
                    AventureroDTO.desdeModelo(habilidad.aventureroEmisor!!),
                    AventureroDTO.desdeModelo(habilidad.aventureroReceptor)
                )
                is Curacion -> CurarDTO(
                    "CURAR",
                    habilidad.poderMagicoEmisor,
                    AventureroDTO.desdeModelo(habilidad.aventureroReceptor)
                )
                is AtaqueMagico -> AtaqueMagicoDTO(
                    "ATAQUE_MAGICO",
                    habilidad.poderMagicoEmisor,
                    habilidad.nivelEmisor,
                    AventureroDTO.desdeModelo(habilidad.aventureroReceptor)
                )
                is Meditacion -> {
                    return MeditarDTO(
                        "MEDITAR",
                        AventureroDTO.desdeModelo(habilidad.aventureroReceptor)
                    )
                }
                else -> {
                    HabilidadNulaDTO(
                        "NADA",
                        AventureroDTO.desdeModelo(habilidad.aventureroReceptor)
                    )
                }
            }
        }
    }

    abstract fun aModelo(): Habilidad

}

class HabilidadNulaDTO(var tipo: String, val objetivo: AventureroDTO) : HabilidadDTO() {
    override fun aModelo() = HabilidadNula(objetivo.aModelo(), objetivo.aModelo())
}

data class AtaqueDTO(val tipo: String, val daño: Double, val precisionFisica: Double, val objetivo: AventureroDTO) :
    HabilidadDTO() {
    override fun aModelo() = Ataque(objetivo.aModelo(), null, precisionFisica, daño, Randomizador())
}

class DefensaDTO(val tipo: String, val source: AventureroDTO, val objetivo: AventureroDTO) : HabilidadDTO() {
    override fun aModelo() = Defensa(source.aModelo(), objetivo.aModelo())
}

data class CurarDTO(val tipo: String, val poderMagico: Double, val objetivo: AventureroDTO) : HabilidadDTO() {
    override fun aModelo() = Curacion(objetivo.aModelo(), null, poderMagico)
}

data class AtaqueMagicoDTO(
    val tipo: String,
    val poderMagico: Double,
    val sourceLevel: Int,
    val objetivo: AventureroDTO
) : HabilidadDTO() {
    override fun aModelo() = AtaqueMagico(objetivo.aModelo(), null, sourceLevel, poderMagico, Randomizador())
}

data class MeditarDTO(val tipo: String, val objetivo: AventureroDTO) : HabilidadDTO() {
    override fun aModelo() = Meditacion(objetivo.aModelo(), objetivo.aModelo())
}