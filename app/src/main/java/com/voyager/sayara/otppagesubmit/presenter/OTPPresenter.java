package com.voyager.sayara.otppagesubmit.presenter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.voyager.sayara.R;
import com.voyager.sayara.otppagesubmit.model.OTPModel;
import com.voyager.sayara.otppagesubmit.view.IOTPView;


/**
 * Created by User on 8/30/2017.
 */

public class OTPPresenter implements IOTPControler {

    IOTPView iotpView;
    Context context;
    OTPModel user;
    String edtOPTNo;
    Boolean checkTermsAndConductionBox;


    public OTPPresenter(IOTPView iotpView, Context context) {
        this.iotpView = iotpView;
        initUser();
        this.context = context;
    }

    @Override
    public void setOPTSecondMsg(TextView v) {
        SpannableString ss = new SpannableString(context.getString(R.string.otp_secondtxt_msg));
        final ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(final View textView) {
                iotpView.moveToTermsAndConductionPage();
            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.highLightText));
                textPaint.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan,59,76, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        v.setText(ss);
        v.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void doOTPValidationAndCheck(String edtOPTNo,Boolean checkTermsAndConductionBox) {
        this.edtOPTNo = edtOPTNo;
        this.checkTermsAndConductionBox = checkTermsAndConductionBox;
        Boolean isLoginSuccess = true;
        final int code = user.validateCheckBoxAndOtp(edtOPTNo,checkTermsAndConductionBox);
        if (code!=0) isLoginSuccess = false;
        final Boolean result = isLoginSuccess;
        iotpView.onSubmit(result, code);
    }

    private void initUser(){
        user = new OTPModel(edtOPTNo,checkTermsAndConductionBox);
    }

}
