
package application;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.midfield.MidField;

import util.ConsoleReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: MixerToRenderer
 * 
 * Date Modified: 2023.08.23
 *
 */
public class MixerToRenderer
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String MIXER_DESCRIPTION = "Experimental Mixer";
    
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
        
        StreamPerformer mixer      = null;
        StreamPerformer mixerInput = null;
        
        try {
            // MidField を起動する．
            MidField.launch(args);
            
            // ミキサーを構成する：
            // ミキサーのフォーマットには，システムプロパティの設定値を適用し，
            // 出力はデフォルトレンダラとして構成する．
            SegmentIo mixIo = new SegmentIo();
            mixIo.configureStreamingMixer(MIXER_DESCRIPTION);
            mixIo.configureDefaultRenderer();
            
            // StreamPerformer を生成し，コンソールへ追加する．
            mixer = StreamPerformer.newInstanceOnConsole(mixIo);
            
            // ミキサーの処理を開始する．
            mixer.open();
            mixer.start();
            
            // ミキサーの入力（ビデオカメラとマイク）を構成する：
            DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
            
            DeviceInfo vidDev = devInfMgr.getInputVideoDeviceInfoList().get(0);
            DeviceInfo audDev = devInfMgr.getInputAudioDeviceInfoList().get(0);
            
            SegmentIo mixInIo = new SegmentIo();
            mixInIo.configureInputDevice(vidDev, audDev);
            mixInIo.configureMixerInput(MIXER_DESCRIPTION);
            
            // StreamPerformer を生成し，コンソールへ追加する．
            mixerInput = StreamPerformer.newInstanceOnConsole(mixInIo);
            
            // ミキサーの入力処理を開始する．
            mixerInput.open();
            mixerInput.start();
            
            // Enterキーの入力を待つ．
            System.out.printf("> Enter キーの入力を待ちます．");
            reader.readLine();
            
            // 入出力処理を終了する．
            mixerInput.stop();
            mixerInput.close();
            
            mixer.stop();
            mixer.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer の処理を終了する．
            if (mixerInput != null) {
                mixerInput.delete();
            }
            if (mixer != null) {
                mixer.delete();
            }
            // ConsoleReader を解放する．
            reader.release();
        }
    }
}