package com.ankit.mishra.Data

fun <E>Collection<out E>.isEmpty() : Boolean = this==null || this.isEmpty()
