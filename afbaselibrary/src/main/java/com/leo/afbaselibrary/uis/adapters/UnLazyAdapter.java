package com.leo.afbaselibrary.uis.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.leo.afbaselibrary.listeners.ITabContent;
import com.leo.afbaselibrary.listeners.ITabPager;

import java.util.List;

/**
 * Created by GongLi on 2018/6/7.
 * Email：lc824767150@163.com
 */

public class UnLazyAdapter<T extends ITabPager> extends FragmentPagerAdapter {
    private List<T> mList;
    private ITabContent tabContent;

    public UnLazyAdapter(FragmentManager fm, List<T> list, ITabContent tabContent) {
        super(fm);
        this.mList = list;
        this.tabContent = tabContent;
    }

    public void setList(List<T> mList) {
        this.mList = mList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return tabContent.getContent(position);
    }
}

