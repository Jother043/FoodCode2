package com.example.foodcode2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodcode2.R
import com.example.foodcode2.databinding.ItemImageBinding

class ImageAdapter(
    private var _images: List<String>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemImageBinding.bind(view)
        fun bind(image: String) {
            // Carga la imagen en el ImageView
            binding.imageView.load(image) {
                crossfade(true)
                error(R.drawable.nocamaras)
            }

            // Muestra la imagen en un diálogo al hacer clic en ella
            binding.imageView.setOnClickListener {
                val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
                val dialog = ImageDialogFragment.newInstance(image)
                dialog.show(fragmentManager, "ImageDialogFragment")
            }
        }


    }

    // Crea una nueva vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(layoutInflater.inflate(R.layout.item_image, parent, false))
    }

    // Vincula los datos de la lista con los elementos de la vista
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(_images[position])
    }

    // Devuelve el número de elementos en la lista
    override fun getItemCount() = _images.size

    fun setImages(images: List<String>) {
        _images = images
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado

        // Imprime las URLs de las imágenes en el log
        for (image in images) {
            Log.d("ImageAdapter", "Image URL: $image")
        }
    }
}