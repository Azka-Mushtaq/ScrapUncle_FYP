import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.scrapbuddy.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavedAddressAdapter(context: Context, arrayList: List<Address>?) : ArrayAdapter<Address>(context, R.layout.address_item,
    arrayList!!
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Recyclable view
        var currentItemView = convertView

        // If the recyclable view is null, inflate the custom layout
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(context).inflate(R.layout.address_item, parent, false)
        }

        // Get the position of the view from the ArrayAdapter
        val currentAddress = getItem(position)

        // Assign the desired image according to the position of the view
        //val imageView: ImageView = currentItemView!!.findViewById(R.id.pickup_image)
        // currentPickup?.let { imageView.setImageResource(it.image) } // Uncomment and use if you have an image resource

        // Assign the desired TextView for Pickup ID
        val address: TextView = currentItemView!!.findViewById(R.id.address_details)
        address.text = currentAddress?.street.toString()+", "+ currentAddress?.city + "\nzipcode:  "+ currentAddress?.zipcode


        // Return the view
        return currentItemView
    }
}
