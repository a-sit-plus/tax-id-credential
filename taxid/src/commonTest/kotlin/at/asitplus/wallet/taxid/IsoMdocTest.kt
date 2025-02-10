package at.asitplus.wallet.taxid

import at.asitplus.signum.indispensable.cosef.CoseHeader
import at.asitplus.signum.indispensable.cosef.CoseKey
import at.asitplus.signum.indispensable.cosef.CoseSigned
import at.asitplus.signum.indispensable.cosef.io.ByteStringWrapper
import at.asitplus.signum.indispensable.cosef.toCoseKey
import at.asitplus.wallet.lib.agent.DefaultCryptoService
import at.asitplus.wallet.lib.agent.EphemeralKeyWithoutCert
import at.asitplus.wallet.lib.cbor.DefaultCoseService
import at.asitplus.wallet.lib.cbor.DefaultVerifierCoseService
import at.asitplus.wallet.lib.data.vckJsonSerializer
import at.asitplus.wallet.lib.iso.*
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.AFFILIATION_COUNTRY
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.BIRTH_DATE
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.EXPIRY_DATE
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.ISSUANCE_DATE
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.ISSUING_AUTHORITY
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.ISSUING_COUNTRY
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.REGISTERED_FAMILY_NAME
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.REGISTERED_GIVEN_NAME
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.RESIDENT_ADDRESS
import at.asitplus.wallet.taxid.TaxIdScheme.Attributes.TAX_NUMBER
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.encodeToString
import kotlin.random.Random

class IsoMdocTest : FreeSpec({

    "issue, store, present, verify" {
        val wallet = Wallet()
        val verifier = Verifier()
        val issuer = Issuer()

        val deviceResponse = issuer.buildDeviceResponse(wallet.deviceKeyInfo)
        wallet.storeMdl(deviceResponse)

        val verifierRequest = verifier.buildDeviceRequest()
        val walletResponse = wallet.buildDeviceResponse(verifierRequest)
        verifier.verifyResponse(walletResponse, issuer.cryptoService.keyMaterial.publicKey.toCoseKey().getOrThrow())

        walletResponse.documents!!.first().issuerSigned.namespaces!!.forEach { (ns,obj)->
            TaxIdScheme.requiredClaims.forEach { id->
                val item= obj.entries.find { it.value.elementIdentifier ==id }
                item.shouldNotBeNull()
                println("$id: ${item.value.elementValue}")
            }
        }
    }

})

class Wallet {

    val cryptoService = DefaultCryptoService(EphemeralKeyWithoutCert())
    val coseService = DefaultCoseService(cryptoService)

    val deviceKeyInfo = DeviceKeyInfo(
        deviceKey = cryptoService.keyMaterial.publicKey.toCoseKey().getOrThrow()
    )

    var storedMdl: TaxIdCredential? = null
    var storedIssuerAuth: CoseSigned<MobileSecurityObject>? = null
    var storedMdlItems: IssuerSignedList? = null

    fun storeMdl(deviceResponse: DeviceResponse) {
        val document = deviceResponse.documents?.first().shouldNotBeNull()
        document.docType shouldBe TaxIdScheme.isoDocType
        val issuerAuth = document.issuerSigned.issuerAuth
        this.storedIssuerAuth = issuerAuth

        issuerAuth.payload.shouldNotBeNull()
        val mso = document.issuerSigned.issuerAuth.payload!!

        val mdlItems = document.issuerSigned.namespaces?.get(TaxIdScheme.isoNamespace).shouldNotBeNull()
        this.storedMdlItems = mdlItems
        mso.valueDigests[TaxIdScheme.isoNamespace].shouldNotBeNull()

        storedMdl = TaxIdCredential(
            extractDataString(mdlItems, TAX_NUMBER),
            extractDataString(mdlItems, AFFILIATION_COUNTRY),
            extractDataString(mdlItems, REGISTERED_FAMILY_NAME),
            extractDataString(mdlItems, REGISTERED_GIVEN_NAME),
            extractDataString(mdlItems, RESIDENT_ADDRESS),
            LocalDate.parse(extractDataString(mdlItems, BIRTH_DATE)),
            issuanceDate = Instant.parse(extractDataString(mdlItems, ISSUANCE_DATE)),
            expiryDate = Instant.parse(extractDataString(mdlItems, EXPIRY_DATE)),
            issuingAuthority = extractDataString(mdlItems, ISSUING_AUTHORITY),
            issuingCountry = extractDataString(mdlItems, ISSUING_COUNTRY),
        )
    }

