import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
val currentDate = dateFormat.format(Date())

@Parcelize
data class Pickup(
    var id: Int?,
    var customerId: Int?,
    var riderId: Int?=null,
    var addressId: Int?,
    var pickupDate: String?,
    var pickupTime: String?,
    var status: String?="pending",
    var totalWeight: String?,
    var createdAt: String? = currentDate,
    var updatedAt: String?=currentDate,
    //var pickupScrapItems: List<PickupScrapItem> = mutableListOf()
): Parcelable