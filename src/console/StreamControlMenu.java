
package console;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.stream.StreamPerformer;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: StreamControlMenu
 *
 * Date Modified: 2021.08.23
 *
 */
public class StreamControlMenu
    extends
        ConsoleMenu
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String MENU_TITLE  = "□システム □入力設定 □出力設定 ■ストリーム操作";
    private static final String MENU_PROMPT = "stm";
    
    private static final String START_STREAM   = "入出力処理開始/再開";
    private static final String STOP_STREAM    = "入出力処理停止";
    private static final String DELETE_STREAM  = "ストリーム削除";
    private static final String SHOW_STREAM_IO = "入出力設定表示";
    private static final String QUIT           = "ストリーム操作終了 → □システム";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private SystemMenu systemMenu = null;
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public StreamControlMenu()
    {
        //
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void setStateInstance(SystemMenu sysMenu)
    {
        this.systemMenu = sysMenu;
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
            new MenuItem(0, START_STREAM, () -> startStream()),
            new MenuItem(1, STOP_STREAM, () -> stopStream()),
            new MenuItem(2, DELETE_STREAM, () -> deleteStream()),
            new MenuItem(),
            new MenuItem(8, SHOW_STREAM_IO, () -> showStreamIo()),
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
    private ConsoleMenu startStream()
    {
        try {
            // 現在選択されている StreamPerformer の処理を開始する．
            StreamPerformer pfmr = ConsoleMenu.getSelectedStream();
            pfmr.start();
        }
        catch (StreamException ex) {
            warningPause("入出力処理を開始できません(%s)．\n", ex.getMessage());
        }
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu stopStream()
    {
        try {
            // 現在選択されている StreamPerformer の処理を停止する．
            StreamPerformer pfmr = ConsoleMenu.getSelectedStream();
            pfmr.stop();
        }
        catch (StreamException ex) {
            warningPause("入出力処理を停止できません(%s)．\n", ex.getMessage());
        }
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu deleteStream()
    {
        // 現在選択されている StreamPerformer を削除する．
        StreamPerformer pfmr = ConsoleMenu.getSelectedStream();
        pfmr.delete();
        
        // 次の状態となる SystemMenu を返す．
        return this.systemMenu;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu showStreamIo()
    {
        // 現在選択されている StreamPerformer の SegmentIo の入出力情報を表示する．
        StreamPerformer pfmr  = ConsoleMenu.getSelectedStream();
        SegmentIo       segIo = pfmr.getSegmentIo();
        printSegmentIo(segIo);
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu quit()
    {
        // 次の状態となる SystemMenu を返す．
        return this.systemMenu;
    }
}