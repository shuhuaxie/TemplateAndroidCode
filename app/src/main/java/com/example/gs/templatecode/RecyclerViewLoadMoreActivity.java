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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_load_more);
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
                if (((LinearLayoutManager) mRecyclerView.
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
        }
        if (mCurrentPage == INIT_PAGE) {
            showLoading();
        }
        // request by page
        mAdapter.addData(mockData());
        dismissLoading();
        mCurrentPage++;
    }


    public static class SimplyAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
        public ArrayList<SimpleInfo> mSimpleInfos = new ArrayList<>();

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

            abstract void bindView(T listInflateData);
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


    private ArrayList<SimpleInfo> mockData() {
        ArrayList<SimpleInfo> simpleInfos = new ArrayList();
        for (int i = mCurrentPage * 10; i < mCurrentPage * 10 + 15; i++) {
            SimpleInfo si = new SimpleInfo();
            si.setName("q_p~ " + i);
            simpleInfos.add(si);
        }

        return simpleInfos;
    }
}
