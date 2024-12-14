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

class PickupAdapter(context: Context, arrayList: List<Pickup>?) : ArrayAdapter<Pickup>(
    context, R.layout.pickup_list_item,
    arrayList!!
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Recyclable view
        var currentItemView = convertView

        // If the recyclable view is null, inflate the custom layout
        if (currentItemView == null) {
            currentItemView =
                LayoutInflater.from(context).inflate(R.layout.pickup_list_item, parent, false)
        }

        // Get the position of the view from the ArrayAdapter
        val currentPickup = getItem(position)

        // Assign the desired image according to the position of the view
        val imageView: ImageView = currentItemView!!.findViewById(R.id.pickup_image)
        // currentPickup?.let { imageView.setImageResource(it.image) } // Uncomment and use if you have an image resource

        // Assign the desired TextView for Pickup ID
        val id: TextView = currentItemView.findViewById(R.id.pickup_id)
        id.text = "Pickup ID #" + currentPickup?.id.toString()

        // Assign the desired TextView for createdAt
        val createdAt: TextView = currentItemView.findViewById(R.id.pickup_date)

        //Formating date
        val inputFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        val date: Date? = inputFormat.parse(currentPickup!!.createdAt)
        val formatedDate = outputFormat.format(date)

        //setting  formated date in created at field in view
        createdAt.text = formatedDate

        // Assign the desired TextView for createdAt
        val status: TextView = currentItemView.findViewById(R.id.pickup_status)

        status.text = if (currentPickup?.status == "pending")
            "View Details"
        else
            currentPickup?.status.toString()
        // Return the view
        return currentItemView
    }
}
