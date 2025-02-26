package com.mucheng.mucute.client.game.module.particle

import com.mucheng.mucute.client.game.InterceptablePacket
import com.mucheng.mucute.client.game.Module
import com.mucheng.mucute.client.game.ModuleCategory
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.data.LevelEvent
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket

class FizzParticleModule : Module("fizz_particle", ModuleCategory.Particle) {

    private val intervalValue by intValue("interval", 500, 100..2000)
    private val offsetY by floatValue("height_offset", 1.0f, -2.0f..5.0f)
    private val particleSize by floatValue("size", 1.0f, 0.1f..5.0f)
    private val particleCount by intValue("count", 1, 1..10)
    private val randomOffset by boolValue("random_offset", false)
    private val offsetRadius by floatValue("offset_radius", 1.0f, 0.1f..5.0f)

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
                    val offsetX = if (randomOffset) (Math.random() * 2 - 1) * offsetRadius else 0.0
                    val offsetZ = if (randomOffset) (Math.random() * 2 - 1) * offsetRadius else 0.0

                    session.clientBound(LevelEventPacket().apply {
                        type = LevelEvent.PARTICLE_FIZZ_EFFECT
                        position = Vector3f.from(
                            packet.position.x + offsetX.toFloat(),
                            packet.position.y + offsetY,
                            packet.position.z + offsetZ.toFloat()
                        )
                        data = (particleSize * 1000).toInt()
                    })
                }
            }
        }
    }
}