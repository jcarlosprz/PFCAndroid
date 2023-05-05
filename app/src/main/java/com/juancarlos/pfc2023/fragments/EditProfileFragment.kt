package com.juancarlos.pfc2023.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class EditProfileFragment() : Fragment(R.layout.fragment_profile_edit) {
    //Usuario
    lateinit var user: UserData

    //PARA ACCEDER A GALERIA Y CAMARA
    private val REQUEST_CODE_PERMISSIONS = 1
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )
    private var latestTmpUri: Uri? = null
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            if (ok) {
                latestTmpUri?.let { uri ->
                    ivImage.setImageURI(uri)
                    uploadFile(uri)
                }
            }
        }
    private lateinit var ivImage: ImageView
    var imgURLFirebase: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()
        ApiRest.initService()
        getUser("22", view)
        view.findViewById<Button>(R.id.btnPEGuardar).setOnClickListener {
            user.description = view.findViewById<TextView>(R.id.etPEDescription).text.toString()
            user.contactEmail = view.findViewById<TextView>(R.id.etPEEmail).text.toString()
            user.contactPhone = view.findViewById<TextView>(R.id.etPEPhone).text.toString()
            user.imgURL=imgURLFirebase
            Log.i("URL", imgURLFirebase)
            Log.i("URL", user.imgURL)
            updateUser("22", user)
            mainActivity.goToFragment(ProfileFragment())
        }
        view.findViewById<Button>(R.id.btnPEDelete).setOnClickListener {
            deleteUser("19")
        }
        //REVISA PERMISOS
        ivImage = view.findViewById(R.id.profile_image)
        checkPermissions()
        view.findViewById<Button>(R.id.btnGaleria).setOnClickListener {
            startGallery()
        }
        view.findViewById<Button>(R.id.btnCamera).setOnClickListener { startCamera() }

    }

    //CAMERA Y GALERIA
    private fun checkPermissions() {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    fun permissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        ivImage.setImageURI(uri)
        uploadFile(uri)
    }

    private fun uploadFile(file: Uri?) {
        file?.let { //Comprueba de forma boleana si es nulo
            val extension = getFileExtension(file)

            val imageRef = FirebaseStorage.getInstance()
                .reference
                .child("notes/images/${UUID.randomUUID()}.$extension")
            val riversRef = imageRef.child("images/${file.lastPathSegment}")
            val uploadTask = riversRef.putFile(file)

            uploadTask.addOnFailureListener {

            }.addOnSuccessListener { taskSnapshot ->
                getUrl(taskSnapshot)

            }

        }
    }

    private fun getUrl(taskSnapshot: UploadTask.TaskSnapshot?) {
        taskSnapshot?.storage?.downloadUrl?.addOnSuccessListener {
            imgURLFirebase = it.toString()
        }?.addOnFailureListener {
            Log.e("MainActivity", it.toString())

        }
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(uri)) ?: "png"
    }

    fun startCamera() {
        if (permissionsGranted()) {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        } else {
            //Mostrar error al usuario
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${requireContext().packageName}.provider",
            tmpFile
        )
    }

    private fun startGallery() {
        getContent.launch("image/*")
    }

    //CONSULTAS API
    private fun getUser(id: String, view: View) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    user = body
                    view.findViewById<TextView>(R.id.tvPEName).text = user.name
                    view.findViewById<TextView>(R.id.tvPEUsername).text = "@" + user.username
                    view.findViewById<TextView>(R.id.etPEDescription).text = user.description
                    view.findViewById<TextView>(R.id.etPEEmail).text = user.contactEmail
                    view.findViewById<TextView>(R.id.etPEPhone).text = user.contactPhone
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }


    private fun updateUser(id: String, updatedUser: UserData) {
        val call = ApiRest.service.updateUser(updatedUser, id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesa la respuesta aquí
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    private fun deleteUser(id: String) {
        val call = ApiRest.service.deleteUser(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // El usuario ha sido eliminado con éxito
                } else {
                    // Ha habido un error en la petición DELETE
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Ha habido un error en la comunicación con el servidor
            }
        })
    }
}