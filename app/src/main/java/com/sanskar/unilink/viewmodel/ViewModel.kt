package com.sanskar.unilink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanskar.unilink.Resource
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.models.User
import com.sanskar.unilink.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {

    private val repository = Repository()

    private val _loginState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val loginState: StateFlow<Resource<Unit>> = _loginState

    private val _signUpState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val signUpState: StateFlow<Resource<Unit>> = _signUpState

    private val _lostItemState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val lostItemState: StateFlow<Resource<Unit>> = _lostItemState

    private val _lostListState = MutableStateFlow<Resource<List<LostFoundItem>>>(Resource.Loading)
    val lostListState: StateFlow<Resource<List<LostFoundItem>>> = _lostListState

    private val _foundItemState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val foundItemState: StateFlow<Resource<Unit>> = _foundItemState

    private val _foundListState = MutableStateFlow<Resource<List<LostFoundItem>>>(Resource.Loading)
    val foundListState: StateFlow<Resource<List<LostFoundItem>>> = _foundListState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = repository.login(email, password)
        }
    }

    fun signUp(user: User, password: String) {
        viewModelScope.launch {
            _signUpState.value = Resource.Loading
            _signUpState.value = repository.signUp(user, password)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }

    fun editUserProfile(user: User) {
        viewModelScope.launch {
            repository.editUserProfile(user)
        }
    }

    fun lostItem(item: LostFoundItem) {
        viewModelScope.launch {
            _lostItemState.value = Resource.Loading
            _lostItemState.value = repository.lostItem(item)
        }
    }

    fun foundItem(item: LostFoundItem) {
        viewModelScope.launch {
            _foundItemState.value = Resource.Loading
            _foundItemState.value = repository.foundItem(item)
        }
    }

    fun getLostItems() {
        viewModelScope.launch {
            _lostListState.value = Resource.Loading
            _lostListState.value = repository.getLostItems()
        }
    }

    fun getFoundItems() {
        viewModelScope.launch {
            _foundListState.value = Resource.Loading
            _foundListState.value = repository.getFoundItems()
        }
    }

    fun resetLostItemState() {
        _lostItemState.value = Resource.Idle
    }

    fun resetFoundItemState() {
        _foundItemState.value = Resource.Idle
    }

}
