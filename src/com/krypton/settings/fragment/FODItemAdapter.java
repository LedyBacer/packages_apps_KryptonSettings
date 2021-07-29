/*
 * Copyright (C) 2021 AOSP-Krypton Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.krypton.settings.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.android.settings.R;
import com.krypton.settings.Utils;

import java.util.List;

public class FODItemAdapter extends Adapter<FODViewHolder> {
    private final Context mContext;
    private final String mSettingKey;
    private final int mItemPadding;
    private Callback mCallback;
    private List<Drawable> mDrawablesList;

    public FODItemAdapter(Context context, String key, int paddingResId) {
        super();
        mContext = context;
        mSettingKey = key;
        mItemPadding = mContext.getResources().getDimensionPixelSize(paddingResId);
    }

    @Override
    public FODViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new FODViewHolder(LayoutInflater.from(mContext).inflate(
            R.layout.fod_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(FODViewHolder viewHolder, final int position) {
        final ImageView imageView = viewHolder.getImageView();
        imageView.setPaddingRelative(mItemPadding, mItemPadding, mItemPadding, mItemPadding);
        imageView.setImageDrawable(mDrawablesList.get(position));
        if (Utils.getSettingInt(mContext, mSettingKey) == position) {
            imageView.setBackgroundResource(R.drawable.btn_checked);
        } else {
            imageView.setBackground(null);
        }
        imageView.setOnClickListener(v -> {
            if (mCallback != null) {
                mCallback.onSelectedItemChanged(position);
            }
            Utils.applySetting(mContext, mSettingKey, position);
            notifyDataSetChanged();
        });
        imageView.setOnLongClickListener(v -> mCallback != null ?
            mCallback.onItemLongClicked(position) : false);
    }

    @Override
    public int getItemCount() {
        return mDrawablesList == null ? 0 : mDrawablesList.size();
    }

    public void setDrawablesList(List<Drawable> list) {
        mDrawablesList = list;
    }

    public void registerCallback(Callback callback) {
        mCallback = callback;
    }

    public void unregisterCallback() {
        mCallback = null;
    }

    interface Callback {
        void onSelectedItemChanged(int newIndex);
        default boolean onItemLongClicked(int index) {
            return false;
        }
    }
}