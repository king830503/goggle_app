package com.example.goggle_vol3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.goggle_vol3.CircleLayout.OnItemClickListener;
import com.example.goggle_vol3.CircleLayout.OnItemSelectedListener;



import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnItemSelectedListener, OnItemClickListener
{
	private Spinner sp;
	private TextView selectedTextView;
	private Handler timehandler = new Handler();
	private int mCenterX = 0;  
    private int mCenterY = 0;
    private int light = 0;
    private int brightness = 0,re_time = 0;
    private int setmode = 0;
    private int q1,q2,s1 = 0,s2 = 0;
    private Button btnColorPicker,b1,b2,b3;
    private ColorPickerDialog dialog;
    private ViewGroup layout_nfc;
    private ViewGroup layout;
    private ViewGroup layout1;  
    private ViewGroup layout2;
    private ViewGroup layout3;
    private ViewGroup layout4;  
    private ViewGroup layout5;
    private ViewGroup layout6;
    private ViewGroup layout7;
    private ViewGroup layout6add;
    private ViewGroup layout5select_one;
    private Rotate3d leftAnimation;  
    private Rotate3d rightAnimation;
    private EditText et_R,et_G,et_B,et_A;
    private SeekBar sb1,sb2,sb3,sb4;
    private static WindowManager wm,wm2;
   	private static WindowManager.LayoutParams params,params2;
	private Button btn_floatView,bt;
	private boolean isAdded = false,isExit,hasTask,login_status = false,light_status=false,isremind = false,wifi=false,checkemail = false;
	private int a=0,r=0,g=0,B=0,a1=0,r1=0,g1=0,B1=0;
	private MyCount mc;
	private RemindCount rc;
	private int re_time1,re_status1,setmode1,brightness1;
	private String str_isadd1,str_isremind1,str_light1;
	private int status = 0,re_status=0,re_cdt_statue =0,now_brightness = 0,sys_brightness=0,position;
	private String sys_lightmode,tname = "",theme_name = "",select_thname = "",login_user,nowtname = "",delete_theme = "",default_theme="";
	private String str_isadd = "false",str_isremind = "false",str_light = "false",today,tot_use_time,nfc_change ="";
	private String th_isadd,th_isremind,th_islight,modify_tname;
	private ArrayList<String> list= new ArrayList<String>();
	private ArrayList<String> list2= new ArrayList<String>();
	private ArrayList<String> theme_val_list= new ArrayList<String>();
	private ArrayList<String> lf_syn_list= new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private boolean startflag=false;
	private int tsec=0,tmin = 0,year,ages,tname_idx,tot_usetime = 0;
	private Configuration mconfig = new Configuration();
	//internet使用的變數
	private Handler mHandler,addHandler,tHandler,themeHandler,synHandler;
	private List<String> param=new ArrayList<String>();
	private String URL="http://140.131.7.239/goggle/member.asmx";
	private String URL1="http://140.131.7.239/goggle/apienvironment.asmx";	
	private String URL2="http://140.131.7.239/goggle/healthset.asmx";	
	private String NAMESPACE="http://tempuri.org/";
	private String METHOD="CheckMember";
	private String METHOD2="addedmember";
	private String METHOD3="addluxvalue";
	private String METHOD4="addedtheme";
	private String METHOD5="getedthemename";
	private String METHOD6="getthemeval";
	private String METHOD7="addtime";
	private String METHOD8= "deledtheme";
	private String METHOD9= "updatetheme";
	private String result5syn,SOAP_ACTION,result,result2,SOAP_ACTION2,SOAP_ACTION3,SOAP_ACTION4,SOAP_ACTION5,SOAP_ACTION6,SOAP_ACTION7,SOAP_ACTION8,SOAP_ACTION9,syn;
	private String ac = "",pwd,Temp,ac2,pwd2,account,password,email;
	Context context;
	

	
	
	Timer timerExit = new Timer();  
	TimerTask task = new TimerTask() {  
	  @Override  
	    public void run() {  
	         isExit = false;  
	         hasTask = true;  
	    }  
	};
	
    NfcAdapter gNfcAdapter;
    private boolean first = true;
    /**
     * 識別目前是否在Resume狀態。
     */
    private boolean gResumed = false;
    /**
     * 識別目前是否在可寫入的模式。
     */
    private boolean gWriteMode = false;
    /**
     * 標記為Log查詢時專用的標籤。
     */
    private static final String TAG = "nfcproject";
    
    PendingIntent gNfcPendingIntent;
    IntentFilter [] gNdefExchangeFilters;
    IntentFilter [] gWriteTagFilters;
    
    EditText gNote;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc);
        layout_nfc = (ViewGroup) findViewById(R.id.lt_nfc);
        // 取得該設備預設的無線感應裝置
        gNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (gNfcAdapter == null) {
            Toast.makeText(this, "不支援NFC感應功能！", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        
        // 取得EditText與Button，並且註冊對應的事件
        findViewById(R.id.write_tag).setOnClickListener(this.gTagWriter);      
        final Button nfc_setting = (Button)findViewById(R.id.nfc_setting);
        gNote = (EditText)findViewById(R.id.note);
        gNote.addTextChangedListener(gTextWatcher);
        
        gNote.setText("a=" + a + ";r=" + r + ";g=" + g + ";b=" + B + ";");
        nfc_setting.setEnabled(true);  
        nfc_setting.setOnClickListener(new Button.OnClickListener() {  
            public void onClick(View v) {
            		lf_syn_list.clear();
    				nfc_change = gNote.getText().toString();
    				for(int i = 0; i< nfc_change.length() ;i++){
    					String s = nfc_change.substring(i,i+1);
    					if(s.equals("=")){
							 s1 = i;
						}else if(s.equals(";")){
							 s2 = i;
						}
    					if(s2 > s1){
    						lf_syn_list.add(nfc_change.substring(s1+1, s2));
							s1 = 0;
							s2 = 0;
							}
    				}
    				a = Integer.parseInt(lf_syn_list.get(0).toString());
    				r = Integer.parseInt(lf_syn_list.get(1).toString());
					g = Integer.parseInt(lf_syn_list.get(2).toString()); 
					B = Integer.parseInt(lf_syn_list.get(3).toString()); 
					if(isAdded){
						wm.removeView(btn_floatView);
						btn_floatView = new Button(getApplicationContext());
		                
		                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
		                
		                wm = (WindowManager) getApplicationContext().getSystemService(
		                        Context.WINDOW_SERVICE);
		                params = new WindowManager.LayoutParams();
		        	
		             // 设置window type
		                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		                /*
		                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		                 * 即拉下通知栏不可见
		                 */

		                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		               

		                // 设置Window flag
		               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
		                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
		                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
		                
		                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
		                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
		                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
		                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		                 
		                 wm.addView(btn_floatView, params);
		                 isAdded = true;
		                   
		                 wm.updateViewLayout(btn_floatView, params);
					}else{
						
						btn_floatView = new Button(getApplicationContext());
		                
		                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
		                
		                wm = (WindowManager) getApplicationContext().getSystemService(
		                        Context.WINDOW_SERVICE);
		                params = new WindowManager.LayoutParams();
		        	
		             // 设置window type
		                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		                /*
		                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		                 * 即拉下通知栏不可见
		                 */

		                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		               

		                // 设置Window flag
		               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
		                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
		                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
		                
		                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
		                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
		                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
		                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		                 
		                 wm.addView(btn_floatView, params);
		                 isAdded = true;
		                   
		                 wm.updateViewLayout(btn_floatView, params);
    			}
            }  
        });
        
        // 註冊讓該Activity負責處理所有接收到的NFC Intents。
        gNfcPendingIntent = PendingIntent.getActivity(this, 0,
        // 指定該Activity為應用程式中的最上層Activity
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        // 建立要處理的Intent Filter負責處理來自Tag或p2p交換的資料。
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        }
        catch (MalformedMimeTypeException e) {}
        gNdefExchangeFilters = new IntentFilter [] {ndefDetected};
        Bundle bundle0311 =this.getIntent().getExtras();
        //first = bundle0311.getBoolean("checkfirst");
        
        
        
        setContentView(R.layout.activity_main);
		//overridePendingTransition(R.anim.in, R.anim.out);
		//這是特效
		
		layout = (ViewGroup) findViewById(R.id.main_circle_layout);
		
		status = 0;
		  
		
		CircleLayout circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);

		selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());
		
		
		
		//宣告Timer
		Timer timer01 =new Timer();
				
		//設定Timer(task為執行內容，0代表立刻開始,間格1秒執行一次)
		timer01.schedule(task_tottime, 0,1000);
		
        
        
        
    }
    
    

    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        gResumed = true;
        // 處理由Android系統送出應用程式處理的intent filter內容
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            // 取得NdefMessage
            NdefMessage [] messages = getNdefMessages(getIntent());
            // 取得實際的內容
            byte [] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
            // 往下送出該intent給其他的處理對象
            setIntent(new Intent());
        }
        // 啟動前景模式支持Nfc intent處理
        enableNdefExchangeMode();
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        gResumed = false;
        // 由於NfcAdapter啟動前景模式將相對花費更多的電力，要記得關閉。
        gNfcAdapter.disableForegroundNdefPush(this);
    }
    
    @Override
    protected void onNewIntent(Intent intent)
    {
        // 覆寫該Intent用於補捉如果有新的Intent進入時，可以觸發的事件任務。
        // NDEF exchange mode
        if (!gWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage [] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
        }
        
        // 監測到有指定ACTION進入，代表要寫入資料至Tag中。
        // Tag writing mode
        if (gWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(getNoteAsNdef(), detectedTag);
        }
    }
    
    /**
     * 取得Intent中放入的NdefMessage。
     * @param intent
     * @return
     */
    NdefMessage [] getNdefMessages(Intent intent)
    {
        // Parse the intent
        NdefMessage [] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable [] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage [rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage)rawMsgs[i];
                }
            }
            else {
                // Unknown tag type
                byte [] empty = new byte [] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord [] {record});
                msgs = new NdefMessage [] {msg};
            }
        }
        else {
            Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }
    
    private void setNoteBody(String body)
    {
        Editable text = gNote.getText();
        text.clear();
        text.append(body);
    }
    
    /**
     * 將輸入的內容轉成NdefMessage。
     * @return
     */
    private NdefMessage getNoteAsNdef()
    {
        byte [] textBytes = gNote.getText().toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(), new byte [] {}, textBytes);
        return new NdefMessage(new NdefRecord [] {textRecord});
    }
    
    /**
     * 啟動Ndef交換資料模式。
     */
    private void enableNdefExchangeMode()
    {
        // 讓NfcAdatper啟動前景Push資料至Tag或應用程式。
        gNfcAdapter.enableForegroundNdefPush(MainActivity.this, getNoteAsNdef());
        
        // 讓NfcAdapter啟動能夠在前景模式下進行intent filter的dispatch。
        gNfcAdapter.enableForegroundDispatch(this, gNfcPendingIntent, gNdefExchangeFilters, null);
    }
    
    private void disableNdefExchangeMode()
    {
        gNfcAdapter.disableForegroundNdefPush(this);
        gNfcAdapter.disableForegroundDispatch(this);
    }
    
    /**
     * 啟動Tag寫入模式，註冊對應的Intent Filter來前景模式監聽是否有Tag進入的訊息。
     */
    private void enableTagWriteMode()
    {
        gWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        gWriteTagFilters = new IntentFilter [] {tagDetected};
        gNfcAdapter.enableForegroundDispatch(this, gNfcPendingIntent, gWriteTagFilters, null);
    }
    
    /**
     * 停止Tag寫入模式，取消前景模式的監測。
     */
    private void disableTagWriteMode()
    {
        gWriteMode = false;
        gNfcAdapter.disableForegroundDispatch(this);
    }
    
    /**
     * 應用程式補捉到Ndef Message，詢問用戶是否要取代目前畫面中的文件。
     * 
     * @param msg
     */
    private void promptForContent(final NdefMessage msg)
    {
        new AlertDialog.Builder(this).setTitle("是否取代現在的內容?").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                String body = new String(msg.getRecords()[0].getPayload());
                setNoteBody(body);
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                
            }
        }).show();
    }
    
    private View.OnClickListener gTagWriter = new View.OnClickListener() {
        
        @Override
        public void onClick(View v)
        {
        	rightMoveHandlenfc();
        	jumpToLayout(leftAnimation);
  		  	status =0;
        }
    };
    
    private TextWatcher gTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // 如果是在Resume的狀態下，當編輯完後，啟動前景發佈訊息的功能。
        	if (gResumed) {
                gNfcAdapter.enableForegroundNdefPush(MainActivity.this, getNoteAsNdef());
            }
        }
    };
    
    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    toast("Tag is read-only.");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    toast("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                toast("Wrote message to pre-formatted tag.");
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        toast("Formatted tag and wrote message");
                        return true;
                    } catch (IOException e) {
                        toast("Failed to format tag.");
                        return false;
                    }
                } else {
                    toast("Tag doesn't support NDEF.");
                    return false;
                }
            }
        } catch (Exception e) {
            toast("Failed to write tag");
        }

        return false;
    }
    
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
	public void onItemSelected(View view, int position, long id, String name) {		
		selectedTextView.setText(name);
	}

	@Override
	public void onItemClick(View view, int position, long id, String name) {
		
		if(name.equals("關於我們")){
			leftMoveHandle();
			layout.setEnabled(false);
			jumpToLayout1(rightAnimation);
			
		}
		if(name.equals("濾光")){
			leftMoveHandle();
			layout.setEnabled(false);
			jumpToLayout2(rightAnimation);
			
		
		}else if(name.equals("提醒")){
			leftMoveHandle();
			layout.setEnabled(false);
			jumpToLayout3(rightAnimation);
		}
		else if(name.equals("亮度")){
			leftMoveHandle();
			layout.setEnabled(false);
			jumpToLayout4(rightAnimation);
		}else if(name.equals("登入/註冊")){
			leftMoveHandle();
			layout.setEnabled(false);
			jumpToLayout6(rightAnimation);
		}else if(name.equals("會員專區") /*&& login_status*/ ){
			leftMoveHandle();
			layout.setEnabled(false);
			jumpToLayout5_select_one(rightAnimation);
		}/*else if(name.equals("會員專區") && !login_status){
			Toast.makeText(MainActivity.this, "請先登入才能使用環境設定及同步功能",Toast.LENGTH_SHORT ).show();
		}*/
		else if(name.equals("特殊病因") && login_status){
			leftMoveHandle();
			layout.setEnabled(false);
    		jumpToLayout5_sick(rightAnimation);
		}else if(name.equals("特殊病因") && !login_status){
			Toast.makeText(MainActivity.this, "請先登入才能使用",Toast.LENGTH_SHORT ).show();
		}
		
	}
	
	public void initFirst(){  
	        leftAnimation = new Rotate3d(0, -90, 0.0f, 0.0f, mCenterX, mCenterY);  
	        rightAnimation = new Rotate3d(90, 0, 0.0f, 0.0f, mCenterX, mCenterY);  
	        leftAnimation.setFillAfter(true);  
	        leftAnimation.setDuration(1000);  
	        rightAnimation.setFillAfter(true);  
	        rightAnimation.setDuration(1000);  
	    }  
	      
	    public void initSecond(){  
	        leftAnimation = new Rotate3d(-90, 0, 0.0f, 0.0f, mCenterX, mCenterY);  
	        rightAnimation = new Rotate3d(0, 90, 0.0f, 0.0f, mCenterX, mCenterY);  
	        leftAnimation.setFillAfter(true);  
	        leftAnimation.setDuration(1000);  
	        rightAnimation.setFillAfter(true);  
	        rightAnimation.setDuration(1000);  
	    }  
	    
	    public void leftMoveHandle() {  
	        initFirst();  
	        layout.startAnimation(leftAnimation);  
	          
	    }
	    
	    	    
	    public void rightMoveHandle1() {  
	        initSecond();  
	        layout1.startAnimation(rightAnimation);  
	          
	    }
	    
	    public void rightMoveHandle2() {  
	        initSecond();  
	        layout2.startAnimation(rightAnimation);  
	          
	    }
	   
	    public void rightMoveHandle3() {  
	        initSecond();  
	        layout3.startAnimation(rightAnimation);  
	          
	    }
        
	    public void rightMoveHandle4() {  
	    	initSecond();  
	        layout4.startAnimation(rightAnimation);  
	          
	    }
	    
	    public void rightMoveHandle5() {  
	    	initSecond();  
	        layout5.startAnimation(rightAnimation);  
	          
	    }
	    
	    public void rightMoveHandle5select_one() {  
	    	initSecond();  
	        layout5select_one.startAnimation(rightAnimation);  
	          
	    }
        
        public void rightMoveHandle5_syn(){
        	initSecond(); 
	    	layout7.startAnimation(rightAnimation); 
	    }
        
        
        public void rightMoveHandlenfc(){
        	initSecond();
        	layout_nfc.startAnimation(rightAnimation);
        }

	    public void rightMoveHandle6() {  
	    	initSecond();  
	        layout6.startAnimation(rightAnimation);  
	          
	    }
	    
        public void rightMoveHandle6add(){
        	initSecond(); 
	    	layout6add.startAnimation(rightAnimation); 
	    }
	    
	    public void jumpToLayout(Rotate3d leftAnimation) {
	    	status = 0;
	        setContentView(R.layout.activity_main);  
	  
	        layout = (ViewGroup) findViewById(R.id.main_circle_layout);  
	        layout.startAnimation(leftAnimation);  
	        CircleLayout circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
			circleMenu.setOnItemSelectedListener(this);
			circleMenu.setOnItemClickListener(this);
	        selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
			selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());
			
			
			
			
	    }  
	    
	    public void jumpToLayout1(Rotate3d rightAnimation) {  
	        setContentView(R.layout.aboutme);
	        status =1;
	        layout1 = (ViewGroup) findViewById(R.id.aboutme);  
	        layout1.startAnimation(rightAnimation);  
	        final Button ab_btn = (Button)findViewById(R.id.ab_btn);
	        ab_btn.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {  
	            	rightMoveHandle1();	                
	                jumpToLayout(leftAnimation);
	            }  
	        });
	    }  
	    
	    public void jumpToLayout2(Rotate3d rightAnimation) {  
	        setContentView(R.layout.lightfiler);  
	        layout2 = (ViewGroup) findViewById(R.id.test1);  
	        layout2.startAnimation(rightAnimation);
	        status = 2;
	        final EditText et_R =(EditText)findViewById(R.id.editText1);
	        final EditText et_B =(EditText)findViewById(R.id.EditText01);
	        final EditText et_G =(EditText)findViewById(R.id.EditText02);
	        final EditText et_A =(EditText)findViewById(R.id.EditText03);
	        final SeekBar sb1 = (SeekBar)findViewById(R.id.seekBar1);
	        final SeekBar sb2 = (SeekBar)findViewById(R.id.seekBar2);
	        final SeekBar sb3 = (SeekBar)findViewById(R.id.seekBar3);	        
			final SeekBar sb4 = (SeekBar)findViewById(R.id.seekBar4);
			final Button b1 = (Button)findViewById(R.id.button1);
	        final Button b2 = (Button) findViewById(R.id.button2);
	        final Button b3 = (Button) findViewById(R.id.button3);  
		
	        final Button green = (Button)findViewById(R.id.button4);
	        final Button yellow = (Button)findViewById(R.id.button5);
	        final Button orange = (Button)findViewById(R.id.button6);
	        
	        initViews();
	        context = this;
			sb1.setProgress(r);
			sb2.setProgress(g);
			sb3.setProgress(B);
			sb4.setProgress(a);
			et_R.setText(Integer.toString(r));
			et_B.setText(Integer.toString(B));
			et_G.setText(Integer.toString(g));
			et_A.setText(Integer.toString(a));
			//複製
			
			if( str_isadd.equals("true") && !isAdded ){
				btn_floatView = new Button(getApplicationContext());
                
                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
                
                wm = (WindowManager) getApplicationContext().getSystemService(
                        Context.WINDOW_SERVICE);
                params = new WindowManager.LayoutParams();
        	
             // 设置window type
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
                /*
                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
                 * 即拉下通知栏不可见
                 */

                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
               

                // 设置Window flag
               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
                
                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                 
                 wm.addView(btn_floatView, params);
                 isAdded = true;
                   
                 wm.updateViewLayout(btn_floatView, params);
                 Toast.makeText(MainActivity.this,"開啟濾光",Toast.LENGTH_SHORT).show();
                 b1.setEnabled(false);
                 b2.setEnabled(true);
			}
			if(isAdded){
				b1.setEnabled(false);
			}
			
			et_R.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
					if(et_R.length() != 0){
					int l = Integer.parseInt(et_R.getText().toString());
					if(l > 100){
						et_R.setText("100");
						et_R.setSelection(3);
					}
					sb1.setProgress(l);
					et_R.setSelection(Integer.toString(l).length());
					} 
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				});
			et_B.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
					if(et_B.length() != 0){
					int l = Integer.parseInt(et_B.getText().toString());
					if(l > 100){
						et_B.setText("100");
						et_B.setSelection(3);
					}
					sb3.setProgress(l);
					et_B.setSelection(Integer.toString(l).length());
					} 
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				});
			
			et_G.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
					if(et_G.length() != 0){
					int l = Integer.parseInt(et_G.getText().toString());
					if(l > 100){
						et_G.setText("100");
						et_G.setSelection(3);
					}
					sb2.setProgress(l);
					et_G.setSelection(Integer.toString(l).length());
					} 
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				});
			
			et_A.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
					if(et_A.length() != 0){
						int l = Integer.parseInt(et_A.getText().toString());
						if(l > 100){
							et_A.setText("80");
							et_A.setSelection(2);
						}
						sb4.setProgress(l);
						et_A.setSelection(Integer.toString(l).length());
						} 
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){
					
				}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					
				}
				});
			
	        sb1.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
	        @Override
	        public void onProgressChanged(SeekBar sb1, int progresValue, boolean fromUser) { 
	        	progress = progresValue;
	        	r = progresValue;
	        	et_R.setText(Integer.toString(r));
	        	if(isAdded){
	        		
	        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
	        	}
	        	}
	      @Override
	      public void onStartTrackingTouch(SeekBar sb1) {
	        
	      }

	      @Override
	      public void onStopTrackingTouch(SeekBar sb1) {
	        
	    	  
	      }
	  });
	        
	        sb2.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
	        @Override
	      public void onProgressChanged(SeekBar sb2, int progresValue, boolean fromUser) { 
	        	progress = progresValue;
	        	g = progresValue;
	        	et_G.setText(Integer.toString(g));
	        	if(isAdded){
	        		
	        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
	        	}
	        }
	      @Override
	      public void onStartTrackingTouch(SeekBar sb2) {
	        
	      }

	      @Override
	      public void onStopTrackingTouch(SeekBar sb2) {
	        
	    	  
	      }
	  });
        
			sb3.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
	        @Override
	      public void onProgressChanged(SeekBar sb3, int progresValue, boolean fromUser) { 
	        	progress = progresValue;
	        	B = progresValue;
	        	et_B.setText(Integer.toString(B));
	        	if(isAdded){
	        		
	        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
	        	}
	        }
	      @Override
	      public void onStartTrackingTouch(SeekBar sb3) {
	        
	      }

	      @Override
	      public void onStopTrackingTouch(SeekBar sb1) {
	        
	    	  
	      }
	  });
			sb4.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
	        @Override
	      public void onProgressChanged(SeekBar sb4, int progresValue, boolean fromUser) { 
	        	progress = progresValue;
	        	a = progress;	        	
	        	et_A.setText(Integer.toString(a));
	        	if(isAdded){
	        		
	        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
	        	}
	        }
	      @Override
	      public void onStartTrackingTouch(SeekBar sb4) {
	        
	      }

	      @Override
	      public void onStopTrackingTouch(SeekBar sb1) {
	        
	    	  
	      }
	  });
	        
	        b1.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {  
	            	if(!isAdded){
	            		btn_floatView = new Button(getApplicationContext());
	                    
	                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
	                    
	                    wm = (WindowManager) getApplicationContext().getSystemService(
	                            Context.WINDOW_SERVICE);
	                    params = new WindowManager.LayoutParams();
	            	
	                 // 设置window type
	                    params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
	                    /*
	                     * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
	                     * 即拉下通知栏不可见
	                     */

	                    params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
	                   

	                    // 设置Window flag
	                   /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
	                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
	                            WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
	                    
	                    // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
	                     params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
	                    		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
	                    		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
	                    		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
	                     
	                     wm.addView(btn_floatView, params);
	                     
	                     isAdded = true;
	                     str_isadd = "true"; 
	                     
	                     wm.updateViewLayout(btn_floatView, params);
	                     Toast.makeText(MainActivity.this,"開啟濾光",Toast.LENGTH_SHORT).show();
	                     b1.setEnabled(false);
	                     b2.setEnabled(true);
	                     
	            	}
	           }
	            	
	        }); 
	      
	          
	        
	        b2.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {  
	            	if(isAdded){
	            		
	        			wm.removeView(btn_floatView);
	        			Toast.makeText(MainActivity.this,"關閉濾光",Toast.LENGTH_SHORT).show();
	        			isAdded = false;
	        			str_isadd = "false";
	        			a = 0;
	        			r = 0;
	        			g = 0;
	        			B = 0;
	        			sb1.setProgress(r);
	        			sb2.setProgress(g);
	        			sb3.setProgress(B);
	        			sb4.setProgress(a);
	        			et_R.setText(Integer.toString(r));
	        			et_B.setText(Integer.toString(B));
	        			et_G.setText(Integer.toString(g));
	        			et_A.setText(Integer.toString(a));
	        			b1.setEnabled(true);
	        
	        			b2.setEnabled(false);
	        			} 
	            }  
	        }); 
	        
	        
	        
	        b3.setEnabled(true);  
	        b3.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
        			//Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	                rightMoveHandle2();
	                jumpToLayout(leftAnimation);
	            }  
	        }); 
	        green.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	r = 0;
	            	g = 100;
	            	B = 0;
	            	a = 20;
	            	sb1.setProgress(r);
	    			sb2.setProgress(g);
	    			sb3.setProgress(B);
	    			sb4.setProgress(a);
	    			et_R.setText(Integer.toString(r));
	    			et_B.setText(Integer.toString(B));
	    			et_G.setText(Integer.toString(g));
	    			et_A.setText(Integer.toString(a));
	            	if(isAdded){		        		
	                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
		        	}
	            }  
	        });
	        yellow.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	r = 100;
	            	g = 100;
	            	B = 0;
	            	a = 20;
	            	sb1.setProgress(r);
	    			sb2.setProgress(g);
	    			sb3.setProgress(B);
	    			sb4.setProgress(a);
	    			et_R.setText(Integer.toString(r));
	    			et_B.setText(Integer.toString(B));
	    			et_G.setText(Integer.toString(g));
	    			et_A.setText(Integer.toString(a));
	            	if(isAdded){		        		
	                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
		        	}
	            }  
	        });
	        orange.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	r = 100;
	            	g = 65;
	            	B = 0;
	            	a = 20;
	            	sb1.setProgress(r);
	    			sb2.setProgress(g);
	    			sb3.setProgress(B);
	    			sb4.setProgress(a);
	    			et_R.setText(Integer.toString(r));
	    			et_B.setText(Integer.toString(B));
	    			et_G.setText(Integer.toString(g));
	    			et_A.setText(Integer.toString(a));
	            	if(isAdded){		        		
	                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
		        	}
	            }  
	        });
	        
	   
	    
	    }
    	    
	    public void jumpToLayout3(Rotate3d rightAnimation) {  
	        setContentView(R.layout.remind);  
	        status = 3;	  
	        layout3 = (ViewGroup) findViewById(R.id.remindlayout1);  
	        layout3.startAnimation(rightAnimation);  
	        final RadioButton re_rb1 = (RadioButton)findViewById(R.id.re_rb1);
	        final RadioButton re_rb2 = (RadioButton)findViewById(R.id.re_rb2);
	        final RadioButton re_rb3 = (RadioButton)findViewById(R.id.re_rb3);
	        final RadioButton re_rb4 = (RadioButton)findViewById(R.id.re_rb4);
	        final Button op_btn = (Button)findViewById(R.id.op_remind);
	        final Button cl_btn = (Button)findViewById(R.id.cl_remind);
	        final Button back_re = (Button)findViewById(R.id.back_remind);
	        final EditText re_et = (EditText)findViewById(R.id.remindet);
	        final TextView re_tv = (TextView)findViewById(R.id.remindtv);
	        final TextView re_use_time = (TextView)findViewById(R.id.use_time);
	        re_et.setEnabled(false);
	        cl_btn.setEnabled(false);
	        handler.removeCallbacks(updateTimer);
	        //設定Delay的時間
	        handler.postDelayed(updateTimer, 1000);
	        re_use_time.setText(tot_use_time);
	        re_et.setText("0");
	        if(re_status ==1 ){
	        	re_rb1.setChecked(true);
	        	
	        }else if(re_status == 2){
	        	re_rb2.setChecked(true);
	        	
	        }else if(re_status == 3){
	        	re_rb3.setChecked(true);
	        	
	        }else if(re_status == 4){
	        	re_rb4.setChecked(true);
	        	re_et.setEnabled(true);
	        	re_et.setText(Integer.toString(re_time / 1000 / 60));
	        }
	        if(re_cdt_statue != 0){
	        	cl_btn.setEnabled(true);
	        }
	        
	        if(str_isremind.equals("true") && !isremind){
	        	rc = new RemindCount(re_time,1000);
	        	rc.start();
	        	cl_btn.setEnabled(true);
	        }
	        	        
	        
	        re_et.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
					if(re_et.length() != 0 ){
						if(Integer.parseInt(re_et.getText().toString()) > 0){
						op_btn.setEnabled(true);
						re_time =Integer.parseInt(re_et.getText().toString()) * 60 * 1000;
	        			rc = new RemindCount(re_time,1000);
						}
					}else{
						op_btn.setEnabled(false);
					}
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				});
	        RadioButton.OnClickListener remindlistener = new RadioButton.OnClickListener(){
	        	public void onClick(View v){
	        		if(re_rb1.isChecked()){
	        			isremind = false;
	        			str_isremind = "false";
	        			re_et.setEnabled(false);
	        			op_btn.setEnabled(true);
	        		}else if(re_rb2.isChecked()){
	        			isremind = false;
	        			str_isremind = "false";
	        			re_et.setEnabled(false);
	        			op_btn.setEnabled(true);
	        		}else if(re_rb3.isChecked()){
	        			isremind = false;
	        			str_isremind = "false";
	        			re_et.setEnabled(false);
	        			op_btn.setEnabled(true);
	        		}else if(re_rb4.isChecked()){
	        			isremind = false;
	        			str_isremind = "false";
	        			re_et.setEnabled(true);
	        			op_btn.setEnabled(true);
	        		}
	        	}
	        	
	        };
	        re_rb1.setOnClickListener(remindlistener);
	        re_rb2.setOnClickListener(remindlistener);
	        re_rb3.setOnClickListener(remindlistener);
	        re_rb4.setOnClickListener(remindlistener);
	        
	        
	        Button.OnClickListener rebtnlistener = new Button.OnClickListener(){
	        	public void onClick(View v){
	        		switch(v.getId())
	        		{
	        			case R.id.op_remind:
	        				if(!isremind){
	        					if(re_rb1.isChecked()){
		    	        			isremind = false;
		    	        			re_status = 1;
		    	        			re_time =1800000;
		    	        			rc = new RemindCount(1800000,1000);
		    	        			str_isremind = "false";
		    	        			op_btn.setEnabled(true);
		    	        			re_et.setEnabled(false);
		    	        		}else if(re_rb2.isChecked()){
		    	        			isremind = false;
		    	        			re_status = 2;
		    	        			re_time = 3600000;
		    	        			rc = new RemindCount(3600000,1000);
		    	        			str_isremind = "false";
		    	        			op_btn.setEnabled(true);
		    	        			re_et.setEnabled(false);
		    	        		}else if(re_rb3.isChecked()){
		    	        			isremind = false;
		    	        			re_status = 3;
		    	        			re_time = 7200000;
		    	        			rc = new RemindCount(7200000,1000);
		    	        			str_isremind = "false";
		    	        			op_btn.setEnabled(true);
		    	        			re_et.setEnabled(false);
		    	        		}else if(re_rb4.isChecked() && Integer.parseInt(re_et.getText().toString()) > 0 ){
		    	        			isremind = false;
		    	        			re_status = 4;
		    	        			re_time =Integer.parseInt(re_et.getText().toString()) * 60 * 1000;
		    	        			str_isremind = "false";
		    	        			rc = new RemindCount(re_time,1000);
		    	        			re_et.setEnabled(true);
		    	        			
		    	        		}
	        					
	        				if(re_rb1.isChecked() || re_rb2.isChecked() || re_rb3.isChecked() || (re_rb4.isChecked() && Integer.parseInt(re_et.getText().toString()) > 0)){
	        				str_isremind = "true";
	        				re_cdt_statue = 1;
	        				rc.cancel();
	        				rc.start();
	        				cl_btn.setEnabled(true);
	        				op_btn.setEnabled(false);
		        			Toast.makeText(MainActivity.this,"開啟提醒",Toast.LENGTH_SHORT).show();
		        			isremind = true;
	        					}
	        				};
	        				break;
	        			case R.id.cl_remind:
	        				re_cdt_statue = 0;
	        				str_isremind = "false";
	        				rc.cancel();
	        				isremind = false;
	        				cl_btn.setEnabled(false);
	        				op_btn.setEnabled(true);
	        				Toast.makeText(MainActivity.this,"關閉提醒",Toast.LENGTH_SHORT).show();
	        				break;
	        			case R.id.back_remind:
	        				status = 0;
	        				rightMoveHandle3();	                
	        				jumpToLayout(leftAnimation);
	        				//Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	        				break;
	        			}
	        		}
	        	
	        };
	        op_btn.setOnClickListener(rebtnlistener);
	        cl_btn.setOnClickListener(rebtnlistener);
	        back_re.setOnClickListener(rebtnlistener);
	        
	    }  
	    	    
	    public void jumpToLayout4(Rotate3d rightAnimation) {  
	        setContentView(R.layout.light);
	        
	        status =4;
        	mc = new MyCount(3000, 1000);
    		mc.start(); 
            mc.cancel();
	        layout4 = (ViewGroup) findViewById(R.id.rlayout1);  
	        layout4.startAnimation(rightAnimation);  
	        final RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton1);
	        final RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
	        final RadioButton rb3 = (RadioButton)findViewById(R.id.radioButton3);
	        final EditText et1 = (EditText)findViewById(R.id.editText1);
	        final SeekBar sb = (SeekBar)findViewById(R.id.sb1);
	        final Button btl1 = (Button)findViewById(R.id.buttonlight1);
	        final Button btl2 = (Button)findViewById(R.id.buttonback1);
	        sb.setProgress(brightness);
	        et1.setText(Integer.toString(brightness));
	        et1.setEnabled(false);
			sb.setEnabled(false);
			btl1.setEnabled(false);
			//複製
	        if(setmode == 1 ){
	        	rb1.setChecked(true);
	        	rb1.setEnabled(false);
	        }else if(setmode == 2){
	        	rb2.setChecked(true);
	        	et1.setEnabled(true);
    			sb.setEnabled(true);
    			btl1.setEnabled(true);
	        }else if(setmode == 3){
	        	rb3.setChecked(true);
	        	rb3.setEnabled(false);
	        }
	        
	        if(str_light.equals("auto")){
	        	str_light = "auto";
    			rb1.setEnabled(false);
    			rb2.setEnabled(true);
    			rb3.setEnabled(true);
    			// 先將亮度設定模式調整為手動
            	Settings.System.putInt(
                getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            	
            	setmode = 1;
    			et1.setEnabled(false);
    			sb.setEnabled(false);
    			btl1.setEnabled(false);
    			
    			//呼叫感光元件	        			
    			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    		     if(LightSensor != null){
    		    	 //有感光元件
    		    	 
    			      mySensorManager.registerListener(
    			        LightSensorListener, 
    			        LightSensor, 
    			        SensorManager.SENSOR_DELAY_NORMAL);
    			      
    			    et1.setEnabled(false);
	        			sb.setEnabled(false);
	        			btl1.setEnabled(false);
	        			light_status = true;
	        			
    			     }else{
    			    	//無感光元件
    		     }
    		    
    			
	        }else if(str_light.equals("manually")){
	        	str_light = "manually";
    			rb2.setEnabled(false);
    			rb1.setEnabled(true);
    			rb3.setEnabled(true);
    			// 先將亮度設定模式調整為手動
            	Settings.System.putInt(
                getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            	
            	setmode = 2;
    			et1.setEnabled(true);
    			sb.setEnabled(true);
	        }else if(str_light.equals("sys_auto")){
	        	str_light = "sys_auto";
	        	setmode = 3;
	        	sb.setProgress(0);
		        et1.setText(Integer.toString(0));
		        Settings.System.putInt(
	                    getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	        	
	        }
	        
	        
	       
	        btl1.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	if(et1.length() != 0){
	                int l = Integer.parseInt(et1.getText().toString());
	                if(l < 256){
	                	str_light = "manually";
	                	sb.setProgress(l);
	                	brightness = l;
	                	Settings.System.putInt(getContentResolver(),
	           	    		 Settings.System.SCREEN_BRIGHTNESS, brightness);
	                	light_status = true;
	                	Toast.makeText(MainActivity.this,"設定螢幕亮度為" + brightness ,Toast.LENGTH_SHORT).show();
	                	if(login_status){
	        	    		Thread tttt=new Thread(new Runnable(){
	        					public void run()
	        					{		
	        						wifi = isNetworkConnected(MainActivity.this);
	                		        if(wifi){
	        						SoapObject request3=new SoapObject(NAMESPACE,METHOD3);
	        						request3.addProperty("uname", account);
	        						request3.addProperty("pwd",password);						
	        						request3.addProperty("light",brightness);
	        						SoapSerializationEnvelope env3=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        						env3.bodyOut=request3;
	        						env3.dotNet=true;
	        						HttpTransportSE htc3=new HttpTransportSE(URL);
	        						try
	        						{
	        							SOAP_ACTION3=NAMESPACE+METHOD3;
	        							htc3.call(SOAP_ACTION3, env3);
	        							String result3=env3.getResponse().toString();
	        							
	        						}catch(Exception e){
	        							
	        							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
	        						}
	        					}else{
	        						//無網路
	        						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	        						
	        					}
	        					}
	        				});
	        				tttt.start();
	        	    	}
	            	} else {
	            		et1.setText("0");
						et1.setSelection(1);
	            	}
	            	}
	            }  
	        });
	        
	        btl2.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) { 
	            	
	                rightMoveHandle4();	                
	                jumpToLayout(leftAnimation);
	                //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	            }  
	        });
	        
	        
	        
	        
	        RadioButton.OnClickListener listener2 = new RadioButton.OnClickListener(){
	        	public void onClick(View v){
	        		if(rb1.isChecked()){
	        			str_light = "auto";
	        			rb1.setEnabled(false);
	        			rb2.setEnabled(true);
	        			rb3.setEnabled(true);
	        			// 先將亮度設定模式調整為手動
	                	Settings.System.putInt(
	                    getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	                	
	                	
	                	
	                	setmode = 1;
	        			et1.setEnabled(false);
	        			sb.setEnabled(false);
	        			btl1.setEnabled(false);
	        			
	        			//呼叫感光元件	        			
	        			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

	        		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	        		     if(LightSensor != null){
	        		    	 //有感光元件
	        		    	 
	        			      mySensorManager.registerListener(
	        			        LightSensorListener, 
	        			        LightSensor, 
	        			        SensorManager.SENSOR_DELAY_NORMAL);
	        			      
	        			    et1.setEnabled(false);
	  	        			sb.setEnabled(false);
	  	        			btl1.setEnabled(false);
	  	        			light_status = true;
	  	        			Toast.makeText(MainActivity.this,"開啟自動調整亮度",Toast.LENGTH_SHORT).show();
	        			     }else{
	        			    	//無感光元件
	        		     }
	        		    
	        			
	        		} else if (rb2.isChecked()){
	        			str_light = "manually";
	        			rb2.setEnabled(false);
	        			rb1.setEnabled(true);
	        			rb3.setEnabled(true);
	        			// 先將亮度設定模式調整為手動
	                	Settings.System.putInt(
	                    getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	                	Toast.makeText(MainActivity.this,"開啟手動設定亮度",Toast.LENGTH_SHORT).show();
	                	setmode = 2;
	                	Settings.System.putInt(getContentResolver(),
		           	    		 Settings.System.SCREEN_BRIGHTNESS, brightness);
	        			et1.setEnabled(true);
	        			sb.setEnabled(true);
	        			btl1.setEnabled(true);
	        			et1.addTextChangedListener(new TextWatcher(){
	        				public void afterTextChanged(Editable edt){ 
	        					if(et1.length() != 0){
	        					int l = Integer.parseInt(et1.getText().toString());
	        					if(l > 255){
	        						et1.setText("255");
	        						et1.setSelection(3);
	        					}	        					
	        					} 
	        				}
	        				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
	        				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	        				});
	        		} else if (rb3.isChecked()){
	        			str_light = "sys_auto";
	        			setmode = 3;
	        			et1.setEnabled(false);
	        			sb.setEnabled(false);
	        			btl1.setEnabled(false);
	        			rb1.setEnabled(true);
	        			rb2.setEnabled(true);
	        			rb3.setEnabled(false);
	        			sb.setProgress(0);
	    		        et1.setText(Integer.toString(0));
	        			Settings.System.putInt(
	    	                    getContentResolver(),
	    	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	    	                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	        			
	        		}
	        		
	        	}
	        };
	        rb1.setOnClickListener(listener2);
	        rb2.setOnClickListener(listener2);
	        rb3.setOnClickListener(listener2);
	        sb.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
	        @Override
	      public void onProgressChanged(SeekBar sb, int progresValue, boolean fromUser) { 
	        	progress = progresValue;
	        	brightness = progress;
	        	light_status = true;
	        	et1.setText(Integer.toString(progresValue));
	        	et1.setSelection(Integer.toString(progress).length());
	        	Settings.System.putInt(getContentResolver(),
          	    		 Settings.System.SCREEN_BRIGHTNESS, progress);
	        	if(login_status){
		    		Thread tttt=new Thread(new Runnable(){
						public void run()
						{		
							wifi = isNetworkConnected(MainActivity.this);
	        		        if(wifi){
							SoapObject request3=new SoapObject(NAMESPACE,METHOD3);
							request3.addProperty("uname", account);
							request3.addProperty("pwd",password);						
							request3.addProperty("light",brightness);
							SoapSerializationEnvelope env3=new SoapSerializationEnvelope(SoapEnvelope.VER11);
							env3.bodyOut=request3;
							env3.dotNet=true;
							HttpTransportSE htc3=new HttpTransportSE(URL);
							try
							{
								SOAP_ACTION3=NAMESPACE+METHOD3;
								htc3.call(SOAP_ACTION3, env3);
								String result3=env3.getResponse().toString();
								
							}catch(Exception e){
								
								Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
							}
						}else{
							//無網路
							Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
							
						}
						}
					});
					tttt.start();
		    	}
	        	
	        }
	      @Override
	      public void onStartTrackingTouch(SeekBar sb4) {
	        
	      }

	      @Override
	      public void onStopTrackingTouch(SeekBar sb1) {
	        
	    	  
	      }
	  });
	        
	       
	    }
	    
	    public void jumpToLayout6(Rotate3d rightAnimation) {  
	        
	    } 
	    
	    
	    public void jumpToLayout5_select_one(Rotate3d rightAnimation) {
	    	//帶更正
	    	
	    	if(!login_status){
	    		setContentView(R.layout.login);  
		        layout6 = (ViewGroup) findViewById(R.id.login_layout);  
		        layout6.startAnimation(rightAnimation);  
		        final TextView login_tv_ac = (TextView)findViewById(R.id.login_tv_account);
		        final TextView login_tv_pwd = (TextView)findViewById(R.id.login_tv_password);
		        final TextView login_tv_result = (TextView)findViewById(R.id.login_result);
		        final EditText login_et_ac = (EditText)findViewById(R.id.login_et_account);
		        final EditText login_et_pwd = (EditText)findViewById(R.id.login_et_password);
		        final Button login_login = (Button)findViewById(R.id.login_btn_login);
		        final Button login_logout = (Button)findViewById(R.id.login_btn_logout);
		        final Button login_add = (Button)findViewById(R.id.login_btn_add);
		        final Button login_back = (Button)findViewById(R.id.login_btn_back);
		        status = 6;
		        login_et_ac.setText(account);
		        login_et_pwd.setText(password);
		        login_tv_result.setText("");
		      
		        mHandler=new Handler()
				{
					public void handleMessage(Message msg)
					{
						switch(msg.what)
						{
							case 1:
								result=msg.obj.toString();
								if(result.length() > 7){
									int age_user = result.length();
									int find = result.indexOf("你");
									ages =Integer.parseInt(result.substring(0, find));
									login_user = result.substring(find, age_user);
									login_tv_result.setText(login_user);
									login_status = true;
									nowtname = "";
									setTitle("Goggle" + "  使用者:" + account);
									
								}else{
									login_tv_result.setText(result );
								}
								break;
							case 2:
								//取得陣列
								result=msg.obj.toString();
								
								theme_name = result ;
								
								//套用第一個主題
								for(int i = 0; i< theme_name.length() ;i++){
			    					String s = theme_name.substring(i,i+1);
			    					if(s.equals("=")){
										 s1 = i;
									}else if(s.equals(";")){
										 s2 = i;
										 break;
									}
			    				}
								if((s2 - s1) > 1){
								default_theme = theme_name.substring(s1+1, s2);
								s1 = 0;
								s2 = 0;
								}
								if(default_theme.length() > 0){
								Thread thr=new Thread(new Runnable(){
			    					public void run()
			    					{
			    						wifi = isNetworkConnected(MainActivity.this);
				        		        if(wifi){
			    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
			    						request6.addProperty("uname", ac);
			    						
			    						request6.addProperty("t_name",default_theme );
			    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			    						env6.bodyOut=request6;
			    						env6.dotNet=true;
			    						HttpTransportSE htc6=new HttpTransportSE(URL);
			    						try
			    						{
			    							SOAP_ACTION6=NAMESPACE+METHOD6;
			    							htc6.call(SOAP_ACTION6, env6);
			    							String result=env6.getResponse().toString();
			    							Message msg=new Message();
			    							msg.what=3;
			    							msg.obj=result;
			    							mHandler.sendMessage(msg);
			    							
			    						}catch(Exception e){
			    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
			    						}
			    						
			    					}else{
			    						//無網路
			    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
			    					}
			    					}
			    					
			    				});
				            	thr.start();
								}
								break;
							case 3:
								result2=msg.obj.toString();
								
								//順序
								theme_val_list.clear();
								for(int q = 0;q < result2.length(); q++){
									String s = result2.substring(q,q+1);
													
									if(s.equals("=")){
										 q1 = q;
									}else if(s.equals(";")){
										 q2 = q;
									}
									if(q2 > q1){
									theme_val_list.add(result2.substring(q1+1, q2));
									q1 = 0;
									q2 = 0;
									}
								}
								
								
								r = Integer.parseInt(theme_val_list.get(0).toString()); 
								g = Integer.parseInt(theme_val_list.get(1).toString()); 
								B = Integer.parseInt(theme_val_list.get(2).toString()); 
								a = Integer.parseInt(theme_val_list.get(3).toString()); 
								re_time = Integer.parseInt(theme_val_list.get(4).toString()); 
								re_status = Integer.parseInt(theme_val_list.get(5).toString()); 
								setmode = Integer.parseInt(theme_val_list.get(6).toString()); 
								brightness = Integer.parseInt(theme_val_list.get(7).toString()); 
								str_isadd = theme_val_list.get(8).toString();
								str_isremind = theme_val_list.get(9).toString();
								str_light = theme_val_list.get(10).toString();
						
								
								if( (str_isadd.equals("true") && !isAdded)){
									
									btn_floatView = new Button(getApplicationContext());
					                
					                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
					                
					                wm = (WindowManager) getApplicationContext().getSystemService(
					                        Context.WINDOW_SERVICE);
					                
					                params = new WindowManager.LayoutParams();
					        	
					             // 设置window type
					                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
					                /*
					                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
					                 * 即拉下通知栏不可见
					                 */

					                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
					               

					                // 设置Window flag
					               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
					                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
					                
					                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
					                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
					                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
					                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
					                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
					                 
					                 wm.addView(btn_floatView, params);
					                 isAdded = true;
					                   
					                 wm.updateViewLayout(btn_floatView, params);
					                 
								}else if((str_isadd.equals("false") && isAdded)){
									wm.removeView(btn_floatView);
									isAdded = false;
									a = 0;
				        			r = 0;
				        			g = 0;
				        			B = 0;
								}else if((str_isadd.equals("true") && isAdded)){
									wm.removeView(btn_floatView);
									btn_floatView = new Button(getApplicationContext());
					                
					                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
					                
					                wm = (WindowManager) getApplicationContext().getSystemService(
					                        Context.WINDOW_SERVICE);
					                
					                params = new WindowManager.LayoutParams();
					        	
					             // 设置window type
					                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
					                /*
					                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
					                 * 即拉下通知栏不可见
					                 */

					                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
					               

					                // 设置Window flag
					               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
					                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
					                
					                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
					                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
					                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
					                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
					                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
					                 
					                 wm.addView(btn_floatView, params);
					                 isAdded = true;
					                   
					                 wm.updateViewLayout(btn_floatView, params);
								}
								
								if(str_isremind.equals("true")){
									
						        	rc = new RemindCount(re_time,1000);
						        	rc.cancel();
						        	rc.start();
						        	isremind = true;
						        }
								
								if(str_light.equals("auto")){
						        	str_light = "auto";
						        	mc = new MyCount(3000, 1000);
						    		mc.start(); 
					    			// 先將亮度設定模式調整為手動
						    		Settings.System.putInt(
						                    getContentResolver(),
						                    Settings.System.SCREEN_BRIGHTNESS_MODE,
						                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					            	Settings.System.putInt(
					                getContentResolver(),
					                Settings.System.SCREEN_BRIGHTNESS_MODE,
					                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					            	
					            	setmode = 1;
					    			
					    			
					    			//呼叫感光元件	        			
					    			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

					    		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
					    		     if(LightSensor != null){
					    		    	 //有感光元件
					    		    	 
					    			      mySensorManager.registerListener(
					    			        LightSensorListener, 
					    			        LightSensor, 
					    			        SensorManager.SENSOR_DELAY_NORMAL);
					    			      
					    			   
						        			light_status = true;
					    			     }else{
					    			    	//無感光元件
					    		     }
					    		    
					    			
						        }else if(str_light.equals("manually")){
						        	str_light = "manually";
					    			
					    			// 先將亮度設定模式調整為手動
					            	Settings.System.putInt(
					                getContentResolver(),
					                Settings.System.SCREEN_BRIGHTNESS_MODE,
					                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					            	Settings.System.putInt(getContentResolver(),
					           	    Settings.System.SCREEN_BRIGHTNESS, brightness);
					            	setmode = 2;
					    			
						        }else if(str_light.equals("sys_auto")){
						        	str_light = "sys_auto";
						        	setmode = 3;
						        	Settings.System.putInt(
						                    getContentResolver(),
						                    Settings.System.SCREEN_BRIGHTNESS_MODE,
						                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
						        }
								

								break;
						}
					}
				};
		        
		        
		        Button.OnClickListener login_btnlistener = new Button.OnClickListener(){
		        	public void onClick(View v){
		        		switch(v.getId())
		        		{
		        			case R.id.login_btn_login:
		        				
		        				wifi = isNetworkConnected(MainActivity.this);
		        		        if(wifi){	        		       
		        				if(login_et_ac.length() != 0 && login_et_pwd.length() != 0){
		        				Thread t=new Thread(new Runnable(){
		        					public void run()
		        					{
		        						SoapObject request=new SoapObject(NAMESPACE,METHOD);
		        						ac = login_et_ac.getText().toString();
		        						pwd = login_et_pwd.getText().toString();
		        						account = ac.toString();
		        						password = pwd.toString();
		        						request.addProperty("account", account);
		        						request.addProperty("pwd",password );
		        						SoapSerializationEnvelope env=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        						env.bodyOut=request;
		        						env.dotNet=true;
		        						HttpTransportSE htc=new HttpTransportSE(URL);
		        						try
		        						{
		        							SOAP_ACTION=NAMESPACE+METHOD;
		        							htc.call(SOAP_ACTION, env);
		        							String result=env.getResponse().toString();
		        							Message msg=new Message();
		        							msg.what=1;
		        							msg.obj=result;
		        							mHandler.sendMessage(msg);
		        							
		        						}catch(Exception e){
		        							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		        						}
		        						
		        						
		        						
		        					}
		        					
		        				});
		        				
		        				//取得所有環境設定
		        				
		        				Thread t2=new Thread(new Runnable(){
		        					public void run()
		        					{
		        				SoapObject request5=new SoapObject(NAMESPACE,METHOD5);
	    						ac = login_et_ac.getText().toString();
	    						pwd = login_et_pwd.getText().toString();
	    						request5.addProperty("uname", ac);
	    						request5.addProperty("pwd",pwd );
	    						SoapSerializationEnvelope env5=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    						env5.bodyOut=request5;
	    						env5.dotNet=true;
	    						HttpTransportSE htc5=new HttpTransportSE(URL);
	    						try
	    						{
	    							SOAP_ACTION5=NAMESPACE+METHOD5;
	    							htc5.call(SOAP_ACTION5, env5);
	    							String result5=env5.getResponse().toString();
	    							Message msg=new Message();
	    							msg.what=2;
	    							msg.obj=result5;
	    							mHandler.sendMessage(msg);	        							
	    						}catch(Exception e){
	    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
	    						}
	    						
		        					}
		        					
		        				});
		        				
		        				
		        				 t.start();
		        				 t2.start();
		        				 break;
		        				}else{
		        					break;
		        				}
		        		        } else{
		        					Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
		        					break;
		        		        }
		        			case R.id.login_btn_logout:
		        				account = "";
		        				password = "";
		        				login_status = false;
		        				login_et_ac.setText("");
		        		        login_et_pwd.setText("");
		        		        login_tv_result.setText("");
		        				setTitle("Goggle");
		        				break;
		        			case R.id.login_btn_add:
		        				rightMoveHandle6();	                
			  	                jumpToLayout6add(leftAnimation);   
		        				break;				
		        			case R.id.login_btn_back:
		        				rightMoveHandle6();	                
		        		  	    jumpToLayout(leftAnimation);
		        				break;
		        	}
		        }
		    };
		    login_login.setOnClickListener(login_btnlistener);
		    login_logout.setOnClickListener(login_btnlistener);
		    login_add.setOnClickListener(login_btnlistener);
		    login_back.setOnClickListener(login_btnlistener);
	    	}else{
	    	
	    	
	    	status = 9;
	        setContentView(R.layout.select_one);  
	  
	        layout5select_one = (ViewGroup) findViewById(R.id.select_one);  
	        layout5select_one.startAnimation(rightAnimation);  
	        
			final Button sl_theme = (Button)findViewById(R.id.goto_theme);
			final Button sl_syn = (Button)findViewById(R.id.goto_syn);
			final Button sl_back = (Button)findViewById(R.id.select_back);
			final Button sl_lf = (Button)findViewById(R.id.goto_lf);
			final Button sl_logout = (Button)findViewById(R.id.goto_lg);
			Button.OnClickListener login_btnlistener = new Button.OnClickListener(){
	        	public void onClick(View v){
	        		switch(v.getId())
	        		{
	        		case R.id.goto_theme:
	        			rightMoveHandle5select_one();
	    	    		jumpToLayout5(leftAnimation);
	        			break;
	        		case R.id.goto_syn:
	        			Thread tttt=new Thread(new Runnable(){
        					public void run()
        					{		
        						if(account.length() != 0 && password.length() != 0 || str_light.equals("manually") || str_light.equals("auto")){
        						wifi = isNetworkConnected(MainActivity.this);
                		        if(wifi){
        						SoapObject request5_syn=new SoapObject(NAMESPACE,METHOD3);
        						request5_syn.addProperty("uname", account);
        						request5_syn.addProperty("pwd",password);						
        						request5_syn.addProperty("light",brightness);
        						SoapSerializationEnvelope env5_syn=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        						env5_syn.bodyOut=request5_syn;
        						env5_syn.dotNet=true;
        						HttpTransportSE htc5_syn=new HttpTransportSE(URL);
        						try
        						{
        							SOAP_ACTION3=NAMESPACE+METHOD3;
        							htc5_syn.call(SOAP_ACTION3, env5_syn);
        							String result3=env5_syn.getResponse().toString();
        							
        						}catch(Exception e){
        							
        							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        						}
        					}else{
        						//無網路
        						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
        						
        					}
        					}else{
        						Toast.makeText(MainActivity.this, "請先確認是否已選用Goggle的調整亮度", Toast.LENGTH_SHORT).show();

        					}
        					}
        				});
        				tttt.start();
        				rightMoveHandle5select_one();
	    	    		jumpToLayout4(leftAnimation);
	        			break;
	        		case R.id.select_back:
	        			rightMoveHandle5select_one();
	    	    		jumpToLayout(leftAnimation);
	    	    		status = 0;
	    	    		break;
	        		case R.id.goto_lf:
	        			rightMoveHandle5select_one();
	        			jumpToLayoutnfc(leftAnimation);
	        			status = 8;
	                    break;
	        		case R.id.goto_lg:
	        			account = "";
        				password = "";
        				login_status = false;
        				setTitle("Goggle");
	        			break;
	        		}
	        	}
			};
			sl_theme.setOnClickListener(login_btnlistener);
			sl_syn.setOnClickListener(login_btnlistener);
			sl_back.setOnClickListener(login_btnlistener);	
			sl_lf.setOnClickListener(login_btnlistener);
			sl_logout.setOnClickListener(login_btnlistener);	    	}
	    }
	    
	    
	    public void jumpToLayoutnfc(Rotate3d rightAnimation){
	    	status = 8;
	    	setContentView(R.layout.nfc);
			layout_nfc = (ViewGroup) findViewById(R.id.lt_nfc);
			layout_nfc.startAnimation(rightAnimation); 
	        // 取得該設備預設的無線感應裝置
	        gNfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
	        if (gNfcAdapter == null) {
	            Toast.makeText(MainActivity.this, "不支援NFC感應功能！", Toast.LENGTH_SHORT).show();
	            MainActivity.this.finish();
	            return;
	        }
	        
	        // 取得EditText與Button，並且註冊對應的事件
	        final Button nfc_setting = (Button)findViewById(R.id.nfc_setting);
	        findViewById(R.id.write_tag).setOnClickListener(MainActivity.this.gTagWriter);        
	        gNote = (EditText)findViewById(R.id.note);
	        gNote.setEnabled(false);
	        gNote.addTextChangedListener(gTextWatcher);
	        
	        
	        gNote.setText("a=" + a + ";r=" + r + ";g=" + g + ";b=" + B + ";");
	        nfc_setting.setEnabled(true);  
	        nfc_setting.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            		lf_syn_list.clear();
	    				nfc_change = gNote.getText().toString();
	    				for(int i = 0; i< nfc_change.length() ;i++){
	    					String s = nfc_change.substring(i,i+1);
	    					if(s.equals("=")){
								 s1 = i;
							}else if(s.equals(";")){
								 s2 = i;
							}
	    					if(s2 > s1){
	    						lf_syn_list.add(nfc_change.substring(s1+1, s2));
								s1 = 0;
								s2 = 0;
								}
	    				}
	    				a = Integer.parseInt(lf_syn_list.get(0).toString());
	    				r = Integer.parseInt(lf_syn_list.get(1).toString());
						g = Integer.parseInt(lf_syn_list.get(2).toString()); 
						B = Integer.parseInt(lf_syn_list.get(3).toString()); 
						if(isAdded){
							wm.removeView(btn_floatView);
							btn_floatView = new Button(getApplicationContext());
			                
			                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
			                
			                wm = (WindowManager) getApplicationContext().getSystemService(
			                        Context.WINDOW_SERVICE);
			                params = new WindowManager.LayoutParams();
			        	
			             // 设置window type
			                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
			                /*
			                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
			                 * 即拉下通知栏不可见
			                 */

			                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
			               

			                // 设置Window flag
			               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
			                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
			                
			                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
			                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
			                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
			                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
			                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
			                 
			                 wm.addView(btn_floatView, params);
			                 isAdded = true;
			                   
			                 wm.updateViewLayout(btn_floatView, params);
						}else{
							
							btn_floatView = new Button(getApplicationContext());
			                
			                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
			                
			                wm = (WindowManager) getApplicationContext().getSystemService(
			                        Context.WINDOW_SERVICE);
			                params = new WindowManager.LayoutParams();
			        	
			             // 设置window type
			                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
			                /*
			                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
			                 * 即拉下通知栏不可见
			                 */

			                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
			               

			                // 设置Window flag
			               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
			                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
			                
			                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
			                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
			                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
			                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
			                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
			                 
			                 wm.addView(btn_floatView, params);
			                 isAdded = true;
			                   
			                 wm.updateViewLayout(btn_floatView, params);
	    			}
	            }  
	        });
	        
	        // 註冊讓該Activity負責處理所有接收到的NFC Intents。
	        gNfcPendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
	        // 指定該Activity為應用程式中的最上層Activity
	                new Intent(MainActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	        
	        // 建立要處理的Intent Filter負責處理來自Tag或p2p交換的資料。
	        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	        try {
	            ndefDetected.addDataType("text/plain");
	        }
	        catch (MalformedMimeTypeException e) {}
	        gNdefExchangeFilters = new IntentFilter [] {ndefDetected};
	        Bundle bundle0311 =MainActivity.this.getIntent().getExtras();
	    }
	    
	    public void jumpToLayout5(Rotate3d rightAnimation){
	    	setContentView(R.layout.goggle_theme);
	    	
	    	layout5 = (ViewGroup) findViewById(R.id.theme_layout);  
	        layout5.startAnimation(rightAnimation);
	        
	        
	        final Button theme_save = (Button)findViewById(R.id.theme_save);
	        final Button theme_back = (Button)findViewById(R.id.theme_back);
	        final Button theme_setting = (Button)findViewById(R.id.theme_setting);
	        
	        final Button theme_delete = (Button)findViewById(R.id.theme_delete);
	        final Button theme_modify = (Button)findViewById(R.id.theme_modify);
	        sp = (Spinner)findViewById(R.id.spinner1);  
	        
	        final TextView value = (TextView)findViewById(R.id.theme_tv);
			final TextView text = (TextView)findViewById(R.id.theme);
			
			
			refreshlist();
			if(nowtname.length() != 0){
				tname_idx = adapter.getPosition(nowtname);
				sp.setSelection(tname_idx);
			}
					sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
				        public void onItemSelected(AdapterView adapterView, View view, int position, long id){ 
				            //取得選取值可以使用「getSelectedItem()」或「getSelectedItemPosition()」；設定預設值則使用「setSelection()」。
				        	adapterView.setSelection(position);
				        	select_thname = adapterView.getSelectedItem().toString();
				        	modify_tname = select_thname;
				        	Thread get_theme=new Thread(new Runnable(){
		    					public void run()
		    					{
		    						wifi = isNetworkConnected(MainActivity.this);
			        		        if(wifi){
		    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
		    						request6.addProperty("uname", ac);
		    						
		    						request6.addProperty("t_name",select_thname );
		    						nowtname = select_thname;
		    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    						env6.bodyOut=request6;
		    						env6.dotNet=true;
		    						HttpTransportSE htc6=new HttpTransportSE(URL);
		    						try
		    						{
		    							SOAP_ACTION6=NAMESPACE+METHOD6;
		    							htc6.call(SOAP_ACTION6, env6);
		    							String result=env6.getResponse().toString();
		    							Message msg=new Message();
		    							msg.what=5;
		    							msg.obj=result;
		    							tHandler.sendMessage(msg);
		    							
		    						}catch(Exception e){
		    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		    						}
		    						
		    					}else{
		    						//無網路
		    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
		    						
		    					}
		    					}
		    					
		    				});
				        	get_theme.start();


				        } 
				        public void onNothingSelected(AdapterView arg0) { 
				        Toast.makeText(MainActivity.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show(); 
				         } 
				         }); 
					
			
			
			
	        status = 5;
	        
	        tHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
						case 1:
							
							break;
						case 2:
							result2=msg.obj.toString();
							if(result2.length() < 7){
							refreshlist();
	                    		Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();
							}else {
		                    	Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();

							}
							break;
						case 3:
							Toast.makeText(MainActivity.this, "您選擇的環境設定是"+select_thname, Toast.LENGTH_SHORT).show(); 
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r = Integer.parseInt(theme_val_list.get(0).toString()); 
							g = Integer.parseInt(theme_val_list.get(1).toString()); 
							B = Integer.parseInt(theme_val_list.get(2).toString()); 
							a = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd = theme_val_list.get(8).toString();
							str_isremind = theme_val_list.get(9).toString();
							str_light = theme_val_list.get(10).toString();
					
							
							if( (str_isadd.equals("true") && !isAdded)){
								
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
				                 
							}else if((str_isadd.equals("false") && isAdded)){
								wm.removeView(btn_floatView);
								isAdded = false;
								a = 0;
			        			r = 0;
			        			g = 0;
			        			B = 0;
							}else if((str_isadd.equals("true") && isAdded)){
								wm.removeView(btn_floatView);
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
							}
							
							if(str_isremind.equals("true")){
								
					        	rc = new RemindCount(re_time,1000);
					        	rc.cancel();
					        	rc.start();
					        	isremind = true;
					        }
							
							if(str_light.equals("auto")){
					        	str_light = "auto";
					        	mc = new MyCount(3000, 1000);
					    		mc.start(); 
				    			// 先將亮度設定模式調整為手動
					    		Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	
				            	setmode = 1;
				    			
				    			
				    			//呼叫感光元件	        			
				    			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

				    		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
				    		     if(LightSensor != null){
				    		    	 //有感光元件
				    		    	 
				    			      mySensorManager.registerListener(
				    			        LightSensorListener, 
				    			        LightSensor, 
				    			        SensorManager.SENSOR_DELAY_NORMAL);
				    			      
				    			   
					        			light_status = true;
				    			     }else{
				    			    	//無感光元件
				    		     }
				    		    
				    			
					        }else if(str_light.equals("manually")){
					        	str_light = "manually";
				    			
				    			// 先將亮度設定模式調整為手動
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(getContentResolver(),
				           	    Settings.System.SCREEN_BRIGHTNESS, brightness);
				            	setmode = 2;
				    			
					        }else if(str_light.equals("sys_auto")){
					        	str_light = "sys_auto";
					        	setmode = 3;
					        	Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					        }
							
							
							break;
						case 4:
							result2=msg.obj.toString();
	                    	
	                    	refresh();
							break;
						case 5:
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r1 = Integer.parseInt(theme_val_list.get(0).toString()); 
							g1 = Integer.parseInt(theme_val_list.get(1).toString()); 
							B1 = Integer.parseInt(theme_val_list.get(2).toString()); 
							a1 = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time1 = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status1 = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode1 = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness1 = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd1 = theme_val_list.get(8).toString();
							str_isremind1 = theme_val_list.get(9).toString();
							str_light1 = theme_val_list.get(10).toString();
							
							if(str_isadd1.equals("true")){
								th_isadd = "開啟";
							}else{
								th_isadd = "關閉";
							}
							
							if(str_isremind1.equals("true")){
								th_isremind = "開啟";
							}else{
								th_isremind = "關閉";
							}
							
							if(setmode1 == 1){
								th_islight = "自動";
							}else if(setmode1 == 2){
								th_islight = "手動";
							}else if(setmode1 == 3){
								th_islight = "系統自動";
							}else if(setmode1 == 0){
								th_islight = "尚未設定";
							}
							
							value.setText("環境設定: " + select_thname + "\n"
										+ "濾光值:  紅:" + r1 + " 綠:" + g1 + " 藍:" + B1 + " 濾波值:" + a1 + "狀態: " + th_isadd + "\n" 
									    + "提醒:  " + "狀態:" + th_isremind + " 時間:" + (re_time1/60000) + "分" + "\n"
									    + "亮度:  " + "狀態:" + th_islight + " 亮度值:" + brightness1);
							
							break;
					}
				}
			};
	   
	        theme_setting.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	if(theme_name.length() != 9){
	            	Thread thr=new Thread(new Runnable(){
    					public void run()
    					{
    						wifi = isNetworkConnected(MainActivity.this);
	        		        if(wifi){
    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
    						request6.addProperty("uname", ac);
    						
    						request6.addProperty("t_name",select_thname );
    						nowtname = select_thname;
    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    						env6.bodyOut=request6;
    						env6.dotNet=true;
    						HttpTransportSE htc6=new HttpTransportSE(URL);
    						try
    						{
    							SOAP_ACTION6=NAMESPACE+METHOD6;
    							htc6.call(SOAP_ACTION6, env6);
    							String result=env6.getResponse().toString();
    							Message msg=new Message();
    							msg.what=3;
    							msg.obj=result;
    							tHandler.sendMessage(msg);
    							
    						}catch(Exception e){
    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
    						}
    						
    					}else{
    						//無網路
    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
    						
    					}
    					}
    					
    				});
	            	thr.start();
	            }else {
					Toast.makeText(MainActivity.this, "要先有儲存過的環境設定才可以設定喔!", Toast.LENGTH_SHORT).show();

	            	}
	            	} 
	            });
	        
	        theme_modify.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	//未完成自動更新
	            	final EditText editText = new EditText(MainActivity.this);
	            	if(list.size()>0){
	            	new AlertDialog.Builder(MainActivity.this).setTitle("確定修改" + select_thname + "環境設定嗎?")
	            	.setPositiveButton("確定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	Thread modify=new Thread(new Runnable(){
	        					public void run()
	        					{		
	        						wifi = isNetworkConnected(MainActivity.this);
	    	        		        if(wifi){
	        						SoapObject request9=new SoapObject(NAMESPACE,METHOD9);
	        						request9.addProperty("uname", account);
	        						
	        						
										now_brightness = brightness;
									
	        						request9.addProperty("r_color",Integer.toString(r));
	        						request9.addProperty("g_color",Integer.toString(g));
	        						request9.addProperty("b_color",Integer.toString(B));
	        						request9.addProperty("a_color",Integer.toString(a));
	        						request9.addProperty("retime",Integer.toString(re_time));
	        						request9.addProperty("restatus",Integer.toString(re_status));
	        						request9.addProperty("setmode",Integer.toString(setmode));
	        						request9.addProperty("brightness",Integer.toString(now_brightness));
	        						
	        						
	        						request9.addProperty("t_name", modify_tname);
	        						request9.addProperty("str_isadd",str_isadd);
	        						request9.addProperty("str_isremind",str_isremind);
	        						request9.addProperty("str_light",str_light);
	        						 
	        						
	        						
	        						SoapSerializationEnvelope env9=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        						env9.bodyOut=request9;
	        						env9.dotNet=true;
	        						HttpTransportSE htc9=new HttpTransportSE(URL);
	        						try
	        						{
	        							SOAP_ACTION9=NAMESPACE+METHOD9;
	        							htc9.call(SOAP_ACTION9, env9);
	        							String result9=env9.getResponse().toString();
	        							Message msg=new Message();
	        							msg.what=2;
	        							msg.obj=result9;
	        							tHandler.sendMessage(msg);
	        						}catch(Exception e){
	        							
	        						}

	        					}else{
	        						//無網路
	        						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	        						
	        					}
	    	        		        
	        					}	
	        				});
	                    	
	                    	modify.start();
	                    	//重新整理
	                    	
	                         
	                          
	                    }
	                }).setNegativeButton("取消", null).show();
	            	}else{
						Toast.makeText(MainActivity.this, "您沒有環境可以修改囉~", Toast.LENGTH_SHORT).show();

	            	}
	            	
	            }  
	        });
	        
	        theme_save.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	//未完成自動更新
	            	final EditText editText = new EditText(MainActivity.this);
	            	new AlertDialog.Builder(MainActivity.this).setTitle("請输入環境設定名稱")
	            	.setView(editText).setPositiveButton("確定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	Thread tttt=new Thread(new Runnable(){
	        					public void run()
	        					{		
	        						wifi = isNetworkConnected(MainActivity.this);
	    	        		        if(wifi){
	        						SoapObject request4=new SoapObject(NAMESPACE,METHOD4);
	        						request4.addProperty("uname", account);
	        						request4.addProperty("pwd",password);
	        						now_brightness = brightness;
	        						request4.addProperty("r_color",Integer.toString(r));
	        						request4.addProperty("g_color",Integer.toString(g));
	        						request4.addProperty("b_color",Integer.toString(B));
	        						request4.addProperty("a_color",Integer.toString(a));
	        						request4.addProperty("retime",Integer.toString(re_time));
	        						request4.addProperty("restatus",Integer.toString(re_status));
	        						request4.addProperty("setmode",Integer.toString(setmode));
	        						request4.addProperty("brightness",Integer.toString(now_brightness));
	        						tname = editText.getText().toString();
	        						
	        						request4.addProperty("t_name", tname);
	        						request4.addProperty("str_isadd",str_isadd);
	        						request4.addProperty("str_isremind",str_isremind);
	        						request4.addProperty("str_light",str_light);
	        						 
	        						
	        						
	        						SoapSerializationEnvelope env4=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        						env4.bodyOut=request4;
	        						env4.dotNet=true;
	        						HttpTransportSE htc4=new HttpTransportSE(URL);
	        						try
	        						{
	        							SOAP_ACTION4=NAMESPACE+METHOD4;
	        							htc4.call(SOAP_ACTION4, env4);
	        							String result4=env4.getResponse().toString();
	        							Message msg=new Message();
	        							msg.what=2;
	        							msg.obj=result4;
	        							tHandler.sendMessage(msg);
	        						}catch(Exception e){
	        							
	        						}

	        					}else{
	        						//無網路
	        						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	        						
	        					}
	    	        		        
	        					}	
	        				});
	                    	
	                    	tttt.start();
	                    	//重新整理
	                    	
	                         
	                          
	                    }
	                }).setNegativeButton("取消", null).show();
	            		
	            	
	            }  
	        });
	        
	        theme_back.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	rightMoveHandle5();	                
	                jumpToLayout(leftAnimation);
	                status = 0;
	  	            //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();

	            }
	        });
	        
	        
	        
	        theme_delete.setOnClickListener(new Button.OnClickListener() {  
	        	
	            public void onClick(View v) {
	            	if(list.size()>0){
	            	new AlertDialog.Builder(MainActivity.this).setTitle("確定刪除" + select_thname  +"環境設定嗎")
	            	.setPositiveButton("確定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	if(theme_name.length() != 9){
	    		            	Thread delete=new Thread(new Runnable(){
	    	    					public void run()
	    	    					{
	    	    						wifi = isNetworkConnected(MainActivity.this);
	    		        		        if(wifi){
	    	    						SoapObject request8=new SoapObject(NAMESPACE,METHOD8);
	    	    						request8.addProperty("uname", ac);
	    	    						delete_theme = select_thname;
	    	    						request8.addProperty("t_name",delete_theme );
	    	    						
	    	    						SoapSerializationEnvelope env8=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	    						env8.bodyOut=request8;
	    	    						env8.dotNet=true;
	    	    						HttpTransportSE htc8=new HttpTransportSE(URL);
	    	    						try
	    	    						{
	    	    							SOAP_ACTION8=NAMESPACE+METHOD8;
	    	    							htc8.call(SOAP_ACTION8, env8);
	    	    							String result8=env8.getResponse().toString();
	    	    							Message msg=new Message();
	    	    							msg.what=4;
	    	    							msg.obj=result;
	    	    							tHandler.sendMessage(msg);
	    	    							
	    	    						}catch(Exception e){
	    	    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
	    	    						}
	    	    						
	    	    					}else{
	    	    						//無網路
	    	    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	    	    						
	    	    					}
	    	    					}
	    	    					
	    	    				});
	    		            	delete.start();
	    		            	
	    		            }
	                    }
	                }).setNegativeButton("取消", null).show();
	            }else{
					Toast.makeText(MainActivity.this, "您沒有環境設定可以刪囉~", Toast.LENGTH_SHORT).show();
	            }
	        }
	        });
	        
	    }
	    
	    public void jumpToLayout5_sick(Rotate3d rightAnimation){
	    	setContentView(R.layout.goggle_theme);
	    	
	    	layout5 = (ViewGroup) findViewById(R.id.theme_layout);  
	        layout5.startAnimation(rightAnimation);
	        final TextView tv1 = (TextView)findViewById(R.id.textView1);
	        final TextView tv2 = (TextView)findViewById(R.id.textView2);
	        final TextView tv3 = (TextView)findViewById(R.id.theme);
	        tv2.setText("特殊病因");
	        tv1.setText("以下是醫師為會員所設定的特殊病因環境，可供會員選擇。");
	        tv3.setText("現有的病因:");
	        final Button theme_save = (Button)findViewById(R.id.theme_save);
	        final Button theme_back = (Button)findViewById(R.id.theme_back);
	        final Button theme_setting = (Button)findViewById(R.id.theme_setting);
	        
	        final Button theme_delete = (Button)findViewById(R.id.theme_delete);
	        final Button theme_modify = (Button)findViewById(R.id.theme_modify);
	        sp = (Spinner)findViewById(R.id.spinner1);  
	        
	        final TextView value = (TextView)findViewById(R.id.theme_tv);
			final TextView text = (TextView)findViewById(R.id.theme);
			
			theme_save.setVisibility(View.INVISIBLE);
			theme_delete.setVisibility(View.INVISIBLE);
			theme_modify.setVisibility(View.INVISIBLE);
			refreshlist_sick();
			if(nowtname.length() != 0){
				tname_idx = adapter.getPosition(nowtname);
				sp.setSelection(tname_idx);
			}
			
					sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
				        public void onItemSelected(AdapterView adapterView, View view, int position, long id){ 
				            //取得選取值可以使用「getSelectedItem()」或「getSelectedItemPosition()」；設定預設值則使用「setSelection()」。
				        	adapterView.setSelection(position);
				        	select_thname = adapterView.getSelectedItem().toString();
				        	modify_tname = select_thname;
				        	Thread get_theme=new Thread(new Runnable(){
		    					public void run()
		    					{
		    						
		    						wifi = isNetworkConnected(MainActivity.this);
			        		        if(wifi){
		    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
		    						request6.addProperty("uname", ac);
		    						
		    						request6.addProperty("t_name",select_thname);
		    						nowtname = select_thname;
		    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    						env6.bodyOut=request6;
		    						env6.dotNet=true;
		    						HttpTransportSE htc6 =new HttpTransportSE(URL2);
		    						try
		    						{
		    							SOAP_ACTION6=NAMESPACE+METHOD6;
		    							htc6.call(SOAP_ACTION6, env6);
		    							String result=env6.getResponse().toString();
		    							Message msg=new Message();
		    							msg.what=5;
		    							msg.obj=result;
		    							tHandler.sendMessage(msg);
		    							
		    						}catch(Exception e){
		    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		    						}
		    						
		    					}else{
		    						//無網路
		    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
		    						
		    					}
		    					}
		    					
		    				});
				        	get_theme.start();


				        } 
				        public void onNothingSelected(AdapterView arg0) { 
				        Toast.makeText(MainActivity.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show(); 
				         } 
				         }); 
					
			
			
			
	        status = 5;
	        
	        tHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
						case 1:
							
							break;
						case 2:
							result2=msg.obj.toString();
							if(result2.length() < 7){
							refreshlist_sick();
	                    		Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();
							}else {
		                    	Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();

							}
							break;
						case 3:
							Toast.makeText(MainActivity.this, "您選擇的環境設定是"+select_thname, Toast.LENGTH_SHORT).show(); 
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r = Integer.parseInt(theme_val_list.get(0).toString()); 
							g = Integer.parseInt(theme_val_list.get(1).toString()); 
							B = Integer.parseInt(theme_val_list.get(2).toString()); 
							a = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd = theme_val_list.get(8).toString();
							str_isremind = theme_val_list.get(9).toString();
							str_light = theme_val_list.get(10).toString();
					
							
							if( (str_isadd.equals("true") && !isAdded)){
								
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
				                 
							}else if((str_isadd.equals("false") && isAdded)){
								wm.removeView(btn_floatView);
								isAdded = false;
								a = 0;
			        			r = 0;
			        			g = 0;
			        			B = 0;
							}else if((str_isadd.equals("true") && isAdded)){
								wm.removeView(btn_floatView);
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
							}
							
							if(str_isremind.equals("true")){
								
					        	rc = new RemindCount(re_time,1000);
					        	rc.cancel();
					        	rc.start();
					        	isremind = true;
					        }
							
							if(str_light.equals("auto")){
					        	str_light = "auto";
					        	mc = new MyCount(3000, 1000);
					    		mc.start(); 
				    			// 先將亮度設定模式調整為手動
					    		Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	
				            	setmode = 1;
				    			
				    			
				    			//呼叫感光元件	        			
				    			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

				    		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
				    		     if(LightSensor != null){
				    		    	 //有感光元件
				    		    	 
				    			      mySensorManager.registerListener(
				    			        LightSensorListener, 
				    			        LightSensor, 
				    			        SensorManager.SENSOR_DELAY_NORMAL);
				    			      
				    			   
					        			light_status = true;
				    			     }else{
				    			    	//無感光元件
				    		     }
				    		    
				    			
					        }else if(str_light.equals("manually")){
					        	str_light = "manually";
				    			
				    			// 先將亮度設定模式調整為手動
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(getContentResolver(),
				           	    Settings.System.SCREEN_BRIGHTNESS, brightness);
				            	setmode = 2;
				    			
					        }else if(str_light.equals("sys_auto")){
					        	str_light = "sys_auto";
					        	setmode = 3;
					        	Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					        }
							
							
							break;
						case 4:
							result2=msg.obj.toString();
	                    	
	                    	refresh_sick();
							break;
						case 5:
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r1 = Integer.parseInt(theme_val_list.get(0).toString()); 
							g1 = Integer.parseInt(theme_val_list.get(1).toString()); 
							B1 = Integer.parseInt(theme_val_list.get(2).toString()); 
							a1 = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time1 = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status1 = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode1 = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness1 = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd1 = theme_val_list.get(8).toString();
							str_isremind1 = theme_val_list.get(9).toString();
							str_light1 = theme_val_list.get(10).toString();
							
							if(str_isadd1.equals("true")){
								th_isadd = "開啟";
							}else{
								th_isadd = "關閉";
							}
							
							if(str_isremind1.equals("true")){
								th_isremind = "開啟";
							}else{
								th_isremind = "關閉";
							}
							
							if(setmode1 == 1){
								th_islight = "自動";
							}else if(setmode1 == 2){
								th_islight = "手動";
							}else if(setmode1 == 3){
								th_islight = "系統自動";
							}else if(setmode1 == 0){
								th_islight = "尚未設定";
							}
							
							value.setText("環境設定: " + select_thname + "\n"
										+ "濾光值:  紅:" + r1 + " 綠:" + g1 + " 藍:" + B1 + " 濾波值:" + a1 + "狀態: " + th_isadd + "\n" 
									    + "提醒:  " + "狀態:" + th_isremind + " 時間:" + (re_time1/60000) + "分" + "\n"
									    + "亮度:  " + "狀態:" + th_islight + " 亮度值:" + brightness1);
							
							break;
					}
				}
			};
	   
	        theme_setting.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	if(theme_name.length() != 9){
	            	Thread thr=new Thread(new Runnable(){
    					public void run()
    					{
    						wifi = isNetworkConnected(MainActivity.this);
	        		        if(wifi){
    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
    						request6.addProperty("uname", ac);
    						
    						request6.addProperty("t_name",select_thname );
    						nowtname = select_thname;
    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    						env6.bodyOut=request6;
    						env6.dotNet=true;
    						HttpTransportSE htc6=new HttpTransportSE(URL2);
    						try
    						{
    							SOAP_ACTION6=NAMESPACE+METHOD6;
    							htc6.call(SOAP_ACTION6, env6);
    							String result=env6.getResponse().toString();
    							Message msg=new Message();
    							msg.what=3;
    							msg.obj=result;
    							tHandler.sendMessage(msg);
    							
    						}catch(Exception e){
    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
    						}
    						
    					}else{
    						//無網路
    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
    						
    					}
    					}
    					
    				});
	            	thr.start();
	            }else {
					Toast.makeText(MainActivity.this, "要先有儲存過的環境設定才可以設定喔!", Toast.LENGTH_SHORT).show();

	            	}
	            	} 
	            });
	        
	        
	       
	        theme_back.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	rightMoveHandle5();	                
	                jumpToLayout(leftAnimation);
	                status = 0;
	  	            //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();

	            }
	        });
	        
	        
	        
	        
	    }
	    
	    public void jumpToLayout6add(Rotate3d rightAnimation){
	    	setContentView(R.layout.login_add);
	    	layout6add = (ViewGroup) findViewById(R.id.addlayout);  
	        layout6add.startAnimation(rightAnimation);  
	        
	        final TextView add_tv_ac = (TextView)findViewById(R.id.login_add_ac);
	        final TextView add_tv_pwd = (TextView)findViewById(R.id.login_add_pwd);
	        final TextView add_tv_result = (TextView)findViewById(R.id.add_result);
	        final EditText add_et_ac = (EditText)findViewById(R.id.add_ac);
	        final EditText add_et_pwd = (EditText)findViewById(R.id.add_pwd);
	        final EditText add_et_email = (EditText)findViewById(R.id.add_email);
	        final EditText add_et_year = (EditText)findViewById(R.id.add_year);
	        final EditText add_et_month = (EditText)findViewById(R.id.add_month);
	        final EditText add_et_day = (EditText)findViewById(R.id.add_day);
	        final Button add = (Button)findViewById(R.id.add);
	        final Button clear = (Button)findViewById(R.id.clear);
	        final Button login = (Button)findViewById(R.id.add_login);
	        
	        
	        	add_et_month.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
					if(add_et_month.length() != 0){
					int l = Integer.parseInt(add_et_month.getText().toString());
					if(l > 12){
						add_et_month.setText("12");
						add_et_month.setSelection(2);
						l = 12;
					}
					add_et_month.setSelection(Integer.toString(l).length());
					} 
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				});
	        	add_et_day.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
					if(add_et_day.length() != 0){
					int l = Integer.parseInt(add_et_day.getText().toString());
					if(add_et_month.getText().toString().length() > 0){
					if(add_et_month.getText().toString().equals("1") || add_et_month.getText().toString().equals("3") || add_et_month.getText().toString().equals("5") || add_et_month.getText().toString().equals("7") || add_et_month.getText().toString().equals("8") || add_et_month.getText().toString().equals("10") || add_et_month.getText().toString().equals("12")){
					if(l > 31){
						add_et_day.setText("31");
						add_et_day.setSelection(2);
						l = 31;
					}
					}else if(add_et_month.getText().toString().equals("4") || add_et_month.getText().toString().equals("6") || add_et_month.getText().toString().equals("9") || add_et_month.getText().toString().equals("11") ){
						if(l > 30){
							add_et_day.setText("30");
							add_et_day.setSelection(2);
							l = 30;
						}
					}else if (add_et_month.getText().toString().equals("2")){
						if(Integer.parseInt(add_et_year.getText().toString()) % 4 == 0){
						if(l > 29){
							add_et_day.setText("29");
							add_et_day.setSelection(2);
							l = 29;
						}
						}else{
							if(l > 28){
								add_et_day.setText("28");
								add_et_day.setSelection(2);
								l = 28;
							}
						}
					}
					}else {
	        			Toast.makeText(MainActivity.this, "請先輸入日期喔", Toast.LENGTH_SHORT).show();

					}
					add_et_day.setSelection(Integer.toString(l).length());
					} 
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				});

	        
	        
	        
	        add_et_email.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable edt){ 
	        		// TODO Auto-generated method stub
	        		if (Linkify.addLinks(add_et_email.getText(), Linkify.EMAIL_ADDRESSES))
	        		{
	        			
	        			checkemail = true;
	        		}
	        		else
	        		{
	        			
	        			checkemail = false;
	        		}
	        		
	        	}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){
					
				}
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					
				}
	        });
	        
	        
	        status = 7;
	        addHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
						case 1:
							result2=msg.obj.toString();
							
							add_tv_result.setText(result2);
							break;
						
					}
				}
			};
	        
	        
	        Button.OnClickListener add_listener = new Button.OnClickListener(){
	        	public void onClick(View v){
	        		switch(v.getId())
	        		{
	        			case R.id.add:
	        				wifi = isNetworkConnected(MainActivity.this);
	        		        if(wifi){
	        		        if(add_et_ac.length() <= 4  ){
	    	        			Toast.makeText(MainActivity.this, "帳號至少需要5字元", Toast.LENGTH_SHORT).show();
	    	        			break;
	        		        }else if(add_et_pwd.length() <= 4){
	    	        			Toast.makeText(MainActivity.this, "密碼至少需要5字元", Toast.LENGTH_SHORT).show();
	    	        			break;
	        		        }else if (isLetterDigit(add_et_ac.getText().toString()).equals("false") ){
	        		        	Toast.makeText(MainActivity.this, "帳號需要英數混合", Toast.LENGTH_SHORT).show();
	    	        			break;
	        		        }else if(isLetterDigit(add_et_pwd.getText().toString()).equals("false") ){
	        		        	Toast.makeText(MainActivity.this, "密碼需要英數混合", Toast.LENGTH_SHORT).show();
	    	        			break;
	        		        }else if(!checkemail){
	        		        	Toast.makeText(MainActivity.this, "email請輸入正確", Toast.LENGTH_SHORT).show();
	    	        			break;
	        		        }else if(add_et_year.length() == 0 || add_et_month.length() == 0 || add_et_day.length() == 0){
	        		        	Toast.makeText(MainActivity.this, "生日的欄位不可空白", Toast.LENGTH_SHORT).show();
	    	        			break;
	        		        }else if(add_et_ac.length() > 4 && add_et_pwd.length() > 4 && checkemail){
		        				Thread tt=new Thread(new Runnable(){
		        					public void run()
		        					{		        						
		        						SoapObject request2=new SoapObject(NAMESPACE,METHOD2);
		        						ac2 = add_et_ac.getText().toString();
		        						account = ac2.toString();
		        						pwd2 = add_et_pwd.getText().toString();
		        						password = pwd2.toString();
		        						email = add_et_email.getText().toString();
		        						year = Integer.parseInt(add_et_year.getText().toString());
		        						request2.addProperty("uname", ac2);
		        						request2.addProperty("pwd",pwd2);
		        						request2.addProperty("mail",email);
		        						request2.addProperty("ages",year);
		        						SoapSerializationEnvelope env2=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        						env2.bodyOut=request2;
		        						env2.dotNet=true;
		        						HttpTransportSE htc2=new HttpTransportSE(URL);
		        						try
		        						{
		        							SOAP_ACTION2=NAMESPACE+METHOD2;
		        							htc2.call(SOAP_ACTION2, env2);
		        							String result2=env2.getResponse().toString();
		        							Message msg=new Message();
		        							msg.what=1;
		        							msg.obj=result2;
		        							addHandler.sendMessage(msg);
		        						}catch(Exception e){
		        							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		        						}
		        					}
		        				});
		        				tt.start();
		        				break;
	        				}else{
    							Toast.makeText(MainActivity.this, "資料輸入不正確", Toast.LENGTH_SHORT).show();
	        					break;
	        				}

	        				}else{
	        		        	//無網路
	        					Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	        					break;
	        		        }
	        			case R.id.clear:
	        				add_et_ac.setText("");
	        				add_et_pwd.setText("");
	        				add_et_email.setText("");
	        				break;
	        			case R.id.add_login:
	        				rightMoveHandle6add();
		    	    		jumpToLayout6(leftAnimation);
		    	    		break;
	        	}
	        }
	        
	    };
	    add.setOnClickListener(add_listener);
	    clear.setOnClickListener(add_listener);
	    login.setOnClickListener(add_listener);
	    }
	    private final SensorEventListener LightSensorListener
	     = new SensorEventListener(){

	   @Override
	   public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // TODO Auto-generated method stub

	   }

	   @Override
	   public void onSensorChanged(SensorEvent event) {
	    if(event.sensor.getType() == Sensor.TYPE_LIGHT && setmode == 1){
	    	brightness =(int)(event.values[0] / 3.14);
	    	mc.cancel();
	    	mc.start();
	    	if(login_status){
	    		Thread tttt=new Thread(new Runnable(){
					public void run()
					{		
						wifi = isNetworkConnected(MainActivity.this);
        		        if(wifi){
						SoapObject request3=new SoapObject(NAMESPACE,METHOD3);
						request3.addProperty("uname", account);
						request3.addProperty("pwd",password);						
						request3.addProperty("light",brightness);
						SoapSerializationEnvelope env3=new SoapSerializationEnvelope(SoapEnvelope.VER11);
						env3.bodyOut=request3;
						env3.dotNet=true;
						HttpTransportSE htc3=new HttpTransportSE(URL);
						try
						{
							SOAP_ACTION3=NAMESPACE+METHOD3;
							htc3.call(SOAP_ACTION3, env3);
							String result3=env3.getResponse().toString();
							
						}catch(Exception e){
							
							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
						}
					}else{
						//無網路
						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
						
					}
					}
				});
				tttt.start();
	    	}

	    }
	    if(status == 4){
	    	refresh_light();
	    }
	   }

	    };
	    class MyCount extends CountDownTimer {   
			public MyCount(long millisInFuture, long countDownInterval) {   
				super(millisInFuture, countDownInterval);   
			}   
			@Override   
			public void onFinish() {
				
				Settings.System.putInt(getContentResolver(),
         	    		 Settings.System.SCREEN_BRIGHTNESS, brightness);
				
			}   
			@Override   
			public void onTick(long millisUntilFinished) {   
				 
			}  
		}
	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    	  
	    	  
	    	 // 判斷是否按下Back  
	    	      if (keyCode == KeyEvent.KEYCODE_BACK) {  
	    	    // 是否要退出  
	    	    	  if(status == 0){
	    	    		  if(isExit == false ) {  
	    	  
	    	               isExit = true; //記錄下一次要退出  
	    	  
	    	               Toast.makeText(this, "再按一次Back退出APP"  
	    	  
	    	                    , Toast.LENGTH_SHORT).show();  
	    	  
	    	   // 如果超過兩秒則恢復預設值           
	    	               	if(!hasTask) { 
	    	            	   timerExit.schedule(task, 2000);  
	    	  
	    	               }  
	    	  
	    	         } else {  
	    	  
	    	              finish(); // 離開程式  
	    	  
	    	              System.exit(0);  
	    	  
	    	         }
	    	    	  }else if(status == 1){
	    	    		  rightMoveHandle1();
	  	                  jumpToLayout(leftAnimation);
	  	                  status = 0;
	  	                //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	    	    	  }
	    	    	  else if(status == 2){
	    	    		  rightMoveHandle2();
	  	                  jumpToLayout(leftAnimation);
	  	                  status = 0;
	  	                //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	    	    		  
	    	    	  }else if(status == 3){
	    	    		  
	    	    		  rightMoveHandle3();
	    	    		  jumpToLayout(leftAnimation);
	    	    		  status =0;
	    	    		  //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	    	    	  }else if(status == 4){
	    	    		  rightMoveHandle4();	                
	  	                  jumpToLayout(leftAnimation);
	  	                  status = 0;
	  	                //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	    	    	  }else if(status == 5){
	    	    		  rightMoveHandle5();	                
	  	                  jumpToLayout(leftAnimation);
	  	                  status = 0;
	  	                //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	    	    	  }else if(status == 6){
	    	    		  rightMoveHandle6();	                
	  	                  jumpToLayout(leftAnimation);
	  	                  status = 0;
	  	                //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	    	    	  }else if(status == 7){
	    	    		  rightMoveHandle6add();
	    	    		  jumpToLayout6(leftAnimation);
	    	    		  status = 6;
	    	    		  //Toast.makeText(MainActivity.this,"登入",Toast.LENGTH_SHORT).show();
	    	    	  }else if(status == 8){
	    	    		  rightMoveHandlenfc();
	    	    		  jumpToLayout(leftAnimation);
	    	    		  status = 0;
	    	    	  }else if(status == 9){
	    	    		  rightMoveHandle5select_one();
	    	    		  jumpToLayout(leftAnimation);
	    	    		  status = 0;
	    	    	  }
	    	  
	    	      }  
	    	  
	    	      return false;  
	    	  
	    	}  
	    	    
	    class RemindCount extends CountDownTimer {   
			public RemindCount(long millisInFuture, long countDownInterval) {   
				super(millisInFuture, countDownInterval);   
			}   
			@Override   
			public void onFinish() {
				remind();
				rc.cancel();
			}   
			@Override   
			public void onTick(long millisUntilFinished) {   
				
			}  
		}
	    
	    public void remind(){
	        AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this); //創建訊息方塊
	        ad.setCancelable(false); 
	        ad.setTitle("提醒");
	        ad.setMessage("休息時間到囉!" + "\n" + "該起來動一動身體囉!");
	        ad.setPositiveButton("關閉提醒", new DialogInterface.OnClickListener() { //按"關閉提醒",則退出應用程式
	            public void onClick(DialogInterface dialog, int i) {
	            	re_cdt_statue = 0;
    				str_isremind = "false";
    				isremind = false;
	       }
	     });
	        ad.setNegativeButton("繼續提醒",new DialogInterface.OnClickListener() { //按"繼續提醒",則不執行任何操作
	            public void onClick(DialogInterface dialog, int i) {
	            	rc.start();
	       }
	     });
	        AlertDialog alert = ad.create();
	        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//
	        alert.setCanceledOnTouchOutside(false);
	        alert.show();//顯示訊息視窗
	  }
	    
	    
	    //自動更新  
	    public void refreshlist(){
	    	
	        themeHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
						case 1:
							result=msg.obj.toString();
							theme_name = result;
							break;
					}
				}
			};
	        
	    	//連線取得所有環境設定
	    	Thread gettheme=new Thread(new Runnable(){
				public void run()
				{
					wifi = isNetworkConnected(MainActivity.this);
    		        if(wifi){
    		        	SoapObject request5=new SoapObject(NAMESPACE,METHOD5);
			
    		        	request5.addProperty("uname", ac);
    		        	request5.addProperty("pwd",pwd );
    		        	SoapSerializationEnvelope env5=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		        	env5.bodyOut=request5;
    		        	env5.dotNet=true;
    		        	HttpTransportSE htc5=new HttpTransportSE(URL);
    		        	try
    		        	{
    		        		SOAP_ACTION5=NAMESPACE+METHOD5;
    		        		htc5.call(SOAP_ACTION5, env5);
    		        		String result5=env5.getResponse().toString();
    		        		Message msg=new Message();
    		        		msg.what=1;
    		        		msg.obj=result5;
    		        		themeHandler.sendMessage(msg);	        							
    		        	}catch(Exception e){
    		        		Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
    		        	}
			
    		        }else{
					//無網路
    		        		Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
					
    		        }
				}
			});
			 gettheme.start();
	    	//
			 
	    	
	    	adapter=new ArrayAdapter(this,android.R.layout.simple_gallery_item,list2);
			adapter.setDropDownViewResource(R.layout.sp_theme);
			adapter.notifyDataSetChanged();
			adapter.setNotifyOnChange(true);
			sp.setPrompt("環境設定");
			sp.setAdapter(adapter);
			list.clear();
			for(int q = 0;q < theme_name.length(); q++){
				String s = theme_name.substring(q,q+1);
								
				if(s.equals("=")){
					 q1 = q;
				}else if(s.equals(";")){
					 q2 = q;
				}
				if(q2 > q1){
				list.add(theme_name.substring(q1+1, q2));
				q1 = 0;
				q2 = 0;
				}
			}
			
			
			
					adapter=new ArrayAdapter(this,android.R.layout.simple_gallery_item,list);
					adapter.setDropDownViewResource(R.layout.sp_theme);
					adapter.notifyDataSetChanged();
					adapter.setNotifyOnChange(true);
					sp.setPrompt("環境設定");
					sp.setAdapter(adapter);
					if(tname.length() != 0 ){
					adapter.add(tname);  
		            position = adapter.getPosition(tname);
		            sp.setSelection(position);
		            tname = "";
					}
					if(delete_theme.length() != 0 ){
						position = adapter.getPosition(tname) - 1 ;
						adapter.remove(delete_theme);
			            sp.setSelection(position);
			            delete_theme = "";
					}
	    }
	    
	  
	    //特殊病因
	    public void refreshlist_sick(){
	        themeHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
						case 1:
							result=msg.obj.toString();
							theme_name = result;
							adapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_gallery_item,list2);
							adapter.setDropDownViewResource(R.layout.sp_theme);
							adapter.notifyDataSetChanged();
							adapter.setNotifyOnChange(true);
							sp.setPrompt("環境設定");
							sp.setAdapter(adapter);
							list.clear();
							for(int q = 0;q < theme_name.length(); q++){
								String s = theme_name.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								list.add(theme_name.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							adapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_gallery_item,list);
							adapter.setDropDownViewResource(R.layout.sp_theme);
							adapter.notifyDataSetChanged();
							adapter.setNotifyOnChange(true);
							sp.setPrompt("環境設定");
							sp.setAdapter(adapter);
							if(tname.length() != 0 ){
							adapter.add(tname);  
				            position = adapter.getPosition(tname);
				            sp.setSelection(position);
				            tname = "";
							}
							if(delete_theme.length() != 0 ){
								position = adapter.getPosition(tname) - 1 ;
								adapter.remove(delete_theme);
					            sp.setSelection(position);
					            delete_theme = "";
							}
							break;
					}
				}
			};
	        
	    	//連線取得所有環境設定
	    	Thread gettheme=new Thread(new Runnable(){
				public void run()
				{
					wifi = isNetworkConnected(MainActivity.this);
    		        if(wifi){
    		        	SoapObject request5=new SoapObject(NAMESPACE,METHOD5);
			
    		        	request5.addProperty("uname", ac);
    		        	request5.addProperty("pwd",pwd );
    		        	SoapSerializationEnvelope env5=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		        	env5.bodyOut=request5;
    		        	env5.dotNet=true;
    		        	HttpTransportSE htc5=new HttpTransportSE(URL2);
    		        	try
    		        	{
    		        		SOAP_ACTION5=NAMESPACE+METHOD5;
    		        		htc5.call(SOAP_ACTION5, env5);
    		        		String result5=env5.getResponse().toString();
    		        		Message msg=new Message();
    		        		msg.what=1;
    		        		msg.obj=result5;
    		        		themeHandler.sendMessage(msg);	        							
    		        	}catch(Exception e){
    		        		Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
    		        	}
			
    		        }else{
					//無網路
    		        		Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
					
    		        }
				}
			});
			 gettheme.start();
	    	
			 
	    	
	    	
			
			
			
					
	    }
	    
	    public void refresh(){
	    	setContentView(R.layout.goggle_theme);
	    	
	    	layout5 = (ViewGroup) findViewById(R.id.theme_layout);  
	        

	        
	        
	        final Button theme_save = (Button)findViewById(R.id.theme_save);
	        final Button theme_back = (Button)findViewById(R.id.theme_back);
	        final Button theme_setting = (Button)findViewById(R.id.theme_setting);
	        
	        final Button theme_delete = (Button)findViewById(R.id.theme_delete);
	        final Button theme_modify = (Button)findViewById(R.id.theme_modify);
	        sp = (Spinner)findViewById(R.id.spinner1); 
	        //假如無法即時更新  請取消下行註解
	        //theme_delete.setText("  ");
	        final TextView value = (TextView)findViewById(R.id.theme_tv);
			final TextView text = (TextView)findViewById(R.id.theme);
			
			
			refreshlist();
			if(nowtname.length() != 0){
				tname_idx = adapter.getPosition(nowtname);
				sp.setSelection(tname_idx);
			} 
              
					sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
				        public void onItemSelected(AdapterView adapterView, View view, int position, long id){ 
				            //取得選取值可以使用「getSelectedItem()」或「getSelectedItemPosition()」；設定預設值則使用「setSelection()」。
				        	adapterView.setSelection(position);
				        	select_thname = adapterView.getSelectedItem().toString();
				        	modify_tname = select_thname;
				        	Thread get_theme=new Thread(new Runnable(){
		    					public void run()
		    					{
		    						wifi = isNetworkConnected(MainActivity.this);
			        		        if(wifi){
		    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
		    						request6.addProperty("uname", ac);
		    						
		    						request6.addProperty("t_name",select_thname );
		    						nowtname = select_thname;
		    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    						env6.bodyOut=request6;
		    						env6.dotNet=true;
		    						HttpTransportSE htc6=new HttpTransportSE(URL);
		    						try
		    						{
		    							SOAP_ACTION6=NAMESPACE+METHOD6;
		    							htc6.call(SOAP_ACTION6, env6);
		    							String result=env6.getResponse().toString();
		    							Message msg=new Message();
		    							msg.what=5;
		    							msg.obj=result;
		    							tHandler.sendMessage(msg);
		    							
		    						}catch(Exception e){
		    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		    						}
		    						
		    					}else{
		    						//無網路
		    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
		    						
		    					}
		    					}
		    					
		    				});
				        	get_theme.start();


				        } 
				        public void onNothingSelected(AdapterView arg0) { 
				        Toast.makeText(MainActivity.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show(); 
				         } 
				         }); 
					
			
			
			
	        status = 5;
	        
	        tHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
						case 1:
							
							break;
						case 2:
							result2=msg.obj.toString();
							
							result2=msg.obj.toString();
							if(result2.length() < 7){
							refreshlist();
	                    		Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();
							}else {
		                    	Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();

							}
							break;
						case 3:
							Toast.makeText(MainActivity.this, "您選擇的環境設定是"+select_thname, Toast.LENGTH_SHORT).show(); 
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r = Integer.parseInt(theme_val_list.get(0).toString()); 
							g = Integer.parseInt(theme_val_list.get(1).toString()); 
							B = Integer.parseInt(theme_val_list.get(2).toString()); 
							a = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd = theme_val_list.get(8).toString();
							str_isremind = theme_val_list.get(9).toString();
							str_light = theme_val_list.get(10).toString();
					
							
							if( (str_isadd.equals("true") && !isAdded)){
								
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
				                 
							}else if((str_isadd.equals("false") && isAdded)){
								wm.removeView(btn_floatView);
								isAdded = false;
								a = 0;
			        			r = 0;
			        			g = 0;
			        			B = 0;
							}else if((str_isadd.equals("true") && isAdded)){
								wm.removeView(btn_floatView);
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
							}
							
							if(str_isremind.equals("true")){
								
					        	rc = new RemindCount(re_time,1000);
					        	rc.cancel();
					        	rc.start();
					        	isremind = true;
					        }
							
							if(str_light.equals("auto")){
					        	str_light = "auto";
					        	mc = new MyCount(3000, 1000);
					    		mc.start(); 
				    			// 先將亮度設定模式調整為手動
					    		Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	
				            	setmode = 1;
				    			
				    			
				    			//呼叫感光元件	        			
				    			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

				    		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
				    		     if(LightSensor != null){
				    		    	 //有感光元件
				    		    	 
				    			      mySensorManager.registerListener(
				    			        LightSensorListener, 
				    			        LightSensor, 
				    			        SensorManager.SENSOR_DELAY_NORMAL);
				    			      
				    			   
					        			light_status = true;
				    			     }else{
				    			    	//無感光元件
				    		     }
				    		    
				    			
					        }else if(str_light.equals("manually")){
					        	str_light = "manually";
				    			
				    			// 先將亮度設定模式調整為手動
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(getContentResolver(),
				           	    Settings.System.SCREEN_BRIGHTNESS, brightness);
				            	setmode = 2;
				    			
					        }else if(str_light.equals("sys_auto")){
					        	str_light = "sys_auto";
					        	setmode = 3;
					        	Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					        }
							
							
							break;
						case 4:
							result2=msg.obj.toString();
	                    	
	                    	refresh();
							break;
						case 5:
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r1 = Integer.parseInt(theme_val_list.get(0).toString()); 
							g1 = Integer.parseInt(theme_val_list.get(1).toString()); 
							B1 = Integer.parseInt(theme_val_list.get(2).toString()); 
							a1 = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time1 = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status1 = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode1 = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness1 = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd1 = theme_val_list.get(8).toString();
							str_isremind1 = theme_val_list.get(9).toString();
							str_light1 = theme_val_list.get(10).toString();
							
							if(str_isadd1.equals("true")){
								th_isadd = "開啟";
							}else{
								th_isadd = "關閉";
							}
							
							if(str_isremind1.equals("true")){
								th_isremind = "開啟";
							}else{
								th_isremind = "關閉";
							}
							
							if(setmode1 == 1){
								th_islight = "自動";
							}else if(setmode1 == 2){
								th_islight = "手動";
							}else if(setmode1 == 3){
								th_islight = "系統自動";
							}else if(setmode1 == 0){
								th_islight = "尚未設定";
							}
							
							value.setText("環境設定: " + select_thname + "\n"
										+ "濾光值:  紅:" + r1 + " 綠:" + g1 + " 藍:" + B1 + " 濾波值:" + a1 + "狀態: " + th_isadd + "\n" 
									    + "提醒:  " + "狀態:" + th_isremind + " 時間:" + (re_time1/60000) + "分" + "\n"
									    + "亮度:  " + "狀態:" + th_islight + " 亮度值:" + brightness1);
							
							break;
					}
				}
			};
	   
	        theme_setting.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	if(theme_name.length() != 9 ){
	            	Thread thr=new Thread(new Runnable(){
    					public void run()
    					{
    						wifi = isNetworkConnected(MainActivity.this);
	        		        if(wifi){
    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
    						request6.addProperty("uname", ac);
    						request6.addProperty("t_name",select_thname );
    						nowtname = select_thname;
    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    						env6.bodyOut=request6;
    						env6.dotNet=true;
    						HttpTransportSE htc6=new HttpTransportSE(URL);
    						try
    						{
    							SOAP_ACTION6=NAMESPACE+METHOD6;
    							htc6.call(SOAP_ACTION6, env6);
    							String result=env6.getResponse().toString();
    							Message msg=new Message();
    							msg.what=3;
    							msg.obj=result;
    							tHandler.sendMessage(msg);
    							
    						}catch(Exception e){
    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
    						}
    						
    					}else{
    						//無網路
    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
    						
    					}
    					}
    					
    				});
	            	thr.start();
	            	}else{
						Toast.makeText(MainActivity.this, "要先有儲存過的環境設定才可以設定喔!", Toast.LENGTH_SHORT).show();

	            	}
	            	}
	            });
	      
	        theme_save.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	
	            	final EditText editText = new EditText(MainActivity.this);
	            	new AlertDialog.Builder(MainActivity.this).setTitle("請输入環境設定名稱")
	            	.setView(editText).setPositiveButton("確定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	Thread tttt=new Thread(new Runnable(){
	        					public void run()
	        					{		
	        						wifi = isNetworkConnected(MainActivity.this);
	    	        		        if(wifi){
	        						SoapObject request4=new SoapObject(NAMESPACE,METHOD4);
	        						request4.addProperty("uname", account);
	        						request4.addProperty("pwd",password);
	        						try {
										now_brightness = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
									} catch (SettingNotFoundException e1) {
										// TODO Auto-generated catch block
										now_brightness = brightness;
									}
	        						request4.addProperty("r_color",Integer.toString(r));
	        						request4.addProperty("g_color",Integer.toString(g));
	        						request4.addProperty("b_color",Integer.toString(B));
	        						request4.addProperty("a_color",Integer.toString(a));
	        						request4.addProperty("retime",Integer.toString(re_time));
	        						request4.addProperty("restatus",Integer.toString(re_status));
	        						request4.addProperty("setmode",Integer.toString(setmode));
	        						request4.addProperty("brightness",Integer.toString(now_brightness));
	        						tname = editText.getText().toString();
	        						
	        						request4.addProperty("t_name", tname);
	        						request4.addProperty("str_isadd",str_isadd);
	        						request4.addProperty("str_isremind",str_isremind);
	        						request4.addProperty("str_light",str_light);
	        						 
	        						
	        						
	        						SoapSerializationEnvelope env4=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        						env4.bodyOut=request4;
	        						env4.dotNet=true;
	        						HttpTransportSE htc4=new HttpTransportSE(URL);
	        						try
	        						{
	        							SOAP_ACTION4=NAMESPACE+METHOD4;
	        							htc4.call(SOAP_ACTION4, env4);
	        							String result4=env4.getResponse().toString();
	        							Message msg=new Message();
	        							msg.what=2;
	        							msg.obj=result4;
	        							tHandler.sendMessage(msg);
	        						}catch(Exception e){
	        							
	        						}

	        					}else{
	        						//無網路
	        						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	        						
	        					}
	    	        		        
	        					}	
	        				});
	                    	
	                    	tttt.start();
	                    	//重新整理
	                    	refresh();
	                    	
	                    }
	                }).setNegativeButton("取消", null).show();
	            		
	           
	            }  
	        });
	        
	        theme_back.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	rightMoveHandle5();	                
	                jumpToLayout(leftAnimation);
	                status = 0;
	  	            Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();

	            }
	        });
	        theme_modify.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	//未完成自動更新
	            	final EditText editText = new EditText(MainActivity.this);
	            	if(list.size()>0){
	            	new AlertDialog.Builder(MainActivity.this).setTitle("確定修改" + select_thname + "環境設定嗎?")
	            	.setPositiveButton("確定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	Thread modify=new Thread(new Runnable(){
	        					public void run()
	        					{		
	        						wifi = isNetworkConnected(MainActivity.this);
	    	        		        if(wifi){
	        						SoapObject request9=new SoapObject(NAMESPACE,METHOD9);
	        						request9.addProperty("uname", account);
	        						
	        						try {
										now_brightness = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
									} catch (SettingNotFoundException e1) {
										// TODO Auto-generated catch block
										now_brightness = brightness;
									}
	        						request9.addProperty("r_color",Integer.toString(r));
	        						request9.addProperty("g_color",Integer.toString(g));
	        						request9.addProperty("b_color",Integer.toString(B));
	        						request9.addProperty("a_color",Integer.toString(a));
	        						request9.addProperty("retime",Integer.toString(re_time));
	        						request9.addProperty("restatus",Integer.toString(re_status));
	        						request9.addProperty("setmode",Integer.toString(setmode));
	        						request9.addProperty("brightness",Integer.toString(now_brightness));
	        						request9.addProperty("t_name", modify_tname);
	        						request9.addProperty("str_isadd",str_isadd);
	        						request9.addProperty("str_isremind",str_isremind);
	        						request9.addProperty("str_light",str_light);
	        						 
	        						
	        						
	        						SoapSerializationEnvelope env9=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        						env9.bodyOut=request9;
	        						env9.dotNet=true;
	        						HttpTransportSE htc9=new HttpTransportSE(URL);
	        						try
	        						{
	        							SOAP_ACTION9=NAMESPACE+METHOD9;
	        							htc9.call(SOAP_ACTION9, env9);
	        							String result9=env9.getResponse().toString();
	        							Message msg=new Message();
	        							msg.what=2;
	        							msg.obj=result9;
	        							tHandler.sendMessage(msg);
	        						}catch(Exception e){
	        							
	        						}

	        					}else{
	        						//無網路
	        						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	        						
	        					}
	    	        		        
	        					}	
	        				});
	                    	
	                    	modify.start();
	                    	//重新整理
	                    	
	                         
	                          
	                    }
	                }).setNegativeButton("取消", null).show();
	            	}else{
						Toast.makeText(MainActivity.this, "您沒有環境可以修改囉~", Toast.LENGTH_SHORT).show();
	            	}
	            	
	            }  
	        });
	       
	        theme_delete.setOnClickListener(new Button.OnClickListener() {  
	        	
	            public void onClick(View v) {
	            	//未測試
	            	if(list.size()>0){
	            	new AlertDialog.Builder(MainActivity.this).setTitle("確定刪除" + select_thname  +"環境設定嗎")
	            	.setPositiveButton("確定", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	if(theme_name.length() != 9){
	    		            	Thread delete=new Thread(new Runnable(){
	    	    					public void run()
	    	    					{
	    	    						wifi = isNetworkConnected(MainActivity.this);
	    		        		        if(wifi){
	    	    						SoapObject request8=new SoapObject(NAMESPACE,METHOD8);
	    	    						request8.addProperty("uname", ac);
	    	    						delete_theme = select_thname;
	    	    						request8.addProperty("t_name",delete_theme );
	    	    						
	    	    						SoapSerializationEnvelope env8=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	    						env8.bodyOut=request8;
	    	    						env8.dotNet=true;
	    	    						HttpTransportSE htc8=new HttpTransportSE(URL);
	    	    						try
	    	    						{
	    	    							SOAP_ACTION8=NAMESPACE+METHOD8;
	    	    							htc8.call(SOAP_ACTION8, env8);
	    	    							String result8=env8.getResponse().toString();
	    	    							Message msg=new Message();
	    	    							msg.what=4;
	    	    							msg.obj=result;
	    	    							tHandler.sendMessage(msg);
	    	    							
	    	    						}catch(Exception e){
	    	    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
	    	    						}
	    	    						
	    	    					}else{
	    	    						//無網路
	    	    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	    	    						
	    	    					}
	    	    					}
	    	    					
	    	    				});
	    		            	delete.start();
	    		            	
	    		            }
	                    }
	                }).setNegativeButton("取消", null).show();
	            } else{
					Toast.makeText(MainActivity.this, "您沒有環境設定可以刪囉~", Toast.LENGTH_SHORT).show();
	            }
	        }
	        });
	    
	    }
	   
	    public void refresh_sick(){
	    	setContentView(R.layout.goggle_theme);
	    	
	    	layout5 = (ViewGroup) findViewById(R.id.theme_layout);  
	        

	        
	        
	        final Button theme_save = (Button)findViewById(R.id.theme_save);
	        final Button theme_back = (Button)findViewById(R.id.theme_back);
	        final Button theme_setting = (Button)findViewById(R.id.theme_setting);
	        
	        final Button theme_delete = (Button)findViewById(R.id.theme_delete);
	        final Button theme_modify = (Button)findViewById(R.id.theme_modify);
	        sp = (Spinner)findViewById(R.id.spinner1); 
	        //假如無法即時更新  請取消下行註解
	        //theme_delete.setText("  ");
	        final TextView value = (TextView)findViewById(R.id.theme_tv);
			final TextView text = (TextView)findViewById(R.id.theme);
			theme_save.setVisibility(View.INVISIBLE);
			theme_delete.setVisibility(View.INVISIBLE);
			theme_modify.setVisibility(View.INVISIBLE);
			refreshlist_sick();
			if(nowtname.length() != 0){
				tname_idx = adapter.getPosition(nowtname);
				sp.setSelection(tname_idx);
			} 
              
					sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
				        public void onItemSelected(AdapterView adapterView, View view, int position, long id){ 
				            //取得選取值可以使用「getSelectedItem()」或「getSelectedItemPosition()」；設定預設值則使用「setSelection()」。
				        	adapterView.setSelection(position);
				        	select_thname = adapterView.getSelectedItem().toString();
				        	modify_tname = select_thname;
				        	Thread get_theme=new Thread(new Runnable(){
		    					public void run()
		    					{
		    						wifi = isNetworkConnected(MainActivity.this);
			        		        if(wifi){
		    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
		    						request6.addProperty("uname", ac);
		    						
		    						request6.addProperty("t_name",select_thname );
		    						nowtname = select_thname;
		    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    						env6.bodyOut=request6;
		    						env6.dotNet=true;
		    						HttpTransportSE htc6=new HttpTransportSE(URL2);
		    						try
		    						{
		    							SOAP_ACTION6=NAMESPACE+METHOD6;
		    							htc6.call(SOAP_ACTION6, env6);
		    							String result=env6.getResponse().toString();
		    							Message msg=new Message();
		    							msg.what=5;
		    							msg.obj=result;
		    							tHandler.sendMessage(msg);
		    							
		    						}catch(Exception e){
		    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		    						}
		    						
		    					}else{
		    						//無網路
		    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
		    						
		    					}
		    					}
		    					
		    				});
				        	get_theme.start();


				        } 
				        public void onNothingSelected(AdapterView arg0) { 
				        Toast.makeText(MainActivity.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show(); 
				         } 
				         }); 
					
			
			
			
	        status = 5;
	        
	        tHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
						case 1:
							
							break;
						case 2:
							result2=msg.obj.toString();
							
							result2=msg.obj.toString();
							if(result2.length() < 7){
							refreshlist();
	                    		Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();
							}else {
		                    	Toast.makeText(MainActivity.this,result2,Toast.LENGTH_SHORT).show();

							}
							break;
						case 3:
							Toast.makeText(MainActivity.this, "您選擇的環境設定是"+select_thname, Toast.LENGTH_SHORT).show(); 
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r = Integer.parseInt(theme_val_list.get(0).toString()); 
							g = Integer.parseInt(theme_val_list.get(1).toString()); 
							B = Integer.parseInt(theme_val_list.get(2).toString()); 
							a = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd = theme_val_list.get(8).toString();
							str_isremind = theme_val_list.get(9).toString();
							str_light = theme_val_list.get(10).toString();
					
							
							if( (str_isadd.equals("true") && !isAdded)){
								
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
				                 
							}else if((str_isadd.equals("false") && isAdded)){
								wm.removeView(btn_floatView);
								isAdded = false;
								a = 0;
			        			r = 0;
			        			g = 0;
			        			B = 0;
							}else if((str_isadd.equals("true") && isAdded)){
								wm.removeView(btn_floatView);
								btn_floatView = new Button(getApplicationContext());
				                
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
				                
				                wm = (WindowManager) getApplicationContext().getSystemService(
				                        Context.WINDOW_SERVICE);
				                
				                params = new WindowManager.LayoutParams();
				        	
				             // 设置window type
				                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
				                /*
				                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
				                 * 即拉下通知栏不可见
				                 */

				                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
				               

				                // 设置Window flag
				               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
				                
				                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
				                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
				                 
				                 wm.addView(btn_floatView, params);
				                 isAdded = true;
				                   
				                 wm.updateViewLayout(btn_floatView, params);
							}
							
							if(str_isremind.equals("true")){
								
					        	rc = new RemindCount(re_time,1000);
					        	rc.cancel();
					        	rc.start();
					        	isremind = true;
					        }
							
							if(str_light.equals("auto")){
					        	str_light = "auto";
					        	mc = new MyCount(3000, 1000);
					    		mc.start(); 
				    			// 先將亮度設定模式調整為手動
					    		Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	
				            	setmode = 1;
				    			
				    			
				    			//呼叫感光元件	        			
				    			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

				    		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
				    		     if(LightSensor != null){
				    		    	 //有感光元件
				    		    	 
				    			      mySensorManager.registerListener(
				    			        LightSensorListener, 
				    			        LightSensor, 
				    			        SensorManager.SENSOR_DELAY_NORMAL);
				    			      
				    			   
					        			light_status = true;
				    			     }else{
				    			    	//無感光元件
				    		     }
				    		    
				    			
					        }else if(str_light.equals("manually")){
					        	str_light = "manually";
				    			
				    			// 先將亮度設定模式調整為手動
				            	Settings.System.putInt(
				                getContentResolver(),
				                Settings.System.SCREEN_BRIGHTNESS_MODE,
				                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				            	Settings.System.putInt(getContentResolver(),
				           	    Settings.System.SCREEN_BRIGHTNESS, brightness);
				            	setmode = 2;
				    			
					        }else if(str_light.equals("sys_auto")){
					        	str_light = "sys_auto";
					        	setmode = 3;
					        	Settings.System.putInt(
					                    getContentResolver(),
					                    Settings.System.SCREEN_BRIGHTNESS_MODE,
					                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					        }
							
							
							break;
						case 4:
							result2=msg.obj.toString();
	                    	
	                    	refresh_sick();
							break;
						case 5:
							result2=msg.obj.toString();
							
							//順序
							theme_val_list.clear();
							for(int q = 0;q < result2.length(); q++){
								String s = result2.substring(q,q+1);
												
								if(s.equals("=")){
									 q1 = q;
								}else if(s.equals(";")){
									 q2 = q;
								}
								if(q2 > q1){
								theme_val_list.add(result2.substring(q1+1, q2));
								q1 = 0;
								q2 = 0;
								}
							}
							
							
							r1 = Integer.parseInt(theme_val_list.get(0).toString()); 
							g1 = Integer.parseInt(theme_val_list.get(1).toString()); 
							B1 = Integer.parseInt(theme_val_list.get(2).toString()); 
							a1 = Integer.parseInt(theme_val_list.get(3).toString()); 
							re_time1 = Integer.parseInt(theme_val_list.get(4).toString()); 
							re_status1 = Integer.parseInt(theme_val_list.get(5).toString()); 
							setmode1 = Integer.parseInt(theme_val_list.get(6).toString()); 
							brightness1 = Integer.parseInt(theme_val_list.get(7).toString()); 
							str_isadd1 = theme_val_list.get(8).toString();
							str_isremind1 = theme_val_list.get(9).toString();
							str_light1 = theme_val_list.get(10).toString();
							
							if(str_isadd1.equals("true")){
								th_isadd = "開啟";
							}else{
								th_isadd = "關閉";
							}
							
							if(str_isremind1.equals("true")){
								th_isremind = "開啟";
							}else{
								th_isremind = "關閉";
							}
							
							if(setmode1 == 1){
								th_islight = "自動";
							}else if(setmode1 == 2){
								th_islight = "手動";
							}else if(setmode1 == 3){
								th_islight = "系統自動";
							}else if(setmode1 == 0){
								th_islight = "尚未設定";
							}
							
							value.setText("環境設定: " + select_thname + "\n"
										+ "濾光值:  紅:" + r1 + " 綠:" + g1 + " 藍:" + B1 + " 濾波值:" + a1 + "狀態: " + th_isadd + "\n" 
									    + "提醒:  " + "狀態:" + th_isremind + " 時間:" + (re_time1/60000) + "分" + "\n"
									    + "亮度:  " + "狀態:" + th_islight + " 亮度值:" + brightness1);
							
							break;
					}
				}
			};
	   
	        theme_setting.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	if(theme_name.length() != 9 ){
	            	Thread thr=new Thread(new Runnable(){
    					public void run()
    					{
    						wifi = isNetworkConnected(MainActivity.this);
	        		        if(wifi){
    						SoapObject request6=new SoapObject(NAMESPACE,METHOD6);
    						request6.addProperty("uname", ac);
    						request6.addProperty("t_name",select_thname );
    						nowtname = select_thname;
    						SoapSerializationEnvelope env6=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    						env6.bodyOut=request6;
    						env6.dotNet=true;
    						HttpTransportSE htc6=new HttpTransportSE(URL2);
    						try
    						{
    							SOAP_ACTION6=NAMESPACE+METHOD6;
    							htc6.call(SOAP_ACTION6, env6);
    							String result=env6.getResponse().toString();
    							Message msg=new Message();
    							msg.what=3;
    							msg.obj=result;
    							tHandler.sendMessage(msg);
    							
    						}catch(Exception e){
    							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
    						}
    						
    					}else{
    						//無網路
    						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
    						
    					}
    					}
    					
    				});
	            	thr.start();
	            	}else{
						Toast.makeText(MainActivity.this, "要先有儲存過的環境設定才可以設定喔!", Toast.LENGTH_SHORT).show();

	            	}
	            	}
	            });
	      
	       
	        theme_back.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	rightMoveHandle5();	                
	                jumpToLayout(leftAnimation);
	                status = 0;
	  	            Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();

	            }
	        });
	        
	        
	    }
	   
	    private void initViews() {
	    	btnColorPicker = (Button) findViewById(R.id.btn_checkcolor);
	    	btnColorPicker.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog = new ColorPickerDialog(context, Color.argb(a, r, g, B), 
							getResources().getString(R.string.btn_color_picker), 
							new ColorPickerDialog.OnColorChangedListener() {
						
						@Override
						public void colorChanged(int color) {
							//a = Color.alpha(color);
							r =(int) (Color.red(color) / 2.55);
							g = (int)(Color.green(color) / 2.55);
							B = (int)(Color.blue(color) / 2.55);
							if(isAdded){
				                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
							}
							
			                //重新載入layout2
							
						        setContentView(R.layout.lightfiler);  
						        layout2 = (ViewGroup) findViewById(R.id.test1);  
						        status = 2;
						        final EditText et_R =(EditText)findViewById(R.id.editText1);
						        final EditText et_B =(EditText)findViewById(R.id.EditText01);
						        final EditText et_G =(EditText)findViewById(R.id.EditText02);
						        final EditText et_A =(EditText)findViewById(R.id.EditText03);
						        final SeekBar sb1 = (SeekBar)findViewById(R.id.seekBar1);
						        final SeekBar sb2 = (SeekBar)findViewById(R.id.seekBar2);
						        final SeekBar sb3 = (SeekBar)findViewById(R.id.seekBar3);	        
								final SeekBar sb4 = (SeekBar)findViewById(R.id.seekBar4);
								final Button b1 = (Button)findViewById(R.id.button1);
						        final Button b2 = (Button) findViewById(R.id.button2);
						        final Button b3 = (Button) findViewById(R.id.button3);  
							
						        final Button green = (Button)findViewById(R.id.button4);
						        final Button yellow = (Button)findViewById(R.id.button5);
						        final Button orange = (Button)findViewById(R.id.button6);
						        
						        
						        initViews();
						       
						        context = MainActivity.this;
								sb1.setProgress(r);
								sb2.setProgress(g);
								sb3.setProgress(B);
								sb4.setProgress(a);
								et_R.setText(Integer.toString(r));
								et_B.setText(Integer.toString(B));
								et_G.setText(Integer.toString(g));
								et_A.setText(Integer.toString(a));
								//複製
								
								if( str_isadd.equals("true") && !isAdded ){
									
									btn_floatView = new Button(getApplicationContext());
					                
					                btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
					                
					                wm = (WindowManager) getApplicationContext().getSystemService(
					                        Context.WINDOW_SERVICE);
					                params = new WindowManager.LayoutParams();
					        	
					             // 设置window type
					                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
					                /*
					                 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
					                 * 即拉下通知栏不可见
					                 */

					                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
					               

					                // 设置Window flag
					               /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
					                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
					                
					                // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
					                 params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
					                		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
					                		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
					                		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
					                 
					                 wm.addView(btn_floatView, params);
					                 isAdded = true;
					                   
					                 wm.updateViewLayout(btn_floatView, params);
					                 Toast.makeText(MainActivity.this,"開啟濾光",Toast.LENGTH_SHORT).show();
					                 b1.setEnabled(false);
					                 b2.setEnabled(true);
								}
								if(isAdded){
									
									b1.setEnabled(false);
								}
								
								et_R.addTextChangedListener(new TextWatcher(){
									public void afterTextChanged(Editable edt){ 
										if(et_R.length() != 0){
										int l = Integer.parseInt(et_R.getText().toString());
										if(l > 100){
											et_R.setText("100");
											et_R.setSelection(3);
										}
										sb1.setProgress(l);
										et_R.setSelection(Integer.toString(l).length());
										} 
									}
									public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
									public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
									});
								et_B.addTextChangedListener(new TextWatcher(){
									public void afterTextChanged(Editable edt){ 
										if(et_B.length() != 0){
										int l = Integer.parseInt(et_B.getText().toString());
										if(l > 100){
											et_B.setText("100");
											et_B.setSelection(3);
										}
										sb3.setProgress(l);
										et_B.setSelection(Integer.toString(l).length());
										} 
									}
									public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
									public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
									});
								
								et_G.addTextChangedListener(new TextWatcher(){
									public void afterTextChanged(Editable edt){ 
										if(et_G.length() != 0){
										int l = Integer.parseInt(et_G.getText().toString());
										if(l > 100){
											et_G.setText("100");
											et_G.setSelection(3);
										}
										sb2.setProgress(l);
										et_G.setSelection(Integer.toString(l).length());
										} 
									}
									public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
									public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
									});
								
								et_A.addTextChangedListener(new TextWatcher(){
									public void afterTextChanged(Editable edt){ 
										if(et_A.length() != 0){
											int l = Integer.parseInt(et_A.getText().toString());
											if(l > 80){
												et_A.setText("80");
												et_A.setSelection(2);
											}
											sb4.setProgress(l);
											et_A.setSelection(Integer.toString(l).length());
											} 
									}
									public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){
										
									}
									public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
										
									}
									});
								
						        sb1.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
						        @Override
						        public void onProgressChanged(SeekBar sb1, int progresValue, boolean fromUser) { 
						        	progress = progresValue;
						        	r = progresValue;
						        	et_R.setText(Integer.toString(r));
						        	if(isAdded){
						        		
						        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
						        	}
						        	}
						      @Override
						      public void onStartTrackingTouch(SeekBar sb1) {
						        
						      }

						      @Override
						      public void onStopTrackingTouch(SeekBar sb1) {
						        
						    	  
						      }
						  });
						        
						        sb2.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
						        @Override
						      public void onProgressChanged(SeekBar sb2, int progresValue, boolean fromUser) { 
						        	progress = progresValue;
						        	g = progresValue;
						        	et_G.setText(Integer.toString(g));
						        	if(isAdded){
						        		
						        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
						        	}
						        }
						      @Override
						      public void onStartTrackingTouch(SeekBar sb2) {
						        
						      }

						      @Override
						      public void onStopTrackingTouch(SeekBar sb2) {
						        
						    	  
						      }
						  });
					        
								sb3.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
						        @Override
						      public void onProgressChanged(SeekBar sb3, int progresValue, boolean fromUser) { 
						        	progress = progresValue;
						        	B = progresValue;
						        	et_B.setText(Integer.toString(B));
						        	if(isAdded){
						        		
						        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
						        	}
						        }
						      @Override
						      public void onStartTrackingTouch(SeekBar sb3) {
						        
						      }

						      @Override
						      public void onStopTrackingTouch(SeekBar sb1) {
						        
						    	  
						      }
						  });
								sb4.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
						        @Override
						      public void onProgressChanged(SeekBar sb4, int progresValue, boolean fromUser) { 
						        	progress = progresValue;
						        	a = progress;	        	
						        	et_A.setText(Integer.toString(a));
						        	if(isAdded){
						        		
						        		btn_floatView.setBackgroundColor(Color.argb((int)(sb4.getProgress() * 2.55), (int)(sb1.getProgress() * 2.55), (int)(sb2.getProgress() * 2.55),(int) (sb3.getProgress() * 2.55)));
						        	}
						        }
						      @Override
						      public void onStartTrackingTouch(SeekBar sb4) {
						        
						      }

						      @Override
						      public void onStopTrackingTouch(SeekBar sb1) {
						        
						    	  
						      }
						  });
						        
						        b1.setOnClickListener(new Button.OnClickListener() {  
						            public void onClick(View v) {  
						            	if(!isAdded){
						            		
						            		btn_floatView = new Button(getApplicationContext());
						                    
						                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
						                    
						                    wm = (WindowManager) getApplicationContext().getSystemService(
						                            Context.WINDOW_SERVICE);
						                    params = new WindowManager.LayoutParams();
						            	
						                 // 设置window type
						                    params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
						                    /*
						                     * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
						                     * 即拉下通知栏不可见
						                     */

						                    params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
						                   

						                    // 设置Window flag
						                   /* params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
						                            WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;*/
						                    
						                    // 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
						                     params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
						                    		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
						                    		 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
						                    		 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
						                     
						                     wm.addView(btn_floatView, params);
						                     
						                     isAdded = true;
						                     str_isadd = "true"; 
						                     
						                     wm.updateViewLayout(btn_floatView, params);
						                     Toast.makeText(MainActivity.this,"開啟濾光",Toast.LENGTH_SHORT).show();
						                     b1.setEnabled(false);
						                     b2.setEnabled(true);
						                     
						            	}
						           }
						            	
						        }); 
						      
						          
						        
						        b2.setOnClickListener(new Button.OnClickListener() {  
						            public void onClick(View v) {  
						            	if(isAdded){
						        			wm.removeView(btn_floatView);
						        			Toast.makeText(MainActivity.this,"關閉濾光",Toast.LENGTH_SHORT).show();
						        			isAdded = false;
						        			str_isadd = "false";
						        			b1.setEnabled(true);
						        			
						        			b2.setEnabled(false);
						        			
						        			} 
						            }  
						        }); 
						        
						        
						        
						        b3.setEnabled(true);  
						        b3.setOnClickListener(new Button.OnClickListener() {  
						            public void onClick(View v) {
					        			//Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
						                rightMoveHandle2();
						                jumpToLayout(leftAnimation);
						            }  
						        });
						        
						        green.setOnClickListener(new Button.OnClickListener() {  
						            public void onClick(View v) {
						            	r = 0;
						            	g = 100;
						            	B = 0;
						            	a = 20;
						            	sb1.setProgress(r);
						    			sb2.setProgress(g);
						    			sb3.setProgress(B);
						    			sb4.setProgress(a);
						    			et_R.setText(Integer.toString(r));
						    			et_B.setText(Integer.toString(B));
						    			et_G.setText(Integer.toString(g));
						    			et_A.setText(Integer.toString(a));
						            	if(isAdded){		        		
						                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
							        	}
						            }  
						        });
						        yellow.setOnClickListener(new Button.OnClickListener() {  
						            public void onClick(View v) {
						            	r = 100;
						            	g = 100;
						            	B = 0;
						            	a = 20;
						            	sb1.setProgress(r);
						    			sb2.setProgress(g);
						    			sb3.setProgress(B);
						    			sb4.setProgress(a);
						    			et_R.setText(Integer.toString(r));
						    			et_B.setText(Integer.toString(B));
						    			et_G.setText(Integer.toString(g));
						    			et_A.setText(Integer.toString(a));
						            	if(isAdded){		        		
						                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
							        	}
						            }  
						        });
						        orange.setOnClickListener(new Button.OnClickListener() {  
						            public void onClick(View v) {
						            	r = 100;
						            	g = 65;
						            	B = 0;
						            	a = 20;
						            	sb1.setProgress(r);
						    			sb2.setProgress(g);
						    			sb3.setProgress(B);
						    			sb4.setProgress(a);
						    			et_R.setText(Integer.toString(r));
						    			et_B.setText(Integer.toString(B));
						    			et_G.setText(Integer.toString(g));
						    			et_A.setText(Integer.toString(a));
						            	if(isAdded){		        		
						                    btn_floatView.setBackgroundColor(Color.argb((int)(a * 2.55),(int) (r * 2.55),(int) (g * 2.55),(int) (B * 2.55)));
							        	}
						            }  
						        });
							
						}
					});
					dialog.show();
				}
			});
	    	
	    }
	    
	    public boolean isNetworkConnected(Context context) {
    		
    		if (context != null) {
    			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
    					.getSystemService(Context.CONNECTIVITY_SERVICE);
    			NetworkInfo mNetworkInfo = mConnectivityManager
    					.getActiveNetworkInfo();
    			if (mNetworkInfo != null) {
    				return mNetworkInfo.isAvailable();
    			}
    		}
    		return false;
    		
    	}
	    
	    
	  //TimerTask無法直接改變元件因此要透過Handler來當橋樑
		private Handler handler = new Handler(){
			 public  void  handleMessage(Message msg) {
				 super.handleMessage(msg);
				 switch(msg.what){
				 case 1:
					 tot_use_time = "已使用" + tot_usetime+ "分";
	                 String s="";                
	                 s=s+tsec+"秒";
	                 break;
	             }
	         }
	    };
	    
	    private TimerTask task_tottime = new TimerTask(){

	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	        	
	        	
	          	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	          	
	          	if(pm.isScreenOn()){
	          		startflag=true;
	          	//抓取時間
	                Calendar c = Calendar.getInstance();
	                int year = c.get(Calendar.YEAR);
	                int month = c.get(Calendar.MONTH) + 1;
	                int day = c.get(Calendar.DAY_OF_MONTH);
	                if(month < 9 && day > 9){
		                today = year + "-0" + month + "-" + day ; 	
	                }else if(month < 9 && day < 9){
	                	today = year + "-0" + month + "-0" + day ;
	                }else if(month > 9 && day < 9){
		                today = year + "-" + month + "-0" + day ;	
	                }else if(month > 9 && day > 9){
		                today = year + "-" + month + "-" + day ;
	                }
	                wifi = isNetworkConnected(MainActivity.this);
    		        if(wifi){
    		        	if(ac.length() != 0){
    		        		if(tmin > 0){
        				Thread time=new Thread(new Runnable(){
        					public void run()
        					{		        						
        						SoapObject request7=new SoapObject(NAMESPACE,METHOD7);
        						request7.addProperty("uname", ac);
        						request7.addProperty("today",today);
        						request7.addProperty("time",tmin);
        						tmin = 0;
        						SoapSerializationEnvelope env7=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        						env7.bodyOut=request7;
        						env7.dotNet=true;
        						HttpTransportSE htc7=new HttpTransportSE(URL1);
        						try
        						{
        							SOAP_ACTION7=NAMESPACE+METHOD7;
        							htc7.call(SOAP_ACTION7, env7);
        							String result7=env7.getResponse().toString();
        							
        						}catch(Exception e){
        							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        						}
        					}
        				});
        				time.start();
        				
    		        		}} 
    		        }
	          		
	          	}else{
	          		startflag=false;
	          		
	          		
	          	}
	            if (startflag){
	                //如果startflag是true則每秒tsec+1
	                tsec++;
	                if(tsec % 60 == 0){
	                	
	                	tot_usetime++;
	                	tmin = 0;
	                	tmin++;
	                	tsec = 0;
	                }
	                Message message = new Message();
	                
	                //傳送訊息1
	                message.what =1;
	                handler.sendMessage(message);
	                
	                
	            }
	        }
	        
	    };
	    private Runnable updateTimer = new Runnable() {
	    	        public void run() {
	    	        	if(status == 3){
	    	            final TextView time = (TextView) findViewById(R.id.use_time);
	    	            
	    	            time.setText(tot_use_time);
	    	            timehandler.postDelayed(this, 1000);
	    	        	}
	    	        }
	    	        	
	    	    };
	    
	  private void refresh_light(){
		  setContentView(R.layout.light);
	        
	        status =4;
	        mc = new MyCount(3000, 1000);
	        mc.start(); 
	        mc.cancel();
	        layout4 = (ViewGroup) findViewById(R.id.rlayout1);  
	          
	        final RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton1);
	        final RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
	        final RadioButton rb3 = (RadioButton)findViewById(R.id.radioButton3);
	        final EditText et1 = (EditText)findViewById(R.id.editText1);
	        final SeekBar sb = (SeekBar)findViewById(R.id.sb1);
	        final Button btl1 = (Button)findViewById(R.id.buttonlight1);
	        final Button btl2 = (Button)findViewById(R.id.buttonback1);
	        sb.setProgress(brightness);
	        et1.setText(Integer.toString(brightness));
	        et1.setEnabled(false);
			sb.setEnabled(false);
			btl1.setEnabled(false);
			//複製
	        if(setmode == 1 ){
	        	rb1.setChecked(true);
	        	rb1.setEnabled(false);
	        }else if(setmode == 2){
	        	rb2.setChecked(true);
	        	et1.setEnabled(true);
  			sb.setEnabled(true);
  			btl1.setEnabled(true);
	        }else if(setmode == 3){
	        	rb3.setChecked(true);
	        	rb3.setEnabled(false);
	        }
	        
	        if(str_light.equals("auto")){
	        	str_light = "auto";
  			rb1.setEnabled(false);
  			rb2.setEnabled(true);
  			rb3.setEnabled(true);
  			// 先將亮度設定模式調整為手動
          	Settings.System.putInt(
              getContentResolver(),
              Settings.System.SCREEN_BRIGHTNESS_MODE,
              Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
          	
          	setmode = 1;
  			et1.setEnabled(false);
  			sb.setEnabled(false);
  			btl1.setEnabled(false);
  			
  			//呼叫感光元件	        			
  			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

  		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
  		     if(LightSensor != null){
  		    	 //有感光元件
  		    	 
  			      mySensorManager.registerListener(
  			        LightSensorListener, 
  			        LightSensor, 
  			        SensorManager.SENSOR_DELAY_NORMAL);
  			      
  			    et1.setEnabled(false);
	        			sb.setEnabled(false);
	        			btl1.setEnabled(false);
	        			light_status = true;
	        			
  			     }else{
  			    	//無感光元件
  		     }
  		    
  			
	        }else if(str_light.equals("manually")){
	        	str_light = "manually";
  			rb2.setEnabled(false);
  			rb1.setEnabled(true);
  			rb3.setEnabled(true);
  			// 先將亮度設定模式調整為手動
          	Settings.System.putInt(
              getContentResolver(),
              Settings.System.SCREEN_BRIGHTNESS_MODE,
              Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
          	
          	setmode = 2;
  			et1.setEnabled(true);
  			sb.setEnabled(true);
	        }else if(str_light.equals("sys_auto")){
	        	str_light = "sys_auto";
	        	setmode = 3;
	        	sb.setProgress(0);
		        et1.setText(Integer.toString(0));
	        	Settings.System.putInt(
	                    getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	        	
	        }
	        
	        
	       
	        btl1.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) {
	            	if(et1.length() != 0){
	                int l = Integer.parseInt(et1.getText().toString());
	                if(l < 256){
	                	str_light = "manually";
	                	sb.setProgress(l);
	                	brightness = l;
	                	Settings.System.putInt(getContentResolver(),
	           	    		 Settings.System.SCREEN_BRIGHTNESS, brightness);
	                	light_status = true;
	                	Toast.makeText(MainActivity.this,"設定螢幕亮度為" + brightness ,Toast.LENGTH_SHORT).show();
	                	if(login_status){
	        	    		Thread tttt=new Thread(new Runnable(){
	        					public void run()
	        					{		
	        						wifi = isNetworkConnected(MainActivity.this);
	                		        if(wifi){
	        						SoapObject request3=new SoapObject(NAMESPACE,METHOD3);
	        						request3.addProperty("uname", account);
	        						request3.addProperty("pwd",password);						
	        						request3.addProperty("light",brightness);
	        						SoapSerializationEnvelope env3=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        						env3.bodyOut=request3;
	        						env3.dotNet=true;
	        						HttpTransportSE htc3=new HttpTransportSE(URL);
	        						try
	        						{
	        							SOAP_ACTION3=NAMESPACE+METHOD3;
	        							htc3.call(SOAP_ACTION3, env3);
	        							String result3=env3.getResponse().toString();
	        							
	        						}catch(Exception e){
	        							
	        							Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
	        						}
	        					}else{
	        						//無網路
	        						Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
	        						
	        					}
	        					}
	        				});
	        				tttt.start();
	        	    	}
	            	} else {
	            		et1.setText("0");
						et1.setSelection(1);
	            	}
	            	}
	            }  
	        });
	        
	        btl2.setOnClickListener(new Button.OnClickListener() {  
	            public void onClick(View v) { 
	            	
	                rightMoveHandle4();	                
	                jumpToLayout(leftAnimation);
	                //Toast.makeText(MainActivity.this,"主畫面",Toast.LENGTH_SHORT).show();
	            }  
	        });
	        
	        
	        
	        
	        RadioButton.OnClickListener listener2 = new RadioButton.OnClickListener(){
	        	public void onClick(View v){
	        		if(rb1.isChecked()){
	        			str_light = "auto";
	        			rb1.setEnabled(false);
	        			rb2.setEnabled(true);
	        			rb3.setEnabled(true);
	        			// 先將亮度設定模式調整為手動
	                	Settings.System.putInt(
	                    getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	                	
	                	
	                	
	                	setmode = 1;
	        			et1.setEnabled(false);
	        			sb.setEnabled(false);
	        			btl1.setEnabled(false);
	        			
	        			//呼叫感光元件	        			
	        			SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

	        		     Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	        		     if(LightSensor != null){
	        		    	 //有感光元件
	        		    	 
	        			      mySensorManager.registerListener(
	        			        LightSensorListener, 
	        			        LightSensor, 
	        			        SensorManager.SENSOR_DELAY_NORMAL);
	        			      
	        			    et1.setEnabled(false);
	  	        			sb.setEnabled(false);
	  	        			btl1.setEnabled(false);
	  	        			light_status = true;
	  	        			Toast.makeText(MainActivity.this,"開啟自動調整亮度",Toast.LENGTH_SHORT).show();
	        			     }else{
	        			    	//無感光元件
	        		     }
	        		    
	        			
	        		} else if (rb2.isChecked()){
	        			str_light = "manually";
	        			rb2.setEnabled(false);
	        			rb1.setEnabled(true);
	        			rb3.setEnabled(true);
	        			// 先將亮度設定模式調整為手動
	                	Settings.System.putInt(
	                    getContentResolver(),
	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	                	Toast.makeText(MainActivity.this,"開啟手動設定亮度",Toast.LENGTH_SHORT).show();
	                	setmode = 2;
	                	Settings.System.putInt(getContentResolver(),
		           	    		 Settings.System.SCREEN_BRIGHTNESS, brightness);
	        			et1.setEnabled(true);
	        			sb.setEnabled(true);
	        			btl1.setEnabled(true);
	        			et1.addTextChangedListener(new TextWatcher(){
	        				public void afterTextChanged(Editable edt){ 
	        					if(et1.length() != 0){
	        					int l = Integer.parseInt(et1.getText().toString());
	        					if(l > 255){
	        						et1.setText("255");
	        						et1.setSelection(3);
	        					}	        					
	        					} 
	        				}
	        				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
	        				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	        				});
	        		} else if (rb3.isChecked()){
	        			str_light = "sys_auto";
	        			setmode = 3;
	        			et1.setEnabled(false);
	        			sb.setEnabled(false);
	        			btl1.setEnabled(false);
	        			rb1.setEnabled(true);
	        			rb2.setEnabled(true);
	        			rb3.setEnabled(false);
	        			sb.setProgress(0);
	    		        et1.setText(Integer.toString(0));
	        			Settings.System.putInt(
	    	                    getContentResolver(),
	    	                    Settings.System.SCREEN_BRIGHTNESS_MODE,
	    	                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	        			
	        		}
	        		
	        	}
	        };
	        rb1.setOnClickListener(listener2);
	        rb2.setOnClickListener(listener2);
	        rb3.setOnClickListener(listener2);
	        sb.setOnSeekBarChangeListener( new OnSeekBarChangeListener() { int progress = 0;
	        @Override
	      public void onProgressChanged(SeekBar sb, int progresValue, boolean fromUser) { 
	        	progress = progresValue;
	        	brightness = progress;
	        	light_status = true;
	        	et1.setText(Integer.toString(progresValue));
	        	et1.setSelection(Integer.toString(progress).length());
	        	Settings.System.putInt(getContentResolver(),
        	    		 Settings.System.SCREEN_BRIGHTNESS, progress);
	        	if(login_status){
		    		Thread tttt=new Thread(new Runnable(){
						public void run()
						{		
							wifi = isNetworkConnected(MainActivity.this);
	        		        if(wifi){
							SoapObject request3=new SoapObject(NAMESPACE,METHOD3);
							request3.addProperty("uname", account);
							request3.addProperty("pwd",password);						
							request3.addProperty("light",brightness);
							SoapSerializationEnvelope env3=new SoapSerializationEnvelope(SoapEnvelope.VER11);
							env3.bodyOut=request3;
							env3.dotNet=true;
							HttpTransportSE htc3=new HttpTransportSE(URL);
							try
							{
								SOAP_ACTION3=NAMESPACE+METHOD3;
								htc3.call(SOAP_ACTION3, env3);
								String result3=env3.getResponse().toString();
								
							}catch(Exception e){
								
								Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
							}
						}else{
							//無網路
							Toast.makeText(MainActivity.this, "請確認網路是否順暢", Toast.LENGTH_SHORT).show();
							
						}
						}
					});
					tttt.start();
		    	}
	        	
	        }
	      @Override
	      public void onStartTrackingTouch(SeekBar sb4) {
	        
	      }

	      @Override
	      public void onStopTrackingTouch(SeekBar sb1) {
	        
	    	  
	      }
	  });
	        
	       
	  }
	  
	  private String isLetterDigit(String str){
   	   boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
   	   String str1;    	   
   	   boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
   	      for(int i =0;i<=str.length()-1;i++){
   	    	  
   	    
   	             if(Character.isDigit(str.charAt(i))){     //用char包装类中的判断数字的方法判断每一个字符
   	                 isDigit = true;  
   	             }
   	             if(Character.isLetter(str.charAt(i))){   //用char包装类中的判断字母的方法判断每一个字符
   	                 isLetter = true;
   	             }
   	           }
   	         String regex = "^[a-zA-Z0-9]+$";
   	        boolean isRight = isDigit && isLetter&&str.matches(regex);
   	  if(isRight){
   		   str1 = "true";
   	  } else{
   		   str1 = "false";
   	  }
   	  return str1;
   	 }

	  
}


