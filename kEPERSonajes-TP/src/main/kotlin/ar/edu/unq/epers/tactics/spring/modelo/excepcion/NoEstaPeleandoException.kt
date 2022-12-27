package ar.edu.unq.epers.tactics.spring.modelo.excepcion

class NoEstaPeleandoException: RuntimeException() {
    override val message: String?
        get() = "Una party no puede  terminar una pelea si no empezó ninguna"
}