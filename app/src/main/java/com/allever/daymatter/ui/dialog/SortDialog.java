package com.allever.daymatter.ui.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allever.daymatter.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Allever on 18/5/28.
 */

public class SortDialog extends DialogFragment {

    private static OptionListener mOptionListener;

    Unbinder unbinder;

    public static SortDialog newInsance(OptionListener optionListener) {
        SortDialog sortDialog = new SortDialog();
        sortDialog.setCancelable(true);
        mOptionListener = optionListener;
        return sortDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sort, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.id_dialog_sort_rb_life,
            R.id.id_dialog_sort_rb_work,
            R.id.id_dialog_sort_rb_memory_day,
            R.id.id_dialog_sort_tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_dialog_sort_rb_life:
                if (mOptionListener != null) {
                    mOptionListener.onItemClick(this, getString(R.string.sort_life), 1);
                }
                break;
            case R.id.id_dialog_sort_rb_work:
                if (mOptionListener != null) {
                    mOptionListener.onItemClick(this, getString(R.string.sort_work), 2);
                }
                break;
            case R.id.id_dialog_sort_rb_memory_day:
                if (mOptionListener != null) {
                    mOptionListener.onItemClick(this, getString(R.string.sort_memory_day), 3);
                }
                break;
            case R.id.id_dialog_sort_tv_cancel:
                if (mOptionListener != null){
                    mOptionListener.onCancel(this);
                }
                break;
            default:
                break;
        }
    }

    public interface OptionListener {
        void onItemClick(DialogFragment dialog, String sortName, int sortId);

        void onCancel(DialogFragment dialog);
    }

}
