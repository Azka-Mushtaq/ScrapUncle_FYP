import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Date

@Parcelize
data class ScrapItem(
    var id: Int?,
    var name: String?,
    var scrapType: String?,
    var description: String?,
    var pricePerKg: Double?,
    var createdAt: String?=currentDate,
    var updatedAt: String?=currentDate
): Parcelable