package com.isu.common.customcomposables


import androidx.compose.ui.graphics.Color

sealed class TicketPriority(
    val status: String,
    val color: Color,
    val textColor:Color,
)
{
    data class Low(val inProgressStatus: String="Low", val inProgressColor: Color = Color(0xffE7F3EC), val lowTextColor:Color=Color(0xff0E8345)) :
        TicketPriority(status = inProgressStatus, inProgressColor,lowTextColor)

    data class High(val openStatus: String="High", val highPriorityColor: Color=Color.Red.copy(0.1f), val highTextColor: Color=Color.Red) :
        TicketPriority(openStatus, highPriorityColor,highTextColor)

    data class Medium(val escalatedStatus: String="Medium", val escalatedColor: Color= Color(0xffFEF8EA), val mediumTextColor:Color=Color(0xffF6BC2F)) :
        TicketPriority(escalatedStatus, escalatedColor,mediumTextColor)

}