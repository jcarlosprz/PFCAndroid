package com.juancarlos.pfc2023.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.UserData
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class EditProfileFragment : Fragment(R.layout.fragment_profile_edit) {
    lateinit var imgProfile: ImageView
    lateinit var currentUserData: UserData //Usuario
    lateinit var mainActivity: MainActivity
    lateinit var currentUserId: String
    var imgURLFirebase: String = ""

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
                    imgProfile.setImageURI(uri)
                    uploadIMG(uri)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Declaracion de variables
        mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()
        mainActivity.setupKeyboardVisibilityListener(false)
        currentUserId = mainActivity.getCurrentUser().toString()



        ApiRest.initService()
        getUser(currentUserId, view)
        //Función Guardar (Actualiza el usuario con un PUT)
        view.findViewById<Button>(R.id.btnUpdateUser).setOnClickListener {
            //Actualiza los valores del objeto
            val etName = view.findViewById<TextView>(R.id.etPEName).text.toString()
            val etUsername = view.findViewById<TextView>(R.id.etPEUsername).text.toString()
            val etDescription = view.findViewById<TextView>(R.id.etPEDescription).text.toString()
            val etEmail = view.findViewById<TextView>(R.id.etPEEmail).text.toString()
            val etPhone = view.findViewById<TextView>(R.id.etPEPhone).text.toString()
            val tvError = view.findViewById<TextView>(R.id.tvEditProfileError)
            if (etName == "" || etUsername == "" || etEmail == "" || etPhone == "") {
                tvError.text = "Rellena todos los campos necesarios"
            } else if (!validarNumeroTelefono(etPhone)) {
                tvError.text = "Número de teléfono no válido"
            } else {
                currentUserData.name = view.findViewById<TextView>(R.id.etPEName).text.toString()
                currentUserData.username =
                    view.findViewById<TextView>(R.id.etPEUsername).text.toString()
                currentUserData.description =
                    view.findViewById<TextView>(R.id.etPEDescription).text.toString()
                currentUserData.email = view.findViewById<TextView>(R.id.etPEEmail).text.toString()
                currentUserData.contactEmail =
                    view.findViewById<TextView>(R.id.etPEEmail).text.toString()
                currentUserData.contactPhone =
                    view.findViewById<TextView>(R.id.etPEPhone).text.toString()
                if (imgURLFirebase != "") {
                    currentUserData.imgURL = imgURLFirebase
                }
                updateUser(currentUserId, currentUserData)
            }


        }
        //Alert Eliminar
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar Cuenta")
            .setMessage("¿Estás seguro/a?\nEsta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { dialog, which ->
                deleteUser(currentUserId)
                mainActivity.goToFragment(LoginFragment())
            }
            .setNegativeButton("Cancelar") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()
        //Eliminar usuario
        view.findViewById<ImageButton>(R.id.btnPEDelete).setOnClickListener {
            alerta.show()
        }

        //Pop-up
        view.findViewById<ImageView>(R.id.imgEP).setOnClickListener {
            showPopup()
        }
        //Abrir Galeria y Camara
        imgProfile = view.findViewById(R.id.imgEP)


    }

    //Pop-up
    private fun showPopup() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.camera_or_gallery_popup, null)
        builder.setView(view)

        // Configurar botones u otros elementos en el layout personalizado

        view.findViewById<CardView>(R.id.btnGaleria).setOnClickListener {
            checkPermissions()
            startGallery()
        }
        view.findViewById<CardView>(R.id.btnCamera).setOnClickListener {
            checkPermissions()
            startCamera()
        }
        val dialog = builder.create()
        dialog.show()
    }

    //Verifica si todos los permisos necesarios han sido otorgados por el usuario
    fun permissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    //Revisa los permisos necesarios para usar la cámara
    private fun checkPermissions() {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    //Permite seleccionar una imagen
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imgProfile.setImageURI(uri)
        uploadIMG(uri)
    }

    //Sube la imagen a Firebase
    private fun uploadIMG(file: Uri?) {
        file?.let { //Comprueba de forma boleana si es nulo
            val extension = getFileExtension(file)
            val imageRef =
                FirebaseStorage.getInstance().reference.child("notes/images/${UUID.randomUUID()}.$extension")
            val riversRef = imageRef.child("images/${file.lastPathSegment}")
            val uploadTask = riversRef.putFile(file)

            uploadTask.addOnFailureListener {
                Log.e("uploadFile", it.toString())
            }.addOnSuccessListener { taskSnapshot ->
                getUrl(taskSnapshot)
            }

        }
    }

    //Obtiene el URL de la imagen (de Firebase)
    private fun getUrl(taskSnapshot: UploadTask.TaskSnapshot?) {
        taskSnapshot?.storage?.downloadUrl?.addOnSuccessListener {
            imgURLFirebase = it.toString()
        }?.addOnFailureListener {
            Log.e("getUrl", it.toString())

        }
    }

    //Obtiene la extensión del archivo que se ha seleccionado
    fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(uri)) ?: "png"
    }

    //Crea un archivo temporal para almacenar temporalmente una imagen (Camara).
    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(
            requireContext().applicationContext, "${requireContext().packageName}.provider", tmpFile
        )
    }

    //Ejecución de la camara
    fun startCamera() {
        if (permissionsGranted()) {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        } else {
            Log.e(
                "startCamera", "Error while accesing the camera, Check the required permissions"
            )
        }
    }

    //Ejecución de la galeria
    private fun startGallery() {
        getContent.launch("image/*")
    }

    /**
     * CONSULTAS A LA API
     */
    //Get del usuario
    private fun getUser(id: String, view: View) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    currentUserData = body
                    val tvName = view.findViewById<TextView>(R.id.tvPEName)
                    val tvUsername = view.findViewById<TextView>(R.id.tvPEUsername)
                    val etName = view.findViewById<TextView>(R.id.etPEName)
                    val etUsername = view.findViewById<TextView>(R.id.etPEUsername)
                    val etDescription = view.findViewById<TextView>(R.id.etPEDescription)
                    val etEmail = view.findViewById<TextView>(R.id.etPEEmail)
                    val etContactPhone = view.findViewById<TextView>(R.id.etPEPhone)
                    val imgProfile = view.findViewById<CircleImageView>(R.id.imgEP)
                    tvName.text = currentUserData.name
                    etName.text = currentUserData.name
                    tvUsername.text = "@" + currentUserData.username
                    etUsername.text = currentUserData.username
                    etDescription.text = currentUserData.description
                    etEmail.text = currentUserData.email
                    etContactPhone.text = currentUserData.contactPhone
                    Glide.with(view)
                        .load(currentUserData.imgURL)
                        .into(imgProfile)
                } else {
                    Log.e("getUser", response.errorBody()?.string() ?: "Error getting user:")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("getUser", "Error: ${t.message}")
            }
        })
    }

    //Put del usuario (completo)
    private fun updateUser(id: String, updatedUser: UserData) {
        val call = ApiRest.service.updateUser(updatedUser, id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Toast.makeText(
                        mainActivity.applicationContext,
                        "Usuario actualizado",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    mainActivity.goToFragment(ProfileFragment())
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody)
                    val errorObject = errorJson.getJSONObject("error")
                    val errorMessage = errorObject.getString("message")
                    var tvError = view?.findViewById<TextView>(R.id.tvEditProfileError)!!
                    Log.e("updateUser", errorMessage)
                    if (errorMessage == "Username already taken") {
                        tvError.text = "El nombre de usuario ya está en uso"
                    } else if (errorMessage == "Email already taken") {
                        tvError.text = "El email ya está en uso"
                    }
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("updateUser", "Error: ${t.message}")
            }
        })
    }

    //Delete del usuario (completo
    private fun deleteUser(id: String) {
        val call = ApiRest.service.deleteUser(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        mainActivity.applicationContext,
                        "Cuenta eliminada",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Log.e("deleteUser", response.errorBody()?.string() ?: "Error deleting user")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("deleteUser", "Error: ${t.message}")
            }
        })
    }

    fun validarNumeroTelefono(numero: String): Boolean {
        val patron = Regex("^[0-9]{9}\$")
        return patron.matches(numero)
    }
}