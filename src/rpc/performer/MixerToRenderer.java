
package rpc.performer;

import com.midfield_system.api.util.Log;
import com.midfield_system.json_rpc.client.MfsRemote;
import com.midfield_system.json_rpc.client.RemoteControlException;
import com.midfield_system.json_rpc.client.SegmentIo;
import com.midfield_system.json_rpc.client.StreamPerformer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class MixerToRenderer
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsRemote       mfsRmt     = null;
        StreamPerformer mixer      = null;
        StreamPerformer mixerInput = null;
        
        try {
            // コマンドライン引数からサーバのIPアドレスとポート番号を取得する．
            var serverAddr = args[0];
            int serverPort = Integer.parseInt(args[1]);
            var mixerName  = args[2];
            
            // ▼サーバと接続し，遠隔操作を開始する．
            mfsRmt = new MfsRemote(serverAddr, serverPort, err -> System.err.println(err));
            mfsRmt.initializeRemoteControl();
            
            // ▼ミキサーを構成し，処理を開始する．
            // ・ミキサーのフォーマットには，システムプロパティの設定値を適用する．
            // ・出力はレンダラ．
            var mixerIo = mfsRmt.newSegmentIo();
            mixerIo.configureStreamingMixer(mixerName);
            mixerIo.configureRenderer();
            
            mixer = mfsRmt.newStreamPerformer(mixerIo);
            mixer.start();
            
            // ▼ミキサー入力（ビデオカメラとマイク）を構成し，処理を開始する．
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
            SegmentIo mixInIo = mfsRmt.newSegmentIo();
            mixInIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            mixInIo.configureMixerInput(mixer.getPerformerId());
            mixerInput = mfsRmt.newStreamPerformer(mixInIo);
            mixerInput.start();
            
            // ▼入出力処理を終了する．
            System.out.printf("> Enter キーの入力を待ちます．");
            System.in.read();
            
            // ■■■■とりあえずこのままにしておいて，
            // 例外の伝え方を改良する．■■■■
            mixerInput.stop();
            mixer.stop();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ▼ミキサー入力とミキサーの全ての処理を終了する．
            if (mixerInput != null) {
                mixerInput.delete();
            }
            if (mixer != null) {
                mixer.delete();
            }
            // ▼サーバと切断し，遠隔操作を終了する．
            if (mfsRmt != null) {
                mfsRmt.shutdownRemoteControl();
            }
        }
    }
}