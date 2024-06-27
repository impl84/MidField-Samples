
package rpc.performer;

import com.midfield_system.api.log.Log;
import com.midfield_system.json_rpc.client.MfsRemote;
import com.midfield_system.json_rpc.client.RemoteControlException;
import com.midfield_system.json_rpc.client.StreamPerformer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class DeviceToRenderer
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsRemote       mfsRmt = null;
        StreamPerformer pfmr   = null;
        
        try {
            // コマンドライン引数からサーバのIPアドレスとポート番号を取得する．
            var serverAddr = args[0];
            int serverPort = Integer.parseInt(args[1]);
            
            // ▼サーバと接続し，遠隔操作を開始する．
            mfsRmt = new MfsRemote(
                serverAddr, serverPort, err -> System.err.println(err)
            );
            mfsRmt.initializeRemoteControl();
            
            // ▼ビデオとオーディオデバイスで入力を構成する．
            // ・入力デバイスを選択して設定する．
            var devInfMgr = mfsRmt.getDeviceInfoManager();
            
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
            var segIo = mfsRmt.newSegmentIo();
            segIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            
            // ▼推奨レンダラで出力を構成する．
            segIo.configurePreferredRenderer();
            
            // ▼RcStreamPerformer を生成する．
            pfmr = mfsRmt.newStreamPerformer(segIo);
            
            // ▼RcStreamPerformer の入出力処理を開始する．
            pfmr.start();
            
            // ▼RcStreamPerformer の入出力処理を終了する．
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
            
            pfmr.stop();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ▼RcStreamPerformer の全ての処理を終了する．
            if (pfmr != null) {
                pfmr.delete();
            }
            // ▼サーバと切断し，遠隔操作を終了する．
            if (mfsRmt != null) {
                mfsRmt.shutdownRemoteControl();
            }
        }
    }
}
