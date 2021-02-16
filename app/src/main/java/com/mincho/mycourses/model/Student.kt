package com.mincho.mycourses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Created by Mincho Simov on 16/02/2021.
 */

@Parcelize
@SuppressLint("ParcelCreator")
data class Student( var facNumber: Long,
                    var fName: String,
                    var sName: String,
                    var course: String): Parcelable
