package com.example.scrapbuddy

import CustomAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class RatesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rates, container, false)

        val arrayList = ArrayList<ScrapItems>()

        // Add all the values from 1 to 15 to the arrayList
        // The items are of the type NumbersView
        arrayList.add(ScrapItems(image=R.drawable.ic_newspaper, name="Newspaper", description = null, scrapType = null,price=18.0))
        arrayList.add(ScrapItems(image=R.drawable.ic_books, name="Copies/Books", description = null, scrapType = null,price=15.0))
        arrayList.add(ScrapItems(image=R.drawable.ic_cardboard, name="Cardboard", description = null, scrapType = null,price=8.0))
        arrayList.add(ScrapItems(image=R.drawable.ic_plastic, name="Plastic", description = null, scrapType = null,price=10.3))
        arrayList.add(ScrapItems(image=R.drawable.ic_iron, name="Iron", description = null, scrapType = null,price=10.3))
        arrayList.add(ScrapItems(image=R.drawable.ic_steel, name="Steel", description = null, scrapType = null,price=30.0))
        arrayList.add(ScrapItems(image=R.drawable.ic_aluminium, name="Aluminium", description = null, scrapType = null,price=12.3))
        arrayList.add(ScrapItems(image=R.drawable.ic_brass, name="Brass", description = null, scrapType = null,price=12.3))
        arrayList.add(ScrapItems(image=R.drawable.ic_copper, name="Copper", description = null, scrapType = null,price=12.3))


        // Now create the instance of the NumbersViewAdapter and pass
        // the context and arrayList created above
        val adapter = CustomAdapter(requireContext(), arrayList)

        // Create the instance of the ListView to set the numbersViewAdapter
        val list = view.findViewById<ListView>(R.id.recyclables)

        // Set the numbersViewAdapter for ListView
        list.adapter = adapter



        //Large Appliances
        val largeAppliances = ArrayList<ScrapItems>()

        // Add all the values from 1 to 15 to the arrayList
        // The items are of the type NumbersView
        largeAppliances.add(ScrapItems(image=R.drawable.ic_newspaper, name="Newspaper", description = null, scrapType = null,price=18.0))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_books, name="Copies/Books", description = null, scrapType = null,price=15.0))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_cardboard, name="Cardboard", description = null, scrapType = null,price=8.0))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_plastic, name="Plastic", description = null, scrapType = null,price=10.3))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_iron, name="Iron", description = null, scrapType = null,price=10.3))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_steel, name="Steel", description = null, scrapType = null,price=30.0))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_aluminium, name="Aluminium", description = null, scrapType = null,price=12.3))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_brass, name="Brass", description = null, scrapType = null,price=12.3))
        largeAppliances.add(ScrapItems(image=R.drawable.ic_copper, name="Copper", description = null, scrapType = null,price=12.3))


        // Now create the instance of the NumbersViewAdapter and pass
        // the context and arrayList created above

        val largerAppliAdapter= CustomAdapter(requireContext(), largeAppliances)
        // Create the instance of the ListView to set the numbersViewAdapter
        val listViewLargeAppliances = view.findViewById<ListView>(R.id.large_appliances)

        // Set the numbersViewAdapter for ListView
        listViewLargeAppliances.adapter = largerAppliAdapter

        return view
    }

}