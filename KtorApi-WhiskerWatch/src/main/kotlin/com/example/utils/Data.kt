package com.example.utils

import com.example.models.users.Rol
import com.example.models.users.User
import com.example.repositories.calendar.CalendarRepositoryImpl
import com.example.repositories.forum.ForumRepositoryImpl
import com.example.repositories.maps.MapRepositoryImpl
import com.example.repositories.users.AssociationRepositoryImpl
import com.example.repositories.users.UserRepositoryImpl
import com.example.services.password.BcryptService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.koin.core.annotation.Singleton
import java.util.*

@Singleton
class Data(
    private val userRep: UserRepositoryImpl,
    private val associationRep: AssociationRepositoryImpl,
    private val mapsRep: MapRepositoryImpl,
    private val calendarsRep: CalendarRepositoryImpl,
    private val forumRep: ForumRepositoryImpl,
    private val passwordEncoder: BcryptService
) {
    private val props: Properties = Properties()
    private val logger = KotlinLogging.logger {}

    suspend fun cleanData() = withContext(Dispatchers.IO){
        props.load(javaClass.classLoader.getResourceAsStream("data.properties"))
        val clean = props.getProperty("clean.data").toBoolean()
        if (clean){
            logger.info { "Limpiando datos del sistema" }
            launch {
                userRep.deleteAll()
            }
            launch {
                associationRep.deleteAll()
            }
            launch {
                mapsRep.deleteAll()
            }
            launch {
                calendarsRep.deleteAll()
            }
            launch {
                forumRep.deleteAll()
            }
        }
    }

    suspend fun firstUseData() = withContext(Dispatchers.IO) {
        props.load(javaClass.classLoader.getResourceAsStream("data.properties"))
        val first = props.getProperty("first.use").toBoolean()
        if(first){
            val admin: User = User(name = "admin", email = "admin@gmail.com", password = passwordEncoder.encryptPassword("123456789"), username="admin", rol = Rol.ADMIN)
            userRep.save(admin)
            props.setProperty("first.use", "false")
        }
    }
}