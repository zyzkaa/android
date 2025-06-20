package pl.edu.am_projekt.fragment.meal
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pl.edu.am_projekt.adapter.ProductAdapter
import pl.edu.am_projekt.databinding.ActivityMealDetailsBinding
import pl.edu.am_projekt.databinding.FragmentAddProductBinding
import pl.edu.am_projekt.model.parcelable.Product
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import androidx.core.view.isVisible
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import pl.edu.am_projekt.activity.BarcodeScanActivity
import pl.edu.am_projekt.activity.MealActivity
import pl.edu.am_projekt.model.CreateProductDto
import pl.edu.am_projekt.model.manager.BarcodeCode

class AddProductFragment : Fragment() {
    companion object {
        private const val CAMERA_REQUEST_CODE = 1000
        private const val BARCODE_SCAN_REQUEST_CODE = 2000
    }
    private var searchJob: Job? = null

    private val barcodeScanLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val barcode = result.data?.getStringExtra("barcode")
                if (barcode != null) {
                    fetchProductFromServer(barcode)
                }
            }
        }



    private lateinit var apiService: ApiService

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)


        val adapter = ProductAdapter(mutableListOf()) { productId, productName ->
            val result = Bundle().apply {
                putInt("selectedProductId", productId)
                putString("selectedProductName", productName)
            }
            parentFragmentManager.setFragmentResult("product_selected", result)
            parentFragmentManager.popBackStack()
        }

        binding.productsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.productsRecycler.adapter = adapter

        binding.addProductButton.setOnClickListener {
                val name = binding.productNameEditText.text.toString()
                val kcal = binding.productKcalEditText.text.toString().toIntOrNull() ?: 0
                val carbs = binding.productCarbsEditText.text.toString().toDoubleOrNull() ?: 0.0
                val prot = binding.productProtEditText.text.toString().toDoubleOrNull() ?: 0.0
                val fat = binding.productFatEditText.text.toString().toDoubleOrNull() ?: 0.0

                if (name.isBlank()) {
                    Toast.makeText(requireContext(), "Wprowadź nazwę produktu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val productDto = CreateProductDto(
                    name = name,
                    caloriesPer100g = kcal,
                    proteinPer100g = prot,
                    carbsPer100g = carbs,
                    fatPer100g = fat
                )

                // Wyślij do backendu i przekaż wynik dalej
                lifecycleScope.launch {
                    try {
                        val response = apiService.addProduct(productDto)
                        if (response.isSuccessful && response.body() != null) {
                            val result = Bundle().apply {
                                putInt("selectedProductId", response.body()!!.id)
                                putString("selectedProductName", response.body()!!.name)
                            }
                            parentFragmentManager.setFragmentResult("product_selected", result)
                            parentFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(requireContext(), "Nie udało się dodać produktu", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Błąd połączenia z serwerem", Toast.LENGTH_SHORT).show()
                    }
                }
        }
//        binding.searchProductButton.setOnClickListener {
//            val query = binding.searchBar.text.toString().trim()
//
//            lifecycleScope.launch {
//                try {
//                    val response = if (query.isBlank()) {
//                        apiService.getTopProducts()
//                    } else {
//                        apiService.searchProducts(query)
//                    }
//                    if (response.isSuccessful && response.body() != null) {
//                        val products = response.body()!!
//                        adapter.updateData(products)
//                    } else {
//                        Toast.makeText(requireContext(), "Brak wyników", Toast.LENGTH_SHORT).show()
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Toast.makeText(requireContext(), "Błąd podczas wyszukiwania", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(400) // debounce: 400 ms po ostatnim wpisie
                    val query = s.toString().trim()

                    try {
                        val response = if (query.isBlank()) {
                            apiService.getTopProducts()
                        } else {
                            apiService.searchProducts(query)
                        }

                        if (response.isSuccessful && response.body() != null) {
                            adapter.updateData(response.body()!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Błąd podczas wyszukiwania", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })





        binding.barcodeButton.setOnClickListener {
            checkCameraPermissionAndStartScanner()
        }



        // Coroutine to load data
        lifecycleScope.launch {
            try {
                val response = apiService.getTopProducts()
                if (response.isSuccessful && response.body() != null) {
                    val products = response.body()
                    adapter.updateData(products!!)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun launchBarcodeScanner() {
        barcodeScanLauncher.launch(Intent(requireContext(), BarcodeScanActivity::class.java))

    }


    private fun checkCameraPermissionAndStartScanner() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            launchBarcodeScanner()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchBarcodeScanner()
        } else {
            Toast.makeText(requireContext(), "Brak dostępu do kamery", Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchProductFromServer(barcode: String) {
        lifecycleScope.launch {
            try {
                val response = apiService.getProductNutrition(barcode) // Ensure this method exists in your ApiService
                if (response.isSuccessful && response.body() != null) {
                    val product = response.body()!!

                    // Fill EditTexts with product data
                    binding.productNameEditText.setText(product.productName)
                    binding.productKcalEditText.setText(product.nutriments?.energyKcal.toString())
                    binding.productCarbsEditText.setText(product.nutriments?.carbohydrates.toString())
                    binding.productProtEditText.setText(product.nutriments?.proteins.toString())
                    binding.productFatEditText.setText(product.nutriments?.fat.toString())

                } else {
                    Toast.makeText(requireContext(), "Product not found for barcode: $barcode", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    override fun onResume() {
        super.onResume()
        (activity as? MealActivity)?.showMealPlanFab(false)
        (activity as? MealActivity)?.showListFab(false)

    }

}

