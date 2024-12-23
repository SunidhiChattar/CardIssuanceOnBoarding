package com.isu.prepaidcard.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import com.isu.common.customcomposables.CustomComposable
import com.isu.common.customcomposables.NotificationScreen
import com.isu.common.customcomposables.loadcard.LoadCard
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.ProfileScreen
import com.isu.prepaidcard.presentation.screens.PDFViewScreen
import com.isu.prepaidcard.presentation.screens.dashboard.CardManagement
import com.isu.prepaidcard.presentation.screens.dashboard.DashboardHomeScreen
import com.isu.prepaidcard.presentation.screens.dashboard.MccCategories
import com.isu.prepaidcard.presentation.screens.dashboard.MccCodeCategories
import com.isu.prepaidcard.presentation.screens.dashboard.addoncard.AddOnCard
import com.isu.prepaidcard.presentation.screens.dashboard.addoncard.AddOnCardCreated
import com.isu.prepaidcard.presentation.screens.dashboard.addoncard.AddOnCardName
import com.isu.prepaidcard.presentation.screens.dashboard.addoncard.AddOnCardPhoneVerification
import com.isu.prepaidcard.presentation.screens.dashboard.addoncard.AddOnCardViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.addoncard.AddOnSetPin
import com.isu.prepaidcard.presentation.screens.dashboard.addoncard.AddOnSteps
import com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment.AddBeneficiaryScreen
import com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment.BeneAmountScreen
import com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment.BeneficiaryListScreen
import com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment.PaymentModeSelectionScreen
import com.isu.prepaidcard.presentation.screens.dashboard.cardstatus.CardStatus
import com.isu.prepaidcard.presentation.screens.dashboard.completekycscreen.CompleteKycScreen
import com.isu.prepaidcard.presentation.screens.dashboard.kitToKitTransfer.KitToKitScreen
import com.isu.prepaidcard.presentation.screens.dashboard.kitToKitTransfer.KitToKitViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.linkcard.LinkCard
import com.isu.prepaidcard.presentation.screens.dashboard.linkcard.LinkCardOtpScreen
import com.isu.prepaidcard.presentation.screens.dashboard.linkcard.LinkCardViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.LoadCardResultScreen
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.LoadCardState
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.LoadCardViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.mandate.MandateHistory
import com.isu.prepaidcard.presentation.screens.dashboard.mandate.Mandates
import com.isu.prepaidcard.presentation.screens.dashboard.orderhistory.OrderHistory
import com.isu.prepaidcard.presentation.screens.dashboard.orderhistory.OrderHistoryDetails
import com.isu.prepaidcard.presentation.screens.dashboard.orderhistory.OrderHistoryViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.orderphysicalcard.OrderCardSelectionScreen
import com.isu.prepaidcard.presentation.screens.dashboard.orderphysicalcard.OrderCardShippingAddressScreen
import com.isu.prepaidcard.presentation.screens.dashboard.orderphysicalcard.OrderCardShippingDetailsScreen
import com.isu.prepaidcard.presentation.screens.dashboard.orderphysicalcard.OrderPhysicalCardViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.reissuance.AddNewAddress
import com.isu.prepaidcard.presentation.screens.dashboard.reissuance.CardReIssuanceViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.reissuance.CardReissuance
import com.isu.prepaidcard.presentation.screens.dashboard.reissuance.EditAddressScreen
import com.isu.prepaidcard.presentation.screens.dashboard.reserpin.ModifyPin
import com.isu.prepaidcard.presentation.screens.dashboard.reserpin.ModifyPinOtpScreen
import com.isu.prepaidcard.presentation.screens.dashboard.reserpin.ModifyPinViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.statement.DetailEachStatement
import com.isu.prepaidcard.presentation.screens.dashboard.statement.DetailedStatement
import com.isu.prepaidcard.presentation.screens.dashboard.statement.Statement
import com.isu.prepaidcard.presentation.screens.dashboard.tokens.Tokens
import com.isu.prepaidcard.presentation.screens.dashboard.transactionSettings.TransactionSettingsScreen
import com.isu.prepaidcard.presentation.screens.dashboard.transactionSettings.TransactionViewModel
import com.isu.prepaidcard.presentation.viewmodels.CardStatusViewModel
import com.isu.prepaidcard.presentation.viewmodels.DashboardViewModel
import com.isu.prepaidcard.presentation.viewmodels.MandateViewModel
import com.isu.prepaidcard.presentation.viewmodels.StatementViewModel

