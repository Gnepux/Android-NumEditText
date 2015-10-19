package com.gnepux.numedittext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gnepux.numedittext.utils.MathUtils;

/**
 * 自定义控件NumEditText
 * Created by Gnepux on 2015/10/19.
 */

public class NumEditText extends LinearLayout implements View.OnClickListener {

    Button btn_min, btn_add;
    EditText numEditText;
    View numEditview;

    // inputType默认不能输入小数
//    int inputType = InputType.TYPE_CLASS_NUMBER;
    int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;

    NumChangedListener numChangedListener = null;

    private static final int MIN_NUM = 1;
    private double mMaxNum = Double.MAX_VALUE;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            double num = Double.parseDouble(s.toString());
            if (num < MIN_NUM) {
                btn_min.setBackgroundResource(R.drawable.num_reduce_icon_disable);
                btn_min.setEnabled(false);
            } else if (num == MIN_NUM) {
                btn_min.setBackgroundResource(R.drawable.num_reduce_icon_disable);
                btn_min.setEnabled(false);
                //numChangedListener.onInputNumChanged(num);
            } else {
                btn_min.setBackgroundResource(R.drawable.num_reduce_icon);
                btn_min.setEnabled(true);
                //numChangedListener.onInputNumChanged(num);
            }

/*            btn_min.setBackgroundResource(R.drawable.num_reduce_icon);
            btn_min.setEnabled(true);

            if (num < MIN_NUM) {
                btn_min.setBackgroundResource(R.drawable.num_reduce_icon_disable);
                btn_min.setEnabled(false);
            }*/
        }
    };

    public NumEditText(Context context) {
        this(context, null);
    }

    public NumEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        numEditview = LayoutInflater.from(context).inflate(R.layout.custom_view_num_edittext, this);
        initWidget();
        addListener();
    }

    private void initWidget() {
        numEditText = (EditText) numEditview.findViewById(R.id.num_edittext);
        btn_add = (Button) numEditview.findViewById(R.id.btn_add);
        btn_min = (Button) numEditview.findViewById(R.id.btn_min);
        numEditText.setText("1.0");
        btn_min.setBackgroundResource(R.drawable.num_reduce_icon_disable);
        btn_min.setEnabled(false);
        numEditText.addTextChangedListener(watcher);
    }

    private void addListener() {
        numEditText.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_min.setOnClickListener(this);
 /*       btn_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 默认显示动画
                add(1, true);
            }
        });

        btn_min.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //默认显示动画
                minus(1, true);
            }
        });*/
    }

    /**
     * 设置数字
     *
     * @param num
     */
    public void setNum(String num) {
        numEditText.setText(num);
    }

    /**
     * 获取数字（String）
     *
     * @return
     */
    public String getNumString() {
        return numEditText.getText().toString();
    }

    /**
     * 获取数字（int）
     *
     * @return
     */
    public double getNumDouble() {
        return Double.valueOf(numEditText.getText().toString());
    }

    /**
     * Disable整个控件
     */
    public void setDisable() {
        numEditText.setEnabled(false);
        btn_add.setEnabled(false);
        btn_min.setEnabled(false);
    }

    /**
     * Enable整个控件
     */
    public void setEnable() {
        numEditText.setEnabled(true);
        btn_add.setEnabled(true);
        btn_min.setEnabled(true);
    }

    /**
     * @param num
     * @param showScaleAnimation 设置是否显示动画
     */
    public void add(int num, boolean showScaleAnimation) {
//        double value = Double.valueOf(numEditText.getText().toString());
//        value += num;
        //numEditText.setText(Integer.toString(value));
 /*       if (value > 0) {
            btn_min.setBackgroundResource(R.drawable.num_reduce_icon);
            btn_min.setEnabled(true);
        }*/

        String valueStr = MathUtils.count(numEditText.getText().toString(), num + "", "+");

        if (showScaleAnimation) {
            scaleEditText(valueStr, 0);
        } else {
            numEditText.setText(valueStr);
            if (numChangedListener != null) {
                numChangedListener.onAddClick();
            }
        }

    }

    /**
     * @param num
     * @param showScaleAnimation 设置是否显示动画
     */
    public void minus(int num, boolean showScaleAnimation) {
//        double value = Double.valueOf(numEditText.getText().toString());
//        value = value - num;

        String valueStr = MathUtils.count(numEditText.getText().toString(), num + "", "-");

        if (Double.parseDouble(valueStr) < 0) {
            valueStr = "0.0";
        }

//        if (value<0) {
//            value = 0;
        //       }
/*        if (value == 0) {
            btn_min.setBackgroundResource(R.drawable.num_reduce_icon_disable);
            btn_min.setEnabled(false);
        }*/
        //numEditText.setText(Integer.toString(value));

        if (showScaleAnimation) {
            scaleEditText(valueStr, 1);
        } else {
            numEditText.setText(valueStr);
            if (numChangedListener != null) {
                numChangedListener.onMinusClick();
            }
        }

    }

    /**
     * @param valueStr
     * @param type     0代表加 1代表减
     */
    private void scaleEditText(final String valueStr, final int type) {

        //放大动画 放大1.3倍
        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //放大动画 持续200毫秒
        scaleAnimation.setDuration(200);
        scaleAnimation.setStartOffset(0);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                numEditText.setText(valueStr);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                if (type == 0) {
                    if (numChangedListener != null) {
                        numChangedListener.onAddClick();
                    }
                } else if (type == 1) {
                    if (getNumDouble() <= mMaxNum) {
                        setNormalStatus();
                    }
                    if (numChangedListener != null) {
                        numChangedListener.onMinusClick();
                    }
                }
            }
        });
        numEditText.startAnimation(scaleAnimation);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.btn_add) {
            // 默认显示动画
            add(1, true);
        } else if (vId == R.id.btn_min) {
            // 默认显示动画
            minus(1, true);
        } else if (vId == R.id.num_edittext) {
            //               numEditText.removeTextChangedListener(watcher);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = inflate(getContext(), R.layout.custom_edit_dialog, null);
            final EditText num = (EditText) dialogView.findViewById(R.id.et_num);
            num.setInputType(inputType);
            num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // 控制editText最长输入3位小数
                    if (s.toString().contains(".")) {
                        String str = s.toString();
                        int dotPos = str.indexOf(".");
                        int len = s.length();

                        if (len - 1 - dotPos > 3) {
                            s.delete(len - 1, len);
                        }
                    }
                }
            });
            builder.setView(dialogView);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                        numEditText.addTextChangedListener(watcher);
                    String result = MathUtils.count(num.getText().toString(), "", "+");

                    // 15010551 控制数量不能为0
                    if (Double.parseDouble(result) > 0) {
                        numEditText.setText(result);
                    } else {
                        Toast.makeText(getContext(), "数量不能为0", Toast.LENGTH_SHORT).show();
                    }
                    if (numChangedListener != null) {
                        numChangedListener.onEditChange();
                    }
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    public interface NumChangedListener {

        void onAddClick();

        void onMinusClick();

        void onEditChange();
        // TODO:手动输入时的监听
        // void onInputNumChanged(int num);
    }

    public void setOnNumChangedListener(NumChangedListener l) {
        numChangedListener = l;
    }

    /**
     * 使用红边框
     */
    public void setAlertStatus() {
        numEditText.setBackgroundResource(R.drawable.red_xu_kuang);
    }

    /**
     * 使用普通灰边框
     */
    public void setNormalStatus() {
        numEditText.setBackgroundResource(R.drawable.xu_kuang);
    }

    public void setShake() {
        //摇摆
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(0, 10, 0, 0);
        alphaAnimation2.setDuration(500); //持续半秒
        alphaAnimation2.setInterpolator(new CycleInterpolator(5)); //一秒晃5下
        numEditText.setAnimation(alphaAnimation2);
        alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAlertStatus();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setNormalStatus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation2.start();
    }

    /**
     * 设置InputType标识
     *
     * @param type
     */
    public void setInputType(int type) {
        inputType = type;
    }

    /*
     * 设置弹窗是否可以输入小数
     */
    public void enableInputDecimal(boolean canInputDecimal) {
        if (canInputDecimal) {
            inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        } else {
            inputType = InputType.TYPE_CLASS_NUMBER;
        }
    }

    public void setMaxNum(String maxNum) {
        if (Double.parseDouble(maxNum) < MIN_NUM) {
            mMaxNum = MIN_NUM;
        }
        this.mMaxNum = Double.parseDouble(maxNum);
/*        if (maxNum < MIN_NUM) {
            this.mMaxNum = MIN_NUM;
        }
        this.mMaxNum = maxNum;*/
    }

}
