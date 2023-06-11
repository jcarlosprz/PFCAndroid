package com.juancarlos.pfc2023.adapters

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.data.AdsListResponse
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.floor

class AdsAdapter(private val adsList: List<AdsListResponse.Data>) :
    RecyclerView.Adapter<AdsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ads, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(adsList[position])

    }

    override fun getItemCount(): Int {
        // Devuelve la cantidad de elementos en la lista itemList
        return adsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(addata: AdsListResponse.Data) {
            val imgProfile = itemView.findViewById<CircleImageView>(R.id.ivProfileAdTeacher)
            val name = itemView.findViewById<TextView>(R.id.tvItemName)
            val username = itemView.findViewById<TextView>(R.id.tvItemUsername)
            val price = itemView.findViewById<TextView>(R.id.tvAdPrice)
            val subject = itemView.findViewById<TextView>(R.id.tvAdTeacherSubject)
            val modality = itemView.findViewById<TextView>(R.id.tvAdTeacherModality)
            val description = itemView.findViewById<TextView>(R.id.tvAdTeacherDescription)
            if (addata.attributes.price == floor(addata.attributes.price)) {
                price.text = addata.attributes.price.toInt().toString() + "€"
            } else {
                price.text = addata.attributes.price.toString() + "€"
            }
            Glide.with(itemView)
                .load(addata.attributes.creator.data.attributes.imgURL)
                .into(imgProfile)
            name.text = addata.attributes.creator.data.attributes.name
            username.text = "@" + addata.attributes.creator.data.attributes.username
            subject.text = addata.attributes.subject
            modality.text = addata.attributes.modality
            description.text = addata.attributes.description

            var btnContactar = itemView.findViewById<TextView>(R.id.btnContactarItem)
            btnContactar.setOnClickListener {
                showPopup(itemView, addata)
            }
        }


    }

    private fun showPopup(itemView: View, addata: AdsListResponse.Data) {
        val contactPhone = addata.attributes.creator.data.attributes.contactPhone
        val contactEmail = addata.attributes.creator.data.attributes.contactEmail
        val context = itemView.context
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.contact_popup, null)
        builder.setView(view)

        // Configurar botones u otros elementos en el layout personalizado
        view.findViewById<Button>(R.id.btnPhoneContact).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$contactPhone")
            }
            itemView.context.startActivity(intent)
        }
        view.findViewById<Button>(R.id.btnEmailContact).setOnClickListener {
            val subject =
                "Clases de " + addata.attributes.subject // Reemplaza esto con el asunto deseado
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(contactEmail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            val chooser = Intent.createChooser(intent, "Seleccionar aplicación de correo")
            if (intent.resolveActivity(itemView.context.packageManager) != null) {
                itemView.context.startActivity(chooser)
            } else {
                Toast.makeText(
                    itemView.context,
                    "No se encontró una aplicación de correo.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}

