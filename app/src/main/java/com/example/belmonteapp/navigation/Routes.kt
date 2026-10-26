package com.example.belmonteapp.navigation

object Routes {
    const val  LOGIN = "login"
    const val  HOME = "home"
    const val REGISTRO = "registro"
    const val PERFIL = "perfil"
    const val NUEVO_VIAJE = "nuevo viaje"
    const val CONDUCTOR = "Conductor"
    const val ADMIN = "Administrador"
    const val MENU = "Menu"
    const val VIAJE = "Viaje"
    const val MIS_VIAJES = "Mis viajes"

    //---- Pantallas del conductor ----
    const val VIAJES_DIA = "viajes_dia"
    const val VIAJES_ASIGNADOS = "viajes_asignados"
    const val DETALLE_VIAJE = "detalle_viaje/{viajeId}"
    const val VIAJE_EN_CURSO = "viaje_en_curso/{viajeId}"
    const val VIAJE_FINALIZADO = "viaje_finalizado/{viajeId}"

    //---- Pantallas del administrador ----
    //PENDIENTES es la pantalla de solicitudes pendientes (la hace el otro integrante)
    const val PENDIENTES = "pendientes"
    const val VIAJES_FECHA = "viajes_fecha"
    const val ASIGNAR_CONDUCTOR = "asignar_conductor/{viajeId}"
    const val MODIFICAR_ASIGNACION = "modificar_asignacion/{viajeId}"

    //Rutas con el id del viaje ya puesto, para usar en navController.navigate(...)
    fun detalleViaje(id: Int) = "detalle_viaje/$id"
    fun viajeEnCurso(id: Int) = "viaje_en_curso/$id"
    fun viajeFinalizado(id: Int) = "viaje_finalizado/$id"
    fun asignarConductor(id: Int) = "asignar_conductor/$id"
    fun modificarAsignacion(id: Int) = "modificar_asignacion/$id"
}