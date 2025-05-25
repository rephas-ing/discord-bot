/*
 * Copyrighted (Rephasing, 2025). Licensed under the EUPL-1.2
 * with the specific provision (EUPL articles 14 & 15) that the
 * applicable law is the (Republic of) Irish law and the Jurisdiction
 * Dublin.
 * Any redistribution must include the specific provision above.
 */

package ing.rephas.discord.bots.general.utils.welcome

import dev.kord.common.entity.Snowflake
import dev.kordex.modules.func.welcome.data.WelcomeChannelData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlin.io.path.*

private val path = Path("./data/welcome-channels.json")

@OptIn(ExperimentalSerializationApi::class)
class RephasingWelcomeChannelData : WelcomeChannelData {
	private var data: MutableMap<Snowflake, String> = mutableMapOf()

	init {
		load()
	}

	private fun load() {
		path.createParentDirectories()

		if (path.exists()) {
			data = Json.decodeFromStream(path.inputStream())
		}
	}

	private fun save() {
		Json.encodeToStream(data, path.outputStream())
	}

	override suspend fun getChannelURLs(): Map<Snowflake, String> = data
	override suspend fun getUrlForChannel(channelId: Snowflake): String? = data[channelId]

	override suspend fun setUrlForChannel(channelId: Snowflake, url: String) {
		data[channelId] = url

		save()
	}

	override suspend fun removeChannel(channelId: Snowflake): String? {
		val result = data.remove(channelId)

		save()

		return result
	}
}
