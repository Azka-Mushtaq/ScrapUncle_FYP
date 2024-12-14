import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.scrapbuddy.R
import com.example.scrapbuddy.ScrapItems


// Custom adapter for the spinner
class ScrapTypeAdapter(private val context: Context, private val items: List<ScrapItems>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    // Create and return the view for each item in the spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.scrap_item, parent, false)

        val item = getItem(position) as ScrapItems

        // Bind data to views
        val name: TextView = view.findViewById(R.id.scrap_item_name)
        name.text = item?.name

        // Assign the desired TextView 1 according to the position of the view
        val price: TextView = view.findViewById(R.id.scrap_item_price)
        price.text = item?.price.toString()

        // Assign the desired TextView 1 according to the position of the view
        val image = view.findViewById<ImageView>(R.id.scrap_item_image)
        image.setImageResource(item.image)
        return view
    }
}
