import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date



@Parcelize
data class User(
    var id: Int = 0,
    var name: String? ,
    var email: String? ,
    var password: String? ,
    var phoneNumber: String?,
    var role: String?,
    var createdAt: String?=currentDate,
    var updatedAt: String? =currentDate,
    var addresses: MutableList<Address>? = mutableListOf(),
    var pickups: MutableList<Pickup>?= mutableListOf()

) : Parcelable