package at.asitplus.wallet.taxid

import at.asitplus.wallet.lib.data.ConstantIndex

object TaxIdScheme : ConstantIndex.CredentialScheme {
    override val schemaUri = "https://wallet.a-sit.at/schemas/1.0.0/taxid.json"
    override val sdJwtType = "Tax Number" //Yes, it is like that in the rulebook
    override val supportedRepresentations: Collection<ConstantIndex.CredentialRepresentation> = listOf(ConstantIndex.CredentialRepresentation.SD_JWT)

    object Attributes {
        /**
         * Character string that enables the
         * tax authorities to assign the user
         * correctly (national format)
         */
        const val TAX_NUMBER = "tax_number"

        /**
         * Alpha-2 country code, as defined
         * in ISO 3166-1, of the user’s
         * country of tax residence
         */
        const val AFFILIATION_COUNTRY = "affiliation_country"

        /**
         * Current last name(s) or
         * surname(s)
         */
        const val REGISTERED_FAMILY_NAME = "registered_family_name"

        /**
         * Current first name(s) including
         * middle name(s)
         */
        const val REGISTERED_GIVEN_NAME = "registered_given_name"

        /**
         * The full address of the place
         * where the user currently resides
         * (street name, house number, city
         * etc.)
         */
        const val RESIDENT_ADDRESS = "resident_address"

        /**
         * Day, month, and year on which
         * the user was born (25-08-1988)
         */
        const val BIRTH_DATE = "birth_date"

        /**
         * Character string made up of
         * numbers and letters
         */
        const val CHURCH_TAX_ID = "church_tax_ID"

        /**
         * IBAN registered with the tax
         * authorities
         */
        const val IBAN = "iban"

        /**
         * Unique identifier for the PID
         */
        const val PID_ID = "pid_id"

        /**
         * Date and time when the Tax ID
         * attestation was issued
         */
        const val ISSUANCE_DATE = "issuance_date"

        /**
         * Information if the Tax ID
         * attestation is verified or revoked
         * (or other status)
         */
        const val VERIFICATION_STATUS = "verification_status"

        /**
         * Date and time when the Tax ID
         * attestation will expire.
         */
        const val EXPIRY_DATE = "expiry_date"

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
        const val ISSUING_AUTHORITY = "issuing_authority"

        /**
         * A number for the Tax ID
         * attestation, assigned by the
         * Provider.
         */
        const val DOCUMENT_NUMBER = "document_number"

        /**
         * A number assigned by the Tax ID
         * attestation Provider for audit
         * control or other purposes.
         */
        const val ADMINISTRATIVE_NUMBER = "administrative_number"

        /**
         * Alpha-2 country code, as defined
         * in ISO 3166-1, of the Tax ID
         * attestation Provider’s country or
         * territory.
         */
        const val ISSUING_COUNTRY = "issuing_country"

        /**
         * Country subdivision code of the
         * jurisdiction that issued the Tax ID
         * attestation, as defined in ISO
         * 3166-2:2020, Clause 8. The first
         * part of the code SHALL be the
         * same as the value for
         * issuing_country.
         */
        const val ISSUING_JURISDICTION = "issuing_jurisdiction"
    }

    val requiredClaims: Collection<String> = setOf(
        Attributes.TAX_NUMBER,
        Attributes.AFFILIATION_COUNTRY,
        Attributes.REGISTERED_FAMILY_NAME,
        Attributes.REGISTERED_GIVEN_NAME,
        Attributes.RESIDENT_ADDRESS,
        Attributes.BIRTH_DATE,
        Attributes.ISSUANCE_DATE,
        Attributes.EXPIRY_DATE,
        Attributes.ISSUING_AUTHORITY,
        Attributes.ISSUING_COUNTRY,
    )
}