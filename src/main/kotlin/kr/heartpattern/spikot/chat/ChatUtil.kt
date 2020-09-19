package kr.heartpattern.spikot.chat

import org.bukkit.ChatColor
import org.bukkit.entity.Player

fun color(text: String, color: ChatColor) = ChatBuilder(text).color(color)
fun black(text: String): ChatBuilder = color(text, ChatColor.BLACK)
fun darkBlue(text: String): ChatBuilder = color(text, ChatColor.DARK_BLUE)
fun darkGreen(text: String): ChatBuilder = color(text, ChatColor.DARK_GREEN)
fun darkAqua(text: String): ChatBuilder = color(text, ChatColor.DARK_AQUA)
fun darkRed(text: String): ChatBuilder = color(text, ChatColor.DARK_RED)
fun darkPurple(text: String): ChatBuilder = color(text, ChatColor.DARK_PURPLE)
fun gold(text: String): ChatBuilder = color(text, ChatColor.GOLD)
fun gray(text: String): ChatBuilder = color(text, ChatColor.GRAY)
fun darkGray(text: String): ChatBuilder = color(text, ChatColor.DARK_GRAY)
fun blue(text: String): ChatBuilder = color(text, ChatColor.BLUE)
fun green(text: String): ChatBuilder = color(text, ChatColor.GREEN)
fun aqua(text: String): ChatBuilder = color(text, ChatColor.AQUA)
fun red(text: String): ChatBuilder = color(text, ChatColor.RED)
fun lightPurple(text: String): ChatBuilder = color(text, ChatColor.LIGHT_PURPLE)
fun yellow(text: String): ChatBuilder = color(text, ChatColor.YELLOW)
fun white(text: String): ChatBuilder = color(text, ChatColor.WHITE)

fun Player.sendJson(block: ChatMessageBuilder.() -> Unit) {
    val build = ChatMessageBuilder()
    build.block()
    sendMessage(build.result.toChatComponent())
}

fun Player.sendJson(message: ChatBuilder) {
    sendMessage(message.toChatComponent())
}

class ChatMessageBuilder(val result: ChatBuilder = ChatBuilder("")) {

    operator fun ChatBuilder.unaryPlus() {
        result += this
    }
}