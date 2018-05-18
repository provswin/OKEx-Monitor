package cn.a4miles.okex_monitor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import cn.a4miles.okex_monitor.adapter.OKExpandableItemAdapter
import cn.a4miles.okex_monitor.entity.OKSymbolLevelItem
import cn.a4miles.okex_monitor.entity.OKSymbolTickersLevelItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import cn.a4miles.okex_monitor.OKMainActivity.ScreenStatusReceiver






class OKMainActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter : OKExpandableItemAdapter

    private lateinit var mList : java.util.ArrayList<MultiItemEntity>

    private val symbols: Array<String> = arrayOf("btc", "eos", "ltc", "eth", "etc", "bch", "btg",
            "xrp")

    private val mInterval:Long = 10 * 1000

    private var mScreenStatusReceiver: ScreenStatusReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_okmain)

        mRecyclerView = findViewById(R.id.rv)

        mList = generateData();

        mAdapter = OKExpandableItemAdapter(mList, mInterval, symbols.asList())

        mRecyclerView.adapter = mAdapter

        mRecyclerView.layoutManager = LinearLayoutManager(this)

        registerSreenStatusReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mScreenStatusReceiver)
    }

    private fun generateData(): java.util.ArrayList<MultiItemEntity> {
        val res = java.util.ArrayList<MultiItemEntity>()
        for (i in 0 until symbols.size) {
            val symbol = OKSymbolLevelItem()
            symbol.symbol = symbols[i]
            symbol.position = i;

            val tickers = OKSymbolTickersLevelItem()
            tickers.position = i
            tickers.symbol = symbols[i]

            symbol.addSubItem(tickers)

            res.add(symbol)
        }
        return res
    }

    private fun registerSreenStatusReceiver() {
        mScreenStatusReceiver = ScreenStatusReceiver()
        val screenStatusIF = IntentFilter()
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON)
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(mScreenStatusReceiver, screenStatusIF)
    }

    internal inner class ScreenStatusReceiver : BroadcastReceiver() {
        var SCREEN_ON = "android.intent.action.SCREEN_ON"
        var SCREEN_OFF = "android.intent.action.SCREEN_OFF"

        override fun onReceive(context: Context?, intent: Intent?) {
            if (SCREEN_ON == intent?.action) {
                mAdapter.resumeAllMonitors()
            } else if (SCREEN_OFF == intent?.action) {
                mAdapter.stopAllMonitors()
            }
        }
    }
}
