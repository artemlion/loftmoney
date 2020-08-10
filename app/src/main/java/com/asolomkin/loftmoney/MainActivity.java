package com.asolomkin.loftmoney;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private Toolbar mToolBar;
    public static final String TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabs);
        mToolBar = findViewById(R.id.toolbar);
        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int activeFragmentIndex = viewPager.getCurrentItem();

                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.ANY_KEY, activeFragmentIndex);
                startActivity(intent);

            }
        });

        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.getTabAt(0).setText(R.string.expences);
        mTabLayout.getTabAt(1).setText(R.string.income);
    }


    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return BudgetFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}

