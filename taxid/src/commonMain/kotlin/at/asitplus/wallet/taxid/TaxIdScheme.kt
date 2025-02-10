package at.asitplus.wallet.taxid

import at.asitplus.wallet.lib.data.ConstantIndex

object TaxIdScheme : ConstantIndex.CredentialScheme {
    override val schemaUri = "https://wallet.a-sit.at/schemas/1.0.0/taxid.json"
    override val sdJwtType = "Tax Number" //Yes, it is like that in the rulebook
    override val isoNamespace = "eu.europa.finance.taxid.1" //undefined in rulebook
    override val isoDocType = "Tax Number" //see rulebook

    object Attributes {
        const val TAX_NUMBER = "tax_number"
        const val AFFILIATION_COUNTRY = "affiliation_country"
        const val REGISTERED_FAMILY_NAME = "registered_family_name"
        const val REGISTERED_GIVEN_NAME = "registered_given_name"
        const val RESIDENT_ADDRESS = "resident_address"
        const val BIRTH_DATE = "birth_date"
        const val CHURCH_TAX_ID = "church_tax_ID"
        const val IBAN = "iban"
        const val PID_ID = "pid_id"
        const val ISSUANCE_DATE = "issuance_date"
        const val VERIFICATION_STATUS = "verification_status"
        const val EXPIRY_DATE = "expiry_date"
        const val ISSUING_AUTHORITY = "issuing_authority"
        const val DOCUMENT_NUMBER = "document_number"
        const val ADMINISTRATIVE_NUMBER = "administrative_number"
        const val ISSUING_COUNTRY = "issuing_country"
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