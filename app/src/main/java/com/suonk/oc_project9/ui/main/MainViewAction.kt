package com.suonk.oc_project9.ui.main

sealed class MainViewAction {
    sealed class Navigate : MainViewAction() {
        data class Detail(val realEstateId: Long) : Navigate()
    }
}