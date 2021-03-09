package com.example.bnr7_with_butterknife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * Class that helps us to get detailed photo of Crime in {@link CrimeFragment}
 */
public class ImageDialog extends DialogFragment {

    private static final String KEY_TO_ARGS="Key_to_args";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ImageView imageView=new ImageView(getActivity());
        imageView.setImageBitmap(BitmapFactory.decodeFile(getArguments().getString(KEY_TO_ARGS)));

        return new AlertDialog.Builder(getActivity())
                .setView(imageView)
                .setTitle(getString(R.string.photo_details_title))
                .create();
    }

    public static ImageDialog newInstance(String path){
        ImageDialog fragment=new ImageDialog();
        Bundle args=new Bundle();
        args.putString(KEY_TO_ARGS,path);
        fragment.setArguments(args);
        return fragment;
    }
}
