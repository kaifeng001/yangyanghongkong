package com.fengkai.zhouyang.yangyanghongkong.home.fragment;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengkai.zhouyang.yangyanghongkong.R;
import com.fengkai.zhouyang.yangyanghongkong.addprodut.model.Product;
import com.fengkai.zhouyang.yangyanghongkong.addprodut.activity.AddProductActivity;
import com.fengkai.zhouyang.yangyanghongkong.activity.ProductDetailsActivity;
import com.fengkai.zhouyang.yangyanghongkong.home.adapter.RecommendAdapter;
import com.fengkai.zhouyang.yangyanghongkong.home.fragment.base.BaseFragment;
import com.fengkai.zhouyang.yangyanghongkong.home.port.IRecommend;
import com.fengkai.zhouyang.yangyanghongkong.home.presenter.RecommendPresenter;
import com.fengkai.zhouyang.yangyanghongkong.view.recycleview.DividerGridItemDecoration;
import com.fengkai.zhouyang.yangyanghongkong.utils.LibTools;

import java.util.ArrayList;
import java.util.List;


public class RecommendFragment extends BaseFragment implements IRecommend {
    private RecyclerView mRecycler;
    private TextView mDelete;
    private TextView mEdit;
    private RecommendAdapter mAdapter;
    private List<Product> mList = new ArrayList<>();
    private RecommendPresenter mPresenter;
    private List<Product> mSelectProducts = new ArrayList<>();
    private static final int REFRESH_VIEW = 0;

    @Override
    public int setLayoutId() {
        return R.layout.layout_recommend;
    }

    @Override
    public void initPresenter() {
        mPresenter = new RecommendPresenter(this);
    }

    @Override
    public void initView(View contentView) {
        mRecycler = contentView.findViewById(R.id.recommend_list);
        mRecycler.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.addItemDecoration(new DividerGridItemDecoration(getContext(), LibTools.dp2px(2), R.color.divider));
        mAdapter = new RecommendAdapter();
    }

    @Override
    public void initTitle(View view) {
        mDelete = view.findViewById(R.id.top_delete);
        mEdit = view.findViewById(R.id.top_edit);
    }

    @Override
    public void initData() {
        mPresenter.setData();
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent;
                int itemCount = mAdapter.getItemCount();
                if (position == itemCount - 1) {
                    intent = new Intent(getContext(), AddProductActivity.class);
                } else {
                    intent = new Intent(getContext(), ProductDetailsActivity.class);
                }
                startActivityForResult(intent, REFRESH_VIEW);
            }
        });
        mAdapter.setOnEditStateClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //TODO 处理数据
            }
        });
        mAdapter.setOnEditStateTrueListener(new RecommendAdapter.OnEditStateTrueListener() {
            @Override
            public void onEditStateTrue() {
                joinEditState();
            }
        });
    }

    public void setBackListener(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return mPresenter.isUseBackKeyEvent();
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    @Override
    public void refreshView() {
        mAdapter.setData(mList);
    }

    @Override
    public void joinEditState() {
        mEdit.setVisibility(View.VISIBLE);
        mDelete.setVisibility(View.VISIBLE);
        mAdapter.setEditState(true);
    }

    @Override
    public void exitEditState() {
        mEdit.setVisibility(View.GONE);
        mDelete.setVisibility(View.GONE);
        clearSelectState();
    }

    @Override
    public void initRecyclerView(List<Product> products) {
        mList.clear();
        mList.addAll(products);
        mAdapter.setData(mList);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public boolean getEditState() {
        return mAdapter.getEditState();
    }

    @Override
    public void clearSelectState() {
        mAdapter.setEditState(false);
    }
}
