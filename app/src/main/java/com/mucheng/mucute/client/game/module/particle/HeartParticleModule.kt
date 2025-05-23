package com.mucheng.mucute.client.game.module.particle

import com.mucheng.mucute.client.game.InterceptablePacket
import com.mucheng.mucute.client.game.Module
import com.mucheng.mucute.client.game.ModuleCategory
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket

class HeartParticleModule : Module("heart_particle", ModuleCategory.Particle) {

    private val intervalValue by intValue("interval", 500, 100..2000)
    private val particleCount by intValue("count", 1, 1..10)

    private var lastParticleTime = 0L

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) {
            return
        }

        val packet = interceptablePacket.packet
        if (packet is PlayerAuthInputPacket) {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastParticleTime >= intervalValue) {
                lastParticleTime = currentTime

                repeat(particleCount) {

                    val entityEventPacket = EntityEventPacket().apply {
                        runtimeEntityId = session.localPlayer.runtimeEntityId
                        type = EntityEventType.LOVE_PARTICLES
                        data = 0
                    }
                    session.clientBound(entityEventPacket)
                }
            }
        }
    }
}