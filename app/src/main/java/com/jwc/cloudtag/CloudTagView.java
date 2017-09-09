package com.jwc.cloudtag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jingwc on 2017/7/6.
 */

public class CloudTagView extends ViewGroup {

    public CloudTagView(Context context) {
        super(context);
    }

    public CloudTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CloudTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取宽度测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 获取高度测量模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 获取宽度大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 获取高度大小
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 测量子view宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置给此当前控件的宽高
        int width = 0;
        int height = 0;
        // 获取子view数量
        int childCount = getChildCount();
        // 子view的宽高
        int childWidth;
        int childHeight;

        MarginLayoutParams childParams;

        View childView;

        // 遍历view计算宽度
        for (int i = 0; i < childCount; i++) {
            childView = getChildAt(i);
            // 获取view的宽度
            childWidth = childView.getMeasuredWidth();
            // 获取view的Params
            childParams = (MarginLayoutParams) childView.getLayoutParams();
            // 获取所有view的总宽度
            width = width + childWidth + childParams.leftMargin + childParams.rightMargin;
        }
        // 如果总宽度超过系统测量宽度则使用系统测量宽度
        width = Math.min(width, widthSize);

        // 用于记录一行子view中的最大高度,考虑一行view中高度不一致的问题
        int childMaxH = 0;
        // 用于记录一行的宽度
        int lineWidth = 0;

        // 遍历view测量高度
        for (int i = 0; i < childCount; i++) {
            childView = getChildAt(i);
            // 获取view的高度
            childHeight = childView.getMeasuredHeight();
            // 获取view的宽度
            childWidth = childView.getMeasuredWidth();
            // 获取params
            childParams = (MarginLayoutParams) childView.getLayoutParams();

            // 判断是否超过一行的宽度
            if ((lineWidth + childWidth + childParams.leftMargin + childParams.rightMargin) > width) {
                // 记录一行的高度
                height += childMaxH;
                // 最大高度重置为0,进行进一行最大高度的记录
                childMaxH = 0;
                // lineWidth 等于下一行第一个view的宽度
                lineWidth = childWidth + childParams.leftMargin + childParams.rightMargin;
            } else {
                // 不到一行的宽度进行增加
                lineWidth = lineWidth + childWidth + childParams.leftMargin + childParams.rightMargin;
            }
            // 判断当前view的高度是否大于最大高度的view
            if ((childHeight + childParams.topMargin + childParams.bottomMargin) > childMaxH) {
                // 将大于当前高度的view的高度和外边距进行赋值
                childMaxH = (childHeight + childParams.topMargin + childParams.bottomMargin);
            }
        }
        // 加入最后一行的高度
        height += childMaxH;

        // 如果总高度超过系统测量高度则使用系统测量的高度
        height = Math.min(height, heightSize);

        // 设置当前控件的size
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY ? widthSize : width),
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 获取当前View的宽度
        int viewGroupWidth = getMeasuredWidth();
        // 绘制水平坐标点
        int x = left;
        // 绘制垂直坐标点
        int y = top;
        // 记录一行中最大高度
        int childMaxH = 0;
        // 获取所有的子view数量
        int childCount = getChildCount();

        MarginLayoutParams childParams;


        for (int i = 0; i < childCount; i++) {
            // 获取当前子view
            View childView = getChildAt(i);
            // 获取当前子view的宽高
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();

            // 获取当前子view设置的params
            childParams = (MarginLayoutParams) childView.getLayoutParams();

            // 判断绘制当前子view是否超出父容器的宽度
            if (x + width + childParams.leftMargin + childParams.rightMargin > viewGroupWidth) {
                // 重新开始一行
                x = left;
                // 记录一行中的最大高度
                y += childMaxH;
                // 重置为0
                childMaxH = 0;
            }
            // 设置子view的布局位置
            childView.layout(x + childParams.leftMargin, y + childParams.topMargin, x + width + childParams.rightMargin, y + height + childParams.bottomMargin);
            // 记录水平绘制坐标的位置
            x += width + childParams.leftMargin + childParams.rightMargin;

            // 判断一行中的最大高度
            if ((height + childParams.topMargin + childParams.bottomMargin) > childMaxH) {
                childMaxH = (height + childParams.topMargin + childParams.bottomMargin);
            }
        }
    }

}
