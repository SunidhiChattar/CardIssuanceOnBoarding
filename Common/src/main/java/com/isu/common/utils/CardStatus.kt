package com.isu.common.utils

sealed class CardStatus(val value: String) {
    object REISSUANCE : CardStatus("REISSUANCE")
    object DEACTIVATE : CardStatus("DEACTIVATE")
    object TEMP_BLOCK : CardStatus("TEMPBLOCK")
    object LOST : CardStatus("LOST")
    object STOLEN : CardStatus("STOLEN")
    object DAMAGED : CardStatus("DAMAGE")
    object ACTIVATE : CardStatus("ACTIVATE")
}