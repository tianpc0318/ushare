package com.miles.ushare;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miles.ushare.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangChang on 2016/5/15.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewAdapter.OnItemClickListener {
    private View view;
    private ViewPager mViewPager;
    FragmentAdapter mFragmentAdapteradapter;
    private TabLayout mTabLayout;
    List<String> titles;
    List<Fragment> fragments;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private static final int VERTICAL_LIST = 0;
    private static final int HORIZONTAL_LIST = 1;
    private static final int VERTICAL_GRID = 2;
    private static final int HORIZONTAL_GRID = 3;
    private static final int STAGGERED_GRID = 4;

    private static final int SPAN_COUNT = 2;
    private int flag = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_tab_layout, container, false);
        initViewPager();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);

        flag = 0;
        configRecyclerView();

        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void configRecyclerView() {

        switch (flag) {
            case VERTICAL_LIST:
                mLayoutManager =
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_LIST:
                mLayoutManager =
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                break;
            case VERTICAL_GRID:
                mLayoutManager =
                        new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_GRID:
                mLayoutManager =
                        new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
                break;
            case STAGGERED_GRID:
                mLayoutManager =
                        new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        if (flag != STAGGERED_GRID) {
            recyclerViewAdapter = new RecyclerViewAdapter(getActivity());
            recyclerViewAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(recyclerViewAdapter);
        } else {

        }

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    private void initViewPager() {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        titles = new ArrayList<>();
        titles.add("精选");
        titles.add("体育");
        titles.add("巴萨");
        titles.add("AAAAAA");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            ListFragment mFragment = new ListFragment();
            mFragment.setArguments(mBundle);
            fragments.add(i, mFragment);
        }
        mFragmentAdapteradapter =
                new FragmentAdapter(getFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onRefresh() {

        // 刷新时模拟数据的变化
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                int temp = (int) (Math.random() * 10);
                if (flag != STAGGERED_GRID) {
                    recyclerViewAdapter.mDatas.add(0, "new" + temp);
                    recyclerViewAdapter.notifyDataSetChanged();
                } else {

                }
            }
        }, 1000);
    }

    @Override
    public void onItemClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(R.string.item_clicked), 0);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(R.string.item_longclicked), 0);
    }

}
