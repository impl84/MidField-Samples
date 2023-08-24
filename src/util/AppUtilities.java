
package util;

import static com.midfield_system.ui.misc.GuiConstants.FONT_GUI_DEFAULT;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.midfield_system.ui.misc.PopupMessage;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.09.19
 *
 */
public class AppUtilities
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String LF_CLASS_NAME = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
    
    private static final String LF_KEY_DEFAULT_FONT = "defaultFont";
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void setLookAndFeel(Component cmp)
    {
        try {
            // L&F を設定する．
            UIManager.setLookAndFeel(LF_CLASS_NAME);
            // ClassNotFoundException, InstantiationException,
            // IllegalAccessException, UnsupportedLookAndFeelException
            
            // L&F で利用されるデフォルトフォントを設定する．
            UIDefaults uiDef = UIManager.getLookAndFeelDefaults();
            uiDef.put(LF_KEY_DEFAULT_FONT, FONT_GUI_DEFAULT);
            
            // Swing のコンポーネントツリーに設定を反映させる．
            SwingUtilities.updateComponentTreeUI(cmp);
            
        }
        catch (Exception ex) {
            PopupMessage.warning(cmp, ex);
        }
    }
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void setLocationToCenter(Frame frame)
    {
        Toolkit   toolkit = Toolkit.getDefaultToolkit();
        Dimension screen  = toolkit.getScreenSize();
        
        frame.setLocation(
            (screen.width - frame.getWidth()) / 2,
            (screen.height - frame.getHeight()) / 2
        );
    }
}