fun NavGraphBuilder.prepaidCardNavGraph(
    dashboardViewModel: DashboardViewModel,
    mandateViewModel: MandateViewModel,
    statementViewModel: StatementViewModel,
    cardStatusViewModel: CardStatusViewModel,
    addOnCardViewModel: AddOnCardViewModel,
    loadCardViewModel: LoadCardViewModel,
    modifyPinViewModel: ModifyPinViewModel,
    linkCardViewModel: LinkCardViewModel,
    loadCardStatw: LoadCardState,
    cardReissuance: CardReIssuanceViewModel,
    orderPhysicalCardViewModel: OrderPhysicalCardViewModel,
    kitViewModel: KitToKitViewModel,
    transactionViewModel: TransactionViewModel,
    orderHistoryViewModel: OrderHistoryViewModel
) {
    CustomComposable<ProfileScreen.DashBoardScreen> {
        DashboardHomeScreen(dashboardViewModel =dashboardViewModel )
    }
    CustomComposable<ProfileScreen.NotificationScreen> {
        NotificationScreen()
    }
    CustomComposable<ProfileScreen.CardManagementScreen> {
        CardManagement(dashboardViewModel = dashboardViewModel)
    }
    CustomComposable<CardManagement.ModifyPinScreen> {
        ModifyPin(
            state = modifyPinViewModel.modifyPinState.collectAsState().value,
            onEvent = { modifyPinViewModel.onEvent(it) }

        )
    }
    CustomComposable<CardManagement.Mandate> {
        Mandates(mandateViewModel)
    }
    CustomComposable<CardManagement.LinkCard> {
        LinkCard(linkCardViewModel = linkCardViewModel)
    }
    CustomComposable<CardManagement.LinkCardOtpScreen> {
        LinkCardOtpScreen(linkCardViewModel = linkCardViewModel)
    }
    CustomComposable<CardManagement.EditAddressScreen> {
        EditAddressScreen(viewModel = cardReissuance)
    }
    CustomComposable<CardManagement.CardReissuance> {
        CardReissuance(cardReissuanceViewModel = cardReissuance)
    }
    CustomComposable<CardManagement.AddAddress> {
        AddNewAddress(viewModel = cardReissuance)
    }
    CustomComposable<CardManagement.CompleteKYC> {
        CompleteKycScreen()
    }
    CustomComposable<CardManagement.MandateHistory> {
        MandateHistory(mandateViewModel)
    }
    CustomComposable<CardManagement.Statement> {
        Statement(statementViewModel)
    }
    CustomComposable<CardManagement.DetailedStatementList> {
        DetailEachStatement(statementViewModel)
    }
    CustomComposable<CardManagement.DetailedStatement> {
        DetailedStatement(statementViewModel)
    }
    CustomComposable<CardManagement.Tokens> {
        Tokens()
    }
    CustomComposable<CardManagement.CardStatus> {
        CardStatus(
            cardStatusViewModel = cardStatusViewModel
        )
    }
    CustomComposable<CardManagement.CardStatus> {
        CardStatus(
            cardStatusViewModel = cardStatusViewModel
        )
    }
    CustomComposable<CardManagement.LoadCardScreen> {

        LoadCard(
            state = loadCardStatw,
            onEvent = loadCardViewModel::onEvent,
            viewModel = loadCardViewModel
        )
    }
    CustomComposable<CardManagement.MCCCategoryScreen> {

        MccCategories(viewModel = dashboardViewModel)
    }
    CustomComposable<CardManagement.MCCCategoryCodeScreen> {

        MccCodeCategories(viewModel = dashboardViewModel)
    }
    CustomComposable<CardManagement.AddOnCarSelfd> {
        AddOnCardName(
            addOnCardViewModel = addOnCardViewModel,
            dashboardViewModel = dashboardViewModel
        )
    }
    CustomComposable<CardManagement.AddOnCardSomeoneElse> {
        AddOnCard(addOnCardViewModel)
    }
    CustomComposable<CardManagement.AddonCardForSomeoneElseSuccess> {
        AddOnSteps()
    }
    CustomComposable<CardManagement.VIEW_PDF_SCREEN> {
        PDFViewScreen(statementViewModel)
    }
    CustomComposable<CardManagement.AddOnCardPhoneVerificationSCreen> {
        AddOnCardPhoneVerification(addOnCardViewModel = addOnCardViewModel)
    }
    CustomComposable<CardManagement.AddonCardSelfSuccess> {
        AddOnCardCreated()
    }
    CustomComposable<CardManagement.SetPinScreen> {
        AddOnSetPin(viewMdel = addOnCardViewModel)
    }
    CustomComposable<CardManagement.LoadCardResultScreen> {

        LoadCardResultScreen(state = loadCardStatw, onEvent = loadCardViewModel::onEvent)
    }
    CustomComposable<CardManagement.OrderPhyicalCardSelectScreen> {

        OrderCardSelectionScreen(viewModel = orderPhysicalCardViewModel)
    }
    CustomComposable<CardManagement.OrderPhyicalCardDetailsScreen> {

        OrderCardShippingDetailsScreen(viewModel = orderPhysicalCardViewModel)
    }
    CustomComposable<CardManagement.OrderPhyicalCardShippingAddressScreen> {

        OrderCardShippingAddressScreen(viewModel = orderPhysicalCardViewModel)
    }
    CustomComposable<CardManagement.KitToKitTransfer> {

        KitToKitScreen(kitToKitViewModel = kitViewModel)
    }
    CustomComposable<CardManagement.TransactionSettings> {

        TransactionSettingsScreen(transactionViewModel = transactionViewModel)
    }
    CustomComposable<CardManagement.ModifyPinOTPScreen> {

        ModifyPinOtpScreen(modifyPinViewModel = modifyPinViewModel)
    }
    CustomComposable<CardManagement.OrderHistoryDetails> {

        OrderHistoryDetails(viewModel = orderHistoryViewModel)
    }
    CustomComposable<CardManagement.OrderHistory> {

        OrderHistory(viewModel = orderHistoryViewModel)
    }
    CustomComposable<CardManagement.PaymentModeSelectionScreen> {

        PaymentModeSelectionScreen()
    }
    CustomComposable<CardManagement.BeneSelectionScreen> {

        BeneficiaryListScreen()
    }
    CustomComposable<CardManagement.BeneAmountPaymentScreen> {

        BeneAmountScreen()
    }
    CustomComposable<CardManagement.AddBeneficiaryScreen> {

        AddBeneficiaryScreen()
    }
}