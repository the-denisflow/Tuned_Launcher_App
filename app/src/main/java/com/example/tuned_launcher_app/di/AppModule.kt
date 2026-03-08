package com.example.tuned_launcher_app.di

import com.example.tuned_launcher_app.data.repository.AppRepository
import com.example.tuned_launcher_app.presentation.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AppRepository(get()) }
    viewModel { AppViewModel(get()) }
}