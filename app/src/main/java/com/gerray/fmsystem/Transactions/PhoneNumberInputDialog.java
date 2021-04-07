package com.gerray.fmsystem.Transactions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PhoneNumberInputDialog extends AppCompatDialogFragment {
    private TextInputEditText phoneInput;
    private TextInputLayout phoneInputLayout;
    String phone, sanitizedPhone;

    public interface PhoneInputInterface{
        void onDialogPositiveClick(String number);
    }
    PhoneInputInterface phoneInputInterface;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.phone_input_layout, null);
        builder.setView(view)
                .setTitle("Phone Number Input")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phone = phoneInput.getText().toString().trim();
                        if (!TextUtils.isEmpty(phone)){
                            sanitizedPhone = Constants.sanitizePhoneNumber(phone);
                            phoneInputInterface.onDialogPositiveClick(sanitizedPhone);
                        }else{
                            phoneInputLayout.setError("A  phone number is required");
                        }
                    }
                });
        phoneInput = view.findViewById(R.id.phone_input);
        phoneInputLayout = view.findViewById(R.id.phone_input_layout);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            phoneInputInterface = (PhoneInputInterface) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }
}