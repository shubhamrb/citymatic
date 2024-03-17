package com.mamits.citymatic.ui.utils.listeners;

import android.view.View;

import com.mamits.citymatic.data.model.form.CustomFieldObject;
import com.mamits.citymatic.data.model.form.ValueModel;
import com.mamits.citymatic.data.model.search.SearchDataModel;

public interface OnItemClickListener {
    void onClick(int pos, View view, CustomFieldObject obj);
    void onClick(int pos, View view, CustomFieldObject obj, String type);
    void onClick(int pos, View view, ValueModel obj);

    void onClick(int pos, View view, SearchDataModel obj);
}
