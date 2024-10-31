package com.example.projectpam.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.projectpam.Adapter.BestSellerAdapter;
import com.example.projectpam.Adapter.CategoryAdapter;
import com.example.projectpam.Adapter.SliderAdapter;
import com.example.projectpam.Helper.ManagmentCart;
import com.example.projectpam.Model.SliderModel;
import com.example.projectpam.R;
import com.example.projectpam.ViewModel.MainViewModel;
import com.example.projectpam.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends BaseActivity {
    
    private ActivityMainBinding binding;
    private MainViewModel viewModel=new MainViewModel();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        initBanners();
        initCategory();
        initBestSeller();
        bottomNavigation();

        Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decor=window.getDecorView();
        decor.setSystemUiVisibility(0);
        
    }

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(view -> {
            ManagmentCart managmentCart=new ManagmentCart(MainActivity.this);
            if (!managmentCart.getListCart().isEmpty()){
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }else {
                Toast.makeText(MainActivity.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBestSeller() {
        binding.progressBarBestSeller.setVisibility(View.VISIBLE);
        viewModel.getBestSeller().observe(this,items->{
         binding.recyclerBestSeller.setLayoutManager(new GridLayoutManager(this, 2));
         binding.recyclerBestSeller.setAdapter(new BestSellerAdapter(items));
         binding.progressBarBestSeller.setVisibility(View.GONE);
        });
        viewModel.loadBestSeller();
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        viewModel.getCategory().observe(this, items->{
            binding.recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerViewCategory.setAdapter(new CategoryAdapter(items));
            binding.progressBarCategory.setVisibility(View.GONE);
        });
        viewModel.loadCategory();
    }

    private void initBanners() {
    binding.progressBar12.setVisibility(View.VISIBLE);
    viewModel.getSlider().observe(this, banneers->{
        setupBanners(banneers);
        binding.progressBar12.setVisibility(View.GONE);
    });
    viewModel.loadSlider();
        }

        private void setupBanners(List<SliderModel> images){
        binding.viewPager2.setAdapter(new SliderAdapter(images, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer transformer=new CompositePageTransformer();
            transformer.addTransformer(new MarginPageTransformer(40));
            binding.viewPager2.setPageTransformer(transformer);

            if (images.size() > 1){
                binding.dotIndicator.setVisibility(View.VISIBLE);
                binding.dotIndicator.attachTo(binding.viewPager2);
            }


    }
}