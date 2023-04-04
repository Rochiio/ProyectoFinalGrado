package com.example.services.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.TokenConfig
import com.example.models.Association
import com.example.models.User
import org.koin.core.annotation.Single
import java.util.*

@Single
class TokenService(
    private val config: TokenConfig
) {
    fun generateTokenUser(user: User): String{
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("email",user.email)
            .withClaim("rol", user.rol.name)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiration * 10000L ))
            .sign(Algorithm.HMAC512(config.secret))
    }

    fun generateTokenAssociation(association: Association): String{
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("email",association.email)
            .withClaim("rol", association.rol.name)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiration * 10000L ))
            .sign(Algorithm.HMAC512(config.secret))
    }

    fun verifyToken():JWTVerifier{
        return JWT.require(Algorithm.HMAC512(config.secret))
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .build()
    }
}