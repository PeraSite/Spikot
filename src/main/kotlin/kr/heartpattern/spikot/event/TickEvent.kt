package kr.heartpattern.spikot.event

import kr.heartpattern.spikot.module.AbstractModule
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

internal object TickEventEmitter : AbstractModule() {
    override fun onEnable() {
        var tickCount = 0
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            TickEvent(tickCount++).execute()
        }, 1L, 1L)
    }
}

/**
 * Event which fire every thick
 * @param tick Number of tick that TickEvent fired.
 */
class TickEvent(val tick: Int) : Event() {
    companion object {
        @JvmField
        val handlerList: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}
