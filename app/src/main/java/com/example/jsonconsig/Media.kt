package com.example.jsonconsig

data class Media(var filename: String="") {

    var volume: Int =0

    constructor (file:String, volume:Int) : this (file) {
        this.volume = volume
        if ( this.volume < 0) {
            this.volume = 0
        }
        if (this. volume > 99) {
            this.volume = 99
        }
    }
}