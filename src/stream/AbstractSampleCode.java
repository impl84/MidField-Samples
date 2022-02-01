
package stream;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.viewer.VideoCanvas;

import util.SimpleViewer;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: AbstractSampleCode
 *
 * Date Modified: 2021.08.24
 *
 */
abstract class AbstractSampleCode
    implements
        WindowListener
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    
    // StreamPerformer
    private StreamPerformer pfmr = null;
    
    // ビデオを表示するためのビューワ
    private SimpleViewer viewer = null;
    
    // サンプルコードの動作状態(true:動作中，false:停止中)
    private boolean isRunning = false;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
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
        // SimpleViewer を閉じようとしている状態．
        // サンプルコードの処理も終了する．
        closeSampleCode();
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
    AbstractSampleCode()
    {
        //
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // サンプルコードの動作状態(true:動作中，false:停止中)を返す．
    //
    boolean isRunning()
    {
        return this.isRunning;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // サンプルコードの処理を開始する．
    //
    void open(ConfigTool cfgTool)
    {
        // 動作状態を確認する．
        if (this.isRunning) {
            return;
        }
        // Stream Performer を生成し，入出力処理を実行する．
        try {
            // SegmentIo を生成する．．
            SegmentIo segIo = new SegmentIo();
            
            // SegmentIo の入出力を構成する．．
            configureInput(cfgTool, segIo);		// IOException
            configureOutput(cfgTool, segIo);	// IOException
            
            // SegmentIo をもとに，StreamPerformer を生成する．
            this.pfmr = StreamPerformer.newInstance(segIo);
            // SystemException, StreamException
            
            // ビューワをセットアップする．
            SwingUtilities.invokeAndWait(() -> setupSimpleViewer());
            // InterruptedException, InvocationTargetException
            
            // 入出力処理を開始する．
            this.pfmr.open();		// StreamException
            this.pfmr.start();		// StreamException
            
            // 動作状態を true にする．
            this.isRunning = true;
        }
        catch (Exception ex) {
            // 例外発生時のメッセージを出力する．
            System.out.println("※サンプルコード実行時に例外が発生しました．");
            ex.printStackTrace();
            
            // StreamPerformer のインスタンスが生成されている場合は，
            // StreamPerformer を終了する．
            if (this.pfmr != null) {
                this.pfmr.delete();
                this.pfmr = null;
            }
        }
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // サンプルコードの処理を終了する．
    //
    void close()
    {
        // サンプルコードの処理を終了する．
        closeSampleCode();
        
        // SimpleViewer を閉じる．
        SwingUtilities.invokeLater(() -> this.viewer.dispose());
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // サンプルコードの概要説明を取得する．
    //
    abstract String getDescription();
    
    // - PACKAGE METHOD --------------------------------------------------------
    // SegmentIo の入力を構成する．
    //
    abstract void configureInput(ConfigTool cfgTool, SegmentIo segIo)
        throws IOException;
    
    // - PACKAGE METHOD --------------------------------------------------------
    // SegmentIo の出力を構成する．
    //
    abstract void configureOutput(ConfigTool cfgTool, SegmentIo segIo)
        throws IOException;
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    // ※EDT上で実行される．
    //
    private void setupSimpleViewer()
    {
        // SimpleViewer のインスタンスを生成する．
        this.viewer = new SimpleViewer(getDescription());
        
        // このインスタンスを SimpleViewer の WindowListener として登録する．
        this.viewer.addWindowListener(this);
        
        // StreamPerformer が映像表示に利用する VideoCanvas を取得し，
        // SimpleViewer ビューワに追加する．
        VideoCanvas vidCvs = this.pfmr.getVideoCanvas();
        this.viewer.addVideoCanvas(vidCvs);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    // サンプルコードの処理を終了する．
    //
    private synchronized void closeSampleCode()
    {
        // 動作状態を確認する．
        if (this.isRunning == false) {
            return;
        }
        try {
            // 入出力処理を終了する．
            this.pfmr.stop();	// StreamException
            this.pfmr.close();
        }
        catch (StreamException ex) {
            // 例外発生時のスタックトレースを出力する．
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer を終了する．
            if (this.pfmr != null) {
                this.pfmr.delete();
                this.pfmr = null;
            }
            // 動作状態を false にする．
            this.isRunning = false;
        }
    }
}
