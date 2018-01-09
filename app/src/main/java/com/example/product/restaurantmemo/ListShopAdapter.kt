package com.example.product.restaurantmemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.RatingBar
import android.widget.TextView
import io.realm.*

class ListShopAdapter(realmResults: OrderedRealmCollection<ShopLog>)
    : RealmBaseAdapter<ShopLog>(realmResults), ListAdapter {

    private var aveNumStars = 0f
    private var numVisits = 0
    private var latestLog = ""

    private class ViewHolder(itemView: View) {
        var name = itemView.findViewById<View>(R.id.shop_name) as TextView
        var visits = itemView.findViewById<View>(R.id.shop_visits) as TextView
        var latestLog = itemView.findViewById<View>(R.id.shop_latest_log) as TextView
        var stars = itemView.findViewById<View>(R.id.shop_star) as RatingBar
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view: View? = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_shop, parent, false)

            holder = ViewHolder(view)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        initItem(getItem(position))

        // Viewにそれぞれ値を代入
        holder.name.text = "${getItem(position)?.placeId} (Name)"
        holder.visits.text = "${numVisits}回このお店に来ています"
        holder.latestLog.text = latestLog
        holder.stars.rating = aveNumStars

        return view
    }

    // 星の平均, 訪れた回数, 最新ログの生成
    private fun initItem(resultItem: ShopLog?) {
        Realm.getDefaultInstance().use { realm ->
            val resultLog = realm.where(ShopLog::class.java)
                    .equalTo("placeId", resultItem?.placeId)
                    .findAllSorted("id", Sort.DESCENDING)

            aveNumStars = resultLog.average("numStars").toFloat()
            numVisits = resultLog.size
            latestLog = extractLatestLog(resultLog)
        }
    }

    // 最新ログの抽出
    private fun extractLatestLog(logs: RealmResults<ShopLog>): String {
        var response = "${logs[0].placeId} (Date)" + if (logs[0].comment.isNotEmpty()) " - ${logs[0].comment}\n" else "\n"
        if (logs.size > 1) {
            response += "${logs[1].placeId} (Date)" + if (logs[1].comment.isNotEmpty()) " - ${logs[1].comment}" else ""
        }

        return response
    }
}
