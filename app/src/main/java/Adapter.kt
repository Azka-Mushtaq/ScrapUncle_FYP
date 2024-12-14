import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.scrapbuddy.R
import com.example.scrapbuddy.ScrapItems

class CustomAdapter(context: Context, arrayList: ArrayList<ScrapItems>) : ArrayAdapter<ScrapItems>(context, R.layout.list_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Recyclable view
        var currentItemView = convertView

        // If the recyclable view is null, inflate the custom layout
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        // Get the position of the view from the ArrayAdapter
        val currentNumberPosition = getItem(position)

        // Assign the desired image according to the position of the view
        val resourceImage: ImageView = currentItemView!!.findViewById(R.id.scrap_item_image)
        currentNumberPosition?.let {
            resourceImage.setImageResource(it.image)
        }

        // Assign the desired TextView 1 according to the position of the view
        val name: TextView = currentItemView.findViewById(R.id.scrap_item_name)
        name.text = currentNumberPosition?.name

        // Assign the desired TextView 1 according to the position of the view
        val price: TextView = currentItemView.findViewById(R.id.scrap_item_price)
        price.text = currentNumberPosition?.price.toString()

        // Return the recyclable view
        return currentItemView
    }
}
