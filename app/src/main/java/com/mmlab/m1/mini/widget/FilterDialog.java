package com.mmlab.m1.mini.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mmlab.m1.mini.MiniActivity;
import com.mmlab.m1.R;
import com.mmlab.m1.mini.helper.Preset;
import com.rey.material.widget.Spinner;


public class FilterDialog extends DialogFragment {

    public static final String TAG = "FilterDialog";
    private String identifier="all";
    private String media="all";
    private String category="all";

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    public OnChangedListener onChangedListener = null;

    public FilterDialog() {
        // Required empty public constructor
    }

    public interface OnChangedListener {
        void onChanged();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onDetach() {
        super.onDetach();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);
        final Spinner mIdentifier = (Spinner) view.findViewById(R.id.spn_identifier);
        final Spinner mMedia = (Spinner) view.findViewById(R.id.spn_media);
        final Spinner mCategory = (Spinner) view.findViewById(R.id.spn_category);

        final ArrayAdapter<String> identifierAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, getResources().getStringArray(R.array.sorting_identifier));
        identifierAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        mIdentifier.setAdapter(identifierAdapter);
        mIdentifier.setSelection(Preset.loadIndentifierPreferences(getActivity().getApplicationContext()));
        final String arrayIdentifier[] = {"all", "expert", "user", "narrator"};
        mIdentifier.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {

            }
        });

        final ArrayAdapter<String> mediaAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, getResources().getStringArray(R.array.sorting_media));
        mediaAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        mMedia.setAdapter(mediaAdapter);
        mMedia.setSelection(Preset.loadMediaPreferences(getActivity().getApplicationContext()));
        final String arrayMedia[] = {"all", "photo", "movie", "audio", "audio tour"};
        mMedia.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {

            }
        });

        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, getResources().getStringArray(R.array.sorting_category));
        categoryAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        mCategory.setAdapter(categoryAdapter);
        mCategory.setSelection(Preset.loadCategoryPreferences(getActivity().getApplicationContext()));
        final String arrayCategory[] = {"all", "古蹟、歷史建築、聚落", "遺址", "人文景觀", "自然景觀",
                "傳統藝術", "民俗及有關文物", "古物", "食衣住行育樂", "其他"};
        mCategory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {

            }
        });


        return new MaterialDialog.Builder(getActivity())
                .title(R.string.about)
                .customView(view, true)
                .widgetColor(getResources().getColor(R.color.colorPrimary))
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .neutralText(R.string.reset)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                        Preset.saveIndentifierPreferences(getActivity().getApplicationContext(), mIdentifier.getSelectedItemPosition());
                        identifier = arrayIdentifier[mIdentifier.getSelectedItemPosition()];
                        Preset.saveMediaPreferences(getActivity().getApplicationContext(), mMedia.getSelectedItemPosition());
                        media = arrayMedia[mMedia.getSelectedItemPosition()];
                        Preset.saveCategoryPreferences(getActivity().getApplicationContext(), mCategory.getSelectedItemPosition());
                        category =  arrayCategory[mCategory.getSelectedItemPosition()];
//                        if (mCategory.getSelectedItemPosition() == 0) {
//                            category = "all";
//                        }
                        ((MiniActivity) getActivity()).startFilter(identifier, media, category);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        mIdentifier.setSelection(0);
                        mMedia.setSelection(0);
                        mCategory.setSelection(0);
                        Preset.saveIndentifierPreferences(getActivity().getApplicationContext(), 0);
                        Preset.saveMediaPreferences(getActivity().getApplicationContext(), 0);
                        Preset.saveCategoryPreferences(getActivity().getApplicationContext(), 0);
                    }
                })
                .build();
    }
}
