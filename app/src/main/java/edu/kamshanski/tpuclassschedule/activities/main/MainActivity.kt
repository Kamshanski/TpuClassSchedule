package edu.kamshanski.tpuclassschedule.activities.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import edu.kamshanski.tpuclassschedule.R
import edu.kamshanski.tpuclassschedule.activities._abstract.BaseAppCompatActivity
import edu.kamshanski.tpuclassschedule.databinding.ActivityMainBinding
import edu.kamshanski.tpuclassschedule.model.loader.LoadStatus
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.ShortSource
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.Source
import edu.kamshanski.tpuclassschedule.utils.lg
import edu.kamshanski.tpuclassschedule.utils.ui.setOnTextChangedListener
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalContracts
@ExperimentalTime
class MainActivity : BaseAppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val vm: MainViewModel by viewModels()

    lateinit var adapter: SourceArrayListAdapter

    override fun initViews() {
        adapter = SourceArrayListAdapter(applicationContext, emptyList(), emptyMap())
        binding.rvSources.adapter = adapter
    }

    override fun initViewModel() {
        vm.getSources("").observe(this) { res ->
            when(res.status) {
                LoadStatus.SUCCESS -> {
                    adapter.data = res.data ?: emptyList()
                    adapter.notifyDataSetChanged()
                }
                LoadStatus.ERROR -> {
                    res.error?.printStackTrace()
                    res.message
                }
                LoadStatus.LOADING -> "${binding.etInput.text} is loading. Please wait"
                LoadStatus.CREATED -> Unit
            }
        }

        vm.fullSources.observe(this) { map ->
            adapter.loadedData = map
            adapter.notifyDataSetChanged()
        }
    }

    override fun initListeners() {
        binding.etInput.setOnTextChangedListener { txt ->
            vm.getSources(txt)
        }
    }

    fun searchSourceInfo(source: ShortSource) {
        vm.getSourceInfo(source)
    }

    inner class SourceArrayListAdapter(val context: Context, var data: List<ShortSource>, var loadedData: Map<String, Source>) : RecyclerView.Adapter<SourceViewHolder>() {

        val listener = View.OnClickListener { v ->
            val viewTag = v.tag
            if (viewTag is ShortSource) {
                searchSourceInfo(viewTag)
            } else {
                lg("False view tag! It's no instaince of Source class: $viewTag")
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.simple_list_item_2_texts, parent, false)
            return SourceViewHolder(view)
        }

        override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
            val source = data[position]
            with(holder) {
                fitView(upperText, source, "${source.name} / ${source.fullName}")
                val loadedSource = loadedData[source.hash]
                val info = StringBuilder().apply {
                    append(source.hash)
                    if (loadedSource != null) {
                        append(": ").append(loadedSource.link)
                        loadedSource.info?.also {
                            append(", ")
                            append(it.fullName)
                            append(" (").append(it.nameAppendix).append(") ,")
                            for (oi in it.otherInfo) {
                                append(oi.info).append(" (").append(oi.link).append("), ")
                            }
                        }
                    }
                }
                fitView(lowerText, source, info.toString())
            }
        }

        private fun fitView(tx: TextView, source: ShortSource, text: String) {
            tx.text = text
            tx.tag = source
            tx.setOnClickListener(listener)
        }

        override fun getItemCount(): Int = data.size
    }

    class SourceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val upperText = view.findViewById<TextView>(R.id.text1)
        val lowerText = view.findViewById<TextView>(R.id.text2)
    }
}