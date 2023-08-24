
package app.performer;

import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.midfield.MfsApplication;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.15
 *
 */
public class MixerToRenderer
{
    public static void main(String[] args)
    {
        StreamPerformer mixer      = null;
        StreamPerformer mixerInput = null;
        
        try {
            // args[0] からミキサー名を取得する．
            var mixerName = args[0];
            
            // ▼MidField System のアプリケーションを起動する．
            var mfsApp = MfsApplication.launch();
            
            // ▼ミキサーを構成し，処理を開始する．
            // ・ミキサーのフォーマットには，システムプロパティの設定値を適用する．
            // ・出力はレンダラ．
            var mixerIo = new SegmentIo();
            mixerIo.configureStreamingMixer(mixerName);
            mixerIo.configureRenderer();
            
            mixer = StreamPerformer.newInstance(mixerIo);
            mfsApp.addStreamPerformer(mixer);
            
            mixer.open();
            mixer.start();
            
            // ▼ミキサー入力（ビデオカメラとマイク）を構成し，処理を開始する．
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
            var mixInIo = new SegmentIo();
            mixInIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            mixInIo.configureMixerInput(mixer.getObjectId());
            mixerInput = StreamPerformer.newInstance(mixInIo);
            mfsApp.addStreamPerformer(mixerInput);
            
            mixerInput.open();
            mixerInput.start();
            
            // ▼入出力処理を終了する．
            System.out.printf("> Enter キーの入力を待ちます．");
            System.in.read();
        }
        catch (MfsException ex) {
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
        }
    }
}