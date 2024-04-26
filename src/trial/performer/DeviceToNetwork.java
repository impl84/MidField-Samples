
package trial.performer;

import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;

import util.ConsolePrinter;
import util.SimpleViewer;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.08.28
 *
 */
public class DeviceToNetwork
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // ▼MidField System を初期化し，起動する．
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // ▼ビデオとオーディオデバイスで入力を構成する．
            // ・入力デバイスを選択して設定する．
            DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
            
            List<DeviceInfo> lsVidDev = devInfMgr.getVideoInputDeviceInfoList();
            if (lsVidDev.size() <= 0) {
                System.out.println("※利用可能なビデオ入力デバイスがありません．");
                return;
            }
            List<DeviceInfo> lsAudDev = devInfMgr.getAudioInputDeviceInfoList();
            if (lsAudDev.size() <= 0) {
                System.out.println("※利用可能なオーディオ入力デバイスがありません．");
                return;
            }
            SegmentIo segIo = new SegmentIo();
            segIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            
            // ▼送信ストリームで出力を構成する．
            // ・送信フォーマットを選択して設定する．
            // ・TCPを利用し，コネクション接続要求を受け入れる．
            // ・プレビューワ—を利用する．
            List<StreamFormat> lsVidFmt = segIo.getOutputVideoFormatList();
            if (lsVidFmt.size() <= 0) {
                System.out.println("※送信可能なビデオフォーマットがありません．");
                return;
            }
            List<StreamFormat> lsAudFmt = segIo.getOutputAudioFormatList();
            if (lsAudFmt.size() <= 0) {
                System.out.println("※送信可能なオーディオフォーマットがありません．");
                return;
            }
            segIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
            segIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
            segIo.setPreviewer();
            
            // ▼StreamPerformer を生成し，その内部にある VideoCanvas を取り出す．
            //   その後，VideoCanvas を表示するための SimpleViewer を生成して表示する．
            pfmr = StreamPerformer.newInstance(segIo);
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Device to Network", vidCvs);
            });
            // ▼StreamPerformer の入出力処理を開始する．
            pfmr.open();
            pfmr.start();
            
            // ▼StreamPerformer の入出力処理を終了する．
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
            
            pfmr.stop();
            pfmr.close();
        }
        catch (MfsException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ▼StreamPerformer の全ての処理を終了する．
            if (pfmr != null) {
                pfmr.delete();
            }
            // ▼MidField System を終了する．
            if (mfs != null) {
                mfs.shutdown();
            }
        }
    }
}