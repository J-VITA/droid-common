package kr.skyware.commons.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import kr.skyware.commons.R;

public class ListDialog extends Dialog implements View.OnClickListener {

    private TextView titleTextView;

    private ListView listView;

    private Button button1;
//    private Button button2;

    private OnClickListener positiveButtonListener;
    private OnClickListener negativeButtonListener;

    private int selectedItemPostion = -1;

    private ListDialog(Context context,int mode) {
        this(context, 0, mode);
    }

    private ListDialog(Context context, int theme, int mode) {
        super(context, theme);

        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 노타이틀
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 테두리 투명 처리

        setContentView(getLayoutInflater().inflate(R.layout.view_list_dialog, null)); // 리스트 다이얼로그를 콘텐트뷰에 세팅

        titleTextView = findViewById(R.id.title); //
        listView = findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        button1 = findViewById(R.id.pos_button);
//        button2 = (Button) findViewById(R.id.button2);

    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getResources().getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        titleTextView.setText(title);
    }

    public void setListItems(int itemsId, int selectedItemPostion, int mode) {
        setListItems(getContext().getResources().getStringArray(itemsId), selectedItemPostion, mode);
    }

    public void setListItems(CharSequence[] items, int selectedItemPostion, int mode) {
        ArrayAdapter<CharSequence> adapter = null;
        switch (mode){
            case 1:
                adapter = new ArrayAdapter<CharSequence>(getContext(),
                        R.layout.simple_list_item_single_choice_checkbox, items);

                listView.setAdapter(adapter);
                break;
            case 0:
            default:
                adapter = new ArrayAdapter<CharSequence>(getContext(),
                        R.layout.support_simple_spinner_dropdown_item, items);
                listView.setAdapter(adapter);
        }

        listView.setItemChecked(selectedItemPostion, true);

        this.selectedItemPostion = selectedItemPostion;
    }

    public CharSequence getListItem(int position) {
        return (CharSequence) listView.getItemAtPosition(position);
    }

    public int getSelectedItemPostion() {
        return selectedItemPostion;
    }

    public void setButton(int whichButton, CharSequence buttonText, OnClickListener buttonListener) {
        switch (whichButton) {
            case BUTTON_POSITIVE:
                button1.setText(buttonText);
                button1.setVisibility(View.VISIBLE);
                button1.setOnClickListener(this);
                positiveButtonListener = buttonListener;
                break;

//            case BUTTON_NEGATIVE:
//                button2.setText(buttonText);
//                button2.setVisibility(View.VISIBLE);
//                button2.setOnClickListener(this);
//                negativeButtonListener = buttonListener;
//                break;

            default:
                break;
        }
    }

    public void setButtonBackGround(Drawable drawable) {
        button1.setBackground(drawable);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pos_button) {
            selectedItemPostion = listView.getCheckedItemPosition();

            if (positiveButtonListener != null)
                positiveButtonListener.onClick(this, BUTTON_POSITIVE);
        }
//        else if (id == R.id.button2) {
//            listView.setItemChecked(selectedItemPostion, true);
//
//            if (negativeButtonListener != null)
//                negativeButtonListener.onClick(this, BUTTON_NEGATIVE);
//        }
    }

    public static class Builder {
        private int theme;
        private Drawable drawable;
        private int mode;
        private Context context;

        private CharSequence title;

        private CharSequence positiveButtonText;
//        private CharSequence negativeButtonText;
        private CharSequence[] items;

        private OnClickListener positiveButtonListener;
        private OnClickListener negativeButtonListener;

        private OnCancelListener cancelListener;
        private OnKeyListener keyListener;

        private boolean cancelable = true;

        private int selectedPostion;

        public Builder(Context context, int mode) {
            this(context, 0, mode);
        }

        public Builder(Context context, int theme, int mode) {
            this.theme = theme;
            this.context = context;
            this.mode = mode;
        }

        public Builder setTitle(int titleId) {
            this.title = context.getString(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setListItems(int itemsId, int selectedItemPosition) {
            this.items = context.getResources().getStringArray(itemsId);
            this.selectedPostion = selectedItemPosition;
            return this;
        }

        public Builder setListItems(CharSequence[] items, int selectedItemPosition) {
            this.items = items;
            this.selectedPostion = selectedItemPosition;
            return this;
        }

        public Builder setPositiveButton(int textId, final OnClickListener listener) {
            this.positiveButtonText = context.getString(textId);
            this.positiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            this.positiveButtonText = text;
            this.positiveButtonListener = listener;
            return this;
        }

        public Builder setButtonBackGround(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }
//        public Builder setNegativeButton(int textId, final OnClickListener listener) {
//            this.negativeButtonText = context.getString(textId);
//            this.negativeButtonListener = listener;
//            return this;
//        }
//
//        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
//            this.negativeButtonText = text;
//            this.negativeButtonListener = listener;
//            return this;
//        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.cancelListener = onCancelListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.keyListener = onKeyListener;
            return this;
        }

        public ListDialog create() {
            final ListDialog dialog = new ListDialog(this.context, this.theme, this.mode);

            dialog.setTitle(this.title);
            dialog.setListItems(this.items, this.selectedPostion, this.mode);

            if (positiveButtonText != null){
                dialog.setButton(BUTTON_POSITIVE, positiveButtonText, positiveButtonListener);
            }

            if( null != drawable )dialog.setButtonBackGround(this.drawable);

//            if (negativeButtonText != null)
//                dialog.setButton(BUTTON_NEGATIVE, negativeButtonText, drawable, negativeButtonListener);

            dialog.setOnCancelListener(cancelListener);
            dialog.setOnKeyListener(keyListener);

            dialog.setCancelable(cancelable);

            return dialog;
        }

        public ListDialog show() {
            ListDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        executeDelayed();
    }

    //hide Navigation Bar
    public void executeDelayed() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideNavBar();
            }
        }, 0);
    }

    public void hideNavBar() {
        View v = getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