    suspend fun buildDeviceResponse(verifierRequest: DeviceRequest): DeviceResponse {
        val itemsRequest = verifierRequest.docRequests[0].itemsRequest
        val isoNamespace = itemsRequest.value.namespaces[TaxIdScheme.isoNamespace].shouldNotBeNull()
        val requestedKeys = isoNamespace.entries.filter { it.value }.map { it.key }

        return DeviceResponse(
            version = "1.0",
            documents = arrayOf(
                Document(
                    docType = TaxIdScheme.isoDocType,
                    issuerSigned = IssuerSigned.fromIssuerSignedItems(
                        namespacedItems = mapOf(
                            TaxIdScheme.isoNamespace to storedMdlItems!!.entries.filter {
                                it.value.elementIdentifier in requestedKeys
                            }.map { it.value }
                        ),
                        issuerAuth = storedIssuerAuth!!
                    ),
                    deviceSigned = DeviceSigned(
                        namespaces = ByteStringWrapper(DeviceNameSpaces(mapOf())),
                        deviceAuth = DeviceAuth(
                            deviceSignature = coseService.createSignedCose(
                                protectedHeader = null,
                                payload = byteArrayOf(),
                                serializer = ByteArraySerializer(),
                                addKeyId = false,
                                addCertificate = false,
                            ).getOrThrow()
                        )
                    )
                )
            ),
            status = 0U,
        )
    }

}

class Issuer {

    val cryptoService = DefaultCryptoService(EphemeralKeyWithoutCert())
    val coseService = DefaultCoseService(cryptoService)

    suspend fun buildDeviceResponse(walletKeyInfo: DeviceKeyInfo): DeviceResponse {

        val iss = Instant.fromEpochMilliseconds(Random.nextLong())
        val exp = Instant.fromEpochMilliseconds(iss.toEpochMilliseconds() + 1)

        val taxID = TaxIdCredential(
            taxNumber = "1337",
            affiliationCountry = "Utopia",
            registeredFamilyName = "Mustermann",
            registeredGivenName = "Max",
            residentAddress = "Heckenrosenweg 4, 95867",
            birthDate = LocalDate.parse("2033-01-31"),
            issuanceDate = iss,
            expiryDate = exp,
            issuingAuthority = "Ministry of Truth",
            issuingCountry = "Airstrip One"
        )
       println( vckJsonSerializer.encodeToString(taxID))
        val issuerSigned = listOf(
            buildIssuerSignedItem(TAX_NUMBER, "1337", 0U),
            buildIssuerSignedItem(AFFILIATION_COUNTRY, "Utopia", 1U),
            buildIssuerSignedItem(REGISTERED_FAMILY_NAME, "Mustermann", 2U),
            buildIssuerSignedItem(REGISTERED_GIVEN_NAME, "Max", 3U),
            buildIssuerSignedItem(RESIDENT_ADDRESS, "Heckenrosenweg 4, 95867", 4U),
            buildIssuerSignedItem(BIRTH_DATE, "2033-01-31", 5U),
            buildIssuerSignedItem(ISSUANCE_DATE, "2051-01-08T16:29:04Z", 6U),
            buildIssuerSignedItem(EXPIRY_DATE, "2051-01-08T16:29:05Z", 7U),
            buildIssuerSignedItem(ISSUING_AUTHORITY, "Ministry of Truth", 8U),
            buildIssuerSignedItem(ISSUING_COUNTRY, "Airstrip One", 9U),
        )

        val mso = MobileSecurityObject(
            version = "1.0",
            digestAlgorithm = "SHA-256",
            valueDigests = mapOf(
                TaxIdScheme.isoNamespace to ValueDigestList(entries = issuerSigned.map {
                    ValueDigest.fromIssuerSignedItem(it, TaxIdScheme.isoNamespace)
                })
            ),
            deviceKeyInfo = walletKeyInfo,
            docType = TaxIdScheme.isoDocType,
            validityInfo = ValidityInfo(
                signed = Clock.System.now(),
                validFrom = Clock.System.now(),
                validUntil = Clock.System.now(),
            )
        )

        return DeviceResponse(
            version = "1.0",
            documents = arrayOf(
                Document(
                    docType = TaxIdScheme.isoDocType,
                    issuerSigned = IssuerSigned.fromIssuerSignedItems(
                        namespacedItems = mapOf(
                            TaxIdScheme.isoNamespace to issuerSigned
                        ),
                        issuerAuth = coseService.createSignedCose(
                            payload = mso,
                            serializer = MobileSecurityObject.serializer(),
                            addKeyId = false,
                            addCertificate = true,
                        ).getOrThrow()
                    ),
                    deviceSigned = DeviceSigned(
                        namespaces = ByteStringWrapper(DeviceNameSpaces(mapOf())),
                        deviceAuth = DeviceAuth()
                    )
                )
            ),
            status = 0U,
        )
    }
}

