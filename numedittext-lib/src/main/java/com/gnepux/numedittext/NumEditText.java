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
 * --------------------
 * | - |   130    | + |
 * --------------------
 *
 * API list:
 *      1.void setNum(String num) 设置数字
 *      2.String getNumString()     获取数字
 *      3.Double getNumDouble()     获取数字
 *      4.void setEnabled(boolean enabled)   设置控件是否可用
 *      5.void add(int num, boolean enableAnimation)   加
 *      6.void minus(int num, boolean enableAnimation)   减
 *      7.void setOnNumChangedListener(OnNumChangedListener l) 设置数量变动的监听
 *      8.void shake()	摇晃动画
 *      9.void setInputType(int type)	设置InputType标识
 *      10.void enableInputDecimal(boolean canInputDecimal)	设置弹窗是否可以输入小数
 *      11.void setMaxNum(String maxNum)	设置最大值
 *      12.void setMinNum(String minNum)    设置最小值
 */

public class NumEditText extends LinearLayout implements View.OnClickListener {

    // 放大比率
    private float SCALE_RATIO = 1.3f;

    // 放大动画持续时间 ms
    private long SCALE_DURATION = 200;

    // 摇晃动画一秒晃动的次数
    private int SHAKE_PER_SECOND = 5;

    // 摇晃动画持续时间 ms
    private long SHAKE_DURATION = 500;

    // EditText inputType
    private int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;   //可以输入小数
//    private int inputType = InputType.TYPE_CLASS_NUMBER;  // 不能输入小数

    // 默认最小值为1
    private double MIN_NUM = 1;

    // 默认最大值为MAX_VALUE
    private double MAX_NUM = Double.MAX_VALUE;

    private Button btn_min;

    private Button btn_add;

    private EditText numEditText;

    private View numEditView;

    private OnNumChangedListener onNumChangedListener = null;

    public interface OnNumChangedListener {

        void onAddClick();

        void onMinusClick();

        void onEditChange();

        // TODO:手动输入时的监听
        // void onInputNumChanged(int num);
    }

    TextWatcher mWatcher = new TextWatcher() {
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
        numEditView = LayoutInflater.from(context).inflate(R.layout.custom_view_num_edittext, this);
        initWidget();   // 初始化控件
        addListener();  // 加监听
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        numEditText = (EditText) numEditView.findViewById(R.id.num_edittext);
        btn_add = (Button) numEditView.findViewById(R.id.btn_add);
        btn_min = (Button) numEditView.findViewById(R.id.btn_min);
        if (inputType == (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)) {
            numEditText.setText(String.valueOf(MIN_NUM));
        } else {
            numEditText.setText(String.valueOf((int)MIN_NUM));
        }

        btn_min.setBackgroundResource(R.drawable.num_reduce_icon_disable);
        btn_min.setEnabled(false);
        numEditText.addTextChangedListener(mWatcher);
    }

    /**
     * 加监听
     */
    private void addListener() {
        numEditText.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_min.setOnClickListener(this);
    }

    /**
     * @param valueStr
     * @param type     0代表加+ 1代表减-
     */
    private void scaleEditText(final String valueStr, final int type) {

        //放大动画
        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, SCALE_RATIO, 1.0f, SCALE_RATIO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //放大动画
        scaleAnimation.setDuration(SCALE_DURATION);
        scaleAnimation.setStartOffset(0);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                numEditText.setText(valueStr);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (type == 0) {
                    if (onNumChangedListener != null) {
                        onNumChangedListener.onAddClick();
                    }
                } else if (type == 1) {
                    if (getNumDouble() <= MAX_NUM) {
                        setNormalStatus();
                    }
                    if (onNumChangedListener != null) {
                        onNumChangedListener.onMinusClick();
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

                    // 控制数量不能为0
                    if (Double.parseDouble(result) > 0) {
                        numEditText.setText(result);
                    } else {
                        Toast.makeText(getContext(), "数量不能为0", Toast.LENGTH_SHORT).show();
                    }
                    if (onNumChangedListener != null) {
                        onNumChangedListener.onEditChange();
                    }
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    /**
     * 设置成警告状态，使用红边框
     */
    private void setAlertStatus() {
        numEditText.setBackgroundResource(R.drawable.red_xu_kuang);
    }

    /**
     * 设置成普通状态，使用普通灰边框
     */
    private void setNormalStatus() {
        numEditText.setBackgroundResource(R.drawable.xu_kuang);
    }

    //=============================================================================================
    // public method

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
     * 获取数字（double）
     *
     * @return
     */
    public double getNumDouble() {
        return Double.valueOf(numEditText.getText().toString());
    }

    /**
     * 设置控件是否可用
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        numEditText.setEnabled(enabled);
        btn_add.setEnabled(enabled);
        btn_min.setEnabled(enabled);
    }

    /**
     * 加
     * @param num 加数
     * @param enableAnimation 设置是否显示动画
     */
    public void add(int num, boolean enableAnimation) {
        String valueStr = MathUtils.count(numEditText.getText().toString(), num + "", "+");

        if (enableAnimation) {
            scaleEditText(valueStr, 0);
        } else {
            numEditText.setText(valueStr);
            if (onNumChangedListener != null) {
                onNumChangedListener.onAddClick();
            }
        }
    }

    /**
     * 减
     * @param num 减数
     * @param enableAnimation 设置是否显示动画
     */
    public void minus(int num, boolean enableAnimation) {
        String valueStr = MathUtils.count(numEditText.getText().toString(), num + "", "-");

        if (Double.parseDouble(valueStr) < MIN_NUM) {
            if (inputType == (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL )) {
                valueStr = String.valueOf(MIN_NUM);
            } else if (inputType == InputType.TYPE_CLASS_NUMBER) {
                valueStr = String.valueOf((int)MIN_NUM);
            }
        }

        if (enableAnimation) {
            scaleEditText(valueStr, 1);
        } else {
            numEditText.setText(valueStr);
            if (onNumChangedListener != null) {
                onNumChangedListener.onMinusClick();
            }
        }
    }

    /**
     * 设置数量变动的监听
     * @param l
     */
    public void setOnNumChangedListener(OnNumChangedListener l) {
        onNumChangedListener = l;
    }

    /**
     * 摇晃动画
     */
    public void shake() {
        TranslateAnimation shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
        shakeAnimation.setDuration(SHAKE_DURATION); //持续半秒
        shakeAnimation.setInterpolator(new CycleInterpolator(SHAKE_PER_SECOND)); //一秒晃5下
        shakeAnimation.setStartOffset(0);
//      numEditText.setAnimation(shakeAnimation);
        shakeAnimation.setAnimationListener(new Animation.AnimationListener() {
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
//      shakeAnimation.start();
        numEditText.startAnimation(shakeAnimation);
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

    /**
     * 设置最大值
     * @param maxNum
     */
    public void setMaxNum(String maxNum) {
        if (Double.parseDouble(maxNum) < MIN_NUM) {
            MAX_NUM = MIN_NUM;
        }
        this.MAX_NUM = Double.parseDouble(maxNum);
    }

    /**
     * 设置最小值
     * @param minNum
     */
    public void setMinNum(String minNum) {
        if (Double.parseDouble(minNum) > MAX_NUM) {
            MIN_NUM = MAX_NUM;
        }
        this.MIN_NUM = Double.parseDouble(minNum);

    }

}
