
package app.performer;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.midfield.MfsApplication;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.13
 *
 */
public class DeviceToNetwork
{
    public static void main(String[] args)
    {
        StreamPerformer pfmr = null;
        
        try {
            // ▼MidField System のアプリケーションを起動する．
            var mfsApp = MfsApplication.launch();
            
            // ▼ビデオとオーディオデバイスで入力を構成する．
            // ・入力デバイスを選択して設定する．
            var devInfMgr = DeviceInfoManager.getInstance();
            
            var lsVidDev = devInfMgr.getVideoInputDeviceInfoList();
            if (lsVidDev.size() <= 0) {
                System.out.println("※利用可能なビデオ入力デバイスがありません．");
                return;
            }
            var lsAudDev = devInfMgr.getAudioInputDeviceInfoList();
            if (lsAudDev.size() <= 0) {
                System.out.println("※利用可能なオーディオ入力デバイスがありません．");
                return;
            }
            var segIo = new SegmentIo();
            segIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            
            // ▼送信ストリームで出力を構成する．
            // ・送信フォーマットを選択して設定する．
            // ・TCPを利用し，コネクション接続要求を受け入れる．
            // ・推奨プレビューワ—を利用する．
            var lsVidFmt = segIo.getOutputVideoFormatList();
            if (lsVidFmt.size() <= 0) {
                System.out.println("※送信可能なビデオフォーマットがありません．");
                return;
            }
            var lsAudFmt = segIo.getOutputAudioFormatList();
            if (lsAudFmt.size() <= 0) {
                System.out.println("※送信可能なオーディオフォーマットがありません．");
                return;
            }
            segIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
            segIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
            segIo.setPreferredPreviewer();
            
            //▼StreamPerformer を生成し，UIへ追加する．
            pfmr = StreamPerformer.newInstance(segIo);
            mfsApp.addStreamPerformer(pfmr);
            
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
        }
    }
}