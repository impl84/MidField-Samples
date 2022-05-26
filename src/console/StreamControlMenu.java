
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
    private static final String MENU_TITLE  = "���V�X�e�� �����͐ݒ� ���o�͐ݒ� ���X�g���[������";
    private static final String MENU_PROMPT = "stm";
    
    private static final String START_STREAM   = "���o�͏����J�n/�ĊJ";
    private static final String STOP_STREAM    = "���o�͏�����~";
    private static final String DELETE_STREAM  = "�X�g���[���폜";
    private static final String SHOW_STREAM_IO = "���o�͐ݒ�\��";
    private static final String QUIT           = "�X�g���[������I�� �� ���V�X�e��";
    
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
            // ���ݑI������Ă��� StreamPerformer �̏������J�n����D
            StreamPerformer pfmr = ConsoleMenu.getSelectedStream();
            pfmr.start();
        }
        catch (StreamException ex) {
            warningPause("���o�͏������J�n�ł��܂���(%s)�D\n", ex.getMessage());
        }
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu stopStream()
    {
        try {
            // ���ݑI������Ă��� StreamPerformer �̏������~����D
            StreamPerformer pfmr = ConsoleMenu.getSelectedStream();
            pfmr.stop();
        }
        catch (StreamException ex) {
            warningPause("���o�͏������~�ł��܂���(%s)�D\n", ex.getMessage());
        }
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu deleteStream()
    {
        // ���ݑI������Ă��� StreamPerformer ���폜����D
        StreamPerformer pfmr = ConsoleMenu.getSelectedStream();
        pfmr.delete();
        
        // ���̏�ԂƂȂ� SystemMenu ��Ԃ��D
        return this.systemMenu;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu showStreamIo()
    {
        // ���ݑI������Ă��� StreamPerformer �� SegmentIo �̓��o�͏���\������D
        StreamPerformer pfmr  = ConsoleMenu.getSelectedStream();
        SegmentIo       segIo = pfmr.getSegmentIo();
        printSegmentIo(segIo);
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu quit()
    {
        // ���̏�ԂƂȂ� SystemMenu ��Ԃ��D
        return this.systemMenu;
    }
}