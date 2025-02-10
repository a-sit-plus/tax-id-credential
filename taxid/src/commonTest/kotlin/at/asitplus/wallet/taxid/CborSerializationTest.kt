package at.asitplus.wallet.taxid

import at.asitplus.wallet.lib.iso.IssuerSignedList
import at.asitplus.wallet.lib.iso.ItemsRequestList
import at.asitplus.wallet.lib.iso.ValueDigestList

private fun ItemsRequestList.findItem(key: String) =
    entries.first { it.key == key }.value

private fun ValueDigestList.findItem(digestId: UInt) =
    entries.first { it.key == digestId }.value

private fun IssuerSignedList.findItem(digestId: UInt) =
    entries.first { it.value.digestId == digestId }.value
