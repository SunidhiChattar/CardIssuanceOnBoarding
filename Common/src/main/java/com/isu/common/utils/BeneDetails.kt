package com.isu.common.utils

import java.util.Random

data class BeneDetails(
    val name: String = "TestCustomer" + Random().nextInt().toString(),
    val IFSCCode: String = "TEST" + Random().nextInt().toString(),
    val bankName: String = "TestBank" + Random().nextInt().toString(),
    val bankAccNumber: String = Random().nextInt().toString(),
)