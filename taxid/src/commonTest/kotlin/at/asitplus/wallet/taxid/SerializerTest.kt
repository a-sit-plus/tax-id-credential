package at.asitplus.wallet.taxid

import at.asitplus.wallet.lib.data.vckJsonSerializer
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.matthewnelson.encoding.base64.Base64
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlin.random.Random


class SerializerTest : FreeSpec({

    "Serialization and deserialization" - {

        "mandatory fields" {
            repeat(100) {

                val cred = TaxIdCredential(
                    taxNumber = randomString(),
                    affiliationCountry = randomString(),
                    registeredFamilyName = randomString(),
                    registeredGivenName = randomString(),
                    residentAddress = randomString(),
                    birthDate = randomLocalDate(),
                    issuanceDate = randomInstant(),
                    expiryDate = randomInstant(),
                    issuingAuthority = randomString(),
                    issuingCountry = randomString(),
                )
                val serialized = vckJsonSerializer.encodeToString(cred)
                val parsed: TaxIdCredential = vckJsonSerializer.decodeFromString(serialized)
                parsed shouldBe cred
            }
        }
        "all fields" {
            repeat(100) {

                val cred = TaxIdCredential(
                    taxNumber = randomString(),
                    affiliationCountry = randomString(),
                    registeredFamilyName = randomString(),
                    registeredGivenName = randomString(),
                    residentAddress = randomString(),
                    birthDate = randomLocalDate(),
                    issuanceDate = randomInstant(),
                    expiryDate = randomInstant(),
                    issuingAuthority = randomString(),
                    issuingCountry = randomString(),
                    churchTaxId = randomString(),
                    iban = randomString(),
                    pidId = randomString(),
                    verificationStatus = randomString(),
                    documentNumber = randomString(),
                    administrativeNumber = randomString(),
                    issuingJurisdiction = randomString(),
                )
                val serialized = vckJsonSerializer.encodeToString(cred)
                val parsed: TaxIdCredential = vckJsonSerializer.decodeFromString(serialized)
                parsed shouldBe cred
            }
        }
    }
})

private fun randomString() = Random.nextBytes(16).encodeToString(Base64())

private fun randomInstant() = Instant.fromEpochMilliseconds(Random.nextLong())

private fun randomLocalDate() = LocalDate(Random.nextInt(1900, 2100), Random.nextInt(1, 12), Random.nextInt(1, 28))
