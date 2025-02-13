package at.asitplus.wallet.taxid

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class TaxIdCredential private constructor(


    /**
     * Date and time when the Tax ID
     * attestation was issued
     */
    @Serializable(with = InstantIso8601Serializer::class)
    @SerialName(TaxIdScheme.Attributes.ISSUANCE_DATE)
    val issuanceDate: Instant,

    /**
     * Character string that enables the
     * tax authorities to assign the user
     * correctly (national format)
     */
    @SerialName(TaxIdScheme.Attributes.TAX_NUMBER)
    val taxNumber: String,

    /**
     * Alpha-2 country code, as defined
     * in ISO 3166-1, of the user’s
     * country of tax residence
     */
    @SerialName(TaxIdScheme.Attributes.AFFILIATION_COUNTRY)
    val affiliationCountry: String,

    /**
     * Current last name(s) or
     * surname(s)
     */
    @SerialName(TaxIdScheme.Attributes.REGISTERED_FAMILY_NAME)
    val registeredFamilyName: String,

    /**
     * Current first name(s) including
     * middle name(s)
     */
    @SerialName(TaxIdScheme.Attributes.REGISTERED_GIVEN_NAME)
    val registeredGivenName: String,

    /**
     * The full address of the place
     * where the user currently resides
     * (street name, house number, city
     * etc.)
     */
    @SerialName(TaxIdScheme.Attributes.RESIDENT_ADDRESS)
    val residentAddress: String,

    /**
     * Day, month, and year on which
     * the user was born (25-08-1988)
     */
    @Serializable(with = LocalDateIso8601Serializer::class)
    @SerialName(TaxIdScheme.Attributes.BIRTH_DATE)
    val birthDate: LocalDate,

    /**
     * Character string made up of
     * numbers and letters
     */
    @SerialName(TaxIdScheme.Attributes.CHURCH_TAX_ID)
    val churchTaxId: String? = null,

    /**
     * IBAN registered with the tax
     * authorities
     */
    @SerialName(TaxIdScheme.Attributes.IBAN)
    val iban: String? = null,

    /**
     * Unique identifier for the PID
     */
    @SerialName(TaxIdScheme.Attributes.PID_ID)
    val pidId: String? = null,


    /**
     * Information if the Tax ID
     * attestation is verified or revoked
     * (or other status)
     */
    @SerialName(TaxIdScheme.Attributes.VERIFICATION_STATUS)
    val verificationStatus: String? = null,

    /**
     * Date and time when the Tax ID
     * attestation will expire.
     */
    @Serializable(with = InstantIso8601Serializer::class)
    @SerialName(TaxIdScheme.Attributes.EXPIRY_DATE)
    val expiryDate: Instant,

    /**
     * Name of the administrative
     * authority that has issued this Tax
     * ID attestation, or the ISO 3166
     * Alpha-2 country code of the
     * respective Member State if there
     * is no separate authority
     * authorized to issue Tax ID
     * attestations.
     */
    @SerialName(TaxIdScheme.Attributes.ISSUING_AUTHORITY)
    val issuingAuthority: String,

    /**
     * A number for the Tax ID
     * attestation, assigned by the
     * Provider.
     */
    @SerialName(TaxIdScheme.Attributes.DOCUMENT_NUMBER)
    val documentNumber: String? = null,

    /**
     * A number assigned by the Tax ID
     * attestation Provider for audit
     * control or other purposes.
     */
    @SerialName(TaxIdScheme.Attributes.ADMINISTRATIVE_NUMBER)
    val administrativeNumber: String? = null,

    /**
     * Alpha-2 country code, as defined
     * in ISO 3166-1, of the Tax ID
     * attestation Provider’s country or
     * territory.
     */
    @SerialName(TaxIdScheme.Attributes.ISSUING_COUNTRY)
    val issuingCountry: String,

    /**
     * Country subdivision code of the
     * jurisdiction that issued the Tax ID
     * attestation, as defined in ISO
     * 3166-2:2020, Clause 8. The first
     * part of the code SHALL be the
     * same as the value for
     * issuing_country.
     */
    @SerialName(TaxIdScheme.Attributes.ISSUING_JURISDICTION)
    val issuingJurisdiction: String? = null,
) {


    constructor(
        /**
         * Character string that enables the
         * tax authorities to assign the user
         * correctly (national format)
         */
        taxNumber: String,

        /**
         * Alpha-2 country code, as defined
         * in ISO 3166-1, of the user’s
         * country of tax residence
         */
        affiliationCountry: String,

        /**
         * Current last name(s) or
         * surname(s)
         */
        registeredFamilyName: String,

        /**
         * Current first name(s) including
         * middle name(s)
         */
        registeredGivenName: String,

        /**
         * The full address of the place
         * where the user currently resides
         * (street name, house number, city
         * etc.)
         */
        residentAddress: String,

        /**
         * Day, month, and year on which
         * the user was born (25-08-1988)
         */
        birthDate: LocalDate,

        /**
         * Character string made up of
         * numbers and letters
         */
        churchTaxId: String? = null,

        /**
         * IBAN registered with the tax
         * authorities
         */
        iban: String? = null,

        /**
         * Unique identifier for the PID
         */
        pidId: String? = null,

        /**
         * Date and time when the Tax ID
         * attestation was issued
         */
        issuanceDate: Instant,

        /**
         * Information if the Tax ID
         * attestation is verified or revoked
         * (or other status)
         */
        verificationStatus: String? = null,

        /**
         * Date and time when the Tax ID
         * attestation will expire.
         */
        expiryDate: Instant,

        /**
         * Name of the administrative
         * authority that has issued this Tax
         * ID attestation, or the ISO 3166
         * Alpha-2 country code of the
         * respective Member State if there
         * is no separate authority
         * authorized to issue Tax ID
         * attestations.
         */
        issuingAuthority: String,

        /**
         * A number for the Tax ID
         * attestation, assigned by the
         * Provider.
         */
        documentNumber: String? = null,

        /**
         * A number assigned by the Tax ID
         * attestation Provider for audit
         * control or other purposes.
         */
        administrativeNumber: String? = null,

        /**
         * Alpha-2 country code, as defined
         * in ISO 3166-1, of the Tax ID
         * attestation Provider’s country or
         * territory.
         */
        issuingCountry: String,

        /**
         * Country subdivision code of the
         * jurisdiction that issued the Tax ID
         * attestation, as defined in ISO
         * 3166-2:2020, Clause 8. The first
         * part of the code SHALL be the
         * same as the value for
         * issuing_country.
         */
        issuingJurisdiction: String? = null,
    ) : this(
        Instant.fromEpochSeconds(issuanceDate.epochSeconds),
        taxNumber = taxNumber,
        affiliationCountry = affiliationCountry,
        registeredFamilyName = registeredFamilyName,
        registeredGivenName = registeredGivenName,
        residentAddress = residentAddress,
        birthDate = birthDate,
        churchTaxId = churchTaxId,
        iban = iban,
        pidId = pidId,
        verificationStatus = verificationStatus,
        expiryDate = Instant.fromEpochSeconds(expiryDate.epochSeconds),
        issuingAuthority = issuingAuthority,
        documentNumber = documentNumber,
        administrativeNumber = administrativeNumber,
        issuingCountry = issuingCountry,
        issuingJurisdiction = issuingJurisdiction,
        )

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
