package com.asolomkin.loftmoney;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
public class MainActivity extends AppCompatActivity {

    public static final String EXPENSE = "expense";
    public static final String INCOME = "income";
    private TabLayout mTabLayout;
    private Toolbar mToolBar;
    private FloatingActionButton mFloatingActionButton;
//    public static final String TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabs);
        mToolBar = findViewById(R.id.toolbar);
        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
//        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        final BudgetPagerAdapter adapter = new BudgetPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(
                    final int position,
                    final float positionOffset,
                    final int positionOffsetPixels
            ) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (position == 2) {
                    mFloatingActionButton.hide();
                } else {
                    mFloatingActionButton.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int activeFragmentIndex = viewPager.getCurrentItem();

                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.ANY_KEY, activeFragmentIndex);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);

            }
        });

        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.getTabAt(0).setText(R.string.expences);
        mTabLayout.getTabAt(1).setText(R.string.income);
        mTabLayout.getTabAt(2).setText(R.string.balance);

    }
    public void loadBalance() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BalanceFragment) {
                ((BalanceFragment)fragment).loadBalance();
                mFloatingActionButton.hide();
            }
        }
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        mFloatingActionButton.hide();
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mFloatingActionButton.show();
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull final FragmentManager fm, final int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(final int position) {
            switch (position) {
                case 0:
                    return BudgetFragment.newInstance(R.color.expenseColor, EXPENSE);
                case 1:
                    return BudgetFragment.newInstance(R.color.incomeColor, INCOME);
                case 2:
                    return BalanceFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }




}

