package com.isu.common.events

import android.util.Log
import com.isu.common.customcomposables.ShippingAddressItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object LogOutEvent{
    private val _logOutEvent= MutableSharedFlow<Any>()
    val logOutEvent=_logOutEvent.asSharedFlow()
    suspend fun logOut(){
        _logOutEvent.emit(Any())
    }

}
object EditAddressEvent {
    private val _editAddressEvent = MutableSharedFlow<ShippingAddressItem>()
    val editAddressEvent = _editAddressEvent.asSharedFlow()
    suspend fun editAddress(item: ShippingAddressItem) {
        Log.d("EDIT_EMIT", "editAddress:${item} ")
        _editAddressEvent.emit(item)
    }

}

object DeleteAddressEvent {
    private val _deleteAddressEvent = MutableSharedFlow<ShippingAddressItem>()
    val deleteAddressEvent = _deleteAddressEvent.asSharedFlow()
    suspend fun deleteAddress(item: ShippingAddressItem) {
        Log.d("DELETE_EMIT", "editAddress:${item} ")
        _deleteAddressEvent.emit(item)
    }

}

object ShowNewAddedCard {
    private val _showNewAddedCard = MutableSharedFlow<Boolean>()
    val showNewAddedCard = _showNewAddedCard.asSharedFlow()
    suspend fun show() {
        _showNewAddedCard.emit(true)
    }

    suspend fun hide() {
        _showNewAddedCard.emit(false)
    }

}
object ShowBottomBarEvent {
    private val _showBottomBarEvent = MutableSharedFlow<Boolean>()
    val showBottomBarEvent = _showBottomBarEvent.asSharedFlow()
    suspend fun show() {
        try {
            _showBottomBarEvent.emit(true)
        } catch (e: Exception) {
            Log.d("ERROR", "show:${e.message} ")
        }

    }

    suspend fun hide() {
        _showBottomBarEvent.emit(false)
    }

}

object ShowAddCard {
    private val _showAddCardModal = MutableSharedFlow<Boolean>()
    val showAddCardModal = _showAddCardModal.asSharedFlow()

    suspend fun show() {
        _showAddCardModal.emit(true)
    }

    suspend fun hide() {
        _showAddCardModal.emit(false)
    }

}
object FetchAllCards {
    private val _fetctAllCards = MutableSharedFlow<Any>()
    val fetctAllCards = _fetctAllCards.asSharedFlow()

    suspend fun fetch() {
        _fetctAllCards.emit(true)
    }


}

object CommingSoonEvent {
    private val _commingSoonEvent = MutableSharedFlow<Any>()
    val commingSoonEvent = _commingSoonEvent.asSharedFlow()
    suspend fun showCommingSoon() {
        _commingSoonEvent.emit(Any())
    }

}
