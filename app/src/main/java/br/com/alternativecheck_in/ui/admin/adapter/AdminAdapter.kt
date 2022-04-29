package br.com.alternativecheck_in.ui.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.alternativecheck_in.databinding.ItemDriverBinding
import br.com.alternativecheck_in.model.Driver

class AdminAdapter(
    private val drivers: List<Driver>,
    private val listener: SendDriver? = null
) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemDriverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(driver: Driver) {
            binding.apply {
                textviewIdDriver.text = driver.driverCod
                textviewNameDriver.text = driver.driverName
                layout.setOnClickListener { listener?.sendDriver(driver) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDriverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(drivers[position])
    }

    override fun getItemCount(): Int = drivers.size

}

interface SendDriver {
    fun sendDriver(driver: Driver)
}
