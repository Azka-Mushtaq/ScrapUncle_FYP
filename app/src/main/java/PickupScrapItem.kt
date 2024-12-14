import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PickupScrapItem(
    var pickupId: Int,
    var scrapItemId: Int,
    var quantityKg: Double,
    var scrapItem: ScrapItem?
): Parcelable