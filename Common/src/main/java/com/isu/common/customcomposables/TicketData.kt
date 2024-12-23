package com.isu.common.customcomposables

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class TicketData(
    val ticketId: String = Random.nextInt(100, 10000).toString(),
    val requestTitle: String,
    val category: String,
    val status: TicketStatus,
    val priority: TicketPriority,
    val description: String,
    val date: String,
)

sealed class TicketStatus(
    val status: String,
    val color: Color,
    val textColor:Color,
)
{
    data class InProgress(
        val inProgressStatus: String = "In Progress",
        val inProgressColor: Color = Color(0xffFEF8EA),
        val inProgressTextColor:Color=Color(0xffF6BC2F)
    ) :
        TicketStatus(status = inProgressStatus, inProgressColor, textColor = inProgressTextColor)

    data class Open(val openStatus: String="Open", val openColor: Color = Color(0xffE7F3EC),val openTextColor:Color=Color(0xff0E8345)) :
        TicketStatus(openStatus, openColor, textColor = openTextColor)

    data class Escalated(val escalatedStatus: String="Escalated", val escalatedColor: Color = Color(0xffFFE4EE),val escalatedTextColor:Color= Color(0xffF561EF)) :
        TicketStatus(escalatedStatus, escalatedColor, textColor = escalatedTextColor)
    data class Closed(val closedStatus: String="Closed", val closedColor: Color = Color(0xffEEF0F1),val closedTextColor: Color=Color(0xff546881)) :
        TicketStatus(closedStatus, closedColor,closedTextColor)
}