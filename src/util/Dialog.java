
package util;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Constants;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: Dialog
 *
 * Date Modified: 2021.10.26
 *
 */
public class Dialog
{
    // - PACKAGE CONSTANT VALUE ------------------------------------------------
    static final String STR_TITLE_MESSAGE = "Message";
    static final String STR_TITLE_WARNING = "Warning";
    static final String STR_TITLE_ERROR   = "Error";
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public static void message(Component parent, String msg)
    {
        popup(parent, STR_TITLE_MESSAGE, msg);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public static void warning(Component parent, Throwable throwable)
    {
        String msg = SystemException.getMessageWithCause(throwable);
        warning(parent, msg);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public static void warning(Component parent, String msg)
    {
        popup(parent, STR_TITLE_WARNING, msg);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public static void error(Component parent, Throwable throwable)
    {
        String msg = SystemException.getMessageWithCause(throwable);
        error(parent, msg);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public static void error(Component parent, String msg)
    {
        popup(parent, STR_TITLE_ERROR, msg);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private static void popup(Component parent, String title, String msg)
    {
        // ダイアログのタイプを決める．
        int type = switch (title) {
        case STR_TITLE_MESSAGE -> JOptionPane.INFORMATION_MESSAGE;
        case STR_TITLE_WARNING -> JOptionPane.WARNING_MESSAGE;
        case STR_TITLE_ERROR -> JOptionPane.ERROR_MESSAGE;
        default -> JOptionPane.PLAIN_MESSAGE;
        };
        // ダイアログを表示する．
        if (SwingUtilities.isEventDispatchThread()) {
            showDialog(parent, title, type, msg);
        }
        else {
            SwingUtilities.invokeLater(() -> showDialog(parent, title, type, msg));
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private static void showDialog(
        Component parent, String title, int type, String msg
    )
    {
        Object[] options = {Constants.STR_OK};
        JOptionPane.showOptionDialog(
            parent,
            msg,
            title,
            JOptionPane.DEFAULT_OPTION,
            type,
            null,
            options,
            options[0]
        );
    }
}
