package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.SesionDao
import com.example.belmonteapp.modelo.Sesion
import com.example.belmonteapp.remote.ApiService
import com.example.belmonteapp.remote.LoginRequest
import com.example.belmonteapp.remote.LoginResponse
import com.example.belmonteapp.remote.UsuarioDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

//Prueba el login contra ms-auth simulando la API con MockK
class SesionRepositoryTest : StringSpec({

    "login() guarda en Room el token y el rol que entrega ms-auth" {
        val mockApi = mockk<ApiService>()
        val sesionDao = mockk<SesionDao>(relaxed = true)
        coEvery { mockApi.login(LoginRequest("conductor@belmonte.cl", "123")) } returns LoginResponse(
            token = "token-jwt",
            usuario = UsuarioDto(id = 3, email = "conductor@belmonte.cl", rol = "CONDUCTOR", idReferencia = 1)
        )
        val repo = SesionRepository(sesionDao, mockApi)

        runTest {
            val sesion = repo.login("conductor@belmonte.cl", "123")

            sesion.token shouldBe "token-jwt"
            sesion.rol shouldBe "CONDUCTOR"
            sesion.idReferencia shouldBe 1
        }
        //primero borra la sesion anterior y despues guarda la nueva
        coVerifyOrder {
            sesionDao.borrar()
            sesionDao.guardar(Sesion(token = "token-jwt", email = "conductor@belmonte.cl", rol = "CONDUCTOR", idReferencia = 1))
        }
    }

    "login() del administrador guarda idReferencia 0 (el backend manda null)" {
        val mockApi = mockk<ApiService>()
        val sesionDao = mockk<SesionDao>(relaxed = true)
        coEvery { mockApi.login(any()) } returns LoginResponse(
            token = "token-admin",
            usuario = UsuarioDto(id = 2, email = "admin@belmonte.cl", rol = "ADMIN", idReferencia = null)
        )
        val repo = SesionRepository(sesionDao, mockApi)

        runTest {
            repo.login("admin@belmonte.cl", "123").idReferencia shouldBe 0
        }
    }

    "cerrarSesion() borra el token de Room" {
        val sesionDao = mockk<SesionDao>(relaxed = true)
        val repo = SesionRepository(sesionDao, mockk())

        runTest {
            repo.cerrarSesion()
        }
        coVerify { sesionDao.borrar() }
    }
})
