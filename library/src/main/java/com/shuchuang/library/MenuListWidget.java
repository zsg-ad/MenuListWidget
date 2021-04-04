package com.shuchuang.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

/**
 * 描述：自定义菜单列表组合控件，用于个人中心菜单列表等
 *
 * @author zsg
 * @date 2020-12-03 1:47
 */

public class MenuListWidget extends RelativeLayout {

    /**
     * 列表图标
     */
    private ImageView startIcon;
    /**
     * 列表标题
     */
    private TextView title;
    /**
     * 列表输入框
     */
    private AppCompatEditText editText;
    /**
     * 列表内容尾部右箭头
     */
    private ImageView endIcon;
    /**
     * 列表下划线
     */
    private View divider;

    /**
     * xml提供的列表字体内容、颜色、大小
     */
    private String titleText;
    private int titleTextColor;
    private float titleTextSize;
    /**
     * xml提供的图标尺寸大小
     */
    private float iconDrawableSize;
    /**
     * xml提供的列表图标地址
     */
    private int startIconDrawable;
    /**
     * startIcon显隐性
     */
    private boolean startIconVisibility;
    /**
     * 列表输入框显隐性，若开启输入框，则自动隐藏列表startIcon和下划线
     * 并将endIcon显隐设置为invisible
     * 默认为false
     */
    private boolean enableEditText;
    /**
     * editText能否被输入，默认可以
     */
    private boolean enableEditInput;
    /**
     * xml设置的EditText要显示的内容
     */
    private String editContent;
    /**
     * xml设置的EditText要显示的提示内容
     */
    private String editHint;
    /**
     * editText内容显示的位置
     */
    private int editContentGravity;
    /**
     * xml提供的尾部图标地址
     */
    private int endIconDrawable;
    /**
     * endIcon显隐性
     */
    private boolean endIconVisibility;
    /**
     * 下划线显隐性
     */
    private boolean dividerVisibility;
    /**
     * 下划线颜色
     */
    private int dividerColor;


