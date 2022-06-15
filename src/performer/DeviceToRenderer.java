
package performer;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;

import util.ConsolePrinter;
import util.ConsoleReader;
import util.SimpleViewer;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: DeviceToRenderer
 * 
 * Date Modified: 2022.06.09
 *
 */
public class DeviceToRenderer
{
// =============================================================================
// CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void main(String[] args)
    {
        // コンソールからの文字入力を扱う ConsoleReader のインスタンスを取得する．
        ConsoleReader reader = ConsoleReader.getInstance();
        
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // MidField System を初期化し，起動する．
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // ビデオとオーディオの入力デバイス情報リストを取得し，
            // 利用する入力デバイスを選択する．
            // （ここでは最初の入力デバイスを選択する．）
            DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
            DeviceInfo        vidDev    = devInfMgr.getInputVideoDeviceInfoList().get(0);
            DeviceInfo        audDev    = devInfMgr.getInputAudioDeviceInfoList().get(0);
            
            // SegmentIo の入力を入力デバイスとして構成する．
            SegmentIo segIo = new SegmentIo();
            segIo.configureInputDevice(vidDev, audDev);
            
            // SegmentIo の出力をデフォルトレンダラとして構成する．
            segIo.configureDefaultRenderer();
            
            // ライブソースオプションを有効にする．
            segIo.setLiveSource(true);
            
            // SegmentIo をもとに StreamPerformer を生成する．
            pfmr = StreamPerformer.newInstance(segIo);
            
            // StreamPerformer から VideoCanvas を取得する．
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            
            // StreamPerformer から VideoCanvas を取得し，SimpleViewer に追加する．
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Device to Renderer", vidCvs);
            });
            // 入出力処理を開始する．
            pfmr.open();
            pfmr.start();
            
            // Enterキーの入力を待つ．
            System.out.printf("> Enter キーの入力を待ちます．");
            reader.readLine();
            
            // 入出力処理を終了する．
            pfmr.stop();
            pfmr.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer の処理を終了する．
            if (pfmr != null) {
                pfmr.delete();
            }
            // MidField System を終了する．
            if (mfs != null) {
                mfs.shutdown();
            }
            // ConsoleReader を解放する．
            reader.release();
        }
    }
}