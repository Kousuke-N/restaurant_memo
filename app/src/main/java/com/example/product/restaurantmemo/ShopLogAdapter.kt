package com.example.product.restaurantmemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.RatingBar
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import java.text.SimpleDateFormat
import java.util.*

class ShopLogAdapter(realmResults: OrderedRealmCollection<ShopLog>)
    : RealmBaseAdapter<ShopLog>(realmResults), ListAdapter {

    private class ViewHolder(itemView: View) {
        var date = itemView.findViewById<View>(R.id.log_date) as TextView
        var comment = itemView.findViewById<View>(R.id.log_comment) as TextView
        var stars = itemView.findViewById<View>(R.id.log_star) as RatingBar
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view: View? = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shop_log, parent, false)

            holder = ViewHolder(view)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val df = SimpleDateFormat("yyyy/MM/dd")
        df.timeZone = (TimeZone.getTimeZone("Asia/Tokyo"))

        val resultItem = getItem(position)

        // Viewにそれぞれ値を代入
        if (resultItem != null) {
            holder.date.text = df.format(resultItem.logDate)
            holder.comment.text = resultItem.comment
            holder.stars.rating = resultItem.starRating
        }

        return view
    }

    override fun isEnabled(position: Int): Boolean {
        return false
    }
}
