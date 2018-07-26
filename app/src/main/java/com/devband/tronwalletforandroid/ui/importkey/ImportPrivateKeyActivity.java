package com.devband.tronwalletforandroid.ui.importkey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportPrivateKeyActivity extends CommonActivity implements ImportPrivateKeyView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_private_key)
    EditText mInputPrivateKey;

    @BindView(R.id.input_password)
    EditText mInputPassword;

    @BindView(R.id.btn_create_wallet)
    Button mCreateWalletButton;

    @BindView(R.id.agree_lost_password)
    CheckBox mChkLostPassword;

    @BindView(R.id.agree_lost_password_recover)
    CheckBox mChkLostPasswordRecover;

    private boolean mCheckPrivateKey;

    private boolean mCheckPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_create_wallet);
        }

        mPresenter = new ImportPrivateKeyPresenter(this);
        mPresenter.onCreate();

        mInputPrivateKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // todo - check invalid private key
                if (!TextUtils.isEmpty(s.toString())) {
                    mCheckPrivateKey = true;
                } else {
                    mCheckPrivateKey = false;
                }

                checkInputRequired();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= WalletAppManager.MIN_PASSWORD_LENGTH) {
                    mCheckPassword = true;
                } else {
                    mCheckPassword = false;
                }

                checkInputRequired();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkInputRequired() {
        if (mCheckPassword && mCheckPrivateKey) {
            mCreateWalletButton.setEnabled(true);
        } else {
            mCreateWalletButton.setEnabled(false);
        }
    }

    @OnClick(R.id.btn_create_wallet)
    public void onCreateAccountClick() {
        if (!mChkLostPassword.isChecked()
                || !mChkLostPasswordRecover.isChecked()) {
            Toast.makeText(ImportPrivateKeyActivity.this, getString(R.string.need_all_agree),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(null, getString(R.string.loading_msg));
    }
}
