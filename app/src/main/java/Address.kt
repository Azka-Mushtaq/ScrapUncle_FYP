import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Address(
    var id: Int,
    var userId: Int,
    var street: String,
    var city: String,
    var zipcode: String
): Parcelable