package com.example.product.restaurantmemo

import android.os.Bundle
import android.support.annotation.CheckResult
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlin.properties.Delegates


class DetailFragment : Fragment() {

    private var mView: View? = null
    private var realm: Realm by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realm = Realm.getDefaultInstance()
        mView = view
    }

    override fun onResume() {
        super.onResume()

        val id = if (arguments != null) { arguments.getLong(MainActivity.EXTRA_ID) } else { null }
        val placeId = realm.where(ShopLog::class.java).equalTo("id", id).findAll()[0].placeId
        val shopLogs = realm.where(ShopLog::class.java)
                .equalTo("placeId", placeId)
                .findAllSorted("id", Sort.DESCENDING)

        initLog(shopLogs)
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    @CheckResult
    fun createInstance(id: Long): DetailFragment {
        val fragment = DetailFragment()
        val args = Bundle()

        args.putLong(MainActivity.EXTRA_ID, id)
        fragment.arguments = args

        return fragment
    }

    private fun initLog(shopLogs: OrderedRealmCollection<ShopLog>) {
        // TODO: 実装後build.gradleを元に戻す
        // TODO: 営業情報etc.の追加

        shop_star_text.text = "あなたの評価 ：" +
                String.format("%1$.1f", shopLogs.average("starRating"))
        shop_star_average.rating = shopLogs.average("starRating").toFloat()

        val logList = view?.findViewById<View>(R.id.log_list_view) as ListView
        logList.adapter = ShopLogAdapter(shopLogs)
    }
}