class Verifier {

    val cryptoService = DefaultCryptoService(EphemeralKeyWithoutCert())
    val coseService = DefaultCoseService(cryptoService)
    val verifierCoseService = DefaultVerifierCoseService()

    suspend fun buildDeviceRequest() = DeviceRequest(
        version = "1.0",
        docRequests = arrayOf(
            DocRequest(
                itemsRequest = ByteStringWrapper(
                    value = ItemsRequest(
                        docType = TaxIdScheme.isoDocType,
                        namespaces = mapOf(
                            TaxIdScheme.isoNamespace to ItemsRequestList(
                                listOf(
                                    SingleItemsRequest(TAX_NUMBER, true),
                                    SingleItemsRequest(AFFILIATION_COUNTRY, true),
                                    SingleItemsRequest(REGISTERED_FAMILY_NAME, true),
                                    SingleItemsRequest(REGISTERED_GIVEN_NAME, true),
                                    SingleItemsRequest(RESIDENT_ADDRESS, true),
                                    SingleItemsRequest(BIRTH_DATE, true),
                                    SingleItemsRequest(ISSUANCE_DATE, true),
                                    SingleItemsRequest(EXPIRY_DATE, true),
                                    SingleItemsRequest(ISSUING_AUTHORITY, true),
                                    SingleItemsRequest(ISSUING_COUNTRY, true),
                                )
                            )
                        )
                    )
                ),
                readerAuth = coseService.createSignedCose(
                    unprotectedHeader = CoseHeader(),
                    payload = byteArrayOf(),
                    serializer = ByteArraySerializer(),
                    addKeyId = false,
                ).getOrThrow()
            )
        )
    )

    fun verifyResponse(deviceResponse: DeviceResponse, issuerKey: CoseKey) {
        val documents = deviceResponse.documents.shouldNotBeNull()
        val doc = documents.first()
        doc.docType shouldBe TaxIdScheme.isoDocType
        doc.errors.shouldBeNull()
        val issuerSigned = doc.issuerSigned
        val issuerAuth = issuerSigned.issuerAuth
        verifierCoseService.verifyCose(issuerAuth, issuerKey).isSuccess shouldBe true
        issuerAuth.payload.shouldNotBeNull()
        val mso = issuerSigned.issuerAuth.payload!!

        mso.docType shouldBe TaxIdScheme.isoDocType
        val mdlItems = mso.valueDigests[TaxIdScheme.isoNamespace].shouldNotBeNull()

        val walletKey = mso.deviceKeyInfo.deviceKey
        val deviceSignature = doc.deviceSigned.deviceAuth.deviceSignature.shouldNotBeNull()
        verifierCoseService.verifyCose(deviceSignature, walletKey).isSuccess shouldBe true
        val namespaces = issuerSigned.namespaces.shouldNotBeNull()
        val issuerSignedItems = namespaces[TaxIdScheme.isoNamespace].shouldNotBeNull()

        extractAndVerifyData(issuerSignedItems, mdlItems, TAX_NUMBER)
        extractAndVerifyData(issuerSignedItems, mdlItems, ISSUING_COUNTRY)
    }

    private fun extractAndVerifyData(
        issuerSignedItems: IssuerSignedList,
        mdlItems: ValueDigestList,
        key: String
    ) {
        val issuerSignedItem = issuerSignedItems.entries.first { it.value.elementIdentifier == key }
        val issuerHash = mdlItems.entries.first { it.key == issuerSignedItem.value.digestId }.shouldNotBeNull()
        val verifierHash = issuerSignedItem.serialized.sha256()
        verifierHash.encodeToString(Base16(strict = true)) shouldBe issuerHash.value.encodeToString(Base16(strict = true))
    }
}

private fun extractDataString(
    mdlItems: IssuerSignedList,
    key: String
): String {
    val element = mdlItems.entries.first { it.value.elementIdentifier == key }
    return element.value.elementValue.toString().shouldNotBeNull()
}


fun buildIssuerSignedItem(elementIdentifier: String, elementValue: Any, digestId: UInt) = IssuerSignedItem(
    digestId = digestId,
    random = Random.nextBytes(16),
    elementIdentifier = elementIdentifier,
    elementValue = elementValue
)
