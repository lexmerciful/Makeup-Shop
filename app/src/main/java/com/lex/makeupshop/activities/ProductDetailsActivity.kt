package com.lex.makeupshop.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.lex.makeupshop.utils.Constants
import com.lex.makeupshop.R
import com.lex.makeupshop.adapters.ColorOptionsAdapter
import com.lex.makeupshop.databinding.ActivityProductDetailsBinding
import com.lex.makeupshop.network.MakeupItem

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    private var makeupItem: MakeupItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.MAKEUP_ITEM_EXTRA)){
            makeupItem = intent.getSerializableExtra(Constants.MAKEUP_ITEM_EXTRA) as MakeupItem
        }

        setView()
    }

    private fun setView() {
        if (makeupItem != null) {
            binding.tvDetailsName.text = makeupItem!!.name
            binding.tvDetailsBrand.text = makeupItem!!.brand
            binding.tvDetailsPriceSign.text = makeupItem!!.price_sign
            binding.tvDetailsPrice.text = makeupItem!!.price.toString()
            binding.tvDetailsCategory.text = makeupItem!!.category
            binding.tvDetailsDescription.text = makeupItem!!.description

            Glide
                .with(this)
                .load(makeupItem!!.image)
                .placeholder(R.drawable.item_placeholder)
                .into(binding.ivDetailsImage)

            val colorOptionLayoutManager =
                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            binding.rvDetailsColorOptions.layoutManager = colorOptionLayoutManager
            val adapter = ColorOptionsAdapter(this, makeupItem!!.product_colors!!)
            binding.rvDetailsColorOptions.adapter = adapter

            binding.btnBuyNow.setOnClickListener {
                if (makeupItem!!.product_link != "") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(makeupItem!!.product_link!!))
                    startActivity(intent)
                }
            }

        }

        setSupportActionBar(binding.toolbarProductDetailsActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = makeupItem!!.name
        binding.toolbarProductDetailsActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}