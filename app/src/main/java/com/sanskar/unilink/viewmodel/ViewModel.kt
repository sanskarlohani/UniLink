package com.sanskar.unilink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanskar.unilink.Resource
import com.sanskar.unilink.SharedPrefManager
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.models.User
import com.sanskar.unilink.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {

    private val repository = Repository()

    // Cache flags to prevent unnecessary API calls
    private var isUserProfileLoaded = false
    private var isLostItemsLoaded = false
    private var isFoundItemsLoaded = false

    // Cache timestamps for data freshness (5 minutes cache)
    private var lastUserProfileFetch = 0L
    private var lastLostItemsFetch = 0L
    private var lastFoundItemsFetch = 0L
    private val cacheTimeout = 5 * 60 * 1000L // 5 minutes

    private val _loginState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val loginState: StateFlow<Resource<Unit>> = _loginState

    private val _signUpState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val signUpState: StateFlow<Resource<Unit>> = _signUpState

    private val _signOutState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val signOutState: StateFlow<Resource<Unit>> = _signOutState

    private val _lostItemState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val lostItemState: StateFlow<Resource<Unit>> = _lostItemState

    private val _lostListState = MutableStateFlow<Resource<List<LostFoundItem>>>(Resource.Loading)
    val lostListState: StateFlow<Resource<List<LostFoundItem>>> = _lostListState

    private val _foundItemState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val foundItemState: StateFlow<Resource<Unit>> = _foundItemState

    private val _foundListState = MutableStateFlow<Resource<List<LostFoundItem>>>(Resource.Loading)
    val foundListState: StateFlow<Resource<List<LostFoundItem>>> = _foundListState

    private val _userProfileState = MutableStateFlow<Resource<User>>(Resource.Idle)
    val userProfileState: StateFlow<Resource<User>> = _userProfileState

    private val _itemState = MutableStateFlow<Resource<LostFoundItem>>(Resource.Idle)
    val itemState: StateFlow<Resource<LostFoundItem>> = _itemState

    // Add authentication state for persistent login
    private val _authState = MutableStateFlow<Resource<Boolean>>(Resource.Idle)
    val authState: StateFlow<Resource<Boolean>> = _authState


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
            _signOutState.value = Resource.Loading
            _signOutState.value = repository.signOut()
            _loginState.value = Resource.Idle
            _signUpState.value = Resource.Idle

            // Clear cache flags
            clearCache()
        }
    }

    fun editUserProfile(user: User) {
        viewModelScope.launch {
            repository.editUserProfile(user)

        }
    }

    fun getUserProfileWithCache(forceRefresh: Boolean = false) {
        if (!forceRefresh && isUserProfileLoaded &&
            (System.currentTimeMillis() - lastUserProfileFetch) < cacheTimeout) {
            return // Data is fresh, no need to fetch
        }

        viewModelScope.launch {
            _userProfileState.value = Resource.Loading
            val result = repository.getUserProfile()
            _userProfileState.value = result

            if (result is Resource.Success) {
                isUserProfileLoaded = true
                lastUserProfileFetch = System.currentTimeMillis()
            }
        }
    }

    fun getLostItemsWithCache(forceRefresh: Boolean = false) {
        if (!forceRefresh && isLostItemsLoaded &&
            (System.currentTimeMillis() - lastLostItemsFetch) < cacheTimeout) {
            return // Data is fresh, no need to fetch
        }

        viewModelScope.launch {
            _lostListState.value = Resource.Loading
            val result = repository.getLostItems()
            _lostListState.value = result

            if (result is Resource.Success) {
                isLostItemsLoaded = true
                lastLostItemsFetch = System.currentTimeMillis()
            }
        }
    }

    fun getFoundItemsWithCache(forceRefresh: Boolean = false) {
        if (!forceRefresh && isFoundItemsLoaded &&
            (System.currentTimeMillis() - lastFoundItemsFetch) < cacheTimeout) {
            return // Data is fresh, no need to fetch
        }

        viewModelScope.launch {
            _foundListState.value = Resource.Loading
            val result = repository.getFoundItems()
            _foundListState.value = result

            if (result is Resource.Success) {
                isFoundItemsLoaded = true
                lastFoundItemsFetch = System.currentTimeMillis()
            }
        }
    }

    // Force refresh method for pull-to-refresh
    fun refreshAllData() {
        getUserProfileWithCache(forceRefresh = true)
        getLostItemsWithCache(forceRefresh = true)
        getFoundItemsWithCache(forceRefresh = true)
    }

    // Keep existing methods for backward compatibility
    fun getUserProfile() = getUserProfileWithCache()
    fun getLostItems() = getLostItemsWithCache()
    fun getFoundItems() = getFoundItemsWithCache()

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

    fun resetLostItemState() {
        _lostItemState.value = Resource.Idle
    }

    fun resetFoundItemState() {
        _foundItemState.value = Resource.Idle
    }

    fun resetSignOutState() {
        _signOutState.value = Resource.Idle
    }

    fun UpdateLostItems(id: String, lostFoundItem: LostFoundItem) {
        viewModelScope.launch {
            repository.UpdateLostItems(id, lostFoundItem)
        }
    }

    fun UpdateFoundItems(id: String, lostFoundItem: LostFoundItem) {
        viewModelScope.launch {
            repository.UpdateFoundItems(id, lostFoundItem)
        }
    }

    fun getItem(id: String, type: String): Resource<LostFoundItem> {
        viewModelScope.launch {

            _lostListState.value = Resource.Loading
            _itemState.value = repository.getItem(id, type)
        }
        return _itemState.value
    }

    // Check authentication state
    fun checkAuthenticationState() {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            _authState.value = repository.checkAuthenticationState()
        }
    }

    fun isUserAuthenticated(): Boolean {
        return repository.isUserAuthenticated()
    }

    fun getCurrentUserEmail(): String? {
        return repository.getCurrentUserEmail()
    }

    private fun clearCache() {
        isUserProfileLoaded = false
        isLostItemsLoaded = false
        isFoundItemsLoaded = false
        lastUserProfileFetch = 0L
        lastLostItemsFetch = 0L
        lastFoundItemsFetch = 0L
    }
}
