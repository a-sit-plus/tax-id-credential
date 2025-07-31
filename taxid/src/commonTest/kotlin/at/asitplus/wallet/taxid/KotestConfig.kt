package io.kotest.provided

import at.asitplus.test.XmlReportingProjectConfig
import at.asitplus.wallet.taxid.Initializer


/** Wires KMP JUnit XML reporting */
class ProjectConfig : XmlReportingProjectConfig() {
    init {
        Initializer.initWithVCK()
    }
}