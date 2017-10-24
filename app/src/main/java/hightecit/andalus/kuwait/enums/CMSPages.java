package hightecit.andalus.kuwait.enums;

import hightecit.andalus.kuwait.common.MyApplication;
import hightecit.andalus.kuwait.R;
import hightecit.andalus.kuwait.model.Label;

/**
 * Created by HTISPL on 8/9/2017.
 */

public enum CMSPages {
    TermsAndCondition(MyApplication.getInstance().getString(R.string.terms_conditions)),
    AboutApp(Label.getLabel(R.string.about_app));
    private String value;

    CMSPages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
