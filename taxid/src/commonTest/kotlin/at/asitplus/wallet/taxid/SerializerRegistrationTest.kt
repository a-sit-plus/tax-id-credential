package at.asitplus.wallet.taxid

import at.asitplus.signum.indispensable.CryptoSignature
import at.asitplus.signum.indispensable.cosef.*
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import at.asitplus.wallet.lib.data.CredentialToJsonConverter
import at.asitplus.wallet.lib.iso.*
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.matthewnelson.encoding.base64.Base64
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlin.random.Random
import kotlin.random.nextUInt


class SerializerRegistrationTest : FreeSpec({

    "Serialization and deserialization" - {
        withData(nameFn = { " for ${it.key}" }, dataMap().entries) {

            val item = it.toIssuerSignedItem()
            val serialized = item.serialize(TaxIdScheme.isoNamespace)

            val deserialized =
                IssuerSignedItem.deserialize(serialized, TaxIdScheme.isoNamespace, item.elementIdentifier)
                    .getOrThrow()

            deserialized.elementValue shouldBe it.value
        }
    }

    "Serialization to JSON Element" {

        val mso = MobileSecurityObject(
            version = "1.0",
            digestAlgorithm = "SHA-256",
            valueDigests = mapOf("foo" to ValueDigestList(listOf(ValueDigest(0U, byteArrayOf())))),
            deviceKeyInfo = deviceKeyInfo(),
            docType = "docType",
            validityInfo = ValidityInfo(Clock.System.now(), Clock.System.now(), Clock.System.now())
        )

        val claims = dataMap()
        val namespacedItems: Map<String, List<IssuerSignedItem>> =
            mapOf(TaxIdScheme.isoNamespace to claims.map { it.toIssuerSignedItem() }.toList())
        val issuerAuth = CoseSigned.create(
            CoseHeader(), null, mso, CryptoSignature.RSAorHMAC(byteArrayOf(1, 3, 3, 7)),
            MobileSecurityObject.serializer()
        )
        val credential = SubjectCredentialStore.StoreEntry.Iso(
            IssuerSigned.fromIssuerSignedItems(namespacedItems, issuerAuth),
            TaxIdScheme.isoNamespace
        )
        val converted = CredentialToJsonConverter.toJsonElement(credential)
            .shouldBeInstanceOf<JsonObject>()
        val jsonMap = converted[TaxIdScheme.isoNamespace]
            .shouldBeInstanceOf<JsonObject>()

        claims.forEach {
            withClue("Serialization for ${it.key}") {
                jsonMap[it.key].shouldNotBeNull()
            }
        }
    }

})

private fun Map.Entry<String, Any>.toIssuerSignedItem() =
    IssuerSignedItem(Random.nextUInt(), Random.nextBytes(32), key, value)

private fun dataMap(): Map<String, Any> {
    val instant = randomInstant()
    val expiry = Instant.fromEpochMilliseconds(instant.epochSeconds + 1)

    return mapOf(
        TaxIdScheme.Attributes.TAX_NUMBER to randomString(),
        TaxIdScheme.Attributes.AFFILIATION_COUNTRY to randomString(),
        TaxIdScheme.Attributes.REGISTERED_FAMILY_NAME to randomString(),
        TaxIdScheme.Attributes.REGISTERED_GIVEN_NAME to randomString(),
        TaxIdScheme.Attributes.RESIDENT_ADDRESS to randomString(),
        TaxIdScheme.Attributes.BIRTH_DATE to randomLocalDate(),
        TaxIdScheme.Attributes.CHURCH_TAX_ID to randomString(),
        TaxIdScheme.Attributes.IBAN to randomString(),
        TaxIdScheme.Attributes.PID_ID to randomString(),
        TaxIdScheme.Attributes.ISSUANCE_DATE to instant,
        TaxIdScheme.Attributes.VERIFICATION_STATUS to randomString(),
        TaxIdScheme.Attributes.EXPIRY_DATE to expiry,
        TaxIdScheme.Attributes.ISSUING_AUTHORITY to randomString(),
        TaxIdScheme.Attributes.DOCUMENT_NUMBER to randomString(),
        TaxIdScheme.Attributes.ADMINISTRATIVE_NUMBER to randomString(),
        TaxIdScheme.Attributes.ADMINISTRATIVE_NUMBER to randomString(),
        TaxIdScheme.Attributes.ISSUING_COUNTRY to randomString(),
        TaxIdScheme.Attributes.ISSUING_JURISDICTION to randomString(),

        )
}

private fun randomString() = Random.nextBytes(16).encodeToString(Base64())


private fun randomInstant() = Instant.fromEpochMilliseconds(Random.nextLong())

private fun randomLocalDate() = LocalDate(Random.nextInt(1900, 2100), Random.nextInt(1, 12), Random.nextInt(1, 28))


private fun deviceKeyInfo() =
    DeviceKeyInfo(CoseKey(CoseKeyType.EC2, keyParams = CoseKeyParams.EcYBoolParams(CoseEllipticCurve.P256)))
