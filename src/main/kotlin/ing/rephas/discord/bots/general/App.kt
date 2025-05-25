/*
 * Copyrighted (Rephasing, 2025). Licensed under the EUPL-1.2
 * with the specific provision (EUPL articles 14 & 15) that the
 * applicable law is the (Republic of) Irish law and the Jurisdiction
 * Dublin.
 * Any redistribution must include the specific provision above.
 */
package ing.rephas.discord.bots.general

import dev.kord.common.entity.Permission
import dev.kord.core.behavior.channel.asChannelOf
import dev.kordex.core.ExtensibleBot
import dev.kordex.core.checks.hasPermission
import dev.kordex.core.utils.env
import dev.kordex.modules.func.phishing.DetectionAction
import dev.kordex.modules.func.phishing.extPhishing
import dev.kordex.modules.func.welcome.welcomeChannel
import dev.kordex.modules.pluralkit.extPluralKit
import ing.rephas.discord.bots.general.utils.welcome.RephasingWelcomeChannelData
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.last
import java.io.File
import kotlin.time.Duration.Companion.seconds

private val TOKEN = env("TOKEN")

suspend fun main() {
	val bot = ExtensibleBot(TOKEN) {
		applicationCommands {
			defaultGuild(env("TEST_SERVER"))
		}

		extensions {
			extPhishing {
				detectionAction = DetectionAction.Kick
				notifyUser = false
				logChannelName = "phishing-logs"
			}

			extPluralKit()

			welcomeChannel(RephasingWelcomeChannelData()) {
				refreshDuration = 30.seconds

				getLogChannel { channel, guild ->
					guild.channels.filter { it.name == "welcome-logs" }.last().asChannelOf()
				}

				staffCommandCheck {
					hasPermission(Permission.ManageMessages)
				}
			}
		}

		if (devMode) {
			plugins {
				if (File("src/main/dist/plugins").isDirectory) {
					pluginPath("src/main/dist/plugins")
				}
			}
		}
	}

	bot.start()
}
