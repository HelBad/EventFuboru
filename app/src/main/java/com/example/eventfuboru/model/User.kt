package com.example.eventfuboru.model

class User {
    lateinit var no_reg: String
    lateinit var no_urut: String
    lateinit var nama: String
    lateinit var email: String
    lateinit var telp: String
    lateinit var pekerjaan: String
    lateinit var kendaraan: String
    lateinit var merk: String
    lateinit var nopol: String
    lateinit var shift: String
    lateinit var token: String
    lateinit var tgl_scan: String
    lateinit var tgl_service: String
    lateinit var status: String
    lateinit var search: String

    constructor() {}
    constructor(no_reg: String, no_urut: String, nama: String, email: String, telp: String,
                pekerjaan: String, kendaraan: String, merk: String, nopol: String, shift: String,
                token: String, tgl_scan: String, tgl_service: String, status: String, search: String) {
        this.no_reg = no_reg
        this.no_urut = no_urut
        this.nama = nama
        this.email = email
        this.telp = telp
        this.pekerjaan = pekerjaan
        this.kendaraan = kendaraan
        this.merk = merk
        this.nopol = nopol
        this.shift = shift
        this.token = token
        this.tgl_scan = tgl_scan
        this.tgl_service = tgl_service
        this.status = status
        this.search = search
    }
}