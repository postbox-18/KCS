package com.example.adm;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.adm.Fragments.Notification.NotificationFragment;
import com.example.adm.Fragments.Orders.OrdersFragment;
import com.example.adm.Fragments.ProfileFragment;
import com.example.adm.Fragments.Users.UserFragment;

public class MyFragments_Adapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private Context context;
    private FragmentManager parentFragmentManager;
    private String TAG="MyFragments_Adapter";
    public MyFragments_Adapter(Context context, FragmentManager parentFragmentManager, int tabCount) {
        super(parentFragmentManager);
        this.parentFragmentManager=parentFragmentManager;
        this.context=context;
        this.tabCount=tabCount;

    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OrdersFragment ordersFragment = new OrdersFragment();
                return ordersFragment;
            case 1:
                UserFragment userFragment = new UserFragment();
                return userFragment;
            case 2:
                NotificationFragment notificationFragment=new NotificationFragment();
                return notificationFragment;
            case 3:
                ProfileFragment profileFragment=new ProfileFragment();
                return profileFragment;

            default:
                return null;
        }

    }

}
