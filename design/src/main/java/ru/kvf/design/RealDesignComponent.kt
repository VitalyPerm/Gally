package ru.kvf.design

import com.arkivanov.decompose.ComponentContext

class RealDesignComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, DesignComponent