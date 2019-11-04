package com.example.gs.templatecode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * 带有上拉刷新的模板代码
 */
public class RecyclerViewLoadMoreActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    int mCurrentPage = 0;
    public static final int INIT_PAGE = 0;
    boolean mRequesting = false;
    private SimplyAdapter mAdapter;
    private boolean mHasMore = true;
    private View mLlEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_load_more);
        mLlEmpty = findViewById(R.id.ll_empty);
        mRecyclerView = findViewById(R.id.recycler_view);
        initRecyclerView();
        requestData();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this
                , RecyclerView.VERTICAL, false));
        mAdapter = new SimplyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // SwipeRefreshLayout 的刷新在页面没有被全部填充时也会触发这个,
                // 解决方案：加载了第一页继续加载第二页，使mHasMore为false，或者填充全部页面。
                if (mHasMore && ((LinearLayoutManager) mRecyclerView.
                        getLayoutManager()).findLastVisibleItemPosition()
                        == mAdapter.getItemCount() - 1) {
                    requestData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void requestData() {
        if (!mRequesting) {
            mRequesting = true;
        } else {
            return;
        }
        if (mCurrentPage == INIT_PAGE) {
            showLoading();
        }
        // request by page
        ArrayList<SimpleInfo> simpleInfos = mockData(mCurrentPage);
        // 请求回调
        onGetData(simpleInfos);
    }

    private void onGetData(ArrayList<SimpleInfo> simpleInfos) {
        if (simpleInfos == null || simpleInfos.size() != 0) {
            mHasMore = false;
            if (mCurrentPage == INIT_PAGE) {
                mLlEmpty.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            if (mCurrentPage == INIT_PAGE) {
                mAdapter.clearData();
            }
            mLlEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mAdapter.addData(simpleInfos);
        mRequesting = false;
        dismissLoading();
        mCurrentPage++;
    }

    //上拉刷新 或者 换一种请求渠道
    public void resetRequestStatus() {
        mCurrentPage = INIT_PAGE;
        mRequesting = false;
        mHasMore = true;
        if (mAdapter.getItemCount() > 0) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    public static class SimplyAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
        private OnItemClickListener mItemClickLis;
        public ArrayList<SimpleInfo> mSimpleInfos = new ArrayList<>();

        SimplyAdapter(OnItemClickListener lis) {
            mItemClickLis = lis;
        }

        SimplyAdapter() {
        }

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_simple, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            holder.onBindViewHolder(mSimpleInfos.get(position));
        }

        @Override
        public int getItemCount() {
            return mSimpleInfos.size();
        }

        public void addData(ArrayList<SimpleInfo> simpleInfos) {
            mSimpleInfos.addAll(simpleInfos);
            notifyDataSetChanged();
        }

        public void clearData() {
            mSimpleInfos.clear();
        }


        public static class EmptyViewHolder extends RecyclerView.ViewHolder {
            public EmptyViewHolder(View view) {
                super(view);
            }
        }

        //多个ViewHolder使用同一个接口
        //public class OrderMainAdapter extends RecyclerView.Adapter
        //        <OrderMainAdapter.CommonViewHolderInterface<OrderListInfoResponse.OrderInfo>> {
        public static abstract class CommonViewHolderInterface<T> extends RecyclerView.ViewHolder {
            public CommonViewHolderInterface(View itemView) {
                super(itemView);
            }

            abstract void bindView(T listInflateData, OnItemClickListener<T> listener);
        }

        public interface OnItemClickListener<T> {
            void onClick(T t);
        }
    }

    public static class SimpleInfo {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
        }

        public void onBindViewHolder(SimpleInfo simpleInfo) {
            mTvName.setText(simpleInfo.getName());
        }
    }


    private ArrayList<SimpleInfo> mockData(int pageNum) {
        ArrayList<SimpleInfo> simpleInfos = new ArrayList();
        if (pageNum != 5)
            for (int i = pageNum * 10; i < pageNum * 10 + 15; i++) {
                SimpleInfo si = new SimpleInfo();
                si.setName("q_p~ " + i);
                simpleInfos.add(si);
            }

        return simpleInfos;
    }

}