    public MenuListWidget(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public MenuListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm = new DisplayMetrics();
        context.getDisplay().getRealMetrics(dm);
        //绑定组合控件布局
        LayoutInflater.from(context).inflate(R.layout.layout_menu_list_item,this,true);
        //绑定attrs获取XML中设置的值
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MenuListWidget);
        titleText = array.getString(R.styleable.MenuListWidget_titleText);
        titleTextSize = array.getDimension(R.styleable.MenuListWidget_titleTextSize,15 * dm.density);
        titleTextColor = array.getColor(R.styleable.MenuListWidget_titleTextColor, Color.BLACK);
        iconDrawableSize = array.getDimension(R.styleable.MenuListWidget_iconDrawableSize,25f);
        startIconDrawable = array.getResourceId(R.styleable.MenuListWidget_startIconDrawable,0);
        startIconVisibility = array.getBoolean(R.styleable.MenuListWidget_startIconVisibility,true);
        enableEditText = array.getBoolean(R.styleable.MenuListWidget_enableEditText,false);
        enableEditInput = array.getBoolean(R.styleable.MenuListWidget_enableEditInput, true);
        editContent = array.getString(R.styleable.MenuListWidget_setEditContent);
        editHint = array.getString(R.styleable.MenuListWidget_setEditHint);
        editContentGravity = array.getInteger(R.styleable.MenuListWidget_editContentGravity, Gravity.CENTER);
        endIconDrawable = array.getResourceId(R.styleable.MenuListWidget_endIconDrawable,0);
        endIconVisibility = array.getBoolean(R.styleable.MenuListWidget_endIconVisibility,true);
        dividerVisibility = array.getBoolean(R.styleable.MenuListWidget_dividerVisibility,true);
        dividerColor = array.getColor(R.styleable.MenuListWidget_dividerColor,Color.GRAY);
        //回收资源，必须调用
        array.recycle();
    }

    /**
     * 此方法会在所有控件都从XML文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取组合布局控件
        startIcon = findViewById(R.id.img_list_item_icon);
        endIcon = findViewById(R.id.img_list_item_end);
        title = findViewById(R.id.text_list_item_title);
        editText = findViewById(R.id.edit_list_item_content);
        divider = findViewById(R.id.view_list_item_divider);
        //将XML中配置的控件属性设置给对应控件
        setTitleSettings();
        setTitle(titleText);
        if (enableEditText){
            startIcon.setVisibility(GONE);
            endIcon.setVisibility(INVISIBLE);
            divider.setVisibility(GONE);
            editText.setVisibility(VISIBLE);
            //规范标题宽度
            LayoutParams params = new LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMarginStart(24);
            title.setLayoutParams(params);
            setEditSettings();
            setEditEnable(enableEditInput);
            setEditContent(editContent);
        }else {
            setStartIcon();
            setEndIcon();
            setDivider();
        }

    }


    /**
     * 代码设置是否能输入
     *
     * @param enable bool
     */
    public void setEditEnable(boolean enable){
        //输入框不能输入但可监听到点击事件
        editText.setFocusable(enable);
        editText.setCursorVisible(enable);
        editText.setFocusableInTouchMode(enable);
        editText.requestFocus();
    }

    /**
     * 可通过代码设置内容
     *
     * @param content 内容
     */
    public void setEditContent(String content){
        editText.setTextColor(Color.GRAY);
        if (content != null){
            editText.setText(content);
            editText.setSelection(content.length());
        }
    }

    /**
     * 最大输入长度限制
     *
     * @param length 长度
     */
    public void setEditMaxLength(final int length){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str.length() > length){
                    //截取前x位
                    editText.setText(str.substring(0,length));
                    editText.requestFocus();
                    //光标移动到最后
                    editText.setSelection(length);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 代码获取输入框实例
     *
     * @return 输入框
     */
    public AppCompatEditText getEditText(){
        return editText;
    }

    /**
     * 代码获取输入框的内容
     *
     * @return 输入框的内容
     */
    public String getEditContent(){
        return Objects.requireNonNull(editText.getText()).toString();
    }

    /**
     * 代码获取菜单列表头部Icon
     *
     * @return startIcon
     */
    public ImageView getStartIcon(){
        return startIcon;
    }

    /**
     * 代码获取菜单列表尾部Icon
     *
     * @return endIcon
     */
    public ImageView getEndIcon(){
        return endIcon;
    }

    /**
     * 可通过代码设置标题
     *
     * @param titleText 标题
     */
    public void setTitle(String titleText) {
        title.setText(titleText);
    }


    private void setTitleSettings() {
        Log.e("titleTextSize"," = " + titleTextSize);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize);
        title.setTextColor(titleTextColor);
    }

    private void setEditSettings(){
        editText.setGravity(editContentGravity);
        editText.setHint(editHint);
    }

    private void setStartIcon() {
        if (!startIconVisibility) {
            startIcon.setVisibility(GONE);
            return;
        }
        ViewGroup.LayoutParams layoutParams = startIcon.getLayoutParams();
        layoutParams.width = (int) iconDrawableSize;
        layoutParams.height = (int) iconDrawableSize;
        startIcon.setLayoutParams(layoutParams);
        startIcon.setImageResource(startIconDrawable);
    }

    private void setEndIcon() {
        if (!endIconVisibility) {
            endIcon.setVisibility(GONE);
            return;
        }
        ViewGroup.LayoutParams layoutParams = endIcon.getLayoutParams();
        layoutParams.width = (int) iconDrawableSize;
        layoutParams.height = (int) iconDrawableSize;
        endIcon.setLayoutParams(layoutParams);
        endIcon.setImageResource(endIconDrawable);
    }

    private void setDivider() {
        if (!dividerVisibility){
            divider.setVisibility(GONE);
        }else {
            divider.setBackgroundColor(dividerColor);
        }
    }
}
