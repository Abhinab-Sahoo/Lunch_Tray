package com.example.lunchtray.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.lunchtray.data.DataSource
import java.text.NumberFormat

class OrderViewModel : ViewModel() {

    // Map of menu items
    val menuItems = DataSource.menuItems

    // Default values for item prices
    private var previousEntreePrice = 0.0
    private var previousSidePrice = 0.0
    private var previousAccompanimentPrice = 0.0

    // Default tax rate
    private val taxRate = 0.08

    // Entree for the order
    private val _entree = MutableLiveData<MenuItem?>()
    val entree: LiveData<MenuItem?> = _entree

    // Side for the order
    private val _side = MutableLiveData<MenuItem?>()
    val side: LiveData<MenuItem?> = _side

    // Accompaniment for the order.
    private val _accompaniment = MutableLiveData<MenuItem?>()
    val accompaniment: LiveData<MenuItem?> = _accompaniment

    // Subtotal for the order
    private val _subtotal = MutableLiveData(0.0)
    val subtotal: LiveData<String> = Transformations.map(_subtotal) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Total cost of the order
    private val _total = MutableLiveData(0.0)
    val total: LiveData<String> = Transformations.map(_total) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Tax for the order
    private val _tax = MutableLiveData(0.0)
    val tax: LiveData<String> = Transformations.map(_tax) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    /**
     * Set the entree for the order.
     */
    fun setEntree(entree: String) { // DONE ALL THE TODOS OF setEntree
        if (_entree.value != null) {
            previousEntreePrice = _entree.value?.price ?: 0.0
        }

        if (_subtotal.value != null) {
            _subtotal.value = _subtotal.value?.minus(previousEntreePrice)
        }

        _entree.value = menuItems[entree]
        updateSubtotal(_entree.value?.price ?: 0.0)
    }

    /**
     * Set the side for the order.
     */
    fun setSide(side: String) { // DONE ALL THE TODOS OF setSide
        if (_side.value != null) {
            previousSidePrice = _side.value?.price ?: 0.0
        }

        if (_subtotal.value != null) {
            _subtotal.value = _subtotal.value?.minus(previousSidePrice)
        }

        _side.value = menuItems[side]
        updateSubtotal(_side.value?.price ?: 0.0)
    }

    /**
     * Set the accompaniment for the order.
     */
    fun setAccompaniment(accompaniment: String) {
        if (_accompaniment.value != null) {
            previousAccompanimentPrice = _accompaniment.value?.price ?: 0.0
        }

        if (_accompaniment.value != null) {
            _subtotal.value = _subtotal.value?.minus(previousAccompanimentPrice)
        }

        _accompaniment.value = menuItems[accompaniment]
        updateSubtotal(_accompaniment.value?.price ?: 0.0)
    }

    /**
     * Update subtotal value.
     */
    private fun updateSubtotal(itemPrice: Double) {
        if (_subtotal.value != null) {
            _subtotal.value = _subtotal.value?.plus(itemPrice)
        } else {
            _subtotal.value = itemPrice
        }
        //  Otherwise, set _subtotal.value to equal the price of the item.

        calculateTaxAndTotal()
    }

    /**
     * Calculate tax and update total.
     */
    fun calculateTaxAndTotal() {
        if (_subtotal.value != null) {
            val taxAmount = _subtotal.value!! * taxRate
            _tax.value = taxAmount
            _total.value = _subtotal.value!! + taxAmount
        }

    }

    /**
     * Reset all values pertaining to the order.
     */
    fun resetOrder() {
        _entree.value = null
        _side.value = null
        _accompaniment.value = null
        _subtotal.value = 0.0
        _total.value = 0.0
        _tax.value = 0.0
    }
}
