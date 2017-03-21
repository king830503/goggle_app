package com.example.goggle_vol3;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ColorPickerDialog extends Dialog {
	private final boolean debug = true;
	private final String TAG = "ColorPicker";
	
	Context context;
	private String title;//����
	private int mInitialColor;//����
    private OnColorChangedListener mListener;

	/**
     * ����暺
     * @param context
     * @param title 撖寡�����
     * @param listener ����
     */
    public ColorPickerDialog(Context context, String title, 
    		OnColorChangedListener listener) {
    	this(context, Color.BLACK, title, listener);
    	
    }
    
    /**
     * 
     * @param context
     * @param initialColor ����
     * @param title ����
     * @param listener ����
     */
    public ColorPickerDialog(Context context, int initialColor, 
    		String title, OnColorChangedListener listener) {
        super(context);
        this.context = context;
        mListener = listener;
        mInitialColor = Color.BLACK;
        this.title = title;        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager manager = getWindow().getWindowManager();
		int height = (int) (manager.getDefaultDisplay().getHeight() * 0.5f);
		int width = (int) (manager.getDefaultDisplay().getWidth() * 0.7f);
		ColorPickerView myView = new ColorPickerView(context, height, width);
        
		setContentView(myView);
        setTitle("選擇完喜愛顏色後點圓環中間");
        
        
    }
    
    private class ColorPickerView extends View {
    	private Paint mPaint;//皜���蝚�
    	private Paint mCenterPaint;//銝剝��蝚�
    	private Paint mLinePaint;//���瑪�蝚�
    	private Paint mRectPaint;//皜���蝚�
    	
    	private Shader rectShader;//皜��������
    	private float rectLeft;//皜���椰x����
    	private float rectTop;//皜���x����
    	private float rectRight;//皜��������
    	private float rectBottom;//皜��������
        
    	private final int[] mCircleColors;//皜��憸
    	private final int[] mRectColors;//皜����
    	
    	private int mHeight;//View擃�
    	private int mWidth;//View摰�
    	private float r;//������(paint銝剝)
    	private float centerRadius;//銝剖�����
    	
    	private boolean downInCircle = true;//��皜�銝�
    	private boolean downInRect;//��皜�����
    	private boolean highlightCenter;//擃漁
    	private boolean highlightCenterLittle;//敺桐漁
    	
		public ColorPickerView(Context context, int height, int width) {
			super(context);
			this.mHeight = height - 36;
			this.mWidth = width;
			setMinimumHeight(height - 36);
			setMinimumWidth(width);
			
			//皜����
	    	mCircleColors = new int[] {0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 
	    			0xFF00FFFF, 0xFF00FF00,0xFFFFFF00, 0xFFFF0000};
	    	Shader s = new SweepGradient(0, 0, mCircleColors, null);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(50);
            r = width / 2 * 0.7f - mPaint.getStrokeWidth() * 0.5f;
            
            //銝剖���
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(mInitialColor);
            mCenterPaint.setStrokeWidth(5);
            centerRadius = (r - mPaint.getStrokeWidth() / 2 ) * 0.7f;
            
            //颲寞��
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(Color.parseColor("#72A1D1"));
            mLinePaint.setStrokeWidth(4);
            
            //暺皜��
            mRectColors = new int[]{0xFF000000, mCenterPaint.getColor(), 0xFFFFFFFF};
            mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRectPaint.setStrokeWidth(5);
            rectLeft = -r - mPaint.getStrokeWidth() * 0.5f;
            rectTop = r + mPaint.getStrokeWidth() * 0.5f + 
            		mLinePaint.getStrokeMiter() * 0.5f + 15;
            rectRight = r + mPaint.getStrokeWidth() * 0.5f;
            rectBottom = rectTop + 50;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			//蝘餃銝剖��
            canvas.translate(mWidth / 2, mHeight / 2 - 50);
            //�銝剖���
            canvas.drawCircle(0, 0, centerRadius,  mCenterPaint);
            //���蝷箔葉敹�����
            if (highlightCenter || highlightCenterLittle) {
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);
                if(highlightCenter) {
                	mCenterPaint.setAlpha(0xFF);
                }else if(highlightCenterLittle) {
                	mCenterPaint.setAlpha(0x90);
                }
                canvas.drawCircle(0, 0, 
                		centerRadius + mCenterPaint.getStrokeWidth(),  mCenterPaint);
                
                mCenterPaint.setStyle(Paint.Style.FILL);
                mCenterPaint.setColor(c);
            }
            //���
            canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
            //�暺皜���
            if(downInCircle) {
            	mRectColors[1] = mCenterPaint.getColor();
            }
            rectShader = new LinearGradient(rectLeft, 0, rectRight, 0, mRectColors, null, Shader.TileMode.MIRROR);
            mRectPaint.setShader(rectShader);
            canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, mRectPaint);
            float offset = mLinePaint.getStrokeWidth() / 2;
            canvas.drawLine(rectLeft - offset, rectTop - offset * 2, 
            		rectLeft - offset, rectBottom + offset * 2, mLinePaint);//撌�
            canvas.drawLine(rectLeft - offset * 2, rectTop - offset, 
            		rectRight + offset * 2, rectTop - offset, mLinePaint);//銝�
            canvas.drawLine(rectRight + offset, rectTop - offset * 2, 
            		rectRight + offset, rectBottom + offset * 2, mLinePaint);//�
            canvas.drawLine(rectLeft - offset * 2, rectBottom + offset, 
            		rectRight + offset * 2, rectBottom + offset, mLinePaint);//銝�
			super.onDraw(canvas);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX() - mWidth / 2;
            float y = event.getY() - mHeight / 2 + 50;
            boolean inCircle = inColorCircle(x, y, 
            		r + mPaint.getStrokeWidth() / 2, r - mPaint.getStrokeWidth() / 2);
            boolean inCenter = inCenter(x, y, centerRadius);
            boolean inRect = inRect(x, y);
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	downInCircle = inCircle;
                	downInRect = inRect;
                	highlightCenter = inCenter;
                case MotionEvent.ACTION_MOVE:
                	if(downInCircle && inCircle) {//down��皜�����, 銝ove銋皜�����
                		float angle = (float) Math.atan2(y, x);
                        float unit = (float) (angle / (2 * Math.PI));
                        if (unit < 0) {
                            unit += 1;
                        }
	               		mCenterPaint.setColor(interpCircleColor(mCircleColors, unit));
	               		if(debug) Log.v(TAG, "�����, ����: " + x + "," + y);
                	}else if(downInRect && inRect) {//down�皜�����, 銝ove銋皜�����
                		mCenterPaint.setColor(interpRectColor(mRectColors, x));
                	}
                	if(debug) Log.v(TAG, "[MOVE] 擃漁: " + highlightCenter + "敺桐漁: " + highlightCenterLittle + " 銝剖��: " + inCenter);
                	if((highlightCenter && inCenter) || (highlightCenterLittle && inCenter)) {//��銝剖���, 敶�宏��銝剖���
                		highlightCenter = true;
                		highlightCenterLittle = false;
                	} else if(highlightCenter || highlightCenterLittle) {//���銝剖���, 敶�宏�銝剖���
                		highlightCenter = false;
                		highlightCenterLittle = true;
                	} else {
                		highlightCenter = false;
                		highlightCenterLittle = false;
                	}
                   	invalidate();
                	break;
                case MotionEvent.ACTION_UP:
                	if(highlightCenter && inCenter) {//���銝剖���, 銝����銝剖���
                		if(mListener != null) {
                			mListener.colorChanged(mCenterPaint.getColor());
                    		ColorPickerDialog.this.dismiss();
                		}
                	}
                	if(downInCircle) {
                		downInCircle = false;
                	}
                	if(downInRect) {
                		downInRect = false;
                	}
                	if(highlightCenter) {
                		highlightCenter = false;
                	}
                	if(highlightCenterLittle) {
                		highlightCenterLittle = false;
                	}
                	invalidate();
                    break;
            }
            return true;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(mWidth, mHeight);
		}

		/**
		 * �������銝�
		 * @param x ����
		 * @param y ����
		 * @param outRadius ��憭���
		 * @param inRadius �������
		 * @return
		 */
		private boolean inColorCircle(float x, float y, float outRadius, float inRadius) {
			double outCircle = Math.PI * outRadius * outRadius;
			double inCircle = Math.PI * inRadius * inRadius;
			double fingerCircle = Math.PI * (x * x + y * y);
			if(fingerCircle < outCircle && fingerCircle > inCircle) {
				return true;
			}else {
				return false;
			}
		}
		
		/**
		 * �����銝剖����
		 * @param x ����
		 * @param y ����
		 * @param centerRadius �����
		 * @return
		 */
		private boolean inCenter(float x, float y, float centerRadius) {
			double centerCircle = Math.PI * centerRadius * centerRadius;
			double fingerCircle = Math.PI * (x * x + y * y);
			if(fingerCircle < centerCircle) {
				return true;
			}else {
				return false;
			}
		}
		
		/**
		 * �����皜�銝�
		 * @param x
		 * @param y
		 * @return
		 */
		private boolean inRect(float x, float y) {
			if( x <= rectRight && x >=rectLeft && y <= rectBottom && y >=rectTop) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * ����銝�
		 * @param colors
		 * @param unit
		 * @return
		 */
		private int interpCircleColor(int colors[], float unit) {
            if (unit <= 0) {
                return colors[0];
            }
            if (unit >= 1) {
                return colors[colors.length - 1];
            }
            
            float p = unit * (colors.length - 1);
            int i = (int)p;
            p -= i;

            // now p is just the fractional part [0...1) and i is the index
            int c0 = colors[i];
            int c1 = colors[i+1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);
            
            return Color.argb(a, r, g, b);
        }
		
		/**
		 * ��������
		 * @param colors
		 * @param x
		 * @return
		 */
		private int interpRectColor(int colors[], float x) {
			int a, r, g, b, c0, c1;
        	float p;
        	if (x < 0) {
        		c0 = colors[0]; 
        		c1 = colors[1];
        		p = (x + rectRight) / rectRight;
        	} else {
        		c0 = colors[1];
        		c1 = colors[2];
        		p = x / rectRight;
        	}
        	a = ave(Color.alpha(c0), Color.alpha(c1), p);
        	r = ave(Color.red(c0), Color.red(c1), p);
        	g = ave(Color.green(c0), Color.green(c1), p);
        	b = ave(Color.blue(c0), Color.blue(c1), p);
        	return Color.argb(a, r, g, b);
		}
		
		private int ave(int s, int d, float p) {
            return s + Math.round(p * (d - s));
        }
    }
    
    /**
     * ����
     * @author <a href="clarkamx@gmail.com">LynK</a>
     * 
     * Create on 2012-1-6 銝��8:21:05
     *
     */
    public interface OnColorChangedListener {
    	/**
    	 * ����
    	 * @param color �葉���
    	 */
        void colorChanged(int color);
    }
    
    public String getTitle() {
		return title;
	}

	public void setTitle() {
		this.title = title;
		
	}

	public int getmInitialColor() {
		return mInitialColor;
	}

	public void setmInitialColor(int mInitialColor) {
		this.mInitialColor = mInitialColor;
	}

	public OnColorChangedListener getmListener() {
		return mListener;
	}

	public void setmListener(OnColorChangedListener mListener) {
		this.mListener = mListener;
	}
}
