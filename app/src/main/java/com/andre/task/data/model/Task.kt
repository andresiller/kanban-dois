package com.andre.task.data.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var id: String,
    var description: String,
    var status: Status
) : Parcelable {
    // 🔥 Construtor vazio exigido pelo Firebase
    constructor() : this(
        id = "",
        description = "",
        status = Status.TODO
    )
}

