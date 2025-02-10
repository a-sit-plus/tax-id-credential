package at.asitplus.wallet.taxid

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.InstantComponentSerializer
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class TaxIdCredential(
    @SerialName(TaxIdScheme.Attributes.TAX_NUMBER)
    val taxNumber: String,

    @SerialName(TaxIdScheme.Attributes.AFFILIATION_COUNTRY)
    val affiliationCountry: String,

    @SerialName(TaxIdScheme.Attributes.REGISTERED_FAMILY_NAME)
    val registeredFamilyName: String,

    @SerialName(TaxIdScheme.Attributes.REGISTERED_GIVEN_NAME)
    val registeredGivenName: String,

    @SerialName(TaxIdScheme.Attributes.RESIDENT_ADDRESS)
    val residentAddress: String,

    @Serializable(with = LocalDateIso8601Serializer::class)
    @SerialName(TaxIdScheme.Attributes.BIRTH_DATE)
    val birthDate: LocalDate,

    @SerialName(TaxIdScheme.Attributes.CHURCH_TAX_ID)
    val churchTaxId: String? = null,

    @SerialName(TaxIdScheme.Attributes.IBAN)
    val iban: String? = null,

    @SerialName(TaxIdScheme.Attributes.PID_ID)
    val pidId: String? = null,

    @Serializable(with = InstantFractionlessSerializer::class)
    @SerialName(TaxIdScheme.Attributes.ISSUANCE_DATE)
    val issuanceDate: Instant,

    @SerialName(TaxIdScheme.Attributes.VERIFICATION_STATUS)
    val verificationStatus: String? = null,

    @Serializable(with = InstantFractionlessSerializer::class)
    @SerialName(TaxIdScheme.Attributes.EXPIRY_DATE)
    val expiryDate: Instant,

    @SerialName(TaxIdScheme.Attributes.ISSUING_AUTHORITY)
    val issuingAuthority: String,

    @SerialName(TaxIdScheme.Attributes.DOCUMENT_NUMBER)
    val documentNumber: String? = null,

    @SerialName(TaxIdScheme.Attributes.ADMINISTRATIVE_NUMBER)
    val administrativeNumber: String? = null,

    @SerialName(TaxIdScheme.Attributes.ISSUING_COUNTRY)
    val issuingCountry: String,

    @SerialName(TaxIdScheme.Attributes.ISSUING_JURISDICTION)
    val issuingJurisdiction: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TaxIdCredential) return false

        if (taxNumber != other.taxNumber) return false
        if (affiliationCountry != other.affiliationCountry) return false
        if (registeredFamilyName != other.registeredFamilyName) return false
        if (registeredGivenName != other.registeredGivenName) return false
        if (residentAddress != other.residentAddress) return false
        if (birthDate != other.birthDate) return false
        if (churchTaxId != other.churchTaxId) return false
        if (iban != other.iban) return false
        if (pidId != other.pidId) return false
        if (issuanceDate != other.issuanceDate) return false
        if (verificationStatus != other.verificationStatus) return false
        if (expiryDate != other.expiryDate) return false
        if (issuingAuthority != other.issuingAuthority) return false
        if (documentNumber != other.documentNumber) return false
        if (administrativeNumber != other.administrativeNumber) return false
        if (issuingCountry != other.issuingCountry) return false
        if (issuingJurisdiction != other.issuingJurisdiction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = taxNumber.hashCode()
        result = 31 * result + affiliationCountry.hashCode()
        result = 31 * result + registeredFamilyName.hashCode()
        result = 31 * result + registeredGivenName.hashCode()
        result = 31 * result + residentAddress.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + (churchTaxId?.hashCode() ?: 0)
        result = 31 * result + (iban?.hashCode() ?: 0)
        result = 31 * result + (pidId?.hashCode() ?: 0)
        result = 31 * result + issuanceDate.hashCode()
        result = 31 * result + (verificationStatus?.hashCode() ?: 0)
        result = 31 * result + expiryDate.hashCode()
        result = 31 * result + issuingAuthority.hashCode()
        result = 31 * result + (documentNumber?.hashCode() ?: 0)
        result = 31 * result + (administrativeNumber?.hashCode() ?: 0)
        result = 31 * result + issuingCountry.hashCode()
        result = 31 * result + (issuingJurisdiction?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "TaxIdCredential(" +
                "taxNumber='$taxNumber', " +
                "affiliationCountry='$affiliationCountry', " +
                "registeredFamilyName='$registeredFamilyName', " +
                "registeredGivenName='$registeredGivenName', " +
                "residentAddress='$residentAddress', " +
                "birthDate=$birthDate, " +
                "churchTaxId=$churchTaxId, " +
                "iban=$iban, " +
                "pidId=$pidId, " +
                "issuanceDate=$issuanceDate, " +
                "verificationStatus=$verificationStatus, " +
                "expiryDate=$expiryDate, " +
                "issuingAuthority='$issuingAuthority', " +
                "documentNumber=$documentNumber, " +
                "administrativeNumber=$administrativeNumber, " +
                "issuingCountry='$issuingCountry', " +
                "issuingJurisdiction=$issuingJurisdiction" +
                ")"
    }
}

private object InstantFractionlessSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("InstantWithoutFractions", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString().let { it.substring(0, it.lastIndexOf('.')) + "Z" })
    }

    override fun deserialize(decoder: Decoder)=Instant.parse(decoder.decodeString())
}
