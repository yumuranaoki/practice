package com.example.yumuranaoki.practice

import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class NewWalletModule {
    @Provides
    fun provideNewWallet() : NewWallet = NewWallet()
}

@Component(modules = arrayOf(NewWalletModule::class))
interface NewWalletComponent {
    fun inject(app: MainActivity)
}
