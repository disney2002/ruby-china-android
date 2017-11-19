package com.testerhome.android;

/**
 * Created by lihuazhang on 2017/5/15.
 */

/**
 * SignIn Activity for login
 */

public class SignInActivity extends EmptyActivity {
    /**
     * when back is pressed,
     * return with result {@link BaseActivity#RESULT_SIGN_IN_CANCELED}
     */
    @Override
    public void onBackPressed() {
        setResult(BaseActivity.RESULT_SIGN_IN_CANCELED);
        finish();
    }
}