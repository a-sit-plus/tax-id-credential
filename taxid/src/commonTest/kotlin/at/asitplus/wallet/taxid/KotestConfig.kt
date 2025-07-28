package io.kotest.provided

import at.asitplus.test.XmlReportingProjectConfig
import at.asitplus.wallet.taxid.Initializer
import at.asitplus.wallet.taxid.Initializer2025


/** Wires KMP JUnit XML reporting */
class ProjectConfig : XmlReportingProjectConfig() {
    init {
        Initializer.initWithVCK()
        Initializer2025.initWithVCK()
    }
}