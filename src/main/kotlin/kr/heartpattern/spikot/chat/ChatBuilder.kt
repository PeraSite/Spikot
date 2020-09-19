package kr.heartpattern.spikot.chat

import com.github.salomonbrys.kotson.set
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.md_5.bungee.api.chat.*
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ChatBuilder(private var text: String) {
    private var bold = false
    private var italic = false
    private var underlined = false
    private var strikethrough = false
    private var obfuscated = false
    private var color = ChatColor.RESET
    private var keybind = false
    private var insertion: String? = ""
    private var clickEvent: Pair<ClickEventType, String>? = null
    private var hoverEvent: Pair<HoverEventType, ChatBuilder>? = null
    private val extra: MutableList<ChatBuilder> = ArrayList()

    fun text(text: String): ChatBuilder = apply { this.text = text }
    fun bold(): ChatBuilder = apply { bold = !bold }
    fun italic(): ChatBuilder = apply { italic = !italic }
    fun underline(): ChatBuilder = apply { underlined = !underlined }
    fun strike(): ChatBuilder = apply { this.strikethrough = !strikethrough }
    fun obfuscate(): ChatBuilder = apply { obfuscated = !obfuscated }
    fun color(color: ChatColor): ChatBuilder = apply { this.color = color }
    fun insertion(insertion: String): ChatBuilder = apply { this.insertion = insertion }
    fun keybind(keybind: Boolean): ChatBuilder = apply { this.keybind = keybind }

    fun click(type: ClickEventType, clickText: String): ChatBuilder = apply { clickEvent = Pair(type, clickText) }
    fun hover(type: HoverEventType, hoverText: ChatBuilder): ChatBuilder = apply { hoverEvent = Pair(type, hoverText) }

    fun toChatComponent(): BaseComponent {
        val component = if (keybind) {
            KeybindComponent(text)
        } else {
            TextComponent(text)
        }
        component.isBold = bold
        component.isItalic = italic
        component.isUnderlined = underlined
        component.isStrikethrough = strikethrough
        component.isObfuscated = obfuscated
        component.color = color.asBungee()
        component.insertion = insertion
        val ce = clickEvent
        if (ce != null) {
            component.clickEvent = ClickEvent(ce.first.type, ce.second)
        }
        val he = hoverEvent
        if (he != null) {
            component.hoverEvent = HoverEvent(he.first.type, arrayOf(he.second.toChatComponent()))
        }
        extra.forEach {
            component.addExtra(it.toChatComponent())
        }
        return component
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun toJson(): JsonObject {
        val json = JsonObject()
        if (keybind) {
            json["keybind"] = text
        } else {
            json["text"] = text
        }
        json["bold"] = bold.toString()
        json["italic"] = italic.toString()
        json["underlined"] = underlined.toString()
        json["strikethrough"] = strikethrough.toString()
        json["obfuscated"] = obfuscated.toString()
        json["color"] = color.name
        insertion?.let {
            json["insertion"] = it
        }
        clickEvent?.let {
            val cJson = JsonObject()
            cJson["action"] = it.first.json
            cJson["value"] = it.second
            json["clickEvent"] = cJson
        }
        hoverEvent?.let {
            val hJson = JsonObject()
            hJson["action"] = it.first.json
            hJson["value"] = it.second
            json["hoverEvent"] = hJson
        }
        if (extra.isNotEmpty()) {
            val array = JsonArray()
            for (builder in extra) {
                array.add(builder.toJson())
            }
            json["extra"] = array
        }
        return json
    }

    operator fun plus(other: ChatBuilder): ChatBuilder = apply { extra.add(other) }
    operator fun plusAssign(other: ChatBuilder) {
        extra.add(other)
    }

    override fun toString(): String {
        return toJson().toString()
    }
}

enum class ClickEventType(val json: String, val type: ClickEvent.Action) {
    OPEN_URL("open_url", ClickEvent.Action.OPEN_URL),
    RUN_COMMAND("run_command", ClickEvent.Action.RUN_COMMAND),
    SUGGEST_COMMAND("suggest_command", ClickEvent.Action.SUGGEST_COMMAND)
}

enum class HoverEventType(val json: String, val type: HoverEvent.Action) {
    SHOW_TEXT("show_text", HoverEvent.Action.SHOW_TEXT),
    SHOW_ITEM("show_item", HoverEvent.Action.SHOW_ITEM),
    SHOW_ENTITY("show_entity", HoverEvent.Action.SHOW_ENTITY)
}