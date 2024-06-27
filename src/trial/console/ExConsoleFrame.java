
package trial.console;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.midfield_system.api.log.LogPrinter;
import com.midfield_system.api.system.event.ResourceStatusEvent;
import com.midfield_system.api.system.event.ScreenSaverStatusEvent;
import com.midfield_system.api.system.event.SystemEvent;
import com.midfield_system.api.system.event.SystemEventListener;
import com.midfield_system.ui.misc.MessagePanel;

import util.AppUtilities;
import util.InputField;
import util.LineReader;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.08.23
 *
 */
@SuppressWarnings("serial")
public class ExConsoleFrame
    extends
        JFrame
    implements
        SystemEventListener,
        WindowListener
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final Dimension DIM_MIN_FRAME  = new Dimension(800, 480);
    private static final Dimension DIM_PREF_FRAME = new Dimension(1080, 640);
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private MessagePanel mpConsoleOut = null;
    private InputField   ifConsoleIn  = null;
    
    private MessagePanel mpSystemLog      = null;
    private MessagePanel mpSystemEvent    = null;
    private MessagePanel mpResourceStatus = null;
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPLEMENTS: SystemEventListener
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: SystemEventListener
    //
    @Override
    public void update(SystemEvent ev)
    {
        // MidField System 内部で発生したイベントの情報を GUI 上に表示する．
        // このメソッドは MidField System 内部で利用しているスレッドから
        // 呼ばれているので，GUI 上への表示に係る処理は EDT に委譲する．
        SwingUtilities.invokeLater(() -> updateOnEdt(ev));
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPLEMENTS: WindowListener
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowActivated(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowClosed(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowClosing(WindowEvent ev)
    {
        if (this.ifConsoleIn != null) {
            this.ifConsoleIn.putString("9");
            this.ifConsoleIn.putString("9");
        }
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowDeactivated(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowDeiconified(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowIconified(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowOpened(WindowEvent ev)
    {}
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    ExConsoleFrame()
    {
        setupGui();
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    LineReader getLineReader()
    {
        LineReader reader = this.ifConsoleIn;
        return reader;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    LogPrinter getConsolePrinter()
    {
        LogPrinter printer = this.mpConsoleOut;
        return printer;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    LogPrinter getSystemLogPrinter()
    {
        LogPrinter printer = this.mpSystemLog;
        return printer;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void putCommandSequence(String commands)
    {
        int len = commands.length();
        for (int i = 0; i < len; i++) {
            String command = Character.toString(commands.charAt(i));
            this.ifConsoleIn.putString(command);
        }
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    void setupGui()
    {
        // このフレーム全体の Look and Feel を設定する．
        AppUtilities.setLookAndFeel(this);
        
        // JSplitPane 左右のパネルを生成する．
        JComponent pnlLeft  = createLeftPane();
        JComponent pnlRight = createRightPane();
        
        // JSplitPane を生成し，ContentPane に配置する．
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight
        );
        Container  container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(splitPane, BorderLayout.CENTER);
        
        // このフレームに関する各種初期設定をする．
        setTitle("Experimental Console for MidField System");
        setPreferredSize(DIM_PREF_FRAME);
        setMinimumSize(DIM_MIN_FRAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        pack();
        
        // JSplitPane のディバイダを真中に設定する．
        splitPane.setDividerLocation(0.5);
        
        // このフレームをスクリーンの中央に表示する．
        AppUtilities.setLocationToCenter(this);
        setVisible(true);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private JComponent createLeftPane()
    {
        // コンソール出力用の MessagePanel を生成する．
        this.mpConsoleOut = new MessagePanel();
        this.mpConsoleOut.setBorder(new TitledBorder("Experimental Console"));
        
        // コンソール入力用のフィールドを生成し，
        // MessagePanel の下部に配置する．
        this.ifConsoleIn = new InputField();
        this.mpConsoleOut.add(this.ifConsoleIn.getComponent(), BorderLayout.SOUTH);
        
        // コンソール出力用の MessagePanel を返す．
        return this.mpConsoleOut;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private JComponent createRightPane()
    {
        // ログ出力用の MessagePanel を生成する．
        this.mpSystemLog = new MessagePanel();
        this.mpSystemLog.setBorder(new TitledBorder("MidField System Log"));
        
        // SystemEvent 表示用の MessagePanel を生成する．
        this.mpSystemEvent = new MessagePanel();
        this.mpSystemEvent.setBorder(new TitledBorder("System Event"));
        
        // 資源利用状況を表示する MessagePanel を生成する．
        this.mpResourceStatus = new MessagePanel();
        this.mpResourceStatus.setBorder(new TitledBorder("Resource Status"));
        
        // 各コンポーネントを配置する．
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(this.mpSystemLog);
        panel.add(this.mpSystemEvent);
        panel.add(this.mpResourceStatus);
        
        // 各コンポーネントが配置された JPanel を返す．
        return panel;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void updateOnEdt(SystemEvent ev)
    {
        if (ev instanceof ResourceStatusEvent) {
            this.mpResourceStatus.println(ev);
        }
        else if (ev instanceof ScreenSaverStatusEvent) {
            ;
        }
        else {
            this.mpSystemEvent.println(ev);
        }
    }
}