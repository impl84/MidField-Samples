
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
 * Date Modified: 2023.09.15
 *
 */
public class MixerToNetwork
{
    public static void main(String[] args)
    {
        StreamPerformer mixer      = null;
        StreamPerformer mixerInput = null;
        
        try {
            // args[0] ����~�L�T�[�����擾����D
            var mixerName = args[0];
            
            // ��MidField System �̃A�v���P�[�V�������N������D
            var mfsApp = MfsApplication.launch();
            
            // ���~�L�T�[���\�����C�������J�n����D
            // �E�~�L�T�[�̃t�H�[�}�b�g�ɂ́C�V�X�e���v���p�e�B�̐ݒ�l��K�p����D
            // �E�o�͂͑��M�X�g���[���D
            var mixerIo = new SegmentIo();
            mixerIo.configureStreamingMixer(mixerName);
            
            var lsVidFmt = mixerIo.getOutputVideoFormatList();
            if (lsVidFmt.size() <= 0) {
                System.out.println("�����M�\�ȃr�f�I�t�H�[�}�b�g������܂���D");
                return;
            }
            var lsAudFmt = mixerIo.getOutputAudioFormatList();
            if (lsAudFmt.size() <= 0) {
                System.out.println("�����M�\�ȃI�[�f�B�I�t�H�[�}�b�g������܂���D");
                return;
            }
            mixerIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
            mixerIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
            mixerIo.setPreviewer();
            
            mixer = StreamPerformer.newInstance(mixerIo);
            mfsApp.addStreamPerformer(mixer);
            
            mixer.open();
            mixer.start();
            
            // ���~�L�T�[���́i�r�f�I�J�����ƃ}�C�N�j���\�����C�������J�n����D
            var devInfMgr = DeviceInfoManager.getInstance();
            
            var lsVidDev = devInfMgr.getVideoInputDeviceInfoList();
            if (lsVidDev.size() <= 0) {
                System.out.println("�����p�\�ȃr�f�I���̓f�o�C�X������܂���D");
                return;
            }
            var lsAudDev = devInfMgr.getAudioInputDeviceInfoList();
            if (lsAudDev.size() <= 0) {
                System.out.println("�����p�\�ȃI�[�f�B�I���̓f�o�C�X������܂���D");
                return;
            }
            var mixInIo = new SegmentIo();
            mixInIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            mixInIo.configureMixerInput(mixer.getObjectId());
            mixerInput = StreamPerformer.newInstance(mixInIo);
            mfsApp.addStreamPerformer(mixerInput);
            
            mixerInput.open();
            mixerInput.start();
            
            // �����o�͏������I������D
            System.out.printf("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
        }
        catch (MfsException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ���~�L�T�[���͂ƃ~�L�T�[�̑S�Ă̏������I������D
            if (mixerInput != null) {
                mixerInput.delete();
            }
            if (mixer != null) {
                mixer.delete();
            }
        }
    }
}