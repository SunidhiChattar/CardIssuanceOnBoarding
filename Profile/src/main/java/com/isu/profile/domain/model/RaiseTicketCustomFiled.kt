package com.isu.profile.domain.model

sealed class RaiseTicketCustomFiled(val string: String = "") {
    data class TransactionRelated(
        val transactionRelatedString: String = "transaction_related",
        val id: Long = 31812447425945,
        val value: String = "transactional_related",
    ) : RaiseTicketCustomFiled("transaction_related")

    data class CardActivation(
        val cardActivationString: String = "card_activation",
        val id: Long = 31812447425945,
        val value: String = "card_activation/loading_related",
    ) : RaiseTicketCustomFiled("card_activation")

    data class CardPinNotWorking(
        val cardActivationString: String = "card_pin_not_working",
        val id: Long = 31812447425945,
        val value: String = "card_pin_not_working",
    ) : RaiseTicketCustomFiled("card_pin_not_working")

    data class CardPinNotWorkingInATM(
        val cardActivationString: String = "card_pin_not_working_atm",
        val id: Long = 31812447425945,
        val value: String = "card_not_working_in_atm_related_inquery",
    ) : RaiseTicketCustomFiled("card_pin_not_working_atm")

    data class GPRCardDispatchInquiry(
        val cardActivationString: String = "gpr_card_dispatch_inquiry",
        val id: Long = 31812447425945,
        val value: String = "gpr_card_dispatch_inquiry",
    ) : RaiseTicketCustomFiled("gpr_card_dispatch_inquiry")

}