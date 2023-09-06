
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
        // �R���\�[������̕������͂����� ConsoleReader �̃C���X�^���X���擾����D
        ConsoleReader reader = ConsoleReader.getInstance();
        
        StreamPerformer mixer      = null;
        StreamPerformer mixerInput = null;
        
        try {
            // MidField ���N������D
            MidField.launch(args);
            
            // �~�L�T�[���\������F
            // �~�L�T�[�̃t�H�[�}�b�g�ɂ́C�V�X�e���v���p�e�B�̐ݒ�l��K�p���C
            // �o�͂̓f�t�H���g�����_���Ƃ��č\������D
            SegmentIo mixIo = new SegmentIo();
            mixIo.configureStreamingMixer(MIXER_DESCRIPTION);
            mixIo.configureDefaultRenderer();
            
            // StreamPerformer �𐶐����C�R���\�[���֒ǉ�����D
            mixer = StreamPerformer.newInstanceOnConsole(mixIo);
            
            // �~�L�T�[�̏������J�n����D
            mixer.open();
            mixer.start();
            
            // �~�L�T�[�̓��́i�r�f�I�J�����ƃ}�C�N�j���\������F
            DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
            
            DeviceInfo vidDev = devInfMgr.getInputVideoDeviceInfoList().get(0);
            DeviceInfo audDev = devInfMgr.getInputAudioDeviceInfoList().get(0);
            
            SegmentIo mixInIo = new SegmentIo();
            mixInIo.configureInputDevice(vidDev, audDev);
            mixInIo.configureMixerInput(MIXER_DESCRIPTION);
            
            // StreamPerformer �𐶐����C�R���\�[���֒ǉ�����D
            mixerInput = StreamPerformer.newInstanceOnConsole(mixInIo);
            
            // �~�L�T�[�̓��͏������J�n����D
            mixerInput.open();
            mixerInput.start();
            
            // Enter�L�[�̓��͂�҂D
            System.out.printf("> Enter �L�[�̓��͂�҂��܂��D");
            reader.readLine();
            
            // ���o�͏������I������D
            mixerInput.stop();
            mixerInput.close();
            
            mixer.stop();
            mixer.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer �̏������I������D
            if (mixerInput != null) {
                mixerInput.delete();
            }
            if (mixer != null) {
                mixer.delete();
            }
            // ConsoleReader ���������D
            reader.release();
        }
    }
}