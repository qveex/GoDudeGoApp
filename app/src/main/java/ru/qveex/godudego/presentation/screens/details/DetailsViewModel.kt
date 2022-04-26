package ru.qveex.godudego.presentation.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.qveex.godudego.domain.use_cases.UseCases
import ru.qveex.godudego.utils.Constants.STAT_ARGUMENT_KEY
import javax.inject.Inject

class DetailsViewModel  @Inject constructor(
    //private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedFoo: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedFoo: StateFlow<String?> = _selectedFoo

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val statId = savedStateHandle.get<Int>(STAT_ARGUMENT_KEY)
            _selectedFoo.value = statId?.let { "Foo" }
        }
    }
}