
package trial.console;

import java.util.List;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.protocol.StreamInfo;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.08.19
 *
 */
public class InputSettingsMenu
    extends
        ConsoleMenu
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String MENU_TITLE  = "□システム ■入力設定 □出力設定 □ストリーム操作";
    private static final String MENU_PROMPT = " in";
    
    private static final String ADD_CAPTURE_DEVICE         = "入力デバイス追加";
    private static final String REFRESH_SOURCE_STREAM_INFO = "受信ストリーム情報収集";
    private static final String CONFIGURE_INCOMING_STREAM  = "受信ストリーム追加";
    private static final String TRANSITION_TO_OUTPUT       = "出力設定       → □出力設定";
    private static final String SHOW_SEGMENT_IO            = "入出力設定表示";
    private static final String QUIT                       = "設定キャンセル → □システム";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private SystemMenu         systemMenu         = null;
    private OutputSettingsMenu outputSettingsMenu = null;
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public InputSettingsMenu()
    {
        //
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void setStateInstance(SystemMenu sysMenu, OutputSettingsMenu outMenu)
    {
        this.systemMenu = sysMenu;
        this.outputSettingsMenu = outMenu;
    }
    
// -----------------------------------------------------------------------------
// PROTECTED METHOD:
// -----------------------------------------------------------------------------
    
    // - PROTECTED METHOD ------------------------------------------------------
    //
    @Override
    protected MenuItem[] initMenuItems()
    {
        MenuItem[] menuItems = new MenuItem[] {
            new MenuItem(0, ADD_CAPTURE_DEVICE, () -> addCaptureDevice()),
            new MenuItem(),
            new MenuItem(1, REFRESH_SOURCE_STREAM_INFO, () -> refreshSourceStreamInfo()),
            new MenuItem(2, CONFIGURE_INCOMING_STREAM, () -> configureIncomingStream()),
            new MenuItem(),
            new MenuItem(3, TRANSITION_TO_OUTPUT, () -> transitionToOutput()),
            new MenuItem(),
            new MenuItem(8, SHOW_SEGMENT_IO, () -> showSegmentIo()),
            new MenuItem(9, QUIT, () -> quit())
        };
        return menuItems;
    }
    
    // - PROTECTED METHOD ------------------------------------------------------
    //
    @Override
    protected String getMenuTitle()
    {
        return MENU_TITLE;
    }
    
    // - PROTECTED METHOD ------------------------------------------------------
    //
    @Override
    protected String getPrompt()
    {
        return MENU_PROMPT;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu addCaptureDevice()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // 入力デバイス情報リストを取得する．
        DeviceInfoManager devMgr   = DeviceInfoManager.getInstance();
        List<DeviceInfo>  lsDevInf = devMgr.getDeviceInfoList();
        int               size     = lsDevInf.size();
        if (size <= 0) {
            warningPause("利用可能な入力デバイスがありません．\n");
            return this;
        }
        // 入力デバイス情報リストの各要素を表示する．
        printDeviceInfoList(lsDevInf);
        
        // 入力デバイス情報リストの要素番号を選択する．
        int num = selectNumber("入力デバイス番号");
        if (num >= size) {
            warningPause("キャンセルしました．\n");
            return this;
        }
        // 選択された入力デバイス情報を取得する．
        DeviceInfo devInf = lsDevInf.get(num);
        
        // 選択された入力デバイス情報から出力フォーマット情報リストを取得する．
        List<StreamFormat> lsStmFmt = devInf.getOutputFormatList();
        size = lsStmFmt.size();
        if (size <= 0) {
            warningPause("利用可能な出力フォーマットがありません．\n");
            return this;
        }
        // 出力フォーマット情報リストの各要素を表示する．
        printStreamFormatList(lsStmFmt);
        
        // 出力フォーマット情報リストの要素番号を選択する．
        num = selectNumber("フォーマット番号");
        if (num >= size) {
            warningPause("キャンセルしました．\n");
            return this;
        }
        // 選択した出力フォーマット情報を取得する．
        StreamFormat stmFmt = lsStmFmt.get(num);
        
        // 入力デバイス・出力フォーマット情報を SegmentIo へ追加し，
        // 現在の SegmentIo の入出力情報を表示する．
        segIo.addInputDevice(devInf, stmFmt);
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu refreshSourceStreamInfo()
    {
        // ソースホスト名を設定する．
        // ※スペース区切りで複数指定可
        print("\n");
        message("送信元ホスト名/IPアドレスを入力して下さい．\n");
        print("  複数指定する場合はスペースで区切って入力して下さい．\n");
        String   buf      = getLine("送信元ホスト名/IPアドレス");
        String[] srcAddrs = buf.split("\\s+");
        
        // 送信元ストリーム情報要求パケットを送信する．
        messagePause("送信元ストリーム情報要求パケットを送信します．\n");
        StreamInfoManager manager = StreamInfoManager.getInstance();
        manager.refreshSourceStreamInfoList(srcAddrs);
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu configureIncomingStream()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // 送信元ストリーム情報のリストを取得する．
        StreamInfoManager manager  = StreamInfoManager.getInstance();
        List<StreamInfo>  lsStmInf = manager.getSourceStreamInfoList();
        int               size     = lsStmInf.size();
        if (size <= 0) {
            warningPause("受信可能なストリームがありません．\n");
            return this;
        }
        // 送信元ストリーム情報の配列の各要素を表示する．
        printStreamInfoList("受信できるストリーム一覧", lsStmInf);
        
        // 送信元ストリーム情報の配列要素番号を選択する．
        int num = selectNumber("ストリーム番号");
        if (num >= size) {
            warningPause("キャンセルしました．\n");
            return this;
        }
        // 選択された送信元ストリーム情報を SegmentIo の入力として設定し，
        // 現在の SegmentIo の入出力情報を表示する．
        StreamInfo stmInf = lsStmInf.get(num);
        segIo.configureIncomingStream(stmInf);
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu transitionToOutput()
    {
        ConsoleMenu nextMenu = null;
        SegmentIo   segIo    = ConsoleMenu.getSegmentIo();
        
        List<IoParam> list = segIo.getInputParamList();
        if (list.isEmpty()) {
            // 入力が設定されていない場合，次のメニューは InputSettingsMenu のまま．
            warningPause("入力が設定されていません．\n");
            nextMenu = this;
        }
        else {
            // 入力が設定されている場合，次のメニューは OutputSettingsMenu になる．
            nextMenu = this.outputSettingsMenu;
        }
        return nextMenu;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu quit()
    {
        // 次のメニューとなる SystemMenu を返す．
        return this.systemMenu;
    }
}
