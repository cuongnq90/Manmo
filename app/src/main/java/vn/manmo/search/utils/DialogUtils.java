package vn.manmo.search.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Gau on 10/01/2017.
 */

public class DialogUtils {

    public static ProgressDialog loadingDialog(final Context context, String text){
        final ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(text);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.setCancelable(false);
        return m_Dialog;
    }
}
