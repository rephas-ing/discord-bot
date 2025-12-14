/*
 * Copyrighted (Rephasing, 2025). Licensed under the EUPL-1.2
 * with the specific provision (EUPL articles 14 & 15) that the
 * applicable law is the (Republic of) Irish law and the Jurisdiction
 * Dublin.
 * Any redistribution must include the specific provision above.
 */

@file:Suppress("StringLiteralDuplication")

package ing.rephas.discord.bots.general.extensions

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.effectiveName
import dev.kordex.core.checks.hasRole
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.ephemeralSubCommand
import dev.kordex.core.commands.converters.impl.member
import dev.kordex.core.commands.converters.impl.user
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.ephemeralSlashCommand
import dev.kordex.core.i18n.toKey
import ing.rephas.discord.bots.general.extensions.PhaseTeamExtension.RoleUserArgs

val ROLE_PHASE_TEAM = Snowflake("1363486661425500270")
val ROLE_CONTRIBUTORS = Snowflake("1449813057701281793")
val ROLE_TRUSTED = Snowflake("1449813091628875887")

class PhaseTeamExtension  : Extension() {
	override val name: String = "phase-team"

	override suspend fun setup() {
		ephemeralSlashCommand {
			name = "phase".toKey()
			description = "Management commands for the Phase team".toKey()

			check {
				hasRole(ROLE_PHASE_TEAM)
			}

			ephemeralSubCommand(::RoleUserArgs) {
				name = "add-contributor".toKey()
				description = "Assign the Phase Contributor role".toKey()

				action {
					arguments.member.addRole(
						ROLE_CONTRIBUTORS,
						"Requested by ${user.asUser().effectiveName}"
					)

					respond {
						content = "Assigned the <@&$ROLE_CONTRIBUTORS> role to ${arguments.member.mention}."
					}
				}
			}

			ephemeralSubCommand(::RoleUserArgs) {
				name = "del-contributor".toKey()
				description = "Remove the Phase Contributor role".toKey()

				action {
					arguments.member.removeRole(
						ROLE_CONTRIBUTORS,
						"Requested by ${user.asUser().effectiveName}"
					)

					respond {
						content = "Removed the <@&$ROLE_CONTRIBUTORS> role from ${arguments.member.mention}."
					}
				}
			}

			ephemeralSubCommand(::RoleUserArgs) {
				name = "add-trusted".toKey()
				description = "Assign the Phase Trusted role".toKey()

				action {
					arguments.member.addRole(
						ROLE_TRUSTED,
						"Requested by ${user.asUser().effectiveName}"
					)

					respond {
						content = "Assigned the <@&$ROLE_TRUSTED> role to ${arguments.member.mention}."
					}
				}
			}

			ephemeralSubCommand(::RoleUserArgs) {
				name = "del-trusted".toKey()
				description = "Remove the Phase Trusted role".toKey()

				action {
					arguments.member.removeRole(
						ROLE_TRUSTED,
						"Requested by ${user.asUser().effectiveName}"
					)

					respond {
						content = "Removed the <@&$ROLE_TRUSTED> role from ${arguments.member.mention}."
					}
				}
			}
		}
	}

	class RoleUserArgs : Arguments() {
		val member by member {
			name = "user".toKey()
			description = "User to add/remove the role to/from.".toKey()
		}
	}
}
