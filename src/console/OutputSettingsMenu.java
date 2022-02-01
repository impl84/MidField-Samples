
package console;

import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.AudioFormat;
import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.IoFormat;
import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.stream.VideoFormat;
import com.midfield_system.api.system.SystemException;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: OutputSettingsMenu
 *
 * Date Modified: 2021.08.23
 *
 */
public class OutputSettingsMenu
    extends
        ConsoleMenu
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String MENU_TITLE  = "□システム □入力設定 ■出力設定 □ストリーム操作";
    private static final String MENU_PROMPT = "out";
    
    private static final String SET_DEFAULT_RENDERER      = "再生/表示設定";
    private static final String CONFIGURE_OUTGOING_STREAM = "送信ストリーム設定";
    private static final String SETUP_STREAM              = "ストリーム生成 → □ストリーム操作";
    private static final String SHOW_SEGMENT_IO           = "入出力設定表示";
    private static final String QUIT                      = "設定キャンセル → □システム";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private SystemMenu        systemMenu        = null;
    private StreamControlMenu streamControlMenu = null;
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public OutputSettingsMenu()
    {
        //
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void setStateInstanue(SystemMenu sysMenu, StreamControlMenu stmMenu)
    {
        this.systemMenu = sysMenu;
        this.streamControlMenu = stmMenu;
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
            new MenuItem(0, SET_DEFAULT_RENDERER, () -> setDefaultRenderer()),
            new MenuItem(1, CONFIGURE_OUTGOING_STREAM, () -> configureOutgoingStream()),
            new MenuItem(),
            new MenuItem(2, SETUP_STREAM, () -> setupStream()),
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
    private ConsoleMenu setDefaultRenderer()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // SegmentIo の出力としてデフォルトレンダラを設定する．
        segIo.configureDefaultRenderer();
        
        // ライブソースオプションを有効にする．
        segIo.setLiveSource(true);
        
        // 現在の SegmentIo の入出力情報を表示する．
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu configureOutgoingStream()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // SegmentIo に設定されている入力情報をもとに，
        // 利用可能な出力フォーマット情報のリストを取得する．
        List<StreamFormat> lsOutFmt = segIo.getOutputVideoFormatList();
        lsOutFmt.addAll(segIo.getOutputAudioFormatList());
        int size = lsOutFmt.size();
        if (size <= 0) {
            warningPause("利用可能な出力フォーマットがありません．\n");
            return this;
        }
        // 出力フォーマット情報リストの各要素を表示する．
        printStreamFormatList(lsOutFmt);
        
        // 出力フォーマット情報リストにビデオフォーマットが含まれている場合
        // ビデオフォーマットを選択する．
        VideoFormat vidFmt = selectVideoFormat(lsOutFmt);
        
        // 出力フォーマット情報リストにオーディオフォーマットが含まれている場合
        // オーディオフォーマットを選択する．
        // ※選択済みのビデオフォーマットが DV/M2TS の場合は，
        // オーディオフォーマットを選択しない．
        AudioFormat audFmt = selectAudioFormat(vidFmt, lsOutFmt);
        if ((vidFmt == null) && (audFmt == null)) {
            return this;
        }
        // SegmentIo の出力を，送信ストリームとして構成する．
        segIo.configureOutgoingStream(vidFmt, audFmt);
        
        // トランスポートプロトコルを選択する．
        ProtocolType type          = ProtocolType.UDP;
        boolean      isTcpSelected = selectTransportProtocol();
        if (isTcpSelected) {
            type = ProtocolType.TCP;
        }
        // 接続の方向を選択する(TCP利用時のみ)．
        boolean isRcvCon = false;
        if (isTcpSelected) {
            isRcvCon = selectConnectionDirection();
        }
        ConnectionMode mode = ConnectionMode.ACTIVE;
        if (isRcvCon) {
            mode = ConnectionMode.PASSIVE;
        }
        // トランスポートプロトコルと接続の方向を設定し，
        // 現在の SegmentIo の入出力情報を表示する．
        segIo.setTransportProtocol(type, mode);
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private VideoFormat selectVideoFormat(List<StreamFormat> lsOutFmt)
    {
        VideoFormat vidFmt = null;
        
        // 与えられた StreamFormat のリストに
        // ビデオフォーマットが含まれているかを確認する．
        boolean isVid = false;
        for (StreamFormat stmFmt : lsOutFmt) {
            if (stmFmt instanceof VideoFormat) {
                isVid = true;
                break;
            }
        }
        if (isVid == false) {
            // 利用可能なビデオフォーマットが無い．
            return vidFmt;
        }
        // ビデオフォーマット番号を選択する．
        int size = lsOutFmt.size();
        int num  = selectNumber("ビデオフォーマット番号");
        if (num >= size) {
            warningPause("コマンドをキャンセルしました．\n");
            return vidFmt;
        }
        // 選択されたビデオフォーマット情報を取得する．
        StreamFormat stmFtm = lsOutFmt.get(num);
        if (stmFtm instanceof VideoFormat) {
            vidFmt = (VideoFormat)stmFtm;
        }
        else {
            warningPause("ビデオフォーマットではありません．\n");
        }
        return vidFmt;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private AudioFormat selectAudioFormat(
        VideoFormat vidFmt, List<StreamFormat> lsOutFmt
    )
    {
        AudioFormat audFmt = null;
        
        // 選択済みのビデオフォーマットが DV/M2TS の場合は，
        // オーディオフォーマットを選択しない．
        if (vidFmt != null) {
            IoFormat ioFmt = vidFmt.getIoFormat();
            if ((ioFmt == IoFormat.DVSD) || (ioFmt == IoFormat.M2TS)) {
                return audFmt;
            }
        }
        // 与えられた StreamFormat のリストに
        // オーディオフォーマットが含まれているかを確認する．
        boolean isAud = false;
        for (StreamFormat stmFmt : lsOutFmt) {
            if (stmFmt instanceof AudioFormat) {
                isAud = true;
                break;
            }
        }
        if (isAud == false) {
            // 利用可能なオーディオフォーマットが無い．
            return audFmt;
        }
        // オーディオフォーマット番号を選択する．
        int size = lsOutFmt.size();
        int num  = selectNumber("オーディオフォーマット番号");
        if (num >= size) {
            warningPause("コマンドをキャンセルしました．\n");
            return audFmt;
        }
        // 選択されたオーディオフォーマット情報を取得する．
        StreamFormat stmFtm = lsOutFmt.get(num);
        if (stmFtm instanceof AudioFormat) {
            audFmt = (AudioFormat)stmFtm;
        }
        else {
            warningPause("オーディオフォーマットではありません．\n");
        }
        return audFmt;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private boolean selectTransportProtocol()
    {
        boolean useTCP = false;
        
        printListTitle("ストリーム転送プロトコル");
        print("  [0] TCP\n");
        print("  [1] UDP\n");
        
        int num = selectNumber("プロトコル番号");
        switch (num) {
        case 0:
            useTCP = true;
            break;
        case 1:
            useTCP = false;
            break;
        default:
            warningPause("コマンドをキャンセルしました．UDPを利用します．\n");
            useTCP = false;
            break;
        }
        return useTCP;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private boolean selectConnectionDirection()
    {
        boolean isRcvCon = false;
        
        printListTitle("コネクション接続方向");
        print("  [0] 受信側から接続をかける\n");
        print("  [1] 送信側から接続をかける\n");
        
        int num = selectNumber("番号");
        switch (num) {
        case 0:
            isRcvCon = true;
            break;
        case 1:
            isRcvCon = false;
            break;
        default:
            warningPause("コマンドをキャンセルしました．受信側から接続をかけます．\n");
            isRcvCon = true;
            break;
        }
        return isRcvCon;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu setupStream()
    {
        // SegmentIo の出力が設定されているかを確認する．
        SegmentIo     segIo = ConsoleMenu.getSegmentIo();
        List<IoParam> list  = segIo.getOutputParamList();
        if (list.isEmpty()) {
            // 出力が設定されていない場合は OutputSettingsMenu のまま
            warningPause("出力が設定されていません．\n");
            return this;
        }
        // プレビューを有効にする．
        segIo.setPreviewer();
        
        // StreamPerformer を生成する．
        StreamPerformer pfmr;
        try {
            pfmr = StreamPerformer.newInstance(segIo);
            // SystemException, StreamException
            
            // ExPerformerFrame を EDT 上で生成する．
            SwingUtilities.invokeLater(() -> { new ExPerformerFrame(pfmr); });
        }
        catch (SystemException | StreamException ex) {
            // ストリーム生成時に例外が発生した．
            warning("ストリームを生成できません(%s)．\n", ex.getMessage());
            return this;
        }
        // ストリームの処理を開始する．
        try {
            pfmr.open();	// StreamException
            pfmr.start();	// StreamException
        }
        catch (StreamException ex) {
            // ストリームの処理開始時にエラーが発生した．
            warning("入出力処理を開始できません(%s)．\n", ex.getMessage());
            return this;
        }
        // 生成したストリームの処理が開始された．
        // 生成したストリームのインスタンスを，ConsoleMenu に設定しておく．
        ConsoleMenu.setSelectedStream(pfmr);
        
        // 新しい StreamPerformer がセットアップできた場合，
        // 次のメニューとなる StreamControlMenu を返す．
        return this.streamControlMenu;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu quit()
    {
        // 次のメニューとなる SystemMenu を返す．
        return this.systemMenu;
    }
}